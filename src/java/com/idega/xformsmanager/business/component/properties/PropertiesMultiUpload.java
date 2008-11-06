package com.idega.xformsmanager.business.component.properties;

import com.idega.xformsmanager.component.beans.LocalizedStringBean;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.3 $
 * 
 * Last modified: $Date: 2008/11/06 12:32:55 $ by $Author: civilis $
 */
public interface PropertiesMultiUpload extends PropertiesComponent{
    
    public abstract LocalizedStringBean getRemoveButtonLabel();

    public abstract void setRemoveButtonLabel(LocalizedStringBean removeButtonLabel);
    
    public abstract LocalizedStringBean getInsertButtonLabel();

    public abstract void setInsertButtonLabel(LocalizedStringBean addButtonLabel);
    
    public abstract LocalizedStringBean getDescriptionLabel();

    public abstract void setDescriptionLabel(LocalizedStringBean addButtonLabel);
    
    public abstract LocalizedStringBean getUploadingFileDescription();

    public abstract void setUploadingFileDescription(LocalizedStringBean addButtonLabel);
    
    
}
