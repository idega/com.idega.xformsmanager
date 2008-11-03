package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 * 
 *          Last modified: $Date: 2008/11/03 15:48:46 $ by $Author: civilis $
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