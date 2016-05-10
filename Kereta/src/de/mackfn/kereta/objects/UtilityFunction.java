package de.mackfn.kereta.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;

import de.mackfn.kereta.tools.Connector;

@XmlRootElement(name = "utilityFunction")
public class UtilityFunction {

	private String id;
	private String distributionId;
	private String description;
	private String author;
	private Date create;
	private String alias;
	
	public UtilityFunction()
	{
		this.id = "";
		this.distributionId = "";
		this.description = "";
		this.author = "";
		this.create = null;
		this.alias = "";
	}
	
	private static UtilityFunction parseResultSet(ResultSet set) throws Exception
	{
		UtilityFunction result = null;
		if (set != null) {
			result = new UtilityFunction();
			result.setId(set.getString("id"));
			result.setDistributionId(set.getString("distribution_id"));
			result.setDescription(set.getString("description"));
			result.setAuthor(set.getString("author"));
			result.setCreate(set.getTimestamp("create"));
			result.setAlias(set.getString("alias"));
		}
		return result;
	}
	
	public static UtilityFunction createUtilityFunction(Connector con, String distributionId, String description, String author, String alias) throws Exception
	{
		java.util.UUID uuid = java.util.UUID.randomUUID();
		String sql = "INSERT INTO kereta_utilityFunction (id, distribution_id, description, author, alias) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, uuid.toString());
		prep.setString(2, distributionId);
		prep.setString(3, description);
		prep.setString(4, author);
		prep.setString(5, alias);
		prep.execute();

		return UtilityFunction.getUtilityFunction(con, uuid.toString());
	}
	
	public static UtilityFunction createUtilityFunction(Connector con) throws Exception
	{
		java.util.UUID uuid = java.util.UUID.randomUUID();
		String sql = "INSERT INTO kereta_utilityFunction (id) VALUES (?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, uuid.toString());
		prep.execute();

		return UtilityFunction.getUtilityFunction(con, uuid.toString());
	}
	
	public boolean persist(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_utilityFunction SET distribution_id=?, description=?, author=?, alias=? WHERE id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.getDistributionId());
			prep.setString(2, this.getDescription());
			prep.setString(3, this.getAuthor());
			prep.setString(4, this.getAlias());
			prep.setString(5, this.getId());
			prep.execute();
		}
		catch (Exception e) { return false; }

		return true;
	}
	
	public boolean delete(Connector con)
	{
		try {
			String sql = "DELETE FROM kereta_utilityFunction WHERE id=?";
			PreparedStatement prep;
	
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.getId());
			prep.execute();
		}
		catch (Exception e) { return false; }
		return true;
	}
	
	public static UtilityFunction getUtilityFunction(Connector con, String id) throws Exception
	{
		UtilityFunction result = null;
		String sql = "SELECT * FROM kereta_utilityFunction WHERE id=? OR alias=?";
		//if (id.length() == 36) sql = "SELECT * FROM kereta_utilityFunction WHERE id=?";
		//else sql = "SELECT * FROM kereta_utilityFunction WHERE alias=?";
		PreparedStatement prep;
		//try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			prep.setString(2, id);
			ResultSet set = prep.executeQuery();
			if (set.next()) {
				result = parseResultSet(set);
			}
		/*} catch (Exception e) {
		}*/
		return result;
	}
	
	public static List<String> getUtilityFunctionIDs(Connector con) throws Exception
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT id FROM kereta_utilityFunction";
		PreparedStatement prep;
		//try {
			prep = con.getConnection().prepareStatement(sql);
			ResultSet set = prep.executeQuery();
			while (set.next()) {
				result.add(set.getString("id"));
			}
		/*} catch (Exception e) {
			
		}*/
		return result;
	}
	
	public static List<String> getDistributionUtilityFunctionIDs(Connector con, String distributionId) throws Exception
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT id FROM kereta_utilityFunction WHERE distribution_id=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, distributionId);
		ResultSet set = prep.executeQuery();
		while (set.next()) {
			result.add(set.getString("id"));
		}

		return result;
	}
	
	public static List<String> getApplicationTypeUtilityFunctionIDs(Connector con, String applicationType) throws Exception
	{
		List<String> result = new ArrayList<String>();
		
		String sql = "SELECT uf.id FROM (kereta_utilityFunction AS uf INNER JOIN (kereta_application AS app INNER JOIN kereta_distribution AS dist ON app.id=dist.application_id) ON uf.distribution_id=dist.id) WHERE app.application_type=?";
		PreparedStatement prep;
		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, applicationType);
		ResultSet set = prep.executeQuery();
		while (set.next()) {
			result.add(set.getString("id"));
		}
		return result;
	}
	
	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(UtilityFunction.class).createMarshaller();
		DOMResult res = new DOMResult();
		marshaller.marshal(this, res);
		return (Document)res.getNode();
	}
	
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getDistributionId() {
		return distributionId;
	}

	public final void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
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


	
}
