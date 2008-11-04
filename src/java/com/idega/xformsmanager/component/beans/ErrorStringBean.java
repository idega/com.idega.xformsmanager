package com.idega.xformsmanager.component.beans;

import com.idega.chiba.web.xml.xforms.validation.ErrorType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 * 
 *          Last modified: $Date: 2008/11/04 17:53:09 $ by $Author: civilis $
 */
public class ErrorStringBean {

	private ErrorType errorType;
	private LocalizedStringBean localizedStringBean;

	public ErrorStringBean(ErrorType errorType,
			LocalizedStringBean localizedStringBean) {

		this.errorType = errorType;
		this.localizedStringBean = localizedStringBean;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	public LocalizedStringBean getLocalizedStringBean() {
		return localizedStringBean;
	}

	public void setLocalizedStringBean(LocalizedStringBean localizedStringBean) {
		this.localizedStringBean = localizedStringBean;
	}
}