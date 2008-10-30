package com.idega.xformsmanager.component;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.xformsmanager.business.component.PageThankYou;
import com.idega.xformsmanager.business.component.properties.PropertiesDocument;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/10/30 22:01:03 $ by $Author: civilis $
 */
public interface FormDocument extends FormComponentContainer {

	public abstract void setFormDocumentModified(boolean changed);

	public abstract boolean isFormDocumentModified();

	public abstract Document getComponentsXml(Locale locale);
	
//	public abstract Document getComponentsXforms();

	public abstract Long getFormId();

	public abstract Locale getDefaultLocale();

	public abstract FormComponentPage getFormConfirmationPage();

	public abstract PageThankYou getThxPage();

	public abstract void registerForLastPage(String register_page_id);

	public abstract String generateNewComponentId();

	public abstract Element getAutofillModelElement();

	public abstract Element getFormDataModelElement();

	public abstract Element getFormMainDataInstanceElement();

	public abstract Element getSubmissionElement();

	public abstract Element getSectionsVisualizationInstanceElement();

	public abstract PropertiesDocument getProperties();

	public abstract String getFormType();

	public abstract LocalizedStringBean getFormTitle();

	public abstract Document getXformsDocument();

	public abstract DMContext getContext();
}