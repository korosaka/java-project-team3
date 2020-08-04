package requests;

import auth.Token;

public class DoSomethingRequest implements Request {
	Token token;

	public DoSomethingRequest(Token token) {
		super();
		this.token = token;
	}

	public Token getToken() {
		return token;
	}
}
