package com.idega.xformsmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentContainer;
import com.idega.xformsmanager.component.FormComponentType;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.manager.XFormsManagerContainer;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
@FormComponentType(FormComponentType.container)
@Service
@Scope("singleton")
public class XFormsManagerContainerImpl extends XFormsManagerImpl implements XFormsManagerContainer {
	
	public List<String[]> getContainedComponentsTypesAndIds(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(xformsComponentDataBean.getElement() == null)
			throw new NullPointerException("Document container element not set");
		
		return getContainedComponentsTypesAndIds(xformsComponentDataBean.getElement());
	}
	
	private List<String[]> getContainedComponentsTypesAndIds(Element fromElement) {
		
		@SuppressWarnings("unchecked")
		List<Element> componentElements = DOMUtil.getChildElements(fromElement);
		List<String[]> componentsTagNamesNIds = new ArrayList<String[]>();
		
		for (Element componentElement : componentElements) {
			
			String componentId = componentElement.getAttribute(FormManagerUtil.id_att);
			
			if(componentId == null || !componentId.startsWith(FormManagerUtil.CTID))
				continue;
			
			String componentType;
			String typeAtt = componentElement.getAttribute(FormManagerUtil.type_att);
			
			if(typeAtt != null && typeAtt.length() != 0)
				componentType = typeAtt;
			else
				componentType = componentElement.getTagName();
			
			componentsTagNamesNIds.add(new String[] {componentType, componentId});
		}
		
		if(componentsTagNamesNIds.isEmpty()) {
			
//			no components found in container element, try to look deeper (recursively)
			
			for (Element componentElement : componentElements) {
				componentsTagNamesNIds.addAll(getContainedComponentsTypesAndIds(componentElement));
			}
		}
		
		return componentsTagNamesNIds;
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