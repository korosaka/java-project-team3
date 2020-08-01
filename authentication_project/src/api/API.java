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
	
	public static void signOut(Token token) throws SQLException {
		System.out.println("Token removed");
		token.remove();
	}
	
	public static void doSomething(Token token) throws AuthorizationException {
		// authorizer
		System.out.println("isTokenValid: " + isTokenValid(token));
	}
	
	private static boolean isTokenValid(Token token) throws AuthorizationException {
		return new TokenAuthorizer(token).authorize();
	}
}
