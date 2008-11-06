package com.idega.xformsmanager.component.beans;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Element;

import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.xform.Bind;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 * 
 *          Last modified: $Date: 2008/11/06 14:15:56 $ by $Author: civilis $
 */
public class ComponentDataBean implements Cloneable {

	private Element element;
	private Bind bind;
	private Element previewElement;
	private Element keyExtInstance;
	private Element keySetvalue;

	private Map<Locale, Element> localizedHtmlComponents;

	public Element getPreviewElement() {
		return previewElement;
	}

	public void setPreviewElement(Element preview_element) {
		this.previewElement = preview_element;
	}

	public Bind getBind() {
		return bind;
	}

	/**
	 * sets bind only, also @see putBind
	 * @param bind
	 */
	public void setBind(Bind bind) {
		this.bind = bind;
	}
	
	/**
	 * sets bind and updates component element to reference the bind
	 * @param bind
	 */
	public void putBind(Bind bind) {

		bind.getFormComponent().getComponentDataBean().getElement()
				.setAttribute(FormManagerUtil.bind_att, bind.getId());
		this.bind = bind;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

//	@Override
//	public Object clone() {
//
//		ComponentDataBean clone = getDataBeanInstance();
//
//		if (getElement() != null)
//			clone.setElement((Element) getElement().cloneNode(true));
//
//		if (getBind() != null)
//			clone.setBind(getBind().clone());
//
//		if (getPreviewElement() != null)
//			clone.setPreviewElement((Element) getPreviewElement().cloneNode(
//					true));
//
//		if (getKeyExtInstance() != null)
//			clone.setKeyExtInstance((Element) getKeyExtInstance().cloneNode(
//					true));
//
//		if (getKeySetvalue() != null)
//			clone.setKeySetvalue((Element) getKeySetvalue().cloneNode(true));
//
//		return clone;
//	}

	protected ComponentDataBean getDataBeanInstance() {

		return new ComponentDataBean();
	}

	public Element getKeyExtInstance() {
		return keyExtInstance;
	}

	public void setKeyExtInstance(Element key_ext_instance) {
		this.keyExtInstance = key_ext_instance;
	}

	public Element getKeySetvalue() {
		return keySetvalue;
	}

	public void setKeySetvalue(Element key_setvalue) {
		this.keySetvalue = key_setvalue;
	}

	public Map<Locale, Element> getLocalizedHtmlComponents() {

		if (localizedHtmlComponents == null)
			localizedHtmlComponents = new HashMap<Locale, Element>();

		return localizedHtmlComponents;
	}

	public void setLocalizedHtmlComponents(
			Map<Locale, Element> localizedHtmlComponents) {
		this.localizedHtmlComponents = localizedHtmlComponents;
	}
}