package responses;

import auth.Token;

public class SignInResponse extends Response {

	Token token;
	
	public SignInResponse(Result result, Status status, Token token) {
		super(result, status);
		// TODO Auto-generated constructor stub
		this.token = token;
	}
	
	public SignInResponse(Result result, Status status) {
		super(result, status);
		// TODO Auto-generated constructor stub
	}

	public Token getToken() {
		return token;
	}
}
