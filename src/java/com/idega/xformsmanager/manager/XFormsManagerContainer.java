package com.idega.xformsmanager.manager;

import java.util.List;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentContainer;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
public interface XFormsManagerContainer extends XFormsManager {

	public abstract List<String[]> getContainedComponentsTypesAndIds(
			FormComponent component);

	public abstract void addChild(FormComponentContainer parent,
			FormComponent child);
}