package auth;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import db.DBConnection;
import model.User;

public class Token {

	private String encodedString;

	public String getEncodedString() {
		return encodedString;
	}

	public Token(User user) throws SQLException {
		super();
		setToken(user.getName());
	}
	
	private void setToken(String token) throws SQLException {
		this.encodedString = getEncodedToken(token);
		String insertQuery = "INSERT INTO Token (id) VALUES (?)";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(insertQuery);
		preStmt.setString(1, this.encodedString);
		preStmt.executeUpdate();
	}

	private String getEncodedToken(String str) {
		return new String(Base64.getEncoder().encode(str.getBytes()));
	}
	
	public String getDecodedString() {
		return new String(Base64.getDecoder().decode(encodedString.getBytes()));
	}
}
