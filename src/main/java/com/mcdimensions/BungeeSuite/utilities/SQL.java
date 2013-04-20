package com.mcdimensions.BungeeSuite.utilities;

import java.sql.*;

import net.md_5.bungee.api.ProxyServer;


public class SQL extends SQLOperations {

	private String host, database, username, password, port;
	private Connection connection;

	public SQL(String host,String port, String database, String username, String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	/**
	 * Reopens the SQL connection if it is closed. This is invoked upon every
	 * query.
	 */
	public void refreshConnection() {
		if (connection == null) {
			initialise();
		}
	}

	/**
	 * Manually close the connection.
	 */
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialise a new connection. This will automatically create the database
	 * file if you are using SQLite and it doesn't already exist.
	 * 
	 * @return
	 */
	public boolean initialise() {

			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host +":" + port + "/"+database, username, password);
				return true;
			} catch (ClassNotFoundException ex) {
				 System.out.println("SQL is unable to conect");
			} catch (SQLException ex) {
				 System.out.println("SQL is unable to conect BungeeSuite will now shutdown BungeeCord");
				 ProxyServer.getInstance().stop();
			}
		
		return false;
	}

	/**
	 * Any query which does not return a ResultSet object. Such as : INSERT,
	 * UPDATE, CREATE TABLE...
	 * 
	 * @param query
	 */
	public void standardQuery(String query) throws SQLException {
		this.refreshConnection();
		super.standardQuery(query, this.connection);
	}

	/**
	 * Check whether a field/entry exists in a database.
	 * @param query
	 * @return Whether or not a result has been found in the query.
	 * @throws SQLException
	 */
	public boolean existenceQuery(String query) throws SQLException {
		this.refreshConnection();
		return super.sqlQuery(query, this.connection).next();
	}

	/**
	 * Any query which returns a ResultSet object. Such as : SELECT Remember to
	 * close the ResultSet object after you are done with it to free up
	 * resources immediately. ----- ResultSet set =
	 * sqlQuery("SELECT * FROM sometable;"); set.doSomething(); set.close();
	 * -----
	 * 
	 * @param query
	 * @return ResultSet
	 */
	public ResultSet sqlQuery(String query) throws SQLException {
		this.refreshConnection();
		return super.sqlQuery(query, this.connection);
	}

	/**
	 * Check whether the table name exists.
	 * 
	 * @param table
	 * @return
	 */
	public boolean doesTableExist(String table) throws SQLException {
		this.refreshConnection();
		return super.checkTable(table, this.connection);
	}

}