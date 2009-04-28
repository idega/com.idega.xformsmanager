package com.idega.xformsmanager.xform;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $ Last modified: $Date: 2009/04/28 12:27:48 $ by $Author: civilis $
 */
public interface FormComponentMapping {
	
	public abstract void setMapping(String mappingExpression);
	
	public abstract void removeMapping();
}