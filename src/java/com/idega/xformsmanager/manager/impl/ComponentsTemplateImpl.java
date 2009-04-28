package com.idega.xformsmanager.manager.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.context.DMContext;

/**
 * represents form-components.xhtml components template
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
@Service
@Scope("singleton")
public class ComponentsTemplateImpl {
	
	private DMContext dmContext;
	private FormManager formManager;
	
	public void loadComponentFromTemplate(FormComponent formComponent) {
		
		// in this phase, the component acts as a template component
		formComponent.setFormDocument(getFormManager()
		        .getFormDocumentTemplate());
		formComponent.loadFromTemplate();
	}
	
	DMContext getDmContext() {
		return dmContext;
	}
	
	public void setDmContext(DMContext dmContext) {
		this.dmContext = dmContext;
	}
	
	FormManager getFormManager() {
		return formManager;
	}
	
	public void setFormManager(FormManager formManager) {
		this.formManager = formManager;
	}
}