package requests;

public class SignInRequest implements Request {
	String username;
	String password;
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public SignInRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
}
