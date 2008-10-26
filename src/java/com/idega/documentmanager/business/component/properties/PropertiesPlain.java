package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:17 $ by $Author: civilis $
 */
public interface PropertiesPlain extends PropertiesComponent {

	public abstract LocalizedStringBean getText();

	public abstract void setText(LocalizedStringBean text);
}