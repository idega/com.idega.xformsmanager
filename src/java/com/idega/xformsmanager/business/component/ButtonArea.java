package com.idega.xformsmanager.business.component;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/13 20:06:33 $ by $Author: civilis $
 */
public interface ButtonArea extends Container {

	public abstract Button addButton(ConstButtonType button_type,
			String component_after_this_id) throws NullPointerException;
}