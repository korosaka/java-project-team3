package auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;

import db.DBConnection;
import exceptions.AuthorizationException;

public class TokenAuthorizer extends Authorizer {

	private Token token;
	
	private final static int EXPIRATION_DURATION = 30;
	
	public TokenAuthorizer(Token token) {
		super();
		this.token = token;
	}

	@Override
	public boolean authorize() throws AuthorizationException {

		if (token == null) throw new AuthorizationException();
		
		try {
			// token existence check
			Connection conn = DBConnection.getConnection();
			String tokenQuery = "SELECT * FROM Token WHERE id = ?";
			PreparedStatement preStmt = conn.prepareStatement(tokenQuery);
			preStmt.setString(1, token.getJwtToken());
			ResultSet rs = preStmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Token not exists");
				conn.close();
				token.remove();
				throw new AuthorizationException();
			}
			// expiration check
			Time createdAt = rs.getTime("created_at");
			Duration p = Duration.between(createdAt.toLocalTime(), LocalDateTime.now());
			System.out.println("until expiration: " + (p.getSeconds()-28800));
			if ((p.getSeconds()-28800) > EXPIRATION_DURATION) {
				System.out.println("Session Expired");
				conn.close();
				token.remove();
				throw new AuthorizationException();
			}
			// user existence check
			String userName = token.parseJwt().getSubject();
			String userQuery = "SELECT * FROM User WHERE name = ?";
			preStmt = conn.prepareStatement(userQuery);
			preStmt.setString(1, userName);
			rs = preStmt.executeQuery();
			if (!rs.next()) {
				System.out.println("user invalid");
				conn.close();
				token.remove();
				throw new AuthorizationException();
			}
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			throw new AuthorizationException();
		}
	}
	
}
