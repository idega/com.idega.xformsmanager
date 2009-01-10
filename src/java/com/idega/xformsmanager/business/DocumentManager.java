package com.idega.xformsmanager.business;

import java.util.List;

import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;

import com.idega.idegaweb.IWMainApplication;
import com.idega.xformsmanager.business.component.ConstComponentCategory;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.datatypes.ComponentType;
import com.idega.xformsmanager.component.datatypes.ConstComponentDatatype;
import com.idega.xformsmanager.manager.impl.CacheManager;
import com.idega.xformsmanager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2009/01/10 12:33:27 $ by $Author: civilis $
 */
public interface DocumentManager {

	public abstract com.idega.xformsmanager.business.Document createForm(
			LocalizedStringBean formName, String formType);

	/**
	 * 
	 * @return List of available form components types by category
	 */
	public abstract List<String> getAvailableFormComponentsTypesList(
			ConstComponentCategory category);

	/**
	 * 
	 * @return List of available form components types by datatype
	 */
	public abstract List<ComponentType> getComponentsByDatatype(
			ConstComponentDatatype category);

	/**
	 * opens form by id, and loads all the structure. @see openFormLazy for lazy
	 * loading behavior
	 * 
	 * @param formId
	 * @return
	 */
	public abstract com.idega.xformsmanager.business.Document openForm(
			Long formId);

	/**
	 * opens form by id, but doesn't load all the structure. Loading is deferred
	 * to when necessary (lazy loading)
	 * 
	 * @param formId
	 * @return
	 */
	public abstract com.idega.xformsmanager.business.Document openFormLazy(
			Long formId);

	/**
	 * opens form by xforms xml, and loads all the structure. @see openFormLazy
	 * for lazy loading behavior
	 * 
	 * @param xformsDoc
	 * @return
	 */
	public abstract com.idega.xformsmanager.business.Document openForm(
			Document xformsDoc);

	/**
	 * used to fix the version of the form that is in flux state (not fixed
	 * state). In other words, it stores the copy of the form of id provided
	 * (formIdToTakeFrom), and generates new form id for it (for static version)
	 * 
	 * @param formIdToTakeFrom
	 *            id of the form to make static version from
	 * @return document of the fixed version form (static)
	 */
	public abstract com.idega.xformsmanager.business.Document takeForm(
			Long formIdToTakeFrom);

	public abstract void setPersistenceManager(
			PersistenceManager persistence_manager);

	public abstract void init(IWMainApplication iwma)
			throws InitializationException;

	public abstract boolean isInited();

	public abstract void setCacheManager(CacheManager cacheManager);

	public abstract void setTransformerService(
			TransformerService transformerService);

	public abstract void setComponentsXforms(Document componentsXforms);

	public abstract void setComponentsXsd(Document componentsXsd);

	public abstract void setFormXformsTemplate(Document formXformsTemplate);
}