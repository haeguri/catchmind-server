package room.dao;

import java.sql.Connection;
import java.sql.Statement;
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
	
	public Room makeRoom(Room room) {
		String sql = "INSERT INTO ROOM " + 
				"(title, limit_num, current_num, is_playing) " +
				"VALUES (?, ?, ?, ?)";
				
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, room.getTitle());
			ps.setInt(2, room.getLimit_num());
			ps.setInt(3, room.getCurrent_num());
			ps.setInt(4,  room.getIs_playing());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				room.setId(rs.getInt(1));
			}
			rs.close();
			ps.close();
			
			return room;
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
	
	public Set<Room> findWatingRoom() {
		
		String sql = "SELECT * FROM room WHERE is_playing=0";
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
	
	public Boolean enterRoom(int roomId){
		String selectSql = "SELECT * FROM room WHERE id=?";
		String updateSql = "update room SET current_num = current_num+1 WHERE id = ?";
		Connection conn = null;
		
//		우선 입장하려는 방의 정보를 받아오고, current_num(현재인원)이 limit_num(제한인원)보다 크거나 같으면 false를 반환한다.  
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(selectSql);
			ps.setInt(1,  roomId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt("current_num") >= rs.getInt("limit_num"))
					return false;
			};
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
//		current_num이 limit_num보다 작을 때에만 방의 current_num 업데이트하고 true를 반환한다.
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(updateSql);
			ps.setInt(1,  roomId);
			ps.executeUpdate();
			ps.close();
			return true;
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
	
	public Boolean hostExitRoom(int roomId) {
		String deleteSql = "DELETE FROM room WHERE id = ?";
		Connection conn = null;
		
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(deleteSql);
			preparedStatement.setInt(1,  roomId);
			System.out.println("before");
			int result = preparedStatement.executeUpdate();
			System.out.println("after");
			if(result != 0) {
				System.out.println("Room delete successfully. @@" + result);
				preparedStatement.close();
				return true;
			} else {
				System.out.println("Room cannot delete.@@" + result);
				preparedStatement.close();
				return false;
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
	
	public void memberExitRoom(int roomId) {
		String updateSql = "UPDATE room SET current_num = current_num-1 WHERE id = ?";
		Connection conn = null;
		
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(updateSql);
			preparedStatement.setInt(1,  roomId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			
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
