package com.idega.xformsmanager.manager.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.util.StringUtil;
import com.idega.util.xml.XPathUtil;
import com.idega.xformsmanager.business.component.properties.PropertiesMultiUpload;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentType;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.beans.ComponentMultiUploadBean;
import com.idega.xformsmanager.component.beans.ErrorStringBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerMultiUpload;
import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.xform.Bind;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.10 $
 * 
 *          Last modified: $Date: 2008/11/06 17:29:32 $ by $Author: civilis $
 */
@FormComponentType(FormComponentType.multiupload)
@Service
@Scope("singleton")
public class XFormsManagerMultiUploadImpl extends XFormsManagerImpl implements
		XFormsManagerMultiUpload {

	private static final String INSTANCE = "instance('data-instance')/Multi_file_upload_with_description_";
	private static final String REPEAT = "upload_entries_";
	private static final String ENTRY = "/entry";
	private static final String AT_START = "index('";
	private static final String AT_END = "')";

	private static final String APPEND_TITLE = ".title";
	private static final String APPEND_REMOVE = ".remove";
	private static final String APPEND_LABEL = ".label";
	private static final String APPEND_DESCRIPTION = ".text";
	private static final String APPEND_NAME = ".name";

	private static final int TITLE_LABEL = 0;
	private static final int INSERT_BUTTON_LABEL = 1;
	private static final int DESCRIPTION_LABEL = 2;
	private static final int UPLOADING_FILE_DESC = 3;
	private static final int REMOVE_BUTTON_LABEL = 4;

	private final XPathUtil labelsXPathUT = new XPathUtil(".//xf:label");
	final private XPathUtil insertXPUT = new XPathUtil(
			".//xf:insert[@at='last()']");
	final private XPathUtil repeatXPUT = new XPathUtil(".//xf:repeat");
	final private XPathUtil deleteXPUT = new XPathUtil(".//xf:delete");
	final private XPathUtil bindXPUT = new XPathUtil(
			".//xf:bind[@id='multiupload']");
	final private XPathUtil outputXPUT = new XPathUtil(".//xf:output[@value]");

	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentMultiUploadBean();
	}

	/*
	 * @Override protected void loadXFormsComponentDataBean(FormComponent
	 * component, Document xform, Element componentElement) {
	 * super.loadXFormsComponentDataBean(component, xform, componentElement);
	 * XPathUtil util = new
	 * XPathUtil(".//xf:bind[@id='bind."+componentElement.getAttribute
	 * (FormManagerUtil.id_att)+"']");
	 * 
	 * Element bindElement = (Element) util.getNode(xform.getFirstChild());
	 * 
	 * Bind bind = Bind.load(bindElement); if (bind != null){
	 * ComponentMultiUploadBean xforms_component = (ComponentMultiUploadBean)
	 * component.getXformsComponentDataBean(); xforms_component.setBind(bind); }
	 * }
	 */

	@Override
	public void removeComponentFromXFormsDocument(FormComponent component) {

		ComponentMultiUploadBean xforms_component = (ComponentMultiUploadBean) component
				.getComponentDataBean();
		Element data_src_element = xforms_component.getMultiUploadInstance();

		if (data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);

		super.removeComponentFromXFormsDocument(component);
	}

	// @Override
	// public void loadComponentFromTemplate(FormComponent component) {
	// super.loadComponentFromTemplate(component);
	// }

	public static final XPathUtil uploadRepeatElementXPath = new XPathUtil(
			".//xf:repeat");

