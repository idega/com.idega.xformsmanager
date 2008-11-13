package com.idega.xformsmanager.component;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/13 20:07:33 $ by $Author: civilis $
 */
public interface FormComponentButtonArea extends FormComponentContainer {

	public abstract void setPageSiblings(FormComponentPage previous,
			FormComponentPage next);

	public abstract void announceLastPage(String lastPageId);
}