package authentication_system;

import java.util.InputMismatchException;
import java.util.Scanner;

import api.API;
import auth.Token;
import model.Role;
import requests.GetAllUserNameRequest;
import requests.GetLoggedInUserRequest;
import requests.SignInRequest;
import requests.SignOutRequest;
import requests.SignUpRequest;
import responses.GetAllUserNameResponse;
import responses.GetLoggedInUserResponse;
import responses.SignInResponse;
import responses.SignOutResponse;
import responses.SignUpResponse;

public class AuthenticationSystem {

	Token token;
	Scanner scanner;
	
	public AuthenticationSystem() {
		super();
		this.token = null;
		scanner = new Scanner(System.in);
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public void run() {
		while (true) {
			if (token == null) {
				System.out.println("======================");
				System.out.println("Choose from below.");
				System.out.println("Sign in[1]");
				System.out.println("Sign up[2]");
				System.out.println("Quit[3]");
				System.out.println("======================");
				System.out.println();
				int num = inputNum();
				if (num == -1) {
					System.out.println("\n" + "You must input NUMBER" + "\n");
					continue;
				}
				if (num == 1) {
					signInFlow();
				} else if (num == 2) {
					signUpFlow();
				} else if (num == 3) {
					scanner.close();
					break;
				}
			} else {
				System.out.println("======================");
				System.out.println("You are logged in. Choose from below.");
				System.out.println("Sign out[1]");
				System.out.println("get LoggedIn user info[2]");
				System.out.println("get LoggedIn all users name[3]");
				System.out.println("======================");
				System.out.println();
				int num2 = inputNum();
				if (num2 == -1) {
					System.out.println("\n" + "You must input NUMBER" + "\n");
					continue;
				}
				if (num2 == 1) {
					signOut();
				} else if (num2 == 2) {
					getLoggedInUser();
				}else if (num2 == 3) {
					getAllUserName();
			}
			}
			}
		}
	
	private int inputNum() {
		int num = -1;
		try {
			num = scanner.nextInt();
		} catch(InputMismatchException e) {
			scanner.next(); // 無限ループ回避のため
		}
		return num;
	}
	
	private void signOut() {
		SignOutResponse resp = (SignOutResponse)API.call(new SignOutRequest(token));
		switch(resp.getResult()) {
		case SUCCESS:
			setToken(null);
			break;
		case FAILURE:
		default:
			System.err.println(resp.getStatus());
		}
	}
	
	private void getLoggedInUser() {
		GetLoggedInUserResponse resp = (GetLoggedInUserResponse)API.call(new GetLoggedInUserRequest(token));
		switch(resp.getResult()) {
		case SUCCESS:
			System.out.println(resp.getUser());
			break;
		case FAILURE:
		default:
			System.err.println(resp.getStatus());
			setToken(null);
		}
	}
	
	private void getAllUserName() {
		GetAllUserNameResponse resp = (GetAllUserNameResponse)API.call(new GetAllUserNameRequest(token));
		switch(resp.getResult()) {
		case SUCCESS:
			System.out.println(resp.usersName());
			break;
		case FAILURE:
		default:
			System.err.println(resp.getStatus());
			setToken(null);
		}
	}
	
	private void signInFlow() {
		scanner = new Scanner(System.in);
		System.out.println("Type in username");
		String name = scanner.nextLine();
		System.out.println("Type in password");
		String password = scanner.nextLine();
		SignInResponse resp = (SignInResponse)API.call(new SignInRequest(name, password));
		switch(resp.getResult()) {
		case SUCCESS:
			setToken(resp.getToken());
			break;
		case FAILURE:
		default:
			System.err.println(resp.getStatus());
			setToken(null);
		}
	}
	
	private void signUpFlow() {
		scanner = new Scanner(System.in);
		System.out.println("Type in username");
		String name = scanner.nextLine();
		System.out.println("Type in password");
		String password = scanner.nextLine();
		System.out.println("choose your role");
		System.out.println("admin[1]");
		System.out.println("normal[any other key]");
		Role role = inputNum() == 1 ? Role.ADMIN : Role.NORMAL;
		SignUpResponse resp = (SignUpResponse)API.call(new SignUpRequest(name, password, role));
		switch(resp.getResult()) {
		case SUCCESS:
			setToken(resp.getToken());
			break;
		case FAILURE:
		default:
			System.err.println(resp.getStatus());
			setToken(null);
		}
	}
}
