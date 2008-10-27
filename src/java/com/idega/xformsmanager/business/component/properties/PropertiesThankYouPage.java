package com.idega.xformsmanager.business.component.properties;

import com.idega.xformsmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:36 $ by $Author: civilis $
 */
public interface PropertiesThankYouPage extends PropertiesPage {
	
	public abstract LocalizedStringBean getText();
	
	public abstract void setText(LocalizedStringBean text);
}