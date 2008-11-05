package com.idega.xformsmanager.component.impl;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.chiba.web.xml.xforms.validation.ErrorType;
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
 * @version $Revision: 1.10 $
 *
 * Last modified: $Date: 2008/11/05 19:42:43 $ by $Author: civilis $
 */
public class FormComponentImpl implements FormComponent, Component {
	
	private String componentId;
	private FormDocument formDocument;
	private ComponentDataBean xformsComponentDataBean;
//	private DMContext context;
	
	protected FormComponent nextSibling;
	protected String type;
	protected FormComponentContainer parent;
	protected PropertiesComponent properties;
//	protected boolean created = false;
//	protected boolean load = false;
	
//	public DMContext getContext() {
//		return context;
//	}
//
//	public void setContext(DMContext context) {
//		this.context = context;
//	}
	
	/*
	public void render() {
		
		if(load || !created) {
			
			XFormsManager xformsManager = getXFormsManager();
			
			if(load) {
				
				xformsManager.loadXFormsComponentFromDocument(this);
				
			} else {
				xformsManager.loadXFormsComponentByTypeFromComponentsXForm(this, type);
				xformsManager.addComponentToDocument(this);
			}
			
			setProperties();
			
			if(!load)
				changeBindNames();
			
			formDocument.setFormDocumentModified(true);
			tellAboutMe();
			
			if(FormComponentFactory.getInstance().isNormalFormElement(this)) {

				if(load) {
					
					getXFormsManager().loadConfirmationElement(this, null);
					
				} else {
					
					FormComponentPage confirmation_page = (FormComponentPage)formDocument.getConfirmationPage();
					
					if(confirmation_page != null) {
						getXFormsManager().loadConfirmationElement(this, confirmation_page);
					}
				}
			}
			
			created = true;
			load = false;
		}
	}
	*/
	
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
//		tellAboutMe();
		
		if(formDocument.getContext().getFormComponentFactory().isNormalFormElement(this)) {

			FormComponentPage confirmationPage = formDocument.getFormConfirmationPage();
			
			if(confirmationPage != null) {
				getXFormsManager().loadConfirmationElement(this, confirmationPage);
			}
		}
	}
	
	public void load() {
		
		XFormsManager xformsManager = getXFormsManager();
		
		xformsManager.loadComponentFromDocument(this);
		
		setProperties();
		
//		those two could be moved to container who's calling this
		getFormDocument().setFormDocumentModified(true);
//		tellAboutMe();
		
		if(getFormDocument().getContext().getFormComponentFactory().isNormalFormElement(this)) {

//			TODO: perhaps just lazyload
			getXFormsManager().loadConfirmationElement(this, null);
		}
//		created = true;
//		load = false;
	}
	
	public void addToConfirmationPage() {
		
		if(getFormDocument().getContext().getFormComponentFactory().isNormalFormElement(this)) {
			
			FormComponentPage confirmationPage = getFormDocument().getFormConfirmationPage();
			
			if(confirmationPage != null) {
				getXFormsManager().loadConfirmationElement(this, confirmationPage);
			}
		}
	}
	
	protected void setProperties() {
		
		ComponentProperties properties = (ComponentProperties)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainLabel(getXFormsManager().getLocalizedStrings(this));
		properties.setPlainRequired(getXFormsManager().isRequired(this));
		properties.setErrorsMessages(getXFormsManager().getErrorLabelLocalizedStrings(this));
		properties.setPlainAutofillKey(getXFormsManager().getAutofillKey(this));
		properties.setPlainHelpText(getXFormsManager().getHelpText(this));
		properties.setPlainVariable(getXFormsManager().getVariable(this));
//		properties.setPlainReadonly(getXFormsManager().isReadonly(this));
//		properties.setPlainValidationText(getXFormsManager().getValidationText(this));
	}
	
	protected void changeBindNames() {
		
		Bind bind = getComponentDataBean().getBind();
		
		if(!bind.getIsShared()) {

//			TODO: do the same with nodeset
			LocalizedStringBean localizedLabel = getProperties().getLabel();
			
			String defaultLocaleLabel = localizedLabel.getString(getFormDocument().getDefaultLocale());
			
			String newBindName = new StringBuffer(defaultLocaleLabel)
								.append('_')
								.append(getId())
								.toString();
			
			bind.rename(newBindName);
		}
	}
	
	/*
	protected void tellAboutMe() {

		String componentId = getId();
//		parent.tellComponentId(componentId);
		FormComponentContainer parent = getParent();
		List<String> id_list = parent.getContainedComponentsIds();

		for (int i = 0; i < id_list.size(); i++) {
			
			if(id_list.get(i).equals(componentId) && i != 0) {
				
				((FormComponent)parent.getContainedComponent(id_list.get(i-1))).setNextSibling(this);
				break;
			}
		}
	}
	*/
	
	public void setNextSibling(FormComponent component) {
		
		nextSibling = component;
	}
	
	public void setNextSiblingRerender(FormComponent nextSibling) {
		
		FormComponent formerNextSibling = this.nextSibling;
		this.nextSibling = nextSibling;
		
		if(nextSibling != null && formerNextSibling != null && !formerNextSibling.getId().equals(nextSibling.getId())) {

			getXFormsManager().moveComponent(this, nextSibling.getId());
			
		} else if(formerNextSibling == null && nextSibling != null) {
			
			getXFormsManager().moveComponent(this, nextSibling.getId());
			
		} else if(nextSibling == null && formerNextSibling != null) {
			
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
	
//	public void setLoad(boolean load) {
//		this.load = load;
//	}
	
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		
		return getHtmlManager().getHtmlRepresentation(this, locale);
	}
	
	public PropertiesComponent getProperties() {
		
		if(properties == null) {
			ComponentProperties properties = new ComponentProperties();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}

	public XFormsManager getXFormsManager() {
		
		return getFormDocument().getContext().getXformsManagerFactory().getXformsManager();
	}
	
	protected HtmlManager getHtmlManager() {
		
		return getFormDocument().getContext().getHtmlManagerFactory().getHtmlManager();
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
		
		return "\nComponent id: "+getId()+" component class: "+getClass();
	}
	public FormComponent getNextSibling() {
		return nextSibling;
	}
	public void setFormDocument(FormDocument formDocument) {
		this.formDocument = formDocument;
	}
	
	public FormDocument getFormDocument() {
		
		if(formDocument == null)
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
//		case VALIDATION:
//			getHtmlManager().clearHtmlComponents(this);
//		    getFormDocument().setFormDocumentModified(true);
//			break;
		
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

	public void setComponentDataBean(
			ComponentDataBean xformsComponentDataBean) {
		this.xformsComponentDataBean = xformsComponentDataBean;
	}
	
//	public void setReadonly(boolean readonly) {
//		getXFormsManager().setReadonly(this, readonly);
//	}
//	
//	public boolean isReadonly() {
//		return getXFormsManager().isReadonly(this);
//	}
	
//	public void setPdfForm(boolean generatePdf) {
//		getXFormsManager().setPdfForm(this, generatePdf);
//	}
//	
//	public boolean isPdfForm() {
//		return getXFormsManager().isPdfForm(this);
//	}
}