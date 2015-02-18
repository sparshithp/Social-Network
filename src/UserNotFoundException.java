
public class UserNotFoundException extends Exception {
	
	/**
	 * Thrown when as supposed user is not a member of the social network
	 */
	private static final long serialVersionUID = 00001L;

	public UserNotFoundException(String missingUserName) {
	}

}
