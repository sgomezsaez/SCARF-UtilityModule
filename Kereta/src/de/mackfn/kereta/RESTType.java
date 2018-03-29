package de.mackfn.kereta;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.mackfn.kereta.objects.Type;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.mackfn.kereta.tools.Connector;
import de.mackfn.kereta.tools.StaticTools;

@Path("/Type")
public class RESTType {

	private Connector con;
	private DocumentBuilder docBuilder;
	
	public RESTType()
	{
		this.con = new Connector(getClass().getClassLoader().getResource("/").getPath() + "kereta.xml");
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();
			
		} catch (Exception e) { }
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getFunctions()
	{
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("types");
		doc.appendChild(root);
		Element links = doc.createElement("links");
		root.appendChild(links);
		
		Element data = doc.createElement("link");
		data.setAttribute("rel", "dataTypes");
		data.setTextContent("/Type/DataType");
		links.appendChild(data);
		
		Element fct = doc.createElement("link");
		fct.setAttribute("rel", "functionTypes");
		fct.setTextContent("/Type/FunctionType");
		links.appendChild(fct);
		
		Element app = doc.createElement("link");
		app.setAttribute("rel", "applicationTypes");
		app.setTextContent("/Type/ApplicationType");
		links.appendChild(app);
		
		Element req = doc.createElement("link");
		req.setAttribute("rel", "requirementTypes");
		req.setTextContent("/Type/RequirementType");
		links.appendChild(req);
		
		return StaticTools.getResponse(200, doc, true, "*");
	}
	
	@Path("/DataType")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getDataTypes()
	{
		return getTypes("kereta_dataType");
	}
	@Path("/DataType/{name}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getDataType(@PathParam("name") String name) throws Exception
	{
		return getType("kereta_dataType", name);
	}
	
	@Path("/ApplicationType")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getAppTypes()
	{
		return getTypes("kereta_applicationType");
	}
	@Path("/ApplicationType/{name}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getApplicationType(@PathParam("name") String name) throws Exception
	{
		return getType("kereta_applicationType", name);
	}
	
	@Path("/RequirementType")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getReqTypes()
	{
		return getTypes("kereta_requirementType");
	}
	@Path("/RequirementType/{name}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getRequirementType(@PathParam("name") String name) throws Exception
	{
		return getType("kereta_requirementType", name);
	}
	
	@Path("/FunctionType")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getFctTypes()
	{
		return getTypes("kereta_functionType");
	}
	@Path("/FunctionType/{name}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getFunctionType(@PathParam("name") String name) throws Exception
	{
		return getType("kereta_functionType", name);
	}
	
	@Path("/DataType/{name}")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response createDataType(@PathParam("name") String name) throws Exception
	{
		return createType("kereta_dataType", name);
	}
	
	@Path("/ApplicationType/{name}")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response createAppType(@PathParam("name") String name) throws Exception
	{
		return createType("kereta_applicationType", name);
	}
	
	@Path("/FunctionType/{name}")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response createFctType(@PathParam("name") String name) throws Exception
	{
		return createType("kereta_functionType", name);
	}
	
	@Path("/DataType/{name}")
	@DELETE
	public Response deleteDataType(@PathParam("name") String name) throws Exception
	{
		return deleteType("kereta_dataType", name);
	}
	
	@Path("/FunctionType/{name}")
	@DELETE
	public Response deleteFctType(@PathParam("name") String name) throws Exception
	{
		return deleteType("kereta_functionType", name);
	}
	
	@Path("/ApplicationType/{name}")
	@DELETE
	public Response deleteAppType(@PathParam("name") String name) throws Exception
	{
		return deleteType("kereta_applicationType", name);
	}
	
	private Response getTypes(String table)
	{
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("types");
		doc.appendChild(root);
		List<String> types = Type.getTypeNames(con, table);
		Iterator<String> it = types.iterator();
		while (it.hasNext())
		{
			Element el = doc.createElement("type");
			el.setTextContent(it.next());
			root.appendChild(el);
		}
		return StaticTools.getResponse(200, doc, true, "*");
	}
	
	private Response getType(String table, String name) throws Exception
	{
		Document doc;
		int status;
		
		if (Type.hasType(con, table, name))
		{
			status = 200;
			doc = docBuilder.newDocument();
			Element root = doc.createElement("type");
			root.setTextContent(name);
			doc.appendChild(root);
		}
		else
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	private Response createType(String table, String name) throws Exception
	{
		int status;
		Document doc;
		Type type = Type.getType(con, table, name);
		if (type != null) 
		{
			status = 409;
			doc = StaticTools.getErrorDoc("resource already exists");
		}
		else 
		{
			Type t = Type.createType(con, name, table);
			if (t == null) 
			{
				status = 500;
				doc = StaticTools.getErrorDoc("resource creation failed");
			}
			else 
			{
				status = 201;
				doc = t.toDOM();
			}
			
		}
		return StaticTools.getResponse(status, doc, true, "*");
	}

	private Response deleteType(String table, String name) throws Exception
	{
		int status;
		Type type = Type.getType(con, name, table);
		if (type == null) 
		{
			status = 404;
		}
		else 
		{
			if (type.deleteType(con, table)) status = 204;
			else status = 500;
			
		}
		return StaticTools.getResponse(status, null, true, "*");
	}

}
