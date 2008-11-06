package com.idega.xformsmanager.manager.impl;

import org.chiba.xml.dom.DOMUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.xformsmanager.business.component.properties.PropertiesSelect;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentType;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.beans.ComponentSelectDataBean;
import com.idega.xformsmanager.component.beans.LocalizedItemsetBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerSelect;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 * 
 *          Last modified: $Date: 2008/11/06 14:32:30 $ by $Author: civilis $
 */
@FormComponentType(FormComponentType.select)
@Service
@Scope("singleton")
public class XFormsManagerSelectImpl extends XFormsManagerImpl implements
		XFormsManagerSelect {

	private static final String local_data_source = "_lds";
	private static final String external_data_source = "_eds";

	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentSelectDataBean();
	}

	@Override
	public void loadComponentFromTemplate(FormComponent component) {
		super.loadComponentFromTemplate(component);
		loadItemsets(component);
	}

	@Override
	public void loadComponentFromDocument(FormComponent component) {
		super.loadComponentFromDocument(component);
		loadItemsets(component);
	}

	protected void loadItemsets(FormComponent component) {

		ComponentSelectDataBean componentDataBean = (ComponentSelectDataBean) component
				.getComponentDataBean();

		String componentId = component.getId();
		Document xform = component.getFormDocument().getXformsDocument();

		Element localDataSourceInstance = FormManagerUtil.getElementById(xform,
				getLocalDataSourceInstanceIdentifier(componentId));

		Element externalDataSourceInstance = FormManagerUtil.getElementById(
				xform, getExternalDataSourceInstanceIdentifier(componentId));

		componentDataBean.setLocalItemsetInstance(localDataSourceInstance);
		componentDataBean
				.setExternalItemsetInstance(externalDataSourceInstance);
	}

	/*
	 * @Override protected void loadXFormsComponentDataBean(FormComponent
	 * component, Document xform, Element componentElement) {
	 * 
	 * super.loadXFormsComponentDataBean(component, xform, componentElement);
	 * 
	 * ComponentSelectDataBean xformsComponentDataBean =
	 * (ComponentSelectDataBean)component.getXformsComponentDataBean();
	 * 
	 * componentElement = xformsComponentDataBean.getElement(); String
	 * componentElementId =
	 * componentElement.getAttribute(FormManagerUtil.id_att);
	 * 
	 * Element localDataSourceInstance =
	 * FormManagerUtil.getElementByIdFromDocument(xform,
	 * FormManagerUtil.head_tag,
	 * getLocalDataSourceInstanceIdentifier(componentElementId)); Element
	 * externalDataSourceInstance =
	 * FormManagerUtil.getElementByIdFromDocument(xform,
	 * FormManagerUtil.head_tag,
	 * getExternalDataSourceInstanceIdentifier(componentElementId));
	 * 
	 * xformsComponentDataBean.setLocalItemsetInstance(localDataSourceInstance);
	 * xformsComponentDataBean
	 * .setExternalItemsetInstance(externalDataSourceInstance); }
	 */

	private String getLocalDataSourceInstanceIdentifier(String componentId) {

		return componentId + local_data_source;
	}

	private String getExternalDataSourceInstanceIdentifier(String componentId) {

		return componentId + external_data_source;
	}

	@Override
	public void addComponentToDocument(FormComponent component) {

		ComponentSelectDataBean templateComponentDataBean = (ComponentSelectDataBean) component
				.getComponentDataBean();

		super.addComponentToDocument(component);

		Document xform = component.getFormDocument().getXformsDocument();

		ComponentSelectDataBean componentDataBean = (ComponentSelectDataBean) component
				.getComponentDataBean();

		Element localItemsetInstanceElement = templateComponentDataBean
				.getLocalItemsetInstance();

		Element dataModelElement = FormManagerUtil.getElementById(xform,
				FormManagerUtil.data_mod);

		if (localItemsetInstanceElement != null) {

			String localId = localItemsetInstanceElement
					.getAttribute(FormManagerUtil.id_att);

			localItemsetInstanceElement = (Element) xform.importNode(
					localItemsetInstanceElement, true);
			localItemsetInstanceElement.setAttribute(FormManagerUtil.id_att,
					getLocalDataSourceInstanceIdentifier(component.getId()));
			localItemsetInstanceElement = (Element) dataModelElement
					.appendChild(localItemsetInstanceElement);
			componentDataBean
					.setLocalItemsetInstance(localItemsetInstanceElement);

			Element itemset = DOMUtil.getElementByAttributeValue(
					componentDataBean.getElement(), "*",
					FormManagerUtil.nodeset_att, constructItemsetInstance(
							localId, null));

			if (itemset != null)
				itemset.setAttribute(FormManagerUtil.nodeset_att,
						constructItemsetInstance(component.getId(),
								PropertiesSelect.LOCAL_DATA_SRC));
		}

		Element externalItemsetInstanceElement = templateComponentDataBean
				.getExternalItemsetInstance();

		if (externalItemsetInstanceElement != null) {

			externalItemsetInstanceElement = (Element) xform.importNode(
					externalItemsetInstanceElement, true);
			externalItemsetInstanceElement.setAttribute(FormManagerUtil.id_att,
					getExternalDataSourceInstanceIdentifier(component.getId()));
			externalItemsetInstanceElement = (Element) dataModelElement
					.appendChild(externalItemsetInstanceElement);
			componentDataBean
					.setExternalItemsetInstance(externalItemsetInstanceElement);
		}
	}

	public Integer getDataSrcUsed(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
				.getComponentDataBean();

		Element componentElement = xformsComponentDataBean.getElement();

		Element itemset = DOMUtil.getChildElement(componentElement,
				FormManagerUtil.itemset_tag);

		if (itemset == null)
			return null;

		String nodesetAttValue = itemset
				.getAttribute(FormManagerUtil.nodeset_att);

		if (nodesetAttValue == null)
			return null;

		String dataSrcInstanceId = nodesetAttValue.substring("instance('"
				.length(), nodesetAttValue.indexOf("')"));

		if (dataSrcInstanceId.endsWith("_eds"))
			return PropertiesSelect.EXTERNAL_DATA_SRC;
		else if (dataSrcInstanceId.endsWith("_lds"))
			return PropertiesSelect.LOCAL_DATA_SRC;

		return null;
	}

	public String getExternalDataSrc(FormComponent component) {

		final Element externalInstance = ((ComponentSelectDataBean) component
				.getComponentDataBean()).getExternalItemsetInstance();

		final String src;

		if (externalInstance == null)
			src = null;
		else
			src = externalInstance.getAttribute(FormManagerUtil.src_att);

		return src;
	}

	public LocalizedItemsetBean getItemset(FormComponent component) {

		Element localInstance = ((ComponentSelectDataBean) component
				.getComponentDataBean()).getLocalItemsetInstance();

		if (localInstance == null)
			return null;

		LocalizedItemsetBean itemsetBean = new LocalizedItemsetBean();
		itemsetBean.setLocalDataSrcElement(localInstance);
		itemsetBean.setComponentsXFormsDocument(component.getFormDocument()
				.getContext().getCacheManager().getComponentsTemplate());
		itemsetBean.setComponent(component);

		return itemsetBean;
	}

	@Override
	public void update(FormComponent component, ConstUpdateType what,
			Object prop) {

		super.update(component, what, prop);

		switch (what) {
		case DATA_SRC_USED:
			updateDataSrcUsed(component);
			break;

		case EXTERNAL_DATA_SRC:
			updateExternalDataSrc(component);
			break;

		default:
			break;
		}
	}

	protected void updateDataSrcUsed(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
				.getComponentDataBean();

		PropertiesSelect properties = (PropertiesSelect) component
				.getProperties();
		Integer dataSrcUsed = properties.getDataSrcUsed();

		Element componentElement = xformsComponentDataBean.getElement();

		if (dataSrcUsed == null) {

			Element itemset = DOMUtil.getChildElement(componentElement,
					FormManagerUtil.itemset_tag);

			if (itemset != null)
				componentElement.removeChild(itemset);

		} else {

			final String itemsetInstanceStr;

			if (dataSrcUsed == PropertiesSelect.EXTERNAL_DATA_SRC) {
				itemsetInstanceStr = constructItemsetInstance(
						component.getId(), PropertiesSelect.EXTERNAL_DATA_SRC);

			} else if (dataSrcUsed == PropertiesSelect.LOCAL_DATA_SRC) {
				itemsetInstanceStr = constructItemsetInstance(
						component.getId(), PropertiesSelect.LOCAL_DATA_SRC);
			} else
				return;

			Element itemset = DOMUtil.getChildElement(componentElement,
					FormManagerUtil.itemset_tag);

			if (itemset == null) {

				itemset = FormManagerUtil.getItemElementById(component
						.getFormDocument().getContext().getCacheManager()
						.getComponentsTemplate(), "itemset");
				itemset = (Element) componentElement.getOwnerDocument()
						.importNode(itemset, true);
				componentElement.appendChild(itemset);
			}

			itemset.setAttribute(FormManagerUtil.nodeset_att,
					itemsetInstanceStr);
		}
	}

	private String constructItemsetInstance(String componentId,
			Integer dataSource) {

		StringBuffer buf = new StringBuffer();

		buf.append("instance('").append(componentId);

		if (dataSource != null) {

			if (dataSource == PropertiesSelect.LOCAL_DATA_SRC)
				buf.append("_lds");
			else
				buf.append("_eds");
		}

		buf
				.append("')/localizedEntries[@lang=instance('localized_strings')/current_language]/item");

		return buf.toString();
	}

	protected void updateExternalDataSrc(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
				.getComponentDataBean();

		Element externalInstance = ((ComponentSelectDataBean) xformsComponentDataBean)
				.getExternalItemsetInstance();

		if (externalInstance == null)
			return;

		String externalDataSrc = ((PropertiesSelect) component.getProperties())
				.getExternalDataSrc();

		if (externalDataSrc == null)
			return;

		externalInstance.setAttribute(FormManagerUtil.src_att, externalDataSrc);
	}

	public void removeSelectComponentSourcesFromXFormsDocument(
			FormComponent component) {

		ComponentSelectDataBean componentDataBean = (ComponentSelectDataBean) component
				.getComponentDataBean();
		Element externalItemsetInstance = componentDataBean
				.getExternalItemsetInstance();

		if (externalItemsetInstance != null)
			externalItemsetInstance.getParentNode().removeChild(
					externalItemsetInstance);

		Element localItemsetInstance = componentDataBean
				.getLocalItemsetInstance();

		if (localItemsetInstance != null)
			localItemsetInstance.getParentNode().removeChild(
					localItemsetInstance);
	}
}