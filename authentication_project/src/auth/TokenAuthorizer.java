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

		if (token == null) throw new AuthorizationException();
		
		try {
			Connection conn = DBConnection.getConnection();
			String tokenQuery = "SELECT * FROM Token WHERE id = ?";
			PreparedStatement preStmt = conn.prepareStatement(tokenQuery);
			preStmt.setString(1, token.getEncodedString());
			ResultSet rs = preStmt.executeQuery();
			rs.next();
			Time createdAt = rs.getTime("created_at");

			String[] decodedStrArr = token.getDecodedString().split(" ");
			String userName = decodedStrArr[0];
			Duration p = Duration.between(createdAt.toLocalTime(), LocalDateTime.now());
			if ((p.getSeconds()-28800) > EXPIRATION_DURATION) {
				System.out.println("Session Expired");
				token.remove();
				throw new AuthorizationException();
			}
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
