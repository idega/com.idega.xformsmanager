package com.idega.xformsmanager.business.component;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
public interface ButtonArea extends Container {

	public abstract Button getButton(ConstButtonType button_type);

	public abstract Button addButton(ConstButtonType button_type,
			String component_after_this_id) throws NullPointerException;
}