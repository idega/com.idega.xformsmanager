package com.idega.xformsmanager.xform;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;

import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * TODO: when bind is shared, all the components should point to the same bind object (shared bind
 * instance) also, bind could have more than one form component
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.14 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class Bind {
	
	private static final Logger logger = Logger.getLogger(Bind.class.getName());
	
	public Bind(FormDocument formDocument) {
		
		this.formDocument = formDocument;
	}
	
	private FormDocument formDocument;
	// private FormComponent formComponent;
	
	private String id;
	private Element bindElement;
	private Nodeset nodeset;
	private String type;
	private String constraint;
	private String p3pType;
	private Boolean isRequired;
	private Boolean readonly;
	private String relevant;
	private Boolean isRelevant;
	private Boolean isShared;
	private List<Bind> childBinds;
	private Bind parentBind;
	
	public Element getBindElement() {
		return bindElement;
	}
	
	protected void setBindElement(Element bindElement) {
		this.bindElement = bindElement;
	}
	
	public String getId() {
		
		if (id == null)
			id = getBindElement().getAttribute(FormManagerUtil.id_att);
		
		return id;
	}
	
	protected void setId(String id) {
		this.id = id;
	}
	
	public Nodeset getNodeset() {
		
		if (nodeset == null) {
			
			String nodesetPath = getBindElement().getAttribute(
			    FormManagerUtil.nodeset_att);
			
			if (StringUtil.isEmpty(nodesetPath)) {
				
				logger.log(Level.WARNING,
				    "No nodeset attribute for bind element. Bind id=" + getId()
				            + ", form id=" + getFormDocument().getId());
				return null;
			}
			
			Element model = (Element) getBindElement().getParentNode();
			
			if (getParentBind() == null) {
				
				nodeset = getNodesetFactory().locate(model, nodesetPath);
			} else {
				nodeset = getNodesetFactory().locate(model, nodesetPath,
				    getParentBind().getNodeset());
			}
			
			if (nodeset == null)
				throw new NullPointerException(
				        "Nodeset path ("
				                + nodesetPath
				                + ") specified in the nodeset attribute, but no nodeset found");
		}
		
		return nodeset;
	}
	
	NodesetFactory getNodesetFactory() {
		
		return NodesetFactory.getNodesetFactory(getFormDocument());
	}
	
	public void setNodeset(Nodeset nodeset) {
		this.nodeset = nodeset;
		
		if (nodeset == null)
			getBindElement().removeAttribute(FormManagerUtil.nodeset_att);
		else
			getBindElement().setAttribute(FormManagerUtil.nodeset_att,
			    nodeset.getPath());
	}
	
	public String getType() {
		
		if (type == null)
			type = getBindElement().getAttribute(FormManagerUtil.type_att);
		
		return type;
	}
	
	public void setType(String type) {
		
		this.type = type;
		
		if (type == null)
			getBindElement().removeAttribute(FormManagerUtil.type_att);
		else
			getBindElement().setAttribute(FormManagerUtil.type_att, type);
	}
	
	public String getConstraint() {
		
		if (constraint == null)
			constraint = getBindElement().getAttribute(
			    FormManagerUtil.constraint_att);
		
		return constraint;
	}
	
	public void setConstraint(String constraint) {
		
		this.constraint = constraint;
		
		if (constraint == null)
			getBindElement().removeAttribute(FormManagerUtil.constraint_att);
		else
			getBindElement().setAttribute(FormManagerUtil.constraint_att,
			    constraint);
	}
	
	public void remove() {
		
		Element bindElement = getBindElement();
		
		bindElement.getParentNode().removeChild(bindElement);
		setBindElement(null);
		id = null;
	}
	
	public void setIsRequired(boolean isRequired) {
		
		this.isRequired = isRequired;
		
		if (isRequired)
			getBindElement().setAttribute(FormManagerUtil.required_att,
			    "instance('control-instance')/required = 'true'");
		else
			getBindElement().removeAttribute(FormManagerUtil.required_att);
	}
	
	public boolean isRequired() {
		
		if (isRequired == null) {
			Element bind = getBindElement();
			isRequired = bind.hasAttribute(FormManagerUtil.required_att)
			        && bind.getAttribute(FormManagerUtil.required_att).length() != 0;
		}
		
		return isRequired;
	}
	
	public boolean isReadonly() {
		
		if (readonly == null) {
			Element bind = getBindElement();
			readonly = bind.hasAttribute(FormManagerUtil.readonly_att)
			        && bind.getAttribute(FormManagerUtil.readonly_att).length() != 0;
		}
		
		return readonly;
	}
	
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
		
		if (readonly)
			getBindElement().setAttribute(FormManagerUtil.readonly_att,
			    "instance('control-instance')/readonly = 'true'");
		else
			getBindElement().removeAttribute(FormManagerUtil.readonly_att);
	}
	
	public void rename(String renameTo) {
		
		if (!canRename()) {
			
			logger
			        .log(
			            Level.WARNING,
			            "Tried to rename bind, though it cannot be renamed (canRename == false). Form id= "
			                    + getFormDocument().getId()
			                    + ", bind id="
			                    + getId());
		} else {
			
			renameTo = FormManagerUtil.escapeNonXmlTagSymbols(renameTo.replace(
			    ' ', '_'));
			
			if (getParentBind() == null) {
				
				Nodeset nodeset = getNodeset();
				
				if (nodeset != null) {
					nodeset.rename(renameTo);
				}
				
				setNodeset(nodeset);
				
				/*
				TODO: inform all referencing ComponentBinds
				ComponentDataBean xformsComponentDataBean = getFormComponent()
				        .getComponentDataBean();
				
				if (xformsComponentDataBean.getPreviewElement() != null) {
					
					xformsComponentDataBean.getPreviewElement().setAttribute(
					    FormManagerUtil.ref_s_att,
					    getBindElement().getAttribute(
					        FormManagerUtil.nodeset_att));
				}
				*/
			} else {
				
				// when parent bind present, renaming current with this logic:
				// if the bind contains - symbol, rename only the string going
				// before -
				// e.g. multiupload-group, when renamed will be newname-group
				
				String currentId = getId();
				
				// currentId.substring(s)
				String postfix = currentId.substring(currentId
				        .indexOf(CoreConstants.MINUS));
				
				String newId = renameTo + postfix;
				
				setId(newId);
				getBindElement().setAttribute(FormManagerUtil.id_att, newId);
			}
		}
	}
	
	public void renameChildren(String newName) {
		
		if (getChildBinds() != null) {
			
			for (Bind childBind : getChildBinds()) {
				
				childBind.rename(newName);
			}
		}
	}
	
	public String getP3pType() {
		
		if (p3pType == null)
			p3pType = getBindElement()
			        .getAttribute(FormManagerUtil.p3ptype_att);
		
		return p3pType;
	}
	
	public void setP3pType(String p3pType) {
		
		if (p3pType == null)
			getBindElement().removeAttribute(FormManagerUtil.p3ptype_att);
		else
			getBindElement().setAttribute(FormManagerUtil.p3ptype_att, p3pType);
	}
	
	public String getRelevant() {
		
		if (relevant == null) {
			Element bind = getBindElement();
			relevant = bind.hasAttribute(FormManagerUtil.relevant_att) ? bind
			        .getAttribute(FormManagerUtil.relevant_att) : null;
		}
		
		return relevant;
	}
	
	public void setRelevant(String relevant) {
		this.relevant = relevant;
		
		if (relevant != null)
			getBindElement().setAttribute(FormManagerUtil.relevant_att,
			    relevant);
		else
			getBindElement().removeAttribute(FormManagerUtil.relevant_att);
	}
	
	/**
	 * @return if the attribute contains xpath expression true() or false(), null, if attribute
	 *         contains anything else
	 */
	public Boolean getIsRelevant() {
		
		if (isRelevant == null) {
			
			String relVal = getRelevant();
			
			if (FormManagerUtil.xpath_true.equals(relVal))
				isRelevant = true;
			else if (FormManagerUtil.xpath_false.equals(relVal))
				isRelevant = false;
		}
		
		return isRelevant;
	}
	
	public void setIsRelevant(Boolean isRelevant) {
		
		if (isRelevant == null)
			throw new IllegalArgumentException("No value provided");
		
		if (isRelevant)
			setRelevant(FormManagerUtil.xpath_true);
		else
			setRelevant(FormManagerUtil.xpath_false);
		
		this.isRelevant = isRelevant;
	}
	
	// public FormComponent getFormComponent() {
	//		
	// if (formComponent == null && getParentBind() != null) {
	// formComponent = getParentBind().getFormComponent();
	// }
	//		
	// return formComponent;
	// }
	//	
	// public void setFormComponent(FormComponent formComponent) {
	// this.formComponent = formComponent;
	// }
	
	public Boolean getIsShared() {
		
		if (isShared == null) {
			
			Element bind = getBindElement();
			String isSharedAtt = bind.hasAttributeNS(
			    FormManagerUtil.idega_namespace, FormManagerUtil.shared_att) ? bind
			        .getAttributeNS(FormManagerUtil.idega_namespace,
			            FormManagerUtil.shared_att)
			        : null;
			
			isShared = isSharedAtt != null
			        && FormManagerUtil.true_string.equals(isSharedAtt);
		}
		
		return isShared;
	}
	
	public void setIsShared(Boolean isShared) {
		
		this.isShared = isShared;
		
		if (isShared != null && isShared)
			getBindElement().setAttributeNS(FormManagerUtil.idega_namespace,
			    FormManagerUtil.shared_att, FormManagerUtil.true_string);
		else
			getBindElement().removeAttributeNS(FormManagerUtil.idega_namespace,
			    FormManagerUtil.shared_att);
	}
	
	public List<Bind> getChildBinds() {
		return childBinds;
	}
	
	protected void setChildBinds(List<Bind> childBinds) {
		this.childBinds = childBinds;
	}
	
	protected Bind getParentBind() {
		return parentBind;
	}
	
	protected void setParentBind(Bind parentBind) {
		this.parentBind = parentBind;
	}
	
	FormDocument getFormDocument() {
		return formDocument;
	}
	
	public boolean canRename() {
		
		return !getIsShared();
	}
	
	public boolean canRemove() {
		
		return !getIsShared();
	}
	
	public boolean hasValidationConstraints() {
		
		return !StringUtil.isEmpty(getType())
		        || !StringUtil.isEmpty(getConstraint());
	}
}