package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.ComponentPlain;
import com.idega.xformsmanager.business.component.properties.PropertiesPlain;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesPlain;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerPlain;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/11/04 17:53:09 $ by $Author: civilis $
 */
public class FormComponentPlainImpl extends FormComponentImpl implements ComponentPlain {

	@Override
	protected void setProperties() {
		
		ComponentPropertiesPlain properties = (ComponentPropertiesPlain)getProperties();
		
		if(properties == null)
			return;
		
		properties.setText(getXFormsManager().getText(this));
	}
	
	@Override
	public PropertiesPlain getProperties() {
		
		if(properties == null) {
			ComponentPropertiesPlain properties = new ComponentPropertiesPlain();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesPlain)properties;
	}
	
	@Override
	public XFormsManagerPlain getXFormsManager() {
		
		return getFormDocument().getContext().getXformsManagerFactory().getXformsManagerPlain();
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
	protected void changeBindNames() { }
}