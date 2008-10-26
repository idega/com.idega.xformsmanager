package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:17 $ by $Author: civilis $
 */
public interface XFormsManagerThankYouPage extends XFormsManagerPage {

	public abstract void update(FormComponent component, ConstUpdateType what);

	public abstract LocalizedStringBean getThankYouText(FormComponent component);
}