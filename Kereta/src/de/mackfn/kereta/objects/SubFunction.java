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
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;

import de.mackfn.kereta.tools.Connector;

@XmlRootElement(name = "subFunction")
public class SubFunction {

	private int number;
	private String utilityFunctionId;
	private String functionId;
	private String author;
	private Date create;
	
	public SubFunction()
	{
		this.number = 1;
		this.utilityFunctionId = "";
		this.functionId = "";
		this.author = "";
		this.create = null;
	}

	private static SubFunction parseResultSet(ResultSet set) throws SQLException
	{
		SubFunction result = null;
		if (set != null) {
			result = new SubFunction();
			result.setNumber(set.getInt("number"));
			result.setUtilityFunctionId(set.getString("utility_function_id"));
			result.setFunctionId(set.getString("function_id"));
			result.setAuthor(set.getString("author"));
			result.setCreate(set.getTimestamp("create"));
		}
		return result;
	}
	
	public static SubFunction createSubFunction(Connector con, String utilityFunctionId, int number, String functionId, String author) throws Exception
	{
		SubFunction sf = SubFunction.getSubFunction(con, utilityFunctionId, number);
		if (sf != null) return null;
		
		String sql = "INSERT INTO kereta_subfunction (number, utility_function_id, function_id, author) VALUES (?, ?, ?, ?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setInt(1, number);
		prep.setString(2, utilityFunctionId);
		prep.setString(3, functionId);
		prep.setString(4, author);
		prep.execute();

		return SubFunction.getSubFunction(con, utilityFunctionId, number);
	}
	
	public static SubFunction createSubFunction(Connector con, String utilityFunctionId, int number) throws Exception
	{
		SubFunction sf = SubFunction.getSubFunction(con, utilityFunctionId, number);
		if (sf != null) return null;
		
		String sql = "INSERT INTO kereta_subfunction (number, utility_function_id) VALUES (?, ?)";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setInt(1, number);
		prep.setString(2, utilityFunctionId);
		prep.execute();

		return SubFunction.getSubFunction(con, utilityFunctionId, number);
	}
	
	public boolean persist(Connector con) throws Exception
	{
		String sql = "UPDATE kereta_subfunction SET function_id=?, author=? WHERE number=? AND utility_function_id=?";
		PreparedStatement prep;

		prep = con.getConnection().prepareStatement(sql);
		prep.setString(1, this.getFunctionId());
		prep.setString(2, this.getAuthor());
		prep.setInt(3, this.getNumber());
		prep.setString(4, this.getUtilityFunctionId());
		prep.execute();

		return true;
		
	}
	
	public boolean delete(Connector con)
	{
		String sql = "DELETE FROM kereta_subfunction WHERE number=? AND utility_function_id=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setInt(1, this.getNumber());
			prep.setString(2, this.getUtilityFunctionId());
			prep.execute();
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static SubFunction getSubFunction(Connector con, String utilityFunctionId, int number) throws Exception
	{
		SubFunction result = null;
		String sql = "SELECT * FROM kereta_subfunction WHERE number=? AND utility_function_id=?";
		PreparedStatement prep;
		//try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setInt(1, number);
			prep.setString(2, utilityFunctionId);
			ResultSet set = prep.executeQuery();
			if (set.next()) {
				result = parseResultSet(set);
			}
		/*} catch (Exception e) {
		}*/
		return result;
	}
	
	public static List<String> getSubFunctionIDs(Connector con) throws Exception
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT id FROM kereta_subfunction";
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
	
	public static List<Integer> getUtilityFunctionSubFunctionNumbers(Connector con, String utilityFunctionID) throws Exception
	{
		List<Integer> result = new ArrayList<Integer>();
		String sql = "SELECT number FROM kereta_subfunction WHERE utility_function_id=?";
		PreparedStatement prep;
		//try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, utilityFunctionID);
			ResultSet set = prep.executeQuery();
			while (set.next()) {
				result.add(set.getInt("number"));
			}
		/*} catch (Exception e) {
			
		}*/
		return result;
	}
	
	public Document toDOM() throws Exception
	{
		Marshaller marshaller = JAXBContext.newInstance(SubFunction.class).createMarshaller();
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

	public final String getUtilityFunctionId() {
		return utilityFunctionId;
	}

	public final void setUtilityFunctionId(String utilityFunctionID) {
		this.utilityFunctionId = utilityFunctionID;
	}

	public final String getFunctionId() {
		return functionId;
	}

	public final void setFunctionId(String functionID) {
		this.functionId = functionID;
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

	
}
