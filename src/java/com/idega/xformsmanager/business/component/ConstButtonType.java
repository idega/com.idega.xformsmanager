package com.idega.xformsmanager.business.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2009/06/05 10:13:25 $ by $Author: valdas $
 */
public enum ConstButtonType {

	PREVIOUS_PAGE_BUTTON {
		@Override
		public String toString() {
			return "fbc_button_previous";
		}
	},
	NEXT_PAGE_BUTTON {
		@Override
		public String toString() {
			return "fbc_button_next";
		}
	},
	SUBMIT_FORM_BUTTON {
		@Override
		public String toString() {
			return "fbc_button_submit";
		}
	},
	SAVE_FORM_BUTTON {
		@Override
		public String toString() {
			return "fbc_button_save";
		}
	},
	GET_FORM_AS_PDF_BUTTON {
		@Override
		public String toString() {
			return "fbc_button_get_as_pdf";
		}
	};
	// RESET_FORM_BUTTON {public String toString() { return "fbc_button_reset";
	// }};

	public static Set<String> getAllTypesInStrings() {

		return getAllStringTypesEnumsMappings().keySet();
	}

	private static Map<String, ConstButtonType> allStringTypesEnumsMappings;

	private synchronized static Map<String, ConstButtonType> getAllStringTypesEnumsMappings() {

		if (allStringTypesEnumsMappings == null) {

			allStringTypesEnumsMappings = new HashMap<String, ConstButtonType>();

			for (ConstButtonType type : values())
				allStringTypesEnumsMappings.put(type.toString(), type);
		}

		return allStringTypesEnumsMappings;
	}

	public static ConstButtonType getByStringType(String type) {

		return getAllStringTypesEnumsMappings().get(type);
	}

	@Override
	public abstract String toString();
}