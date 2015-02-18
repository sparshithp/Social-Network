import java.util.Set;


public class AccountDAO implements IAccountDAO {
	
	private static boolean connected = false; 
	
	/* This is meant to be the real Data Access Object for persisting Account objects.
	 * But it's not implemented yet, so it's all stubbed out.
	 * Testing must continue in its absence, unfortunately.
	 */
	
	public AccountDAO (String dataBase) {
		// some fancy DB operation
		// sets connected to true if already connected to DB or if connection succeeds 
	}
	
	public boolean isConnectedToDB() {
		return connected;
	}

	public void save(Account member) {
		// TODO Auto-generated method stub
		
	}

	public Account findByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Account member) {
		// TODO Auto-generated method stub
		
	}

	public void update(Account member) {
		// TODO Auto-generated method stub
		
	}

	public Set<Account> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
