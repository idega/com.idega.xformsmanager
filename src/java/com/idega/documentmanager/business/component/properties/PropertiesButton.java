package com.idega.documentmanager.business.component.properties;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/10/26 17:01:17 $ by $Author: civilis $
 */
public interface PropertiesButton extends PropertiesComponent {
	
	public abstract String getReferAction();
		
	public abstract void setReferAction(String referAction);
}