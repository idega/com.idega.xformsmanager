package com.idega.xformsmanager.manager.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chiba.xml.xslt.TransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.idega.idegaweb.IWMainApplication;
import com.idega.util.CoreConstants;
import com.idega.xformsmanager.business.DocumentManager;
import com.idega.xformsmanager.business.PersistedFormDocument;
import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.business.XFormPersistenceType;
import com.idega.xformsmanager.business.component.ConstComponentCategory;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.datatypes.ComponentType;
import com.idega.xformsmanager.component.datatypes.ConstComponentDatatype;
import com.idega.xformsmanager.component.impl.FormComponentFactory;
import com.idega.xformsmanager.component.impl.FormDocumentImpl;
import com.idega.xformsmanager.component.impl.FormDocumentTemplateImpl;
import com.idega.xformsmanager.context.DMContext;
import com.idega.xformsmanager.generator.ComponentsGenerator;
import com.idega.xformsmanager.manager.HtmlManagerFactory;
import com.idega.xformsmanager.manager.XFormsManagerFactory;
import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.9 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
@Scope("singleton")
@Service
public class FormManager implements DocumentManager {

	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.xformsmanager";

	private static Logger logger = Logger
	        .getLogger(FormManager.class.getName());

	private boolean inited = false;

	private PersistenceManager persistenceManager;
	private TransformerService transformerService;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private XFormsManagerFactory xformsManagerFactory;
	@Autowired
	private HtmlManagerFactory htmlManagerFactory;
	@Autowired
	private FormComponentFactory formComponentFactory;
	@Autowired
	private ComponentsGenerator componentsGenerator;
	@Autowired
	private ComponentsTemplateImpl componentsTemplate;

	private DMContext DMContext;
	private Document componentsXforms;
	private Document componentsXsd;
	private Document formXformsTemplate;

	@Override
	public com.idega.xformsmanager.business.Document createForm(
	        LocalizedStringBean formTitle, String formType) {

		DMContext context = getDMContext();

		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setXformsDocument(context.getCacheManager()
		        .getFormXformsTemplateCopy());
		formDocument.setContext(context);

		formDocument.loadDocument();

		LocalizedStringBean formDocumentTitle = formDocument.getFormTitle();
		for (Locale currentLocale : formTitle.getLanguagesKeySet())
			formDocumentTitle.setString(currentLocale, formTitle
			        .getString(currentLocale));

		Locale currentDefaultLocale = formTitle.getLanguagesKeySet().iterator()
		        .next();
		formDocument.setDefaultLocale(currentDefaultLocale);
		formDocument.setFormTitle(formDocumentTitle);

		formDocument.setFormType(formType);

		return formDocument;
	}

	@Override
	public com.idega.xformsmanager.business.Document openForm(Long formId) {

		if (formId == null)
			throw new NullPointerException("Form id was not provided");

		DMContext context = getDMContext();

		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(context);

		PersistedFormDocument persistedFormDocument = loadPersistedFormDocument(
		    context, formId);

		formDocument.setXformsDocument(persistedFormDocument
		        .getXformsDocument());
		formDocument.loadDocument(persistedFormDocument);

		return formDocument;
	}

	@Override
	public com.idega.xformsmanager.business.Document openFormLazy(Long formId) {

		if (formId == null)
			throw new NullPointerException("Form id was not provided");

		DMContext context = getDMContext();

		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(context);

		PersistedFormDocument persistedFormDocument = loadPersistedFormDocument(
		    context, formId);

		formDocument.setXformsDocument(persistedFormDocument
		        .getXformsDocument());
		formDocument.lazyLoadDocument(persistedFormDocument);

		return formDocument;
	}

	@Override
	public com.idega.xformsmanager.business.Document openFormLazy(
	        Document xformsDoc) {

		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(getDMContext());
		formDocument.setXformsDocument(xformsDoc);
		formDocument.lazyLoadDocument();

		return formDocument;
	}

	@Override
	public com.idega.xformsmanager.business.Document openForm(Document xformsDoc) {

		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(getDMContext());
		formDocument.setXformsDocument(xformsDoc);
		formDocument.loadDocument();

		return formDocument;
	}

	@Override
	public com.idega.xformsmanager.business.Document takeForm(
	        Long formIdToTakeFrom) {

		if (formIdToTakeFrom == null)
			throw new NullPointerException("Form id was not provided");

		DMContext context = getDMContext();

		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(context);

		PersistedFormDocument persistedFormDocument = takeAndloadFormDocument(
		    context, formIdToTakeFrom);

		formDocument.setXformsDocument(persistedFormDocument
		        .getXformsDocument());
		formDocument.lazyLoadDocument(persistedFormDocument);

		return formDocument;
	}

	public FormManager() {
	}

	@Override
	public List<String> getAvailableFormComponentsTypesList(
	        ConstComponentCategory category) {

		return category == null ? getCacheManager()
		        .getAvailableFormComponentsTypesList() : getCacheManager()
		        .getComponentTypesByCategory(category.getComponentsCategory());
	}

