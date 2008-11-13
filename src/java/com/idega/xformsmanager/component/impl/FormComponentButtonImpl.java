package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;
import com.idega.xformsmanager.component.FormComponentButton;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesButton;
import com.idega.xformsmanager.manager.XFormsManagerButton;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/13 20:10:24 $ by $Author: civilis $
 */
public class FormComponentButtonImpl extends FormComponentImpl implements
		Button, FormComponentButton {

	@Override
	public XFormsManagerButton getXFormsManager() {
		return getFormDocument().getContext().getXformsManagerFactory()
				.getXformsManagerButton();
	}

	@Override
	public void create() {
		super.create();

		setSiblingsAndParentPages(
				getParent().getParentPage().getPreviousPage(), getParent()
						.getParentPage().getNextPage());
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
}