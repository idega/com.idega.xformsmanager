package com.idega.xformsmanager.component.impl;

import com.idega.xformsmanager.xform.FormComponentMapping;
import com.idega.xformsmanager.xform.FormComponentOutputMappingImpl;
import com.idega.xformsmanager.xform.Nodeset;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $ Last modified: $Date: 2009/04/29 10:46:50 $ by $Author: civilis $
 */
public class FormComponentOutputImpl extends FormComponentImpl {
	
	@Override
	public FormComponentMapping getFormComponentMapping(Nodeset nodeset) {
		
		return new FormComponentOutputMappingImpl(this, nodeset);
	}
}