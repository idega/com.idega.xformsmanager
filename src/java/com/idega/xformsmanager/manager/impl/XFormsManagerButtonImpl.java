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
import com.idega.xformsmanager.xform.BindFactory;
import com.idega.xformsmanager.xform.Nodeset;
import com.idega.xformsmanager.xform.NodesetFactory;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.14 $ Last modified: $Date: 2009/04/23 14:15:27 $ by $Author: civilis $
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
			
			if (toggleElement != null) {
				
				buttonDataBean.setToggleElement(null);
				toggleElement.getParentNode().removeChild(toggleElement);
			}
			
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
		}
	}
	
	@Override
	public void addComponentToDocument(FormComponent component) {
		super.addComponentToDocument(component);
		loadToggleElement(component);
	}
	
	public void setLastPageToSubmitButton(FormComponent component,
	        String last_page_id) {
		
		// TODO: this should change the toggle in the submission
	}
	
	/**
	 * Creates xf:setvalue element for button element (if doesn't exist already), fills it's text
	 * content with referAction parameter value. Creates action bind and action nodeset if doesn't
	 * exist.
	 * 
	 * @param component
	 *            - current button component
	 * @param referAction
	 *            - action information to insert to setvalue text content. If referAction == null,
	 *            xf:setvalue element is removed and the check for any additional action references
	 *            is performed. If no found, the action bind and action nodeset are removed as well.
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
			
			NodeList dispatces = FormManagerUtil
			        .getElementsContainingAttribute(buttonElement,
			            "idega:dispatch", "name");
			
			Element validationDispatch = (Element) dispatces.item(0);
			validationDispatch.getParentNode().insertBefore(setValueEl,
			    validationDispatch);
			
			BindFactory bindFactory = getBindFactory(formComponent
			        .getFormDocument());
			Bind bind = bindFactory.locate(formComponent, actionTaken);
			
			if (bind == null) {
				
				Element modelElement = FormManagerUtil
				        .getFormInstanceModelElement(xform);
				
				NodesetFactory nodesetFactory = getNodesetFactory(formComponent
				        .getFormDocument());
				Nodeset nodeset = nodesetFactory.locate(modelElement,
				    actionTaken);
				
				if (nodeset == null)
					nodeset = nodesetFactory.create(modelElement, actionTaken);
				
				Variable var = new Variable(actionTaken,
				        VariableDataType.STRING);
				nodeset.setMapping(var.getDefaultStringRepresentation());
				
				// create
				bind = bindFactory.create(formComponent, actionTaken, null,
				    nodeset);
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
}