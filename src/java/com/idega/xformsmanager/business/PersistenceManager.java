package com.idega.xformsmanager.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.idega.xformsmanager.business.Form;
import com.idega.xformsmanager.business.PersistedFormDocument;
import com.idega.xformsmanager.business.Submission;
import com.idega.xformsmanager.component.FormDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $ Last modified: $Date: 2009/01/19 21:49:13 $ by $Author: civilis $
 */
public interface PersistenceManager {
	
	public abstract PersistedFormDocument loadForm(Long formId);
	
	public abstract PersistedFormDocument loadPopulatedForm(String submissionUUID);
	
	public abstract PersistedFormDocument saveForm(FormDocument document)
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
	public abstract String saveSubmittedData(Long formId, InputStream is,
	        String identifier, boolean finalSubmission) throws IOException;
	
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
	public abstract String saveSubmittedDataByExistingSubmission(
	        String submissionUUID, Long formId, InputStream is, String identifier)
	        throws IOException;
	
	public abstract List<Submission> getAllStandaloneFormsSubmissions();
	
	public abstract List<Submission> getFormsSubmissions(long formId);
	
	public abstract Submission getSubmission(long submissionId);
}