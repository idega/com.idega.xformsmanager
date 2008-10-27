package com.idega.xformsmanager.manager;

import java.util.List;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentContainer;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 * 
 *          Last modified: $Date: 2008/10/27 10:27:34 $ by $Author: civilis $
 */
public interface XFormsManagerContainer extends XFormsManager {

	public abstract List<String[]> getContainedComponentsTagNamesAndIds(
			FormComponent component);

	public abstract void addChild(FormComponentContainer parent,
			FormComponent child);
}