package requests;

import auth.Token;

public class GetAllUserNameRequest implements Request {
	Token token;

	public GetAllUserNameRequest(Token token) {
		super();
		this.token = token;
	}

	public Token getToken() {
		return token;
	}
}
