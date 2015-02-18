import java.util.Set;


public interface IAccountDAO {
	
	// Interface for the Account Data Access Object implementations
	
	public void save(Account member); 
	public Account findByUserName(String userName) ;
	public void delete(Account member); 
	public void update(Account member); 
	public Set<Account> findAll();
}
