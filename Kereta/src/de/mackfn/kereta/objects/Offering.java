package de.mackfn.kereta.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@XmlType(propOrder={"number", "distributionId", "nefologConfiguration", "nefologConfigurationId", "nefologOfferingName", "nefologServiceType", "nefologProvider", "author", "create"})
public class Offering 
{

	private int number;
	private String distributionId;
	private String nefologConfiguration;
	private String nefologConfigurationId;
	private String nefologOfferingName;
	private String nefologServiceType;
	private String nefologProvider;
	private Date create;
	private String author;
	
	public Offering()
	{
		
	}
	
	private static Offering parseResultSet(ResultSet set) throws Exception
	{
		Offering result = null;
		if (set != null) {
			result = new Offering();
			result.setNumber(set.getInt("number"));
			result.setNefologConfigurationId(set.getString("nefolog_configuration_id"));
			result.setNefologConfiguration(set.getString("nefolog_configuration"));
			result.setNefologOfferingName(set.getString("nefolog_offering_name"));
			result.setNefologServiceType(set.getString("nefolog_service_type"));
			result.setNefologProvider(set.getString("nefolog_provider"));
			result.setDistributionId(set.getString("distribution_id"));
			result.setAuthor(set.getString("author"));
			result.setCreate(set.getTimestamp("create"));
		}
		return result;
	}
	
	public static Offering create(Connector con, String distributionId, int number) throws Exception
	{
		if (Offering.getOffering(con, distributionId, number) != null) return null;
		
		String sql = "INSERT INTO kereta_offering (distribution_id, number) VALUES (?, ?)";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, distributionId);
			prep.setInt(2, number);
			prep.execute();

		} catch (Exception e) {
			return null;
		}
		return Offering.getOffering(con, distributionId, number);
	}
	
	public boolean persist(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_offering SET nefolog_configuration=?, nefolog_configuration_id=?, nefolog_offering_name=?, nefolog_service_type=?, nefolog_provider=?, author=? WHERE distribution_id=? and number=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, this.nefologConfiguration);
		prep.setString(2, this.nefologConfigurationId);
		prep.setString(3, this.nefologOfferingName);
		prep.setString(4, this.nefologServiceType);
		prep.setString(5, this.nefologProvider);
		prep.setString(6, this.author);
		prep.setString(7, this.distributionId);
		prep.setInt(8, this.number);
		prep.execute();

		return true;
	}
	
	public boolean delete(Connector con) throws SQLException, Exception
	{
		String sql = "DELETE FROM kereta_offering WHERE distribution_id=? and number=?";
		PreparedStatement prep;
		try
		{
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.distributionId);
			prep.setInt(2, this.number);
			prep.execute();
		}
		catch (Exception exc) {
			return false;
		}

		return true;
	}
	
	public static Offering getOffering(Connector con, String distributionId, int number) throws Exception
	{
		Offering result = null;
		String sql = "SELECT * FROM kereta_offering WHERE distribution_id=? and number=?";

		PreparedStatement prep;
		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, distributionId);
		prep.setInt(2, number);
		ResultSet set = prep.executeQuery();
		if (set.next()) {
			result = parseResultSet(set);
		}

		return result;
	}
	
	public static List<Integer> getDistributionOfferingNumbers(Connector con, String distributionId) throws Exception
	{
		List<Integer> result = new ArrayList<Integer>();
		String sql = "SELECT number FROM kereta_offering WHERE distribution_id=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, distributionId);
		ResultSet set = prep.executeQuery();
		while (set.next()) {
			result.add(set.getInt("number"));
		}

		return result;
	}
	
	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(Offering.class).createMarshaller();
		DOMResult res = new DOMResult();
		marshaller.marshal(this, res);
		return (Document)res.getNode();
	}
	
	public final int getNumber() {
		return number;
	}

	public final void setNumber(int number) {
		this.number = number;
	}

	public final String getDistributionId() {
		return distributionId;
	}

	public final void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}

	public final String getNefologConfigurationId() {
		return nefologConfigurationId;
	}

	public final void setNefologConfigurationId(String nefologConfigurationId) {
		this.nefologConfigurationId = nefologConfigurationId;
	}

	public final String getNefologOfferingName() {
		return nefologOfferingName;
	}

	public final void setNefologOfferingName(String nefologOfferingName) {
		this.nefologOfferingName = nefologOfferingName;
	}

	public final String getNefologServiceType() {
		return nefologServiceType;
	}

	public final void setNefologServiceType(String nefologServiceType) {
		this.nefologServiceType = nefologServiceType;
	}

	public final String getNefologProvider() {
		return nefologProvider;
	}

	public final void setNefologProvider(String nefologProvider) {
		this.nefologProvider = nefologProvider;
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
	
	public final String getNefologConfiguration() {
		return nefologConfiguration;
	}

	public final void setNefologConfiguration(String nefologConfiguration) {
		this.nefologConfiguration = nefologConfiguration;
	}

}
