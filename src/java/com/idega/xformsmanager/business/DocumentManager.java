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
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:34 $ by $Author: civilis $
 */
public interface DocumentManager {

	public abstract com.idega.xformsmanager.business.Document createForm(LocalizedStringBean formName, String formType);

	/**
	 * 
	 * @return List of available form components types by category
	 */
	public abstract List<String> getAvailableFormComponentsTypesList(ConstComponentCategory category);
	
	/**
	 * 
	 * @return List of available form components types by datatype
	 */
	public abstract List<ComponentType> getComponentsByDatatype(ConstComponentDatatype category);

	public abstract com.idega.xformsmanager.business.Document openForm(Long formId);
	
	public abstract com.idega.xformsmanager.business.Document openForm(Document xformsDoc);
	
	public abstract com.idega.xformsmanager.business.Document takeForm(Long formIdToTakeFrom);
	
	public abstract void setPersistenceManager(PersistenceManager persistence_manager);
	
	public abstract void init(IWMainApplication iwma) throws InitializationException;
	
	public abstract boolean isInited();
	
	public abstract void setCacheManager(CacheManager cacheManager);
	
	public abstract void setTransformerService(TransformerService transformerService);
	
	public abstract void setComponentsXforms(Document componentsXforms);
	
	public abstract void setComponentsXsd(Document componentsXsd);
	
	public abstract void setFormXformsTemplate(Document formXformsTemplate);
}