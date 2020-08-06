package responses;

import model.User;

public class GetLoggedInUserResponse extends Response {
	
	User user;

	public GetLoggedInUserResponse(Result result, Status status, User user) {
		super(result, status);
		this.user = user;
	}

	public GetLoggedInUserResponse(Result result, Status status) {
		super(result, status);
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}
}
