package com.idega.xformsmanager.component.properties.impl;

import com.idega.util.CoreConstants;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:42 $ by $Author: civilis $
 */
public class ComponentPropertiesButton extends ComponentProperties implements PropertiesButton {
	
	private String referAction;
	
	public String getReferAction() {
		return referAction;
	}
	public void setReferAction(String referAction) {
		setReferActionPlain(referAction);
		component.update(ConstUpdateType.BUTTON_REFER_TO_ACTION);
	}
	public void setReferActionPlain(String referAction) {
		this.referAction = CoreConstants.EMPTY.equals(referAction) ? null : referAction;
	}
}