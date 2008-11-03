package com.idega.xformsmanager.component.impl;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.xformsmanager.business.component.PageThankYou;
import com.idega.xformsmanager.business.component.properties.PropertiesDocument;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentContainer;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.context.DMContext;
import com.idega.xformsmanager.manager.XFormsManagerContainer;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/03 12:57:37 $ by $Author: civilis $
 */
public class FormDocumentTemplateImpl implements FormDocument {

	private Document xformsDocument;
	private DMContext context;

	public Document getXformsDocument() {
		return xformsDocument;
	}

	public void setXformsDocument(Document xformsDocument) {
		this.xformsDocument = xformsDocument;
	}

	public DMContext getContext() {
		return context;
	}

	public void setContext(DMContext context) {
		this.context = context;
	}

	public String generateNewComponentId() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");
	}

	public Element getAutofillModelElement() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public Document getComponentsXml(Locale locale) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");
	}

	public Locale getDefaultLocale() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public FormComponentPage getFormConfirmationPage() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public Element getFormDataModelElement() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public Long getFormId() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public Element getFormMainDataInstanceElement() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public LocalizedStringBean getFormTitle() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public String getFormType() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public PropertiesDocument getProperties() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public Element getSectionsVisualizationInstanceElement() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public Element getSubmissionElement() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public PageThankYou getThxPage() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public boolean isFormDocumentModified() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");
	}

	public void registerForLastPage(String register_page_id) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setFormDocumentModified(boolean changed) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public FormComponent addFormComponent(String componentType,
			String nextSiblingId) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void componentsOrderChanged() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public FormComponent getContainedComponent(String componentId) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public List<String> getContainedComponentsIds() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public FormComponentPage getParentPage() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public XFormsManagerContainer getXFormsManager() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void unregisterComponent(String componentId) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void addToConfirmationPage() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void create() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public FormDocument getFormDocument() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public String getId() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public FormComponent getNextSibling() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public FormComponentContainer getParent() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public String getType() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public ComponentDataBean getComponentDataBean() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void load() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void loadFromTemplate() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void remove() {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setFormDocument(FormDocument form_document) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setId(String id) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setNextSibling(FormComponent component) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setNextSiblingRerender(FormComponent nextSibling) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setParent(FormComponentContainer parent) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setType(String type) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void setComponentDataBean(
			ComponentDataBean xformsComponentDataBean) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}

	public void update(ConstUpdateType what) {
		throw new UnsupportedOperationException(
				"Unsupported for template document");

	}
}