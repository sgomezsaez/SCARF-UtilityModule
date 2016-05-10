package de.mackfn.kereta;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import org.w3c.dom.NodeList;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.mackfn.kereta.objects.Application;
import de.mackfn.kereta.objects.Requirement;
import de.mackfn.kereta.objects.Distribution;
import de.mackfn.kereta.tools.Connector;
import de.mackfn.kereta.tools.StaticTools;

@Path("/Application")
public class RESTApplication {

	private Connector con;
	private DocumentBuilder docBuilder;
	
	public RESTApplication()
	{
		this.con = new Connector(getClass().getClassLoader().getResource("/resources").getPath() + "kereta.xml");
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();
			
		} catch (Exception e) { }
	}
	
	/* ------------------ ROOT ----------------------------- */
	/* --------------------------------------------- */
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getApplications() throws Exception
	{
		List<String> appIDs = Application.getApplicationIDs(con);

		Document doc = docBuilder.newDocument();
		Element apps = doc.createElement("applications");
		doc.appendChild(apps);
		for (int i = 0; i < appIDs.size(); i++)
		{
			Document aDoc = (Document)this.getApplication(appIDs.get(i)).getEntity();
			Element app = aDoc.getDocumentElement();
			Node appClone = doc.importNode(app, true);
			apps.appendChild(appClone);
		}
		
		return StaticTools.getResponse(200, doc, true, "*");
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response createApplication() throws Exception
	{
		Document doc;
		int status;
		
		Application application = Application.create(con);
		if (application != null) 
		{
			status = 201;
			doc = application.toDOM();
		}
		else {
			status = 500;
			doc = StaticTools.getErrorDoc("resource creation failed");
		}
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id}/ - ------------------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getApplication(@PathParam("id") String id) throws Exception
	{
		Application app = Application.getApplication(con, id, 0);
		Document doc;
		int status;
		
		if (app != null)
		{
			status = 200;
			doc = app.toDOM();
		
			Element links = doc.createElement("links");
			
			Element me = doc.createElement("link");
			links.appendChild(me);
			me.setAttribute("rel", "this");
			me.setTextContent("/Application/" + app.getId());
			
			if (app.isApplication()) 
			{
				Element chl = doc.createElement("link");
				links.appendChild(chl);
				chl.setAttribute("rel", "tiers");
				chl.setTextContent("/Application/" + app.getId() + "/Tier");
			}
			
			Element tops = doc.createElement("link");
			links.appendChild(tops);
			tops.setAttribute("rel", "distributions");
			tops.setTextContent("/Application/" + app.getId() + "/Distribution");
			
			Element fctR = doc.createElement("link");
			links.appendChild(fctR);
			fctR.setAttribute("rel", "requirements");
			fctR.setTextContent("/Application/" + app.getId() + "/Requirement");
			
			doc.getDocumentElement().appendChild(links);
		}
		else 
		{
			doc = StaticTools.getErrorDoc("resource not found");
			status = 404;
		}
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public Response updateApplication(Document data, @PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		Application application = Application.getApplication(con, id, 0);
		
		if (application != null)
		{
			
			Element app = data.getDocumentElement();

			if (app.getTagName().equals("application"))
			{
				for (int i = 0; i < app.getChildNodes().getLength(); i++)
				{
					if (app.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)app.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							application.setAuthor(el.getTextContent());
							break;
						case "description":
							application.setDescription(el.getTextContent());
							break;
						case "name":
							application.setName(el.getTextContent());
							break;
						case "applicationType":
							application.setApplicationType(el.getTextContent());
							break;
						case "alias":
							application.setAlias(el.getTextContent());
							break;
					}
				}
			}

			if (application.persist(con))
			{
				doc = application.toDOM();
				status = 200;
			}
			else 
			{
				status = 500;
				doc = StaticTools.getErrorDoc("resource persistence failed");
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");		
	}
	
	@Path("/{id}")
	@DELETE
	public Response deleteApplication(@PathParam("id") String id) throws Exception
	{
		int status;
		Application application = Application.getApplication(con, id, 0);
		if (application == null) 
		{
			status = 404;
		}
		else {
			if (application.delete(con)) status = 204;
			else status = 500;
		}
		return StaticTools.getResponse(status, null, true, "*");
		//return Response.status(status).build();
	}
	
	/* ----------------- /{id}/rank ----------------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/compare")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response compareApplicationTopologies(@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		if (app != null)
		{
			status = 200;
			Client client = Client.create();
			MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters();
			
			doc = this.docBuilder.newDocument();
			Element rankXml = doc.createElement("listing");
			doc.appendChild(rankXml);
			
			Iterator<String> it = uriParms.keySet().iterator();
			while (it.hasNext()) 
			{
				String topID = it.next();
				String val = uriParms.getFirst(topID);
				
				StringTokenizer tokens = new StringTokenizer(val, ":");
				if (tokens.countTokens() == 2)
				{
					String fct = tokens.nextToken();
					String ident = tokens.nextToken();
					
					WebResource webResource = client.resource(con.getKeretaRoot() + "/UtilityFunction/" + fct + "/calc?key=" + ident);
					ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
					if (response.getStatus() != 200) {	
						return Response.status(500).entity(StaticTools.getErrorDoc("calculation failed")).build();
					}
					Document tDoc = response.getEntity(Document.class);
					Node tNode = tDoc.getDocumentElement();
					Node tCopy = doc.importNode(tNode, true);
					rankXml.appendChild(tCopy);
				}
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");		
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/select")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response selectApplicationTopologies(@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		if (app != null)
		{
			status = 200;
			Client client = Client.create();
			MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters();
			
			doc = this.docBuilder.newDocument();
			Element selXml = doc.createElement("selection");
			doc.appendChild(selXml);
			
			Document temp = null;
			double tempResult = 0.0;
			
			Iterator<String> it = uriParms.keySet().iterator();
			while (it.hasNext()) 
			{
				String topID = it.next();
				String val = uriParms.getFirst(topID);
				
				StringTokenizer tokens = new StringTokenizer(val, ":");
				if (tokens.countTokens() == 2)
				{
					String fct = tokens.nextToken();
					String ident = tokens.nextToken();
					
					WebResource webResource = client.resource(con.getKeretaRoot() + "/UtilityFunction/" + fct + "/calc?key=" + ident);
					ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
					if (response.getStatus() != 200) {	
						return Response.status(500).entity(StaticTools.getErrorDoc("calculation failed")).build();
					}
					Document tDoc = response.getEntity(Document.class);
					Node tNode = tDoc.getDocumentElement();
					NodeList chldNodes = tNode.getChildNodes();
					for (int i = 0; i < chldNodes.getLength(); i++)
					{
						Node chld = chldNodes.item(i);
						if (chld.getNodeName().equals("result"))
						{
							double chldResult = Double.parseDouble(chld.getTextContent());
							if (temp == null || chldResult > tempResult) 
							{
								tempResult = chldResult;
								temp = tDoc;
							}
						}
						
					}
					
				}
			}
			if (temp != null) 
			{
				Node tNode = temp.getDocumentElement();
				Node tCopy = doc.importNode(tNode, true);
				selXml.appendChild(tCopy);
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	/* --------------- /{id}/Tier  ----------------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Tier") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getApplicationChildren(@PathParam("id") String id) throws Exception
	{
		Application app = Application.getApplication(con, id, 0);
		Document doc;
		int status;
		if (app == null) {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		else
		{
			status = 200;
			List<Integer> cNbrs = Application.getChildApplicationNbrs(con, app.getId());
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("tiers");
			doc.appendChild(root);
			Iterator<Integer> it = cNbrs.iterator();
			while (it.hasNext())
			{
				int cNbr = it.next();

				Document cDoc = (Document)this.getApplicationChild(app.getId(), cNbr).getEntity();
				Node cCopy = doc.importNode(cDoc.getDocumentElement(), true);
				root.appendChild(cCopy);
			}
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Tier/{number}") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getApplicationChild(@PathParam("id") String id, @PathParam("number") int number) throws Exception
	{
		Application parent = Application.getApplication(con, id, 0);
		Application app = null;
		if (parent != null) app = Application.getApplication(con, parent.getId() ,number);
		Document doc;
		int status;
		
		if (app != null)
		{
			if (app.isApplication())
			{
				doc = StaticTools.getErrorDoc("resource not found");
				status = 404;
			}
			else
			{
				status = 200;
				doc = app.toDOM();
			
				Element links = doc.createElement("links");
				
				Element me = doc.createElement("link");
				links.appendChild(me);
				me.setAttribute("rel", "this");
				me.setTextContent("/Application/" + app.getId() + "/Tier/" + app.getTierNbr());
				
				Element chl = doc.createElement("link");
				links.appendChild(chl);
				chl.setAttribute("rel", "application");
				chl.setTextContent("/Application/" + app.getId());
				
				Element fctR = doc.createElement("link");
				links.appendChild(fctR);
				fctR.setAttribute("rel", "requirements");
				fctR.setTextContent("/Application/" + app.getId() +"/Tier/" + app.getTierNbr() + "/Requirement");
				
				doc.getDocumentElement().appendChild(links);
			}
		}
		else 
		{
			doc = StaticTools.getErrorDoc("resource not found");
			status = 404;
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
		
	}
		
	@Path("/{id}/Tier/{number}") 
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createApplicationChild(@PathParam("id") String id, @PathParam("number") int number) throws Exception
	{
		Application app = Application.getApplication(con, id , 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId() ,number);
		Document doc;
		int status;

		if (app == null) {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		else if (tier != null) {
			status = 409;
			doc = StaticTools.getErrorDoc("resource already exists");	
		}
		else if (number <= 0)
		{
			status = 408;
			doc = StaticTools.getErrorDoc("number must be bigger than 0");
		}
		else
		{
			Application child = Application.getApplication(con, id, number);
			if (child != null)
			{
				status = 408;
				doc = StaticTools.getErrorDoc("resource already exists");
			}
			else
			{
				status = 201;
				Application result = Application.create(con, app.getId(), number);
				doc = (Document)this.getApplication(result.getId()).getEntity();
			}
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Tier/{number}") 
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public Response createApplicationChild(Document data, @PathParam("id") String id, @PathParam("number") int number) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id , 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId() ,number);
		
		if (tier != null)
		{
			Element t = data.getDocumentElement();

			if (t.getTagName().equals("tier"))
			{
				for (int i = 0; i < t.getChildNodes().getLength(); i++)
				{
					if (t.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)t.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							tier.setAuthor(el.getTextContent());
							break;
						case "description":
							tier.setDescription(el.getTextContent());
							break;
						case "name":
							tier.setName(el.getTextContent());
							break;
					}
				}
			}
			if (tier.persist(con))
			{
				doc = tier.toDOM();
				status = 200;
			}
			else 
			{
				status = 500;
				doc = StaticTools.getErrorDoc("persistence failed");
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}		
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	
	@Path("/{id}/Tier/{number}") 
	@DELETE
	public Response deleteApplicationChild(@PathParam("id") String id, @PathParam("number") int number) throws Exception
	{
		Application app = Application.getApplication(con, id , 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId() ,number);
		int status;
		
		if (tier != null)
		{
			if (tier.delete(con)) status = 204;
			else status = 500;
		}
		else status = 404;
		
		//return Response.status(status).build();
		return StaticTools.getResponse(status, null, true, "*");
	}

	
	/* --------------- ROOT/{id}/Requirement ----------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Requirement") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getApplicationRequirements(@PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		if (app != null)
		{
			List<String> reqNames = Requirement.getApplicationRequirementNames(con, app.getId(), 0, "");
			
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("requirements");
			doc.appendChild(root);
			
			for (int i = 0; i < reqNames.size(); i++)
			{
				Document docReq = getRequirement(app, reqNames.get(i));
				Node oldNode = docReq.getDocumentElement();
				Node newNode = doc.importNode(oldNode, true);
				root.appendChild(newNode);
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Tier/{number}/Requirement") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getTierRequirements(@PathParam("id") String id, @PathParam("number") int number) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId(), number);
		if (tier != null)
		{
			List<String> reqNames = Requirement.getApplicationRequirementNames(con, tier.getId(), tier.getTierNbr(), "");
			
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("requirements");
			doc.appendChild(root);
			
			for (int i = 0; i < reqNames.size(); i++)
			{
				Document docReq = getRequirement(tier, reqNames.get(i));
				Node oldNode = docReq.getDocumentElement();
				Node newNode = doc.importNode(oldNode, true);
				root.appendChild(newNode);
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	/* --------------- /{id}/Requirement/{name} ----------- */
	/* --------------------------------------------- */
	

	@Path("/{id}/Requirement/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getApplicationRequirement(@PathParam("id") String id, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		Requirement req = null;
		if (app != null) req = Requirement.getRequirement(con, app.getId(), 0, name);
		
		if (req != null)
		{
			status = 200;
			doc = req.toDOM();
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Tier/{number}/Requirement/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getTierRequirement(@PathParam("id") String id, @PathParam("number") int number, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId(), number);
		Requirement req = null;
		if (tier != null) req = Requirement.getRequirement(con, app.getId(), tier.getTierNbr(), name);
		
		if (req != null)
		{
			status = 200;
			doc = req.toDOM();
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Requirement/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createApplicationRequirement(@PathParam("id") String id, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		Requirement req = null;
		if (app != null) req = Requirement.getRequirement(con, app.getId(), 0, name);
		
		if (req == null)
		{
			req = Requirement.create(con, app.getId(), 0, name);
			if (req != null) {
				status = 201;
				doc = req.toDOM();
			}
			else {
				status = 500;
				doc = StaticTools.getErrorDoc("resource creation failed");
			}
		}
		else {
			status = 409;
			doc = StaticTools.getErrorDoc("resource already exists");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Tier/{number}/Requirement/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createTierRequirement(@PathParam("id") String id, @PathParam("number") int number, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId(), number);
		Requirement req = null;
		if (tier != null) req = Requirement.getRequirement(con, tier.getId(), tier.getTierNbr(), name);
		
		if (req == null)
		{
			req = Requirement.create(con, tier.getId(), tier.getTierNbr(), name);
			if (req != null) {
				status = 201;
				doc = req.toDOM();
			}
			else {
				status = 500;
				doc = StaticTools.getErrorDoc("resource creation failed");
			}
		}
		else {
			status = 409;
			doc = StaticTools.getErrorDoc("resource already exists");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	
	@Path("/{id}/Requirement/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public Response updateApplicationRequirement(Document data, @PathParam("id") String id, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		Requirement requirement = null;
		if (app != null) requirement = Requirement.getRequirement(con, app.getId(), 0, name);
		
		if (requirement != null)
		{
			Element r = data.getDocumentElement();

			if (r.getTagName().equals("requirement"))
			{
				for (int i = 0; i < r.getChildNodes().getLength(); i++)
				{
					if (r.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)r.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							requirement.setAuthor(el.getTextContent());
							break;
						case "value":
							requirement.setValue(el.getTextContent());
							break;
						case "dataType":
							requirement.setDataType(el.getTextContent());
							break;
						case "requirementType":
							requirement.setRequirementType(el.getTextContent());
							break;
						case "demand":
							requirement.setDemand(el.getTextContent());
							break;
						}
				}
			}
			if (requirement.persist(con))
			{
				doc = requirement.toDOM();
				status = 200;
			}
			else 
			{
				status = 500;
				doc = StaticTools.getErrorDoc("persistence failed");
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Tier/{number}/Requirement/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public Response updateTierRequirement(Document data, @PathParam("id") String id, @PathParam("number") int number, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId(), number);
		Requirement requirement = null;
		if (tier != null) requirement = Requirement.getRequirement(con, app.getId(), tier.getTierNbr(), name);
		
		if (requirement != null)
		{
			Element r = data.getDocumentElement();

			if (r.getTagName().equals("requirement"))
			{
				for (int i = 0; i < r.getChildNodes().getLength(); i++)
				{
					if (r.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)r.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							requirement.setAuthor(el.getTextContent());
							break;
						case "value":
							requirement.setValue(el.getTextContent());
							break;
						case "dataType":
							requirement.setDataType(el.getTextContent());
							break;
						case "requirementType":
							requirement.setRequirementType(el.getTextContent());
							break;
						case "demand":
							requirement.setDemand(el.getTextContent());
							break;
						}
				}
			}
			if (requirement.persist(con))
			{
				doc = requirement.toDOM();
				status = 200;
			}
			else 
			{
				status = 500;
				doc = StaticTools.getErrorDoc("persistence failed");
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}	
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Requirement/{name}") 
	@DELETE
	public Response deleteApplicationRequirement(@PathParam("id") String id, @PathParam("name") String name) throws Exception
	{
		int status;
		Application app = Application.getApplication(con, id, 0);
		Requirement req = null;
		if (app != null) req = Requirement.getRequirement(con, app.getId(), 0, name);
		
		if (req != null)
		{
			if (req.delete(con)) status = 204;
			else status = 500;
		}
		else status = 404;
		
		return StaticTools.getResponse(status, null, true, "*");
		//return Response.status(status).build();
	}
	
	@Path("/{id}/Tier/{number}/Requirement/{name}") 
	@DELETE
	public Response deleteTierRequirement(@PathParam("id") String id, @PathParam("number") int number, @PathParam("name") String name) throws Exception
	{
		int status;
		Application app = Application.getApplication(con, id, 0);
		Application tier = null;
		if (app != null) tier = Application.getApplication(con, app.getId(), number);
		Requirement requirement = null;
		if (tier != null) requirement = Requirement.getRequirement(con, app.getId(), tier.getTierNbr(), name);
		
		if (requirement != null)
		{
			if (requirement.delete(con)) status = 204;
			else status = 500;
		}
		else status = 404;

		return StaticTools.getResponse(status, null, true, "*");
		//return Response.status(status).build();
	}
	
	/* --------------- HELPER  ----------------------------- */
	/* --------------------------------------------- */
	
	
	private Document getRequirement(Application application, String name) throws Exception
	{
		Requirement req = Requirement.getRequirement(con, application.getId(), application.getTierNbr(), name);
		if (req == null) return StaticTools.getErrorDoc("ressource not found");
		
		Document doc = req.toDOM();
		Element lks = doc.createElement("links");
		Element me = doc.createElement("link");
		me.setAttribute("rel", "this");
		Element origin = doc.createElement("link");
		origin.setAttribute("rel", "origin");
		
		if (application.isApplication()) 
		{
			me.setTextContent("/Application/" + application.getId() + "/Requirement/" + name);
			origin.setTextContent("/Application/" + application.getId());
		}
		else 
		{
			me.setTextContent("/Application/" + application.getId() + "/Tier/" + application.getTierNbr() + "/Requirement/" + name);
			origin.setTextContent("/Application/" + application.getId() + "/Tier/" + application.getTierNbr());
		}
		lks.appendChild(me);
		lks.appendChild(origin);
		
		doc.getDocumentElement().appendChild(lks);
		return doc;
	}
	
	public Response updateRequirement(Document data, String applicationId,  int applicationNumber, String name, String type) throws Exception
	{
		String typ = "";
		if (type.equals("Functional")) typ = "functional";
		else if (type.equals("NonFunctional")) typ = "non-functional";
		
		Application root = Application.getApplication(con, applicationId, 0);
		Application app = null;
		if (root != null) app = Application.getApplication(con, root.getId(), applicationNumber);
		Document doc;
		int status;
		
		if (app != null)
		{
			Requirement requirement = Requirement.getRequirement(con, app.getId(),app.getTierNbr(), name);
			if (requirement != null)
			{
				if (!requirement.getRequirementType().equals(typ))
				{
					status = 404;
					doc = StaticTools.getErrorDoc("resource not found");
				}
				else
				{
					Element req = data.getDocumentElement();
	
					if (req.getTagName().equals("requirement"))
					{
						for (int i = 0; i < req.getChildNodes().getLength(); i++)
						{
							Element el = (Element)req.getChildNodes().item(i);
							switch (el.getTagName())
							{
								case "author":
									requirement.setAuthor(el.getTextContent());
									break;
								case "value":
									requirement.setValue(el.getTextContent());
									break;
								case "dataType":
									requirement.setDataType(el.getTextContent());
									break;
							}
						}
					}
					if (requirement.persist(con))
					{
						doc = requirement.toDOM();
						status = 200;
					}
					else 
					{
						status = 500;
						doc = StaticTools.getErrorDoc("resource persistence failed");
					}
				}
			}
			else 
			{
				status = 404;
				doc = StaticTools.getErrorDoc("resource not found");
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	public Response deleteRequirement(String applicationId, int applicationNumber, String name, String type) throws Exception
	{
		String typ = "";
		if (type.equals("Functional")) typ = "functional";
		else if (type.equals("NonFunctional")) typ = "non-functional";
		
		Application root = Application.getApplication(con, applicationId, 0);
		Application app = null;
		if (root != null) app = Application.getApplication(con, root.getId(), applicationNumber);
		int status;
		if (app != null)
		{
			Requirement req = Requirement.getRequirement(con, app.getId(), app.getTierNbr(), name);
			if (req != null)
			{
				if (!req.getRequirementType().equals(typ)) status = 404;
				else if (req.delete(con)) 
				{
					status = 204;
				}
				else status = 500;
			}
			else status = 404;
		}
		else status = 404;
		
		return StaticTools.getResponse(status, null, true, "*");
	//	return Response.status(status).build();
	}	
	
	
	/* ----------------- /{id}/Distribution -------------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Distribution")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getApplicationDistributions(@PathParam("id") String id) throws Exception
	{
		RESTDistribution todo = new RESTDistribution();
		Application app = Application.getApplication(con, id, 0);
		Document doc;
		int status;
		
		if (app != null)
		{
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("distributions");
			doc.appendChild(root);
			
			List<String> topIDs = Distribution.getDistributionIDsByApplication(con, app.getId());
			for (int i = 0; i < topIDs.size(); i++)
			{
				Document tDoc = (Document)todo.getDistribution(topIDs.get(i)).getEntity();
				if (tDoc != null) 
				{
					Node tCopy = doc.importNode(tDoc.getDocumentElement(), true);
					root.appendChild(tCopy);
				}
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
	@Path("/{id}/Distribution")
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createApplicationDistribution(@PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		Application app = Application.getApplication(con, id, 0);
		
		if (app != null)
		{
			if (!app.isTier())
			{
				Distribution top = Distribution.createDistribution(con);
				
				if (top != null) 
				{
					top.setApplicationId(app.getId());
					top.persist(con);
					status = 201;
					doc = top.toDOM();
				}
				else 
				{
					status = 500;
					doc = StaticTools.getErrorDoc("resource creation failed");
				}
			}
			else 
			{
				status = 406;
				doc = StaticTools.getErrorDoc("child-applications can't have a distribution resource");
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
		//return Response.status(status).entity(doc).build();
	}
	
}
