package exceptions;

public class UserNameInvalidException extends Exception {

	public UserNameInvalidException() {
		super("User name should not be empty and less than 20 characters");
	}
}
