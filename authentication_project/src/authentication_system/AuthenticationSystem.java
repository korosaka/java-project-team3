package authentication_system;

import java.sql.SQLException;
import java.util.Scanner;

import api.API;
import auth.Token;
import exceptions.PasswordInvalidException;
import exceptions.UserNameInvalidException;
import model.Role;
import model.User;

public class AuthenticationSystem {
	
	Token token;
	Scanner scanner = new Scanner(System.in);

	public void run() {
		while (true) {
			if (token == null) {
				System.out.println("Choose from below.");
				System.out.println("Sign in[1]");
				System.out.println("Sign up[2]");
				System.out.println("Quit[3]");
				int num = scanner.nextInt();
				if (num == 1) {
					token = signInFlow();
				} else if (num == 2) {
					token = signUpFlow();
				} else if (num == 3) {
					break;
				}
			} else {
				System.out.println("You are logged in. Choose from below.");
				System.out.println("Sign out[1]");
				System.out.println("Do something[2]");
				int num2 = scanner.nextInt();
				if (num2 == 1) {
					signOut();
					token = null;
				} else if (num2 == 2) {
					doSomething(token);
				}
			}
		}
	}
	
	private void signOut() {
		System.out.println("sign out");
	}
	
	private void doSomething(Token token) {
		System.out.println("do Something");
		API.doSomething(token);
	}
	
	private Token signInFlow() {
		scanner = new Scanner(System.in);
		System.out.println("Type in username");
		String name = scanner.nextLine();
		System.out.println("Type in password");
		String password = scanner.nextLine();
		Token token;
		try {
			token = API.signIn(name, password);
			return token;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	private Token signUpFlow() {
		scanner = new Scanner(System.in);
		System.out.println("Type in username");
		String name = scanner.nextLine();
		System.out.println("Type in password");
		String password = scanner.nextLine();
		System.out.println("choose your role");
		System.out.println("admin[1]");
		System.out.println("normal[any other key]");
		Role role = scanner.nextInt() == 1 ? Role.ADMIN : Role.NORMAL;
		Token token;
		try {
			token = API.signUp(name, password, role);
			return token;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