//	@Override
//	protected void loadBindsAndNodesets(FormComponent component) {
//		ComponentDataBean xformsComponentDataBean = component
//				.getComponentDataBean();
//
//		Element repeatElement = uploadRepeatElementXPath
//				.getNode(xformsComponentDataBean.getElement());
//
//		String bindId = repeatElement.getAttribute(FormManagerUtil.bind_att);
//
//		Bind bind = Bind.locate(component, bindId);
//
//		if (bind == null)
//			throw new NullPointerException("Binding not found by bind id: "
//					+ bindId + " in the form "
//					+ component.getFormDocument().getId());
//
//		xformsComponentDataBean.setBind(bind);
//	}

	@Override
	public void addComponentToDocument(FormComponent component) {

		super.addComponentToDocument(component);
		updateNodesetReferencingAttributes(component);
	}
	
	private final XPathUtil insertElementXPath = new XPathUtil(".//xf:insert[@at]");
	private final XPathUtil deleteElementXPath = new XPathUtil(".//xf:delete[@at]");
	
	private void updateNodesetReferencingAttributes(FormComponent component) {
		
		String nodesetName = component.getComponentDataBean().getBind().getNodeset().getNodesetElement().getNodeName();
		
		Element insertElement = insertElementXPath.getNode(component.getComponentDataBean().getElement());
		Element deleteElement = deleteElementXPath.getNode(component.getComponentDataBean().getElement());
		
		String insertNodeset = insertElement.getAttribute(FormManagerUtil.nodeset_att);
		
		insertNodeset = "instance('data-instance')/"+nodesetName+"/entry";
		
		insertElement.setAttribute(FormManagerUtil.nodeset_att, insertNodeset);
		deleteElement.setAttribute(FormManagerUtil.nodeset_att, insertNodeset);
	}
	
	@Override
	public void bindsRenamed(FormComponent component) {
		
		super.bindsRenamed(component);
		updateNodesetReferencingAttributes(component);
	}

	// private String constructInsertNodeset(FormComponent component) {
	//
	// StringBuffer buf = new StringBuffer();
	//
	// buf.append(INSTANCE).append(component.getId()).append(ENTRY);
	// return buf.toString();
	//
	// }
	//
	// private String constructBindValue(String component_id) {
	//
	// StringBuffer buf = new StringBuffer();
	// buf.append("upload_").append(component_id);
	// return buf.toString();
	//
	// }
	//
	// private String constructRepeatId(String component_id) {
	// StringBuffer buf = new StringBuffer();
	// buf.append(REPEAT).append(component_id);
	// return buf.toString();
	// }
	//
	// private String constructDeleteAt(String component_id) {
	// StringBuffer buf = new StringBuffer();
	// buf.append(AT_START).append(REPEAT).append(component_id).append(AT_END);
	// return buf.toString();
	// }
	//
	// private String constructOutputValueAt(String component_id) {
	//
	// StringBuffer buf = new StringBuffer();
	// buf
	// .append(
	// "if(. !='' and ./description ='', instance('localized_strings')/")
	// .append(component_id)
	// .append(
	// ".info[@lang=instance('localized_strings')/current_language], '')");
	// return buf.toString();
	//
	// }

	@Override
	public void update(FormComponent component, ConstUpdateType what,
			Object prop) {

		super.update(component, what, prop);

		switch (what) {

		case ADD_BUTTON_LABEL:
			updateAddButtonLabel(component);
			break;

		case REMOVE_BUTTON_LABEL:
			updateRemoveButtonLabel(component);
			break;
		case DESCRIPTION_BUTTON_LABEL:
			updateDescriptionButtonLabel(component);
			break;
		case UPLOADING_FILE_DESC:
			updateUploadingFileDescription(component);
			break;
		default:
			break;
		}
	}

	private NodeList getLabelNodeList(FormComponent component) {
		return labelsXPathUT.getNodeset(component.getComponentDataBean()
				.getElement());
	}

	protected void updateAddButtonLabel(FormComponent component) {

		PropertiesMultiUpload properties = (PropertiesMultiUpload) component
				.getProperties();
		LocalizedStringBean localizedText = properties.getInsertButtonLabel();
		NodeList labels = getLabelNodeList(component);

		Element addButtonlabel = (Element) labels.item(INSERT_BUTTON_LABEL);

		// String ref = addButtonlabel.getAttribute(FormManagerUtil.ref_s_att);

		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
				addButtonlabel,
				component.getFormDocument().getXformsDocument(), localizedText);

		// FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att,
		// !StringUtil
		// .isEmpty(ref) ? null : new StringBuilder(component.getId())
		// .append(APPEND_LABEL).toString(), null, addButtonlabel,
		// component.getFormDocument().getXformsDocument(), localizedText);
	}

	protected void updateRemoveButtonLabel(FormComponent component) {

		PropertiesMultiUpload properties = (PropertiesMultiUpload) component
				.getProperties();
		LocalizedStringBean localizedText = properties.getRemoveButtonLabel();
		NodeList labels = getLabelNodeList(component);

		Element removeButtonlabel = (Element) labels.item(REMOVE_BUTTON_LABEL);
//		String ref = removeButtonlabel.getAttribute(FormManagerUtil.ref_s_att);
		
		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
				removeButtonlabel,
				component.getFormDocument().getXformsDocument(), localizedText);

