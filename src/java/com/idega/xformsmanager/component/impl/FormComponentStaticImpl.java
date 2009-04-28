package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.ComponentStatic;
import com.idega.xformsmanager.business.component.properties.PropertiesStatic;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesStatic;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerPlain;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class FormComponentStaticImpl extends FormComponentImpl implements
        ComponentStatic {
	
	@Override
	protected void setProperties() {
		
		ComponentPropertiesStatic properties = (ComponentPropertiesStatic) getProperties();
		
		if (properties == null)
			return;
		
		properties.setText(getXFormsManager().getText(this));
	}
	
	@Override
	public PropertiesStatic getProperties() {
		
		if (properties == null) {
			ComponentPropertiesStatic properties = new ComponentPropertiesStatic();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesStatic) properties;
	}
	
	@Override
	public XFormsManagerPlain getXFormsManager() {
		
		return getFormDocument().getContext().getXformsManagerFactory()
		        .getXformsManagerPlain();
	}
	
	@Override
	public void update(ConstUpdateType what) {
		
		getXFormsManager().update(this, what, null);
		
		switch (what) {
			case LABEL:
				getHtmlManager().clearHtmlComponents(this);
				getFormDocument().setFormDocumentModified(true);
				break;
			case TEXT:
				getHtmlManager().clearHtmlComponents(this);
				getFormDocument().setFormDocumentModified(true);
				break;
			
			case AUTOFILL_KEY:
				break;
			
			default:
				break;
		}
	}
	
	@Override
	protected void changeBindNames() {
	}
}