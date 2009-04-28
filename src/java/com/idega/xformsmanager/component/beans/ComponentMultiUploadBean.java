package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.5 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentMultiUploadBean extends ComponentDataBean {
	
	private Element multi_upload_instance;
	
	@Override
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentMultiUploadBean();
	}
	
	public Element getMultiUploadInstance() {
		return multi_upload_instance;
	}
	
	public void setMultiUploadInstance(Element multi_upload_instance) {
		this.multi_upload_instance = multi_upload_instance;
	}
}