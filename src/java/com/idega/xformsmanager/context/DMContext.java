package com.idega.xformsmanager.context;

import org.w3c.dom.Document;

import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.component.impl.FormComponentFactory;
import com.idega.xformsmanager.generator.ComponentsGenerator;
import com.idega.xformsmanager.manager.HtmlManagerFactory;
import com.idega.xformsmanager.manager.XFormsManagerFactory;
import com.idega.xformsmanager.manager.impl.CacheManager;
import com.idega.xformsmanager.manager.impl.ComponentsTemplateImpl;

/**
 * placeholder databean with singleton instances
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class DMContext {
	
	private CacheManager cacheManager;
	private PersistenceManager persistenceManager;
	private XFormsManagerFactory xformsManagerFactory;
	private HtmlManagerFactory htmlManagerFactory;
	private FormComponentFactory formComponentFactory;
	private ComponentsGenerator componentsGenerator;
	private ComponentsTemplateImpl componentsTemplate;
	
	public XFormsManagerFactory getXformsManagerFactory() {
		return xformsManagerFactory;
	}
	
	public void setXformsManagerFactory(
	        XFormsManagerFactory xformsManagerFactory) {
		this.xformsManagerFactory = xformsManagerFactory;
	}
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}
	
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
	
	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public HtmlManagerFactory getHtmlManagerFactory() {
		return htmlManagerFactory;
	}
	
	public void setHtmlManagerFactory(HtmlManagerFactory htmlManagerFactory) {
		this.htmlManagerFactory = htmlManagerFactory;
	}
	
	public Document getComponentsTemplateDocument() {
		return getCacheManager().getComponentsTemplate();
	}
	
	public FormComponentFactory getFormComponentFactory() {
		return formComponentFactory;
	}
	
	public void setFormComponentFactory(
	        FormComponentFactory formComponentFactory) {
		this.formComponentFactory = formComponentFactory;
	}
	
	public ComponentsGenerator getComponentsGenerator() {
		return componentsGenerator;
	}
	
	public void setComponentsGenerator(ComponentsGenerator componentsGenerator) {
		this.componentsGenerator = componentsGenerator;
	}
	
	public ComponentsTemplateImpl getComponentsTemplate() {
		return componentsTemplate;
	}
	
	public void setComponentsTemplate(ComponentsTemplateImpl componentsTemplate) {
		this.componentsTemplate = componentsTemplate;
	}
}