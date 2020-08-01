package auth;

import exceptions.AuthorizationException;

public abstract class Authorizer {
	public abstract boolean authorize() throws AuthorizationException;
}
