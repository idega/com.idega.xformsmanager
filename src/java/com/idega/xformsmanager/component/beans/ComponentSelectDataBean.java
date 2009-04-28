package com.idega.xformsmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public class ComponentSelectDataBean extends ComponentDataBean {
	
	private Element local_itemset_instance;
	private Element external_itemset_instance;
	
	@Override
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentSelectDataBean();
	}
	
	public Element getExternalItemsetInstance() {
		return external_itemset_instance;
	}
	
	public void setExternalItemsetInstance(Element external_itemset_instance) {
		this.external_itemset_instance = external_itemset_instance;
	}
	
	public Element getLocalItemsetInstance() {
		return local_itemset_instance;
	}
	
	public void setLocalItemsetInstance(Element local_itemset_instance) {
		this.local_itemset_instance = local_itemset_instance;
	}
}
