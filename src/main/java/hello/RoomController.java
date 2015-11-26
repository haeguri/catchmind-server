package hello;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import room.dao.RoomDAO;
import user.dao.UserDAO;
import room.model.Room;
import user.model.User;

import java.util.Set;
import java.util.Map;

@RestController
public class RoomController {
	private static final ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
	private static final RoomDAO roomDAO = (RoomDAO) context.getBean("roomDAO");
	private static final UserDAO userDAO = (UserDAO) context.getBean("userDAO");
	private static final HttpHeaders responseHeaders = new HttpHeaders();

//	로비에 대기중인 방의 목록을 반환하는 함수. 
	@RequestMapping(value = "/rooms", method = RequestMethod.GET)
	public ResponseEntity<Set<Room>> getRooms() {
		System.out.println(roomDAO.findWatingRoom());
		
		Set<Room> rooms = roomDAO.findWatingRoom();
		if( rooms.isEmpty() == false) {
			return new ResponseEntity<Set<Room>> (rooms, responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<Set<Room>> (null, responseHeaders, HttpStatus.NOT_FOUND);
		}
	}

//	특정 방을 반환하는 함수.
	@RequestMapping(value = "/rooms/{roomId}", method = RequestMethod.GET)
	public Room getRoomById(@PathVariable("roomId") int roomId) {
		
		Room room1 = roomDAO.findByRoomId(roomId);
		return room1;
	}

//	특정 방에 입장해있는 사용자를 반환하는 함수.
	@RequestMapping(value = "/rooms/{roomId}/users", method = RequestMethod.GET)
	public ResponseEntity<Set<User>> findUsersInRoom(@PathVariable("roomId") int roomId) {
		Set<User> users = userDAO.findUsersInRoom(roomId);
		
		return new ResponseEntity<Set<User>>(users, responseHeaders, HttpStatus.OK);
	}
	
//	데이터를 받아 방을 만든 후, 그 방을 반환하는 함수.
	@RequestMapping(value = "/room/make", method = RequestMethod.POST)
	public Room makeRoom(
			@RequestParam("title") String title,
			@RequestParam("limit_num") int limit_num,
			@RequestParam("host") int host_id)
		{
		Room room = roomDAO.makeRoom(new Room(0, title, limit_num, 1, 0));
		userDAO.enterRoom(room.getId(), host_id);
		return room;
	}
	
//	데이터를 받아 방 입장을 처리해주는 함수.
	@RequestMapping(value = "/room/enter", method = RequestMethod.POST)
	public ResponseEntity<Object> enterRoom(
			@RequestParam("user_id") int user_id,
			@RequestParam("room_id") int room_id)
	{
		if(roomDAO.enterRoom(room_id)) {
			System.out.println("true");
			userDAO.enterRoom(room_id, user_id);
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);
		}else {
			System.out.println("false");
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}
	
//	데이터를 받아 방 퇴장을 처리해주는 함수.
	@RequestMapping(value = "/room/exit", method = RequestMethod.POST)
	public ResponseEntity<Object> exitRoom(
			@RequestParam("user_id") int user_id,
			@RequestParam("room_id") int room_id,
			@RequestParam("is_host") Boolean is_host)
	{
		System.out.println("room exit" + is_host);
		userDAO.exitRoom(room_id, user_id);
		if (is_host) {
			roomDAO.hostExitRoom(room_id);
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.NO_CONTENT);
		} else {
			roomDAO.memberExitRoom(room_id);
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);
		}
	}

}