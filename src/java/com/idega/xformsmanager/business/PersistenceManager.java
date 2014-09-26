package com.idega.xformsmanager.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.idega.xformsmanager.component.FormDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.9 $ Last modified: $Date: 2009/02/13 17:19:06 $ by $Author: donatas $
 */
public interface PersistenceManager {

	public abstract PersistedFormDocument loadForm(Long formId);

	public abstract PersistedFormDocument loadPopulatedForm(
	        String submissionUUID) throws InvalidSubmissionException;

	public abstract PersistedFormDocument loadPopulatedForm(
	        String submissionUUID, boolean pdfView) throws InvalidSubmissionException;

	public abstract PersistedFormDocument saveForm(FormDocument document)
	        throws IllegalAccessException;

	public abstract PersistedFormDocument saveAllVersions(FormDocument document, Long formId)
    	throws IllegalAccessException;

	/**
	 * stores document under the storeBasePath specified. This should be the relative path to the
	 * content repository root in use. E.g. if the root is /files then the base path could look like
	 * this: /bpm/forms/
	 *
	 * @param document
	 * @param storeBasePath
	 * @return
	 * @throws IllegalAccessException
	 */
	public abstract PersistedFormDocument saveForm(FormDocument document,
	        String storeBasePath) throws IllegalAccessException;

	public abstract PersistedFormDocument takeForm(Long formId);

	public abstract List<Form> getStandaloneForms();

	/**
	 * Stores submitted data
	 *
	 * @param formId
	 *            - not null
	 * @param is
	 *            - not null
	 * @param identifier
	 *            - could be null, some random identifier would be generated
	 * @return submission UUID
	 * @throws IOException
	 */
	public abstract String saveSubmittedData(Long formId, InputStream is, String identifier, boolean finalSubmission, Integer formSubmitter) throws Exception;

	/**
	 * @param submissionUUID
	 * @param formId
	 *            - not null
	 * @param is
	 *            - not null
	 * @param identifier
	 *            - could be null, would be generated some random identifier
	 * @return submission id
	 * @throws IOException
	 */
	public abstract String saveSubmittedDataByExistingSubmission(String submissionUUID, Long formId, InputStream is, String identifier, Integer formSubmitter) throws Exception;

	/**
	 * set submission to invalid, this is used for saved forms, and this means, that the saved form
	 * cannot be viewed by the submissionUUID. This usually means that the form is already submitted
	 *
	 * @param submissionUUID
	 * @throws IOException
	 */
	public abstract void invalidateSubmittedDataByExistingSubmission(
	        String submissionUUID);

	public abstract List<Submission> getAllStandaloneFormsSubmissions();

	public abstract List<Submission> getFormsSubmissions(long formId);

	public abstract Submission getSubmission(long submissionId);
}