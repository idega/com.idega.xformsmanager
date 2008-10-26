package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedItemsetBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:17 $ by $Author: civilis $
 */
public interface XFormsManagerSelect extends XFormsManager {

	public abstract void loadXFormsComponentByTypeFromComponentsXForm(FormComponent component,
			String componentType) throws NullPointerException;

	public abstract void addComponentToDocument(FormComponent component);

	public abstract Integer getDataSrcUsed(FormComponent component);

	public abstract String getExternalDataSrc(FormComponent component);

	public abstract LocalizedItemsetBean getItemset(FormComponent component);

	public abstract void update(FormComponent component, ConstUpdateType what);

	public abstract void removeSelectComponentSourcesFromXFormsDocument(
			FormComponent component);

}