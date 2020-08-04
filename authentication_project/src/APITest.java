import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.API;
import auth.Token;
import model.Role;
import model.User;
import requests.DoSomethingRequest;
import requests.SignInRequest;
import requests.SignOutRequest;
import requests.SignUpRequest;
import responses.DoSomethingResponse;
import responses.Result;
import responses.SignInResponse;
import responses.SignOutResponse;
import responses.SignUpResponse;
import responses.Status;

class APITest {
	
	User loggedInUser;
	User newUser;

	@BeforeEach
	void setUp() throws Exception {
		loggedInUser = new User("testUser1", "password1", Role.ADMIN);
		loggedInUser.create();
	}

	@AfterEach
	void tearDown() throws Exception {
		loggedInUser.remove();
		if (newUser != null) {
			newUser.remove();
		}
	}
	
	@Test
	void do_sign_out_failure_without_token() {
		SignOutResponse resp2 = (SignOutResponse)API.call(new SignOutRequest(null));
		assertEquals(resp2.getResult(), Result.FAILURE);
		assertEquals(resp2.getStatus(), Status.AUTHORIZATION_ERROR);
	}
	
	@Test
	void do_sign_out_success() {
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest(loggedInUser.getName(), loggedInUser.getPassword()));
		Token token = resp.getToken();
		SignOutResponse resp2 = (SignOutResponse)API.call(new SignOutRequest(token));
		assertEquals(resp2.getResult(), Result.SUCCESS);
		assertEquals(resp2.getStatus(), Status.USER_LOGOUT_SUCCESS);
	}
	
	@Test
	void do_something_failure_wrong_token() throws SQLException {
		Token wrongToken = new Token(new User("user_dummy", "Password", Role.ADMIN));
		DoSomethingResponse resp2 = (DoSomethingResponse)API.call(new DoSomethingRequest(wrongToken));
		assertEquals(resp2.getResult(), Result.FAILURE);
		assertEquals(resp2.getStatus(), Status.AUTHORIZATION_ERROR);
	}
	
	@Test
	void do_something_failure_empty_token() throws SQLException {
		DoSomethingResponse resp2 = (DoSomethingResponse)API.call(new DoSomethingRequest(null));
		assertEquals(resp2.getResult(), Result.FAILURE);
		assertEquals(resp2.getStatus(), Status.AUTHORIZATION_ERROR);
	}
	
	@Test
	void do_something_success() {
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest(loggedInUser.getName(), loggedInUser.getPassword()));
		Token token = resp.getToken();
		DoSomethingResponse resp2 = (DoSomethingResponse)API.call(new DoSomethingRequest(token));
		assertEquals(resp2.getResult(), Result.SUCCESS);
		assertEquals(resp2.getStatus(), Status.DO_SOMETHING_SUCCESS);
		API.call(new SignOutRequest(token));
	}
	
	@Test
	void signUp_success() {
		newUser = new User("testUser2", "password2", Role.NORMAL);
		SignUpResponse resp = (SignUpResponse)API.call(new SignUpRequest(newUser.getName(), newUser.getPassword(), newUser.getRole()));
		assertEquals(resp.getResult(), Result.SUCCESS);
		assertEquals(resp.getStatus(), Status.USER_SUCCESSFULLY_CREATED);
		assertNotNull(resp.getToken());
	}
	
	@Test
	void signUp_failure_user_already_exists() {
		newUser = new User("testUser1", "password2", Role.NORMAL);
		SignUpResponse resp = (SignUpResponse)API.call(new SignUpRequest(newUser.getName(), newUser.getPassword(), newUser.getRole()));
		assertEquals(resp.getResult(), Result.FAILURE);
		assertEquals(resp.getStatus(), Status.DB_ERROR);
		assertNull(resp.getToken());
	}
	
	@Test
	void signUp_failure_user_name_too_short() {
		newUser = new User("", "password2", Role.NORMAL);
		SignUpResponse resp = (SignUpResponse)API.call(new SignUpRequest(newUser.getName(), newUser.getPassword(), newUser.getRole()));
		assertEquals(resp.getResult(), Result.FAILURE);
		assertEquals(resp.getStatus(), Status.USERNAME_ERROR);
		assertNull(resp.getToken());
	}
	
	@Test
	void signUp_failure_user_password_too_short() {
		newUser = new User("testUser1", "password", Role.NORMAL);
		SignUpResponse resp = (SignUpResponse)API.call(new SignUpRequest(newUser.getName(), newUser.getPassword(), newUser.getRole()));
		assertEquals(resp.getResult(), Result.FAILURE);
		assertEquals(resp.getStatus(), Status.PASSWORD_ERROR);
		assertNull(resp.getToken());
	}

	@Test
	void signIn_success() {
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest(loggedInUser.getName(), loggedInUser.getPassword()));
		assertEquals(resp.getResult(), Result.SUCCESS);
		assertEquals(resp.getStatus(), Status.USER_SUCCESSFULLY_LOGGEDIN);
		assertNotNull(resp.getToken());
		API.call(new SignOutRequest(resp.getToken()));
	}
	
	@Test
	void signIn_failure_password_wrong() {
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest(loggedInUser.getName(), "dummy_password"));
		assertEquals(resp.getResult(), Result.FAILURE);
		assertEquals(resp.getStatus(), Status.DB_ERROR);
		assertNull(resp.getToken());
	}
	
	@Test
	void signIn_failure_password_too_short() {
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest(loggedInUser.getName(), ""));
		assertEquals(resp.getResult(), Result.FAILURE);
		assertEquals(resp.getStatus(), Status.PASSWORD_ERROR);
		assertNull(resp.getToken());
	}
	
	@Test
	void signIn_failure_name_wrong() {
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest("wrong_name", loggedInUser.getPassword()));
		assertEquals(resp.getResult(), Result.FAILURE);
		assertEquals(resp.getStatus(), Status.DB_ERROR);
		assertNull(resp.getToken());
	}
	
	@Test
	void signIn_failure_name_too_short() {
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest("", loggedInUser.getPassword()));
		assertEquals(resp.getResult(), Result.FAILURE);
		assertEquals(resp.getStatus(), Status.USERNAME_ERROR);
		assertNull(resp.getToken());
	}

	
}
