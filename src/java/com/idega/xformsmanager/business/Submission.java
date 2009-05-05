package com.idega.xformsmanager.business;

import java.util.Date;

import org.w3c.dom.Document;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $ Last modified: $Date: 2009/05/05 14:06:04 $ by $Author: civilis $
 */
public interface Submission {
	
	public abstract Long getSubmissionId();
	
	public abstract String getSubmissionStorageIdentifier();
	
	public abstract String getSubmissionStorageType();
	
	public abstract Date getDateSubmitted();
	
	public abstract Form getXform();
	
	public abstract Boolean getIsFinalSubmission();
	
	public abstract Document getSubmissionDocument();
	
	public abstract String getSubmissionUUID();
}