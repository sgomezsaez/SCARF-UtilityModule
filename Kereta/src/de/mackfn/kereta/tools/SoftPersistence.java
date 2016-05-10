package de.mackfn.kereta.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class SoftPersistence {
	
	public static String createParameterSet(String utilityFunctionID, int subFunctionNumber, Map<String, String> assignment, String ident, String folder) throws Exception
	{
		File fpath = new File(folder);
		if (!fpath.exists()) fpath.mkdir();
		
		Document doc = null;
		Element root = null;
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		File file = new File(folder + utilityFunctionID + ".xml");
		if (!file.exists())
		{
			doc = docBuilder.newDocument();
			
			root = doc.createElement("assignments");
			root.setAttribute("utilityFunction", utilityFunctionID);
			doc.appendChild(root);
		}
		else 
		{
			doc = docBuilder.parse(file);
			root = doc.getDocumentElement();
		}
		
		Element sub = null;
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList)xPath.evaluate("subfunction[@number='" + subFunctionNumber + "']", root, XPathConstants.NODESET);
		if (nodes.getLength() > 0) 
		{
			sub = (Element)nodes.item(0);
		}
		else
		{
			sub = doc.createElement("subfunction");
			sub.setAttribute("number", Integer.toString(subFunctionNumber));
			root.appendChild(sub);
		}

		
		nodes = (NodeList)xPath.evaluate("parms[@id='" + ident + "']", sub, XPathConstants.NODESET);
		if  (nodes.getLength() == 1) 
		{
			Element el = (Element)nodes.item(0);
			el.getParentNode().removeChild(el);
		}
		
		Element parms = doc.createElement("parms");
		parms.setAttribute("id", ident);
		sub.appendChild(parms);
		
		for(Entry<String, String> item : assignment.entrySet()) {
			Element par = doc.createElement(item.getKey());
			par.setTextContent(item.getValue());
			parms.appendChild(par);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult streamResult =  new StreamResult(file);
		transformer.transform(source, streamResult);	

		return ident;
	}

	public static Map<String, String> getParameterSet(String utilityFunctionID, int subFunctionNumber, String ident, String folder) throws Exception
	{
		Map<String, String> result = new HashMap<String, String>();
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		File file = new File(folder + utilityFunctionID + ".xml");
		if (file.exists())
		{
			Document doc = docBuilder.parse(file);
			Element root = doc.getDocumentElement();

			Element sub = null;
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList)xPath.evaluate("subfunction[@number='" + subFunctionNumber + "']", root, XPathConstants.NODESET);
			if (nodes.getLength() > 0) 
			{
				sub = (Element)nodes.item(0);
				
				nodes = (NodeList)xPath.evaluate("parms[@id='" + ident + "']", sub, XPathConstants.NODESET);
				if  (nodes.getLength() == 1) 
				{
					Element parms = (Element)nodes.item(0);
					NodeList pList = parms.getChildNodes();
					for (int i = 0; i < pList.getLength(); i++)
					{
						Element parm = (Element)pList.item(i);
						result.put(parm.getTagName(), parm.getTextContent());
					}
				}
			}

		}
		return result;
	}

	public static JsonObject getParameterJson(String utilityFunctionID, int subFunctionNumber, String ident, String folder) throws Exception
	{
		Map<String, String> vSet = getParameterSet(utilityFunctionID, subFunctionNumber, ident, folder);
		Iterator<String> it = vSet.keySet().iterator();
		String jsonStr = "";
		while (it.hasNext()) 
		{
			String k = it.next();
			String v = vSet.get(k);
			if (jsonStr.equals("")) jsonStr += "{ ";
			else jsonStr += ", ";
			jsonStr += "\"" + k + "\": " + v;
		}
		
		if (jsonStr.equals("")) jsonStr = "{ }";
		else jsonStr += " }";
		Gson gson = new Gson();
		JsonElement element = gson.fromJson (jsonStr, JsonElement.class);
		JsonObject jsonObj = element.getAsJsonObject();
		
		return jsonObj;
	}
}
