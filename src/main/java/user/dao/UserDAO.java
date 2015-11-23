package user.dao;

import java.util.Set;

import user.model.User;


public interface UserDAO {
	public User signupUser(User user);
	public User findByUsername(String username);
	public Set<User> findWatingUsers();
	public Set<User> findUsersInRoom(int roomId);
	public void enterRoom(int roomId, int userId);
	public void exitRoom(int roomId, int userId);
	
}
