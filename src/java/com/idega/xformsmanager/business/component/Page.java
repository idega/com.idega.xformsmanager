package com.idega.xformsmanager.business.component;

import com.idega.xformsmanager.business.component.properties.PropertiesPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/20 16:31:28 $ by $Author: civilis $
 */
public interface Page extends Container {

	public abstract PropertiesPage getProperties();

	public abstract ButtonArea getButtonArea();

	public abstract ButtonArea createButtonArea(String nextSiblingId);

	/**
	 * special page reflects non usual step in the form - this is either some
	 * behavioral page (e.g. save form), thank you page and so on
	 * 
	 * @return
	 */
	public abstract boolean isSpecialPage();
}