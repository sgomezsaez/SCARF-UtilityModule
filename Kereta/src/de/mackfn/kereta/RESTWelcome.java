package de.mackfn.kereta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import de.mackfn.kereta.config.DbInit;
import de.mackfn.kereta.tools.Connector;

@Path("/")
public class RESTWelcome {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getWelcome() throws Exception
	{
		@SuppressWarnings("resource")
		String result = new Scanner(getClass().getResourceAsStream("/developer.html"), "UTF-8").useDelimiter("\\A").next();

		return Response.status(200).entity(result).build();
	}

	@Path("Installation")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getInstallation()
	{
		String result = "<!DOCTYPE html><html><head><title>Kereta API</title></head>";
		result += "<body style='font-family: monospace;'>";
		result += "<p>Please name a existing MySQL database, user and password.<br>";
		result += "Recommended database options: Character set=utf8; Collation order=utf8_bin.<br>";
		result += "User requires at least SELECT, INSERT, UPDATE AND DELETE permissions.</p>";
		result += "<form action='Installation' method='post'>";
		result += "<table>";
		result += "<tr><td><b>Local Kereta Root:</b></td><td><input type='text' name='root' value='http://localhost:8080/Kereta'></td><tr>";
		result += "<tr><td><b>Nefolog Root:</b></td><td><input type='text' name='nefolog' value='http://scarf-nefolog:8080/nefolog'></td><tr>";
		result += "<tr><td>&nbsp;</td><td></td></tr>";
		result += "<tr><td><b>Database Name:</b></td><td><input type='text' name='dbName' value='Kereta'></td><tr>";
		result += "<tr><td><b>Database Host:</b></td><td><input type='text' name='dbHost' value='scarf-kereta-database'></td></tr>";
		result += "<tr><td><b>Database Port:</b></td><td><input type='text' name='dbPort' value='3306'></td></tr>";
		result += "<tr><td><b>Database User:</b></td><td><input type='text' name='dbUser' value='user'></td></tr>";
		result += "<tr><td><b>Database Password:</b></td><td><input type='text' name='password'></td></tr>";
		result += "<tr><td><b>Drop Existing Tables:</b></td><td><input type='checkbox' name='cDrop' checked='true'> (requires DROP permission.)</td></tr>";
		result += "<tr><td><b>Create Tables:</b></td><td><input type='checkbox' name='cTable' checked='true'> (requires CREATE permission.)</td></tr>";
		result += "<tr><td><b>Inital Resources:</b></td><td><input type='checkbox' checked='true' name='cResources'></td></tr>";
		result += "<tr><td></td><td><input type='submit' value='Submit'></td</tr>";
		result += "</table>";
		result += "</form>";
		result += "</body></html>";

		return Response.status(200).entity(result).build();
	}

