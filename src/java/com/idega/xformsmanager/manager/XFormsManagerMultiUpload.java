package com.idega.xformsmanager.manager;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.6 $
 * 
 *          Last modified: $Date: 2009/01/21 14:15:12 $ by $Author: arunas $
 */
public interface XFormsManagerMultiUpload extends XFormsManager {

	public abstract LocalizedStringBean getRemoveButtonLabel(
			FormComponent component);

	public abstract LocalizedStringBean getInsertButtonLabel(
			FormComponent component);

	public abstract LocalizedStringBean getDescriptionButtonLabel(
			FormComponent component);

	public abstract LocalizedStringBean getUploadingFileDescription(
			FormComponent component);
	
	public abstract LocalizedStringBean getUploaderHeaderText(
			FormComponent component);
}