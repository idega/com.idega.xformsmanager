package com.idega.documentmanager.business.component;

import com.idega.documentmanager.business.component.properties.PropertiesSelect;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:16 $ by $Author: civilis $
 */
public interface ComponentSelect extends Component {
	
	public abstract PropertiesSelect getProperties();
}