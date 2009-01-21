package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.business.component.ComponentMultiUpload;
import com.idega.xformsmanager.business.component.properties.PropertiesMultiUpload;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesMultiUpload;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerMultiUpload;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.7 $
 * 
 *          Last modified: $Date: 2009/01/21 14:15:12 $ by $Author: arunas $
 */
public class FormComponentMultiUploadImpl extends FormComponentImpl implements
		ComponentMultiUpload {

	@Override
	public XFormsManagerMultiUpload getXFormsManager() {

		return getFormDocument().getContext().getXformsManagerFactory()
				.getXformsManagerMultiUploadDescription();
	}

	@Override
	public PropertiesMultiUpload getProperties() {
		if (properties == null) {
			ComponentPropertiesMultiUpload properties = new ComponentPropertiesMultiUpload();
			properties.setComponent(this);
			this.properties = properties;
		}

		return (PropertiesMultiUpload) properties;
	}

	@Override
	protected void setProperties() {

		super.setProperties();
		ComponentPropertiesMultiUpload properties = (ComponentPropertiesMultiUpload) getProperties();
		properties.setPlainRemoveButtonLabel(getXFormsManager()
				.getRemoveButtonLabel(this));
		properties.setPlainAddButtonLabel(getXFormsManager().getInsertButtonLabel(
				this));
		properties.setPlainDescriptionButtonLabel(getXFormsManager()
				.getDescriptionButtonLabel(this));
		properties.setPlainUploadingFileDescription(getXFormsManager()
				.getUploadingFileDescription(this));
		properties.setPlainUploaderHeaderText(getXFormsManager().getUploaderHeaderText(this));
	}

	@Override
	public void update(ConstUpdateType what, Object prop) {

		super.update(what, prop);

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
//		case LABEL:
//			getHtmlManager().clearHtmlComponents(this);
//			getFormDocument().setFormDocumentModified(true);
//			break;
		case UPLOADING_FILE_DESC:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
		case UPLOADER_HEADER_TEXT:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
		default:
			break;
		}
	}
}