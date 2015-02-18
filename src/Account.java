import java.util.HashSet;
import java.util.Set;


public class Account implements Cloneable {
	
	/* 
	 * Base perstitable class for the social network: stores member information
	 * 
	 */
	
	// unique user name 
	private String userName;
	
	// by default, automatic-acceptance is off 
	private boolean autoAccept = false;
	
	// members to whom the account owner owes a response: these members have sent me a friend request 
	private Set<String> pendingResponses = new HashSet<String>();
	
	// members to whom the account owner sent a friend request
	private Set<String> pendingRequests = new HashSet<String>();
	
	// friends of the account owner, identified by their user name
	private Set<String> friends = new HashSet<String>();
	
	// members who are forbidden to see or know about the account owner
	// these members should not be able to send a friend request to this account
	private Set<String> blocked = new HashSet<String>();
	
	// no viable constructor: only SocialNetwork objects can create accounts
	protected Account() {
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Set<String> blockedMembers() {
		return blocked;
	}

	public Set<String> whoWantsToBeFriends() {
		return pendingResponses;
	}
	
	public Set<String> whoDidIAskToBefriend() {
		return pendingRequests;
	}
	
	public Set<String> getFriends() {
		return friends;
	}
	
	// block a member from seeing and knowing about this account owner
	public void block(Account member) {
		blocked.add(member.getUserName());
	}
	
	// unblock a member 
	public void unblock(Account member) {
		blocked.remove(member.getUserName());
	}

	// request friendship from this account owner (unless blocked)
	public void befriend(Account fromMember) {
		if (this.blocked.contains(fromMember)) return;
		pendingResponses.add(fromMember.getUserName());
		fromMember.whoDidIAskToBefriend().add(this.getUserName());
		if (autoAccept) {
			fromMember.accepted(this);
		}
	}
	
	// unfriend the owner of this account
	public void unfriend(Account member) {
		friends.remove(member.getUserName());
		member.friends.remove(this.getUserName());
	}

	public boolean hasFriends() {
		return !(friends.isEmpty());
	}

	// notification from another member owner that a pending friend from this account has been accepted
	// also updates notifying member's account
	public void accepted(Account member) {
		friends.add(member.getUserName());
		pendingRequests.remove(member.getUserName());
		member.friends.add(this.getUserName());
		member.pendingResponses.remove(this.getUserName());
	}
	
	// notification from another member that a pending friend request from this account owner has been rejected 
	// also updates notifying member's account 
	public void rejected(Account member) {
		pendingRequests.remove(member.getUserName());
		member.pendingResponses.remove(this.getUserName());
	}
	
	// returns true if member is a friend of this account owner
	public boolean hasFriend(Account member) {
		return friends.contains(member.getUserName());
	}
	
	// automatically accept all future friend requests
	public void autoAccept() {
		autoAccept = true;
	}
	
	// stop automatic friend acceptance
	public void explicitAccept() {
		autoAccept = false;
	}
	
	// return auto-acceptance status
	public boolean autoAccepts() {
		return autoAccept;
	}
	
	// the remaining methods allows this object to be cloned and clones to be compared using equality
	
	@Override
	public boolean equals (Object m) {
		if (!this.userName.equals(((Account) m).userName)) return false;
		if (!this.autoAccept == (((Account) m).autoAccept)) return false;
		if (!this.pendingRequests.equals(((Account) m).pendingRequests)) return false;
		if (!this.pendingResponses.equals(((Account) m).pendingResponses)) return false;
		if (!this.blocked.equals(((Account) m).blocked)) return false;
		if (!this.friends.equals(((Account) m).friends)) return false;	
		return true;
	}
	
	@Override
	public int hashCode() {
		return getUserName().hashCode();
	}
	
	@Override 
	protected Account clone()  {
		Account clone = new Account();
		clone.autoAccept = this.autoAccept;
		clone.userName = this.userName;
		for (String each : this.friends) {
			clone.friends.add(each);
		}
		for (String each : this.pendingResponses) {
			clone.pendingResponses.add(each);
		}
		for (String each : this.pendingRequests) {
			clone.pendingRequests.add(each);
		}
		for (String each : this.blocked) {
			clone.blocked.add(each);
		}
		return clone;
	}
}
