package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentContainerDataBean extends ComponentDataBean {
	
	private Element childrenContainerElement;
	
	@Override
	protected ComponentContainerDataBean getDataBeanInstance() {
		
		return new ComponentContainerDataBean();
	}
	
	public Element getChildrenContainerElement() {
		return childrenContainerElement != null ? childrenContainerElement
		        : getElement();
	}
	
	public void setChildrenContainerElement(Element childrenContainerElement) {
		this.childrenContainerElement = childrenContainerElement;
	}
}