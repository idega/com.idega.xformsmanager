package com.idega.xformsmanager.xform;

import org.w3c.dom.Element;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.manager.impl.XFormsManagerMultiUploadImpl;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentBindMultiUploadImpl extends ComponentBindImpl {
	
	public ComponentBindMultiUploadImpl(FormComponent formComponent, Bind bind) {
		super(formComponent, bind);
	}
	
	@Override
	public void updateBindReference() {
		
		super.updateBindReference();
		
		Bind entriesBind = getBind().getChildBinds().iterator().next();
		
		// our component element is group
		
		Element componentElement = getFormComponent().getComponentDataBean()
		        .getElement();
		
		// and component repeat element references root bind of this component
		Element repeatElement = XFormsManagerMultiUploadImpl.uploadRepeatElementXPath
		        .getNode(componentElement);
		repeatElement.setAttribute(FormManagerUtil.bind_att, entriesBind
		        .getId());
	}
}