package com.idega.xformsmanager.manager;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/02 18:54:21 $ by $Author: civilis $
 */
public interface XFormsManagerMultiUpload extends XFormsManager {

	public abstract void addComponentToDocument(FormComponent component);

	public abstract LocalizedStringBean getRemoveButtonLabel(
			FormComponent component);

	public abstract LocalizedStringBean getAddButtonLabel(
			FormComponent component);

	public abstract LocalizedStringBean getUploadingFileDescription(
			FormComponent component);
}