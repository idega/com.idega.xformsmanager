package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:17 $ by $Author: civilis $
 */
public interface XFormsManagerPage extends XFormsManagerContainer {

	public abstract void addComponentToDocument(FormComponent component);

	public abstract void removeComponentFromXFormsDocument(FormComponent component);

	public abstract void moveComponent(FormComponent component,
			String before_component_id);

	public abstract void pageContextChanged(FormComponent component);

}