package com.idega.xformsmanager.form.impl;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.idega.xformsmanager.business.PersistedFormDocument;
import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.impl.FormDocumentImpl;
import com.idega.xformsmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/10/30 22:01:03 $ by $Author: civilis $
 */
@Service
@Scope("singleton")
public class Form {
	
//	protected static Logger logger = Logger.getLogger(Form.class.getName());
	
//	protected FormDocumentImpl formDocument;
	
//	private Locale defaultDocumentLocale;
//	private Map<String, FormComponent> formComponents;
//	private int lastComponentId = 0;
//	private boolean documentChanged = true;
//	private DocumentBuilder builder;
//	private DMContext context;
	
	/*
	public Form(DMContext context) {
		
		formDocument = new FormDocumentImpl();
		formDocument.setContext(context);
		formDocument.setForm(this);
//		formDocument.setFormDocument(formDocument);
	}
	*/
	
	private FormDocumentImpl createFormDocument() {
		
		return new FormDocumentImpl();
	}
	
	public com.idega.xformsmanager.business.Document createDocument(LocalizedStringBean formTitle, DMContext context, String formType) {

//		Form form = new Form(context);
//		form.setContext(context);
//		form.formDocument.setLoad(true);
		
		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setXformsDocument(context.getCacheManager().getFormXformsTemplateCopy());
		formDocument.setContext(context);
		
//		context.setXformsXmlDoc(context.getCacheManager().getFormXformsTemplateCopy());
		formDocument.loadDocument();
//		loadDocumentInternal(formDocument, null);
		
		LocalizedStringBean formDocumentTitle = formDocument.getFormTitle();
		for (Locale currentLocale : formTitle.getLanguagesKeySet()) 
			formDocumentTitle.setString(currentLocale, formTitle.getString(currentLocale));
		
		Locale currentDefaultLocale = formTitle.getLanguagesKeySet().iterator().next();
		formDocument.setDefaultLocale(currentDefaultLocale);
		formDocument.setFormTitle(formDocumentTitle);
		
//		form.formDocument.setFormId(null);
		formDocument.setFormType(formType);

		return formDocument;
	}
	
//	public com.idega.xformsmanager.business.Document getDocument() {
//		
//		return formDocument;
//	}
	
//	public String generateNewComponentId() {
//		
//		if(lastComponentId == 0) {
//			
//			Set<String> allIds = FormManagerUtil.getAllComponentsIds(formDocument.getXformsDocument());
//			
//			for (String id : allIds) {
//				int idNr = FormManagerUtil.parseIdNumber(id);
//				
//				if(idNr > lastComponentId)
//					lastComponentId = idNr;
//			}
//		}
//		
//		return FormManagerUtil.CTID+(++lastComponentId);
//	}
	
//	protected Map<String, FormComponent> getFormComponents() {
//		
//		if(formComponents == null)
//			formComponents = new HashMap<String, FormComponent>();
//		
//		return formComponents;
//	}
//	
//	public void setFormDocumentModified(boolean changed) {
//		documentChanged = changed;
//	}
//	
//	public boolean isFormDocumentModified() {
//		return documentChanged;
//	}
//	public Document getFormXFormsDocumentClone() {
//		return (Document)getContext().getXformsXmlDoc().cloneNode(true);
//	}
	
//	TODO: perhaps move to formdocument
//	public static void loadDocumentInternal(FormDocumentImpl formDocument, PersistedFormDocument persistedForm) {
//		
////		Document xformsXmlDoc = getContext().getXformsXmlDoc();
//		Document xformsXmlDoc = formDocument.getXformsDocument();
//		
//		if(persistedForm != null) {
//			formDocument.setFormId(persistedForm.getFormId());
//			formDocument.setFormType(persistedForm.getFormType());
//			
//		} else {
//			
//			try {
//				formDocument.setFormId(new Long(FormManagerUtil.getFormId(xformsXmlDoc)));
//			} catch (Exception e) {
//				formDocument.setFormId(null);
//			}
//		}
//		
//		formDocument.setContainerElement(FormManagerUtil.getComponentsContainerElement(xformsXmlDoc));
//		formDocument.loadContainerComponents();
//		formDocument.setProperties();
//	}
	
	public com.idega.xformsmanager.business.Document loadDocument(Long formId, DMContext context) {
		
		if(formId == null)
			throw new NullPointerException("Form id was not provided");
		
		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(context);
		
//		Form form = new Form(context);
//		form.setContext(context);
//		form.formDocument.setLoad(true);
		
		PersistedFormDocument persistedFormDocument = loadPersistedFormDocument(context, formId);

		formDocument.setXformsDocument(persistedFormDocument.getXformsDocument());
//		context.setXformsXmlDoc(persistedFormDocument.getXformsDocument());
//		loadDocumentInternal(formDocument, persistedFormDocument);
		formDocument.loadDocument(persistedFormDocument);
		
		return formDocument;
	}
	
