package de.mackfn.kereta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.mackfn.kereta.objects.Offering;
import de.mackfn.kereta.objects.Performance;
import de.mackfn.kereta.objects.Requirement;
import de.mackfn.kereta.objects.Application;
import de.mackfn.kereta.objects.Distribution;
import de.mackfn.kereta.objects.UtilityFunction;
import de.mackfn.kereta.tools.Connector;
import de.mackfn.kereta.tools.StaticTools;

/* ------------------ ROOT ----------------------------- */
/* --------------------------------------------- */

@Path("/Distribution")
public class RESTDistribution {

	private Connector con;
	private DocumentBuilder docBuilder;
	
	
	public RESTDistribution()
	{
		this.con = new Connector(getClass().getClassLoader().getResource("/").getPath() + "kereta.xml");
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();
			
		} catch (Exception e) { }
	}
	
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributions() throws Exception
	{
		List<String> topIDs = Distribution.getDistributionIDs(con);

		Document doc = docBuilder.newDocument();
		Element tops = doc.createElement("distributions");
		doc.appendChild(tops);
		for (int i = 0; i < topIDs.size(); i++)
		{
			Document tDoc = (Document)this.getDistribution(topIDs.get(i)).getEntity();
			Element top = tDoc.getDocumentElement();
			Node topClone = doc.importNode(top, true);
			tops.appendChild(topClone);
		}
		
		return StaticTools.getResponse(200, doc, true, "*");
	}
	
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createDistribution() throws Exception
	{
		Document doc;
		int status;
		Distribution top = Distribution.createDistribution(con);
		
		if (top != null) 
		{
			status = 201;
			doc = top.toDOM();
		}
		else {
			status = 500;
			doc = StaticTools.getErrorDoc("resource creation failed");
		}

		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ------------------ ROOT/{id} ------------------------- */
	/* --------------------------------------------- */
	
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistribution(@PathParam("id") String id) throws Exception
	{
		Distribution top = Distribution.getDistribution(con, id);
		Document doc;
		int status;

		if (top != null)
		{
			doc = top.toDOM();
		
			status = 200;
			Element links = doc.createElement("links");
			
			Element uf = doc.createElement("link");
			links.appendChild(uf);
			uf.setAttribute("rel", "utilityFunctions");
			uf.setTextContent("/Distribution/" + top.getId() + "/UtilityFunction");
			
			Element ap = doc.createElement("link");
			links.appendChild(ap);
			ap.setAttribute("rel", "application");
			ap.setTextContent("/Application/" + top.getApplicationId());
			
			Element of = doc.createElement("link");
			links.appendChild(of);
			of.setAttribute("rel", "offerings");
			of.setTextContent("/Distribution/" + top.getId() + "/Offering");
			
			Element me = doc.createElement("link");
			links.appendChild(me);
			me.setAttribute("rel", "this");
			me.setTextContent("/Distribution/" + top.getId());

			doc.getDocumentElement().appendChild(links);
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
			
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public Response updateDistribution(Document data, @PathParam("id") String id) throws Exception
	{
		Distribution distribution = Distribution.getDistribution(con, id);
		Document doc;
		int status;
		
		if (distribution != null)
		{
			Element top = data.getDocumentElement();

			if (top.getTagName().equals("distribution"))
			{
				for (int i = 0; i < top.getChildNodes().getLength(); i++)
				{
					if (top.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)top.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							distribution.setAuthor(el.getTextContent());
							break;
						case "applicationId":
							distribution.setApplicationId(el.getTextContent());
							break;
						case "representation":
							distribution.setRepresentation(el.getTextContent());
							break;
						case "language":
							distribution.setLanguage(el.getTextContent());
							break;
						case "langVersion":
							distribution.setLangVersion(el.getTextContent());
							break;
						case "alias":
							distribution.setAlias(el.getTextContent());
							break;
						case "namespace":
							distribution.setNamespace(el.getTextContent());
							break;
					}
				}
			}
			if (distribution.persist(con))
			{
				doc = distribution.toDOM();
				status = 200;
			}
			else {
				status = 500;
				doc = StaticTools.getErrorDoc("persistence failed");
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
				
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}")
	@DELETE
	public Response deleteDistribution(@PathParam("id") String id) throws Exception
	{
		Distribution distribution = Distribution.getDistribution(con, id);
		int status;
		
		if (distribution != null)
		{
			if (distribution.delete(con)) status = 204;
			else status = 500;
		}
		else {
			status = 404;
		}
				
		return StaticTools.getResponse(status, null, true, "*");
	}
	
	/* ------------------ ROOT/{id}/check ---------------------*/
	/* --------------------------------------------*/
	@Path("/{id}/check") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response checkDistribution(@PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		Distribution dstr = Distribution.getDistribution(con, id);
		if (dstr != null)
		{
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("comparison");
			root.setAttribute("distributionId", dstr.getId());
			doc.appendChild(root);
			
			Element gbl = doc.createElement("global");
			root.appendChild(gbl);
			List<String> reqNames = Requirement.getApplicationRequirementNames(con, dstr.getApplicationId(), 0, "");
			Iterator<String> it = reqNames.iterator();
			while (it.hasNext())
			{
				Requirement req = Requirement.getRequirement(con, dstr.getApplicationId(), 0, it.next());
			
				if (req != null)
				{
					Element chk = doc.createElement("check");
					Element nam = doc.createElement("name");
					Element dt = doc.createElement("dataType");
					Element rqT = doc.createElement("requirementType");
					nam.setTextContent(req.getName());
					dt.setTextContent(req.getDataType());
					rqT.setTextContent(req.getRequirementType());
					chk.appendChild(nam);
					chk.appendChild(dt);
					
					Element xReq = doc.createElement("requirement");
					Element xVal = doc.createElement("value");
					Element xDem = doc.createElement("demand");
					xVal.setTextContent(req.getValue());
					xDem.setTextContent(req.getDemand());
					
					chk.appendChild(rqT);
					xReq.appendChild(xVal);
					xReq.appendChild(xDem);
					chk.appendChild(xReq);
					
					Element xPrf = doc.createElement("performance");
					
					gbl.appendChild(chk);
					gbl.appendChild(xPrf);
				}
			}
			
			List<Integer> tierNbrs = Application.getChildApplicationNbrs(con, dstr.getApplicationId());
			Iterator<Integer> tIt = tierNbrs.iterator();
			Element tiers = doc.createElement("tiers");
			root.appendChild(tiers);
			
			while (tIt.hasNext())
			{
				int nbr = tIt.next();
				
				String sql = "SELECT offering_number FROM kereta_offeringTier WHERE application_id=? AND distribution_id=? AND application_tier=?";
				PreparedStatement prep;
				prep = con.getConnection().prepareStatement(sql);
				prep.setString(1, dstr.getApplicationId());
				prep.setString(2, dstr.getId());
				prep.setInt(3, nbr);
				
				List<String> perfs = new ArrayList<String>();
				int offeringNbr = 0;
				ResultSet set = prep.executeQuery();
				/* Should be only one offering per tier */
				if (set.next()) 
				{
					offeringNbr = set.getInt(1);
					perfs = Performance.getOfferingPerformanceNames(con, dstr.getId(), offeringNbr);
				}
				Element tier = doc.createElement("tier");
				tier.setAttribute("tierNbr", Integer.toString(nbr));
				if (offeringNbr > 0) tier.setAttribute("offeringNbr", Integer.toString(offeringNbr));
			
				List<String> tReqNames = Requirement.getApplicationRequirementNames(con, dstr.getApplicationId(), nbr, "");
				Iterator<String> rtIt = tReqNames.iterator();
				while (rtIt.hasNext())
				{
					Requirement req = Requirement.getRequirement(con, dstr.getApplicationId(), nbr, rtIt.next());
				
					if (req != null)
					{
						Element chk = doc.createElement("check");
						Element nam = doc.createElement("name");
						Element dt = doc.createElement("dataType");
						Element rqT = doc.createElement("requirementType");
						rqT.setTextContent(req.getRequirementType());
						nam.setTextContent(req.getName());
						dt.setTextContent(req.getDataType());
						chk.appendChild(nam);
						chk.appendChild(dt);
						chk.appendChild(rqT);
						
						Element xReq = doc.createElement("requirement");
						Element xVal = doc.createElement("value");
						Element xDem = doc.createElement("demand");
						xVal.setTextContent(req.getValue());
						xDem.setTextContent(req.getDemand());
						xReq.appendChild(xVal);
						xReq.appendChild(xDem);
						
						Element xPrf = doc.createElement("performance");
						if (perfs.contains(req.getName()))
						{
							Performance perf = Performance.getPerformance(con, dstr.getId(), offeringNbr, req.getName());
							{
								Element pVal = doc.createElement("value");
								Element pDem = doc.createElement("fulfilment");	
								pVal.setTextContent(perf.getValue());
								pDem.setTextContent(perf.getDemand());
								xPrf.appendChild(pVal);
								xPrf.appendChild(pDem);
							}
							perfs.remove(req.getName());
						}
						tier.appendChild(chk);
						chk.appendChild(xPrf);
						chk.appendChild(xReq);
					}	
				}
				
				Iterator<String> pIt = perfs.iterator();
				while (pIt.hasNext())
				{
					Performance perf = Performance.getPerformance(con, dstr.getId(), offeringNbr, pIt.next());
					if (perf != null)
					{
						Element chk = doc.createElement("check");
						Element nam = doc.createElement("name");
						Element dt = doc.createElement("dataType");
						Element rqT = doc.createElement("requirementType");
						nam.setTextContent(perf.getName());
						dt.setTextContent(perf.getDataType());
						rqT.setTextContent(perf.getRequirementType());
						chk.appendChild(nam);
						chk.appendChild(dt);
						chk.appendChild(rqT);
						
						Element xReq = doc.createElement("requirement");
						chk.appendChild(xReq);
						
						Element xPrf = doc.createElement("performance");
						
						Element pVal = doc.createElement("value");
						Element pDem = doc.createElement("fulfilment");	
						pVal.setTextContent(perf.getValue());
						pDem.setTextContent(perf.getDemand());
						xPrf.appendChild(pVal);
						xPrf.appendChild(pDem);
	
						tier.appendChild(chk);
						chk.appendChild(xPrf);	
					}
				}
				
				tiers.appendChild(tier);
			}
			
			
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	/* ------------------ ROOT/{id}/UtilityFunction ----------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/UtilityFunction") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributionUFs(@PathParam("id") String id) throws Exception
	{
		Client client = Client.create();
		
		Document doc;
		int status;
		Distribution dstr = Distribution.getDistribution(con, id);
		if (dstr != null)
		{
			status = 200;
			List<String> ufIDs = UtilityFunction.getDistributionUtilityFunctionIDs(con, dstr.getId());
			
			doc = docBuilder.newDocument();
			Element ufs = doc.createElement("utilityFunctions");
			doc.appendChild(ufs);
			for (int i = 0; i < ufIDs.size(); i++)
			{
				WebResource webResource = client.resource(con.getKeretaRoot() + "/UtilityFunction/" + ufIDs.get(i));
				ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
				if (response.getStatus() == 200) {	
					Document uDoc = (Document)response.getEntity(Document.class);
					Element uf = uDoc.getDocumentElement();
					Node ufClone = doc.importNode(uf, true);
					ufs.appendChild(ufClone);
				}
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/UtilityFunction") 
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createDistributionUF(@PathParam("id") String id) throws Exception
	{
		Document doc;
		int status;
		UtilityFunction uf = UtilityFunction.createUtilityFunction(con);
		Distribution dstr = Distribution.getDistribution(con, id);
		if (uf == null) 
		{
			status = 500;
			doc = StaticTools.getErrorDoc("resource creation failed");
		}
		else if (dstr != null)
		{
			uf.setDistributionId(dstr.getId());
			uf.persist(con);
			doc = uf.toDOM();
			status = 201;
			
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}

	
	/* ------------------ ROOT/{id}/Offering ----------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Offering") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributionOfferings(@PathParam("id") String id) throws Exception
	{
		Distribution top = Distribution.getDistribution(con, id);
		
		Document doc;
		int status;
		
		if (top != null)
		{
			status = 200;
			
			List<Integer> offNbrs = Offering.getDistributionOfferingNumbers(con, top.getId());
			doc = docBuilder.newDocument();
			Element ofs = doc.createElement("offerings");
			doc.appendChild(ofs);
			for (int i = 0; i < offNbrs.size(); i++)
			{
				Document oDoc = (Document)this.getDistributionOffering(top.getId(), offNbrs.get(i)).getEntity();
				Element of = oDoc.getDocumentElement();
				Node oClone = doc.importNode(of, true);
				ofs.appendChild(oClone);
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	
	@Path("/{id}/Offering/{offeringNbr}") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributionOffering(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr) throws Exception
	{
		Distribution top = Distribution.getDistribution(con, id);
		int status;
		Document doc;
		
		if (top != null)
		{
			Offering offering = Offering.getOffering(con, top.getId(), offeringNbr);

			if (offering != null) 
			{
				doc = offering.toDOM();
				status = 200;
				Element lks = doc.createElement("links");
				
				Element me = doc.createElement("link");
				me.setAttribute("rel", "this");
				me.setTextContent("/Distribution/" + offering.getDistributionId() + "/Offering/" + offering.getNumber());
				lks.appendChild(me);
				
				Element perf = doc.createElement("link");
				perf.setAttribute("rel", "performances");
				perf.setTextContent("/Distribution/" + offering.getDistributionId() + "/Offering/" + offering.getNumber() + "/Performance");
				lks.appendChild(perf);
				
				doc.getDocumentElement().appendChild(lks);
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
	
	@Path("/{id}/Offering/{offeringNbr}") 
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createDistributionOffering(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr) throws Exception
	{
		Distribution distribution = Distribution.getDistribution(con, id);
		
		Document doc;
		int status;
		
		if (distribution != null)
		{
			Offering offering = Offering.getOffering(con, distribution.getId(), offeringNbr);
			if (offering == null)
			{
				offering = Offering.create(con, distribution.getId(), offeringNbr);
				if (offering != null) {
					doc = offering.toDOM();
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
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Offering/{offeringNbr}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	@PUT
	public Response updateDistributionOffering(Document data, @PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr) throws Exception
	{
		Distribution distribution = Distribution.getDistribution(con, id);
		Document doc;
		int status;
		
		if (distribution != null)
		{
			Offering offering = Offering.getOffering(con, distribution.getId(), offeringNbr);
			
			Element off = data.getDocumentElement();

			if (off.getTagName().equals("offering"))
			{
				for (int i = 0; i < off.getChildNodes().getLength(); i++)
				{
					if (off.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)off.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							offering.setAuthor(el.getTextContent());
							break;
						case "nefologConfiguration":
							offering.setNefologConfiguration(el.getTextContent());
							break;
						case "nefologConfigurationId":
							offering.setNefologConfigurationId(el.getTextContent());
							break;
						case "nefologOfferingName":
							offering.setNefologOfferingName(el.getTextContent());
							break;
						case "nefologServiceType":
							offering.setNefologServiceType(el.getTextContent());
							break;
						case "nefologProvider":
							offering.setNefologProvider(el.getTextContent());
							break;
					}
				}
			}
			if (offering.persist(con))
			{
				doc = offering.toDOM();
				status = 200;
			}
			else {
				status = 500;
				doc = StaticTools.getErrorDoc("persistence failed");
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
				
		return StaticTools.getResponse(status, doc, true, "*");
	}

	@Path("/{id}/Offering/{offeringNbr}") 
	@DELETE
	public Response deleteDistributionOffering(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr) throws Exception
	{
		Distribution distribution = Distribution.getDistribution(con, id);
		int status;
		
		if (distribution != null)
		{
			Offering offering = Offering.getOffering(con, distribution.getId(), offeringNbr);
			if (offering != null)
			{
				if (offering.delete(con)) status = 204;
				else status = 500;
			}
			else status = 404;
		}
		else {
			status = 404;
		}
		
		return StaticTools.getResponse(status, null, true, "*");
	}
	
	/* ---- /{id}/Offering/{offeringNbr}/Tier/ ------- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Offering/{offeringNbr}/Tier") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributionOfferingTiers(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr) throws Exception
	{
		Document doc;
		int status;
		
		Distribution dstr = Distribution.getDistribution(con, id);
		Offering ofr = null; 
		if (dstr != null) ofr = Offering.getOffering(con, dstr.getId(), offeringNbr);
		
		if (dstr != null && ofr != null) 
		{
			String sql = "SELECT application_tier FROM kereta_offeringTier WHERE application_id=? AND distribution_id=? AND offering_number=?";
			PreparedStatement prep;

			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, dstr.getApplicationId());
			prep.setString(2, ofr.getDistributionId());
			prep.setInt(3, ofr.getNumber());
			
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("tiers");
			doc.appendChild(root);
			
			ResultSet set = prep.executeQuery();
			while (set.next()) 
			{
				Response res = this.getDistributionOfferingTier(id, offeringNbr, set.getInt(1));
				if (res.getStatus() == 200) 
				{
					Document dTr = (Document)res.getEntity();
					Node tr = doc.importNode(dTr.getDocumentElement(), true);
					root.appendChild(tr);
				}
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
		
	/* ---- /{id}/Offering/{offeringNbr}/Tier/{tier-nbr} -- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Offering/{offeringNbr}/Tier/{tierNbr}") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributionOfferingTier(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr, @PathParam("tierNbr") int tierNbr) throws Exception
	{
		Document doc;
		int status;
		
		Distribution dstr = Distribution.getDistribution(con, id);
		Offering ofr = null; 
		Application tier = null;
		if (dstr != null) {
			tier = Application.getApplication(con, dstr.getApplicationId(), tierNbr);
			ofr = Offering.getOffering(con, dstr.getId(), offeringNbr);
		}
		
		if (dstr != null && tier != null && ofr != null) 
		{
			if (!this.hasOfferingTier(ofr, tier))
			{
				status = 404;
				doc = StaticTools.getErrorDoc("There is no connection between the Offering resource and the requested Tier resource");
			}
			else
			{
				Client client = Client.create();
				WebResource webResource = client.resource(con.getKeretaRoot() + "/Application/" + dstr.getApplicationId() + "/Tier/" + tierNbr);
				ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
				return Response.status(response.getStatus()).entity(response.getEntity(Document.class)).build();
			}
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Offering/{offeringNbr}/Tier/{tierNbr}") 
	@Produces(MediaType.APPLICATION_XML)
	@DELETE
	public Response deleteDistributionOfferingTier(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr, @PathParam("tierNbr") int tierNbr) throws Exception
	{
		int status;
		
		Distribution dstr = Distribution.getDistribution(con, id);

		Offering ofr = null; 
		Application tier = null;
		if (dstr != null) {
			tier = Application.getApplication(con, dstr.getApplicationId(), tierNbr);
			ofr = Offering.getOffering(con, dstr.getId(), offeringNbr);
		}
		
		if (dstr != null && tier != null && ofr != null) 
		{
			if (!this.hasOfferingTier(ofr, tier)) status = 404;
			else
			{
				String sql = "DELETE FROM kereta_offeringTier WHERE application_id=? AND application_tier=? AND distribution_id=? AND offering_number=?";
				PreparedStatement prep;

				prep = con.getConnection().prepareStatement(sql);
				prep.setString(1, tier.getId());
				prep.setInt(2, tier.getTierNbr());
				prep.setString(3, ofr.getDistributionId());
				prep.setInt(4, ofr.getNumber());
				prep.execute();
				status = 204;
			}
		}
		else status = 404;

		return StaticTools.getResponse(status, null, true, "*");
	}
	
	private boolean hasOfferingTier(Offering offering, Application tier) throws SQLException, Exception
	{
		if (offering == null || tier == null) return false;
		String sql = "SELECT COUNT(*) FROM kereta_offeringTier WHERE application_id=? AND application_tier=? AND distribution_id=? AND offering_number=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, tier.getId());
		prep.setInt(2, tier.getTierNbr());
		prep.setString(3, offering.getDistributionId());
		prep.setInt(4, offering.getNumber());
		
		ResultSet set = prep.executeQuery();
		int count = 0;
		if (set.next()) count = set.getInt(1);
		return (count > 0);
	}
	
	@Path("/{id}/Offering/{offeringNbr}/Tier/{tierNbr}") 
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createDistributionOfferingTier(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr, @PathParam("tierNbr") int tierNbr) throws Exception
	{
		Document doc;
		int status;
		
		Distribution dstr = Distribution.getDistribution(con, id);
		if (dstr != null) {

			String sql = "INSERT INTO kereta_offeringTier (application_id, application_tier, distribution_id, offering_number) VALUES (?, ?, ?, ?)";
			PreparedStatement prep;

			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, dstr.getApplicationId());
			prep.setInt(2, tierNbr);
			prep.setString(3, dstr.getId());
			prep.setInt(4, offeringNbr);
			
			prep.execute();	

			status = 200;
			doc = StaticTools.getErrorDoc("ok");
		}
		else 
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	
	/* ---- /{id}/Offering/{offeringNbr}/Performance/{name} -- */
	/* --------------------------------------------- */
	
	@Path("/{id}/Offering/{offeringNbr}/Performance/") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributionOfferingPerformances(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr) throws Exception
	{
		Distribution dstr = Distribution.getDistribution(con, id);
		Offering offering = null;
		if (dstr != null) offering = Offering.getOffering(con, dstr.getId(), offeringNbr);
		Document doc; 
		int status;
		
		if (offering != null)
		{
			status = 200;
			doc = this.docBuilder.newDocument();
			Element root = doc.createElement("performances");
			doc.appendChild(root);

			List<String> pNames = Performance.getOfferingPerformanceNames(con, offering.getDistributionId(), offering.getNumber());	
			Iterator<String> it = pNames.iterator();
			while (it.hasNext()) 
			{
				Response res = this.getDistributionOfferingPerformance(dstr.getId(), offering.getNumber(), it.next());
				if (res.getStatus() == 200)
				{
					Document docPerf = (Document)res.getEntity();
					Node perf = doc.importNode(docPerf.getDocumentElement(), true);
					root.appendChild(perf);
				}
			}
		}
		else
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Offering/{offeringNbr}/Performance/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getDistributionOfferingPerformance(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr, @PathParam("name") String name) throws Exception
	{
		Distribution dstr = Distribution.getDistribution(con, id);
		Document doc;
		int status;
		
		if (dstr != null)
		{
			status = 200;
			Offering offering = Offering.getOffering(con, dstr.getId(), offeringNbr);
			if (offering != null)
			{
				doc = this.getPerformance(offering, name);
				if (doc != null) {
					status = 200;
					
					Element links = doc.createElement("links");
					
					Element me = doc.createElement("link");
					links.appendChild(me);
					me.setAttribute("rel", "this");
					me.setTextContent("/Distribution/" + dstr.getId() + "/Offering/" + offering.getNumber() + "/Performance/" + name);
	
					
					Element origin = doc.createElement("link");
					links.appendChild(origin);
					origin.setAttribute("rel", "origin");
					origin.setTextContent("/Distribution/" + dstr.getId() + "/Offering/" + offering.getNumber());
					
					doc.getDocumentElement().appendChild(links);
				}
				else 
				{
					doc = StaticTools.getErrorDoc("resource not found");
					status = 404;
				}
			}
			else
			{
				doc = StaticTools.getErrorDoc("resource not found");
				status = 404;
			}
		}
		else
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Offering/{offeringNbr}/Performance/{name}") 
	@Produces(MediaType.APPLICATION_XML)
	@POST
	public Response createDistributionOfferingPerformance(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Distribution distribution = Distribution.getDistribution(con, id);
		Offering offer = null;
		Performance performance = null;
		if (distribution != null) offer = Offering.getOffering(con, distribution.getId(), offeringNbr);
		if (offer != null) performance = Performance.getPerformance(con, offer.getDistributionId(), offer.getNumber(), name);
		
		if (offer == null)
		{
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
		else if (performance != null)
		{
			status = 409;
			doc = StaticTools.getErrorDoc("resource already exists");
		}
		else 
		{
			performance = Performance.create(con, offer.getDistributionId(), offer.getNumber(), name);
			doc = performance.toDOM();
			status = 201;
		}
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Offering/{offeringNbr}/Performance/{name}") 
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@PUT
	public Response updateDistributionOfferingPerformance(Document data, @PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr, @PathParam("name") String name) throws Exception
	{
		Document doc;
		int status;
		Distribution distribution = Distribution.getDistribution(con, id);
		Offering offer = null;
		Performance performance = null;
		if (distribution != null) offer = Offering.getOffering(con, distribution.getId(), offeringNbr);
		if (offer != null) performance = Performance.getPerformance(con, offer.getDistributionId(), offer.getNumber(), name);
		
		if (performance != null)
		{
			Element perf = data.getDocumentElement();

			if (perf.getTagName().equals("performance"))
			{
				for (int i = 0; i < perf.getChildNodes().getLength(); i++)
				{
					if (perf.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
					
					Element el = (Element)perf.getChildNodes().item(i);
					switch (el.getTagName())
					{
						case "author":
							performance.setAuthor(el.getTextContent());
							break;
						case "value":
							performance.setValue(el.getTextContent());
							break;
						case "dataType":
							performance.setDataType(el.getTextContent());
							break;
						case "requirementType":
							performance.setRequirementType(el.getTextContent());
							break;
						case "demand":
							performance.setDemand(el.getTextContent());
							break;
					}
				}
			}
			if (performance.persist(con))
			{
				doc = performance.toDOM();
				status = 200;
			}
			else {
				status = 500;
				doc = StaticTools.getErrorDoc("persistence failed");
			}
		}
		else {
			status = 404;
			doc = StaticTools.getErrorDoc("resource not found");
		}
				
		return StaticTools.getResponse(status, doc, true, "*");
	}
	
	@Path("/{id}/Offering/{offeringNbr}/Performance/{name}") 
	@DELETE
	public Response deleteDistributionOfferingPerformance(@PathParam("id") String id, @PathParam("offeringNbr") int offeringNbr, @PathParam("name") String name) throws Exception
	{
		int status;
		Distribution distribution = Distribution.getDistribution(con, id);
		Offering offer = null;
		Performance performance = null;
		if (distribution != null) offer = Offering.getOffering(con, distribution.getId(), offeringNbr);
		if (offer != null) performance = Performance.getPerformance(con, offer.getDistributionId(), offer.getNumber(), name);
		
		if (performance == null)
		{
			status = 404;
		}
		else 
		{
			if (performance.delete(con)) status = 204;
			else status = 500;
		}
		return StaticTools.getResponse(status, null, true, "*");
	}
	
	public Document getPerformance(Offering offering, String name) throws Exception
	{
		Document doc = null;
		if (offering != null)
		{
			Performance perf = Performance.getPerformance(con, offering.getDistributionId(), offering.getNumber(), name);;
			if (perf != null) doc = perf.toDOM();
		}
		return doc;
	}
	
}








