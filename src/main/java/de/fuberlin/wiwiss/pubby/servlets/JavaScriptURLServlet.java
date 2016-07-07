package de.fuberlin.wiwiss.pubby.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import de.fuberlin.wiwiss.pubby.Configuration;
import de.fuberlin.wiwiss.pubby.HypermediaControls;
import de.fuberlin.wiwiss.pubby.ResourceDescription;

/**
 * Servlet for serving JavaScript documents based on a description
 * of a given resource.
 */
public class JavaScriptURLServlet extends BaseServlet {
	
	@Override
	protected boolean doGet(String relativeURI,
			HttpServletRequest request, 
			HttpServletResponse response,
			Configuration config) throws IOException {
		HypermediaControls controller = config.getControls(relativeURI, false);

		ResourceDescription description = controller == null ? 
				null : controller.getResourceDescription();
				
		// Check if resource exists in dataset
		if (description == null) {
			response.setStatus(404);
			response.setContentType("text/plain");
			response.getOutputStream().println("// Nothing known about <" + controller.getAbsoluteIRI() + ">");
			return true;
		}
		Model model = description.getModel();
		
		List<RDFNode> l = model.listObjectsOfProperty(ResourceFactory.createProperty("http://kdeg.scss.tcd.ie/ns/rrf#functionBody")).toList();
		for(RDFNode n : l) {
			if(n.isLiteral()) {
				String s = ((Literal) n).getValue().toString();
				response.getOutputStream().println(s);
			}
		}
		
		return true;
	}
	
	private static final long serialVersionUID = 1L;
}