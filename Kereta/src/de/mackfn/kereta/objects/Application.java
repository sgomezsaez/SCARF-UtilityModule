package de.mackfn.kereta.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.mackfn.kereta.tools.Connector;

@XmlRootElement(name = "application")
@XmlType(propOrder={"id", "tierNbr" ,"alias", "name", "description", "applicationType", "author", "create"})
public class Application {

	private String id;
	private int tierNbr;
	private String description;
	private String name;
	private String applicationType;
	private String author;
	private Date create;
	private String alias;
	DocumentBuilder docBuilder;

	public Application()
	{
		tierNbr = 0;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			this.docBuilder = docFactory.newDocumentBuilder();
			
		} catch (Exception e) { }
	}
	
	private static Application parseResultSet(ResultSet set) throws Exception
	{
		Application result = null;
		if (set != null) {

			result = new Application();
			result.setId(set.getString("id"));
			result.setTierNbr(set.getInt("tier"));
			result.setName(set.getString("name"));
			result.setDescription(set.getString("description"));
			result.setApplicationType(set.getString("application_type"));
			result.setAlias(set.getString("alias"));
			result.setAuthor(set.getString("author"));
			result.setCreate(set.getTimestamp("create"));

		}
		return result;
	}
	
	public static Application create(Connector con) throws Exception
	{
		java.util.UUID uuid = java.util.UUID.randomUUID();
		String sql = "INSERT INTO kereta_application (id, tier) VALUES (?, ?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, uuid.toString());
		prep.setInt(2, 0);
		
		prep.execute();

		return getApplication(con, uuid.toString(), 0);
	}
	
	public static Application create(Connector con, String id, int tier) throws Exception
	{
		String sql = "INSERT INTO kereta_application (id, tier) VALUES (?, ?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, id);
		prep.setInt(2, tier);
		
		prep.execute();

		return getApplication(con, id, tier);
	}
	
	public boolean persist(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_application SET name=?, description=?, application_type=?, author=?, alias=? WHERE id=? and tier=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, this.name);
		prep.setString(2, this.description);
		prep.setString(3, this.applicationType);
		prep.setString(4, this.author);
		prep.setString(5, this.alias);
		prep.setString(6, this.id);
		prep.setInt(7, this.tierNbr);
		prep.execute();

		return true;
	}

	public boolean delete(Connector con) throws Exception
	{
		String sql;
		if (this.tierNbr == 0) sql = "DELETE FROM kereta_application WHERE id=?";
		else sql = "DELETE FROM kereta_application WHERE id=? AND tier=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, this.id);
		if (this.tierNbr > 0) prep.setInt(2, this.tierNbr);
		prep.execute();

		return true;
	}

	
	public static Application getApplication(Connector con, String id, int tier) throws Exception
	{
		Application result = null;
		String sql= "SELECT * FROM kereta_application WHERE (alias=? OR id=?) AND tier=?";
		//if (id.length() == 36) sql = "SELECT * FROM kereta_application WHERE id=? and tier=?";
		//else sql = "SELECT * FROM kereta_application WHERE alias=? and tier=?";
		
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, id);
		prep.setString(2, id);
		prep.setInt(3, tier);
		
		ResultSet set = prep.executeQuery();
		if (set.next()) {
			result = parseResultSet(set);
		}

		return result;
	}
	
	public static List<Integer> getChildApplicationNbrs(Connector con, String id) throws Exception
	{
		List<Integer> result = new ArrayList<Integer>();
		String sql = "SELECT tier FROM kereta_application WHERE id=? AND tier!=0";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, id);
		ResultSet set = prep.executeQuery();
		while (set.next()) {
			result.add(set.getInt("tier"));
		}

		return result;
	}
	
	public static List<String> getApplicationIDs(Connector con)
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT id FROM kereta_application WHERE tier=0";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			ResultSet set = prep.executeQuery();
			while (set.next()) {
				result.add(set.getString("id"));
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	public static List<String> getApplicationIDsByType(Connector con, String applicationType)
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT id FROM kereta_application WHERE application_type=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, applicationType);
			ResultSet set = prep.executeQuery();
			while (set.next()) {
				result.add(set.getString("id"));
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(Application.class).createMarshaller();
		DOMResult res = new DOMResult();
		marshaller.marshal(this, res);
		Document result = (Document)res.getNode();
		if (this.isApplication())
		{
			NodeList tmp = result.getDocumentElement().getElementsByTagName("tierNbr");
			for (int i = 0; i < tmp.getLength(); i++) result.getDocumentElement().removeChild(tmp.item(i));
		}
		else 
		{
			NodeList tmp = result.getDocumentElement().getElementsByTagName("id");
			for (int i = 0; i < tmp.getLength(); i++) {
				Node nId = tmp.item(i);
				Element nAId = result.createElement("applicationId");
				nAId.setTextContent(nId.getTextContent());
				result.getDocumentElement().replaceChild(nAId, nId);
			}
			NodeList tmpAlias = result.getDocumentElement().getElementsByTagName("alias");
			for (int i = 0; i < tmpAlias.getLength(); i++) result.getDocumentElement().removeChild(tmpAlias.item(i));
			tmpAlias = result.getDocumentElement().getElementsByTagName("applicationType");
			for (int i = 0; i < tmpAlias.getLength(); i++) result.getDocumentElement().removeChild(tmpAlias.item(i));
			
			Document tierDoc = this.docBuilder.newDocument();
			Element tierEl = tierDoc.createElement("tier");
			tierDoc.appendChild(tierEl);
			
			for (int i = 0; i < result.getDocumentElement().getChildNodes().getLength(); i++)
			{
				Node move = result.getDocumentElement().getChildNodes().item(i);
				Node imp = tierDoc.importNode(move, true);
				tierEl.appendChild(imp);
			}
			result = tierDoc;
		}

		return result;
	}
	
	public boolean isTier()
	{
		return (this.tierNbr > 0);
	}
	
	public boolean isApplication()
	{
		return (this.tierNbr == 0);
	}
	
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final int getTierNbr() {
		return tierNbr;
	}

	public final void setTierNbr(int tierNbr) {
		this.tierNbr = tierNbr;
	}
	
	/*public final String getTier() {
		return tier;
	}

	public final void setTier(String tier) {
		this.tier = tier;
	}*/
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getApplicationType() {
		return applicationType;
	}

	public final void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public final String getAuthor() {
		return author;
	}

	public final void setAuthor(String author) {
		this.author = author;
	}

	public final Date getCreate() {
		return create;
	}

	public final void setCreate(Date create) {
		this.create = create;
	}

	public final String getAlias() {
		return alias;
	}

	public final void setAlias(String alias) {
		this.alias = alias;
	}
	
	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}
	
}
