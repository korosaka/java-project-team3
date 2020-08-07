package responses;

import java.util.ArrayList;

import model.User;

public class GetAllUserNameResponse extends Response {
		
	ArrayList<User> usersName = new ArrayList<User>();

	public GetAllUserNameResponse(Result result, Status status, ArrayList<User> userName) {
		super(result, status);
		this.usersName = usersName;
	}

	public GetAllUserNameResponse(Result result, Status status) {
		super(result, status);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<User> usersName() {
		return usersName;
	}
}
