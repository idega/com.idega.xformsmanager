package com.idega.xformsmanager.business;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 * 
 *          Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
public interface Form {

	public abstract String getFormStorageType();

	public abstract String getFormType();

	public abstract Long getFormId();

	public abstract Long getFormParent();

	public abstract String getFormStorageIdentifier();

	public abstract Integer getVersion();

	public abstract Date getDateCreated();

	public abstract String getDisplayName();

	public abstract List<? extends Submission> getXformSubmissions();
}