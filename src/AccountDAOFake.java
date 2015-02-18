import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class AccountDAOFake implements IAccountDAO {
	/*
	 * A full in-memory fake of AccountDAO.
	 * 
	 * IMPORTANT NOTE:
	 * If you wish to take advantage of the Account class for simulating the database,
	 * make sure that your storage representation relies on fully cloned objects, not just Account references passed.
	 * That is, it's not enough to store an in-memory reference to an Account object. 
	 * Tests should not be able to distinguish this object from a real DAO object connected to a real DB. 
	 * In that sense, this class is a bit more than a fake that works only with certain inputs. 
	 * It implements a full Account DOA simulator that will work with all test cases
	 * that need an AccountDAO as a collaborator. 
	 */
	
	Map<String, Account> db = new HashMap<String, Account>();
	
		
	public boolean isFullMock() {  // really, I should call this isFullFake()
		return true;
	}

	public void save(Account member) {
		// implement this method
		db.put(member.getUserName(), member);
	}

	public Account findByUserName(String userName)  {
		return db.get(userName);
	}
	
	public Set<Account> findAll()  {
		return new HashSet<Account>(db.values());
		// implement this method
	}

	public void delete(Account member) {
		// implement this method
		db.remove(member).getUserName();
	}

	public void update(Account member) {
		// implement this method
		db.put(member.getUserName(), member);
	}

}
