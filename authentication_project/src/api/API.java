package api;

import java.sql.SQLException;

import auth.Token;
import auth.TokenAuthorizer;
import exceptions.AuthorizationException;
import exceptions.PasswordInvalidException;
import exceptions.UserNameInvalidException;
import model.Role;
import model.User;
import utils.UserValidation;

public class API {

	public static Token signUp(String username, String password, Role role) throws SQLException, PasswordInvalidException, UserNameInvalidException {
		User user = new User(username, password, role);
		if (!UserValidation.usernameValidation(username)) {
			throw new UserNameInvalidException();
		}
		if (!UserValidation.passwordValidation(password)) {
			throw new PasswordInvalidException();
		}
		user.create();
		return new Token(user);
	}
	
	public static Token signIn(String username, String password) throws SQLException, UserNameInvalidException, PasswordInvalidException {
		User user = new User(username, password);
		if (!UserValidation.usernameValidation(username)) {
			throw new UserNameInvalidException();
		}
		if (!UserValidation.passwordValidation(password)) {
			throw new PasswordInvalidException();
		}
		user = user.getUserFromDB();
		return new Token(user);
	}
	
	public static void signOut(Token token) {
		System.out.println("Token removed");
	}
	
	public static void doSomething(Token token) {
		// authorizer
		System.out.println("isTokenValid: " + isTokenValid(token));
		isTokenValid(token);
	}
	
	private static boolean isTokenValid(Token token) {
		try {
			return new TokenAuthorizer(token).authorize();
		} catch (AuthorizationException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
}
