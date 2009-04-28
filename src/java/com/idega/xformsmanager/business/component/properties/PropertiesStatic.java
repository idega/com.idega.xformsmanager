package com.idega.xformsmanager.business.component.properties;

import com.idega.xformsmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public interface PropertiesStatic extends PropertiesComponent {

	public abstract LocalizedStringBean getText();

	public abstract void setText(LocalizedStringBean text);
}