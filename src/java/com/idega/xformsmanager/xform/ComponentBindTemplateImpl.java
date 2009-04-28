package com.idega.xformsmanager.xform;

import com.idega.xformsmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentBindTemplateImpl implements ComponentBind {
	
	private Bind bind;
	
	public ComponentBindTemplateImpl(Bind bind) {
		
		this.bind = bind;
	}
	
	public FormComponent getFormComponent() {
		throw new IllegalArgumentException(
		        "No form component for template bind");
	}
	
	public Bind getBind() {
		return bind;
	}
	
	public boolean exists() {
		return getBind() != null;
	}
	
	public void updateBindReference() {
	}
}