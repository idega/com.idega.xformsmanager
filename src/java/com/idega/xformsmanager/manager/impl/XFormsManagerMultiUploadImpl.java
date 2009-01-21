package com.idega.xformsmanager.manager.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.11 $
 * 
 *          Last modified: $Date: 2009/01/21 14:15:12 $ by $Author: arunas $
 */
@FormComponentType(FormComponentType.multiupload)
@Service
@Scope("singleton")
public class XFormsManagerMultiUploadImpl extends XFormsManagerImpl implements
		XFormsManagerMultiUpload {

	private static final int TITLE_LABEL = 0;
	private static final int INSERT_BUTTON_LABEL = 1;
	private static final int DESCRIPTION_LABEL = 2;
	private static final int UPLOADING_FILE_DESC = 3;
	private static final int REMOVE_BUTTON_LABEL = 4;

	private final XPathUtil labelsXPathUT = new XPathUtil(".//xf:label");
	
	final private XPathUtil uploadErrorMsgElementXpath = new XPathUtil(".//xf:output[@value]");
	final private XPathUtil uploadHeaderElementXpath = new XPathUtil(".//xf:output[@model='data_model']");

	
	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentMultiUploadBean();
	}

	@Override
	public void removeComponentFromXFormsDocument(FormComponent component) {

		ComponentMultiUploadBean xforms_component = (ComponentMultiUploadBean) component
				.getComponentDataBean();
		Element data_src_element = xforms_component.getMultiUploadInstance();

		if (data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);

		super.removeComponentFromXFormsDocument(component);
	}

	public static final XPathUtil uploadRepeatElementXPath = new XPathUtil(".//xf:repeat");

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
		case UPLOADER_HEADER_TEXT:
			updateUploaderHeaderText(component);
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

		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
				addButtonlabel,
				component.getFormDocument().getXformsDocument(), localizedText);

	}

	protected void updateRemoveButtonLabel(FormComponent component) {

		PropertiesMultiUpload properties = (PropertiesMultiUpload) component
				.getProperties();
		LocalizedStringBean localizedText = properties.getRemoveButtonLabel();
		NodeList labels = getLabelNodeList(component);

		Element removeButtonlabel = (Element) labels.item(REMOVE_BUTTON_LABEL);
		
		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
				removeButtonlabel,
				component.getFormDocument().getXformsDocument(), localizedText);


	}

	protected void updateUploadingFileDescription(FormComponent component) {
		
		PropertiesMultiUpload properties = (PropertiesMultiUpload) component.getProperties();
		LocalizedStringBean localizedText = properties.getUploadingFileDescription();
		
		NodeList labels = getLabelNodeList(component);

		Element label = (Element) labels.item(UPLOADING_FILE_DESC);
		
		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
				label,
				component.getFormDocument().getXformsDocument(), localizedText);
		
	}
	
	protected void updateUploaderHeaderText(FormComponent component) {
		
		PropertiesMultiUpload properties = (PropertiesMultiUpload) component.getProperties();
		LocalizedStringBean localizedText = properties.getUploaderHeaderText();
		
		Element headerElem = getUploadersHeaderElement(component);
		
		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,	headerElem,
				component.getFormDocument().getXformsDocument(), localizedText);
		
	}

	public LocalizedStringBean getUploaderHeaderText(FormComponent component) {
		
		return FormManagerUtil.getElementLocalizedStrings(getUploadersHeaderElement(component),
				component.getFormDocument().getXformsDocument());
	}
	
	private Element getUploadersHeaderElement(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getComponentDataBean();
		
		Element headerTextElem = (Element) uploadHeaderElementXpath.getNode(xformsComponentDataBean.getElement());
		
		return headerTextElem;
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

//		PropertiesMultiUpload properties = (PropertiesMultiUpload) component
//				.getProperties();

		Element output = (Element) uploadErrorMsgElementXpath.getNode(xformsComponentDataBean
				.getElement());

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