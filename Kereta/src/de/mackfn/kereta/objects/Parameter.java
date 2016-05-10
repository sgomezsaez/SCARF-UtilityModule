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

@XmlRootElement(name="parameter")
@XmlType(propOrder={"name", "dataType", "defaultValue", "description", "functionId", "author", "create"})
public class Parameter {

	private String name;
	private String functionId;
	private String dataType;
	private String defaultValue;
	private String description;
	private String author;
	private Date create;
	
	private static Parameter parseResultSet(ResultSet set)
	{
		Parameter result = null;
		if (set != null) {
			try {
				result = new Parameter();
				result.setName(set.getString("name"));
				result.setFunctionId(set.getString("function_id"));
				result.setDataType(set.getString("data_type"));
				result.setDefaultValue(set.getString("default_value"));
				result.setAuthor(set.getString("author"));
				result.setCreate(set.getTimestamp("create"));
				result.setDescription(set.getString("description"));
			} catch (Exception e) {
				result = null;
			}
		}
		return result;
	}
	
	public static Parameter createParameter(Connector con, String name, String functionID, String dataType, String defaultValue, String description, String author) throws Exception
	{
		Parameter par = Parameter.getParameter(con, functionID, name);
		if (par != null) return null;
		
		String sql = "INSERT INTO kereta_parameter (name, function_id, data_type, default_value, description, author) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement prep;
		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, name);
		prep.setString(2, functionID);
		prep.setString(3, dataType);
		prep.setString(4, defaultValue);
		prep.setString(5, description);
		prep.setString(6, author);
		prep.execute();

		return Parameter.getParameter(con, functionID, name);
	}
	
	public static Parameter createParameter(Connector con, String name, String functionID) throws Exception
	{
		Parameter par = Parameter.getParameter(con, functionID, name);
		if (par != null) return null;
		
		String sql = "INSERT INTO kereta_parameter (name, function_id) VALUES (?, ?)";
		PreparedStatement prep;
		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, name);
		prep.setString(2, functionID);
		prep.execute();

		return Parameter.getParameter(con, functionID, name);
	}
	
	public boolean updateParameter(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_parameter SET data_type=?, default_value=?, author=?, description=? WHERE function_id=? and name=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, this.dataType);
		prep.setString(2, this.defaultValue);
		prep.setString(3, this.author);
		prep.setString(4, this.description);
		prep.setString(5, this.functionId);
		prep.setString(6, this.name);
		prep.execute();

		return true;
	}
	
	public static Parameter getParameter(Connector con, String functionID, String name)
	{
		Parameter result = null;
		String sql = "SELECT * FROM kereta_parameter WHERE name=? AND function_id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, name);
			prep.setString(2, functionID);
			ResultSet set = prep.executeQuery();
			if (set.next()) {
				result = parseResultSet(set);
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	public boolean delete(Connector con)
	{
		String sql = "DELETE FROM kereta_parameter WHERE name=? AND function_id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.getName());
			prep.setString(2, this.getFunctionId());
			prep.execute();
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	public static boolean deleteFunctionParameters(Connector con, Function fct)
	{
		if (fct == null) return false;
		String sql = "DELETE FROM kereta_parameter WHERE function_id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, fct.getId());
			prep.execute();
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	public static List<String> getParameterNames(Connector con, String functionID)
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT name FROM kereta_parameter WHERE function_id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, functionID);
			ResultSet set = prep.executeQuery();
			while (set.next()) {
				result.add(set.getString("name"));
			}
		} catch (Exception e) {
			
		}
		return result;
	}

	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(Parameter.class).createMarshaller();
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
	public final String getFunctionId() {
		return functionId;
	}
	public final void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public final String getDataType() {
		return dataType;
	}
	public final void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public final String getDefaultValue() {
		return defaultValue;
	}
	public final void setDefaultValue(String default_value) {
		this.defaultValue = default_value;
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
	public final String getDescription() {
		return description;
	}
	public final void setDescription(String description) {
		this.description = description;
	}
}
