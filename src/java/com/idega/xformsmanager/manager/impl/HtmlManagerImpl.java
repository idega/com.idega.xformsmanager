package com.idega.xformsmanager.manager.impl;

import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.manager.HtmlManager;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:37 $ by $Author: civilis $
 */
public class HtmlManagerImpl implements HtmlManager {
	
	public Element getHtmlRepresentation(FormComponent component, Locale locale) throws Exception {
		
		ComponentDataBean componentDataBean = component.getXformsComponentDataBean();
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
			throw new IllegalStateException("Component html representation couldn't be resolved");
		
		return localizedRepresentation;
	}
	
	public void clearHtmlComponents(FormComponent component) {
		component.getXformsComponentDataBean().getLocalizedHtmlComponents().clear();
	}
	
	protected Document getXFormsDocumentHtmlRepresentation(FormComponent component, Locale locale) throws Exception {
		FormDocument formDocument = component.getFormDocument();
		Document componentsXml = formDocument.getComponentsXml(locale);
		
		return componentsXml;
	}
}