package de.mackfn.kereta.tools;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import de.mackfn.kereta.tools.Calculation.CalcTypes;

public class StaticTools {

	
	public static Response getResponse(int status, Object entity, boolean CORS, String origin)
	{
		if (CORS)
		{
			return Response
				.status(status)
				.header("Access-Control-Allow-Origin", origin)
	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
	            .header("Access-Control-Allow-Credentials", "true")
	            .header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTION, HEAD, DELETE")
	            .header("Access-Control-Max-Age", "1209600")
				.entity(entity)
				.build();
		}
		else 
		{
			return Response
					.status(status)
					.entity(entity)
					.build();
		}
	}
	
	public static Document getErrorDoc(String msg) throws Exception
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = doc.createElement("error");
		doc.appendChild(root);
		Element msgE = doc.createElement("message");
		msgE.setTextContent(msg);
		root.appendChild(msgE);
		return doc;
	}
	
	public static String xml2String(Document doc) 
	{
		try {
	        StringWriter sw = new StringWriter();
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        transformer.transform(new DOMSource(doc), new StreamResult(sw));
	        return sw.toString();
	    } catch (Exception ex) {
	        throw new RuntimeException("Error converting to String", ex);
	    }
	}
	
	public static String double2String(double value, int digits)
	{
		return String.format(Locale.US, "%." + digits +"f", value);
	}
	
	public static Document string2xml(String xmlString)
	{
		Document doc = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlString));
			doc = db.parse(is);
		}
		catch (Exception e) { }
		return doc;
	}
	
	public static HashMap<String, Double> json2MapDouble(String json)
	{
		HashMap<String,Double> result = new Gson().fromJson(json, new TypeToken<HashMap<String, Double>>(){}.getType());
		return result;
	}
	
	public static double JsonGetDouble(JsonObject jObject, String variable)
	{
		double result = Double.NaN;
		try {
			JsonElement jEl = jObject.get(variable);
			result = jEl.getAsDouble();
		}
		catch (Exception e) {}
		return result;
	}
	
	public static double[] jsonGetArrayDouble(JsonObject jObject, String variable)
	{
		double[] result = new double[0];
		try {
			JsonElement jEl = jObject.get(variable);
			JsonArray jAr = jEl.getAsJsonArray();
			result = new double[jAr.size()];
			for (int i = 0; i < jAr.size(); i++)
			{
				result[i] = jAr.get(i).getAsDouble();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		return result;
	}
	
	public static int[] jsonGetArrayInt(JsonObject jObject, String variable)
	{
		int[] result = new int[0];
		try {
			JsonElement jEl = jObject.get(variable);
			JsonArray jAr = jEl.getAsJsonArray();
			result = new int[jAr.size()];
			for (int i = 0; i < jAr.size(); i++)
			{
				result[i] = jAr.get(i).getAsInt();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		return result;
	}
	
	public static List<String> getVarsWithIndex(JsonObject jObject, String index)
	{
		List<String> result = new ArrayList<String>();
		Set<Entry<String, JsonElement>> set = jObject.entrySet();
		Iterator<Entry<String, JsonElement>> it = set.iterator();
		while (it.hasNext())
		{
			Entry<String, JsonElement> itm = it.next();
			if (Parser.hasIndex(itm.getKey().toString(), index)) result.add(itm.getKey());
		}
		return result;
	}
	
	public static JsonArray jsonGetJsonArray(JsonObject jObject, String variable)
	{
		JsonArray jAr = null;
		try {			
			JsonElement jEl = jObject.get(variable);
			jAr = jEl.getAsJsonArray();
		}
		catch (Exception e) { e.printStackTrace(); }
		return jAr;
	}
	
	public static Map<String, JsonArray> jsonGetJsonArrayMap(JsonObject jObject, List<String> variables)
	{
		Map<String, JsonArray> result = new HashMap<String, JsonArray>(); 
		Iterator<String> it = variables.iterator();
		while (it.hasNext())
		{
			String variable = it.next();
			JsonArray jAr = null;
			try {			
				JsonElement jEl = jObject.get(variable);
				jAr = jEl.getAsJsonArray();
			}
			catch (Exception e) { e.printStackTrace(); }
			result.put(variable, jAr);
		}
		return result;
	}
	
	public static String date2String(Date date, String format) 
	{
		   SimpleDateFormat f = new SimpleDateFormat(format);
		   return f.format(date);
	}
	
	public static boolean isDigit(String input) {  
	    return input.matches("[-+]?\\d*\\.?\\d+");  
	} 

	public static CalcTypes getType(String token)
	{
		String tok = token;
		if (token.contains("_")) tok = token.substring(0, token.indexOf('_'));
		
		Map<String, Calculation.CalcTypes> type = new HashMap<String, Calculation.CalcTypes>();
		type.put("+", CalcTypes.ADD);
		type.put("-", CalcTypes.SUB);
		type.put("*", CalcTypes.MUL);
		type.put("/", CalcTypes.DIV);
		type.put("^", CalcTypes.POW);
		type.put("MIN", CalcTypes.MIN);
		type.put("MAX", CalcTypes.MAX);
		type.put("SQU", CalcTypes.SQR);
		type.put("SQR", CalcTypes.SQROOT);
		type.put("ROT", CalcTypes.ROOT);
		type.put("SUM", CalcTypes.SUM);
		type.put("PCT", CalcTypes.PRCT);
		type.put("AND", CalcTypes.AND);
		type.put("ORR", CalcTypes.OR);
		type.put("XOR", CalcTypes.XOR);
		type.put("NOT", CalcTypes.NOT);
		type.put("IFF", CalcTypes.IF);
		type.put("IFE", CalcTypes.IFE);
		type.put("IGR", CalcTypes.INTGR);
		type.put("FCT", CalcTypes.FCT);
		type.put("FAC", CalcTypes.FAC);
		type.put(">", CalcTypes.BIGR);
		type.put("<", CalcTypes.SMLR);
		type.put("EQU", CalcTypes.EQU);
		type.put("LEQ", CalcTypes.ESML);
		type.put("BEQ", CalcTypes.EBIG);
		type.put("MOD", CalcTypes.MOD);
		
		if (type.containsKey(tok)) return type.get(tok);
		else return null;
	}
}
