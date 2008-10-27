package com.idega.xformsmanager.manager;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:35 $ by $Author: civilis $
 */
public interface XFormsManagerButton extends XFormsManager {
	
	public abstract void renewButtonPageContextPages(FormComponent component, FormComponentPage previous, FormComponentPage next);
	
	public abstract void setLastPageToSubmitButton(FormComponent component, String last_page_id);
	
	public abstract String getReferAction(FormComponent component);
	
	public abstract boolean isSubmitButton(FormComponent component);
}