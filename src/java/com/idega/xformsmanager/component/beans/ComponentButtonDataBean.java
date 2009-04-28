package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentButtonDataBean extends ComponentDataBean {
	
	private Element toggle_element;
	
	public Element getToggleElement() {
		return toggle_element;
	}
	
	public void setToggleElement(Element toggle_element) {
		this.toggle_element = toggle_element;
	}
	
	@Override
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentButtonDataBean();
	}
}