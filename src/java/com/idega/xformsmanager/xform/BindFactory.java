package com.idega.xformsmanager.xform;

import java.util.ArrayList;
import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;
import com.idega.util.xml.XPathUtil;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
@Service(BindFactory.beanIdentifier)
@Scope("singleton")
public class BindFactory {
	
	static final String beanIdentifier = "xformsBindFactory";
	
	private static XPathUtil bindElementXPath;
	private static final String bindIdVariable = "bindId";
	private static final String modelIdVariable = "modelId";
	
	private ThreadLocal<FormDocument> formDocumentLocal = new ThreadLocal<FormDocument>();
	
	/**
	 * @param formDocument
	 * @return bindFactory with the formDocument set for the current thread. i.e. you cannot pass
	 *         the BindFactory instance amongst threads (get new factory for separate thread)
	 */
	public static BindFactory getBindFactory(FormDocument formDocument) {
		
		BindFactory bindFactory = ELUtil.getInstance().getBean(beanIdentifier);
		bindFactory.formDocumentLocal.set(formDocument);
		
		return bindFactory;
	}
	
	/**
	 * locates bind element in the xforms document using xpath //xf:bind[@id=$bindId] creates new
	 * Bind object, which contains bind relevant data
	 * 
	 * @param bindId
	 *            - bind id
	 * @return - Bind object, which contains bind relevant data
	 */
	public Bind locate(String bindId) {
		
		XPathUtil bindElementXPath = getBindElementXPath();
		FormDocument formDocument = getFormDocument();
		Element bindElement;
		
		synchronized (bindElementXPath) {
			
			bindElementXPath.clearVariables();
			bindElementXPath.setVariable(bindIdVariable, bindId);
			bindElement = (Element) bindElementXPath.getNode(formDocument
			        .getXformsDocument());
		}
		
		if (bindElement == null)
			return null;
		
		Bind bind = createBind();
		bind.setId(bindId);
		bind.setBindElement(bindElement);
		// bind.setFormComponent(formComponent);
		
		loadChildBinds(bind);
		
		return bind;
	}
	
	private Bind createBind() {
		
		return new Bind(getFormDocument());
	}
	
	private void loadChildBinds(Bind parentBind) {
		
		Element parentBindElement = parentBind.getBindElement();
		
		@SuppressWarnings("unchecked")
		List<Element> childElements = DOMUtil
		        .getChildElements(parentBindElement);
		
		if (childElements != null && !childElements.isEmpty()) {
			
			ArrayList<Bind> childBinds = new ArrayList<Bind>(childElements
			        .size());
			
			for (Element element : childElements) {
				
				if (FormManagerUtil.bind_tag.equals(element.getNodeName())) {
					
					// load child bind
					Bind childBind = load(element);
					childBind.setParentBind(parentBind);
					childBinds.add(childBind);
				}
			}
			
			parentBind.setChildBinds(childBinds);
		}
	}
	
	private Element getModel(Document xform, String modelId) {
		
		Element model;
		
		if (StringUtil.isEmpty(modelId))
			model = FormManagerUtil.getDefaultFormModelElement(xform);
		
		else {
			
			XPathUtil xpath = FormManagerUtil.getFormModelElementByIdXPath();
			
			synchronized (xpath) {
				
				xpath.clearVariables();
				xpath.setVariable(modelIdVariable, modelId);
				model = (Element) xpath.getNode(xform);
			}
		}
		
		return model;
	}
	
	public Bind load(Element bindElement) {
		
		if (bindElement == null)
			return null;
		
		Bind bind = createBind();
		bind.setId(bindElement.getAttribute(FormManagerUtil.id_att));
		bind.setBindElement(bindElement);
		
		loadChildBinds(bind);
		
		return bind;
	}
	
	// TODO: use not bindId, but component id. and create bindId here
	public Bind create(String bindId, String modelId, Nodeset nodeset) {
		
		Bind bind = locate(bindId);
		
		if (bind == null) {
			
			FormDocument formDocument = getFormDocument();
			
			Document xform = formDocument.getXformsDocument();
			Element model = getModel(xform, modelId);
			
			// create
			Element bindElement = xform.createElementNS(
			    FormManagerUtil.xforms_namespace_uri, FormManagerUtil.bind_tag);
			bindElement.setAttribute(FormManagerUtil.id_att, bindId);
			
			model.appendChild(bindElement);
			
			bind = createBind();
			bind.setId(bindId);
			bind.setBindElement(bindElement);
		}
		
		bind.setNodeset(nodeset);
		
		return bind;
	}
	
