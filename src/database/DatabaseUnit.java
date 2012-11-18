package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import requests.FriendRequest;
import requests.FriendRequest.FriendRequestStatus;

import logic.Debt;
import logic.DebtStatus;
import logic.User;

public class DatabaseUnit {

	public static final String DB_HOST_NAME = "mysql://localhost";
	public static final int DB_PORT = 3306;
	public static final String DB_NAME = "debtlist";
	public static final String DB_USERNAME = "debtlist";
	public static final String DB_PASSWORD = null;
	
	// Database tables
	public static final String TABLE_USER = "user";
	public static final String TABLE_DEBT = "debt";
	public static final String TABLE_FRIEND_REQUEST = "friendRequest";
	
	// Database fields
	public static final String FIELD_USER_USERNAME = "username";
	public static final String FIELD_USER_PASSWORD = "password";
	public static final String FIELD_USER_ID = "id";
	
	public static final String FIELD_DEBT_ID = "id";
	public static final String FIELD_DEBT_AMOUNT = "amount";
	public static final String FIELD_DEBT_WHAT = "what";
	public static final String FIELD_DEBT_TO_USER = "toUser";
	public static final String FIELD_DEBT_FROM_USER = "fromUser";
	public static final String FIELD_DEBT_COMMENT = "comment";
	public static final String FIELD_DEBT_STATUS = "status";
	public static final String FIELD_DEBT_REQUESTED_BY_USER = "requestedByUser";
	
	public static final String FIELD_FRIEND_REQUEST_ID = "id";
	public static final String FIELD_FRIEND_REQUEST_TO_USER = "toUser";
	public static final String FIELD_FRIEND_REQUEST_FROM_USER = "fromUser";
	public static final String FIELD_FRIEND_REQUEST_STATUS = "status";
	
	private Statement st;
	private Connection con;
	
	/**
	 * Connects to the database specified with this class' constants
	 * @return	True if the connection was successfully established, false if not.
	 */
	public boolean connect() {
		try {
			//Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");

			//Define URL of database server for
			String url =
				"jdbc:" + DB_HOST_NAME + ":" + DB_PORT + "/" + DB_NAME;

			//Get a connection to the database for a
			con = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);

			//Display URL and connection information
			System.out.println("URL: " + url);
			System.out.println("Connection: " + con);

			//Get a Statement object
			st = con.createStatement();
			return true;
		}catch( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Attempts to close the connection to the database
	 */
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {}
	}
	
