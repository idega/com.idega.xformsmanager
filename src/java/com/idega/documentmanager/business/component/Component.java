package com.idega.documentmanager.business.component;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:16 $ by $Author: civilis $
 */
public interface Component {

	public abstract String getId();
	
	public abstract Element getHtmlRepresentation(Locale locale) throws Exception;
	
	public abstract PropertiesComponent getProperties();
	
	public abstract void remove();
	
	public abstract String getType();
	
//	public Element getDefaultHtmlRepresentationByType(String component_type);
}