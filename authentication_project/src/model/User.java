package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import com.mysql.cj.xdevapi.Statement;

import db.DBConnection;
import utils.PasswordUtil;

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
	
	public User(String name) {
		super();
		setName(name);
	}
	
	public void create() throws SQLException {
		String insertQuery = "INSERT INTO User (name, password, isAdmin) VALUES (?, ?, ?)";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(insertQuery);
		preStmt.setString(1, getName());
		preStmt.setString(2, PasswordUtil.hashPassword(getPassword()));
		preStmt.setBoolean(3, getRole() == Role.ADMIN);
		preStmt.executeUpdate();
		conn.close();
	}
	
	public void remove() throws SQLException {
		String deleteQuery = "DELETE FROM User WHERE name = ?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(deleteQuery);
		preStmt.setString(1, getName());
		preStmt.executeUpdate();
		conn.close();
	}
	
	public User getUserFromDB() throws SQLException {
		String query = "SELECT * FROM User WHERE name = ?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(query);
		preStmt.setString(1, getName());
		ResultSet rs = preStmt.executeQuery();
		rs.next();
		conn.close();
		return this;
	}
	
//	public ArrayList<User> getAllUserFromDB() throws SQLException {
//		String query = "SELECT * FROM User WHERE name = ?";
//		Connection conn = DBConnection.getConnection();
//		PreparedStatement preStmt = conn.prepareStatement(query);
//		preStmt.setString(1, getName());
//		ResultSet rs = preStmt.executeQuery();
//		rs.next();
//		conn.close();
//		return this;
//	}
	
	@Override
	public String toString() {
		return "User [name=" + name + ", role=" + role + "]";
	}

	public boolean checkIfUserValid() throws SQLException {
		String query = "SELECT * FROM User WHERE name = ?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(query);
		preStmt.setString(1, getName());
		ResultSet rs = preStmt.executeQuery();
		if (!rs.next()) {
			conn.close();
			return false;
		};
		setRole(rs.getBoolean(4) == true ? Role.ADMIN : Role.NORMAL);
		String passwordInDB = rs.getString("password");
		conn.close();
		return PasswordUtil.compare(getPassword().toCharArray(), passwordInDB.toCharArray());
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
