package com.idega.xformsmanager.manager.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentContainer;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.manager.XFormsManagerContainer;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:37 $ by $Author: civilis $
 */
public class XFormsManagerContainerImpl extends XFormsManagerImpl implements XFormsManagerContainer {
	
	public List<String[]> getContainedComponentsTagNamesAndIds(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(xformsComponentDataBean.getElement() == null)
			throw new NullPointerException("Document container element not set");
		
		@SuppressWarnings("unchecked")
		List<Element> components_elements = DOMUtil.getChildElements(xformsComponentDataBean.getElement());
		List<String[]> components_tag_names_and_ids = new ArrayList<String[]>();
		
		for (Iterator<Element> iter = components_elements.iterator(); iter.hasNext();) {
			
			Element component_element = iter.next();
			String[] tag_name_and_id = new String[2];
			tag_name_and_id[1] = component_element.getAttribute(FormManagerUtil.id_att);
			
			if(tag_name_and_id[1] == null || !tag_name_and_id[1].startsWith(FormManagerUtil.CTID))
				continue;
			
			tag_name_and_id[0] = component_element.getTagName();
			
			String name_val = component_element.getAttribute(FormManagerUtil.name_att);
			
			if(name_val != null && !name_val.equals(""))
				tag_name_and_id[0] = name_val;
			
			components_tag_names_and_ids.add(tag_name_and_id);
		}
		return components_tag_names_and_ids;
	}
	
	public void addChild(FormComponentContainer parent, FormComponent child) {
		
		Element componentElement = child.getXformsComponentDataBean().getElement();
		
		if(child.getNextSibling() == null) {
			
			Element parentElement = parent.getXformsComponentDataBean().getElement();
			componentElement = (Element)parentElement.appendChild(componentElement);
			
		} else {
			
			Element nextSiblingElement = child.getNextSibling().getXformsComponentDataBean().getElement();
			componentElement = (Element)nextSiblingElement.getParentNode().insertBefore(componentElement, nextSiblingElement);
		}
		
		child.getXformsComponentDataBean().setElement(componentElement);
	}
}