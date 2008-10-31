package com.idega.xformsmanager.manager.impl;

import org.chiba.xml.dom.DOMUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.FormComponentType;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.impl.FormComponentFactory;
import com.idega.xformsmanager.manager.XFormsManagerPage;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
@FormComponentType(FormComponentType.page)
@Service
@Scope("singleton")
public class XFormsManagerPageImpl extends XFormsManagerContainerImpl implements XFormsManagerPage {

	@Override
	public void loadXFormsComponentFromDocument(FormComponent component) {
		
		super.loadXFormsComponentFromDocument(component);
		checkForSpecialTypes(component);
		
//		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
//		Element case_element = xformsComponentDataBean.getElement();
//		xformsComponentDataBean.setElement((Element)case_element.getElementsByTagName(FormManagerUtil.group_tag).item(0));
	}
	
	@Override
	public void addComponentToDocument(FormComponent component) {
		
		super.addComponentToDocument(component);
		
//		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
//		Element componentElement = xformsComponentDataBean.getElement();
//		
//		String componentId = componentElement.getAttribute(FormManagerUtil.id_att);
//		Element case_element = componentElement.getOwnerDocument().createElementNS(FormManagerUtil.idega_namespace, FormManagerUtil.idegans_case_tag);
		
		
//		String name = componentElement.getAttribute(FormManagerUtil.name_att);
//		if(name != null && name.length() != 0) {
//			
//			componentElement.removeAttribute(FormManagerUtil.name_att);
//			case_element.setAttribute(FormManagerUtil.name_att, name);
//		}
//		componentElement.getParentNode().replaceChild(case_element, componentElement);
//		componentElement.removeAttribute(FormManagerUtil.id_att);
//		case_element.setAttribute(FormManagerUtil.id_att, componentId);
//		case_element.appendChild(componentElement);
		
		checkForSpecialTypes(component);
		pageContextChanged(component);
	}
	
	protected void checkForSpecialTypes(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		String pageType = xformsComponentDataBean.getElement().getAttribute(FormManagerUtil.type_att);
		if(pageType != null && 
				pageType.equals(FormComponentFactory.confirmation_page_type) ||
				pageType.equals(FormComponentFactory.page_type_thx))
			component.setType(pageType);
	}
	
	/*
	@Override
	public void removeComponentFromXFormsDocument(FormComponent component) {
		
		removeComponentLocalization(component);
		removeComponentBindings(component);
		removeSectionVisualization(component);
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element element_to_remove = xformsComponentDataBean.getElement();
		element_to_remove.getParentNode().getParentNode().removeChild(element_to_remove.getParentNode());
	}
	*/
	
	private void removeSectionVisualization(FormComponent component) {
		
		Element section = FormManagerUtil.getElementByIdFromDocument(component.getFormDocument().getXformsDocument(), FormManagerUtil.head_tag, component.getId()+"_section");
		
		if(section != null)
			section.getParentNode().removeChild(section);
	}
	
	/*
	@Override
	public void moveComponent(FormComponent component, String nextSiblingId) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Document xform = component.getFormDocument().getXformsDocument();
		
		Element componentElement = xformsComponentDataBean.getElement();
		Element element_to_insert_before = null;

		if(nextSiblingId != null) {
			
			element_to_insert_before = FormManagerUtil.getElementByIdFromDocument(xform, FormManagerUtil.body_tag, nextSiblingId);
		} else {

			Element components_container = (Element)componentElement.getParentNode();
			element_to_insert_before = DOMUtil.getLastChildElement(components_container);
		}
		
		Element componentElement = (Element)((Element)componentElement.getParentNode()).insertBefore(componentElement, element_to_insert_before);
		
		xformsComponentDataBean.setElement(
				(Element)((Element)((Element)componentElement.getParentNode()).insertBefore(componentElement, element_to_insert_before))
				.getElementsByTagName(FormManagerUtil.group_tag).item(0)
		);
	}
	*/
	
//	@Override
//	protected Element getInsertBeforeComponentElement(FormComponent component_after_this) {
//		return (Element)component_after_this.getXformsComponentDataBean().getElement().getParentNode();
//	}
	
	public void pageContextChanged(FormComponent component) {
//		TODO: untested and unchanged after document manager update
		FormDocument formDocument = component.getFormDocument();
		
		if(!formDocument.getProperties().isStepsVisualizationUsed() || FormComponentFactory.page_type_thx.equals(component.getType()))
			return;
		
		Element instance = formDocument.getSectionsVisualizationInstanceElement();
		Element section = FormManagerUtil.getElementByIdFromDocument(component.getFormDocument().getXformsDocument(), FormManagerUtil.head_tag, component.getId()+"_section");
		
		if(section == null) {
			
			section = FormManagerUtil.getItemElementById(component.getFormDocument().getContext().getCacheManager().getComponentsTemplate(), FormManagerUtil.section_item);
			section = (Element)component.getFormDocument().getXformsDocument().importNode(section, true);
			section.setAttribute(FormManagerUtil.id_att, component.getId()+"_section");
			Element id_el = (Element)section.getElementsByTagName(FormManagerUtil.id_att).item(0);
			FormManagerUtil.setElementsTextNodeValue(id_el, component.getId());
		} else
			section.getParentNode().removeChild(section);

		int seq_idx = component.getParent().getContainedComponentsIds().indexOf(component.getId())+1;
		
		if(seq_idx == 1)
			section.setAttribute("selected", "true");
		else
			section.setAttribute("selected", "false");
		
		Element seq_el = (Element)section.getElementsByTagName("seq_index").item(0);
		FormManagerUtil.setElementsTextNodeValue(seq_el, String.valueOf(seq_idx));
		
		FormComponentPage page = (FormComponentPage)component;
		FormComponentPage next = page.getNextPage();
		FormComponentPage prev = page.getPreviousPage();
		
		boolean inserted = false;
		
		if(next != null)
			inserted = insertSectionIntoContext(component, false, next, instance, section);
		
		if(prev != null && !inserted)
			inserted = insertSectionIntoContext(component, true, prev, instance, section);
		
		if(!inserted)
			insertSectionIntoContext(component, false, null, instance, section);
	}
	
	private boolean insertSectionIntoContext(FormComponent component, boolean prev, FormComponentPage relevant_page, Element instance, Element section) {
		
		
		if(relevant_page != null) {
			Element relevant_section = FormManagerUtil.getElementByIdFromDocument(component.getFormDocument().getXformsDocument(), FormManagerUtil.head_tag, relevant_page.getId()+"_section");
			
			if(relevant_section == null)
				return false;
			
			if(!prev)
				relevant_section.getParentNode().insertBefore(section, relevant_section);
			else
				DOMUtil.insertAfter(section, relevant_section);
			
		} else {

			Element class_exp = (Element)instance.getElementsByTagName("class_exp").item(0);
			class_exp.getParentNode().insertBefore(section, class_exp);
		}
		
		return true;
	}
}