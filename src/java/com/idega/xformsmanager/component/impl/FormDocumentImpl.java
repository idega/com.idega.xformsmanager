package com.idega.xformsmanager.component.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.idega.util.xml.XmlUtil;
import com.idega.xformsmanager.business.PersistedFormDocument;
import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.business.component.properties.ParametersManager;
import com.idega.xformsmanager.business.component.properties.PropertiesDocument;
import com.idega.xformsmanager.business.ext.FormVariablesHandler;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ComponentPropertiesDocument;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.context.DMContext;
import com.idega.xformsmanager.context.Event;
import com.idega.xformsmanager.generator.ComponentsGenerator;
import com.idega.xformsmanager.manager.XFormsManagerDocument;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * TODO: put isLazyLoaded(true) everywhere the loading of the document is needed
 * for further processing. This could be accomplished by using aspects and
 * annotations too
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.18 $
 * 
 *          Last modified: $Date: 2009/02/13 17:19:06 $ by $Author: donatas $
 */
public class FormDocumentImpl extends FormComponentContainerImpl implements
		com.idega.xformsmanager.business.Document,
		com.idega.xformsmanager.component.FormDocument {

	private FormVariablesHandler formVariablesHandler;
	private LocalizedStringBean formTitle;
	private LocalizedStringBean formErrorMsg;
	private Long formId;
	private Integer formVersion;
	private PropertiesDocument properties;
	private List<String> registeredForLastPageIdPages;
	private ParametersManager parametersManager;
	private String formType;
	private Map<Locale, Document> localizedComponentsDocuments;
	private Locale defaultLocale;
	private boolean formDocumentModified = true;
	private DocumentBuilder builder;

	private DMContext context;
	private Document xformsDocument;

	private PersistedFormDocument persistedFormForLazyLoading;
	private boolean loaded = false;

	private int lastComponentId = 0;

	public void setContainerElement(Element container_element) {
		getXFormsManager().setComponentsContainer(this, container_element);
	}

	@Override
	public XFormsManagerDocument getXFormsManager() {

		return getContext().getXformsManagerFactory()
				.getXformsManagerDocument();
	}

	public Document getXformsDocument() {
		return xformsDocument;
	}

	public void populateSubmissionDataWithXML(Document submission, boolean clean) {

		getXFormsManager().populateSubmissionDataWithXML(this, submission,
				clean);
	}

	public void setFormDocumentModified(boolean formDocumentModified) {
		this.formDocumentModified = formDocumentModified;

		if (formDocumentModified)
			getLocalizedComponentsDocuments().clear();
	}

	public boolean isFormDocumentModified() {
		return formDocumentModified;
	}

	public Document getComponentsXml(Locale locale) {

		Map<Locale, Document> localizedComponentsDocuments = getLocalizedComponentsDocuments();

		Document doc;

		if (!localizedComponentsDocuments.containsKey(locale)) {

			try {
				ComponentsGenerator componentsGenerator = getContext()
						.getComponentsGenerator();
				Document xformClone = (Document) getXformsDocument().cloneNode(
						true);

				FormManagerUtil.modifyXFormsDocumentForViewing(xformClone);
				FormManagerUtil.setCurrentFormLocale(xformClone, locale);
				FormManagerUtil
						.modifyFormForLocalisationInFormbuilder(xformClone);

				doc = componentsGenerator
						.generateHtmlRepresentation(xformClone);

				localizedComponentsDocuments.put(locale, doc);
				setFormDocumentModified(false);

			} catch (Exception e) {

				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Exception while generating components html document",
						e);
				doc = localizedComponentsDocuments.isEmpty() ? null
						: localizedComponentsDocuments.values().iterator()
								.next();
			}

		} else {
			doc = localizedComponentsDocuments.get(locale);
		}

		return doc;
	}

	public Long getFormId() {

		return formId;
	}

	public void setFormId(Long formId) {

		FormManagerUtil.setFormId(getXformsDocument(), String.valueOf(formId));
		this.formId = formId;
	}

	@Override
	public String getId() {
		return String.valueOf(getFormId());
	}

	public Locale getDefaultLocale() {

		if (defaultLocale == null) {
			defaultLocale = FormManagerUtil
					.getDefaultFormLocale(getXformsDocument());
		}

		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {

		FormManagerUtil
				.setDefaultFormLocale(getXformsDocument(), defaultLocale);
		this.defaultLocale = defaultLocale;
	}

	public Page addPage(String nextSiblingPageId) {

		if (nextSiblingPageId == null && !getContainedPagesIdList().isEmpty()) {

			// find the first special page to insert before

			for (String pageId : getContainedPagesIdList()) {

				if (((FormComponentPage) getContainedComponent(pageId))
						.isSpecialPage()) {

					nextSiblingPageId = pageId;
					break;
				}
			}
		}

		Page page = (Page) super.addComponent(FormComponentFactory.page_type,
				nextSiblingPageId);
		componentsOrderChanged();
		return page;
	}

	@Override
	public Component addComponent(String component_type,
			String component_after_this_id) throws NullPointerException {
		throw new NullPointerException("Use addPage method instead");
	}

	public Page getPage(String pageId) {
		return (Page) getContainedComponent(pageId);
	}

	public List<Page> getSpecialPages() {

		ArrayList<Page> specialPages = new ArrayList<Page>(
				getContainedComponentsIds().size());

		for (FormComponent childPage : getContainedComponents().values()) {

			if (((FormComponentPage) childPage).isSpecialPage()) {
				specialPages.add((Page) childPage);
			}
		}

		return specialPages;
	}

	public List<String> getContainedPagesIdList() {
		return getContainedComponentsIds();
	}

	public String generateNewComponentId() {

		if (lastComponentId == 0) {

			Set<String> allIds = FormManagerUtil
					.getAllComponentsIds(getXformsDocument());

			for (String id : allIds) {
				int idNr = FormManagerUtil.parseIdNumber(id);

				if (idNr > lastComponentId)
					lastComponentId = idNr;
			}
		}

		return FormManagerUtil.CTID + (++lastComponentId);
	}

	public String getFormSourceCode() throws Exception {

		return FormManagerUtil.serializeDocument(getXformsDocument());
	}

	public void setFormSourceCode(String newSourceCode) throws Exception {

		if (builder == null)
			builder = XmlUtil.getDocumentBuilder();

		clear();

		ByteArrayInputStream bais = new ByteArrayInputStream(newSourceCode
				.getBytes("UTF-8"));
		Document newXForm = builder.parse(new InputSource(bais));
		setXformsDocument(newXForm);
		loadDocument();
	}

	public void loadDocument() {

		loadDocumentInternal(null);
	}

	public void loadDocument(PersistedFormDocument persistedForm) {

		loadDocumentInternal(persistedForm);
	}

	public void lazyLoadDocument(PersistedFormDocument persistedForm) {

		if (persistedForm != null) {
			setFormId(persistedForm.getFormId());
			setFormType(persistedForm.getFormType());
			setFormVersion(persistedForm.getVersion());
		}

		setContainerElement(FormManagerUtil
				.getComponentsContainerElement(getXformsDocument()));

		persistedFormForLazyLoading = persistedForm;
	}

	public void lazyLoadDocument() {
		lazyLoadDocument(null);
	}

	/**
	 * checks if document is loaded. If loadIfNotLoaded is true, then it loads
	 * the document if it isn't already
	 * 
	 * @param loadIfNotLoaded
	 */
	protected boolean isLazyLoaded(boolean loadIfNotLoaded) {

		if (!loaded && loadIfNotLoaded) {
			loadDocumentInternal(persistedFormForLazyLoading);
			loaded = true;
		}

		return loaded;
	}

	private void loadDocumentInternal(PersistedFormDocument persistedForm) {

		Document xformsXmlDoc = getXformsDocument();

		if (persistedForm != null) {
			setFormId(persistedForm.getFormId());
			setFormType(persistedForm.getFormType());
			setFormVersion(persistedForm.getVersion());
		}

		setContainerElement(FormManagerUtil
				.getComponentsContainerElement(xformsXmlDoc));
		loadContainerComponents();
		setProperties();

		loaded = true;
	}

	public LocalizedStringBean getFormTitle() {

		if (formTitle == null)
			formTitle = FormManagerUtil.getFormTitle(getXformsDocument());

		return formTitle;
	}

	public LocalizedStringBean getFormErrorMsg() {

		if (formErrorMsg == null)
			formErrorMsg = FormManagerUtil.getFormErrorMsg(getXformsDocument());

		return formErrorMsg;
	}

	public void setFormTitle(LocalizedStringBean formTitle) {

		if (formTitle != null) {

			FormManagerUtil.setFormTitle(getXformsDocument(), formTitle);
			this.formTitle = formTitle;
		} else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Tried to set form title, but no formTitle provided");
		}
	}

	public void setFormErrorMsg(LocalizedStringBean formError) {

		if (formError != null) {

			FormManagerUtil.setFormErrorMsg(getXformsDocument(), formError);
			this.formErrorMsg = formError;
		} else {
			Logger
					.getLogger(getClass().getName())
					.log(Level.WARNING,
							"Tried to set form error message, but no formError provided");
		}
	}

	public void rearrangeDocument() {
		rearrangeComponents();
	}

	@Override
	public void remove() {
		throw new NullPointerException("You shall not remove ME!!!!");
	}

	@Override
	public void componentsOrderChanged() {

		Map<String, FormComponent> contained_components = getContainedComponents();
		int components_amount = getContainedComponents().size();
		int i = 0;

		for (String comp_id : getContainedComponentsIds()) {

			FormComponentPage page = (FormComponentPage) contained_components
					.get(comp_id);
			if (page == null)
				throw new NullPointerException(
						"Component, which id was provided in list was not found. Provided: "
								+ getId());

			page
					.setPageSiblings(
							i == 0 ? null
									: (FormComponentPage) contained_components
											.get(getContainedComponentsIds()
													.get(i - 1)),
							(i + 1) == components_amount ? null
									: (FormComponentPage) contained_components
											.get(getContainedComponentsIds()
													.get(i + 1)));

			page.pagesSiblingsChanged();
			i++;
		}
		announceRegisteredForLastPage();
	}

	protected void announceRegisteredForLastPage() {

		for (FormComponent childPage : getContainedComponents().values()) {

			if (FormComponentFactory.page_type_thx.equals(childPage.getType())) {

				dispatchEvent(Event.thxPageIdChanged, childPage.getId(), this);
				break;
			}
		}
	}

	@Override
	public void rearrangeComponents() {

		List<String> contained_components_id_list = getContainedComponentsIds();
		int components_amount = contained_components_id_list.size();
		Map<String, FormComponent> contained_components = getContainedComponents();

		for (int i = components_amount - 1; i >= 0; i--) {

			String component_id = contained_components_id_list.get(i);

			if (contained_components.containsKey(component_id)) {

				FormComponentPage page = (FormComponentPage) contained_components
						.get(component_id);

				if (i != components_amount - 1) {

					page.setNextSiblingRerender(contained_components
							.get(contained_components_id_list.get(i + 1)));
				} else
					page.setNextSiblingRerender(null);

				page.setPageSiblings(i == 0 ? null
						: (FormComponentPage) contained_components
								.get(getContainedComponentsIds().get(i - 1)),
						(i + 1) == components_amount ? null
								: (FormComponentPage) contained_components
										.get(getContainedComponentsIds().get(
												i + 1)));
				page.pagesSiblingsChanged();

			} else
				throw new NullPointerException(
						"Component, which id was provided in list was not found. Provided: "
								+ component_id);
		}
	}

	@Override
	public void loadContainerComponents() {
		super.loadContainerComponents();
		componentsOrderChanged();
	}

	@Override
	public void setProperties() {

		ComponentPropertiesDocument properties = (ComponentPropertiesDocument) getProperties();

		if (properties == null)
			return;

		properties.setPlainStepsVisualizationUsed(getXFormsManager()
				.getIsStepsVisualizationUsed(this));
		properties.setPlainSubmissionAction(getXFormsManager()
				.getSubmissionAction(this));
	}

	public void save() throws Exception {

		PersistenceManager persistenceManager = getContext()
				.getPersistenceManager();

		if (persistenceManager == null)
			throw new NullPointerException("Persistence manager not set");

		PersistedFormDocument formDocument = persistenceManager.saveForm(this);
		setFormType(formDocument.getFormType());
		setFormId(formDocument.getFormId());
	}
	
	public void saveAllVersions(Long parentId) throws Exception {

		PersistenceManager persistenceManager = getContext()
				.getPersistenceManager();

		if (persistenceManager == null)
			throw new NullPointerException("Persistence manager not set");

		PersistedFormDocument formDocument = persistenceManager.saveAllVersions(this, parentId);
		setFormType(formDocument.getFormType());
		setFormId(formDocument.getFormId());
	}
	
	public void save(String storeBasePath) throws Exception {

		PersistenceManager persistenceManager = getContext()
				.getPersistenceManager();

		if (persistenceManager == null)
			throw new NullPointerException("Persistence manager not set");

		PersistedFormDocument formDocument = persistenceManager.saveForm(this, storeBasePath);
		setFormType(formDocument.getFormType());
		setFormId(formDocument.getFormId());
	}

	public Page getConfirmationPage() {

		return (Page) getFormConfirmationPage();
	}

	public FormComponentPage getFormConfirmationPage() {

		for (FormComponent childPage : getContainedComponents().values()) {

			if (FormComponentFactory.confirmation_page_type.equals(childPage
					.getType())) {
				return (FormComponentPage) childPage;
			}
		}

		return null;
	}

	protected List<String> getRegisteredForLastPageIdPages() {

		if (registeredForLastPageIdPages == null)
			registeredForLastPageIdPages = new ArrayList<String>();

		return registeredForLastPageIdPages;
	}

	public void registerForLastPage(String register_page_id) {

		if (!getContainedComponents().containsKey(register_page_id))
			throw new IllegalArgumentException(
					"I don't contain provided page id: " + register_page_id);

		if (!getRegisteredForLastPageIdPages().contains(register_page_id))
			getRegisteredForLastPageIdPages().add(register_page_id);
	}

	public Page addConfirmationPage() {

		if (getConfirmationPage() != null)
			throw new IllegalArgumentException(
					"Confirmation page already exists in the form");

		Page page = (Page) super.addComponent(
				FormComponentFactory.confirmation_page_type, null);

		componentsOrderChanged();
		addToConfirmationPage();

		return page;
	}

	public Element getAutofillModelElement() {

		return getXFormsManager().getAutofillAction(this);
	}

	public Element getFormDataModelElement() {

		return getXFormsManager().getFormDataModelElement(this);
	}

	public Element getFormMainDataInstanceElement() {

		return getXFormsManager().getFormMainDataInstanceElement(this);
	}

	public void clear() {

		setXformsDocument(null);
		lastComponentId = 0;
		setFormDocumentModified(true);

		getLocalizedComponentsDocuments().clear();
		setRegisteredForLastPageIdPages(null);
		formTitle = null;
		parametersManager = null;
		getContainedComponentsIds().clear();
		getContainedComponents().clear();
	}

	@Override
	public void unregisterComponent(String component_id) {

		super.unregisterComponent(component_id);
		getRegisteredForLastPageIdPages().remove(component_id);
	}

	@Override
	public PropertiesDocument getProperties() {

		if (properties == null) {
			ComponentPropertiesDocument properties = new ComponentPropertiesDocument();
			properties.setComponent(this);
			this.properties = properties;
		}

		return properties;
	}

	public Element getSectionsVisualizationInstanceElement() {

		return getXFormsManager().getSectionsVisualizationInstanceElement(this);
	}

	@Override
	public void update(ConstUpdateType what) {

		getXFormsManager().update(this, what, null);

		switch (what) {

		case STEPS_VISUALIZATION_USED:

			rearrangeComponents();
			break;

		case SUBMISSION_ACTION:

			break;

		default:
			break;
		}
	}

	public void setRegisteredForLastPageIdPages(
			List<String> registeredForLastPageIdPages) {
		this.registeredForLastPageIdPages = registeredForLastPageIdPages;
	}

	public ParametersManager getParametersManager() {

		if (parametersManager == null) {
			parametersManager = new ParametersManager();
			parametersManager.setFormDocumentComponent(this);
		}

		return parametersManager;
	}

	public Element getSubmissionElement() {
		// TODO: this can be put to component data bean and lazy loaded there
		return FormManagerUtil.getSubmissionElement(getXformsDocument());
	}

	public Element getSubmissionInstanceElement() {
		// TODO: this can be put to component data bean and lazy loaded there
		return FormManagerUtil
				.getFormSubmissionInstanceElement(getXformsDocument());
	}

	public FormVariablesHandler getFormVariablesHandler() {

		if (formVariablesHandler == null)
			formVariablesHandler = new FormVariablesHandler(getXformsDocument());

		return formVariablesHandler;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}
	
	public Integer getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Integer formVersion) {
		this.formVersion = formVersion;
	}

	public Map<Locale, Document> getLocalizedComponentsDocuments() {

		if (localizedComponentsDocuments == null)
			localizedComponentsDocuments = new HashMap<Locale, Document>(4);

		return localizedComponentsDocuments;
	}

	public boolean isReadonly() {
		return getXFormsManager().isReadonly(this);
	}

	public void setReadonly(boolean readonly) {

		getXFormsManager().setReadonly(this, readonly);
	}

	public void setPdfForm(boolean generatePdf) {

		getXFormsManager().setPdfForm(this, generatePdf);
	}

	public boolean isPdfForm() {

		return getXFormsManager().isPdfForm(this);
	}

	public DMContext getContext() {
		return context;
	}

	public void setContext(DMContext context) {
		this.context = context;
	}

	public Document getComponentsXforms() {
		return getContext().getCacheManager().getComponentsTemplate();
	}

	@Override
	public FormDocument getFormDocument() {
		return this;
	}

	@Override
	public void setFormDocument(FormDocument formDocument) {
		throw new UnsupportedOperationException(
				"setting form document for formdocument not supported");
	}

	public void setXformsDocument(Document xformsDocument) {
		this.xformsDocument = xformsDocument;
	}
}