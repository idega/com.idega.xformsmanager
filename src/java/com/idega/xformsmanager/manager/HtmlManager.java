package com.idega.xformsmanager.manager;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.xformsmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:34 $ by $Author: civilis $
 */
public interface HtmlManager {

	public abstract Element getHtmlRepresentation(FormComponent component, Locale locale)
			throws Exception;

	public abstract void clearHtmlComponents(FormComponent component);
}