package com.idega.xformsmanager.business;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import com.idega.idegaweb.IWMainApplication;
import com.idega.util.IOUtil;
import com.idega.util.StringHandler;
import com.idega.util.xml.XmlUtil;
import com.idega.xformsmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 *          Last modified: $Date: 2008/12/28 11:53:45 $ by $Author: civilis $
 */
public class PersistedFormDocument {

	private Document xformsDocument;
	private Long formId;
	private String formType;
	private Integer version;

	private boolean outputReplaced;

	public Document getXformsDocument() {
		return xformsDocument;
	}

	public void setXformsDocument(Document xformsDocument) {
		if (!outputReplaced && IWMainApplication.getDefaultIWMainApplication().getSettings().getBoolean("xforms_idega_output", Boolean.FALSE)) {
			String xformInString = null;
			InputStream xformStream = null;
			try {
				//	Replacing buggy "xf:output" to "idega:output"
				StringWriter sw = new StringWriter();
				XmlUtil.prettyPrintDOM(xformsDocument, sw);
				xformInString = sw.toString();
				xformInString = StringHandler.replace(xformInString, "xf:output", FormManagerUtil.output_tag);

				//	Re-building XML document
				xformStream = StringHandler.getStreamFromString(xformInString);
				xformsDocument = XmlUtil.getDocumentBuilder().parse(xformStream);
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error converting String to XForm:\n" + xformInString, e);
			} finally {
				IOUtil.close(xformStream);
				outputReplaced = true;
			}
		}

		this.xformsDocument = xformsDocument;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}