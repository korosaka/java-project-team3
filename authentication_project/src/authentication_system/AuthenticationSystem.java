package authentication_system;

import java.util.Scanner;

import api.API;
import auth.Token;
import model.Role;
import requests.DoSomethingRequest;
import requests.SignInRequest;
import requests.SignOutRequest;
import requests.SignUpRequest;
import responses.DoSomethingResponse;
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
				int num = scanner.nextInt();
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
				System.out.println("Do something[2]");
				System.out.println("======================");
				System.out.println();
				int num2 = scanner.nextInt();
				if (num2 == 1) {
					signOut();
				} else if (num2 == 2) {
					requestAPI();
				}
			}
		}
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
	
	private void requestAPI() {
		DoSomethingResponse resp = (DoSomethingResponse)API.call(new DoSomethingRequest(token));
		switch(resp.getResult()) {
		case SUCCESS:
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
		Role role = scanner.nextInt() == 1 ? Role.ADMIN : Role.NORMAL;
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
