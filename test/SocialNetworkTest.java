import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class SocialNetworkTest {
	
	IAccountDAO accountDAO = DAOFactory.getInstance().getAccountDAOFake();
	
	ISocialNetwork sn = new SocialNetwork(accountDAO);
	Account m, m1, m2, m3, m4;
	Set<Account> all = new HashSet<Account>();

	
	@Before
	public void setUp() throws Exception {
		m = sn.join("John");
		m1 = sn.join("Hakan");
		m2 = sn.join("Serra");
		m3 = sn.join("Dean");
		m4 = sn.join("Hasan");
		// ... other test accounts/members you need to create ... 
		
		/* you can set expectation for mock objects here or in the tests
		 * when injected fake DAO is a 
		 * mock, like this...
		 */
		
		/* if (DAOFactory.isMock(... injected IAccountDAO here...)) {
			//
			// accountDAO is a Mockito mock object, so you may set expectations
			// with when..then clauses, etc. 
			//
		*/
		}

	@After
	public void tearDown() throws Exception {
	
	}
	

	@Test
	public void canJoinSocialNetwork() throws UserExistsException {
		Account newMember = sn.join("Gloria");
		assertEquals("Gloria", newMember.getUserName());
	}

	@Test(expected = NoUserLoggedInException.class)
	public void mustLoginBeforeUsingSocialNetwork()
			throws NoUserLoggedInException, UserNotFoundException {
		sn.hasMember("Jane");
		sn.listMembers();
		sn.sendFriendRequestTo("Jane");
		sn.acceptFriendRequestFrom("Jane");
		sn.rejectFriendRequestFrom("Jane");
		sn.block("Jane");
		sn.unfriend("Jane");
		sn.acceptAutomatically();
		sn.acceptExplicitly();
		sn.leave();
		sn.recommendFriends();
	}

	@Test
	public void canLoginAndFindYourself() throws NoUserLoggedInException,
			UserNotFoundException {
		sn.login(m);
		assertTrue(sn.hasMember(m.getUserName()));
	}

	@Test
	public void canLoginAndFindOthers() throws UserNotFoundException,
			NoUserLoggedInException {
		sn.login(m);
		assertTrue(sn.hasMember(m1.getUserName()));
		assertTrue(sn.hasMember(m2.getUserName()));
	}

	@Test
	public void canListMembers() throws NoUserLoggedInException,
			UserNotFoundException {
		sn.login(m);
		Set<String> members = sn.listMembers();
		assertTrue(members.contains(m1.getUserName()));
		assertTrue(members.contains(m2.getUserName()));
	}

	@Test
	public void sendingFriendRequestCreatesPendingRequestAndResponse()
			throws UserNotFoundException, NoUserLoggedInException {
		assertTrue(m2.whoWantsToBeFriends().isEmpty());
		sn.login(m1);
		sn.sendFriendRequestTo(m2.getUserName());
		m1 = sn.login(m1);
		m2 = sn.login(m2);
		assertTrue(m2.whoWantsToBeFriends().contains(m1.getUserName()));
		assertTrue(m1.whoDidIAskToBefriend().contains(m2.getUserName()));
	}

	@Test
	public void acceptingFriendRequestCreatesFriendship()
			throws UserNotFoundException, NoUserLoggedInException {
		assertTrue(m1.getFriends().isEmpty());
		assertTrue(m2.getFriends().isEmpty());
		sn.login(m1);
		sn.sendFriendRequestTo(m2.getUserName());
		m2 = sn.login(m2);
		sn.acceptFriendRequestFrom(m1.getUserName());
		m1 = sn.login(m1);
		assertTrue(m2.getFriends().contains(m1.getUserName()));
		assertTrue(m1.getFriends().contains(m2.getUserName()));
	}

	@Test
	public void rejectingFriendRequestClearsPendingRequestAndResponse()
			throws UserNotFoundException, NoUserLoggedInException {
		assertTrue(m1.getFriends().isEmpty());
		assertTrue(m2.getFriends().isEmpty());
		sn.login(m1);
		sn.sendFriendRequestTo(m2.getUserName());
		sn.login(m2);
		sn.rejectFriendRequestFrom(m1.getUserName());
		assertFalse(m2.whoWantsToBeFriends().contains(m1.getUserName()));
		assertFalse(m1.whoDidIAskToBefriend().contains(m2.getUserName()));
	}

	@Test(expected = UserNotFoundException.class)
	public void canNotSendAFriendRequestToNonExistingMember()
			throws UserNotFoundException, NoUserLoggedInException {
		sn.login(m1);
		sn.sendFriendRequestTo("Anonymous");
	}

	@Test(expected = UserExistsException.class)
	public void canNotJoinSocialNetworkAgain() throws UserExistsException {
		sn.join("Hakan");
	}

	@Test
	public void blockingAMemberMakesUserInvisibleToHerInHasMember()
			throws UserExistsException, UserNotFoundException,
			NoUserLoggedInException {
		sn.login(m1);
		sn.block(m2.getUserName());
		sn.login(m2);
		assertFalse(sn.hasMember(m1.getUserName()));
	}

	@Test
	public void blockingAMemberMakesUserInvisibleToHerInListMembers()
			throws UserExistsException, UserNotFoundException,
			NoUserLoggedInException {
		sn.login(m1);
		sn.block(m2.getUserName());
		sn.login(m2);
		Set<String> allMembers = sn.listMembers();
		assertFalse(allMembers.contains(m1.getUserName()));
	}

	@Test
	public void recommendMembersReturnsSharedFriendsOfMyFriends()
			throws UserNotFoundException, NoUserLoggedInException {
		sn.login(m);
		sn.sendFriendRequestTo(m1.getUserName());
		sn.sendFriendRequestTo(m2.getUserName());
		sn.login(m1);
		sn.acceptFriendRequestFrom(m.getUserName());
		sn.sendFriendRequestTo(m3.getUserName());
		sn.login(m2);
		sn.acceptFriendRequestFrom(m.getUserName());
		sn.acceptFriendRequestFrom(m3.getUserName());
		sn.login(m3);
		sn.acceptFriendRequestFrom(m1.getUserName());
		sn.acceptFriendRequestFrom(m2.getUserName());
		sn.login(m);
		Set<String> recommendations = sn.recommendFriends();
		assertTrue(recommendations.contains(m3.getUserName()));
		assertFalse(recommendations.contains(m2.getUserName()));
	}

	@Test
	public void canLeaveSocialNetwork() throws UserNotFoundException,
			NoUserLoggedInException {
		sn.login(m1);
		sn.leave();
		// might have to do additional checking if using a Mockito mock
		sn.login(m2);
		assertFalse(sn.hasMember(m1.getUserName()));
	}	
	
	/* 
	 * The rest are spy tests that work with the unimplemented DAO stub or any real or stubbed  
	 * version of a DAO object to verify that persistence operations are called. 
	 * These tests ONLY ensure that the right persistence operations of the mocked IAccountDAO implementation are called with
	 * the right parameters. They don't need to verify that the underlying DB is actually updated. 
	 * So we assume that if the DB connection is real, persistence operations of IAccountDAO will succeed. 
	 * 
	 */

	
	@Test public void newAccountsAreAlwaysSaved() throws UserExistsException {
		// make sure that when a new member account is created, it will be persisted 
		fail();
	}
	
	@Test public void willAttemptToPersistSendingAFriendRequest() throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
		// make sure that when a logged-in member issues a friend request, any changes to the affected accounts will be persisted
		fail();	}
	
	@Test public void willAttemptToPersistAcceptanceOfFriendRequest() throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
		// make sure that when a logged-in member issues a friend request, any changes to the affected accounts will be persisted
		fail();	}
	
	@Test public void willAttemptToPersistRejectionOfFriendRequest() throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
		// make sure that when a logged-in member rejects a friend request, any changes to the affected accounts will be persisted
		fail();	}
	
	@Test public void willAttemptToPersistBlockingAMember() throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
		// make sure that when a logged-in member blocks another member, any changes to the affected accounts will be persisted
		fail();	}
		
	@Test public void willAttemptToPersistLeavingSocialNetwork() throws UserExistsException, UserNotFoundException, NoUserLoggedInException {
		// make sure that when a logged-in member leaves the social network, his account will be permanenlty deleted and  
		// any changes to the affected accounts will be persisted
		fail();	
	}
	
	
	/*
	 * The rest are auxiliary tests. 
	 * They make sure that Account objects are cloneable and equality works as expected with them.
	 */
	
	@Test
	
	public void setEqualityWorksAsExpected() {
		Set<String> s1 = new HashSet<String>();
		Set<String> s2 = new HashSet<String>();
		s1.add("abc");
		s1.add("def");
		s2.add("def");
		s2.add("abc");
		assertEquals(s1, s2);
	}
	
	public void accountEqualityWorksAsExpected() {
		Account m3 = m1;
		assertEquals(m1, m3);
		assertEquals(m1, m1.clone());
		assertFalse(m1.equals(m2)); 
	}
	
	@Test public void canCloneAccount() {
		Account orig = m1;
		Account initCopy = orig.clone();
		m1.befriend(m4);
		m1.autoAccept();
		m1.befriend(m2);
		m3.befriend(m1);
		m1.block(m);	
		Account clone = m1.clone();
		assertTrue(clone.equals(orig));
		assertFalse(initCopy.equals(clone));
		assertTrue(orig.getFriends().contains(m2.getUserName()));
		assertTrue(clone.getFriends().contains(m2.getUserName()));
		assertTrue(clone.whoDidIAskToBefriend().contains(m3.getUserName()));
		assertTrue(clone.blockedMembers().contains(m.getUserName()));
		assertTrue(clone.whoWantsToBeFriends().contains(m4.getUserName()));
	}

}
