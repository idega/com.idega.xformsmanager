package com.idega.xformsmanager.xform;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentBindImpl implements ComponentBind {
	
	private FormComponent formComponent;
	private Bind bind;
	
	public ComponentBindImpl(FormComponent formComponent, Bind bind) {
		
		this.formComponent = formComponent;
		this.bind = bind;
	}
	
	public FormComponent getFormComponent() {
		return formComponent;
	}
	
	public Bind getBind() {
		return bind;
	}
	
	public boolean exists() {
		return getBind() != null;
	}
	
	public void updateBindReference() {
		
		if (exists()) {
			
			updateBindAttribute(getBind().getId());
		} else {
			
			Logger
			        .getLogger(getClass().getName())
			        .log(
			            Level.WARNING,
			            "called updateBindReference, but no bind exists - removing bind attribute. Is this expected? Use ComponentBindNoBindImpl for the case, when no bind exists");
			
			removeBindAttribute();
		}
	}
	
	private void updateBindAttribute(String newValue) {
		
		getFormComponent().getComponentDataBean().getElement().setAttribute(
		    FormManagerUtil.bind_att, newValue);
	}
	
	private void removeBindAttribute() {
		
		getFormComponent().getComponentDataBean().getElement().removeAttribute(
		    FormManagerUtil.bind_att);
	}
}