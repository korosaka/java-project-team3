package requests;

import auth.Token;

public class SignOutRequest implements Request {
	Token token;

	public Token getToken() {
		return token;
	}

	public SignOutRequest(Token token) {
		super();
		this.token = token;
	}
}
