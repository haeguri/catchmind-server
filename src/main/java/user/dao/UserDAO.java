package user.dao;

import java.util.Set;

import user.model.User;


public interface UserDAO {
	public User signupUser(User user);
	public User getUser(int userId);
	public Set<User> findWatingUsers();
	public Set<User> findUsersInRoom(int roomId);
	public void enterRoom(int roomId, int userId);
	public void exitRoom(int roomId, int userId);
	
}
