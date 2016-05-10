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
@XmlType(propOrder={"name", "author", "create"})
public class Type {

	private String name;
	private String author;
	private Date create;
	
	private static Type parseResultSet(ResultSet set)
	{
		Type result = null;
		if (set != null) {
			try {
				result = new Type();
				result.setName(set.getString("name"));
				result.setAuthor(set.getString("author"));
				result.setCreate(set.getTimestamp("create"));
			} catch (Exception e) {
				result = null;
			}
		}
		return result;
	}
	
	public static Type getType(Connector con, String name, String table)
	{
		Type result = null;
		String sql = "SELECT * FROM " + table + " WHERE name=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, name);
			ResultSet set = prep.executeQuery();
			if (set.next()) {
				result = parseResultSet(set);
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	public static Type createType(Connector con, String name, String table)
	{

		String sql = "INSERT INTO " + table + " (name, author) VALUES (?, ?)";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, name);
			prep.setString(2, "user");
			prep.execute();
			return getType(con, name, table);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public boolean deleteType(Connector con, String table)
	{
		if (this.author.equals("Kereta")) return false;
		
		String sql = "DELETE FROM " + table + " WHERE name=?";
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, this.name);
			prep.execute();

		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean hasType(Connector con, String table, String name) throws Exception
	{
		String sql = "SELECT COUNT(*) FROM " + table + " WHERE name=?";
		PreparedStatement prep;
		int count = 0;

			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, name);
			ResultSet set = prep.executeQuery();
			if (set.next()) {
				count = set.getInt(1);
			}


		return (count > 0);
	}
	
	public static List<String> getTypeNames(Connector con, String table)
	{
		List<String> result = new ArrayList<String>();
		String sql = "SELECT name FROM " + table;
		PreparedStatement prep;
		try {
			prep = con.getConnection().prepareStatement(sql);
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
		Marshaller marshaller = JAXBContext.newInstance(Type.class).createMarshaller();
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
