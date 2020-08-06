package api;

import java.sql.SQLException;

import auth.Token;
import auth.TokenAuthorizer;
import exceptions.AuthorizationException;
import exceptions.PasswordInvalidException;
import exceptions.UserNameInvalidException;
import model.Role;
import model.User;
import requests.GetLoggedInUserRequest;
import requests.Request;
import requests.SignInRequest;
import requests.SignOutRequest;
import requests.SignUpRequest;
import responses.GetLoggedInUserResponse;
import responses.NotFoundResponse;
import responses.Response;
import responses.Result;
import responses.SignInResponse;
import responses.SignOutResponse;
import responses.SignUpResponse;
import responses.Status;
import utils.UserValidation;

public class API {
	
	public static Response call(Request request) {
		if (request instanceof SignUpRequest) {
			return signUp((SignUpRequest)request);
		} else if (request instanceof SignInRequest) {
			return signIn((SignInRequest) request);
		} else if (request instanceof SignOutRequest) {
			return signOut((SignOutRequest) request);
		} else if (request instanceof GetLoggedInUserRequest) {
			return getLoggedInUser((GetLoggedInUserRequest) request);
		} else {
			return new NotFoundResponse(Result.FAILURE, Status.NOT_FOUND_ERROR);
		}
	}

	public static SignUpResponse signUp(SignUpRequest request) {
		User user = new User(request.getUsername(), request.getPassword(), request.getRole());
		if (!UserValidation.usernameValidation(request.getUsername())) {
			return new SignUpResponse(Result.FAILURE, Status.USERNAME_ERROR);
		}
		if (!UserValidation.passwordValidation(request.getPassword())) {
			return new SignUpResponse(Result.FAILURE, Status.PASSWORD_ERROR);
		}
		try {
			user.create();
			return new SignUpResponse(Result.SUCCESS, Status.USER_SUCCESSFULLY_CREATED, new Token(user));
		} catch (SQLException e) {
			return new SignUpResponse(Result.FAILURE, Status.DB_ERROR);
		}
	}
	
	public static SignInResponse signIn(SignInRequest request) {
		User user = new User(request.getUsername(), request.getPassword());
		if (!UserValidation.usernameValidation(request.getUsername())) {
			return new SignInResponse(Result.FAILURE, Status.USERNAME_ERROR);
		}
		if (!UserValidation.passwordValidation(request.getPassword())) {
			return new SignInResponse(Result.FAILURE, Status.PASSWORD_ERROR);
		}
		try {
			user = user.checkIfUserExistsAndReturn();
			return new SignInResponse(Result.SUCCESS, Status.USER_SUCCESSFULLY_LOGGEDIN, new Token(user));
		} catch (SQLException e) {
			return new SignInResponse(Result.FAILURE, Status.DB_ERROR);
		}
	}
	
	public static SignOutResponse signOut(SignOutRequest request) {
		try {
			isTokenValid(request.getToken());
			request.getToken().remove();
		} catch (SQLException e) {
			return new SignOutResponse(Result.FAILURE, Status.DB_ERROR);
		} catch (AuthorizationException e) {
			// TODO Auto-generated catch block
			return new SignOutResponse(Result.FAILURE, Status.AUTHORIZATION_ERROR);
		}
		return new SignOutResponse(Result.SUCCESS, Status.USER_LOGOUT_SUCCESS);
	}
	
	public static GetLoggedInUserResponse getLoggedInUser(GetLoggedInUserRequest request) {
		// authorizer
		try {
			isTokenValid(request.getToken());
			String username = request.getToken().getDecodedString().split(" ")[0];
			User user = new User(username).getUserFromDB();
			return new GetLoggedInUserResponse(Result.SUCCESS, Status.DO_SOMETHING_SUCCESS, user);
		} catch (AuthorizationException e) {
			return new GetLoggedInUserResponse(Result.FAILURE, Status.AUTHORIZATION_ERROR);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return new GetLoggedInUserResponse(Result.FAILURE, Status.DB_ERROR);
		}
	}
	
	private static boolean isTokenValid(Token token) throws AuthorizationException {
		return new TokenAuthorizer(token).authorize();
	}
}
