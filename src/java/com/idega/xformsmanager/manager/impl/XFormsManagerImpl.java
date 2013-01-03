package com.idega.xformsmanager.manager.impl;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chiba.xml.dom.DOMUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.block.process.variables.Variable;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormComponentButtonArea;
import com.idega.xformsmanager.component.FormComponentContainer;
import com.idega.xformsmanager.component.FormComponentPage;
import com.idega.xformsmanager.component.FormComponentType;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.component.beans.ComponentDataBean;
import com.idega.xformsmanager.component.beans.ErrorStringBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.xformsmanager.component.properties.impl.ConstUpdateType;
import com.idega.xformsmanager.manager.XFormsManager;
import com.idega.xformsmanager.util.FormManagerUtil;
import com.idega.xformsmanager.xform.Bind;
import com.idega.xformsmanager.xform.BindFactory;
import com.idega.xformsmanager.xform.ComponentBind;
import com.idega.xformsmanager.xform.ComponentBindImpl;
import com.idega.xformsmanager.xform.ComponentBindNoBindImpl;
import com.idega.xformsmanager.xform.ComponentBindTemplateImpl;
import com.idega.xformsmanager.xform.Nodeset;
import com.idega.xformsmanager.xform.NodesetFactory;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.25 $ Last modified: $Date: 2009/04/29 12:23:32 $ by $Author: arunas $
 */
