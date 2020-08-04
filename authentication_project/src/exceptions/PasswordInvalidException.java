package exceptions;

public class PasswordInvalidException extends Exception {

	public PasswordInvalidException() {
		super("Password should be more than 8 and less than 16 characters");
	}
}
