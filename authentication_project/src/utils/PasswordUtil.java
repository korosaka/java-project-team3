package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

	public static String hashPassword(String plainTextPassword){
	    return BCrypt.withDefaults().hashToString(12, plainTextPassword.toCharArray());
	}
	
	public static boolean compare(char[] password, char[] hashedPassword) {
		return BCrypt.verifyer().verify(password, hashedPassword).verified;
	}
}
