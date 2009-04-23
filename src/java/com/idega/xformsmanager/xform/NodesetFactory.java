package com.idega.xformsmanager.xform;

import org.chiba.xml.dom.DOMUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;
import com.idega.util.xml.XPathUtil;
import com.idega.xformsmanager.component.FormDocument;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $ Last modified: $Date: 2009/04/23 14:17:14 $ by $Author: civilis $
 */
@Service(NodesetFactory.beanIdentifier)
@Scope("singleton")
public class NodesetFactory {
	
	static final String beanIdentifier = "xformsNodesetFactory";
	
	private static XPathUtil nodesetElementParentXPath;
	private static XPathUtil nodesetElementByMappingXPath;
	
	private static final String nodesetPathVariable = "nodesetPath";
	private static final String mappingVariable = "mapping";
	private static final String instanceIdVariable = "instanceId";
	
	private static final XPathUtil nodesetElementXPath = new XPathUtil(
	        ".//*[name(.) = $nodesetPath]");
	
	private ThreadLocal<FormDocument> formDocumentLocal = new ThreadLocal<FormDocument>();
	
	public static NodesetFactory getNodesetFactory(FormDocument formDocument) {
		
		NodesetFactory bindFactory = ELUtil.getInstance().getBean(
		    beanIdentifier);
		bindFactory.formDocumentLocal.set(formDocument);
		
		return bindFactory;
	}
	
	/**
	 * @param model
	 * @param nodesetPath
	 * @param parentBindNodeset
	 *            - if not provided @see javadoc of locate(Element model, String nodesetPath), else
	 *            parent bind nodeset element is used as context for the current nodeset path
	 * @return
	 */
	public Nodeset locate(Element model, String nodesetPath,
	        Nodeset parentBindNodeset) {
		
		final Element nodesetElement;
		
		if (parentBindNodeset == null) {
			
			Element instanceElement = findInstance(model, nodesetPath);
			String pathToLookWith = nodesetPath;
			
			// TODO: use chiba to locate those nodesets (modelitem)
			if (pathToLookWith.startsWith("instance")) {
				pathToLookWith = pathToLookWith.substring(pathToLookWith
				        .indexOf(CoreConstants.SLASH) + 1);
			}
			
			pathToLookWith = pathToLookWith.contains(CoreConstants.SLASH) ? pathToLookWith
			        .substring(0, pathToLookWith.indexOf(CoreConstants.SLASH))
			        : pathToLookWith;
			
			synchronized (nodesetElementXPath) {
				
				nodesetElementXPath.clearVariables();
				
				nodesetElementXPath.setVariable(nodesetPathVariable,
				    pathToLookWith);
				nodesetElement = (Element) nodesetElementXPath
				        .getNode(instanceElement);
			}
		} else {
			// when parent bind nodeset is provided, considering that this
			// nodeset path will be relative to the parent one
			
			XPathUtil nodesetXPath = new XPathUtil(nodesetPath);
			nodesetElement = nodesetXPath.getNode(parentBindNodeset
			        .getNodesetElement());
		}
		
		if (nodesetElement == null)
			return null;
		
		Nodeset nodeset = new Nodeset(getFormDocument());
		nodeset.setPath(nodesetPath);
		nodeset.setNodesetElement(nodesetElement);
		nodeset.setParentBindNodeset(parentBindNodeset);
		
		return nodeset;
	}
	
	/**
	 * locates nodeset element in xf:instance/data. only simple path (i.e. the element name that's
	 * the child of data element) is being supported
	 * 
	 * @param model
	 *            - current
	 * @param nodesetPath
	 *            - only simple path (i.e. the element name that's the child of data element) is
	 *            being supported
	 * @return Nodeset object with nodeset relevant data
	 */
	public Nodeset locate(Element model, String nodesetPath) {
		
		return locate(model, nodesetPath, null);
	}
	
	public Nodeset locateByMapping(Element model, String mapping) {
		
		Element instance = FormManagerUtil.getInstanceElement(model);
		XPathUtil nodesetElementByMappingXPath = getNodesetElementByMappingXPath();
		
		Element nodesetElement;
		
		synchronized (nodesetElementByMappingXPath) {
			
			nodesetElementByMappingXPath.clearVariables();
			nodesetElementByMappingXPath.setVariable(mappingVariable, mapping);
			nodesetElement = (Element) nodesetElementByMappingXPath
			        .getNode(instance);
		}
		
		if (nodesetElement == null)
			return null;
		
		Nodeset nodeset = new Nodeset(getFormDocument());
		nodeset.setPath(nodesetElement.getNodeName());
		nodeset.setNodesetElement(nodesetElement);
		
		return nodeset;
	}
	