	@Path("Installation")
	@POST
	@Produces(MediaType.TEXT_HTML)
	public Response getCreateTables(
			@FormParam("root") String root, @FormParam("nefolog") String nefolog,
			@FormParam("dbName") String db,
			@FormParam("dbHost") String host, @FormParam("dbPort") String port,
			@FormParam("dbUser") String user, @FormParam("dbPW") String pw,
			@FormParam("cDrop") String drop, @FormParam("cTable") String table, @FormParam("cResources") String resources) throws Exception
	{
		String result = "";
		List<String> tables = new ArrayList<String>();
		tables.add("kereta_applicationType");
		tables.add("kereta_functionType");
		tables.add("kereta_dataType");
		tables.add("kereta_requirementType");
		tables.add("kereta_application");
		tables.add("kereta_distribution");
		tables.add("kereta_offering");
		tables.add("kereta_offeringTier");
		tables.add("kereta_requirement");
		tables.add("kereta_performance");
		tables.add("kereta_utilityFunction");
		tables.add("kereta_subfunction");
		tables.add("kereta_function");
		tables.add("kereta_parameter");

		String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + db;

		//File fpath = new File("kereta");
		//if (!fpath.exists()) fpath.mkdir();

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element xRoot = doc.createElement("settings");
		doc.appendChild(xRoot);
		Element xKereta = doc.createElement("KeretaRoot");
		xKereta.setTextContent(root);
		xRoot.appendChild(xKereta);
		Element xNefolog = doc.createElement("NefologRoot");
		xNefolog.setTextContent(nefolog);
		xRoot.appendChild(xNefolog);
		Element xUrl = doc.createElement("url");
		xUrl.setTextContent(dbUrl);
		xRoot.appendChild(xUrl);
		Element xUser = doc.createElement("user");
		xUser.setTextContent(user);
		xRoot.appendChild(xUser);
		Element xPW = doc.createElement("password");
		xPW.setTextContent(pw);
		xRoot.appendChild(xPW);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		String resURL = getClass().getClassLoader().getResource("/").getPath();
		StreamResult streamResult =  new StreamResult(resURL + "kereta.xml");
		transformer.transform(source, streamResult);

		boolean bDrop = (drop != null);
		boolean bTable = (table != null);
		boolean bResources = (resources != null);

		boolean connected = false;
		String cStatus = "failed";
		Connector con = new Connector(getClass().getClassLoader().getResource("/").getPath() + "kereta.xml");
		if (con.isConnected())
		{
			connected = true;
			cStatus = "ok";
		}

		result += "<!DOCTYPE html><html><head><title>Kereta API</title></head>";
		result += "<body style='font-family: monospace;'>";
		result += "<table>";
		result += "<tr><td><b>Kereta Root:</b></td><td>" + root + "</td><tr>";
		result += "<tr><td><b>Nefolog Root:</b></td><td>" + nefolog + "</td><tr>";
		result += "<tr><td>&nbsp;</td><td></td><tr>";
		result += "<tr><td><b>Database URL:</b></td><td>" + dbUrl + "</td><tr>";
		result += "<tr><td><b>Try to connect:</b></td><td>" + cStatus + "</td><tr>";
		if (connected)
		{
			result += "<tr><td>&nbsp;</td><td></td><tr>";
			if (bDrop)
			{
				Iterator<String> it = tables.iterator();
				while (it.hasNext())
				{
					String tab = it.next();
					if (DbInit.DropTable(con, tab)) result += "<tr><td><b>DROP " + tab + ":</b></td><td>ok</td><tr>";
					else result += "<tr><td><b>DROP " + tab + ":</b></td><td>failed</td><tr>";
				}

			}
			result += "<tr><td>&nbsp;</td><td></td><tr>";
			if (bTable)
			{
				for (int i = 0; i < 4; i++)
				{
					String tab = tables.get(i);
					if (DbInit.Type(con,tab)) result += "<tr><td><b>CREATE " + tab + ":</b></td><td>ok</td><tr>";
					else result += "<tr><td><b>CREATE " + tab + ":</b></td><td>failed</td><tr>";
				}

				if (DbInit.Application(con)) result += "<tr><td><b>CREATE kereta_application:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_application:</b></td><td>failed</td><tr>";

				if (DbInit.Distribution(con)) result += "<tr><td><b>CREATE kereta_distribution:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_distribution:</b></td><td>failed</td><tr>";

				if (DbInit.Offering(con)) result += "<tr><td><b>CREATE kereta_offering:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_offering:</b></td><td>failed</td><tr>";

				if (DbInit.OfferingTier(con)) result += "<tr><td><b>CREATE kereta_offeringTier:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_offeringTier:</b></td><td>failed</td><tr>";

				if (DbInit.Requirement(con)) result += "<tr><td><b>CREATE kereta_requirement:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_requirement:</b></td><td>failed</td><tr>";

				if (DbInit.Performance(con)) result += "<tr><td><b>CREATE kereta_performance:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_performance:</b></td><td>failed</td><tr>";

				if (DbInit.UtilityFunction(con)) result += "<tr><td><b>CREATE kereta_utilityFunction:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_utilityFunction:</b></td><td>failed</td><tr>";

				if (DbInit.Subfunction(con)) result += "<tr><td><b>CREATE kereta_subfunction:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_subfunction:</b></td><td>failed</td><tr>";

				if (DbInit.Function(con)) result += "<tr><td><b>CREATE kereta_function:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_function:</b></td><td>failed</td><tr>";

				if (DbInit.Parameter(con)) result += "<tr><td><b>CREATE kereta_parameter:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>CREATE kereta_parameter:</b></td><td>failed</td><tr>";

				result += "<tr><td>&nbsp;</td><td></td></tr>";

				if (DbInit.MandatoryResources(con)) result += "<tr><td><b>INSERT Mandatory Resources:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>INSERT Mandatory Type Resources:</b></td><td>failed</td><tr>";
			}


			if (bResources)
			{
				InputSource src = new InputSource(getClass().getResourceAsStream("/initial.xml"));
				Document initial = docBuilder.parse(src);

				result += "<tr><td>&nbsp;</td><td></td></tr>";
				if (DbInit.InitialResources(con, initial)) result += "<tr><td><b>INSERT Inital Function Resources:</b></td><td>ok</td><tr>";
				else result += "<tr><td><b>INSERT Inital Function Resources:</b></td><td>failed</td><tr>";

			}
		}
		else
		{
			result += "<tr> <td></td><td><td></tr>";
			result += "<tr><td></td><td>" + con.getErrorMessage() + "<td></tr>";
			result += "<tr><td></td><td>Please verify database URL, username and password.<td></tr>";
			result += "<tr><td></td><td><form action='Installation' method='get'><input type='submit' value='back'></form></td</tr>";
		}
		result += "</body></html>";

		return Response.status(200).entity(result).build();
	}
}

