package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentDocumentDataBean extends ComponentDataBean {
	
	private Element autofillAction;
	private Element formDataModel;
	private Element formMainDataInstanceElement;
	private Element sectionsVisualizationInstance;
	
	public Element getAutofillAction() {
		return autofillAction;
	}
	
	public void setAutofillAction(Element autofillAction) {
		this.autofillAction = autofillAction;
	}
	
	public Element getFormDataModel() {
		return formDataModel;
	}
	
	public void setFormDataModel(Element formDataModel) {
		this.formDataModel = formDataModel;
	}
	
	public Element getSectionsVisualizationInstance() {
		return sectionsVisualizationInstance;
	}
	
	public void setSectionsVisualizationInstance(
	        Element sectionsVisualizationInstance) {
		this.sectionsVisualizationInstance = sectionsVisualizationInstance;
	}
	
	@Override
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentDocumentDataBean();
	}
	
	public Element getFormMainDataInstanceElement() {
		return formMainDataInstanceElement;
	}
	
	public void setFormMainDataInstanceElement(
	        Element formMainDataInstanceElement) {
		this.formMainDataInstanceElement = formMainDataInstanceElement;
	}
}
