package com.idega.xformsmanager.xform;

import com.idega.xformsmanager.component.FormComponent;

/**
 * component - bind connection representation. multiple components could reference the same bind
 * (which pertains to document)
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public interface ComponentBind {
	
	public abstract FormComponent getFormComponent();
	
	public abstract Bind getBind();
	
	/**
	 * @return if the component - bind reference exist
	 */
	public abstract boolean exists();
	
	/**
	 * update the bind referencing info for component (update the bind attribute value)
	 */
	public abstract void updateBindReference();
}