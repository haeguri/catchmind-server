package room.dao;

import java.util.Set;

import room.model.Room;

public interface RoomDAO {
	public Room makeRoom(Room room);
	public Set<Room> findWatingRoom();
	public Room findByRoomId(int roomId);
	public Boolean enterRoom(int room_id);
	public Boolean hostExitRoom(int room_id);
	public void memberExitRoom(int room_id);
}