	private Element findInstance(Element model, String nodesetPath) {
		
		String instanceId = nodesetPath.contains(FormManagerUtil.inst_start) ?

		nodesetPath.substring(nodesetPath.indexOf(FormManagerUtil.inst_start)
		        + FormManagerUtil.inst_start.length(), nodesetPath
		        .indexOf(FormManagerUtil.inst_end)) : null;
		
		if (nodesetPath.contains(FormManagerUtil.slash))
			nodesetPath = nodesetPath.substring(0, nodesetPath
			        .indexOf(FormManagerUtil.slash));
		
		Element instance;
		
		if (StringUtil.isEmpty(instanceId)) {
			instance = FormManagerUtil.getInstanceElement(model);
			
		} else {
			
			XPathUtil xpath = FormManagerUtil.getInstanceElementByIdXPath();
			
			synchronized (xpath) {
				
				xpath.clearVariables();
				xpath.setVariable(instanceIdVariable, instanceId);
				instance = (Element) xpath.getNode(model);
			}
		}
		
		return instance;
	}
	
	/**
	 * Creates nodeset element and places it under xf:instance/data element under model provided
	 * 
	 * @param model
	 *            - instance to place nodeset in
	 * @param nodesetPath
	 *            - path of nodeset. currently supported simple nodeset element name only
	 * @return created nodeset object
	 */
	public Nodeset create(Element model, String nodesetPath) {
		
		Nodeset nodeset = locate(model, nodesetPath);
		
		if (nodeset == null) {
			
			Element instance = findInstance(model, nodesetPath);
			Document xform = model.getOwnerDocument();
			// create
			Element nodesetElement = xform.createElement(nodesetPath);
			Element parent = (Element) getNodesetElementParentXPath().getNode(
			    instance);
			parent.appendChild(nodesetElement);
			
			nodeset = new Nodeset(getFormDocument());
			nodeset.setNodesetElement(nodesetElement);
			nodeset.setPath(nodesetPath);
		}
		
		return nodeset;
	}
	
	public Nodeset importNodeset(Element model, Nodeset nodesetToImport,
	        String newNodesetName) {
		
		// TODO: check if nodeset by such name already exist
		
		Element nodesetElement = nodesetToImport.getNodesetElement();
		String path = nodesetToImport.getPath();
		path = path.replaceFirst(nodesetElement.getNodeName(), newNodesetName);
		
		nodesetElement = (Element) model.getOwnerDocument().importNode(
		    nodesetElement, true);
		nodesetElement = (Element) model.getOwnerDocument().renameNode(
		    nodesetElement, nodesetElement.getNamespaceURI(), newNodesetName);
		
		Element instance = findInstance(model, path);
		// Element instance = FormManagerUtil.getInstanceElement(model);
		Element parent = DOMUtil.getFirstChildElement(instance);
		// (Element) getNodesetElementParentXPath().getNode(
		// instance);
		parent.appendChild(nodesetElement);
		
		Nodeset nodeset = new Nodeset(getFormDocument());
		nodeset.setNodesetElement(nodesetElement);
		nodeset.setPath(path);
		
		return nodeset;
	}
	
	private synchronized XPathUtil getNodesetElementByMappingXPath() {
		
		if (nodesetElementByMappingXPath == null)
			nodesetElementByMappingXPath = new XPathUtil(
			        ".//*[@mapping = $mapping]");
		
		return nodesetElementByMappingXPath;
	}
	
	private synchronized XPathUtil getNodesetElementParentXPath() {
		
		if (nodesetElementParentXPath == null)
			nodesetElementParentXPath = new XPathUtil("./data");
		
		return nodesetElementParentXPath;
	}
	
	public Nodeset cloneNodeset(Nodeset cloneableNodeset) {
		
		Nodeset nodeset = new Nodeset(cloneableNodeset.getFormDocument());
		nodeset.setNodesetElement(cloneableNodeset.getNodesetElement());
		nodeset.setPath(cloneableNodeset.getPath());
		
		return nodeset;
	}
	
	public FormDocument getFormDocument() {
		return formDocumentLocal.get();
	}
}