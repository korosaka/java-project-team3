package responses;

import java.util.ArrayList;

import model.User;

public class GetAllUserNameResponse extends Response {
		
	ArrayList<User> users;

	public GetAllUserNameResponse(Result result, Status status, ArrayList<User> users) {
		super(result, status);
		this.users = users;
	}

	public GetAllUserNameResponse(Result result, Status status) {
		super(result, status);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<User> getAllUsers() {
		return users;
	}
}
