package com.idega.xformsmanager.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.xformsmanager.component.FormComponentType;

/**
 * TODO: make formComponentImpl and extending classes spring beans, and move
 * this there
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 * 
 *          Last modified: $Date: 2008/11/20 16:31:27 $ by $Author: civilis $
 */
@Service
@Scope("singleton")
public class XFormsManagerFactory {

	@Autowired
	@FormComponentType(FormComponentType.base)
	private XFormsManager xformsManager;
	@Autowired
	@FormComponentType(FormComponentType.button)
	private XFormsManagerButton xformsManagerButton;
	@Autowired
	@FormComponentType(FormComponentType.container)
	private XFormsManagerContainer xformsManagerContainer;
	@Autowired
	@FormComponentType(FormComponentType.document)
	private XFormsManagerDocument xformsManagerDocument;
	@Autowired
	@FormComponentType(FormComponentType.page)
	private XFormsManagerPage xformsManagerPage;
	@Autowired
	@FormComponentType(FormComponentType.plain)
	private XFormsManagerPlain xformsManagerPlain;
	@Autowired
	@FormComponentType(FormComponentType.select)
	private XFormsManagerSelect xformsManagerSelect;
	@Autowired
	@FormComponentType(FormComponentType.multiupload)
	private XFormsManagerMultiUpload xformsManagerMultiUploadDescription;

	public XFormsManagerFactory() {
	}

	public XFormsManager getXformsManager() {
		return xformsManager;
	}

	public void setXformsManager(XFormsManager xformsManager) {
		this.xformsManager = xformsManager;
	}

	public XFormsManagerButton getXformsManagerButton() {
		return xformsManagerButton;
	}

	public void setXformsManagerButton(XFormsManagerButton xformsManagerButton) {
		this.xformsManagerButton = xformsManagerButton;
	}

	public XFormsManagerContainer getXformsManagerContainer() {
		return xformsManagerContainer;
	}

	public void setXformsManagerContainer(
			XFormsManagerContainer xformsManagerContainer) {
		this.xformsManagerContainer = xformsManagerContainer;
	}

	public XFormsManagerDocument getXformsManagerDocument() {
		return xformsManagerDocument;
	}

	public void setXformsManagerDocument(
			XFormsManagerDocument xformsManagerDocument) {
		this.xformsManagerDocument = xformsManagerDocument;
	}

	public XFormsManagerPage getXformsManagerPage() {
		return xformsManagerPage;
	}

	public void setXformsManagerPage(XFormsManagerPage xformsManagerPage) {
		this.xformsManagerPage = xformsManagerPage;
	}

	public XFormsManagerPlain getXformsManagerPlain() {
		return xformsManagerPlain;
	}

	public void setXformsManagerPlain(XFormsManagerPlain xformsManagerPlain) {
		this.xformsManagerPlain = xformsManagerPlain;
	}

	public XFormsManagerSelect getXformsManagerSelect() {
		return xformsManagerSelect;
	}

	public void setXformsManagerSelect(XFormsManagerSelect xformsManagerSelect) {
		this.xformsManagerSelect = xformsManagerSelect;
	}

	public XFormsManagerMultiUpload getXformsManagerMultiUploadDescription() {
		return xformsManagerMultiUploadDescription;
	}

	public void setXformsManagerMultiUploadDescription(
			XFormsManagerMultiUpload xformsManagerMultiUploadDescription) {
		this.xformsManagerMultiUploadDescription = xformsManagerMultiUploadDescription;
	}
}