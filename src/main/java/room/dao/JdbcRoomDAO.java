package room.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import room.model.Room;

public class JdbcRoomDAO implements RoomDAO{
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void insert(Room room) {
		String sql = "INSERT INTO ROOM " + 
				"(id, title, limit_num, current_num, is_playing) " +
				"VALUES (?, ?, ?, ?, ?)";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,  room.getId());
			ps.setString(2, room.getTitle());
			ps.setInt(3, room.getLimit_num());
			ps.setInt(4, room.getCurrent_num());
			ps.setInt(5,  room.getIs_playing());
			ps.executeUpdate();
			ps.close();
	
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
	
	public Set<Room> findAllRoom() {
		
		String sql = "SELECT * FROM ROOM";
		Room room = null;
		Set<Room> rooms = new HashSet<>();
		
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				room = new Room(
						rs.getInt("id"),
						rs.getString("title"),
						rs.getInt("limit_num"),
						rs.getInt("current_num"),
						rs.getInt("is_playing")
				);
				rooms.add(room);
			}
			rs.close();
			ps.close();
			System.out.println(Arrays.toString(rooms.toArray()));
			return rooms;
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
	
	public Room findByRoomId(int roomId) {
		
		String sql = "SELECT * FROM ROOM WHERE id = ?;";
		

		Room room = null;
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,  roomId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				room = new Room(
						rs.getInt("id"),
						rs.getString("title"),
						rs.getInt("limit_num"),
						rs.getInt("current_num"),
						rs.getInt("is_playing")
				);
				System.out.println("Before return" + room);
			}
			rs.close();
			ps.close();
			return room;
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
