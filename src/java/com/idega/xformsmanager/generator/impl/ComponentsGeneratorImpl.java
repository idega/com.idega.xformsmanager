package com.idega.xformsmanager.generator.impl;

import javax.xml.parsers.DocumentBuilder;

import org.chiba.adapter.ui.UIGenerator;
import org.chiba.web.flux.FluxAdapter;
import org.chiba.xml.xslt.TransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.idega.chiba.web.xml.xforms.connector.xslt.UIGeneratorFactory;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.xml.XmlUtil;
import com.idega.xformsmanager.generator.ComponentsGenerator;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 * 
 *          Last modified: $Date: 2008/11/13 13:44:49 $ by $Author: civilis $
 */
@Service
@Scope("singleton")
public class ComponentsGeneratorImpl implements ComponentsGenerator {

	private UIGenerator htmlRepresentationGenerator;
	@Autowired
	private UIGeneratorFactory uiGeneratorFactory;

	public void init(IWMainApplication iwma, TransformerService transfService) {

		if (htmlRepresentationGenerator == null) {

			try {
				htmlRepresentationGenerator = getUiGeneratorFactory()
						.createUIGenerator(transfService,
								iwma.getServletContext());
				
				htmlRepresentationGenerator.setParameter("scripted", false);
				htmlRepresentationGenerator.setParameter("formbuilder", true);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Document generateHtmlRepresentation(Document xform) {

		try {
			DocumentBuilder documentBuilder = XmlUtil.getDocumentBuilder();
			Document generatedDocument = documentBuilder.newDocument();

			FluxAdapter adapter = new FluxAdapter();
			adapter.setXForms(xform);
			adapter.init();
			
			UIGenerator gen = getHtmlRepresentationGenerator();
			
			synchronized (gen) {

				gen.setInput(xform);
				gen.setOutput(generatedDocument);
				gen.generate();
			}

			return generatedDocument;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected UIGenerator getHtmlRepresentationGenerator() {
		return htmlRepresentationGenerator;
	}

	UIGeneratorFactory getUiGeneratorFactory() {
		return uiGeneratorFactory;
	}

	@Override
	public boolean isInstantiated() {
		if (this.htmlRepresentationGenerator != null) {
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
}