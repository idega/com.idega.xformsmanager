package com.idega.xformsmanager.xform;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;

import com.idega.util.StringUtil;
import com.idega.xformsmanager.component.FormComponent;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * represents bind nodeset attribute
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $ Last modified: $Date: 2009/04/29 10:49:03 $ by $Author: civilis $
 */
public class Nodeset {
	
	/**
	 * this path should be the same as the value of the bind nodeset attribute
	 */
	private String path;
	private Element nodesetElement;
	private Nodeset parentBindNodeset;
	private FormDocument formDocument;
	
	public Nodeset(FormDocument formDocument) {
		
		this.formDocument = formDocument;
	}
	
	public String getContent() {
		return nodesetElement.getTextContent();
	}
	
	public void setContent(String content) {
		nodesetElement.setTextContent(content);
	}
	
	public String getMapping() {
		
		return getNodesetElement().getAttribute(FormManagerUtil.mapping_att);
	}
	
	public void setMapping(String mapping) {
		
		if (mapping == null)
			getNodesetElement().removeAttribute(FormManagerUtil.mapping_att);
		else
			getNodesetElement().setAttribute(FormManagerUtil.mapping_att,
			    mapping);
	}
	
	public String getPath() {
		return path;
	}
	
	protected void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return xpath usable path to the model item. e.g. instance('data-instance')/mymodelitem
	 */
	public String getXPathPath() {
		
		return "instance('data-instance')/" + getPath();
	}
	
	public Element getNodesetElement() {
		return nodesetElement;
	}
	
	protected void setNodesetElement(Element nodesetElement) {
		this.nodesetElement = nodesetElement;
	}
	
	public void remove() {
		
		getNodesetElement().getParentNode().removeChild(getNodesetElement());
		path = null;
		
		if (!StringUtil.isEmpty(getMapping())) {
			
			mappingSiblingChanged(getMapping());
		}
	}
	
	public void rename(String newName) {
		
		String path = getPath();
		Element nodesetElement = getNodesetElement();
		
		path = path.replaceFirst(nodesetElement.getNodeName(), newName);
		nodesetElement = (Element) nodesetElement.getOwnerDocument()
		        .renameNode(nodesetElement, nodesetElement.getNamespaceURI(),
		            newName);
		setNodesetElement(nodesetElement);
		setPath(path);
	}
	
	/**
	 * sets mapping for the nodeset, and also, might add the mapping updater for the component
	 * element in case there are more than one component in the form with the same mapping
	 * 
	 * @param formComponent
	 * @param mappingExpression
	 */
	public void setMapping(FormComponent formComponent, String mappingExpression) {
		
		if (StringUtil.isEmpty(mappingExpression)) {
			
			Logger.getLogger(getClass().getName()).log(
			    Level.WARNING,
			    "Passed empty mapping expression, removing mapping. Component = "
			            + formComponent.getId() + ", form id="
			            + formComponent.getFormDocument().getId());
			removeMapping(formComponent);
			
		} else if (!mappingExpression.equals(getMapping())) {
			
			String previousMapping = getMapping();
			setMapping(mappingExpression);
			FormComponentMapping mapping = formComponent
			        .getFormComponentMapping(this);
			mapping.setMapping(mappingExpression);
			mappingSiblingChanged(previousMapping, mappingExpression);
		}
	}
	
	public void removeMapping(FormComponent formComponent) {
		
		FormComponentMapping mapping = formComponent
		        .getFormComponentMapping(this);
		
		String previousMapping = getMapping();
		setMapping(null);
		mapping.removeMapping();
		
		if (!StringUtil.isEmpty(previousMapping)) {
			
			mappingSiblingChanged(previousMapping);
		}
	}
	
	private void mappingSiblingChanged(String... relevantMappings) {
		
		getFormDocument().mappingSiblingChanged(relevantMappings);
	}
	
	public Nodeset getParentBindNodeset() {
		return parentBindNodeset;
	}
	
	public void setParentBindNodeset(Nodeset parentBindNodeset) {
		this.parentBindNodeset = parentBindNodeset;
	}
	
	FormDocument getFormDocument() {
		return formDocument;
	}
	
	public void reactToMappingSiblingChanged(FormComponent formComponent,
	        String... relevantMappings) {
		
		if (isRelevantMapping(relevantMappings)) {
			
			FormComponentMapping mapping = formComponent
			        .getFormComponentMapping(this);
			mapping.setMapping(getMapping());
		}
	}
	
	private boolean isRelevantMapping(String... mappings) {
		
		String myMapping = getMapping();
		
		if (!StringUtil.isEmpty(myMapping)) {
			
			for (String mapping : mappings) {
				
				if (mapping.equals(myMapping))
					return true;
			}
		}
		
		return false;
	}
}