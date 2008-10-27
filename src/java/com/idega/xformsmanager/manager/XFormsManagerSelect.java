package com.idega.xformsmanager.manager;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.beans.LocalizedItemsetBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/10/27 20:23:46 $ by $Author: civilis $
 */
public interface XFormsManagerSelect extends XFormsManager {

	public abstract void loadComponentFromTemplate(FormComponent component,
			String componentType) throws NullPointerException;

	public abstract void addComponentToDocument(FormComponent component);

	public abstract Integer getDataSrcUsed(FormComponent component);

	public abstract String getExternalDataSrc(FormComponent component);

	public abstract LocalizedItemsetBean getItemset(FormComponent component);

	public abstract void update(FormComponent component, ConstUpdateType what);

	public abstract void removeSelectComponentSourcesFromXFormsDocument(
			FormComponent component);

}