package com.idega.xformsmanager.manager.impl;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.manager.HtmlManager;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2009/06/05 08:11:03 $ by $Author: valdas $
 */
@Service
@Scope("singleton")
public class HtmlManagerImpl implements HtmlManager {
	
	public Element getHtmlRepresentation(FormComponent component, Locale locale) throws Exception {
		
		ComponentDataBean componentDataBean = component.getComponentDataBean();
		Map<Locale, Element> localizedHtmlRepresentations = componentDataBean.getLocalizedHtmlComponents();
		
		Element localizedRepresentation;
		
		if(!localizedHtmlRepresentations.containsKey(locale)) {
			
			Document doc = getXFormsDocumentHtmlRepresentation(component, locale);
			
			if(doc != null) {
			
				localizedRepresentation = FormManagerUtil.getElementById(doc, component.getId());
			} else 
				localizedRepresentation = null;
			
			if(localizedRepresentation != null)
				localizedHtmlRepresentations.put(locale, localizedRepresentation);
			
		} else {
		
			localizedRepresentation = localizedHtmlRepresentations.get(locale);
		}
		
		if(localizedRepresentation == null)
			throw new IllegalStateException("Component html representation couldn't be resolved for: " + component.getType());
		
		return localizedRepresentation;
	}
	
	public void clearHtmlComponents(FormComponent component) {
		component.getComponentDataBean().getLocalizedHtmlComponents().clear();
	}
	
	protected Document getXFormsDocumentHtmlRepresentation(FormComponent component, Locale locale) throws Exception {
		FormDocument formDocument = component.getFormDocument();
		Document componentsXml = formDocument.getComponentsXml(locale);
		
		return componentsXml;
	}
}