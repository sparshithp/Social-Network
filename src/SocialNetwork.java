import java.util.HashSet;
import java.util.Set;



public class SocialNetwork implements ISocialNetwork {
	
	private Account currentUser = null;
	private IAccountDAO accountDAO = DAOFactory.getInstance().getAccountDAO();
	
	
	//Task1
	public SocialNetwork(IAccountDAO injectedAccountDAO) {
		this.accountDAO = injectedAccountDAO;
	}
	
	public IAccountDAO getAccountDAO(){
		return this.accountDAO;
	}
	
	public void setAccountDAO(IAccountDAO injectedAccountDAO){
		this.accountDAO = injectedAccountDAO;
	}
	//Task 1

	private class MyAccount extends Account {
		
		public MyAccount(String userName) {
			setUserName(userName);
		}
	}

	public Account join(String userName) throws UserExistsException {
		Account member = new MyAccount(userName);
		Account existingMember = accountDAO.findByUserName(userName);
		if (existingMember != null) throw new UserExistsException(userName);
		accountDAO.save(member);
		return member;
	}
	
	public Account login(Account me) throws UserNotFoundException {
		if (me == null) throw new UserNotFoundException("Null");
		Account member = accountDAO.findByUserName(me.getUserName());
		if (member == null) throw new UserNotFoundException(me.getUserName());
		currentUser = member;
		return member;
	}

	public void logout() {
		currentUser = null;
		
	}
	
	public Set<String> listMembers() throws NoUserLoggedInException{
		if (currentUser == null) throw new NoUserLoggedInException();
		Set<Account> members = accountDAO.findAll();
		Set<String> userNames = new HashSet<String>();
		for (Account each : members) {
			if (!each.blockedMembers().contains(currentUser.getUserName())) {
				userNames.add(each.getUserName());
			}
		}
		return userNames;	
	}

	public boolean hasMember(String userName) throws NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Account member = accountDAO.findByUserName(userName);
		if (member == null) {
			return false;
		}
		if (member.blockedMembers().contains(currentUser.getUserName())) {
			return false;
		}
		return true;
	}

	public void block(String userName) throws UserNotFoundException, NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Account member = accountDAO.findByUserName(userName); 
		if (member == null) throw new UserNotFoundException(userName);
		currentUser.block(member);
		accountDAO.update(currentUser);
	}
	
	public void unBlock(String userName) throws UserNotFoundException, NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Account member = accountDAO.findByUserName(userName);
		if (member == null) throw new UserNotFoundException(userName);
		accountDAO.update(currentUser);	
	}
	
	public void sendFriendRequestTo(String userName) throws UserNotFoundException, NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Account toMember = accountDAO.findByUserName(userName);
		if (toMember == null) throw new UserNotFoundException(userName);
		toMember.befriend(currentUser);
		accountDAO.update(currentUser); 
	}

	public void leave() throws NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		for (String each : currentUser.getFriends()) {
			Account friend = accountDAO.findByUserName(each);
			friend.unfriend(currentUser);
			accountDAO.update(friend);
		}
		accountDAO.update(currentUser);
		currentUser = null;
	}
	
	public void unfriend(String userName) throws UserNotFoundException, NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Account member = accountDAO.findByUserName(userName);
		if (member == null) throw new UserNotFoundException(userName);
		member.unfriend(currentUser);
		accountDAO.update(member);
		accountDAO.update(currentUser);
	}
	
	public void acceptFriendRequestFrom(String userName) throws UserNotFoundException, NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Account member = accountDAO.findByUserName(userName);
		if (member == null) throw new UserNotFoundException(userName);
		member.accepted(currentUser);
		accountDAO.update(member);
		accountDAO.update(currentUser);
	}
	
	public void rejectFriendRequestFrom(String userName) throws UserNotFoundException, NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Account member = accountDAO.findByUserName(userName);
		if (member == null) throw new UserNotFoundException(userName);
		member.rejected(currentUser);
		accountDAO.update(member);
	}

	public void acceptAutomatically() throws NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		currentUser.autoAccept();
		accountDAO.update(currentUser);
	}
	
	public void acceptExplicitly() throws NoUserLoggedInException {
		if (currentUser == null) throw new NoUserLoggedInException();
		currentUser.explicitAccept();
		accountDAO.update(currentUser);
	}

	public Set<String> recommendFriends() throws NoUserLoggedInException, UserNotFoundException {
		if (currentUser == null) throw new NoUserLoggedInException();
		Set<String> recommendations = new HashSet<String>();
		Set<String> seen = new HashSet<String>();
		for (String each: currentUser.getFriends()) {
			Account friend = accountDAO.findByUserName(each);
			if (friend == null ) throw new UserNotFoundException(each);
			for (String friendOfFriend: friend.getFriends()) {
				if (seen.contains(friendOfFriend)) {
					//if (!currentUser.getFriends().contains(friendOfFriend) && !currentUser.blockedMembers().contains(friendOfFriend))
						recommendations.add(friendOfFriend);
				} 
				else {
					seen.add(friendOfFriend);
				}
			}
		}
		return recommendations;
	}

}
