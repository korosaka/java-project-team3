package auth;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import exceptions.AuthorizationException;

public class TokenAuthorizer extends Authorizer {

	private Token token;
	
	public TokenAuthorizer(Token token) {
		super();
		this.token = token;
	}

	@Override
	public boolean authorize() throws AuthorizationException {
		// TODO check if token is valid, 1: time, 2: user exists
		if (token == null) throw new AuthorizationException();
		try {
			Connection conn = DBConnection.getConnection();
			String tokenQuery = "SELECT * FROM Token WHERE id = ?";
			PreparedStatement preStmt = conn.prepareStatement(tokenQuery);
			preStmt.setString(1, token.getEncodedString());
			ResultSet rs = preStmt.executeQuery();
			rs.next();
			Date createdAt = rs.getDate("created_at");
			System.out.println("createdAt:" + createdAt);

			String userName = token.getDecodedString();
			String userQuery = "SELECT * FROM User WHERE name = ?";
			preStmt = conn.prepareStatement(userQuery);
			preStmt.setString(1, userName);
			rs = preStmt.executeQuery();
			rs.next();
			int id = rs.getInt("id");
			return id > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new AuthorizationException();
		}
	}
	
}
