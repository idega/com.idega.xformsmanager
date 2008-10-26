package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesThankYouPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:17 $ by $Author: civilis $
 */
public class ComponentPropertiesThankYouPage extends ComponentPropertiesPage implements PropertiesThankYouPage {

	private LocalizedStringBean text;

	public LocalizedStringBean getText() {
		return text;
	}

	public void setText(LocalizedStringBean text) {
		this.text = text;
		component.update(ConstUpdateType.THANKYOU_TEXT);
	}
	
	public void setPlainText(LocalizedStringBean text) {
		this.text = text;
	}
}