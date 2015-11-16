package user.dao;

import java.util.Set;

import user.model.User;


public interface UserDAO {
	public User signupUser(User user);
	public Set<User> findAllUser();
	public User findByUsername(String username);
}