	@Override
	public List<ComponentType> getComponentsByDatatype(
	        ConstComponentDatatype datatype) {
		return getCacheManager().getComponentTypesByDatatype(
		    datatype.getDatatype());
	}

	@Override
	public void init(IWMainApplication iwma) throws InitializationException {
		if (inited) {
			logger.log(Level.WARNING,
			    "init(): tried to call, when already inited");
			return;
		}

		long start = System.currentTimeMillis();
		try {

			getCacheManager()
			        .setFormComponentFactory(getFormComponentFactory());
			getFormComponentFactory().setFormManager(this);
			DMContext = new DMContext();
			DMContext.setCacheManager(getCacheManager());
			DMContext.setPersistenceManager(getPersistenceManager());
			DMContext.setXformsManagerFactory(getXformsManagerFactory());
			DMContext.setHtmlManagerFactory(getHtmlManagerFactory());
			DMContext.setFormComponentFactory(getFormComponentFactory());

			ComponentsTemplateImpl componentsTemplate = getComponentsTemplate();
			componentsTemplate.setDmContext(DMContext);
			componentsTemplate.setFormManager(this);
			DMContext.setComponentsTemplate(componentsTemplate);

			getComponentsGenerator().init(iwma, getTransformerService());
			DMContext.setComponentsGenerator(getComponentsGenerator());

			List<String> componentsTypes = null;

			Map<String, List<String>> categorizied = FormManagerUtil
			        .getCategorizedComponentsTypes(getComponentsXforms());

			for (List<String> vals : categorizied.values()) {

				if (componentsTypes == null)
					componentsTypes = vals;
				else
					componentsTypes.addAll(vals);
			}

			CacheManager cacheManager = getCacheManager();

			cacheManager.setFormXformsTemplate(getFormXformsTemplate());
			cacheManager.setAllComponentsTypes(componentsTypes);
			cacheManager.setCategorizedComponentTypes(FormManagerUtil
			        .getCategorizedComponentsTypes(getComponentsXforms()));
			cacheManager.setTypesByDatatype(FormManagerUtil
			        .getComponentsTypesByDatatype(getComponentsXforms()));
			cacheManager.setComponentsXforms(getComponentsXforms());
			cacheManager.setComponentsXsd(getComponentsXsd());

			inited = true;

			long end = System.currentTimeMillis();
			logger.info("FormManager initialized in: " + (end - start));

		} catch (Exception e) {

			logger
			        .log(
			            Level.SEVERE,
			            "Could not initialize FormManager. See \"caused by\" for details.",
			            e);
			throw new InitializationException(
			        "Could not initialize FormManager. See \"caused by\" for details.",
			        e);
		}
	}

	@Override
	public boolean isInited() {
		return inited;
	}

	public TransformerService getTransformerService() {
		return transformerService;
	}

	@Override
	public void setTransformerService(TransformerService transformerService) {
		this.transformerService = transformerService;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	@Override
	@Autowired
	@XFormPersistenceType(CoreConstants.REPOSITORY)
	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	public Document getComponentsXforms() {
		return componentsXforms;
	}

	@Override
	public void setComponentsXforms(Document componentsXforms) {
		this.componentsXforms = componentsXforms;
	}

	public Document getComponentsXsd() {
		return componentsXsd;
	}

	@Override
	public void setComponentsXsd(Document componentsXsd) {
		this.componentsXsd = componentsXsd;
	}

	public Document getFormXformsTemplate() {
		return formXformsTemplate;
	}

	@Override
	public void setFormXformsTemplate(Document formXformsTemplate) {
		this.formXformsTemplate = formXformsTemplate;
	}

	public XFormsManagerFactory getXformsManagerFactory() {
		return xformsManagerFactory;
	}

	public void setXformsManagerFactory(
	        XFormsManagerFactory xformsManagerFactory) {
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

	private FormDocumentImpl createFormDocument() {

		return new FormDocumentImpl();
	}

	protected PersistedFormDocument loadPersistedFormDocument(
	        DMContext context, Long formId) {

		PersistenceManager persistenceManager = context.getPersistenceManager();
		return persistenceManager.loadForm(formId);
	}

	protected PersistedFormDocument takeAndloadFormDocument(DMContext context,
	        Long formIdToTakeFrom) {

		PersistenceManager persistenceManager = context.getPersistenceManager();
		return persistenceManager.takeForm(formIdToTakeFrom);
	}

	public FormDocument getFormDocumentTemplate() {

		// TODO: use as singleton
		FormDocumentTemplateImpl template = new FormDocumentTemplateImpl();
		template.setXformsDocument(getCacheManager().getComponentsTemplate());
		template.setContext(getDMContext());
		return template;
	}

	ComponentsGenerator getComponentsGenerator() {
		return componentsGenerator;
	}

	ComponentsTemplateImpl getComponentsTemplate() {
		return componentsTemplate;
	}
}