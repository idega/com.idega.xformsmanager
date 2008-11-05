package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.ComponentMultiUploadDescription;
import com.idega.xformsmanager.business.component.properties.PropertiesMultiUpload;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesMultiUpload;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerMultiUpload;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/11/05 17:07:07 $ by $Author: civilis $
 */
public class FormComponentMultiUploadImpl extends FormComponentImpl implements ComponentMultiUploadDescription{
	
	@Override
	public XFormsManagerMultiUpload getXFormsManager() {
		
		return getFormDocument().getContext().getXformsManagerFactory().getXformsManagerMultiUploadDescription();
	}
	
	/*
	@Override
	public void setReadonly(boolean readonly) {
		
		super.setReadonly(readonly);
		
		Element bindElement = this.getXformsComponentDataBean().getBind().getBindElement();
		Element groupElem = this.getXformsComponentDataBean().getElement();
		
		if(readonly) {
		    bindElement.setAttribute(FormManagerUtil.relevant_att, FormManagerUtil.xpath_false);
		    groupElem.setAttribute(FormManagerUtil.bind_att, bindElement.getAttribute("id"));
//			TODO: needs to transform to link list for downloading files
		}else {
		    
		    if (!CoreConstants.EMPTY.equals(bindElement.getAttribute(FormManagerUtil.relevant_att)))
			bindElement.removeAttribute(FormManagerUtil.relevant_att);
		    	groupElem.removeAttribute(FormManagerUtil.bind_att);
		    
		}
	}
	*/
	
	@Override
	public PropertiesMultiUpload getProperties(){
		if(properties == null) {
			ComponentPropertiesMultiUpload properties = new ComponentPropertiesMultiUpload();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesMultiUpload)properties;
	    
	}
	
	@Override
	protected void setProperties() {
	
	    	super.setProperties();
		ComponentPropertiesMultiUpload properties = (ComponentPropertiesMultiUpload)getProperties();
		properties.setPlainRemoveButtonLabel(getXFormsManager().getRemoveButtonLabel(this));
		properties.setPlainAddButtonLabel(getXFormsManager().getAddButtonLabel(this));
		properties.setPlainDescriptionButtonLabel(getXFormsManager().getDescriptionButtonLabel(this));
		properties.setPlainUploadingFileDescription(getXFormsManager().getUploadingFileDescription(this));
	}
	
	@Override
	public void update(ConstUpdateType what, Object prop) {
		
		getXFormsManager().update(this, what, prop);
		
		switch (what) {
		case ADD_BUTTON_LABEL:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
			
		case REMOVE_BUTTON_LABEL:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
		case DESCRIPTION_BUTTON_LABEL:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;	
		case LABEL:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
		case UPLOADING_FILE_DESC:
		    	getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;

		default:
			break;
		}
	}
	
}