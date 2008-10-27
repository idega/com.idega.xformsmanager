package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.PageThankYou;
import com.idega.xformsmanager.business.component.properties.PropertiesThankYouPage;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesThankYouPage;
import com.idega.xformsmanager.manager.XFormsManagerThankYouPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:40 $ by $Author: civilis $
 */
public class FormComponentThankYouPageImpl extends FormComponentPageImpl implements PageThankYou {
	
	@Override
	public PropertiesThankYouPage getProperties() {
		
		if(properties == null) {
			ComponentPropertiesThankYouPage properties = new ComponentPropertiesThankYouPage();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesThankYouPage)properties;
	}
	
	@Override
	public XFormsManagerThankYouPage getXFormsManager() {
		return getFormDocument().getContext().getXformsManagerFactory().getXformsManagerThankYouPage();
	}
	
	@Override
	protected void setProperties() {
		super.setProperties();
		
		ComponentPropertiesThankYouPage properties = (ComponentPropertiesThankYouPage)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainText(getXFormsManager().getThankYouText(this));
	}
}