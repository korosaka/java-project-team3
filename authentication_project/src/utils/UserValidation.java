package utils;

public class UserValidation {

	public static boolean usernameValidation(String name) {
		return name.length() > 0 && name.length() < 20;
	}
	
	public static boolean passwordValidation(String password) {
		return password.length() > 8 && password.length() < 16;
	}
}
