package com.idega.xformsmanager.generator.impl;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;

import org.chiba.adapter.ui.UIGenerator;
import org.chiba.adapter.ui.XSLTGenerator;
import org.chiba.web.flux.FluxAdapter;
import org.chiba.xml.xslt.TransformerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.idega.idegaweb.IWMainApplication;
import com.idega.util.xml.XmlUtil;
import com.idega.xformsmanager.generator.ComponentsGenerator;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/11/13 09:45:06 $ by $Author: civilis $
 */
@Service
@Scope("singleton")
public class ComponentsGeneratorImpl implements ComponentsGenerator {

	private UIGenerator htmlRepresentationGenerator;

	public void init(IWMainApplication iwma, TransformerService transfService) {

		if (htmlRepresentationGenerator == null) {

			String html4uriStr = "file:"
					+ iwma.getApplicationRealPath()
					+ "idegaweb/bundles/org.chiba.web.bundle/resources/xslt/html4.xsl";

			URI html4uri = URI.create(html4uriStr);

			// TODO: unify createUIGenerator() at IdegaXFormsSessionBase and
			// this method

			XSLTGenerator gen = new XSLTGenerator();
			gen.setTransformerService(transfService);
			gen.setStylesheetURI(html4uri);
			gen.setParameter("selector-prefix", "s_");
			gen.setParameter("data-prefix", "d_");
			gen.setParameter("trigger-prefix", "t_");

			htmlRepresentationGenerator = gen;
		}
	}

	public Document generateHtmlRepresentation(Document xform) {

		try {
			UIGenerator gen = getHtmlRepresentationGenerator();

			FluxAdapter adapter = new FluxAdapter();
			adapter.setXForms(xform);
			adapter.init();

			gen.setInput(xform);

			DocumentBuilder documentBuilder = XmlUtil.getDocumentBuilder();
			Document generatedDocument = documentBuilder.newDocument();

			gen.setOutput(generatedDocument);
			gen.generate();

			return generatedDocument;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected UIGenerator getHtmlRepresentationGenerator() {
		return htmlRepresentationGenerator;
	}
}