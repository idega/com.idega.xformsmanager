package com.idega.xformsmanager.manager;

import com.idega.block.process.variables.Variable;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
public interface XFormsManager {

	public abstract void loadComponentFromTemplate(FormComponent component,
			String componentType);

	public abstract void loadXFormsComponentFromDocument(FormComponent component);

	public abstract void addComponentToDocument(FormComponent component);

	public abstract void update(FormComponent component, ConstUpdateType what);

	public abstract void moveComponent(FormComponent component,
			String nextSiblingId);

	public abstract void removeComponentFromXFormsDocument(
			FormComponent component);

//	public abstract String insertBindElement(FormComponent component,
//			Element new_bind_element, String bind_id);

	// public abstract void changeBindName(FormComponent component,
	// String new_bind_name);

	public abstract LocalizedStringBean getLocalizedStrings(
			FormComponent component);

	public abstract LocalizedStringBean getErrorLabelLocalizedStrings(
			FormComponent component);

	public abstract LocalizedStringBean getHelpText(FormComponent component);

	public abstract LocalizedStringBean getValidationText(
			FormComponent component);

	public abstract void loadConfirmationElement(FormComponent component,
			FormComponentPage confirmationPage);

	public abstract String getAutofillKey(FormComponent component);

	public abstract boolean isRequired(FormComponent component);

	public abstract Variable getVariable(FormComponent component);

	// public abstract boolean isReadonly(FormComponent component);
	//	
	// public void setReadonly(FormComponent component, boolean readonly);
}