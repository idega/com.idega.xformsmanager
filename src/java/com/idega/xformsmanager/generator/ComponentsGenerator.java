package com.idega.xformsmanager.generator;

import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;

import com.idega.idegaweb.IWMainApplication;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/11/13 09:45:08 $ by $Author: civilis $
 */
public interface ComponentsGenerator {

	/**
	 * 
	 * @return <code>true</code> when init() method already called, 
	 * <code>false</code> otherwise;
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	public boolean isInstantiated();
	
	public abstract void init(IWMainApplication iwma,
			TransformerService transfService);

	public abstract Document generateHtmlRepresentation(Document xform);
}