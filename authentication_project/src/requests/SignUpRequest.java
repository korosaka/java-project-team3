package requests;

import model.Role;

public class SignUpRequest implements Request {

	String username;
	String password;
	Role role;
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Role getRole() {
		return role;
	}

	public SignUpRequest(String username, String password, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
