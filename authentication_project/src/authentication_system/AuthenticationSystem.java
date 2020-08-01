package authentication_system;

import java.sql.SQLException;
import java.util.Scanner;

import api.API;
import auth.Token;
import exceptions.AuthorizationException;
import exceptions.PasswordInvalidException;
import exceptions.UserNameInvalidException;
import model.Role;
import model.User;

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
				System.out.println("Choose from below.");
				System.out.println("Sign in[1]");
				System.out.println("Sign up[2]");
				System.out.println("Quit[3]");
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
				System.out.println("You are logged in. Choose from below.");
				System.out.println("Sign out[1]");
				System.out.println("Do something[2]");
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
		System.out.println("sign out");
		try {
			API.signOut(token);
			setToken(null);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private void requestAPI() {
		try {
			API.doSomething(token);
		} catch (AuthorizationException e) {
			System.err.println(e.getMessage());
			setToken(null);
		}
	}
	
	private void signInFlow() {
		scanner = new Scanner(System.in);
		System.out.println("Type in username");
		String name = scanner.nextLine();
		System.out.println("Type in password");
		String password = scanner.nextLine();
		try {
			Token token = API.signIn(name, password);
			setToken(token);
		} catch (Exception e) {
			System.err.println(e.getMessage());
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
		try {
			Token token = API.signUp(name, password, role);
			setToken(token);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			setToken(null);
		}
	}
}
