package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;

public class User {

	private String name;
	private String password;
	private Role role;
	
	public User(String name, String password, Role role) {
		super();
		setName(name);
		setPassword(password);
		setRole(role);
	}
	
	public User(String name, String password) {
		super();
		setName(name);
		setPassword(password);
	}
	
	public void create() throws SQLException {
		String insertQuery = "INSERT INTO User (name, password, isAdmin) VALUES (?, ?, ?)";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(insertQuery);
		preStmt.setString(1, getName());
		preStmt.setString(2, getPassword());
		preStmt.setBoolean(3, getRole() == Role.ADMIN);
		preStmt.executeUpdate();
		conn.close();
	}
	
	public User getUserFromDB() throws SQLException {
		String query = "SELECT * FROM User WHERE name = ? AND password = ?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(query);
		preStmt.setString(1, getName());
		preStmt.setString(2, getPassword());
		ResultSet rs = preStmt.executeQuery();
		rs.next();
		setRole(rs.getBoolean(4) == true ? Role.ADMIN : Role.NORMAL);
		conn.close();
		return this;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(Role role) {
		this.role = role;
	}
		
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Role getRole() {
		return role;
	}
}
