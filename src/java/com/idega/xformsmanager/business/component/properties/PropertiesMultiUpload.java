package com.idega.xformsmanager.business.component.properties;

import com.idega.xformsmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.5 $
 * 
 *          Last modified: $Date: 2009/01/21 14:15:12 $ by $Author: arunas $
 */
public interface PropertiesMultiUpload extends PropertiesComponent {

	public abstract LocalizedStringBean getRemoveButtonLabel();

	public abstract void setRemoveButtonLabel(LocalizedStringBean removeButtonLabel);

	public abstract LocalizedStringBean getInsertButtonLabel();

	public abstract void setInsertButtonLabel(LocalizedStringBean addButtonLabel);

	public abstract LocalizedStringBean getDescriptionLabel();

	public abstract void setDescriptionLabel(LocalizedStringBean descriptionLabel);

	public abstract LocalizedStringBean getUploadingFileDescription();

	public abstract void setUploadingFileDescription(LocalizedStringBean uploadedFileDescription);
	
	public abstract LocalizedStringBean getUploaderHeaderText();

	public abstract void setUploaderHeaderText(LocalizedStringBean headerText);
	
	
}