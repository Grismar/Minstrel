package grismar.argus;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ArgusData {
	public Statement statement;
	private Connection connection = null;
	
	public ArgusData() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		 
	    try
	    {
	    	// create a database connection
	    	connection = DriverManager.getConnection("jdbc:sqlite:argus.db");
	    	// create a statement on the open connection
	    	statement = connection.createStatement();
	    	statement.setQueryTimeout(30);
		}
	    catch(SQLException e)
	    {
	    	System.err.println(e.getMessage());
	    }
	}
	
	public Array getFirstColumn(String sql) {
		try {
			ResultSet rs = statement.executeQuery(sql);
			return rs.getArray(0);
		} catch (SQLException e) {
			return null;
		}
	}
}
