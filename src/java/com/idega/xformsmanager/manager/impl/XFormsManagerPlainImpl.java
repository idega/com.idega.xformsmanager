package com.idega.xformsmanager.manager.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.util.StringUtil;
import com.idega.util.xml.XPathUtil;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.business.component.properties.PropertiesStatic;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentType;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerPlain;
import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.xform.Bind;
import com.idega.xformsmanager.xform.ComponentBind;
import com.idega.xformsmanager.xform.ComponentBindImpl;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.9 $ Last modified: $Date: 2009/04/29 10:48:33 $ by $Author: civilis $
 */
@FormComponentType(FormComponentType.plain)
@Service
@Scope("singleton")
public class XFormsManagerPlainImpl extends XFormsManagerImpl implements
        XFormsManagerPlain {

	@Override
	public void update(FormComponent component, ConstUpdateType what,
	        Object prop) {

		switch (what) {

			case LABEL:
				updateLabel(component);
				break;

			case VARIABLE_NAME:
				updateVariableName(component);
				break;
			case TEXT:
				updateText(component);
				break;

			default:
				break;
		}
	}

	protected void updateText(FormComponent component) {
		ComponentDataBean xformsComponentDataBean = component.getComponentDataBean();

		PropertiesStatic properties = (PropertiesStatic) component.getProperties();
		LocalizedStringBean localizedText = properties.getText();

		NodeList outputs = FormManagerUtil.getElementsContainingAttribute(xformsComponentDataBean.getElement(), "xf:output",
		    FormManagerUtil.ref_s_att);

		if (outputs == null || outputs.getLength() == 0)
			outputs = FormManagerUtil.getElementsContainingAttribute(xformsComponentDataBean.getElement(), FormManagerUtil.output_tag,
				    FormManagerUtil.ref_s_att);

		Element output;
		String localizationKey = null;
		if (outputs == null || outputs.getLength() == 0) {
			if (localizedText == null)
				return;

			output = xformsComponentDataBean.getElement().getOwnerDocument().createElementNS(
			            component.getFormDocument().getFormDataModelElement().getNamespaceURI(), "xf:output");
			output = (Element) xformsComponentDataBean.getElement().appendChild(output);
			localizationKey = component.getId() + ".text";
		} else
			output = (Element) outputs.item(0);

		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, localizationKey, null, output,
				component.getFormDocument().getXformsDocument(), localizedText);
	}

	@Override
	public LocalizedStringBean getText(FormComponent component) {
		Element output = component.getComponentDataBean().getElement();

		if (!output.hasAttribute(FormManagerUtil.ref_s_att)) {
			XPathUtil outputXPUT = new XPathUtil(".//xf:output");
			output = (Element) outputXPUT.getNode(output);

			if (output == null) {
				outputXPUT = new XPathUtil(".//" + FormManagerUtil.output_tag);
				output = (Element) outputXPUT.getNode(output);
			}

			if (output == null)
				return null;
		}

		return FormManagerUtil.getElementLocalizedStrings(output, component.getFormDocument().getXformsDocument());
	}

	@Override
	protected boolean removeTextNodes() {
		return true;
	}

	@Override
	protected void updateVariableName(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();
		PropertiesComponent properties = component.getProperties();

		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		if (!componentBind.exists() && properties.getVariable() != null) {

			Bind bind = createBindForOutput(component);
			xformsComponentDataBean.setComponentBind(new ComponentBindImpl(
			        component, bind));
		}

		super.updateVariableName(component);
	}

	private Bind createBindForOutput(FormComponent outputComponent) {

		ComponentDataBean xformsComponentDataBean = outputComponent
		        .getComponentDataBean();
		Element output = xformsComponentDataBean.getElement()
		        .getOwnerDocument().createElementNS(
		            outputComponent.getFormDocument().getFormDataModelElement()
		                    .getNamespaceURI(), FormManagerUtil.output_tag);
		output = (Element) xformsComponentDataBean.getElement().appendChild(
		    output);

		Element label = xformsComponentDataBean.getElement().getOwnerDocument()
		        .createElementNS(
		            outputComponent.getFormDocument().getFormDataModelElement()
		                    .getNamespaceURI(), FormManagerUtil.label_tag);
		output.appendChild(label);

		Bind bind = getBindFactory(outputComponent.getFormDocument()).create(
		    "bind." + outputComponent.getId(), null, null);
		output.setAttribute(FormManagerUtil.bind_att, bind.getId());

		return bind;
	}

	@Override
	protected void updateLabel(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		PropertiesComponent props = component.getProperties();
		LocalizedStringBean locStr = props.getLabel();

		NodeList labels = xformsComponentDataBean.getElement()
		        .getElementsByTagName(FormManagerUtil.label_tag);

		if (labels == null || labels.getLength() == 0)
			return;

		Element label = (Element) labels.item(0);

		String ref = label.getAttribute(FormManagerUtil.ref_s_att);

		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, !StringUtil
		        .isEmpty(ref) ? null : new StringBuilder(component.getId())
		        .append(".label").toString(), null, label, component
		        .getFormDocument().getXformsDocument(), locStr);
	}
}