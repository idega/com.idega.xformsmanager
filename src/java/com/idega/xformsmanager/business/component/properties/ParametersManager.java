package com.idega.xformsmanager.business.component.properties;

import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import com.idega.util.CoreConstants;
import com.idega.util.URIUtil;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:36 $ by $Author: civilis $
 */
public class ParametersManager {
	
	private FormDocument document;

	public void update(Map<String, String> parameters) {

//		TODO: move to document api
		Element dataElement = FormManagerUtil.getFormSubmissionInstanceDataElement(document.getFormDataModelElement());
		
		Element paramsEl = FormManagerUtil.getFormParamsElement(dataElement);
		
		if(paramsEl == null) {

			paramsEl = FormManagerUtil.createFormParamsElement(dataElement, true);
		}
		
		final String params;
		
		if(paramsEl != null) {
			params = paramsEl.getTextContent();
		} else {
			params = null;
		}
		
		URIUtil uriUtil = new URIUtil(params);
		
		for (Entry<String, String> param : parameters.entrySet()) {
		
			if(!FormManagerUtil.isEmpty(param.getKey())) {
			
				uriUtil.setParameter(param.getKey(), param.getValue() == null ? CoreConstants.EMPTY : param.getValue());
			}
		}
		
		paramsEl.setTextContent(uriUtil.getUri());
	}
	
	public void cleanUpdate(Map<String, String> parameters) {
		
//		TODO: clean existing parameters before
		update(parameters);
	}
	
	public Map<String, String> resolve() {
		
		Element dataInstance = document.getFormMainDataInstanceElement();
		Element paramsEl = FormManagerUtil.getFormParamsElement(dataInstance);
		
		return paramsEl == null ? new URIUtil(null).getParameters() : new URIUtil(paramsEl.getTextContent()).getParameters();
	}
	
	public void setFormDocumentComponent(FormDocument document) {
		this.document = document;
	}
	
	public FormDocument getFormDocumentComponent() {
		return document;
	}
}