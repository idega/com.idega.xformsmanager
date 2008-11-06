package com.idega.xformsmanager.xform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.util.CoreConstants;
import com.idega.util.StringUtil;
import com.idega.util.xml.XPathUtil;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * represents bind nodeset attribute
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/06 11:39:38 $ by $Author: civilis $
 */
public class Nodeset implements Cloneable {

	/**
	 *	this path should be the same as the value of the bind nodeset attribute 
	 */
	private String path;
	private Element nodesetElement;
	private Nodeset parentBindNodeset;

	protected Nodeset() {
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

	private static final String instanceIdVariable = "instanceId";

	/**
	 * locates nodeset element in xf:instance/data. only simple path (i.e. the
	 * element name that's the child of data element) is being supported
	 * 
	 * @param model
	 *            - current
	 * @param nodesetPath
	 *            - only simple path (i.e. the element name that's the child of
	 *            data element) is being supported
	 * @return Nodeset object with nodeset relevant data
	 */
	public static Nodeset locate(Element model, String nodesetPath) {

		return locate(model, nodesetPath, null);
	}

	/**
	 * 
	 * @param model
	 * @param nodesetPath
	 * @param parentBindNodeset
	 *            - if not provided @see javadoc of locate(Element model, String
	 *            nodesetPath), else parent bind nodeset element is used as context for the current nodeset path
	 * @return
	 */
	public static Nodeset locate(Element model, String nodesetPath,
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

		Nodeset nodeset = new Nodeset();
		nodeset.setPath(nodesetPath);
		nodeset.setNodesetElement(nodesetElement);
		nodeset.setParentBindNodeset(parentBindNodeset);

		return nodeset;
	}

	public static Nodeset locateByMapping(Element model, String mapping) {

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

		Nodeset nodeset = new Nodeset();
		nodeset.setPath(nodesetElement.getNodeName());
		nodeset.setNodesetElement(nodesetElement);

		return nodeset;
	}

	private static Element findInstance(Element model, String nodesetPath) {

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
	 * Creates nodeset element and places it under xf:instance/data element
	 * under model provided
	 * 
	 * 
	 * @param model
	 *            - instance to place nodeset in
	 * @param nodesetPath
	 *            - path of nodeset. currently supported simple nodeset element
	 *            name only
	 * @return created nodeset object
	 */
	public static Nodeset create(Element model, String nodesetPath) {

		Nodeset nodeset = locate(model, nodesetPath);

		if (nodeset == null) {

			Element instance = findInstance(model, nodesetPath);
			Document xform = model.getOwnerDocument();
			// create
			Element nodesetElement = xform.createElement(nodesetPath);
			Element parent = (Element) getNodesetElementParentXPath().getNode(
					instance);
			parent.appendChild(nodesetElement);

			nodeset = new Nodeset();
			nodeset.setNodesetElement(nodesetElement);
			nodeset.setPath(nodesetPath);
		}

		return nodeset;
	}

	public static Nodeset importNodeset(Element model, Nodeset nodesetToImport,
			String newNodesetName) {
		
//		TODO: check if nodeset by such name already exist

		Element nodesetElement = nodesetToImport.getNodesetElement();
		String path = nodesetToImport.getPath();
		path = path.replaceFirst(nodesetElement.getNodeName(), newNodesetName);

		nodesetElement = (Element) model.getOwnerDocument().importNode(
				nodesetElement, true);
		nodesetElement = (Element) model.getOwnerDocument().renameNode(
				nodesetElement, nodesetElement.getNamespaceURI(),
				newNodesetName);

		Element instance = FormManagerUtil.getInstanceElement(model);
		Element parent = (Element) getNodesetElementParentXPath().getNode(
				instance);
		parent.appendChild(nodesetElement);

		Nodeset nodeset = new Nodeset();
		nodeset.setNodesetElement(nodesetElement);
		nodeset.setPath(path);
		
		System.out.println("nodeset path after import="+path);
		
		return nodeset;
	}

	private static XPathUtil nodesetElementParentXPath;
	private static XPathUtil nodesetElementByMappingXPath;

	private static final String nodesetPathVariable = "nodesetPath";
	private static final String mappingVariable = "mapping";

	private static final XPathUtil nodesetElementXPath = new XPathUtil(
			".//*[name(.) = $nodesetPath]");

	private static synchronized XPathUtil getNodesetElementByMappingXPath() {

		if (nodesetElementByMappingXPath == null)
			nodesetElementByMappingXPath = new XPathUtil(
					".//*[@mapping = $mapping]");

		return nodesetElementByMappingXPath;
	}

	private static synchronized XPathUtil getNodesetElementParentXPath() {

		if (nodesetElementParentXPath == null)
			nodesetElementParentXPath = new XPathUtil("./data");

		return nodesetElementParentXPath;
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
	}

	/**
	 * clones only nodeset object, but not the elements, that
	 * are referenced
	 */
	@Override
	public Nodeset clone() {

		Nodeset nodeset = new Nodeset();
//		nodeset.setNodesetElement((Element) (getNodesetElement() == null ? null
//				: getNodesetElement().cloneNode(true)));
		nodeset.setNodesetElement(getNodesetElement());
		nodeset.setPath(getPath());

		return nodeset;
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

	public Nodeset getParentBindNodeset() {
		return parentBindNodeset;
	}

	public void setParentBindNodeset(Nodeset parentBindNodeset) {
		this.parentBindNodeset = parentBindNodeset;
	}
}