//		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, !StringUtil
//				.isEmpty(ref) ? null : new StringBuilder(component.getId())
//				.append(APPEND_REMOVE).toString(), null, removeButtonlabel,
//				component.getFormDocument().getXformsDocument(), localizedText);
	}

	protected void updateUploadingFileDescription(FormComponent component) {
		PropertiesMultiUpload properties = (PropertiesMultiUpload) component
				.getProperties();
		LocalizedStringBean localizedText = properties
				.getUploadingFileDescription();
		NodeList labels = getLabelNodeList(component);

		Element label = (Element) labels.item(UPLOADING_FILE_DESC);
		
		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
				label,
				component.getFormDocument().getXformsDocument(), localizedText);
		
//		String ref = label.getAttribute(FormManagerUtil.ref_s_att);
//
//		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, !StringUtil
//				.isEmpty(ref) ? null : new StringBuilder(component.getId())
//				.append(APPEND_NAME).toString(), null, label, component
//				.getFormDocument().getXformsDocument(), localizedText);
	}

	protected void updateDescriptionButtonLabel(FormComponent component) {

		PropertiesMultiUpload properties = (PropertiesMultiUpload) component
				.getProperties();
		LocalizedStringBean localizedText = properties.getDescriptionLabel();
		NodeList labels = getLabelNodeList(component);

		Element descriptionButtonlabel = (Element) labels
				.item(DESCRIPTION_LABEL);
		
		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
				descriptionButtonlabel,
				component.getFormDocument().getXformsDocument(), localizedText);
		
//		String ref = descriptionButtonlabel
//				.getAttribute(FormManagerUtil.ref_s_att);
//
//		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, !StringUtil
//				.isEmpty(ref) ? null : new StringBuilder(component.getId())
//				.append(APPEND_DESCRIPTION).toString(), null,
//				descriptionButtonlabel, component.getFormDocument()
//						.getXformsDocument(), localizedText);
	}

	// @Override
	// protected void updateLabel(FormComponent component) {
	//		
	// PropertiesMultiUpload properties = (PropertiesMultiUpload) component
	// .getProperties();
	// LocalizedStringBean localizedText = properties.getLabel();
	//		
	// NodeList labels = getLabelNodeList(component);
	// Element title = (Element) labels.item(TITLE_LABEL);
	// String ref = title.getAttribute(FormManagerUtil.ref_s_att);
	//
	// FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, !StringUtil
	// .isEmpty(ref) ? null : new StringBuilder(component.getId())
	// .append(APPEND_TITLE).toString(), null, title, component
	// .getFormDocument().getXformsDocument(), localizedText);
	// }

	@Override
	protected void updateErrorMsg(FormComponent component,
			ErrorStringBean errString) {

		ComponentDataBean xformsComponentDataBean = component
				.getComponentDataBean();

		PropertiesMultiUpload properties = (PropertiesMultiUpload) component
				.getProperties();

		Element output = (Element) outputXPUT.getNode(xformsComponentDataBean
				.getElement());

		// TODO: FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, new
		// StringBuilder(component.getId()).append(".info").toString(),
		// FormManagerUtil.localized_entries, output,
		// component.getFormDocument().getXformsDocument(),
		// properties.getErrorMsg());
		output.removeAttribute(FormManagerUtil.ref_s_att);

	}

	public LocalizedStringBean getInsertButtonLabel(FormComponent component) {

		NodeList labels = getLabelNodeList(component);

		Element insertLabelElement = (Element) labels.item(INSERT_BUTTON_LABEL);

		return FormManagerUtil.getElementLocalizedStrings(insertLabelElement,
				component.getFormDocument().getXformsDocument());
	}

	public LocalizedStringBean getRemoveButtonLabel(FormComponent component) {

		NodeList labels = getLabelNodeList(component);

		Element removeLabelElement = (Element) labels.item(REMOVE_BUTTON_LABEL);

		return FormManagerUtil.getElementLocalizedStrings(removeLabelElement,
				component.getFormDocument().getXformsDocument());
	}

	public LocalizedStringBean getDescriptionButtonLabel(FormComponent component) {

		NodeList labels = getLabelNodeList(component);

		Element descriptionLabelElement = (Element) labels
				.item(DESCRIPTION_LABEL);

		return FormManagerUtil.getElementLocalizedStrings(
				descriptionLabelElement, component.getFormDocument()
						.getXformsDocument());
	}

	public LocalizedStringBean getUploadingFileDescription(
			FormComponent component) {

		NodeList labels = getLabelNodeList(component);

		Element uplFileLabelElement = (Element) labels
				.item(UPLOADING_FILE_DESC);

		return FormManagerUtil.getElementLocalizedStrings(uplFileLabelElement,
				component.getFormDocument().getXformsDocument());
	}
}