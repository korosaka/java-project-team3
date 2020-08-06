package auth;


import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import db.DBConnection;
import model.User;

public class Token {

	private String jwtToken;
	public static final String SECRET = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

	public String getJwtToken() {
		return jwtToken;
	}

	public Token(User user) throws SQLException {
		super();
		setToken(createToken(user.getName()));
	}
	
	private String createToken(String username) {
		Instant now = Instant.now();

		Key hmacKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECRET), 
		                            SignatureAlgorithm.HS256.getJcaName());
		String jwtToken = Jwts.builder()
				.claim("name", username)
				.setSubject(username)
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
				.signWith(SignatureAlgorithm.HS256, hmacKey)
				.compact();
		return jwtToken;
	}
	
	public Claims parseJwt() throws SignatureException {
		Key hmacKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECRET), 
                SignatureAlgorithm.HS256.getJcaName());
		Claims claims = Jwts.parser()
					.setSigningKey(hmacKey)
					.parseClaimsJws(jwtToken)
					.getBody();
		return claims;
	}
	
	private void setToken(String token) throws SQLException {
		String insertQuery = "INSERT INTO Token (id) VALUES (?)";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(insertQuery);
		preStmt.setString(1, token);
		preStmt.executeUpdate();
		conn.close();
		jwtToken = token;
	}
	
	public void remove() throws SQLException {
		String deleteQuery = "DELETE FROM Token WHERE id = ?";
		Connection conn = DBConnection.getConnection();
		PreparedStatement preStmt = conn.prepareStatement(deleteQuery);
		preStmt.setString(1, this.jwtToken);
		preStmt.executeUpdate();
		conn.close();
	}
}
