package com.idega.xformsmanager.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/10/31 18:30:43 $ by $Author: civilis $
 */
@Service("htmlFact")
@Scope("singleton")
public class HtmlManagerFactory {

	@Autowired private HtmlManager 		htmlManager;
	
	public HtmlManager getHtmlManager() {
		return htmlManager;
	}
	public void setHtmlManager(HtmlManager htmlManager) {
		this.htmlManager = htmlManager;
	}
}