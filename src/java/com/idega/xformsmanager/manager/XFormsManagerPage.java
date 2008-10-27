package com.idega.xformsmanager.manager;

import com.idega.xformsmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:35 $ by $Author: civilis $
 */
public interface XFormsManagerPage extends XFormsManagerContainer {

	public abstract void addComponentToDocument(FormComponent component);

	public abstract void removeComponentFromXFormsDocument(FormComponent component);

	public abstract void moveComponent(FormComponent component,
			String before_component_id);

	public abstract void pageContextChanged(FormComponent component);

}