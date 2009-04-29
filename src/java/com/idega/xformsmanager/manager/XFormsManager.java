package com.idega.xformsmanager.manager;

import java.util.Map;

import com.idega.block.process.variables.Variable;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.10 $
 * 
 *          Last modified: $Date: 2009/04/29 12:23:32 $ by $Author: arunas $
 */
public interface XFormsManager {

	/**
	 * loads component from template document set to this component. In this
	 * phase, the references of nodes are to the template document. called in
	 * the load from template phase
	 * 
	 * @param component
	 */
	public abstract void loadComponentFromTemplate(FormComponent component);

	/**
	 * loads the component from it's document. called in load phase of the
	 * component
	 * 
	 * @param component
	 */
	public abstract void loadComponentFromDocument(FormComponent component);

	/**
	 * adds component, which is loaded from template (@see
	 * loadComponentFromTemplate), to the form document provided. After this,
	 * references of nodes are nomore to the template document. called in the
	 * create phase after this is called, the component will no longer contain
	 * the template elements (like template component data bean)
	 * 
	 * @param component
	 */
	public abstract void addComponentToDocument(FormComponent component);

	public abstract void update(FormComponent component, ConstUpdateType what,
			Object property);

	public abstract void moveComponent(FormComponent component,
			String nextSiblingId);

	public abstract void removeComponentFromXFormsDocument(
			FormComponent component);

	// public abstract String insertBindElement(FormComponent component,
	// Element new_bind_element, String bind_id);

	// public abstract void changeBindName(FormComponent component,
	// String new_bind_name);

	public abstract LocalizedStringBean getLocalizedStrings(
			FormComponent component);

	public abstract Map<ErrorType, LocalizedStringBean> getErrorLabelLocalizedStrings(
			FormComponent component);

	public abstract LocalizedStringBean getHelpText(FormComponent component);

	// public abstract LocalizedStringBean getValidationText(
	// FormComponent component);

	public abstract void loadConfirmationElement(FormComponent component,
			FormComponentPage confirmationPage);

	public abstract String getAutofillKey(FormComponent component);
	
	public abstract String getCaculate(FormComponent component);
	
	public abstract boolean isCalculate(FormComponent component);

	public abstract boolean isRequired(FormComponent component);

	public abstract Variable getVariable(FormComponent component);

	// public abstract boolean isReadonly(FormComponent component);
	//	
	// public void setReadonly(FormComponent component, boolean readonly);
	
	public abstract void bindsRenamed(FormComponent component);
}