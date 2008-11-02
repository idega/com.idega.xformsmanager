package com.idega.xformsmanager.manager;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.beans.LocalizedItemsetBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/02 18:54:21 $ by $Author: civilis $
 */
public interface XFormsManagerSelect extends XFormsManager {

	public abstract Integer getDataSrcUsed(FormComponent component);

	public abstract String getExternalDataSrc(FormComponent component);

	public abstract LocalizedItemsetBean getItemset(FormComponent component);

	public abstract void removeSelectComponentSourcesFromXFormsDocument(
			FormComponent component);
}