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
@XmlType(propOrder={"applicationId", "applicationTier", "name", "value", "demand", "dataType", "requirementType", "author", "create"})
public class Requirement {

	private String name;
	private String applicationId;
	private int applicationTier;
	private String value;
	private String demand;
	private String dataType;
	private String requirementType;
	private Date create;
	private String author;
	
	public Requirement()
	{

	}
	
	private static Requirement parseResultSet(ResultSet set)
	{
		Requirement result = null;
		if (set != null) {
			try {
				result = new Requirement();
				result.setName(set.getString("name"));
				result.setApplicationId(set.getString("application_id"));
				result.setApplicationTier(set.getInt("application_tier"));
				result.setValue(set.getString("value"));
				result.setDemand(set.getString("demand"));
				result.setDataType(set.getString("data_type"));
				result.setRequirementType(set.getString("requirement_type"));
				result.setAuthor(set.getString("author"));
				result.setCreate(set.getTimestamp("create"));

			} catch (Exception e) {
				result = null;
			}
		}
		return result;
	}
	
	public static Requirement create(Connector con, String applicationId, int applicationTier, String name) throws Exception
	{
		String sql = "INSERT INTO kereta_requirement (name, application_id, application_tier) VALUES (?, ?, ?)";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, name);
			prep.setString(2, applicationId);
			prep.setInt(3, applicationTier);
			prep.execute();

		} catch (Exception e) {
			return null;
		}
		return Requirement.getRequirement(con, applicationId, applicationTier, name);
	}
	
	public boolean persist(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_requirement SET value=?, demand=?, data_type=?, requirement_type=?, author=? WHERE name=? AND application_id=? AND application_tier=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.value);
			prep.setString(2, this.demand);
			prep.setString(3, this.dataType);
			prep.setString(4, this.requirementType);
			prep.setString(5, this.author);
			prep.setString(6, this.name);
			prep.setString(7, this.applicationId);
			prep.setInt(8, this.applicationTier);
			prep.execute();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean delete(Connector con) throws Exception
	{
		String sql = "DELETE FROM kereta_requirement WHERE name=? AND application_id=? AND application_tier=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.name);
			prep.setString(2, this.applicationId);
			prep.setInt(3, this.applicationTier);
			prep.execute();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static Requirement getRequirement(Connector con, String applicationId, int applicationTier, String name) throws Exception
	{
		Requirement result = null;
		String sql = "SELECT * FROM kereta_requirement WHERE name=? AND application_id=? AND application_tier=?";

		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, name);
		prep.setString(2, applicationId);
		prep.setInt(3, applicationTier);
		ResultSet set = prep.executeQuery();
		if (set.next()) {
			result = parseResultSet(set);
		}

		return result;
	}
	
	public static List<String> getApplicationRequirementNames(Connector con, String applicationId, int applicationTier, String requirementType) throws Exception
	{
		List<String> result = new ArrayList<String>();
		String sql;
		if (requirementType.equals("")) sql = "SELECT name FROM kereta_requirement WHERE application_id=? AND application_tier=?";
		else sql = "SELECT name FROM kereta_requirement WHERE application_id=? AND application_tier=? AND requirement_type=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, applicationId);
		prep.setInt(2, applicationTier);
		if (!requirementType.equals("")) prep.setString(3, requirementType);
		ResultSet set = prep.executeQuery();
		while (set.next()) {
			result.add(set.getString("name"));
		}

		return result;
	}
	
	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(Requirement.class).createMarshaller();
		DOMResult res = new DOMResult();
		marshaller.marshal(this, res);
		return (Document)res.getNode();
	}
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getApplicationId() {
		return applicationId;
	}

	public final void setApplicationId(String topologyId) {
		this.applicationId = topologyId;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

	public final String getDataType() {
		return dataType;
	}

	public final void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public final String getRequirementType() {
		return requirementType;
	}

	public final void setRequirementType(String type) {
		this.requirementType = type;
	}
	
	public final Date getCreate() {
		return create;
	}

	public final void setCreate(Date create) {
		this.create = create;
	}

	public final String getAuthor() {
		return author;
	}

	public final void setAuthor(String author) {
		this.author = author;
	}
	
	public final int getApplicationTier() {
		return applicationTier;
	}

	public final void setApplicationTier(int applicationTier) {
		this.applicationTier = applicationTier;
	}
	
	public final String getDemand() {
		return demand;
	}

	public final void setDemand(String demand) {
		this.demand = demand;
	}
	
}
