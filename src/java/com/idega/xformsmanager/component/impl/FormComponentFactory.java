package com.idega.xformsmanager.component.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.xformsmanager.business.component.ConstButtonType;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.manager.impl.FormManager;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 *
 * Last modified: $Date: 2008/11/22 09:48:04 $ by $Author: arunas $
 */
@Service
@Scope("singleton")
public class FormComponentFactory {
	
	private Map<String, List<String>> components_tags_classified;
	private static final String type_simple = "type_simple";
	private static final String type_select = "type_select";
	private static final String type_non_display = "type_non_display";
	private static final String type_plain = "type_plain";
	private static final String type_upload = "type_upload";
	private static final String type_page = "type_page";
	
	public static final String page_type_tag = FormManagerUtil.idegans_case_tag;
	public static final String page_type = "fbc_page";
	public static final String confirmation_page_type = "fbc_confirmation_page";
	public static final String button_type = FormManagerUtil.trigger_tag;
	public static final String fbc_button_area = "fbc_button_area";
	public static final String page_type_thx = "thx_page";
	
	private FormManager formManager;
	
	private FormComponentFactory() { 
		
		components_tags_classified = new HashMap<String, List<String>>();
		
		List<String> types = new ArrayList<String>();
		
		types.add("fbc_autofill_name");
		types.add("fbc_autofill_first_name");
		types.add("fbc_autofill_last_name");
		types.add("fbc_autofill_personal_id");
		types.add("fbc_autofill_address");
		types.add("fbc_autofill_phones");
		types.add("fbc_autofill_emails");		
		types.add("fbc_autofill_country");
		types.add("fbc_autofill_province");
		types.add("fbc_autofill_city");
		types.add("fbc_autofill_commune");
		types.add("fbc_autofill_postal_code");
		types.add("fbc_identifier_number");
		types.add("fbc_text");
		types.add("fbc_date");
		types.add("fbc_text_output");
		types.add("fbc_textarea");
		types.add("fbc_secret");
		types.add("fbc_email");
		types.add("fbc_upload");
		types.add("xf:input");
		types.add("xf:secret");
		types.add("xf:textarea");
		components_tags_classified.put(type_simple, types);
		
		List<String> non_display_types = new ArrayList<String>();
		non_display_types.add(fbc_button_area);
		non_display_types.add(page_type);
		non_display_types.add(confirmation_page_type);
		components_tags_classified.put(type_non_display, non_display_types);
		
		types = new ArrayList<String>();
		
		types.add("fbc_multiple_select_minimal");
		types.add("xf:select");
		types.add("fbc_single_select_minimal");
		types.add("xf:select1");
		types.add("fbc_multiple_select");
		types.add("fbc_single_select");
		
		components_tags_classified.put(type_select, types);
		
		types = new ArrayList<String>();
		types.add("fbc_static_output");
		types.add("fbc_simple_text");
		types.add("fbc_header_text");
		types.add("fbc_separator");
		
		components_tags_classified.put(type_plain, types);
		
		types = new ArrayList<String>();
		types.add("fbc_multiupload");
		components_tags_classified.put(type_upload, types);
		
		types = new ArrayList<String>();
		types.add(page_type_tag);
		types.add(page_type);
		types.add(confirmation_page_type);
		types.add(page_type_thx);
		types.add("save_page");
		components_tags_classified.put(type_page, types);
	}
	
	public FormComponent getFormComponentByType(String componentType, boolean loadFromTemplate) {
		
		FormComponent component = recognizeFormComponent(componentType);
		component.setType(componentType);
		
		if(loadFromTemplate) {
		
			component.setFormDocument(getFormManager().getFormDocumentTemplate());
			component.loadFromTemplate();
		}
		
		return component;
	}
	
	public FormComponent recognizeFormComponent(String componentType) {
		
		if(components_tags_classified.get(type_upload).contains(componentType))
			return new FormComponentMultiUploadImpl();
		if(components_tags_classified.get(type_select).contains(componentType))
			return new FormComponentSelectImpl();
		if(components_tags_classified.get(type_page).contains(componentType))
			return new FormComponentPageImpl();
		if(componentType.equals(fbc_button_area))
			return new FormComponentButtonAreaImpl();
		if(componentType.equals(button_type) || ConstButtonType.getAllTypesInStrings().contains(componentType))
			return new FormComponentButtonImpl();
		if(components_tags_classified.get(type_plain).contains(componentType))
			return new FormComponentPlainImpl();
		
		
		return new FormComponentImpl();
	}
	
	public boolean isNormalFormElement(FormComponent form_component) {
		
		String type = form_component.getType();
		return 
			components_tags_classified.get(type_select).contains(type) ||
			components_tags_classified.get(type_simple).contains(type);
	}
	
	public void filterNonDisplayComponents(List<String> all_components_types) {
		
		List<String> non_disp_types = components_tags_classified.get(type_non_display);
		
		for (Iterator<String> iter = all_components_types.iterator(); iter.hasNext();) {
			
			if(non_disp_types.contains(iter.next()))
				iter.remove();
		}
	}

	FormManager getFormManager() {
		return formManager;
	}

	public void setFormManager(FormManager formManager) {
		this.formManager = formManager;
	}
}