//////////////////////////////////////////////////////
//
//	JDBCpostgreSQLClient class
//	Last edited 09/26/2020
//
//////////////////////////////////////////////////////

import java.sql.*;

/**
 * JDBCpostgreSQLClient establishes a connection with a database. Commands can be given to
 * the database and results can be retrieved.
 */
public class JDBCpostgreSQLClient {

	public Connection connection;

	/**
	 * The JDBCpostgreSQLClient constructor tries to connect to a psql database
	 * given the database URL, client's username, and client's password.
	 * 
	 * @param databaseURL The URL of the database.
	 * @param username The username of the client connecting to the database.
	 * @param password The password of the client connecting to the database.
	 */
	public JDBCpostgreSQLClient(String databaseURL, String username, String password)
	{
		connection = null;
		try
		{
			//Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(databaseURL, username, password);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	/**
	 * queryFor gives a command to the database and returns the results.
	 * 
	 * @param command The statement used to query in SQL.
	 * @return A ResultSet object with data from the query that can be iterated through.
	 */
	public ResultSet queryFor(String sqlCommand)
	{
		ResultSet result;

		try
		{
			Statement stmt = conn.createStatement();
			String sqlStatement = command;
			result = stmt.executeQuery(sqlStatement);
		}
		catch (Exception e)
		{
			System.out.println("Error accessing the database.");
		}

		return result;
	}

	/**
	 * Close the current connection to a database.
	 */
	public void close()
	{
		try 
		{
			connection.close();
			System.out.println("Connection Closed.");
		} 
		catch(Exception e) 
		{
			System.out.println("Connection NOT Closed.");
		}
	}
}