@FormComponentType(FormComponentType.base)
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class XFormsManagerImpl implements XFormsManager {

	private static final String autofill_attr = "autofillkey";
	public static final String HTML_EDITOR_SYLE_CLASS_NAME = "enableHTMLEditor";

	@Override
	public void loadComponentFromTemplate(FormComponent templateFormComponent) {

		final String componentType = templateFormComponent.getType();
		final CacheManager cacheManager = templateFormComponent
		        .getFormDocument().getContext().getCacheManager();

		ComponentDataBean templateComponentDataBean = cacheManager
		        .getXformsComponentTemplate(componentType);

		if (templateComponentDataBean == null) {

			templateComponentDataBean = loadComponentTemplateDataBean(templateFormComponent);
		}

		if (templateComponentDataBean != null) {

			templateFormComponent.setId(templateComponentDataBean.getElement()
			        .getAttribute(FormManagerUtil.id_att));

			// here we set only template component data bean
			templateFormComponent
			        .setComponentDataBean(templateComponentDataBean);
		} else {

			throw new RuntimeException(
			        "Component not found in components template document by provided type: "
			                + componentType
			                + ", check the components template document.");
		}
	}

	protected synchronized ComponentDataBean loadComponentTemplateDataBean(
	        FormComponent templateFormComponent) {

		final String componentType = templateFormComponent.getType();
		final CacheManager cacheManager = templateFormComponent
		        .getFormDocument().getContext().getCacheManager();

		ComponentDataBean templateComponentDataBean = cacheManager
		        .getXformsComponentTemplate(componentType);

		if (templateComponentDataBean == null) {

			Element componentTemplateElement = FormManagerUtil.getElementById(
			    templateFormComponent.getFormDocument().getXformsDocument(),
			    componentType);

			if (componentTemplateElement != null) {

				templateComponentDataBean = newXFormsComponentDataBeanInstance();

				templateComponentDataBean.setElement(componentTemplateElement);
				templateFormComponent
				        .setComponentDataBean(templateComponentDataBean);

				// TODO: load other stuff from the template, like
				// instances, or references or or or
				loadBindsAndNodesets(templateFormComponent);
				loadExtKeyElements(templateFormComponent);

				templateComponentDataBean = templateFormComponent
				        .getComponentDataBean();
				Bind templateBind = templateComponentDataBean
				        .getComponentBind().getBind();

				templateComponentDataBean
				        .setComponentBind(new ComponentBindTemplateImpl(
				                templateBind));

				cacheManager.cacheXformsComponent(componentType,
				    templateComponentDataBean);

			} else {

				Logger.getLogger(getClass().getName()).log(
				    Level.SEVERE,
				    "Component not found in components template document by provided type: "
				            + componentType);
			}
		}

		return templateComponentDataBean;
	}

	@Override
	public void loadComponentFromDocument(FormComponent component) {

		Document xform = component.getFormDocument().getXformsDocument();
		Element componentElement = FormManagerUtil.getElementById(xform,
		    component.getId());
		ComponentDataBean xformsComponentDataBean = newXFormsComponentDataBeanInstance();
		component.setComponentDataBean(xformsComponentDataBean);
		xformsComponentDataBean.setElement(componentElement);

		loadBindsAndNodesets(component);
		loadExtKeyElements(component);
	}

	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentDataBean();
	}

	protected void loadExtKeyElements(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();
		Document xform = component.getFormDocument().getXformsDocument();

		xformsComponentDataBean.setKeyExtInstance(FormManagerUtil
		        .getElementById(xform, component.getId()
		                + FormManagerUtil.autofill_instance_ending));
		xformsComponentDataBean.setKeySetvalue(FormManagerUtil
		        .getElementById(xform, component.getId()
		                + FormManagerUtil.autofill_setvalue_ending));
	}

	protected void loadBindsAndNodesets(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		String bindId = xformsComponentDataBean.getElement().getAttribute(
		    FormManagerUtil.bind_att);

		if (!StringUtil.isEmpty(bindId)) {

			Bind bind = getBindFactory(component.getFormDocument()).locate(
			    bindId);

			if (bind == null)
				throw new NullPointerException("Binding not found by bind id: "
				        + bindId + " in the form "
				        + component.getFormDocument().getId());

			xformsComponentDataBean.setComponentBind(createComponentBind(
			    component, bind));
		} else {
			xformsComponentDataBean
			        .setComponentBind(new ComponentBindNoBindImpl(component));
		}
	}

	protected ComponentBind createComponentBind(FormComponent component,
	        Bind bind) {

		return new ComponentBindImpl(component, bind);
	}

	/**
	 * import component from template component
	 */
	@Override
	public void addComponentToDocument(FormComponent component) {

		ComponentDataBean templateComponentDataBean = component
		        .getComponentDataBean();

		ComponentDataBean componentDataBean = newXFormsComponentDataBeanInstance();
		component.setComponentDataBean(componentDataBean);
		Document xform = component.getFormDocument().getXformsDocument();

		// resolving template element and importing to xform document
		Element componentElement = templateComponentDataBean.getElement();
		componentElement = (Element) xform.importNode(componentElement, true);
		componentDataBean.setElement(componentElement);

		String componentId = component.getId();
		componentElement.setAttribute(FormManagerUtil.id_att, componentId);

		FormManagerUtil.replaceAttributesByExpression(componentElement,
		    "componentId", componentId);

		localizeComponent(componentId, componentElement, xform, component
		        .getFormDocument().getContext().getCacheManager()
		        .getComponentsTemplate());

		// TODO: is this something we need?
		if (removeTextNodes())
			FormManagerUtil.removeTextNodes(componentElement);

		ComponentBind componentBind;

		if (templateComponentDataBean.getComponentBind().exists()) {

			// TODO: this is not good, see uncommented code at createFromTemplate
			String defaultBindId = component.getId() + CoreConstants.UNDER
			        + FormManagerUtil.bind_att;

			Bind bind = getBindFactory(component.getFormDocument())
			        .createFromTemplate(
			            templateComponentDataBean.getComponentBind().getBind(),
			            defaultBindId);

			componentBind = createComponentBind(component, bind);
			componentBind.updateBindReference();

		} else {
			// TODO: create separately
			componentBind = new ComponentBindNoBindImpl(component);
		}

		componentDataBean.setComponentBind(componentBind);

		FormComponentContainer parent = component.getParent();
		parent.getXFormsManager().addChild(parent, component);
		componentElement = componentDataBean.getElement();

		if (componentDataBean.getElement().getAttributeNode(autofill_attr) != null) {

			component.getProperties().setAutofillKey(
			    componentDataBean.getElement().getAttributeNode(autofill_attr)
			            .getValue());
			componentElement.removeAttribute(autofill_attr);

			updateAutofillKey(component);
		}
	}

	@Override
	public void bindsRenamed(FormComponent component) {

	}

	protected boolean removeTextNodes() {
		return true;
	}

	protected void localizeComponent(String componentId, Element context,
	        Document xform, Document componentsTemplate) {

		// TODO: remake this, localizations could be already there in the xform
		// document, just keys would be changed
		NodeList elements = FormManagerUtil.getLocalizableElements(context);

		for (int i = 0; i < elements.getLength(); i++) {

			Element localizableElement = (Element) elements.item(i);

			final String ref = localizableElement
			        .getAttribute(FormManagerUtil.ref_s_att);
			final String value = localizableElement
			        .getAttribute(FormManagerUtil.value_att);
			final String attributeValue;
			final String attributeName;

			if (FormManagerUtil.isLocalizableExpressionCorrect(ref)) {
				attributeName = FormManagerUtil.ref_s_att;
				attributeValue = ref;
			} else if (FormManagerUtil.isLocalizableExpressionCorrect(value)) {
				attributeName = FormManagerUtil.value_att;
				attributeValue = value;
			} else {
				attributeName = null;
				attributeValue = null;
			}

			if (attributeName != null) {

				String key = FormManagerUtil.getKeyFromRef(attributeValue);
				FormManagerUtil.putLocalizedText(attributeName, FormManagerUtil
				        .getComponentLocalizationKey(componentId, key), null,
				    localizableElement, xform, FormManagerUtil
				            .getLocalizedStrings(key, componentsTemplate));
			}
		}
	}

	protected void updateConstraintRequired(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		PropertiesComponent props = component.getProperties();
		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		if (componentBind.exists()) {

			componentBind.getBind().setIsRequired(props.isRequired());

			if (props.isRequired())
				checkUpdateComponentValidation(component, ErrorType.required);

		} else {

			Logger
			        .getLogger(getClass().getName())
			        .log(
			            Level.SEVERE,
			            "Bind element not set in xforms_component data bean. See where component is rendered for cause.");
			throw new NullPointerException("Bind element is not set");
		}
	}

	protected void checkUpdateComponentValidation(FormComponent component,
	        ErrorType errType) {

		PropertiesComponent props = component.getProperties();

		LocalizedStringBean locStr = props.getErrorMsg(errType);

		if (locStr == null) {

			locStr = FormManagerUtil.getDefaultErrorMessage(errType, component
			        .getFormDocument().getContext()
			        .getComponentsTemplateDocument());
			// create default one and set to properties
			props.setErrorMsg(errType, locStr);
		}
	}

	@Override
	public void update(FormComponent component, ConstUpdateType what,
	        Object property) {

		// TODO: perhaps implement it another way: register updatables, which
		// would listen to update event and update themselves if neccessary
		switch (what) {
			case LABEL:
				updateLabel(component);
				break;

			case ERROR_MSG:

				ErrorStringBean errString = (ErrorStringBean) property;
				updateErrorMsg(component, errString);
				break;

			case HELP_TEXT:
				updateHelpText(component);
				break;

			case CONSTRAINT_REQUIRED:
				updateConstraintRequired(component);
				break;

			case P3P_TYPE:
				updateP3pType(component);
				break;

			case AUTOFILL_KEY:
				updateAutofillKey(component);
				break;

			case VARIABLE_NAME:
				updateVariableName(component);
				break;
			case CALCULATE_EXP:
				updateCalculate(component);
				break;
			case HTML_EDITOR:
				updateHtmlEditor(component);
				break;
			default:
				break;
		}
	}

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

		FormManagerUtil.putLocalizedText(FormManagerUtil.ref_s_att, null, null,
		    label, component.getFormDocument().getXformsDocument(), locStr);
	}

	protected void updateErrorMsg(FormComponent component,
	        ErrorStringBean errString) {

		FormManagerUtil.setErrorLabelLocalizedStrings(component
		        .getComponentDataBean().getElement(), component.getId(),
		    component.getId(), errString, component.getFormDocument()
		            .getContext().getComponentsTemplateDocument());
	}

	protected void updateHelpText(FormComponent component) {

		PropertiesComponent properties = component.getProperties();
		LocalizedStringBean helpMsg = properties.getHelpText();

		FormManagerUtil.setHelpTextLocalizedStrings(component
		        .getComponentDataBean().getElement(), component.getId(),
		    helpMsg, component.getFormDocument().getContext()
		            .getComponentsTemplateDocument());
	}

	@Override
	public void moveComponent(FormComponent component, String nextSiblingId) {

		ComponentDataBean componentDataBean = component.getComponentDataBean();

		Document xform = component.getFormDocument().getXformsDocument();

		Element elementToMove = componentDataBean.getElement();
		// TODO: resolve this from parent
		Element nextSiblingElement = null;
		Element componentContainer = (Element) elementToMove.getParentNode();

		if (nextSiblingId != null) {

			nextSiblingElement = FormManagerUtil.getElementById(xform,
			    nextSiblingId);
		} else {

			nextSiblingElement = DOMUtil
			        .getLastChildElement(componentContainer);
		}

		componentDataBean.setElement((Element) componentContainer.insertBefore(
		    elementToMove, nextSiblingElement));

		changePreviewElementOrder(component);
	}

	protected void changePreviewElementOrder(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		Element preview_element = xformsComponentDataBean.getPreviewElement();

		if (preview_element == null)
			return;

		FormComponent comp_after_this = component.getNextSibling();

		if (comp_after_this != null) {

			Element comp_after_preview = comp_after_this.getComponentDataBean()
			        .getPreviewElement();

			if (comp_after_preview == null)
				return;

			xformsComponentDataBean
			        .setPreviewElement((Element) comp_after_preview
			                .getParentNode().insertBefore(preview_element,
			                    comp_after_preview));

		} else {
			FormComponentPage confirmation_page = component.getFormDocument()
			        .getFormConfirmationPage();

			if (confirmation_page == null)
				throw new NullPointerException(
				        "Confirmation page not found, but preview element exists.");

			FormComponentButtonArea button_area = (FormComponentButtonArea) confirmation_page
			        .getButtonArea();

			appendPreviewElement(component, confirmation_page
			        .getComponentDataBean().getElement(),
			    button_area == null ? null : button_area.getComponentDataBean()
			            .getElement());
		}
	}

	@Override
	public void removeComponentFromXFormsDocument(FormComponent component) {

		removeComponentLocalization(component);
		removeComponentBindings(component);

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		Element remove_this = xformsComponentDataBean.getPreviewElement();
		if (remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);

		remove_this = xformsComponentDataBean.getKeyExtInstance();
		if (remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);

		remove_this = xformsComponentDataBean.getKeySetvalue();
		if (remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);

		remove_this = xformsComponentDataBean.getElement();
		if (remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);
	}

	protected void removeComponentBindings(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();
		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		if (componentBind.exists()) {

			Bind bind = componentBind.getBind();

			if (bind.canRemove()) {

				Node bindElementParent = bind.getBindElement().getParentNode();
				Nodeset nodeset = bind.getNodeset();

				bind.remove();

				if (nodeset != null) {

					// TODO: perhaps move this code to nodeset.remove, if that's special case (non
					// normal and throw exception or at least log)
					// we don't remove nodeset if there exists any bind elements
					// pointing to this nodeset
					NodeList bindsByNodeset = FormManagerUtil
					        .getBindsByNodeset(bindElementParent, nodeset
					                .getPath());

					if (bindsByNodeset == null
					        || bindsByNodeset.getLength() == 0)
						nodeset.remove();
				}
			}
		}
	}

	protected void removeComponentLocalization(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		NodeList children = xformsComponentDataBean.getElement()
		        .getElementsByTagName("*");

		Element loc_model = FormManagerUtil.getElementById(component
		        .getFormDocument().getXformsDocument(),
		    FormManagerUtil.data_mod);

		Element loc_strings = (Element) loc_model.getElementsByTagName(
		    FormManagerUtil.loc_tag).item(0);

		for (int i = 0; i < children.getLength(); i++) {

			Element child = (Element) children.item(i);

			String ref = child.getAttribute(FormManagerUtil.ref_s_att);

			if (FormManagerUtil.isLocalizableExpressionCorrect(ref)) {

				String key = FormManagerUtil.getKeyFromRef(ref);

				// those elements should be the child nodes
				NodeList localization_elements = loc_strings
				        .getElementsByTagName(key);

				if (localization_elements != null) {

					int elements_count = localization_elements.getLength();

					for (int j = 0; j < elements_count; j++) {

						loc_strings.removeChild(localization_elements.item(0));
					}
				}
			}
		}
	}

	protected void updateP3pType(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();
		PropertiesComponent props = component.getProperties();

		// TODO: check if bind exists
		xformsComponentDataBean.getComponentBind().getBind().setP3pType(
		    props.getP3ptype());
	}

	protected void updateVariableName(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		if (componentBind.exists()) {

			Bind bind = componentBind.getBind();

			PropertiesComponent properties = component.getProperties();
			String variableStringRepresentation = properties.getVariable() == null ? null
			        : properties.getVariable().getDefaultStringRepresentation();

			Nodeset nodeset = bind.getNodeset();

			if (variableStringRepresentation == null) {

				if (nodeset != null) {

					nodeset.removeMapping(component);
				}

			} else {

				if (nodeset == null) {

					nodeset = getNodesetFactory(component.getFormDocument())
					        .create(
					            FormManagerUtil
					                    .getFormInstanceModelElement(component
					                            .getFormDocument()
					                            .getXformsDocument()),
					            bind.getId());

					bind.setNodeset(nodeset);
				}

				nodeset.setMapping(component, variableStringRepresentation);
			}
		}
	}

	protected void updateHtmlEditor(FormComponent component) {
		ComponentDataBean xformsComponentDataBean = component.getComponentDataBean();

		boolean htmlEditor = component.getProperties().isUseHtmlEditor();
		String htmlEditorClassName = htmlEditor ? HTML_EDITOR_SYLE_CLASS_NAME : CoreConstants.EMPTY;

		Element element = xformsComponentDataBean.getElement();

		String currentClassValue = element.getAttribute("class");
		String newClassValue = currentClassValue == null ? CoreConstants.EMPTY : new StringBuilder(currentClassValue).toString();
		boolean needToUpdate = true;
		if (htmlEditor) {
			newClassValue = StringUtil.isEmpty(newClassValue) ?
					htmlEditorClassName :
					new StringBuilder(newClassValue).append(CoreConstants.SPACE).append(htmlEditorClassName).toString();
		} else if (StringUtil.isEmpty(newClassValue)) {
			needToUpdate = false;
		} else {
			newClassValue = newClassValue.replaceAll(HTML_EDITOR_SYLE_CLASS_NAME, CoreConstants.EMPTY);
		}

		if (needToUpdate) {
			element.setAttribute("class", newClassValue);
		}
	}

	protected void updateAutofillKey(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		PropertiesComponent props = component.getProperties();
		String autofillKey = props.getAutofillKey();

		if (autofillKey == null
		        && (xformsComponentDataBean.getKeyExtInstance() != null || xformsComponentDataBean
		                .getKeySetvalue() != null)) {

			Element rem_el = xformsComponentDataBean.getKeyExtInstance();
			if (rem_el != null)
				rem_el.getParentNode().removeChild(rem_el);
			xformsComponentDataBean.setKeyExtInstance(null);

			rem_el = xformsComponentDataBean.getKeySetvalue();
			if (rem_el != null)
				rem_el.getParentNode().removeChild(rem_el);
			xformsComponentDataBean.setKeySetvalue(null);

		} else if (autofillKey != null) {

			autofillKey = FormManagerUtil.autofill_key_prefix + autofillKey;

			String src = FormManagerUtil.context_att_pref + autofillKey;

			if (xformsComponentDataBean.getKeyExtInstance() != null) {

				xformsComponentDataBean.getKeyExtInstance().setAttribute(
				    FormManagerUtil.src_att, src);

			} else {

				Element model = component.getFormDocument()
				        .getFormDataModelElement();
				Element instanceElement = model.getOwnerDocument()
				        .createElementNS(model.getNamespaceURI(),
				            FormManagerUtil.instance_tag);
				instanceElement.setAttribute(FormManagerUtil.relevant_att,
				    FormManagerUtil.xpath_false);

				instanceElement = (Element) model.appendChild(instanceElement);
				instanceElement.setAttribute(FormManagerUtil.src_att, src);
				instanceElement.setAttribute(FormManagerUtil.id_att, component
				        .getId()
				        + FormManagerUtil.autofill_instance_ending);
				xformsComponentDataBean.setKeyExtInstance(instanceElement);
			}

			String value = new StringBuilder(FormManagerUtil.inst_start)
			        .append(
			            xformsComponentDataBean.getKeyExtInstance()
			                    .getAttribute(FormManagerUtil.id_att)).append(
			            FormManagerUtil.inst_end).append(FormManagerUtil.slash)
			        .append(autofillKey).toString();

			if (xformsComponentDataBean.getKeySetvalue() != null) {

				xformsComponentDataBean.getKeySetvalue().setAttribute(
				    FormManagerUtil.value_att, value);

			} else {
				Element autofill_model = component.getFormDocument()
				        .getAutofillModelElement();
				Element setval_el = autofill_model.getOwnerDocument()
				        .createElementNS(autofill_model.getNamespaceURI(),
				            FormManagerUtil.setvalue_tag);
				setval_el = (Element) autofill_model.appendChild(setval_el);

				setval_el.setAttribute(FormManagerUtil.bind_att,
				    xformsComponentDataBean.getComponentBind().getBind()
				            .getId());

				setval_el.setAttribute(FormManagerUtil.value_att, value);
				setval_el.setAttribute(FormManagerUtil.model_att, component
				        .getFormDocument().getFormDataModelElement()
				        .getAttribute(FormManagerUtil.id_att));
				setval_el.setAttribute(FormManagerUtil.id_att, component
				        .getId()
				        + FormManagerUtil.autofill_setvalue_ending);
				xformsComponentDataBean.setKeySetvalue(setval_el);
			}
		}
	}

	public void updateCalculate(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		PropertiesComponent props = component.getProperties();

		if (componentBind.exists() && props.isCalculate())
			componentBind.getBind().setCalculate(props.getCalculateExp());
		else
			componentBind.getBind().setCalculate(null);
	}

	@Override
	public String getCaculate(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component.getComponentDataBean();

		ComponentBind componentBind = xformsComponentDataBean.getComponentBind();

		if (!componentBind.exists()) {
			return CoreConstants.EMPTY;
		}
	    return componentBind.getBind().getCalculate().equals(CoreConstants.EMPTY) ? CoreConstants.EMPTY : componentBind.getBind().getCalculate();
    }

	@Override
	public boolean isCalculate(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		return componentBind.exists() && componentBind.getBind().isCalculate();
	}

	@Override
	public LocalizedStringBean getLocalizedStrings(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		return FormManagerUtil.getLabelLocalizedStrings(xformsComponentDataBean
		        .getElement(), component.getFormDocument().getXformsDocument());
	}

	@Override
	public boolean isRequired(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();
		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		return componentBind.exists() && componentBind.getBind().isRequired();
	}

	public boolean isReadonly(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();
		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		return componentBind.exists() && componentBind.getBind().isReadonly();
	}

	@Override
	public Map<ErrorType, LocalizedStringBean> getErrorLabelLocalizedStrings(
	        FormComponent component) {
		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();
		return FormManagerUtil
		        .getErrorLabelLocalizedStrings(xformsComponentDataBean
		                .getElement());
	}

	@Override
	public LocalizedStringBean getHelpText(FormComponent component) {
		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		return FormManagerUtil
		        .getHelpTextLocalizedStrings(xformsComponentDataBean
		                .getElement());
	}

	@Override
	public Variable getVariable(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		if (componentBind.exists()) {

			Bind bind = componentBind.getBind();
			Nodeset nodeset = bind.getNodeset();

			// TODO: probably impossible to have bind without nodeset - remove this check
			if (nodeset == null)
				return null;

			String mapping = nodeset.getMapping();

			return StringUtil.isEmpty(mapping) ? null : Variable
			        .parseDefaultStringRepresentation(mapping);
		} else {
			return null;
		}
	}

	@Override
	public void loadConfirmationElement(FormComponent component,
	        FormComponentPage confirmation_page) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		Element preview_element = FormManagerUtil.getElementById(component
		        .getFormDocument().getXformsDocument(), FormManagerUtil.preview
		        + '.' + component.getId());

		if (preview_element != null) {
			xformsComponentDataBean.setPreviewElement(preview_element);
			return;
		}

		if (confirmation_page == null)
			return;

		ComponentBind componentBind = xformsComponentDataBean
		        .getComponentBind();

		if (componentBind.exists()) {

			// creating new preview element
			FormComponent component_after_this = component.getNextSibling();
			Element page_element = confirmation_page.getComponentDataBean()
			        .getElement();

			if (component_after_this != null) {

				Element preview_after = null;

				// if preview_after == null, that could mean 2 things:
				// - errornous form xforms document (ignore)
				// - form component is not "normal" component (default), taking next
				// if exists
				while (component_after_this != null
				        && (preview_after = component_after_this
				                .getComponentDataBean().getPreviewElement()) == null)
					component_after_this = component_after_this
					        .getNextSibling();

				if (preview_after == null)
					appendPreviewElement(component, page_element,
					    confirmation_page.getButtonArea() == null ? null
					            : ((FormComponent) confirmation_page
					                    .getButtonArea())
					                    .getComponentDataBean().getElement());
				else {

					Element output_element = createPreviewElement(component);
					output_element = (Element) preview_after.getParentNode()
					        .insertBefore(output_element, preview_after);
					xformsComponentDataBean.setPreviewElement(output_element);
				}

			} else
				appendPreviewElement(component, page_element, confirmation_page
				        .getButtonArea() == null ? null
				        : ((FormComponent) confirmation_page.getButtonArea())
				                .getComponentDataBean().getElement());
		}
	}

	protected Element createPreviewElement(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		Element output_element = component.getFormDocument()
		        .getXformsDocument().createElementNS(
		            xformsComponentDataBean.getElement().getNamespaceURI(),
		            FormManagerUtil.output_tag);

		output_element.setAttribute(FormManagerUtil.id_att,
		    FormManagerUtil.preview + '.' + component.getId());
		output_element.setAttribute(FormManagerUtil.bind_att,
		    xformsComponentDataBean.getComponentBind().getBind().getId());

		Element component_element = xformsComponentDataBean.getElement();
		Element component_label = DOMUtil.getChildElement(component_element,
		    FormManagerUtil.label_tag);

		if (component_label != null) {

			Element cloned_label = (Element) component_label.cloneNode(true);
			output_element.appendChild(cloned_label);
		}
		return output_element;
	}

	protected void appendPreviewElement(FormComponent component,
	        Element page_element, Element button_area) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		Element output_element = createPreviewElement(component);

		if (button_area == null)
			output_element = (Element) page_element.appendChild(output_element);
		else
			output_element = (Element) button_area.getParentNode()
			        .insertBefore(output_element, button_area);

		xformsComponentDataBean.setPreviewElement(output_element);
	}

	@Override
	public String getAutofillKey(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component
		        .getComponentDataBean();

		Element inst = xformsComponentDataBean.getKeyExtInstance();

		if (inst == null)
			return null;
		String src = inst.getAttribute(FormManagerUtil.src_att);
		String key = src.substring(FormManagerUtil.context_att_pref.length());

		return key.startsWith(FormManagerUtil.autofill_key_prefix) ? key
		        .substring(FormManagerUtil.autofill_key_prefix.length()) : key;
	}

	protected BindFactory getBindFactory(FormDocument formDocument) {

		return BindFactory.getBindFactory(formDocument);
	}

	protected NodesetFactory getNodesetFactory(FormDocument formDocument) {

		return NodesetFactory.getNodesetFactory(formDocument);
	}

	@Override
	public boolean isUseHtmlEditor(FormComponent component) {
		ComponentDataBean xformsComponentDataBean = component.getComponentDataBean();

		Element element = xformsComponentDataBean.getElement();
		if (element == null) {
			return false;
		}

		String currentClassValue = element.getAttribute("class");
		return StringUtil.isEmpty(currentClassValue) ? Boolean.FALSE : currentClassValue.indexOf(HTML_EDITOR_SYLE_CLASS_NAME) != -1;
	}
}