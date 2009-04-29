package com.idega.xformsmanager.xform;

/**
 * represents component (not nodeset) mapping handling - i.e. handling situation, when more than one
 * component is referencing nodesets with the same mapping
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $ Last modified: $Date: 2009/04/29 10:48:43 $ by $Author: civilis $
 */
public interface FormComponentMapping {
	
	public abstract void setMapping(String mappingExpression);
	
	public abstract void removeMapping();
}