package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

import com.idega.xformsmanager.manager.impl.XFormsManagerMultiUploadImpl;
import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.xform.Bind;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/06 14:15:58 $ by $Author: civilis $
 */
public class ComponentMultiUploadBean extends ComponentDataBean {

	private Element multi_upload_instance;

	/*
	@Override
	public Object clone() {

		ComponentMultiUploadBean clone = (ComponentMultiUploadBean) super
				.clone();

		if (multi_upload_instance != null)
			clone.setMultiUploadInstance((Element) multi_upload_instance
					.cloneNode(true));
		return clone;
	}
	*/

	@Override
	protected ComponentDataBean getDataBeanInstance() {

		return new ComponentMultiUploadBean();
	}

	@Override
	public void putBind(Bind bind) {
		setBind(bind);

		Bind groupBind = bind.getChildBinds().iterator().next();

		// our component element is group
		getElement().setAttribute(FormManagerUtil.bind_att, groupBind.getId());

		// and component repeat element references root bind of this component
		Element repeatElement = XFormsManagerMultiUploadImpl.uploadRepeatElementXPath
				.getNode(getElement());
		repeatElement.setAttribute(FormManagerUtil.bind_att, bind.getId());
	}

	public Element getMultiUploadInstance() {
		return multi_upload_instance;
	}

	public void setMultiUploadInstance(Element multi_upload_instance) {
		this.multi_upload_instance = multi_upload_instance;
	}
}