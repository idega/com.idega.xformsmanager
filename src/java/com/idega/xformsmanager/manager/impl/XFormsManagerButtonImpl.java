package com.idega.xformsmanager.manager.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.block.process.variables.Variable;
import com.idega.block.process.variables.VariableDataType;
import com.idega.util.xml.XPathUtil;
import com.idega.xformsmanager.business.component.ConstButtonType;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.FormComponentType;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.ComponentButtonDataBean;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManagerButton;
import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.xform.Bind;
import com.idega.xformsmanager.xform.Nodeset;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.12 $
 * 
 *          Last modified: $Date: 2008/11/20 16:31:28 $ by $Author: civilis $
 */
@FormComponentType(FormComponentType.button)
@Service
@Scope("singleton")
public class XFormsManagerButtonImpl extends XFormsManagerImpl implements
		XFormsManagerButton {

	private static final String actionTaken = "actionTaken";

	private final XPathUtil referActionSetValueElementXPath = new XPathUtil(
			".//xf:setvalue[@bind='" + actionTaken + "']");

	@Override
	public void loadComponentFromDocument(FormComponent component) {

		super.loadComponentFromDocument(component);
		loadToggleElement(component);
	}

	protected void loadToggleElement(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
				.getComponentDataBean();

		ComponentButtonDataBean xFormsComponentButtonDataBean = (ComponentButtonDataBean) xformsComponentDataBean;

		NodeList toggles = xFormsComponentButtonDataBean.getElement()
				.getElementsByTagName(FormManagerUtil.toggle_tag);

		if (toggles == null)
			return;

		Element toggleElement = (Element) toggles.item(0);
		xFormsComponentButtonDataBean.setToggleElement(toggleElement);
	}

	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentButtonDataBean();
	}

	public void renewButtonPageContextPages(FormComponent component,
			FormComponentPage previous, FormComponentPage next) {

		if (component.getType().equals(
				ConstButtonType.PREVIOUS_PAGE_BUTTON.toString())) {

			renewControlButton(component, previous);

		} else if (component.getType().equals(
				ConstButtonType.NEXT_PAGE_BUTTON.toString())) {

			renewControlButton(component, next);
		}
	}

	protected void renewControlButton(FormComponent button,
			FormComponentPage relevantPage) {

		ComponentButtonDataBean buttonDataBean = (ComponentButtonDataBean) button
				.getComponentDataBean();

		Element toggleElement = buttonDataBean.getToggleElement();

		if (relevantPage == null || relevantPage.isSpecialPage()) {

			buttonDataBean.setToggleElement(null);
			toggleElement.getParentNode().removeChild(toggleElement);

		} else {

			FormDocument formDocument = button.getFormDocument();

			if (toggleElement == null) {

				toggleElement = FormManagerUtil.getItemElementById(
						formDocument.getContext().getCacheManager()
								.getComponentsTemplate(), "toggle-element");

				toggleElement = (Element) buttonDataBean.getElement()
						.getOwnerDocument().importNode(toggleElement, true);
				toggleElement = (Element) buttonDataBean.getElement()
						.appendChild(toggleElement);
				buttonDataBean.setToggleElement(toggleElement);
			}

			toggleElement.setAttribute(FormManagerUtil.case_att, relevantPage
					.getId());

			/*
			 * if (formDocument.getProperties().isStepsVisualizationUsed()) {
			 * 
			 * if (FormComponentFactory.page_type_thx.equals(relevantPage
			 * .getType())) { removeSetValues(button); } else {
			 * 
			 * Element setval = FormManagerUtil.getElementById(button
			 * .getFormDocument().getXformsDocument(), button .getId() +
			 * FormManagerUtil.set_section_vis_cur);
			 * 
			 * if (setval == null) {
			 * 
			 * setval = createSetValue(button, true); setval = (Element)
			 * toggleElement.getParentNode() .insertBefore(setval,
			 * toggleElement); } setval .setAttribute(
			 * FormManagerUtil.ref_s_att, "instance('" +
			 * FormManagerUtil.sections_visualization_instance_id +
			 * "')/section[id='" + button.getParent() .getParentPage().getId() +
			 * "']/@selected");
			 * 
			 * setval = FormManagerUtil.getElementById(button
			 * .getFormDocument().getXformsDocument(), button .getId() +
			 * FormManagerUtil.set_section_vis_rel);
			 * 
			 * if (setval == null) {
			 * 
			 * setval = createSetValue(button, false); setval = (Element)
			 * toggleElement.getParentNode() .insertBefore(setval,
			 * toggleElement); }
			 * 
			 * setval .setAttribute( FormManagerUtil.ref_s_att, "instance('" +
			 * FormManagerUtil.sections_visualization_instance_id +
			 * "')/section[id='" + relevantPage.getId() + "']/@selected"); } }
			 * else removeSetValues(button);
			 */
		}
	}

	/*
	 * private void removeSetValues(FormComponent component) {
	 * 
	 * Element setval = FormManagerUtil.getElementById(component
	 * .getFormDocument().getXformsDocument(), component.getId() +
	 * FormManagerUtil.set_section_vis_cur);
	 * 
	 * if (setval != null) setval.getParentNode().removeChild(setval);
	 * 
	 * setval = FormManagerUtil.getElementById(component.getFormDocument()
	 * .getXformsDocument(), component.getId() +
	 * FormManagerUtil.set_section_vis_rel);
	 * 
	 * if (setval != null) setval.getParentNode().removeChild(setval); }
	 */

	/*
	 * private Element createSetValue(FormComponent component, boolean current)
	 * {
	 * 
	 * Document xform = component.getFormDocument().getXformsDocument(); Element
	 * setValue = xform.createElementNS( FormManagerUtil.xforms_namespace_uri,
	 * FormManagerUtil.setvalue_tag);
	 * setValue.setAttribute(FormManagerUtil.event_att,
	 * FormManagerUtil.DOMActivate_att_val);
	 * setValue.setAttribute(FormManagerUtil.value_att, new StringBuilder(
	 * "instance('").append(
	 * FormManagerUtil.sections_visualization_instance_id).append(
	 * "')/class_exp[@for='").append(current ? "false" : "true")
	 * .append("']/@for").toString());
	 * 
	 * setValue.setAttribute(FormManagerUtil.id_att, component.getId() +
	 * (current ? FormManagerUtil.set_section_vis_cur :
	 * FormManagerUtil.set_section_vis_rel)); return setValue; }
	 */

	@Override
	public void addComponentToDocument(FormComponent component) {
		super.addComponentToDocument(component);
		loadToggleElement(component);
	}

	public void setLastPageToSubmitButton(FormComponent component,
			String last_page_id) {
		// ((ComponentButtonDataBean) component.getComponentDataBean())
		// .getToggleElement().setAttribute(FormManagerUtil.case_att,
		// last_page_id);

		// TODO: this should change the toggle in the submission
	}

	// protected Element createToggleElement(FormComponent component) {
	//			
	// ComponentDataBean xformsComponentDataBean =
	// component.getComponentDataBean();
	//			
	// Element toggle_element =
	// FormManagerUtil.getItemElementById(component.getFormDocument().getContext().getCacheManager().getComponentsTemplate(),
	// "toggle-element");
	// Element button_element = xformsComponentDataBean.getElement();
	// NodeList refreshs =
	// button_element.getElementsByTagName(FormManagerUtil.refresh_tag);
	// toggle_element =
	// (Element)button_element.getOwnerDocument().importNode(toggle_element,
	// true);
	//			
	// if(refreshs == null || refreshs.getLength() == 0)
	// toggle_element = (Element)button_element.appendChild(toggle_element);
	// else
	// toggle_element = (Element)button_element.insertBefore(toggle_element,
	// refreshs.item(refreshs.getLength()-1));
	//			
	// ((ComponentButtonDataBean)xformsComponentDataBean).setToggleElement(toggle_element);
	// return toggle_element;
	// }

	/**
	 * Creates xf:setvalue element for button element (if doesn't exist
	 * already), fills it's text content with referAction parameter value.
	 * Creates action bind and action nodeset if doesn't exist.
	 * 
	 * @param component
	 *            - current button component
	 * @param referAction
	 *            - action information to insert to setvalue text content. If
	 *            referAction == null, xf:setvalue element is removed and the
	 *            check for any additional action references is performed. If no
	 *            found, the action bind and action nodeset are removed as well.
	 */
	public void setReferAction(FormComponent component, String referAction) {

		if (referAction == null)
			removeReferAction(component);
		else
			setOrCreateReferAction(component, referAction);
	}

	private void removeReferAction(FormComponent component) {

		Element buttonElement = component.getComponentDataBean().getElement();

		Element setValueEl = getReferActionSetValueElement(buttonElement);

		if (setValueEl != null) {
			setValueEl.getParentNode().removeChild(setValueEl);
		}
	}

	private void setOrCreateReferAction(FormComponent formComponent,
			String referAction) {

		Element buttonElement = formComponent.getComponentDataBean()
				.getElement();
		Element setValueEl = getReferActionSetValueElement(buttonElement);

		if (setValueEl == null) {

			Document xform = buttonElement.getOwnerDocument();
			setValueEl = xform.createElementNS(
					FormManagerUtil.xforms_namespace_uri,
					FormManagerUtil.setvalue_tag);
			// setValueEl.setAttribute(FormManagerUtil.event_att,
			// FormManagerUtil.DOMActivate_att_val);

			// idega:dispatch name="idega-validate"

			NodeList dispatces = FormManagerUtil
					.getElementsContainingAttribute(buttonElement,
							"idega:dispatch", "name");

			Element validationDispatch = (Element) dispatces.item(0);
			validationDispatch.getParentNode().insertBefore(setValueEl,
					validationDispatch);

			/*
			 * if (!DOMUtil.hasElementChildren(buttonElement)) {
			 * buttonElement.appendChild(setValueEl);
			 * 
			 * } else {
			 * 
			 * Element elToAppendAfter = getLabelElement(buttonElement);
			 * 
			 * if (elToAppendAfter == null) elToAppendAfter = DOMUtil
			 * .getFirstChildElement(buttonElement);
			 * 
			 * Element next = DOMUtil.getNextSiblingElement(elToAppendAfter);
			 * 
			 * if (next != null) buttonElement.insertBefore(setValueEl, next);
			 * else buttonElement.appendChild(setValueEl); }
			 */

			Bind bind = Bind.locate(formComponent, actionTaken);

			if (bind == null) {

				Element modelElement = FormManagerUtil
						.getFormInstanceModelElement(xform);

				Nodeset nodeset = Nodeset.locate(modelElement, actionTaken);

				if (nodeset == null)
					nodeset = Nodeset.create(modelElement, actionTaken);

				Variable var = new Variable(actionTaken,
						VariableDataType.STRING);
				nodeset.setMapping(var.getDefaultStringRepresentation());

				// create
				bind = Bind.create(formComponent, actionTaken, null, nodeset);
				bind.setIsShared(true);
			}

			setValueEl.setAttribute(FormManagerUtil.bind_att, bind.getId());
		}

		setValueEl.setTextContent(referAction);
	}

	public String getReferAction(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
				.getComponentDataBean();
		Element buttonElement = xformsComponentDataBean.getElement();
		Element setValueEl = getReferActionSetValueElement(buttonElement);

		return setValueEl == null ? null : setValueEl.getTextContent();
	}

	private Element getReferActionSetValueElement(Node context) {

		return referActionSetValueElementXPath.getNode(context);
	}

	// private Element getLabelElement(Node context) {
	//
	// return FormManagerUtil.getLabelElementXPath().getNode(context);
	// }

	// private synchronized XPathUtil getBindToExistsXPath() {
	//
	// if (bindToExistsXPath == null)
	// bindToExistsXPath = new XPathUtil(".//*[@bind=$bindId]");
	//
	// return bindToExistsXPath;
	// }

	@Override
	public void update(FormComponent component, ConstUpdateType what,
			Object prop) {

		super.update(component, what, prop);

		switch (what) {
		case BUTTON_REFER_TO_ACTION:

			updateReferAction(component);
			break;

		default:
			break;
		}
	}

	protected void updateReferAction(FormComponent component) {

		PropertiesButton properties = (PropertiesButton) component
				.getProperties();
		String referAction = properties.getReferAction();
		setReferAction(component, referAction);
	}

	// @Override
	// protected void updateReadonly(FormComponent component) {
	//		
	// setReadonly(component, component.getProperties().isReadonly());
	// }
	//	
	// @Override
	// public void setReadonly(FormComponent component, boolean readonly) {
	//		
	// ComponentDataBean xformsComponentDataBean =
	// component.getXformsComponentDataBean();
	// Bind bind = xformsComponentDataBean.getBind();
	//		
	// if(bind == null) {
	//
	// bind = Bind.create(component.getContext().getXformsXmlDoc(),
	// "bind."+component.getId(), null, null);
	// xformsComponentDataBean.setBind(bind);
	// xformsComponentDataBean.getElement().setAttribute(FormManagerUtil.bind_att,
	// bind.getId());
	// Nodeset nodeset =
	// Nodeset.create(FormManagerUtil.getFormInstanceModelElement(component.getContext().getXformsXmlDoc()),
	// bind.getId());
	// bind.setNodeset(nodeset);
	// }
	//		
	// bind.setIsRelevant(!readonly);
	// }

	@Override
	public boolean isReadonly(FormComponent component) {

		// TODO: is this correct behavior?
		ComponentDataBean xformsComponentDataBean = component
				.getComponentDataBean();
		Bind bind = xformsComponentDataBean.getBind();

		return bind != null
				&& bind.getNodeset().getContent().equals(
						FormManagerUtil.xpath_false);
	}

	// private synchronized Element getSendSubmissionElement(Node context) {
	//
	// if (sendSubmissionXPath == null)
	// sendSubmissionXPath = new XPathUtil(
	// ".//xf:send[@submission='submit_data_submission']");
	//
	// return (Element) sendSubmissionXPath.getNode(context);
	// }

	// public boolean isSubmitButton(FormComponent component) {
	//
	// ComponentDataBean xformsComponentDataBean = component
	// .getComponentDataBean();
	// return getSendSubmissionElement(xformsComponentDataBean.getElement()) !=
	// null;
	// }
}