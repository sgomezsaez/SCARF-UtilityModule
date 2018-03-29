package de.mackfn.kereta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.mackfn.kereta.objects.Application;
import de.mackfn.kereta.objects.Function;
import de.mackfn.kereta.objects.UtilityFunction;
import de.mackfn.kereta.tools.Connector;
import de.mackfn.kereta.tools.StaticTools;

@Path("/Search")
public class RESTSearch {

	private Connector con;
	private DocumentBuilder docBuilder;
	
	public RESTSearch()
	{
		this.con = new Connector(getClass().getClassLoader().getResource("/").getPath() + "kereta.xml");
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();
			
		} catch (Exception e) { }
	}
	
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response search(@Context UriInfo uriInfo) throws Exception
	{
		Document doc; 
		int status;
		
		Client client = Client.create();
		
		String resource = "";
		String appType = "";
		String fctType = "";
		
		MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters(); 
		if (uriParms.keySet().contains("resource")) resource = uriParms.getFirst("resource").toLowerCase();
		if (uriParms.keySet().contains("functionType")) fctType = uriParms.getFirst("functionType");
		if (uriParms.keySet().contains("applicationType")) appType = uriParms.getFirst("applicationType");
		status = 200;
		doc = this.docBuilder.newDocument();
		Element root = doc.createElement("selection");
		doc.appendChild(root);
		
		switch (resource)
		{
		case "application":
			
			List<String> aIds = Application.getApplicationIDsByType(con, appType);
			Iterator<String> aIt = aIds.iterator();
			while (aIt.hasNext())
			{
				WebResource webResource = client.resource(con.getKeretaRoot() + "/Application/" + aIt.next());
				ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
				if (response.getStatus() == 200) {	
					Document aDoc = response.getEntity(Document.class);
					Node aNode = aDoc.getDocumentElement();
					Node aCopy = doc.importNode(aNode, true);
					root.appendChild(aCopy);
				}
			}
			break;
		case "distribution":
			List<String> aIds2 = Application.getApplicationIDsByType(con, appType);
			Iterator<String> aIt2 = aIds2.iterator();
			while (aIt2.hasNext())
			{
				WebResource webResource = client.resource(con.getKeretaRoot() + "/Application/" + aIt2.next() + "/Distribution");
				ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
				if (response.getStatus() == 200) {	
					Document dDoc = response.getEntity(Document.class);
					Node dNodes = dDoc.getDocumentElement();
					for (int i=0; i<dNodes.getChildNodes().getLength(); i++)
					{
						Node dCopy = doc.importNode(dNodes.getChildNodes().item(i), true);
						root.appendChild(dCopy);
					}
				}
			}
			break;
			
		case "utilityfunction":
			List<String> ufIds = UtilityFunction.getApplicationTypeUtilityFunctionIDs(con, appType);
			Iterator<String> ufIt = ufIds.iterator();
			while (ufIt.hasNext())
			{
				WebResource webResource = client.resource(con.getKeretaRoot() + "/UtilityFunction/" + ufIt.next());
				ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
				if (response.getStatus() == 200) {	
					Document aDoc = response.getEntity(Document.class);
					Node aNode = aDoc.getDocumentElement();
					Node aCopy = doc.importNode(aNode, true);
					root.appendChild(aCopy);
				}
			}
			break;
			
		case "function":
			List<String> fctIds = new ArrayList<String>();
			if (!appType.equals(""))
			{
				List<String> ufIds2 = UtilityFunction.getApplicationTypeUtilityFunctionIDs(con, appType);
				Iterator<String> ufIt2 = ufIds2.iterator();
				while (ufIt2.hasNext())
				{
					List<String> tmpIds = Function.getUtilityFunctionFunctionIDs(con, ufIt2.next(), fctType);
					Iterator<String> tmpIt = tmpIds.iterator();
					while (tmpIt.hasNext()) 
					{
						String fId = tmpIt.next();
						if (!fctIds.contains(fId)) fctIds.add(fId);
					}
				}
			}
			else fctIds = Function.getFunctionIDs(con, fctType);
			
			Iterator<String> fctIt = fctIds.iterator();
			while (fctIt.hasNext())
			{
				WebResource webResource = client.resource(con.getKeretaRoot() + "/Function/" + fctIt.next());
				ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
				if (response.getStatus() == 200) {	
					Document aDoc = response.getEntity(Document.class);
					Node aNode = aDoc.getDocumentElement();
					Node aCopy = doc.importNode(aNode, true);
					root.appendChild(aCopy);
				}
			}
			
			break;
			
		default:
			doc = StaticTools.getErrorDoc("missing or illegal query parameter 'resource' - value can be 'Application', 'Distribution', 'UtilityFunction' or 'Function'");
			status = 400;	
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
}
