package com.idega.xformsmanager.component.impl;

import java.util.HashMap;
import java.util.Map;

import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.ConstButtonType;
import com.idega.xformsmanager.component.FormComponentButton;
import com.idega.xformsmanager.component.FormComponentButtonArea;
import com.idega.xformsmanager.component.FormComponentPage;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:40 $ by $Author: civilis $
 */
public class FormComponentButtonAreaImpl extends FormComponentContainerImpl implements ButtonArea, FormComponentButtonArea {

	protected Map<String, String> buttons_type_id_mapping;

	public Button getButton(ConstButtonType buttonType) {

		if(buttonType == null)
			throw new NullPointerException("Button type provided null");
		
		return !getButtonsTypeIdMapping().containsKey(buttonType) ? null : 
			(Button)getContainedComponent(getButtonsTypeIdMapping().get(buttonType));
	}
	
	public Button addButton(ConstButtonType buttonType, String nextSiblingId) {
		
		if(buttonType == null)
			throw new IllegalArgumentException("Button type not provided");
		
		if(getButtonsTypeIdMapping().containsKey(buttonType))
			throw new IllegalArgumentException("Button by type provided: "+buttonType+" already exists in the button area");
		
		return (Button)addComponent(buttonType.toString(), nextSiblingId);
	}
	
	protected Map<String, String> getButtonsTypeIdMapping() {
		
		if(buttons_type_id_mapping == null)
			buttons_type_id_mapping = new HashMap<String, String>();
		
		return buttons_type_id_mapping;
	}
	
//	@Override
//	public void render() {
//		super.render();
//		((FormComponentPage)parent).setButtonAreaComponentId(getId());
//	}
	
	@Override
	public void create() {
		super.create();
		getParent().setButtonAreaComponentId(getId());
	}
	
	@Override
	public void load() {
		super.load();
		getParent().setButtonAreaComponentId(getId());
	}
	
	@Override
	public FormComponentPage getParent() {
		return (FormComponentPage)super.getParent();
	}
	
	@Override
	public void remove() {
		super.remove();
		((FormComponentPage)parent).setButtonAreaComponentId(null);
	}
	public void setButtonMapping(String button_type, String button_id) {
		
		getButtonsTypeIdMapping().put(button_type, button_id);
	}
	public void setPageSiblings(FormComponentPage previous, FormComponentPage next) {
		
		FormComponentButton button = (FormComponentButton)getButton(ConstButtonType.PREVIOUS_PAGE_BUTTON);
		
		if(button != null)
			button.setSiblingsAndParentPages(previous, next);
		
		button = (FormComponentButton)getButton(ConstButtonType.NEXT_PAGE_BUTTON);
		
		if(button != null)
			button.setSiblingsAndParentPages(previous, next);
	}
	public FormComponentPage getPreviousPage() {
		return ((FormComponentPage)parent).getPreviousPage();
	}
	public FormComponentPage getNextPage() {
		return ((FormComponentPage)parent).getNextPage();
	}
	public FormComponentPage getCurrentPage() {
		return (FormComponentPage)parent;
	}
	
	public void announceLastPage(String last_page_id) {
		
		FormComponentButton submit_button = (FormComponentButton)getButton(ConstButtonType.SUBMIT_FORM_BUTTON);
		
		if(submit_button == null)
			return;
		
		submit_button.setLastPageId(last_page_id);
	}
}