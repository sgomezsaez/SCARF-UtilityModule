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
@XmlType(propOrder={"distributionId", "offeringNumber", "name", "value", "demand", "dataType", "requirementType", "author", "create"})
public class Performance {

	private String name;
	private String distributionId;
	private int offeringNumber;
	private String value;
	private String demand;
	private String dataType;
	private String requirementType;
	private Date create;
	private String author;
	
	public Performance()
	{

	}
	
	private static Performance parseResultSet(ResultSet set)
	{
		Performance result = null;
		if (set != null) {
		try
		{
				result = new Performance();
				result.setName(set.getString("name"));
				result.setDistributionId(set.getString("distribution_id"));
				result.setOfferingNumber(set.getInt("offering_number"));
				result.setValue(set.getString("value"));
				result.setDemand(set.getString("demand"));
				result.setDataType(set.getString("data_type"));
				result.setRequirementType(set.getString("requirement_type"));
				result.setAuthor(set.getString("author"));
				result.setCreate(set.getTimestamp("create"));
		}
		catch (Exception e) 
		{
			return null;
		}
		}	
		return result;
	}
	
	public static Performance create(Connector con, String distributionId, int offeringNumber, String name) throws Exception
	{
		String sql = "INSERT INTO kereta_performance (name, distribution_id, offering_number) VALUES (?, ?, ?)";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, name);
			prep.setString(2, distributionId);
			prep.setInt(3, offeringNumber);
			prep.execute();

		} catch (Exception e) {
			return null;
		}
		return Performance.getPerformance(con, distributionId, offeringNumber, name);
	}
	
	public boolean persist(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_performance SET value=?, demand=?, data_type=?, requirement_type=?, author=? WHERE name=? AND distribution_id=? AND offering_number=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.value);
			prep.setString(2, this.demand);
			prep.setString(3, this.dataType);
			prep.setString(4, this.requirementType);
			prep.setString(5, this.author);
			prep.setString(6, this.name);
			prep.setString(7, this.distributionId);
			prep.setInt(8, this.offeringNumber);
			prep.execute();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean delete(Connector con) throws Exception
	{
		String sql = "DELETE FROM kereta_performance WHERE name=? AND distribution_id=? AND offering_number=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.name);
			prep.setString(2, this.distributionId);
			prep.setInt(3, this.offeringNumber);
			prep.execute();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static Performance getPerformance(Connector con, String distributionId, int offeringNumber, String name) throws Exception
	{
		Performance result = null;
		String sql = "SELECT * FROM kereta_performance WHERE name=? AND distribution_id=? AND offering_number=?";

		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, name);
		prep.setString(2, distributionId);
		prep.setInt(3, offeringNumber);
		ResultSet set = prep.executeQuery();
		if (set.next()) {
			result = parseResultSet(set);
		}

		return result;
	}
	
	public static List<String> getOfferingPerformanceNames(Connector con, String distributionId, int offeringNumber) throws Exception
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT name FROM kereta_performance WHERE distribution_id=? AND offering_number=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, distributionId);
		prep.setInt(2, offeringNumber);
		ResultSet set = prep.executeQuery();
		while (set.next()) {
			result.add(set.getString("name"));
		}

		return result;
	}
	
	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(Performance.class).createMarshaller();
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

	public final String getDistributionId() {
		return distributionId;
	}

	public final void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
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
	
	public final int getOfferingNumber() {
		return offeringNumber;
	}

	public final void setOfferingNumber(int offeringNumber) {
		this.offeringNumber = offeringNumber;
	}
	
	public final String getDemand() {
		return demand;
	}

	public final void setDemand(String demand) {
		this.demand = demand;
	}

}
