package requests;

import auth.Token;

public class GetLoggedInUserRequest implements Request {
	Token token;

	public GetLoggedInUserRequest(Token token) {
		super();
		this.token = token;
	}

	public Token getToken() {
		return token;
	}
}
