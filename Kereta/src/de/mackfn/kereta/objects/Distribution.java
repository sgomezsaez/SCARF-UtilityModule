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
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;

import de.mackfn.kereta.tools.Connector;

@XmlRootElement
@XmlType(propOrder={"id", "alias", "namespace", "applicationId", "representation", "language", "langVersion", "author", "create"})
public class Distribution {

	private String representation;
	private String language;
	private String langVersion;
	private String author;
	private String applicationId;
	private Date create;
	private String alias;
	private String namespace;
	
	public Distribution()
	{
		this.id = "";
		this.representation = "";
		this.language = "";
		this.langVersion = "";
		this.author = "";
		this.create = null;
		this.alias = "";
		this.namespace = "";
		this.applicationId = "";
	}
	
	private static Distribution parseResultSet(ResultSet set)
	{
		Distribution result = null;
		if (set != null) {
			try {
				String id = set.getString("id");
				result = new Distribution();
				result.setId(id);
				result.setRepresentation(set.getString("topology"));
				result.setLanguage(set.getString("topology_language"));
				result.setLangVersion(set.getString("topology_language_version"));
				result.setAuthor(set.getString("author"));
				result.setCreate(set.getTimestamp("create"));
				result.setAlias(set.getString("alias"));
				result.setNamespace(set.getString("namespace"));
				result.setApplicationId(set.getString("application_id"));
			} catch (Exception e) {
				result = null;
			}
		}
		return result;
	}
	
	public static Distribution createDistribution(Connector con)
	{
		java.util.UUID uuid = java.util.UUID.randomUUID();
		String sql = "INSERT INTO kereta_distribution (id) VALUES (?)";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, uuid.toString());
			
			prep.execute();

		} catch (Exception e) {
			return null;
		}
		return getDistribution(con, uuid.toString());
	}
	
	public boolean persist(Connector con)
	{
		String sql = "UPDATE kereta_distribution SET topology=?, application_id=?, topology_language=?, topology_language_version=?, author=?, alias=?, namespace=? WHERE id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.representation);
			prep.setString(2, this.applicationId);
			prep.setString(3, this.language);
			prep.setString(4, this.langVersion);
			prep.setString(5, this.author);
			prep.setString(6, this.alias);
			prep.setString(7, this.namespace);
			prep.setString(8, this.id);
			prep.execute();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean delete(Connector con)
	{
		String sql = "DELETE FROM kereta_distribution WHERE id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.id);
			prep.execute();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static Distribution getDistribution(Connector con, String id)
	{
		Distribution result = null;
		String sql = "SELECT * FROM kereta_distribution WHERE id=? OR alias=?";
		//if (id.length() == 36) sql = "SELECT * FROM kereta_distribution WHERE id=?";
		//else sql = "SELECT * FROM kereta_distribution WHERE alias=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			prep.setString(2, id);
			ResultSet set = prep.executeQuery();
			if (set.next()) {
				result = parseResultSet(set);
			}
		} catch (Exception e) { }
		return result;
	}
	
	public static List<String> getDistributionIDs(Connector con)
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT id FROM kereta_distribution";
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
	
	public static List<String> getDistributionIDsByApplication(Connector con, String applicationID)
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT id FROM kereta_distribution WHERE application_id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, applicationID);
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
		Marshaller marshaller = JAXBContext.newInstance(Distribution.class).createMarshaller();
		DOMResult res = new DOMResult();
		marshaller.marshal(this, res);
		return (Document)res.getNode();
	}
	
	private String id;
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getRepresentation() {
		return representation;
	}

	public final void setRepresentation(String representation) {
		this.representation = representation;
	}

	public final String getLanguage() {
		return language;
	}

	public final void setLanguage(String language) {
		this.language = language;
	}

	public final String getLangVersion() {
		return langVersion;
	}

	public final void setLangVersion(String langVersion) {
		this.langVersion = langVersion;
	}

	public final String getAuthor() {
		return author;
	}

	public final void setAuthor(String author) {
		this.author = author;
	}

	public final String getApplicationId() {
		return applicationId;
	}

	public final void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
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
	
	public final String getNamespace() {
		return namespace;
	}

	public final void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
