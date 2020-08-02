package responses;

public abstract class Response {
	
	Result result;
	Status status;
	
	public Response(Result result, Status status) {
		super();
		this.result = result;
		this.status = status;
	}
	
	public Result getResult() {
		return result;
	}
	public Status getStatus() {
		return status;
	}
}
