import static org.mockito.Mockito.mock;


public class DAOFactory {
	
	/* Creates instances of Account Data Access Objects.
	 * DAOs interface with the persistent store, or DB.
	 */
	
	private static DAOFactory instance = null;
	private static AccountDAO accountDAOImplementation = new AccountDAO("AccountDatabase");
	
	public static DAOFactory getInstance() {
		if (instance == null) {
				instance = new DAOFactory();
		}
		return instance;
	}
	
	public IAccountDAO getAccountDAO() {
		return accountDAOImplementation;
	}

	public IAccountDAO getAccountDAOFake() {
		return new AccountDAOFake();
	}
	
	public IAccountDAO getAccountDAOMock() {
		/* this one returns a MockitoMock object, which can be used as a test double
		 that is custom configured in the tests 
		 */
		return mock(IAccountDAO.class);
	}
	
	public boolean isMock (IAccountDAO dao) {
		/*
		 *  with this method, you can check whether an IAccountDAO object is mock
		 *  or fake in your test code
		 */
		return !(dao instanceof AccountDAOFake) && !(dao instanceof AccountDAO);
	}

}
