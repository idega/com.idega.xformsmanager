package com.idega.xformsmanager.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;

import org.chiba.xml.xslt.TransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.idega.core.business.DefaultSpringBean;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.util.xml.XmlUtil;
import com.idega.xformsmanager.manager.impl.CacheManager;
import com.idega.xformsmanager.manager.impl.FormManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2009/06/05 15:06:19 $ by $Author: eiki $
 */
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DocumentManagerFactory extends DefaultSpringBean {

	private DocumentManager documentManager;

	public static final String COMPONENTS_XFORMS_CONTEXT_PATH = "resources/templates/form-components.xhtml";
	public static final String COMPONENTS_XSD_CONTEXT_PATH = "resources/templates/default-components.xsd";
	public static final String FORM_XFORMS_TEMPLATE_RESOURCES_PATH = "resources/templates/form-template.xhtml";

	@Autowired private CacheManager cacheManager;

//	just any object will do here. could be "this", but what about volatile?`
	private volatile Lock lock = new ReentrantLock();

	public CacheManager getCacheManager() {
		return cacheManager;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	public DocumentManagerFactory() { }

	public DocumentManager newDocumentManager(IWMainApplication iwma) {

		DocumentManager documentManager = getDocumentManager();

		if(!documentManager.isInited()) {

			synchronized (lock) {

				if(!documentManager.isInited()) {

					if(iwma == null) {

						IWContext iwc = IWContext.getCurrentInstance();

						if(iwc != null)
							iwma = iwc.getIWMainApplication();
						else
							iwma = IWMainApplication.getDefaultIWMainApplication();
					}

					TransformerService transformerService = (TransformerService)iwma.getAttribute(TransformerService.class.getName());

					CacheManager cacheManager = getCacheManager();
					cacheManager.initAppContext(iwma);

					IWBundle bundle = iwma.getBundle(FormManager.IW_BUNDLE_IDENTIFIER);

					try {
						Document componentsXforms = getDocumentFromBundle(iwma, bundle, COMPONENTS_XFORMS_CONTEXT_PATH);
						Document componentsXsd = getDocumentFromBundle(iwma, bundle, COMPONENTS_XSD_CONTEXT_PATH);
						Document formXformsTemplate = getDocumentFromBundle(iwma, bundle, FORM_XFORMS_TEMPLATE_RESOURCES_PATH);

						//check for custom components, xsd, default form stuff
						String componentsOverridePath = iwma.getSettings().getProperty("FB.components.def.path", "");
						String componentsXSDOverridePath = iwma.getSettings().getProperty("FB.components.xsd.path", "");
						String defaultFormTemplateOverridePath = iwma.getSettings().getProperty("FB.default.form.path", "");

						if(componentsOverridePath.length()!=0 && getRepositoryService().getExistence(componentsOverridePath)){
							componentsXforms = getDocumentFromSlide(componentsOverridePath);
						}
						else{
							componentsXforms = getDocumentFromBundle(iwma, bundle, COMPONENTS_XFORMS_CONTEXT_PATH);
						}

						if(componentsXSDOverridePath.length()!=0 && getRepositoryService().getExistence(componentsXSDOverridePath)){
							componentsXforms = getDocumentFromSlide(componentsXSDOverridePath);

						}
						else {
							componentsXsd = getDocumentFromBundle(iwma, bundle, COMPONENTS_XSD_CONTEXT_PATH);
						}

						if(defaultFormTemplateOverridePath.length()!=0 && getRepositoryService().getExistence(defaultFormTemplateOverridePath)){
							formXformsTemplate = getDocumentFromSlide(defaultFormTemplateOverridePath);
						}
						else {
							formXformsTemplate = getDocumentFromBundle(iwma, bundle, FORM_XFORMS_TEMPLATE_RESOURCES_PATH);
						}



						documentManager.setComponentsXforms(componentsXforms);
						documentManager.setComponentsXsd(componentsXsd);
						documentManager.setFormXformsTemplate(formXformsTemplate);
						documentManager.setCacheManager(cacheManager);
						documentManager.setTransformerService(transformerService);
						documentManager.init(iwma);

					} catch (Exception e) {
						throw new RuntimeException("Failed to initialize document manager", e);
					}
				}
			}
		}

		return documentManager;
	}

	public DocumentManager getDocumentManager() {
		return documentManager;
	}

	@Autowired
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}

	private Document getDocumentFromBundle(IWMainApplication iwma, IWBundle bundle, String pathWithinBundle) throws Exception {
		Document doc = null;
		InputStream stream = getResourceInputStream(iwma, bundle, pathWithinBundle);

		DocumentBuilder docBuilder = XmlUtil.getDocumentBuilder();
		doc = docBuilder.parse(stream);
		stream.close();

		return doc;
	}

	private Document getDocumentFromSlide(String pathToFile) throws Exception {
		Document doc = null;
		InputStream stream = getRepositoryService().getInputStream(pathToFile);

		DocumentBuilder docBuilder = XmlUtil.getDocumentBuilder();
		doc = docBuilder.parse(stream);
		stream.close();

		return doc;
	}

	private InputStream getResourceInputStream(IWMainApplication iwma, IWBundle bundle, String pathWithinBundle) throws IOException {

//
//		NOT SOMETHING 	we used
//		InputStream stream = null;
//		IWMainApplication app = bundle.getApplication();
//		try {
//			stream = new FileInputStream(IWBundleResourceFilter.copyResourceFromJarToWebapp(app, bundle.getResourcesPath() + pathWithinBundle));
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

//		String workspaceDir = System.getProperty(DefaultIWBundle.SYSTEM_BUNDLES_RESOURCE_DIR);
//
//		if(workspaceDir != null) {
//
//			String bundleInWorkspace = new StringBuilder(workspaceDir).append(CoreConstants.SLASH).append(FormManager.IW_BUNDLE_IDENTIFIER).append(CoreConstants.SLASH).toString();
//			return new FileInputStream(bundleInWorkspace + pathWithinBundle);
//		}

		return bundle.getResourceInputStream(pathWithinBundle);
	}
}