	/**
	 * Returns the users contained in the database
	 * @return	A map with the users as keys and their passwords as values
	 * @throws SQLException
	 */
	public Map<User, String> loadUsers() throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM " + TABLE_USER);
		Map<User, String> users = new HashMap<User, String>();
		while(rs.next()) {
			users.put(new User(rs.getLong(FIELD_USER_ID), rs.getString(FIELD_USER_USERNAME)), rs.getString(FIELD_USER_PASSWORD));
		}
		return users;
	}
	
	/**
	 * Will load the users' friends and their friend request
	 * @param users			The users to load friends and friend requests for, represented
	 * 						in a map with the user name as key, and user object as value
	 * @throws SQLException
	 */
	public void loadFriends(Map<String, User> users) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM " + TABLE_FRIEND_REQUEST);
		while(rs.next()) {
			FriendRequestStatus status = Enum.valueOf(FriendRequestStatus.class, rs.getString(FIELD_FRIEND_REQUEST_STATUS));
			switch(status) {
			case ACCEPTED:
				// Add the users as friends
				users.get(rs.getString(FIELD_FRIEND_REQUEST_FROM_USER)).addFriend(users.get(rs.getString(FIELD_FRIEND_REQUEST_TO_USER)));
				users.get(rs.getString(FIELD_FRIEND_REQUEST_TO_USER)).addFriend(users.get(rs.getString(FIELD_FRIEND_REQUEST_FROM_USER)));
				// Fall through to add the friend request
			case PENDING:
				// Add the friend request to the target friend
				users.get(rs.getString(FIELD_FRIEND_REQUEST_TO_USER)).addFriendRequest(new FriendRequest(rs.getString(FIELD_FRIEND_REQUEST_TO_USER), users.get(rs.getString(FIELD_FRIEND_REQUEST_FROM_USER)), status, rs.getLong(FIELD_FRIEND_REQUEST_ID)));
				break;
			default:
				System.out.println("Skipped FriendRequest with weird status while loading friends.");
			}
		}
	}

	/**
	 * Converts the given list of users to a map with the users' user name as key and the user objects as values,
	 * as used by this class' methods
	 * @param users	The users in a list
	 * @return		The users in a map, indexed with the users' user name
	 */
	public static Map<String, User> listToMap(List<User> users) {
		Map<String, User> map = new HashMap<String, User>();
		for (User u : users) {
			map.put(u.getUsername(), u);
		}
		return map;
	}
	
	/**
	 * Load the given users' debts. REQUESTED and DECLINED debts will be put in pending debts.
	 * CONFIRMED, COMPLETED_BY_TO, COMPLETED_BY_FROM, MERGE and COMPLETED will be put in confirmed debts.
	 * @param users	The users
	 * @throws SQLException
	 */
	public void loadDebts(Map<String, User> users) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM " + TABLE_DEBT);
		while(rs.next()) {
			Debt d = new Debt(rs.getLong(FIELD_DEBT_ID), rs.getDouble(FIELD_DEBT_AMOUNT), rs.getString(FIELD_DEBT_WHAT), users.get(rs.getString(FIELD_DEBT_FROM_USER)), users.get(rs.getString(FIELD_DEBT_TO_USER)), rs.getString(FIELD_DEBT_COMMENT), users.get(rs.getString(FIELD_DEBT_REQUESTED_BY_USER)), Enum.valueOf(DebtStatus.class, rs.getString(FIELD_DEBT_STATUS)));
			switch(d.getStatus()) {
			case REQUESTED:
			case DECLINED:
				d.getFrom().addPendingDebt(d);
				d.getTo().addPendingDebt(d);
				break;
			case CONFIRMED:
			case COMPLETED_BY_TO:
			case COMPLETED_BY_FROM:
			case MERGE:
			case COMPLETED:
				d.getFrom().addConfirmedDebt(d);
				d.getTo().addConfirmedDebt(d);
				break;
			default:
				System.out.println("Failed loading debt: " + d);
			}
		}
	}
	
	/**
	 * INCOMPLETE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * 
	 * Saves ALL the given users (make sure you only send users that has been updated, or that are to be created)
	 * Note: Friends will only be saved if at least the user with the friend request is passed as argument TODO: Is this stupid?
	 * @param users			A list containing all the users to save
	 * @param passwords		A map on the following form: <user name, password>
	 * @throws SQLException
	 */
	public void save(List<User> users, Map<String, String> passwords) throws SQLException {
		for (User u : users) {
			// Check if this is a new user
//			if(st.executeQuery("SELECT " + FIELD_USER_ID + " FROM " + TABLE_USER + " WHERE " + FIELD_USER_ID + "=" + u.getId()).next()) {
			if(SQLHelper.exists(st, TABLE_USER, FIELD_USER_ID, u.getId() + "")) {
				// User already exists
				// TODO: What can be updated? Password?
			} else {
				// New user
				st.executeUpdate("INSERT INTO " + TABLE_USER + "(" + FIELD_USER_USERNAME + ", " + FIELD_USER_PASSWORD + 
						") VALUES (" + u.getUsername() + ", " + passwords.get(u.getUsername()) + ")");
			}
			// Save the friends (from the requests, since all friends must have sent a request some time)
			for (int i = 0; i < u.getNumberOfFriendRequests(); i++) {
				FriendRequest req = u.getFriendRequest(i);
				// Check if the request already exists
				if(SQLHelper.exists(st, TABLE_FRIEND_REQUEST, FIELD_FRIEND_REQUEST_ID, req.getId() + "")) {
					// Update the value that CAN change (no matter if it actually has changed)
					SQLHelper.update(st, TABLE_FRIEND_REQUEST, new String[]{FIELD_FRIEND_REQUEST_STATUS}, new String[]{req.getStatus().toString()}, FIELD_FRIEND_REQUEST_ID, req.getId() + "");
				} else {
					// If not, create it
					// TODO insert....
				}
			}
			// TODO ...
		}
	}
}
