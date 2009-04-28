package com.idega.xformsmanager.xform;

import com.idega.xformsmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentBindNoBindImpl implements ComponentBind {
	
	private FormComponent formComponent;
	
	public ComponentBindNoBindImpl(FormComponent formComponent) {
		
		this.formComponent = formComponent;
	}
	
	public FormComponent getFormComponent() {
		return formComponent;
	}
	
	public Bind getBind() {
		return null;
	}
	
	public boolean exists() {
		return false;
	}
	
	public void updateBindReference() {
	}
}