package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/06 14:15:48 $ by $Author: civilis $
 */
public class ComponentButtonDataBean extends ComponentDataBean {

	private Element toggle_element;

	public Element getToggleElement() {
		return toggle_element;
	}

	public void setToggleElement(Element toggle_element) {
		this.toggle_element = toggle_element;
	}

	/*
	@Override
	public Object clone() {

		ComponentButtonDataBean clone = (ComponentButtonDataBean) super.clone();

		// try {
		// clone = (ComponentButtonDataBean)super.clone();
		//			
		// } catch (Exception e) {
		//			
		// clone = new ComponentButtonDataBean();
		// }

		if (toggle_element != null)
			clone.setToggleElement((Element) toggle_element.cloneNode(true));

		return clone;
	}
	*/

	@Override
	protected ComponentDataBean getDataBeanInstance() {

		return new ComponentButtonDataBean();
	}
}