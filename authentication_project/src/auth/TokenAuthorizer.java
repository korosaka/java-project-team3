package auth;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import db.DBConnection;
import exceptions.AuthorizationException;

public class TokenAuthorizer extends Authorizer {

	private Token token;
	
	private final static int EXPIRATION_DURATION = 60;
	
	public TokenAuthorizer(Token token) {
		super();
		this.token = token;
	}

	@Override
	public boolean authorize() throws AuthorizationException {
		// TODO check if token is valid, 1: time, 2: user exists
		System.out.println("token?::::" + token);

		if (token == null) throw new AuthorizationException();
		
		try {
			Connection conn = DBConnection.getConnection();
			String tokenQuery = "SELECT * FROM Token WHERE id = ?";
			PreparedStatement preStmt = conn.prepareStatement(tokenQuery);
			preStmt.setString(1, token.getEncodedString());
			ResultSet rs = preStmt.executeQuery();
			rs.next();
			Time createdAt = rs.getTime("created_at");
			System.out.println("createdAt:" + createdAt.toLocalTime());
			System.out.println("local time:" + LocalDateTime.now());
			//TODO: expiration check

			String[] decodedStrArr = token.getDecodedString().split(" ");
			String userName = decodedStrArr[0];
			System.out.println(decodedStrArr[1]); 
			Duration p = Duration.between(createdAt.toLocalTime(), LocalDateTime.now());
			if ((p.getSeconds()-28800) > EXPIRATION_DURATION) {
				System.out.println("Session Expired");
				token.remove();
				throw new AuthorizationException();
			}
			System.out.println("p seconds: " + (p.getSeconds() - 28800));
			String userQuery = "SELECT * FROM User WHERE name = ?";
			preStmt = conn.prepareStatement(userQuery);
			preStmt.setString(1, userName);
			rs = preStmt.executeQuery();
			rs.next();
			int id = rs.getInt("id");
			conn.close();
			if (id <= 0) {
				token.remove();
				throw new AuthorizationException();
			}
			return id > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			throw new AuthorizationException();
		}
	}
	
}
