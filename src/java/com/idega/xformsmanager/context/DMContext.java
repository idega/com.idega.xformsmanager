package com.idega.xformsmanager.context;

import org.w3c.dom.Document;

import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.component.impl.FormComponentFactory;
import com.idega.xformsmanager.generator.ComponentsGenerator;
import com.idega.xformsmanager.manager.HtmlManagerFactory;
import com.idega.xformsmanager.manager.XFormsManagerFactory;
import com.idega.xformsmanager.manager.impl.CacheManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $
 * 
 *          Last modified: $Date: 2008/11/13 09:44:37 $ by $Author: civilis $
 */
public class DMContext {

	private CacheManager cacheManager;
	private PersistenceManager persistenceManager;
	private XFormsManagerFactory xformsManagerFactory;
	private HtmlManagerFactory htmlManagerFactory;
	private FormComponentFactory formComponentFactory;
	private ComponentsGenerator componentsGenerator;

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

	public Document getComponentsTemplate() {
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
}