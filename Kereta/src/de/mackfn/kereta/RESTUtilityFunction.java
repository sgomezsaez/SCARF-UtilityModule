package de.mackfn.kereta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import com.google.gson.JsonObject;

import de.mackfn.kereta.objects.Distribution;
import de.mackfn.kereta.objects.Function;
import de.mackfn.kereta.objects.SubFunction;
import de.mackfn.kereta.objects.UtilityFunction;
import de.mackfn.kereta.tools.Calculation;
import de.mackfn.kereta.tools.Connector;
import de.mackfn.kereta.tools.NefologWrapper;
import de.mackfn.kereta.tools.Parser;
import de.mackfn.kereta.tools.SoftPersistence;
import de.mackfn.kereta.tools.StaticTools;

@Path("/UtilityFunction")
public class RESTUtilityFunction {

	private Connector con;
	private DocumentBuilder docBuilder;
	
	public RESTUtilityFunction()
	{
		this.con = new Connector(getClass().getClassLoader().getResource("/").getPath() + "kereta.xml");
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();
			
		} catch (Exception e) { }
	}
	
	/* ------------------ ROOT ----------------------------- */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getUtilityFunctions() throws Exception
	{
		List<String> ufIDs = UtilityFunction.getUtilityFunctionIDs(con);

		Document doc = docBuilder.newDocument();
		Element ufs = doc.createElement("utilityFunctions");
		doc.appendChild(ufs);
		for (int i = 0; i < ufIDs.size(); i++)
		{
			Document uDoc = (Document)this.getUtilityFunction(ufIDs.get(i)).getEntity();
			Element uf = uDoc.getDocumentElement();
			Node ufClone = doc.importNode(uf, true);
			ufs.appendChild(ufClone);
		}
		
		return StaticTools.getResponse(200, doc, true, "*");
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response createUtilityFunction() throws Exception
	{
		Document doc;
		int status;
		UtilityFunction uf = UtilityFunction.createUtilityFunction(con);
		status = 201;
		doc = uf.toDOM();
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id} ----------------------------- */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/{id}")
	@GET
	public Response getUtilityFunction(@PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, id);
		
		if (uf != null)
		{
			status = 200;
			doc = uf.toDOM();
			Element links = doc.createElement("links");
			Element sub = doc.createElement("link");
			links.appendChild(sub);
			sub.setTextContent("/UtilityFunction/" + uf.getId() + "/SubFunction");
			sub.setAttribute("rel", "SubFunction");
			
			Element me = doc.createElement("link");
			links.appendChild(me);
			me.setAttribute("rel", "this");
			me.setTextContent("/UtilityFunction/" + uf.getId());

			doc.getDocumentElement().appendChild(links);
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@Path("/{id}")
	@PUT
	public Response updateUtilityFunction(Document data, @PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		UtilityFunction utilityFunction = UtilityFunction.getUtilityFunction(con, id);
		
		if (utilityFunction != null)
		{
			Element uf = data.getDocumentElement();

			if (uf.getTagName().equals("utilityFunction"))
			{
				for (int i = 0; i < uf.getChildNodes().getLength(); i++)
				{
					if (uf.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)uf.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							utilityFunction.setAuthor(el.getTextContent());
							break;
						case "description":
							utilityFunction.setDescription(el.getTextContent());
							break;
						case "distributionId":
							utilityFunction.setDistributionId(el.getTextContent());
							break;
						case "alias":
							utilityFunction.setAlias(el.getTextContent());
							break;
					}
				}
			}
			if (utilityFunction.persist(con))
			{
				doc = utilityFunction.toDOM();
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
	public Response deleteUtilityFunction(@PathParam("id") String id) throws Exception
	{
		int status;
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, id);
		
		if (uf != null)
		{
			if (uf.delete(con)) status = 204;
			else status = 500;
		}
		else status = 404;
		
		return StaticTools.getResponse(status, null, true, "*");
	}
	
	
	
	/* ----------------- /{id}/calc------------------------- */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/{id}/calc")
	@GET
	public Response calcUtilityFunction(@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception
	{
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, id);
		Document doc;
		int status;
		
		if (uf != null)
		{
			status = 200;
			doc = this.docBuilder.newDocument();
			Element ufXml = doc.createElement("calculation");
			ufXml.setAttribute("class", "utilityFunction");
			doc.appendChild(ufXml);
			Element ufRes = doc.createElement("result");
			ufXml.appendChild(ufRes);
			Element top = doc.createElement("distributionId");
			top.setTextContent(uf.getDistributionId());
			ufXml.appendChild(top);
			Element subXml = doc.createElement("subCalculations");
			ufXml.appendChild(subXml);
			
			
			double calc = 0;
			List<Integer> nbrs = SubFunction.getUtilityFunctionSubFunctionNumbers(con, uf.getId());
			double satisfaction = -1;
			double revenue = 0;
			double cost = 0;
			for (int i = 0; i < nbrs.size(); i++)
			{
				Document res = (Document)this.calcFunction(id, nbrs.get(i), uriInfo).getEntity();
				Element cXml = (Element)res.getElementsByTagName("calculation").item(0);
				String subType = cXml.getAttribute("type");
				System.out.println("type is " + subType);
				Element rXml = (Element)res.getElementsByTagName("result").item(0);
				double subRes = Double.parseDouble(rXml.getTextContent());
				if (subType.toLowerCase().contains("revenue") || subType.toLowerCase().contains("rev")) revenue = subRes;
				else if (subType.equals("cost")) cost = subRes;
				else if (subType.toLowerCase().contains("satisfaction") || subType.toLowerCase().contains("satisf")) satisfaction = subRes;
				Node subNode = doc.importNode(res.getDocumentElement(), true);
				subXml.appendChild(subNode);
			}
			
			System.out.println("satisfaction is " + satisfaction);
			System.out.println("revenue is " + revenue);
			System.out.println("cost is " + cost);

			if (satisfaction < 0) calc = revenue - cost;
			else calc = (revenue * satisfaction) - cost;
			ufRes.setTextContent(StaticTools.double2String(calc, 2));
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id}/calc------------------------- */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/{id}/clone")
	@GET
	public Response cloneUtilityFunction(@PathParam("id") String id, @Context UriInfo uriInfo) throws Exception
	{
		Document doc;
		int status;
		MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters();
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, id);
		String targetId = "";
		if (uriParms.containsKey("distribution")) targetId = uriParms.getFirst("distribution");
		Distribution target = Distribution.getDistribution(con, targetId);

		if (uf != null && target != null)
		{
			UtilityFunction ufNew = UtilityFunction.createUtilityFunction(con);
			ufNew.setAuthor(uf.getAuthor());
			ufNew.setDescription(uf.getDescription());
			ufNew.setDistributionId(target.getId());
			ufNew.persist(con);
			
			List<Integer> sfNbrs = SubFunction.getUtilityFunctionSubFunctionNumbers(con, uf.getId());
			for (int i = 0; i < sfNbrs.size(); i++)
			{
				SubFunction sf = SubFunction.getSubFunction(con, uf.getId(), sfNbrs.get(i));
				SubFunction sfNew = SubFunction.createSubFunction(con, ufNew.getId(), sfNbrs.get(i));
				sfNew.setAuthor(sf.getAuthor());
				sfNew.setFunctionId(sf.getFunctionId());
				sfNew.persist(con);
			}
			status = 201;
			doc = ufNew.toDOM();
		}

		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id}/SubFunction ----------------- */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/{id}/SubFunction")
	@GET
	public Response getUtilityFunctionSubs(@PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, id);
		
		if (uf != null)
		{
			doc = docBuilder.newDocument();
			status = 200;
			List<Integer> nbrs = SubFunction.getUtilityFunctionSubFunctionNumbers(con, uf.getId());
		
			Element sfs = doc.createElement("subfunctions");
			doc.appendChild(sfs);
			for (int i = 0; i < nbrs.size(); i++)
			{
				Document sDoc = (Document)this.getUtilityFunctionSub(uf.getId(), nbrs.get(i)).getEntity();
				Element sf = sDoc.getDocumentElement();
				Node sfClone = doc.importNode(sf, true);
				sfs.appendChild(sfClone);
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id}/SubFunction/{number} --------- */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/{id}/SubFunction/{number}")
	@GET
	public Response getUtilityFunctionSub(@PathParam("id") String id, @PathParam("number") int number) throws Exception
	{
		Document doc;
		int status;
		
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, id);
		if (uf != null)
		{
			SubFunction sf = SubFunction.getSubFunction(con, uf.getId(), number);

			if (sf != null)
			{
				doc = sf.toDOM();
				status = 200;

				Element links = doc.createElement("links");
				Element me = doc.createElement("link");
				links.appendChild(me);
				me.setAttribute("rel", "this");
				me.setTextContent("/UtilityFunction/" + uf.getId() + "/SubFunction/" + number);

				if (sf.getFunctionId() != null && !sf.getFunctionId().contains("nefolog$"))
				{
					Element fct = doc.createElement("link");
					links.appendChild(fct);
					fct.setAttribute("rel", "function");
					fct.setTextContent("/Function/" + sf.getFunctionId());
				}
				
				Element par = doc.createElement("link");
				links.appendChild(par);
				par.setAttribute("rel", "parameters");
				if (sf.getFunctionId() != null && sf.getFunctionId().contains("nefolog$")) 
				{
					par.setTextContent("/UtilityFunction/" + uf.getId() + "/SubFunction/" + number + "/NefologParameter");
				}
				else 
				{
					par.setTextContent("/Function/" + sf.getFunctionId() + "/Parameter");
				}
				
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
	
	@Path("/{id}/SubFunction/{number}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@PUT
	public Response updateSubFunction(Document data, @PathParam("id") String utilityFunctionId, @PathParam("number") int sfNumber) throws Exception
	{	
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, utilityFunctionId);
		Document doc = null;
		int status;
		if (uf != null)
		{
			SubFunction sf = SubFunction.getSubFunction(con, uf.getId(), sfNumber);
			if (sf != null)
			{
				Element sfEl = data.getDocumentElement();
	
				for (int i = 0; i < sfEl.getChildNodes().getLength(); i++)
				{
					if (sfEl.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)sfEl.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							sf.setAuthor(el.getTextContent());
							break;
						case "functionId":
							String item = el.getTextContent();
							if (!item.contains("nefolog$"))
							{
								Function fct = Function.getFunction(con, item);
								if (fct != null) item = fct.getId();
							}
							sf.setFunctionId(item);
							break;
					}
				}
				
				if (sf.persist(con)) {
					status = 200;
					doc = sf.toDOM();
				}
				else {
					status = 500;
					doc = StaticTools.getErrorDoc("persistence failed");
				}
				
			}
			else {
				status = 404; // NOT FOUND
				doc = StaticTools.getErrorDoc("resource not found");
			}
		}
		else {
			status = 404; // NOT FOUND
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/SubFunction/{number}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createSubFunction( @PathParam("id") String utilityFunctionId, @PathParam("number") int sfNumber) throws Exception
	{	
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, utilityFunctionId);
		Document doc = null;
		int status;
		if (uf != null)
		{
			SubFunction sf = SubFunction.getSubFunction(con, uf.getId(), sfNumber);
			if (sf == null)
			{
				sf = SubFunction.createSubFunction(con, uf.getId(), sfNumber);
				if (sf != null) {
					status = 200;
					doc = sf.toDOM();
				}
				else {
					status = 500;
					doc = StaticTools.getErrorDoc("resource creation failed");
				}
			}
			else {
				status = 409; // NOT FOUND
				doc = StaticTools.getErrorDoc("resource already exists");
			}
		}
		else {
			status = 404; // NOT FOUND
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/SubFunction/{number}")
	@DELETE
	public Response deleteSubFunction(@PathParam("id") String utilityFunctionId, @PathParam("number") int sfNumber) throws Exception
	{	
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, utilityFunctionId);
		int status;
		if (uf != null)
		{
			SubFunction sf = SubFunction.getSubFunction(con, uf.getId(), sfNumber);
			if (sf != null)
			{
				if (sf.delete(con)) status = 204;
				else status = 500;
			}
			else status = 404;
		}
		else  status = 404; // NOT FOUND

		return StaticTools.getResponse(status, null, true, "*");
	}

	/* ----------------- /{id}/SubFunction/{number}/calc ----- */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/{id}/SubFunction/{number}/calc")
	@GET
	public Response calcFunction(@PathParam("id") String utilityFunctionID, @PathParam("number") int number, @Context UriInfo uriInfo) throws Exception
	{
		MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters();
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, utilityFunctionID);
		Document doc = null;
		int status;
		
		if (uf != null)
		{
			status = 200;
			SubFunction sFct = SubFunction.getSubFunction(con, uf.getId(), number);
			
			String ident = "0";
			if (uriParms.containsKey("key")) ident = uriParms.getFirst("key");
			JsonObject vSet = SoftPersistence.getParameterJson(uf.getId(), number, ident, getClass().getClassLoader().getResource("/").getPath() + "parms/");
			double result = Double.NaN;
			
			String type = "";
			String fctStr = "";
			List<String> rpn = new ArrayList<String>();
			
			if (sFct.getFunctionId().contains("$"))
			{
				String configID = "";
				StringTokenizer toks = new StringTokenizer(sFct.getFunctionId(), "$");
				if (toks.countTokens() == 2) {
					toks.nextToken();
					configID = toks.nextToken();	
					fctStr = "configuration: " + configID;
				}
				result = NefologWrapper.getLowestCost(configID, vSet, con.getNefologRoot());
				type = "cost";
			}
			else
			{
				Calculation calc = null;
				if (sFct != null)
				{
					Parser psr = new Parser();
					Function fct = Function.getFunction(con, sFct.getFunctionId());
					if (fct != null)
					{
						List<String> toks = psr.tokenizer(fct.getFormula());
						rpn = psr.getRPN(toks);
						calc = psr.getCalculation(rpn);
						result = calc.calculate(vSet);
						type = fct.getFunctionType();
						fctStr = fct.getFormula();
					}
				}
			}
			
			doc = this.docBuilder.newDocument();
			Element calcXml = doc.createElement("calculation");
			calcXml.setAttribute("class", "subfunction");
			calcXml.setAttribute("number", Integer.toString(number));
			calcXml.setAttribute("type", type);
			doc.appendChild(calcXml);
			
			
			Element resXml = doc.createElement("result");
			resXml.setTextContent(StaticTools.double2String(result, 2));
			calcXml.appendChild(resXml);
			
			Element fctXml = doc.createElement("formula");
			fctXml.setTextContent(fctStr);
			calcXml.appendChild(fctXml);
			
			Element rpnXml = doc.createElement("rpn");
			rpnXml.setTextContent(rpn.toString());
			calcXml.appendChild(rpnXml);
			
			Element parmsXml = doc.createElement("parameters");
			calcXml.appendChild(parmsXml);
			Map<String, String> vMap = SoftPersistence.getParameterSet(uf.getId(), number, ident, getClass().getClassLoader().getResource("/").getPath() + "parms/");
			Iterator<String> it = vMap.keySet().iterator();
			while (it.hasNext()) 
			{
				String k = it.next();
				Element pXml = doc.createElement("parameter");
				pXml.setAttribute("name", k);
				pXml.setTextContent(vMap.get(k));
				parmsXml.appendChild(pXml);
			}
		}
		else status = 404;
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ----------------- /{id}/SubFunction/{number}/assign --- */
	/* --------------------------------------------- */
	
	@Path("/{id}/SubFunction/{number}/assign")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response assignParameter(@PathParam("id") String id, @PathParam("number") int number, @Context UriInfo uriInfo) throws Exception
	{
		Document doc;
		MultivaluedMap<String, String> uriParms = uriInfo.getQueryParameters(); 
		Map<String, String> parms = new HashMap<String, String>();
		Iterator<String> it = uriParms.keySet().iterator();
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, id);
		String result = "";
		int status;
		String ident = "0";
		
		if (uf != null)
		{
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("key");
			doc.appendChild(root);
			
			while (it.hasNext())
			{
				String key = it.next();
				String value = uriParms.getFirst(key);
				if (key.equals("key")) {
					ident = value;
				}
				else parms.put(key, value);
			}
			Date expire = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(expire);
			cal.add(Calendar.DATE, 3);
			expire = cal.getTime();
			result = SoftPersistence.createParameterSet(uf.getId(), number, parms, ident, getClass().getClassLoader().getResource("/").getPath() + "parms/");
			root.setTextContent(result);
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ------ /{id}/SubFunction/{number}/NefologParameter ------ */
	/* --------------------------------------------- */
	
	@Produces(MediaType.APPLICATION_XML)
	@Path("/{id}/SubFunction/{number}/NefologParameter")
	@GET
	public Response getNefologParameter(@PathParam("id") String utilityFunctionID, @PathParam("number") int number) throws Exception
	{	
		UtilityFunction uf = UtilityFunction.getUtilityFunction(con, utilityFunctionID);
		SubFunction sFct = SubFunction.getSubFunction(con, uf.getId(), number);
		
		Document doc = this.docBuilder.newDocument();
		Element root = doc.createElement("parameters");
		Document nefDoc = null;
		doc.appendChild(root);
		
		if (sFct.getFunctionId().contains("$"))
		{
			String configID = "";
			StringTokenizer toks = new StringTokenizer(sFct.getFunctionId(), "$");
			if (toks.countTokens() == 2) {
				toks.nextToken();
				configID = toks.nextToken();	
			}
			nefDoc = NefologWrapper.getParametersDoc(configID, con.getNefologRoot());
		}
		int status = 404;
		if (nefDoc != null) 
		{
			status = 200;
			NodeList vars = nefDoc.getElementsByTagName("variable");
			for (int i = 0; i < vars.getLength(); i++)
			{
				Element el = doc.createElement("parameter");
				el.setTextContent(vars.item(i).getTextContent());
				root.appendChild(el);
			}
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
}














