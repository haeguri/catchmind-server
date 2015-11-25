package user.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import user.model.User;

public class JdbcUserDAO implements UserDAO{
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public User login(String username, String password) {
		String sql = "SELECT * FROM user WHERE username = ? and password = ?;";
		
		Connection conn = null;
		User user = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("rs.next() is true.");
				user = new User(
						rs.getInt("id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getInt("current_room")
				);
				System.out.println("Before return" + user);
				rs.close();
				ps.close();
				return user;
			} else {
				System.out.println("rs.next() is false.");
				rs.close();
				ps.close();
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	public User signupUser(User user) {
		String sql = "INSERT INTO user " + 
				"(username, password) " +
				"VALUES (?, ?)";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				user.setId(rs.getInt(1));
			}
			rs.close();
			ps.close();
			
			return user;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
			
		}
	}
	
	public Set<User> findWatingUsers() {
		
		String sql = "SELECT * FROM user WHERE current_room IS NULL";
		User user = null;
		Set<User> users = new HashSet<>();
		
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				user = new User(
						rs.getInt("id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getInt("current_room")
				);
				users.add(user);
			}
			rs.close();
			ps.close();
			System.out.println(Arrays.toString(users.toArray()));
			return users;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	public User getUser(int userId) {
		
		String sql = "SELECT * FROM user WHERE id = ?;";
		
		User user = null;
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("rs.next() is true.");
				user = new User(
						rs.getInt("id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getInt("current_room")
				);
				System.out.println("Before return" + user);
				rs.close();
				ps.close();
				return user;
			} else {
				System.out.println("rs.next() is false.");
				rs.close();
				ps.close();
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	public void enterRoom(int roomId, int userId) {
		String sql = "UPDATE user SET current_room = ? WHERE id = ?";
		
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, roomId);
			ps.setInt(2, userId);
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}	
	}
	
	public void exitRoom(int roomId, int userId) {
		String sql = "UPDATE user SET current_room = NULL WHERE id = ?";
		
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}	
	}
	
	

	public Set<User> findUsersInRoom(int roomId) {
		
		String sql = "SELECT * FROM user where current_room = ?";
		User user = null;
		Set<User> users = new HashSet<>();
		
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, roomId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				user = new User(
						rs.getInt("id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getInt("current_room")
				);
				users.add(user);
			}
			rs.close();
			ps.close();
			System.out.println(Arrays.toString(users.toArray()));
			return users;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	
}
