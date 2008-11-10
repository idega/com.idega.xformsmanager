package com.idega.xformsmanager.business;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/10 12:15:54 $ by $Author: anton $
 */
public interface Form {

	public abstract String getFormStorageType();

	public abstract String getFormType();

	public abstract Long getFormId();

	public abstract Form getFormParent();

	public abstract String getFormStorageIdentifier();

	public abstract Integer getVersion();

	public abstract Date getDateCreated();

	public abstract String getDisplayName();

	public abstract List<? extends Submission> getXformSubmissions();
}