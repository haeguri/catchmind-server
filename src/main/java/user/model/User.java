package user.model;

public class User {
	int id;
	String username;
	String password;
	int current_room;
	
	public User(int id, String username, String password, int current_room) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.current_room = current_room;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", current_room=" + current_room
				+ "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCurrent_room() {
		return current_room;
	}

	public void setCurrent_room(int current_room) {
		this.current_room = current_room;
	}
		
	
}
