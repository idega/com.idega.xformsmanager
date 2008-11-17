package com.idega.xformsmanager.business.component;

import com.idega.xformsmanager.business.component.properties.PropertiesButton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/17 18:03:00 $ by $Author: civilis $
 */
public interface Button extends Component {

	public abstract PropertiesButton getProperties();

	public abstract boolean isSubmitButton();
}