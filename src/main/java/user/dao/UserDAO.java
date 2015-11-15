package user.dao;

import java.util.Set;

import room.model.Room;

public interface UserDAO {
	public void insert(Room room);
	public Set<Room> findAllRoom();
	public Room findByRoomId(int roomId);
}
