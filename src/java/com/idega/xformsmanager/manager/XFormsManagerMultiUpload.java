package com.idega.xformsmanager.manager;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:34 $ by $Author: civilis $
 */
public interface XFormsManagerMultiUpload extends XFormsManager{
	
	public abstract void loadXFormsComponentByTypeFromComponentsXForm(FormComponent component,
			String componentType) throws NullPointerException;

	public abstract void addComponentToDocument(FormComponent component);
	
	public abstract LocalizedStringBean getRemoveButtonLabel(FormComponent component);
	
	public abstract LocalizedStringBean getAddButtonLabel(FormComponent component);
	
	public abstract LocalizedStringBean getUploadingFileDescription(FormComponent component);
	
	public abstract void update(FormComponent component, ConstUpdateType what);
}