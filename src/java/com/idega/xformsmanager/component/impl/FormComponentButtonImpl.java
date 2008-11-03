package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;
import com.idega.xformsmanager.component.FormComponentButton;
import com.idega.xformsmanager.component.FormComponentButtonArea;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesButton;
import com.idega.xformsmanager.manager.XFormsManagerButton;

/**
 * TODO: for control buttons (next, previous): when the previous or next page is
 * not present, don't use toggle (works), also don't use toggle for thank you
 * page and so. When the page appears before, or after, modify the toggle of the
 * control button!
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/03 17:36:07 $ by $Author: civilis $
 */
public class FormComponentButtonImpl extends FormComponentImpl implements
		Button, FormComponentButton {

	@Override
	public XFormsManagerButton getXFormsManager() {
		return getFormDocument().getContext().getXformsManagerFactory()
				.getXformsManagerButton();
	}

	// @Override
	// public void render() {
	// boolean load = this.load;
	// super.render();
	// FormComponentButtonArea buttonArea = (FormComponentButtonArea)parent;
	// if(!load)
	// setSiblingsAndParentPages(buttonArea.getPreviousPage(),
	// buttonArea.getNextPage());
	// ((FormComponentButtonArea)parent).setButtonMapping(getType(), getId());
	// }

	@Override
	public void create() {
		super.create();

		setSiblingsAndParentPages(getParent().getPreviousPage(), getParent()
				.getNextPage());
		((FormComponentButtonArea) parent).setButtonMapping(getType(), getId());
	}

	@Override
	public FormComponentButtonArea getParent() {
		return (FormComponentButtonArea) super.getParent();
	}

	@Override
	public void load() {
		super.load();
		((FormComponentButtonArea) parent).setButtonMapping(getType(), getId());
	}

	public void setSiblingsAndParentPages(FormComponentPage previous,
			FormComponentPage next) {
		getXFormsManager().renewButtonPageContextPages(this, previous, next);
	}

	public void setLastPageId(String pageId) {
		getXFormsManager().setLastPageToSubmitButton(this, pageId);
	}

	@Override
	public PropertiesButton getProperties() {

		if (properties == null) {
			ComponentPropertiesButton properties = new ComponentPropertiesButton();
			properties.setComponent(this);
			this.properties = properties;
		}

		return (PropertiesButton) properties;
	}

	@Override
	protected void setProperties() {

		super.setProperties();

		ComponentPropertiesButton properties = (ComponentPropertiesButton) getProperties();
		XFormsManagerButton xformsManager = getXFormsManager();

		properties.setReferAction(xformsManager.getReferAction(this));
	}

	@Override
	public void remove() {

		getProperties().setReferAction(null);
		super.remove();
	}

	/*
	 * @Override public void setReadonly(boolean readonly) {
	 * 
	 * // for submit button only - hide if readonly (form)
	 * 
	 * if(getXFormsManager().isSubmitButton(this))
	 * getXFormsManager().setReadonly(this, readonly); }
	 */
}