package com.idega.xformsmanager.component;

import org.w3c.dom.Node;

import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.impl.FormComponentContainerImpl;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.context.Event;

/**
 * this interface represents "internal to the xform document manager" form component representation.
 * Shouldn't be used outside the xform document manager
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $ Last modified: $Date: 2009/04/23 14:12:39 $ by $Author: civilis $
 */
public interface FormComponent {
	
	/**
	 * loads xforms component from components template. This should be called BEFORE create().
	 */
	public abstract void loadFromTemplate();
	
	/**
	 * adds templated component to the xform document. This method should be called AFTER
	 * loadFromTemplate(). The sufficient information should be already added to this component
	 * (like parent container)
	 * 
	 * @see FormComponentContainerImpl addComponent method for example
	 */
	public abstract void create();
	
	/**
	 * loads xforms component from the xform document. The sufficient information should be already
	 * added to this component (like parent container)
	 * 
	 * @see FormComponentContainerImpl loadContainerComponents method for example
	 */
	public abstract void load();
	
	// public abstract void render();
	
	/**
	 * specifies the component, before which this component should go in the xforms document
	 * 
	 * @param component
	 *            sibling component
	 */
	public abstract void setNextSibling(FormComponent component);
	
	public abstract FormComponent getNextSibling();
	
	/**
	 * moves this component to new location before next sibling specified
	 * 
	 * @param nextSibling
	 */
	public abstract void setNextSiblingRerender(FormComponent nextSibling);
	
	public abstract String getId();
	
	public abstract void setId(String id);
	
	public abstract void setType(String type);
	
	public abstract String getType();
	
	public abstract PropertiesComponent getProperties();
	
	public abstract void remove();
	
	// public abstract void setLoad(boolean load);
	
	public abstract void setParent(FormComponentContainer parent);
	
	public abstract void setFormDocument(FormDocument form_document);
	
	/**
	 * performs adding component to the confirmation page
	 */
	public abstract void addToConfirmationPage();
	
	public abstract FormComponentContainer getParent();
	
	public abstract void update(ConstUpdateType what);
	
	public abstract void update(ConstUpdateType what, Object property);
	
	public abstract ComponentDataBean getComponentDataBean();
	
	public abstract void setComponentDataBean(
	        ComponentDataBean componentDataBean);
	
	public abstract FormDocument getFormDocument();
	
	public abstract void dispatchEvent(Event event, Object eventContext,
	        FormComponent dispatcher);
	
	public abstract Node appendChildNode(Node childNode);
	
	public abstract void mappingSiblingChanged(String... relevantMappings);
}