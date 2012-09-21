package logic;
import java.util.ArrayList;
import java.util.List;

import requests.XMLParsable;


public class User extends XMLParsable{

	private String username, password, surName, lastName;
	private List<User> friends;
	private List<Debt> debtList;
	private boolean isOnline;
	
	public User(String username, String password) {
		// TODO: Add all variables to the list!
		this.username = username;
		addVariable("username", this.username);
		this.password = password;
		addVariable("password", this.password);
	}
	
	public User(String username, String password, List<User> friends) {
		this(username, password);
		this.friends = friends;
		addVariable("friends", friends);
	}
	
	public int getNumberOfFriends() {
		return friends.size();
	}
	
	public User getFriend(int i) {
		return friends.get(i);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	
	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	@Override
	public String getClassName() {
		return "User";
	}
	
	public void addFriend(User friend) {
		if(friends == null) {
			this.friends = new ArrayList<User>();
			addVariable("friends", this.friends);
		}
		friends.add(friend);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Username: " + username + ", password: " + password);
		if(friends != null) {
			sb.append(", friends = [");
			for (int i = 0; i < friends.size(); i++) {
				sb.append(friends.get(i).toString());
				if(i < friends.size() - 1) {
					sb.append(", ");
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}
}
