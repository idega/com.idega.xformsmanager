package com.idega.xformsmanager.component;

import com.idega.xformsmanager.business.component.ButtonArea;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/20 16:31:27 $ by $Author: civilis $
 */
public interface FormComponentPage extends FormComponentContainer {

	public abstract void setButtonAreaComponentId(String buttonAreaId);

	public abstract void setPageSiblings(FormComponentPage previous,
			FormComponentPage next);

	public abstract void pagesSiblingsChanged();

	public abstract FormComponentPage getPreviousPage();

	public abstract FormComponentPage getNextPage();

	public abstract ButtonArea getButtonArea();

	/**
	 * special page reflects non usual step in the form - this is either some
	 * behavioral page (e.g. save form), thank you page and so on
	 * 
	 * @return
	 */
	public abstract boolean isSpecialPage();
}