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

@XmlRootElement(name = "function")
@XmlType(propOrder={"id", "alias", "formula", "description", "functionType", "author", "create"})
public class Function {

	private String id;
	private String formula;
	private String description;
	private String functionType;
	private String author;
	private String alias;
	private Date create;
	
	public Function()
	{
		this.id = "";
		this.formula = "";
		this.description = "";
		this.functionType = "";
		this.author = "";
		this.create = null;
		this.alias = "";
	}
	
	private static Function parseResultSet(ResultSet set)
	{
		Function result = null;
		if (set != null) {
			try {
				result = new Function();
				result.setId(set.getString("id"));
				result.setFormula(set.getString("function"));
				result.setDescription(set.getString("description"));
				result.setFunctionType(set.getString("function_type"));
				result.setAuthor(set.getString("author"));
				result.setCreate(set.getTimestamp("create"));
				result.setAlias(set.getString("alias"));
			} catch (Exception e) {
				result = null;
			}
		}
		return result;
	}
	
	public static Function createFunction(Connector con) throws Exception
	{
		java.util.UUID uuid = java.util.UUID.randomUUID();
		String sql = "INSERT INTO kereta_function (id) VALUES (?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, uuid.toString());
		prep.execute();

		return Function.getFunction(con, uuid.toString());
	}
	
	public boolean deleteFunction(Connector con) throws Exception
	{
		String sql = "DELETE FROM kereta_function WHERE id=?";
		PreparedStatement prep;
		try
		{
		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, this.getId());
		prep.execute();
		}
		catch (Exception e)
		{
			return false;		
		}
		return true;
	}
	
	public static Function createFunction(Connector con, String function, String description, String function_type, String author, String alias) throws Exception
	{
		java.util.UUID uuid = java.util.UUID.randomUUID();
		String sql = "INSERT INTO kereta_function (id, function, description, function_type, author, alias) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, uuid.toString());
		prep.setString(2, function);
		prep.setString(3, description);
		prep.setString(4, function_type);
		prep.setString(5, author);
		prep.setString(6, alias);
		prep.execute();

		return Function.getFunction(con, uuid.toString());
	}
	
	public boolean updateFunction(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_function SET function=?, description=?, function_type=?, author=?, alias=? WHERE id=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, this.getFormula());
		prep.setString(2, this.getDescription());
		prep.setString(3, this.getFunctionType());
		prep.setString(4, this.getAuthor());
		prep.setString(5, this.getAlias());
		prep.setString(6, this.getId());
		prep.execute();

		return true;
		
	}
	
	public static Function getFunction(Connector con, String id)
	{
		Function result = null;
		String sql= "SELECT * FROM kereta_function WHERE id=? OR alias=?";
		//if (id.length() == 36) sql = "SELECT * FROM kereta_function WHERE id=?";
		//else sql = "SELECT * FROM kereta_function WHERE alias=?";
		
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, id);
			prep.setString(2, id);
			ResultSet set = prep.executeQuery();
			if (set.next()) {
				result = parseResultSet(set);
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	public static List<String> getFunctionIDs(Connector con, String type)
	{
		List<String> result = new ArrayList<String>();
		String sql;
		if (type.equals("")) sql = "SELECT id FROM kereta_function";
		else sql =  "SELECT id FROM kereta_function WHERE function_type=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			if (!type.equals("")) prep.setString(1, type);
			ResultSet set = prep.executeQuery();
			while (set.next()) {
				result.add(set.getString("id"));
			}
		} catch (Exception e) {
			
		}
		return result;
	}

	public static List<String> getUtilityFunctionFunctionIDs(Connector con, String utilityFunctionId, String functionType) throws Exception
	{
		List<String> result = new ArrayList<String>();
		
		String sql;
		if (functionType.equals("")) sql = "SELECT fct.id FROM (kereta_function AS fct INNER JOIN kereta_subfunction AS sf ON fct.id=sf.function_id) WHERE sf.utility_function_id=?";
		else sql = "SELECT fct.id FROM (kereta_function AS fct INNER JOIN kereta_subfunction AS sf ON fct.id=sf.function_id) WHERE sf.utility_function_id=? and fct.function_type=?";
		PreparedStatement prep;
		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, utilityFunctionId);
		if (!functionType.equals("")) prep.setString(2, functionType);
		ResultSet set = prep.executeQuery();
		while (set.next()) {
			result.add(set.getString("id"));
		}
		return result;
	}
	
	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(Function.class).createMarshaller();
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

	public final String getFormula() {
		return formula;
	}

	public final void setFormula(String formula) {
		this.formula = formula;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getFunctionType() {
		return functionType;
	}

	public final void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public final String getAuthor() {
		return author;
	}

	public final void setAuthor(String author) {
		this.author = author;
	}

	public final String getAlias() {
		return alias;
	}

	public final void setAlias(String alias) {
		this.alias = alias;
	}
	
	public final Date getCreate() {
		return create;
	}

	public final void setCreate(Date create) {
		this.create = create;
	}
}
