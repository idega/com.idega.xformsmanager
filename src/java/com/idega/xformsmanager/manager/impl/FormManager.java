package com.idega.xformsmanager.manager.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chiba.xml.xslt.TransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.idega.idegaweb.IWMainApplication;
import com.idega.xformsmanager.business.DocumentManager;
import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.business.XFormPersistenceType;
import com.idega.xformsmanager.business.component.ConstComponentCategory;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.datatypes.ComponentType;
import com.idega.xformsmanager.component.datatypes.ConstComponentDatatype;
import com.idega.xformsmanager.component.impl.FormComponentFactory;
import com.idega.xformsmanager.context.DMContext;
import com.idega.xformsmanager.form.impl.Form;
import com.idega.xformsmanager.generator.impl.ComponentsGeneratorImpl;
import com.idega.xformsmanager.manager.HtmlManagerFactory;
import com.idega.xformsmanager.manager.XFormsManagerFactory;
import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
@Scope("singleton")
//@Service("xformsDocumentManager")
@Service
public class FormManager implements DocumentManager {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.xformsmanager";
	
	private static Logger logger = Logger.getLogger(FormManager.class.getName());
	
	private boolean inited = false;
	
	private PersistenceManager persistenceManager;
	private TransformerService transformerService;
	@Autowired private CacheManager cacheManager;
	@Autowired private XFormsManagerFactory xformsManagerFactory;
	@Autowired private HtmlManagerFactory htmlManagerFactory;
	@Autowired private FormComponentFactory formComponentFactory;
	@Autowired private Form form;
	
	private DMContext DMContext;
	private Document componentsXforms;
	private Document componentsXsd;
	private Document formXformsTemplate;
	
	public com.idega.xformsmanager.business.Document createForm(LocalizedStringBean formName, String formType) {
		
		return getForm().createDocument(formName, getDMContext(), formType);
	}
	
//	private DMContext getNewDMContext() {
//		
//		DMContext context = new DMContext();
//		context.setCacheManager(getCacheManager());
//		context.setPersistenceManager(getPersistenceManager());
//		context.setXformsManagerFactory(getXformsManagerFactory());
//		context.setHtmlManagerFactory(getHtmlManagerFactory());
//		return context;
//	}
	
	public com.idega.xformsmanager.business.Document openForm(Long formId) {
		
		return getForm().loadDocument(formId, getDMContext());
	}
	
	public com.idega.xformsmanager.business.Document openForm(Document xformsDoc) {
		
		return getForm().loadDocument(xformsDoc, getDMContext());
	}
	
	public com.idega.xformsmanager.business.Document takeForm(Long formIdToTakeFrom) {
		
		return getForm().takeAndLoadDocument(formIdToTakeFrom, getDMContext());
	}
	
	public FormManager() { }
	
	public List<String> getAvailableFormComponentsTypesList(ConstComponentCategory category) {
		
		return category == null ? getCacheManager().getAvailableFormComponentsTypesList()
				: getCacheManager().getComponentTypesByCategory(category.getComponentsCategory());
	}
	
	public List<ComponentType> getComponentsByDatatype(ConstComponentDatatype datatype) {
		return getCacheManager().getComponentTypesByDatatype(datatype.getDatatype());
	}
	
	public void init(IWMainApplication iwma) throws InitializationException {
		if(inited) {			
			logger.log(Level.WARNING, "init(): tried to call, when already inited");
			return;
		}

		long start = System.currentTimeMillis();
		try {

			getCacheManager().setFormComponentFactory(getFormComponentFactory());
			getFormComponentFactory().setCacheManager(getCacheManager());
			DMContext = new DMContext();
			DMContext.setCacheManager(getCacheManager());
			DMContext.setPersistenceManager(getPersistenceManager());
			DMContext.setXformsManagerFactory(getXformsManagerFactory());
			DMContext.setHtmlManagerFactory(getHtmlManagerFactory());
			DMContext.setFormComponentFactory(getFormComponentFactory());
			
			// setup ComponentsGenerator
			ComponentsGeneratorImpl.init(iwma);
			ComponentsGeneratorImpl componentsGenerator = ComponentsGeneratorImpl.getInstance();
			componentsGenerator.setTransformerService(getTransformerService());
			
			List<String> componentsTypes = null;
			
			Map<String, List<String>> categorizied = FormManagerUtil.getCategorizedComponentsTypes(getComponentsXforms());
			
			for (List<String> vals : categorizied.values()) {

				if(componentsTypes == null)
					componentsTypes = vals;
				else
					componentsTypes.addAll(vals);
			}
			
			CacheManager cacheManager = getCacheManager();
			
			cacheManager.setFormXformsTemplate(getFormXformsTemplate());
			cacheManager.setAllComponentsTypes(componentsTypes);
			cacheManager.setCategorizedComponentTypes(FormManagerUtil.getCategorizedComponentsTypes(getComponentsXforms()));
			cacheManager.setTypesByDatatype(FormManagerUtil.getComponentsTypesByDatatype(getComponentsXforms()));
			cacheManager.setComponentsXforms(getComponentsXforms());
			cacheManager.setComponentsXsd(getComponentsXsd());
			
			inited = true;
			
			long end = System.currentTimeMillis();
			logger.info("FormManager initialized in: "+(end-start));
			
		} catch (Exception e) {

			logger.log(Level.SEVERE, "Could not initialize FormManager. See \"caused by\" for details.", e);
			throw new InitializationException("Could not initialize FormManager. See \"caused by\" for details.", e);
		}
	}
	
	public boolean isInited() {
		return inited;
	}
	
	public TransformerService getTransformerService() {
		return transformerService;
	}

	public void setTransformerService(TransformerService transformerService) {
		this.transformerService = transformerService;
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

	@Autowired
	@XFormPersistenceType("slide")
	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	public Document getComponentsXforms() {
		return componentsXforms;
	}

	public void setComponentsXforms(Document componentsXforms) {
		this.componentsXforms = componentsXforms;
	}

	public Document getComponentsXsd() {
		return componentsXsd;
	}

	public void setComponentsXsd(Document componentsXsd) {
		this.componentsXsd = componentsXsd;
	}

	public Document getFormXformsTemplate() {
		return formXformsTemplate;
	}

	public void setFormXformsTemplate(Document formXformsTemplate) {
		this.formXformsTemplate = formXformsTemplate;
	}

	public XFormsManagerFactory getXformsManagerFactory() {
		return xformsManagerFactory;
	}

	public void setXformsManagerFactory(XFormsManagerFactory xformsManagerFactory) {
		this.xformsManagerFactory = xformsManagerFactory;
	}

	public HtmlManagerFactory getHtmlManagerFactory() {
		return htmlManagerFactory;
	}

	public void setHtmlManagerFactory(HtmlManagerFactory htmlManagerFactory) {
		this.htmlManagerFactory = htmlManagerFactory;
	}

	FormComponentFactory getFormComponentFactory() {
		return formComponentFactory;
	}

	void setFormComponentFactory(FormComponentFactory formComponentFactory) {
		this.formComponentFactory = formComponentFactory;
	}

	DMContext getDMContext() {
		return DMContext;
	}

	Form getForm() {
		return form;
	}
}