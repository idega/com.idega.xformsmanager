package com.idega.xformsmanager.component.properties.impl;

import com.idega.xformsmanager.business.component.properties.PropertiesStatic;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentPropertiesStatic extends ComponentProperties implements
        PropertiesStatic {
	
	private LocalizedStringBean text;
	
	public LocalizedStringBean getText() {
		return text;
	}
	
	public void setText(LocalizedStringBean text) {
		this.text = text;
		component.update(ConstUpdateType.TEXT);
	}
	
	public void setPlainText(LocalizedStringBean text) {
		this.text = text;
	}
}