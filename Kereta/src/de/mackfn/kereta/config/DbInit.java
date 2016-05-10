package de.mackfn.kereta.config;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


import de.mackfn.kereta.tools.Connector;

public class DbInit {

	
	public static boolean DropTable(Connector con, String table)
	{
		try 
		{
			String sql =
			"DROP TABLE IF EXISTS `" + table + "`;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static boolean Type(Connector con, String table)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `" + table + "` (" +
				"`name` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT ''," +
				"`author` varchar(128) DEFAULT NULL," +
				"`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
				"PRIMARY KEY (`name`)" +
			") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Application(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_application` (" +
					  "`id` varchar(36) NOT NULL DEFAULT ''," +
					  "`name` varchar(128) DEFAULT NULL," +
					  "`description` text," +
					  "`application_type` varchar(128) DEFAULT NULL," +
					  "`alias` varchar(128) DEFAULT NULL," +
					  "`author` varchar(128) DEFAULT NULL," +
					  "`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
					  "`tier` int(11) NOT NULL DEFAULT '0'," +
					  "PRIMARY KEY (`id`,`tier`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Distribution(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_distribution` (" +
				"`id` varchar(36) NOT NULL," +
				"`topology` text," +
				"`topology_language` varchar(128) DEFAULT NULL," +
				"`topology_language_version` varchar(128) DEFAULT NULL," +
				"`author` varchar(128) DEFAULT NULL," +
				"`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
				"`alias` varchar(128)  DEFAULT NULL," +
				"`namespace` varchar(128)  DEFAULT NULL," +
				"`application_id` varchar(36) DEFAULT NULL," +
  				"PRIMARY KEY (`id`)" +
  			") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Offering(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_offering` ("
				+ "`number` int(11) NOT NULL DEFAULT '0',"
				+ "`nefolog_configuration_id` int(11) DEFAULT NULL,"
				+ "`nefolog_offering_name` varchar(128) DEFAULT NULL,"
				+ "`nefolog_service_type` varchar(128) DEFAULT NULL,"
				+ " `author` varchar(128) DEFAULT NULL,"
				+ " `create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
				+ " `distribution_id` varchar(36) NOT NULL,"
				+ " `nefolog_provider` varchar(128) DEFAULT NULL,"
				+ "`nefolog_configuration` varchar(128) DEFAULT NULL,"
				+ "PRIMARY KEY (`number`,`distribution_id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Requirement(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_requirement` ("
			+ "`name` varchar(128) NOT NULL DEFAULT '',"
			+ "`value` varchar(128) DEFAULT NULL,"
			+ "`data_type` varchar(128) DEFAULT NULL,"
			+ "`author` varchar(128) DEFAULT NULL,"
			+ "`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "`requirement_type` varchar(128) DEFAULT NULL, "
			+ "`application_id` varchar(36) NOT NULL DEFAULT '',"
			+ "`application_tier` int(11) NOT NULL,"
			+ "`demand` varchar(1) DEFAULT '=',"
			+ "PRIMARY KEY (`name`,`application_id`,`application_tier`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Performance(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_performance` ("
			+ "`name` varchar(128) NOT NULL,"
			+ "`distribution_id` varchar(36) NOT NULL,"
			+ " `offering_number` int(11) NOT NULL,"
			+ "`value` varchar(128) DEFAULT NULL,"
			+ "`requirement_type` varchar(36) DEFAULT NULL,"
			+ "`data_type` varchar(128) DEFAULT NULL,"
			+ "`author` varchar(128) DEFAULT NULL,"
			+ "`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "`demand` varchar(1) DEFAULT NULL,"
			+ " PRIMARY KEY (`name`,`distribution_id`,`offering_number`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean UtilityFunction(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_utilityFunction` ("
			+ "`id` varchar(36) NOT NULL,"
			+ "`distribution_id` varchar(36) DEFAULT NULL,"
			+ "`description` varchar(512) DEFAULT NULL,"
			+ "`author` varchar(128) DEFAULT NULL,"
			+ "`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "`alias` varchar(128) DEFAULT NULL,"
			+ "PRIMARY KEY (`id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Subfunction(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_subfunction` ("
			+ "`number` int(11) NOT NULL,"
			+ "`utility_function_id` varchar(36) NOT NULL,"
			+ "`function_id` varchar(36) DEFAULT NULL,"
			+ "`author` varchar(128) DEFAULT NULL,"
			+ "`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "PRIMARY KEY (`number`,`utility_function_id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Function(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_function` ("
			+ "`id` varchar(36) NOT NULL,"
			+ "`function` text,"
			+ "`description` text,"
			+ "`function_type` varchar(128) DEFAULT NULL,"
			+ "`author` varchar(128) DEFAULT NULL,"
			+ "`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "`alias` varchar(128) DEFAULT NULL,"
			+ "PRIMARY KEY (`id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean OfferingTier(Connector con)
	{
		try
		{
			String sql = 
			"CREATE TABLE `kereta_offeringTier` ("
			+ "`application_id` varchar(36) NOT NULL,"
			+ "`application_tier` int(11) NOT NULL,"
			+ "`offering_number` int(11) NOT NULL,"
			+ "`distribution_id` varchar(36) NOT NULL,"
			+ "PRIMARY KEY (`application_id`,`application_tier`,`offering_number`,`distribution_id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean Parameter(Connector con)  
	{
		try 
		{	
			String sql =
			"CREATE TABLE `kereta_parameter` ("
			+ "`name` varchar(128) NOT NULL,"
			+ "`function_id` varchar(36) NOT NULL,"
			+ "`data_type` varchar(128) DEFAULT NULL,"
			+ "`default_value` varchar(128) DEFAULT NULL,"
			+ "`author` varchar(128) DEFAULT NULL,"
			+ "`create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
			+ "`description` varchar(512) DEFAULT NULL,"
			+ "PRIMARY KEY (`name`,`function_id`)"
			+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);		
			prep.execute();
			
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean MandatoryResources(Connector con)
	{
		try
		{
			String sql = "INSERT INTO kereta_requirementType (name, author) VALUES (?, ?)";			
			PreparedStatement prep;
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, "functional");
			prep.setString(2, "Kereta");
			prep.execute();
			prep.setString(1, "non-functional");
			prep.execute();
			
			sql = "INSERT INTO kereta_functionType (name, author) VALUES (?, ?)";			
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(1, "misc");
			prep.setString(2, "Kereta");
			prep.execute();
			prep.setString(1, "revenue");
			prep.execute();
			prep.setString(1, "cost");
			prep.execute();

			sql = "INSERT INTO kereta_dataType (name, author) VALUES (?, ?)";			
			prep = con.getConnection().prepareStatement(sql);
			prep.setString(2, "Kereta");
			prep.setString(1, "number");
			prep.execute();
			prep.setString(1, "string");
			prep.execute();
			prep.setString(1, "array of numbers");
			prep.execute();
			prep.setString(1, "array of strings");
			prep.execute();
			prep.setString(1, "array of arrays");
			prep.execute();
			
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public static boolean InitialResources(Connector con, Document initialXml)
	{
		try
		{
			Node fct = initialXml.getElementsByTagName("function").item(0);
			Node prm = initialXml.getElementsByTagName("parameter").item(0);
			Node app = initialXml.getElementsByTagName("application").item(0);
			Node dtr = initialXml.getElementsByTagName("distribution").item(0);
			Node aTp = initialXml.getElementsByTagName("applicationType").item(0);
			Node off = initialXml.getElementsByTagName("offering").item(0);
			Node ofT = initialXml.getElementsByTagName("offeringTier").item(0);
			Node ufc = initialXml.getElementsByTagName("utilityFunction").item(0);
			Node sfc = initialXml.getElementsByTagName("subFunction").item(0);
			Node req = initialXml.getElementsByTagName("requirement").item(0);
			Node prf = initialXml.getElementsByTagName("performance").item(0);
			Statement stat = con.getConnection().createStatement();
			stat.execute(fct.getTextContent());
			stat.execute(prm.getTextContent());
			stat.execute(app.getTextContent());
			stat.execute(dtr.getTextContent());
			stat.execute(aTp.getTextContent());
			stat.execute(off.getTextContent());
			stat.execute(ofT.getTextContent());
			stat.execute(ufc.getTextContent());
			stat.execute(sfc.getTextContent());
			stat.execute(req.getTextContent());
			stat.execute(prf.getTextContent());
			return true;
		}
		catch (Exception e) { return false; }
	}
		
	
}