	private boolean sharedBindExists(Document xform, String bindId) {
		
		return getSharedBindElement(xform, bindId) != null;
	}
	
	private Element getSharedBindElement(Document xform, String bindId) {
		
		return FormManagerUtil.getElementById(xform, bindId);
	}
	
	private Bind loadSharedBind(Document xform, String bindId) {
		
		Element sharedBindElement = getSharedBindElement(xform, bindId);
		Bind bind = load(sharedBindElement);
		
		return bind;
	}
	
	/**
	 * creates bind using template bind.
	 * 
	 * @param templateBind
	 * @param defaultBindId
	 *            will be used as bind id, if the template bind is not shared. otherwise, the
	 *            template bind id will be used
	 * @return newly created bind
	 */
	public Bind createFromTemplate(Bind templateBind, String defaultBindId) {
		
		FormDocument formDocument = getFormDocument();
		Document xform = formDocument.getXformsDocument();
		String templateBindId = templateBind.getId();
		
		if (templateBind.getIsShared()) {
			
			if (sharedBindExists(xform, templateBindId)) {
				
				Bind bind = loadSharedBind(xform, templateBindId);
				return bind;
			}
		}
		
		// if bind is shared, using the same id, as is in the template
		String newBindId = templateBind.getIsShared() ? templateBind.getId()
		        : defaultBindId;
		
		Element bindElement = (Element) xform.importNode(templateBind
		        .getBindElement(), true);
		bindElement.setAttribute(FormManagerUtil.id_att, newBindId);
		
		Element model = formDocument.getFormDataModelElement();
		model.appendChild(bindElement);
		
		String typeAtt = bindElement.getAttribute(FormManagerUtil.type_att);
		
		if (typeAtt != null && typeAtt.startsWith(FormManagerUtil.fb_)) {
			
			String newTypeAtt = newBindId + typeAtt;
			
			bindElement.setAttribute(FormManagerUtil.type_att, newTypeAtt);
			FormManagerUtil.copySchemaType(formDocument.getContext()
			        .getCacheManager().getComponentsXsd(), xform, typeAtt,
			    newTypeAtt);
		}
		
		Nodeset nodeset = templateBind.getNodeset();
		String nodesetName = templateBind.getIsShared() ? nodeset
		        .getNodesetElement().getNodeName() : newBindId;
		
		if (nodeset != null) {
			
			nodeset = getNodesetFactory().importNodeset(
			    FormManagerUtil.getFormInstanceModelElement(xform), nodeset,
			    nodesetName);
		}
		
		Bind bind = load(bindElement);
		bind.setNodeset(nodeset);
		bind.renameChildren(bind.getId());
		
		return bind;
	}
	
	/*
	public Bind cloneBind(Bind cloneableBind) {
		
		Bind bind = createBind();
		bind.setBindElement(cloneableBind.getBindElement());
		bind.setId(cloneableBind.getId());
		bind.setNodeset(cloneableBind.getNodeset() == null ? null
		        : getNodesetFactory().cloneNodeset(cloneableBind.getNodeset()));
		
		if (cloneableBind.getChildBinds() != null) {
			
			ArrayList<Bind> childBinds = new ArrayList<Bind>(cloneableBind
			        .getChildBinds().size());
			
			for (Bind childBind : cloneableBind.getChildBinds()) {
				
				Bind newChildBind = cloneBind(childBind);
				newChildBind.setParentBind(bind);
				childBinds.add(newChildBind);
			}
			
			bind.setChildBinds(childBinds);
		}
		
		return bind;
	}
	*/
	
	private synchronized XPathUtil getBindElementXPath() {
		
		if (bindElementXPath == null)
			bindElementXPath = new XPathUtil(".//xf:bind[@id=$bindId]");
		
		return bindElementXPath;
	}
	
	NodesetFactory getNodesetFactory() {
		
		return NodesetFactory.getNodesetFactory(getFormDocument());
	}
	
	FormDocument getFormDocument() {
		return formDocumentLocal.get();
	}
}