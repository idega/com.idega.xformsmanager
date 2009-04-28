package com.idega.xformsmanager.component.properties.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.idega.block.process.variables.Variable;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.util.StringUtil;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.beans.ErrorStringBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.xform.ComponentBind;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $ Last modified: $Date: 2009/04/28 13:01:50 $ by $Author: arunas $
 */
public class ComponentProperties implements PropertiesComponent {
	
	private boolean required = false;
	// private boolean readonly = false;
	private LocalizedStringBean label;
	private Map<ErrorType, LocalizedStringBean> errorsMessages;
	private LocalizedStringBean helpText;
	// private LocalizedStringBean validationText;
	private String p3ptype;
	private String autofillKey;
	private Variable variable;
	private String calculate;
	private boolean isCalculate = false;
	
	protected FormComponent component;
	
	public LocalizedStringBean getErrorMsg(ErrorType errorType) {
		
		return getErrorsMessages().get(errorType);
	}
	
	public void setErrorMsg(ErrorType errorType, LocalizedStringBean errorMsg) {
		getErrorsMessages().put(errorType, errorMsg);
		component.update(ConstUpdateType.ERROR_MSG, new ErrorStringBean(
		        errorType, errorMsg));
	}
	
	public Collection<ErrorType> getExistingErrors() {
		
		Collection<ErrorType> errors = getErrorsMessages().keySet();
		
		if (!isRequired() && errors.contains(ErrorType.required)) {
			
			errors = new HashSet<ErrorType>(errors);
			errors.remove(ErrorType.required);
		}
		
		return errors;
	}
	
	// public LocalizedStringBean getErrorMsg() {
	// return errorMsg;
	// }
	// public void setErrorMsg(LocalizedStringBean error_msg) {
	// this.errorMsg = error_msg;
	// component.update(ConstUpdateType.ERROR_MSG);
	// }
	public LocalizedStringBean getLabel() {
		return label;
	}
	
	public void setLabel(LocalizedStringBean label) {
		this.label = label;
		component.update(ConstUpdateType.LABEL);
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
		component.update(ConstUpdateType.CONSTRAINT_REQUIRED);
	}
	
	public void setPlainLabel(LocalizedStringBean label) {
		this.label = label;
	}
	
	public void setPlainRequired(boolean required) {
		this.required = required;
	}
	
	public void setComponent(FormComponent component) {
		this.component = component;
	}
	
	public String toString() {
		return new StringBuffer().append("\nrequired: ").append(required)
		        .append("\nlabel: ").append(label).append("\np3ptype: ")
		        .append(p3ptype).append("\nautofill key: ").append(autofillKey)
		        .append("\nhelp text: ").append(helpText).append("\nerrors: ")
		        .append(getErrorsMessages())
		        .append("\ncalc expression: ")
		        .append(calculate)
		        .append("\nisCalculate: ")
		        .append(isCalculate)
		        // .append("\nreadonly: ")
		        // .append(readonly)
		        // .append("\nvalidationText: ")
		        // .append(validationText)
		        .toString();
	}
	
	public String getP3ptype() {
		return p3ptype;
	}
	
	public void setP3ptype(String p3ptype) {
		this.p3ptype = p3ptype;
		component.update(ConstUpdateType.P3P_TYPE);
	}
	
	public void setPlainP3ptype(String p3ptype) {
		this.p3ptype = p3ptype;
	}
	
	public String getAutofillKey() {
		return autofillKey;
	}
	
	public void setAutofillKey(String autofill_key) {
		
		this.autofillKey = autofill_key;
		component.update(ConstUpdateType.AUTOFILL_KEY);
	}
	
	public void setPlainAutofillKey(String autofill_key) {
		this.autofillKey = autofill_key;
	}
	
	public LocalizedStringBean getHelpText() {
		return helpText;
	}
	
	public void setHelpText(LocalizedStringBean help_text) {
		this.helpText = help_text;
		component.update(ConstUpdateType.HELP_TEXT);
	}
	
	public void setPlainHelpText(LocalizedStringBean help_text) {
		this.helpText = help_text;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	public void setVariable(Variable variable) {
		this.variable = variable;
		component.update(ConstUpdateType.VARIABLE_NAME);
	}
	
	public void setVariable(String variableStringRepresentation) {
		setVariable(StringUtil.isEmpty(variableStringRepresentation) ? null
		        : Variable
		                .parseDefaultStringRepresentation(variableStringRepresentation));
	}
	
	public void setPlainVariable(Variable variable) {
		this.variable = variable;
	}
	
	public boolean isHasValidationConstraints() {
		
		if (isRequired())
			return true;
		
		ComponentBind componentBind = component.getComponentDataBean()
		        .getComponentBind();
		
		return componentBind.exists()
		        && componentBind.getBind().hasValidationConstraints();
	}
	
	protected Map<ErrorType, LocalizedStringBean> getErrorsMessages() {
		
		if (errorsMessages == null)
			errorsMessages = new HashMap<ErrorType, LocalizedStringBean>(4);
		
		return errorsMessages;
	}
	
	public void setErrorsMessages(
	        Map<ErrorType, LocalizedStringBean> errorsMessages) {
		this.errorsMessages = errorsMessages;
	}
	
	public String getCalculateExp() {
		return calculate;
	}
	
	public void setCalculateExp(String calculate_exp) {
		
		this.calculate = calculate_exp;
		component.update(ConstUpdateType.CALCULATE_EXP);
	}
	
	public void setPlainCalculateExp(String calculate_exp) {
		this.calculate = calculate_exp;
	}
	
	public boolean isCalculate() {
	    return isCalculate;
    }
	
	public void setIsCalculate(boolean isCalculate) {
	
		this.isCalculate = isCalculate;
		component.update(ConstUpdateType.CALCULATE);
	
	}

	public void setPlainIsCalculate(boolean isCalculate) {
		
		this.isCalculate = isCalculate;
	}
}