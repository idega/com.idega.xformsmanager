package com.idega.xformsmanager.component;

/**
 * TODO: don't use setLastPageId, but instead implement eventing mechanism (e.g. thxPageIdChanged event)
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/17 18:03:16 $ by $Author: civilis $
 */
public interface FormComponentButton extends FormComponent {

	public abstract void setSiblingsAndParentPages(FormComponentPage previous,
			FormComponentPage next);

	public abstract void setLastPageId(String last_page_id);
}