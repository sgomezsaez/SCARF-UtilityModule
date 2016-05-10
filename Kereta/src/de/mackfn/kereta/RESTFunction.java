package de.mackfn.kereta;

import java.util.Iterator;
import java.util.List;

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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.mackfn.kereta.objects.Function;
import de.mackfn.kereta.objects.Parameter;
import de.mackfn.kereta.tools.Calculation;
import de.mackfn.kereta.tools.Connector;
import de.mackfn.kereta.tools.Parser;
import de.mackfn.kereta.tools.StaticTools;

@Path("/Function")
public class RESTFunction {

	private Connector con;
	private DocumentBuilder docBuilder;
	
	public RESTFunction()
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
	public Response getFunctions() throws Exception
	{
		List<String> fctIDs = Function.getFunctionIDs(con, "");

		Document doc = docBuilder.newDocument();
		Element fcts = doc.createElement("functions");
		doc.appendChild(fcts);
		for (int i = 0; i < fctIDs.size(); i++)
		{
			Response resp = this.getFunction(fctIDs.get(i));
			Document fDoc = (Document)resp.getEntity();
			Element fct = fDoc.getDocumentElement();
			Node fctClone = doc.importNode(fct, true);
			fcts.appendChild(fctClone);
		}
		return StaticTools.getResponse(200, doc, true, "*");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response createFunction() throws Exception
	{
		Document doc;
		int status;
		Function function = Function.createFunction(con);
		
		if (function!= null) 
		{
			status = 201;
			doc = function.toDOM();
		}
		else 
		{
			status = 500;
			doc = StaticTools.getErrorDoc("resource not found");
		}

		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id} ----------------------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/calc")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response calcXML(@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception
	{
		Function function = Function.getFunction(con, id);
		Document doc = null;
		int status;
		
		MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters(); 
		Iterator<String> it = uriParms.keySet().iterator();
		
		String jsonStr = "";
		while (it.hasNext())
		{
			if (jsonStr.equals("")) jsonStr += "{ ";
			else jsonStr += ", ";
			String k = it.next();
			jsonStr += "\"" + k + "\": " + uriParms.getFirst(k); 
		}
		if (jsonStr.equals("")) jsonStr = "{ }";
		else jsonStr += " }";
		
		Gson gson = new Gson();
		JsonElement element = gson.fromJson (jsonStr, JsonElement.class);
		JsonObject jsonObj = element.getAsJsonObject();
		
		if (function != null) 
		{
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("calculation");
			doc.appendChild(root);
			Parser parser = new Parser();
			List<String> toks = parser.tokenizer(function.getFormula());
			List<String> rpn = parser.getRPN(toks);
			Calculation calc = parser.getCalculation(rpn);
			
			Element result = doc.createElement("result");
			root.appendChild(result);
			result.setTextContent(StaticTools.double2String(calc.calculate(jsonObj), 4));
			
			Element fct = doc.createElement("formula");
			fct.setTextContent(function.getFormula());
			root.appendChild(fct);
			
			Element toc = doc.createElement("toks");
			toc.setTextContent(toks.toString());
			root.appendChild(toc);
			
			Element rp = doc.createElement("rpn");
			rp.setTextContent(rpn.toString());
			root.appendChild(rp);
			
			Element parms = doc.createElement("parameters");
			parms.setTextContent(jsonStr);
			root.appendChild(parms);
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/calc")
	@Produces(MediaType.TEXT_PLAIN)
	@GET
	public Response calcPlain(@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception
	{
		Function function = Function.getFunction(con, id);
		String result = "NaN";
		int status;
		
		MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters(); 
		Iterator<String> it = uriParms.keySet().iterator();
		
		String jsonStr = "";
		while (it.hasNext())
		{
			if (jsonStr.equals("")) jsonStr += "{ ";
			else jsonStr += ", ";
			String k = it.next();
			jsonStr += "\"" + k + "\": " + uriParms.getFirst(k); 
		}
		if (jsonStr.equals("")) jsonStr = "{ }";
		else jsonStr += " }";
		
		Gson gson = new Gson();
		JsonElement element = gson.fromJson (jsonStr, JsonElement.class);
		JsonObject jsonObj = element.getAsJsonObject();
		
		if (function != null) 
		{
			status = 200;
			Parser parser = new Parser();
			List<String> toks = parser.tokenizer(function.getFormula());
			List<String> rpn = parser.getRPN(toks);
			Calculation calc = parser.getCalculation(rpn);
			
			result = Double.toString(calc.calculate(jsonObj));
		}
		else status = 404;
		
		return StaticTools.getResponse(status, result, true, "*");
	}
	
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getFunction(@PathParam("id") String id) throws Exception
	{
		Function fun = Function.getFunction(con, id);
		Document doc = null;
		
		if (fun != null)
			try {
				doc = fun.toDOM();
			} catch (Exception e) {
		}
		if (doc != null)
		{
			Element links = doc.createElement("links");
			
			Element pars = doc.createElement("link");
			links.appendChild(pars);
			pars.setAttribute("rel", "parameters");
			pars.setTextContent("/Function/" + fun.getId() + "/Parameter");

			
			Element me = doc.createElement("link");
			links.appendChild(me);
			me.setAttribute("rel", "this");
			me.setTextContent("/Function/" + fun.getId());
			
			doc.getDocumentElement().appendChild(links);
		}
		if (doc != null) return StaticTools.getResponse(200, doc, true, "*");
		else { 		
			doc = StaticTools.getErrorDoc("resource not found");
			return StaticTools.getResponse(404, doc, true, "*");
		
		}
	}
	
	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response updateFunction(Document data, @PathParam("id") String id) throws Exception
	{
		Function fct = Function.getFunction(con, id);
		if (fct != null) 
		{
			for (int i = 0; i < data.getDocumentElement().getChildNodes().getLength(); i++)
			{
				if (data.getDocumentElement().getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
				
				Element el = (Element)data.getDocumentElement().getChildNodes().item(i);
				switch (el.getTagName())
				{
					case "author":
						fct.setAuthor(el.getTextContent());
						break;
					case "description":
						fct.setDescription(el.getTextContent());
						break;
					case "functionType":
						fct.setFunctionType(el.getTextContent());
						break;
					case "formula":
						fct.setFormula(el.getTextContent());
						break;
					case "alias":
						fct.setAlias(el.getTextContent());
						break;
				}
			}
			fct.updateFunction(con);
		}
		return this.getFunction(id);
	}
	
	@Path("/{id}")
	@DELETE
	public Response deleteFunction(@PathParam("id") String id) throws Exception
	{
		int status;
		Function fct = Function.getFunction(con, id);
		if (fct == null) status = 404;
		else
		{
			if (fct.deleteFunction(con) && Parameter.deleteFunctionParameters(con, fct)) status = 204;	
			else status = 500;
		}
		return StaticTools.getResponse(status, null, true, "*");
	}
	
	public Calculation getCalculation(String id) throws Exception
	{
		Calculation result = null;
		Function function = Function.getFunction(con, id);
		if (function != null)
		{
			Parser parser = new Parser();
			List<String> toks = parser.tokenizer(function.getFormula());
			List<String> rpn = parser.getRPN(toks);
			result = parser.getCalculation(rpn);
		}
		return result;
	}
	
	/* ----------------- /{id}/Parameter/ ------------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Parameter/")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getParameters(@PathParam("id") String functionID) throws Exception
	{	
		Document doc;
		int status;
		Function fun = Function.getFunction(con, functionID);
		if (fun != null)
		{
			status = 200;
			doc = docBuilder.newDocument();
			Element parms = doc.createElement("parameters");
			doc.appendChild(parms);
			
			List<String> parmNames = Parameter.getParameterNames(con, fun.getId());
			for (int i = 0; i < parmNames.size(); i++)
			{
				Document pDoc = (Document)this.getParameter(functionID, parmNames.get(i)).getEntity();
				Element parm = pDoc.getDocumentElement();
				Node parmClone = doc.importNode(parm, true);
				parms.appendChild(parmClone);
			} 
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id}/Parameter/{name} ------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Parameter/{name}")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getParameter(@PathParam("id") String functionID, @PathParam("name") String name) throws Exception
	{
		Function fun = Function.getFunction(con, functionID);
		Document doc = null;
		int status; 
		if (fun != null)
		{
			Parameter par = Parameter.getParameter(con, fun.getId(), name);
			
			if (par != null)
				try {
					doc = par.toDOM();
				} catch (Exception e) {
			}
			
			if (doc != null)
			{
				status = 200;
				Element links = doc.createElement("links");
				
				Element me = doc.createElement("link");
				links.appendChild(me);
				me.setAttribute("rel", "this");
				me.setTextContent("/Function/" + fun.getId() + "/Parameter/" + name);
				
				Element fct = doc.createElement("link");
				links.appendChild(fct);
				fct.setAttribute("rel", "function");
				fct.setTextContent("/Function/" + fun.getId());
				
				doc.getDocumentElement().appendChild(links);
			}
			else {
				status = 404;
				doc = StaticTools.getErrorDoc("resource not found");
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Parameter/{name}")
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createParameter(@PathParam("id") String functionID, @PathParam("name") String name) throws Exception
	{	
		Document doc = null;
		Parameter parameter = null;
		int status;
		Function fun = Function.getFunction(con, functionID);
		if (fun != null)
		{
			parameter = Parameter.createParameter(con, name, fun.getId());
			if (parameter != null) 
			{
				doc = parameter.toDOM();
				status = 201;
			}
			else {
				status = 500;
				doc = StaticTools.getErrorDoc("resource creation failed");
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}

		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Parameter/{name}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@PUT
	public Response updateParameter(Document data, @PathParam("id") String functionID, @PathParam("name") String name) throws Exception
	{	
		Function fun = Function.getFunction(con, functionID);
		String fctId = functionID;
		if (fun != null)
		{
			fctId = fun.getId();
			Parameter parm = Parameter.getParameter(con, fctId, name);
			if (parm != null)
			{
				for (int i = 0; i < data.getDocumentElement().getChildNodes().getLength(); i++)
				{
					if (data.getDocumentElement().getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					Element el = (Element)data.getDocumentElement().getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							parm.setAuthor(el.getTextContent());
							break;
						case "dataType":
							parm.setDataType(el.getTextContent());
							break;
						case "defaultValue":
							parm.setDefaultValue(el.getTextContent());
							break;
						case "description":
							parm.setDescription(el.getTextContent());
							break;
					}
				}
				parm.updateParameter(con);
			}
			else {
				Document doc = StaticTools.getErrorDoc("resource not found");
				return StaticTools.getResponse(404, doc, true, "*");
			}
		}
		//return StaticTools.getResponse(200, doc, true, "*");
		return this.getParameter(fctId, name);
	}
	
	@Path("/{id}/Parameter/{name}")
	@DELETE
	public Response deleteParameter(@PathParam("id") String functionID, @PathParam("name") String name) throws Exception
	{
		int status;
		Function fct = Function.getFunction(con, functionID);
		if (fct == null) status = 404;
		else
		{
			Parameter p = Parameter.getParameter(con, fct.getId(), name);
			if (p == null) status = 404;
			else
			{
				if (p.delete(con)) status = 204;	
				else status = 500;
			}
		}
		return StaticTools.getResponse(status, null, true, "*");
	}
}
