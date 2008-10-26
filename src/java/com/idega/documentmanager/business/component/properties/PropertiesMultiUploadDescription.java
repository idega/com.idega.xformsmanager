package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.beans.LocalizedStringBean;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.1 $
 * 
 * Last modified: $Date: 2008/10/26 17:01:17 $ by $Author: civilis $
 */
public interface PropertiesMultiUploadDescription extends PropertiesComponent{
    
    public abstract LocalizedStringBean getRemoveButtonLabel();

    public abstract void setRemoveButtonLabel(LocalizedStringBean removeButtonLabel);
    
    public abstract LocalizedStringBean getAddButtonLabel();

    public abstract void setAddButtonLabel(LocalizedStringBean addButtonLabel);
    
    public abstract LocalizedStringBean getDescriptionLabel();

    public abstract void setDescriptionLabel(LocalizedStringBean addButtonLabel);
    
    public abstract LocalizedStringBean getUploadingFileDescription();

    public abstract void setUploadingFileDescription(LocalizedStringBean addButtonLabel);
    
    
}
