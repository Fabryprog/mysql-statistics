/**
 * 
 */
package it.fabryprog.mysql.statistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fabryprog <fabryprog@gmail.com>
 *
 */
public class MySqlConnector  {

	public static MySqlConnector instance;
	private MySqlConnector() {}
	
	private Connection conn;
	
	public static MySqlConnector getInstance() {
		if(instance == null) {
			instance = new MySqlConnector();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public void connect() throws Exception {
		Class.forName (StandAlone.prop.getProperty("driver")).newInstance ();
		this.conn = DriverManager.getConnection (StandAlone.prop.getProperty("url"), StandAlone.prop.getProperty("username"), StandAlone.prop.getProperty("password"));
	}

	public void enableLog() throws Exception {
		PreparedStatement stmt = null;
		try {
			String query = "SET GLOBAL log_output = 'TABLE'";
			stmt = conn.prepareStatement(query);
				
			stmt.executeQuery();
			stmt.close();
			
			query = "SET GLOBAL general_log = 1";
			stmt = conn.prepareStatement(query);

			stmt.executeQuery();
		} finally {
			try {stmt.close();} catch(Exception e) {}
		}
	}

	public void disableLog() throws Exception {
		PreparedStatement stmt = null;
		try {
			String query = "SET GLOBAL general_log = 0";
			stmt = conn.prepareStatement(query);

			stmt.executeQuery();
		} finally {
			try {stmt.close();} catch(Exception e) {}
		}
	}

	public List<Map<String, String>> executeToken(String token, Properties prop, List params) throws Exception {
		List<Map<String, String>> result = new LinkedList();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = prop.getProperty(token);
			if(query.toLowerCase().startsWith("select")) {
				// TODO max result;
				stmt = ((Connection)conn).prepareStatement(query);
				
				int paramsCounter = StringUtils.countMatches(query, "?");
				
				for(int i = 1; i <= paramsCounter; i++) {
					stmt.setObject(i, params.get(i - 1));
				}
	
				rs = stmt.executeQuery();
				
				
				ResultSetMetaData metaData = rs.getMetaData();
				int c = metaData.getColumnCount(); //number of column
				
				//TODO check type field
				Map<String, String> map = null;
				
				while(rs.next()) {
					map = new HashMap<String, String>();
					for(int i = 1; i <= c; i++) {
						map.put(metaData.getColumnName(i), rs.getString(i));
					}
					
					result.add(map);
				}
			} else {
				throw new Exception("Quert not supported!");
			}
		} finally {
			try {rs.close();} catch(Exception e) {}
			try {stmt.close();} catch(Exception e) {}
		}

		return result;
	}

}