	public com.idega.xformsmanager.business.Document takeAndLoadDocument(Long formIdToTakeFrom, DMContext context) {
		
		if(formIdToTakeFrom == null)
			throw new NullPointerException("Form id was not provided");
		
		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(context);
//		Form form = new Form(context);
//		form.setContext(context);
//		form.formDocument.setLoad(true);
		
		PersistedFormDocument persistedFormDocument = takeAndloadFormDocument(context, formIdToTakeFrom);

		formDocument.setXformsDocument(persistedFormDocument.getXformsDocument());
//		context.setXformsXmlDoc(persistedFormDocument.getXformsDocument());
//		loadDocumentInternal(formDocument, persistedFormDocument);
		formDocument.loadDocument(persistedFormDocument);
		
		return formDocument;
	}
	
	public com.idega.xformsmanager.business.Document loadDocument(Document xformsXmlDoc, DMContext context) {
		
		FormDocumentImpl formDocument = createFormDocument();
		formDocument.setContext(context);
		formDocument.setXformsDocument(xformsXmlDoc);
//		loadDocumentInternal(formDocument, null);
		formDocument.loadDocument();
		
//		Form form = new Form(context);
//		form.formDocument.setLoad(true);
//		form.setContext(context);
//		context.setXformsXmlDoc(xformsXmlDoc);
		
//		form.loadDocumentInternal(null);
//		form.formDocument.setFormId(null);
		
		return formDocument;
	}
	
	/*
	public static Form loadDocument(Long formId, Document xformsXmlDoc, DMContext context) throws InitializationException, Exception {
		
		Form form = loadDocument(xformsXmlDoc, context);
		form.formDocument.setFormId(formId);
		
		return form;
	}
	
	public static Form loadDocumentAndGenerateId(Document xformsXmlDoc, DMContext context) throws InitializationException, Exception {
		
		if(true)
			throw new UnsupportedOperationException();
		Form form = loadDocument(xformsXmlDoc, context);
		
		String defaultFormName = form.getDocument().getFormTitle().getString(form.getDefaultLocale());
		//form.formDocument.setFormId(context.getPersistenceManager().generateFormId(defaultFormName));
		
		return form;
	}
	*/
	
	protected PersistedFormDocument loadPersistedFormDocument(DMContext context, Long formId) {
		
		PersistenceManager persistenceManager = context.getPersistenceManager();
		return persistenceManager.loadForm(formId);
	}
	
	protected PersistedFormDocument takeAndloadFormDocument(DMContext context, Long formIdToTakeFrom) {
		
		PersistenceManager persistenceManager = context.getPersistenceManager();
		return persistenceManager.takeForm(formIdToTakeFrom);
	}
	
//	public String getXFormsDocumentSourceCode() throws Exception {
//		
//		return FormManagerUtil.serializeDocument(getContext().getXformsXmlDoc());
//	}
	/*
	public void setXFormsDocumentSourceCode(String srcXml) throws Exception {
		
		if(builder == null)
			builder = XmlUtil.getDocumentBuilder();
		
		clear();
//		formDocument.setLoad(true);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(srcXml.getBytes("UTF-8"));
		Document xformsXmlDoc = builder.parse(new InputSource(bais));
		getContext().setXformsXmlDoc(xformsXmlDoc);
		loadDocumentInternal(null);
	}
	*/
	
//	public Locale getDefaultLocale() {
//		
//		if(defaultDocumentLocale == null)
//			defaultDocumentLocale = FormManagerUtil.getDefaultFormLocale(getContext().getXformsXmlDoc());
//		
//		return defaultDocumentLocale;
//	}
	
//	public void tellComponentId(String component_id) {
//		
//		int id_number = FormManagerUtil.parseIdNumber(component_id);
//		
//		if(id_number > lastComponentId)
//			lastComponentId = id_number;
//	}
	
//	public void clear() {
//		
//		getContext().setXformsXmlDoc(null);
//		defaultDocumentLocale = null;
//		formComponents = null;
//		lastComponentId = 0;
//		documentChanged = true;
//		formDocument.clear();
//		formDocument.setForm(this);
//		formDocument.setFormDocument(formDocument);
//	}
//	
//	public DMContext getContext() {
//		return context;
//	}
//
//	public void setContext(DMContext context) {
//		this.context = context;
//	}
}