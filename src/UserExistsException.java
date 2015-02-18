
public class UserExistsException extends Exception {
	
	/**
	 * Thrown when a new user is already a member of the social network
	 */
	private static final long serialVersionUID = 00002L;

	public UserExistsException(String existingUserName) {
	}

}
