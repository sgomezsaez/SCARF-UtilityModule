package de.mackfn.kereta.tools;

import java.sql.DriverManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.sql.Connection;

public class Connector {
	
	private String keretaRoot;
	private String nefologRoot;
	private Connection con;
	private String error;
	private String settingsPath;
	
	public Connector(String settings)
	{
		this.settingsPath = settings;

		this.con = null;
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			File file = new File(settingsPath);
			if (file.exists())
			{		
				Document doc = docBuilder.parse(file);
				Element root = doc.getDocumentElement();
				String database = root.getElementsByTagName("url").item(0).getTextContent();
				String user = root.getElementsByTagName("user").item(0).getTextContent();
				String pw = root.getElementsByTagName("password").item(0).getTextContent();
				this.keretaRoot =  root.getElementsByTagName("KeretaRoot").item(0).getTextContent();
				this.nefologRoot =  root.getElementsByTagName("NefologRoot").item(0).getTextContent();
				Class.forName("com.mysql.jdbc.Driver");
				this.con = DriverManager
				.getConnection(database, user, pw);
			}
		} catch (Exception e) {
			this.error = e.getMessage();
		}
	}
	
	public String getKeretaRoot()
	{
		return this.keretaRoot;
	}
	
	public String getNefologRoot()
	{
		return this.nefologRoot;
	}
	
	public Connection getConnection() throws Exception
	{
		if (this.isConnected()) return con;
		else throw new Exception("not connected");
	}
	
	public boolean isConnected()
	{
		return (con != null);
	}
	
	public String getErrorMessage()
	{
		return error;
	}
	
}
