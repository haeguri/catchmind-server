package user.dao;

import java.util.Set;

import user.model.User;


public interface UserDAO {
	public User login(String username, String password);
	public User signup(String username, String password);
	public User getUser(int userId);
	public Set<User> findWatingUsers();
	public Set<User> findUsersInRoom(int roomId);
	public void enterRoom(int roomId, int userId);
	public void exitRoom(int roomId, int userId);
	
}
