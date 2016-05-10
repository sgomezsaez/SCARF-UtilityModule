package de.mackfn.kereta.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class NefologWrapper {

	
	public static List<String> getParameters(String configID, String nefologRoot)
	{
		List<String> parms = new ArrayList<String>();
		String uri = nefologRoot + "/costCalculator?configid=" + configID;
		
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
		
		if (response.getStatus() != 200) {	
			throw new RuntimeException("Failed: HTTP eCode: " + response.getStatus());
		}

		Document doc = response.getEntity(Document.class);
		NodeList vars = doc.getElementsByTagName("variable");
		for (int i = 0; i < vars.getLength(); i++) parms.add(vars.item(i).getTextContent());
		
		return parms;
	}
	
	public static Document getParametersDoc(String configID, String nefologRoot)
	{
		String uri = nefologRoot + "/costCalculator?configid=" + configID;
		
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
		
		if (response.getStatus() != 200) {	
			throw new RuntimeException("Failed: HTTP eCode: " + response.getStatus());
		}

		Document doc = response.getEntity(Document.class);
		return doc;
	}
	
	public static List<Double> getCosts(String configID, JsonObject vars, String nefologRoot)
	{
		List<Double> result = new ArrayList<Double>();
		String uri = nefologRoot + "/costCalculator?configid=" + configID;

		Set<Entry<String, JsonElement>> set = vars.entrySet();
		Iterator<Entry<String, JsonElement>> it = set.iterator();
		while (it.hasNext())
		{
			Entry<String, JsonElement> itm = it.next();
			uri += "&" + itm.getKey() + "=" + itm.getValue().getAsString();
		}
		
		Client client = Client.create();
		WebResource webResource = client.resource(uri);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
		
		if (response.getStatus() != 200) {	
			throw new RuntimeException("Failed: HTTP eCode: " + response.getStatus());
		}

		Document doc = response.getEntity(Document.class);
		NodeList ress = doc.getElementsByTagName("result");
		
		for (int i = 0; i < ress.getLength(); i++)
		{
			Element res = (Element)ress.item(i);
			Node resCost = res.getElementsByTagName("cost").item(0);
			
			String resTemp = resCost.getFirstChild().getTextContent().replace("$", "").replace("€", "");
			Double resDbl = Double.parseDouble(resTemp);
			int tmp = (int)(resDbl * 100);
			result.add(tmp / 100.0);
		}
		
		Collections.sort(result);
		return result;
	}
	
	public static Double getLowestCost(String configID, JsonObject vars, String nefologRoot)
	{
		
		List<Double> result = getCosts(configID, vars, nefologRoot);
		if (result.size() == 0) return Double.NaN;
		else return result.get(0);
	}
	
	
}
