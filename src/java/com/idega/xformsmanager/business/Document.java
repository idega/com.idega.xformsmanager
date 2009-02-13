package com.idega.xformsmanager.business;

import java.util.List;

import org.w3c.dom.Element;

import com.idega.xformsmanager.business.component.Container;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.business.component.properties.ParametersManager;
import com.idega.xformsmanager.business.component.properties.PropertiesDocument;
import com.idega.xformsmanager.business.ext.FormVariablesHandler;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $ Last modified: $Date: 2009/02/13 17:19:06 $ by $Author: donatas $
 */
public interface Document extends Container {
	
	/**
	 * Creates new form page.
	 * 
	 * @param nextSiblingPageId
	 *            - where new component should be placed. If provided, new component will be
	 *            inserted <b>before</b> component with nextSiblingPageId. If null is passed, page
	 *            will be appended after last non special page.
	 * @see getSpecialPages javadoc
	 * @return newly created form page
	 */
	public abstract Page addPage(String nextSiblingPageId);
	
	public abstract String getFormSourceCode() throws Exception;
	
	public abstract void setFormSourceCode(String new_source_code)
	        throws Exception;
	
	public abstract LocalizedStringBean getFormTitle();
	
	public abstract LocalizedStringBean getFormErrorMsg();
	
	public abstract Long getFormId();
	
	public abstract Integer getFormVersion();
	
	public abstract String getFormType();
	
	public abstract void setFormTitle(LocalizedStringBean form_name)
	        throws Exception;
	
	public abstract void setFormErrorMsg(LocalizedStringBean form_error)
	        throws Exception;
	
	/**
	 * using getContainedPagesIdList method get components id list, then use this list to change the
	 * order of components, and then call this method for changes to take an effect
	 */
	public abstract void rearrangeDocument();
	
	public abstract Page getPage(String page_id);
	
	/**
	 * special page reflects non usual step in the form - this is either some behavioral page (e.g.
	 * save form), thank you page and so on
	 * 
	 * @return
	 */
	public abstract List<Page> getSpecialPages();
	
	public abstract List<String> getContainedPagesIdList();
	
	public abstract void save() throws Exception;
	
	public abstract void saveAllVersions(Long parentId) throws Exception;
	
	/**
	 * stores document under the storeBasePath specified. This should be the relative path to the
	 * content repository root in use. E.g. if the root is /files then the base path could look like
	 * this: /bpm/forms/
	 * 
	 * @param storeBasePath
	 * @throws Exception
	 */
	public abstract void save(String storeBasePath) throws Exception;
	
	public abstract org.w3c.dom.Document getXformsDocument();
	
	// public abstract Page getConfirmationPage();
	
	public abstract Page addConfirmationPage();
	
	public abstract PropertiesDocument getProperties();
	
	public abstract ParametersManager getParametersManager();
	
	public abstract Element getSubmissionInstanceElement();
	
	public abstract FormVariablesHandler getFormVariablesHandler();
	
	public abstract void setReadonly(boolean readonly);
	
	public abstract void setPdfForm(boolean generatePdf);
	
	public abstract void setFormType(String formType);
	
	public abstract void populateSubmissionDataWithXML(
	        org.w3c.dom.Document submission, boolean clean);
}