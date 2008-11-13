package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.ConstButtonType;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentButton;
import com.idega.xformsmanager.component.FormComponentButtonArea;
import com.idega.xformsmanager.component.FormComponentPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/13 20:09:40 $ by $Author: civilis $
 */
public class FormComponentButtonAreaImpl extends FormComponentContainerImpl
		implements ButtonArea, FormComponentButtonArea {

	public Button addButton(ConstButtonType buttonType, String nextSiblingId) {

		if (buttonType == null)
			throw new IllegalArgumentException("Button type not provided");

		return (Button) addComponent(buttonType.toString(), nextSiblingId);
	}

	@Override
	public void create() {
		super.create();
		getParentPage().setButtonAreaComponentId(getId());
	}

	@Override
	public void load() {
		super.load();
		getParentPage().setButtonAreaComponentId(getId());
	}

	@Override
	public void remove() {
		super.remove();
		getParentPage().setButtonAreaComponentId(null);
	}

	public void setPageSiblings(FormComponentPage previous,
			FormComponentPage next) {

		for (FormComponent child : getContainedComponents().values()) {

			if (ConstButtonType.PREVIOUS_PAGE_BUTTON.toString().equals(
					child.getType())
					|| ConstButtonType.NEXT_PAGE_BUTTON.toString().equals(
							child.getType())) {

				((FormComponentButton) child).setSiblingsAndParentPages(
						previous, next);
			}
		}
	}

	public void announceLastPage(String lastPageId) {

		for (FormComponent child : getContainedComponents().values()) {

			if (ConstButtonType.SUBMIT_FORM_BUTTON.toString().equals(
					child.getType())) {

				((FormComponentButton) child).setLastPageId(lastPageId);
			}
		}
	}
}