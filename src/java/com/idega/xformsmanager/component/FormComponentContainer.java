package com.idega.xformsmanager.component;

import java.util.List;

import com.idega.xformsmanager.manager.XFormsManagerContainer;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $ Last modified: $Date: 2009/04/23 14:12:56 $ by $Author: civilis $
 */
public interface FormComponentContainer extends FormComponent {
	
	/**
	 * @param componentId
	 * @return component which is contained in this container
	 */
	public abstract FormComponent getContainedComponent(String componentId);
	
	/**
	 * add child component from the component type
	 * 
	 * @param componentType
	 * @param nextSiblingId
	 *            the sibling that should follow the inserted component. In other words, insert
	 *            before this sibling. If not specified, the component will be appended to the
	 *            children
	 * @return created component
	 */
	public abstract FormComponent addFormComponent(String componentType,
	        String nextSiblingId);
	
	public abstract List<String> getContainedComponentsIds();
	
	public abstract void componentsOrderChanged();
	
	/**
	 * unregisters component from the container children list. This should be called by the form
	 * component itself on remove() method
	 * 
	 * @param componentId
	 */
	public abstract void unregisterComponent(String componentId);
	
	public abstract FormComponentPage getParentPage();
	
	public abstract XFormsManagerContainer getXFormsManager();
}