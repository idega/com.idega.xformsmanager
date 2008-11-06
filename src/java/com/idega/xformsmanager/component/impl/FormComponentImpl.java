package com.idega.xformsmanager.component.impl;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentContainer;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ComponentProperties;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.HtmlManager;
import com.idega.xformsmanager.manager.XFormsManager;
import com.idega.xformsmanager.xform.Bind;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.13 $
 * 
 *          Last modified: $Date: 2008/11/06 17:29:32 $ by $Author: civilis $
 */
public class FormComponentImpl implements FormComponent, Component {

	private String componentId;
	private FormDocument formDocument;
	private ComponentDataBean xformsComponentDataBean;

	protected FormComponent nextSibling;
	protected String type;
	protected FormComponentContainer parent;
	protected PropertiesComponent properties;

	public void loadFromTemplate() {

		XFormsManager xformsManager = getXFormsManager();
		xformsManager.loadComponentFromTemplate(this);
	}

	public void create() {

		XFormsManager xformsManager = getXFormsManager();

		xformsManager.addComponentToDocument(this);
		setProperties();
		changeBindNames();

		FormDocument formDocument = getFormDocument();
		formDocument.setFormDocumentModified(true);
		// tellAboutMe();

		if (formDocument.getContext().getFormComponentFactory()
				.isNormalFormElement(this)) {

			FormComponentPage confirmationPage = formDocument
					.getFormConfirmationPage();

			if (confirmationPage != null) {
				getXFormsManager().loadConfirmationElement(this,
						confirmationPage);
			}
		}
	}

	public void load() {

		XFormsManager xformsManager = getXFormsManager();

		xformsManager.loadComponentFromDocument(this);

		setProperties();

		// those two could be moved to container who's calling this
		getFormDocument().setFormDocumentModified(true);
		// tellAboutMe();

		if (getFormDocument().getContext().getFormComponentFactory()
				.isNormalFormElement(this)) {

			// TODO: perhaps just lazyload
			getXFormsManager().loadConfirmationElement(this, null);
		}
	}

	public void addToConfirmationPage() {

		if (getFormDocument().getContext().getFormComponentFactory()
				.isNormalFormElement(this)) {

			FormComponentPage confirmationPage = getFormDocument()
					.getFormConfirmationPage();

			if (confirmationPage != null) {
				getXFormsManager().loadConfirmationElement(this,
						confirmationPage);
			}
		}
	}

	protected void setProperties() {

		ComponentProperties properties = (ComponentProperties) getProperties();

		if (properties == null)
			return;

		properties.setPlainLabel(getXFormsManager().getLocalizedStrings(this));
		properties.setPlainRequired(getXFormsManager().isRequired(this));
		properties.setErrorsMessages(getXFormsManager()
				.getErrorLabelLocalizedStrings(this));
		properties.setPlainAutofillKey(getXFormsManager().getAutofillKey(this));
		properties.setPlainHelpText(getXFormsManager().getHelpText(this));
		properties.setPlainVariable(getXFormsManager().getVariable(this));
	}

	protected void changeBindNames() {

		Bind bind = getComponentDataBean().getBind();

		if (!bind.getIsShared()) {

			LocalizedStringBean localizedLabel = getProperties().getLabel();

			String defaultLocaleLabel = localizedLabel
					.getString(getFormDocument().getDefaultLocale());

			String newBindName = new StringBuffer(defaultLocaleLabel).append(
					'_').append(getId()).toString();

			bind.rename(newBindName);
		}
		
		getXFormsManager().bindsRenamed(this);
	}

	public void setNextSibling(FormComponent component) {

		nextSibling = component;
	}

	public void setNextSiblingRerender(FormComponent nextSibling) {

		FormComponent formerNextSibling = this.nextSibling;
		this.nextSibling = nextSibling;

		if (nextSibling != null && formerNextSibling != null
				&& !formerNextSibling.getId().equals(nextSibling.getId())) {

			getXFormsManager().moveComponent(this, nextSibling.getId());

		} else if (formerNextSibling == null && nextSibling != null) {

			getXFormsManager().moveComponent(this, nextSibling.getId());

		} else if (nextSibling == null && formerNextSibling != null) {

			getXFormsManager().moveComponent(this, null);
		}
	}

	public String getId() {

		return componentId;
	}

	public void setId(String id) {

		componentId = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Element getHtmlRepresentation(Locale locale) throws Exception {

		return getHtmlManager().getHtmlRepresentation(this, locale);
	}

	public PropertiesComponent getProperties() {

		if (properties == null) {
			ComponentProperties properties = new ComponentProperties();
			properties.setComponent(this);
			this.properties = properties;
		}

		return properties;
	}

	public XFormsManager getXFormsManager() {

		return getFormDocument().getContext().getXformsManagerFactory()
				.getXformsManager();
	}

	protected HtmlManager getHtmlManager() {

		return getFormDocument().getContext().getHtmlManagerFactory()
				.getHtmlManager();
	}

	public String getType() {
		return type;
	}

	public void remove() {

		getXFormsManager().removeComponentFromXFormsDocument(this);
		getFormDocument().setFormDocumentModified(true);
		getParent().unregisterComponent(getId());
	}

	public void setParent(FormComponentContainer parent) {
		this.parent = parent;
	}

	public FormComponentContainer getParent() {
		return parent;
	}

	@Override
	public String toString() {

		return "\nComponent id: " + getId() + " component class: " + getClass();
	}

	public FormComponent getNextSibling() {
		return nextSibling;
	}

	public void setFormDocument(FormDocument formDocument) {
		this.formDocument = formDocument;
	}

	public FormDocument getFormDocument() {

		if (formDocument == null)
			formDocument = getParent().getFormDocument();

		return formDocument;
	}

	public void update(ConstUpdateType what) {

		update(what, null);
	}

	public void update(ConstUpdateType what, Object prop) {

		getXFormsManager().update(this, what, prop);

		switch (what) {
		case LABEL:

			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			changeBindNames();
			break;

		case ERROR_MSG:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;

		case HELP_TEXT:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;

		case CONSTRAINT_REQUIRED:
			getFormDocument().setFormDocumentModified(true);
			break;

		case P3P_TYPE:
			break;

		case AUTOFILL_KEY:
			break;

		case VARIABLE_NAME:
			break;

		default:
			break;
		}
	}

	public ComponentDataBean getComponentDataBean() {
		return xformsComponentDataBean;
	}

	public void setComponentDataBean(ComponentDataBean xformsComponentDataBean) {
		this.xformsComponentDataBean = xformsComponentDataBean;
	}
}