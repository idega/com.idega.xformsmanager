package com.idega.xformsmanager.component.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/27 10:27:36 $ by $Author: civilis $
 */
public class LocalizedStringBean {

	private static final Logger LOGGER = Logger.getLogger(LocalizedStringBean.class.getName());

	private Map<Locale, String> strings;

	public LocalizedStringBean() {
		strings = new HashMap<Locale, String>();
	}

	public LocalizedStringBean(String defaultString) {
		strings = new HashMap<Locale, String>();
		setString(new Locale(Locale.ENGLISH.toString()), defaultString);
	}

	public Set<Locale> getLanguagesKeySet() {
		return strings.keySet();
	}

	/**
	 * if You don't want to change the text, provide text value as null
	 *
	 * @param locale
	 * @param text
	 */
	public void setString(Locale locale, String text) {
		if (locale == null)
			throw new IllegalArgumentException("Locale is not provided");

		strings.put(locale, text);
	}

	public String getString(Locale locale) {
		if (!strings.containsKey(locale)) {
			LOGGER.warning("There are no labels for locale: " + locale + ". Labels exist for locale(s): " + strings.keySet());
		}

		return strings.get(locale);
	}

	public void removeString(Locale locale) {
		strings.remove(locale);
	}

	public void clear() {
		strings.clear();
	}

	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer("LocalizedStringBean:");

		for (Iterator<Locale> iter = strings.keySet().iterator(); iter.hasNext();) {
			Locale locale = iter.next();

			toString.append("\nlocale: ")
			.append(locale.toString())
			.append(" value: ")
			.append(strings.get(locale));
		}

		return toString.toString();
	}
}