package db;

import java.sql.*;

public class DBConnection {

	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(DBConstants.CONN_STRING, DBConstants.USERNAME, DBConstants.PASSWORD);
		return conn;
	}
}
