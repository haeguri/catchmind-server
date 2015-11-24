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

	// /greeting에 대한 요청들을 greeting() 메서드로 매핑된다.
	// @RequestMapping GET, PUT, POST 등의 메서드들을 가리지 않는다.
	// @RequestMapping(method=GET)과 같이 쓸 수도 있다.
	// @RequestMapping("/greeting")
	// public Greeting greeting(@RequestParam(value="name",
	// defaultValue="World") String name)
	// {
	// Set<Room> rooms = roomDAO.findAllRoom();
	// return new Greeting(counter.incrementAndGet(),
	// String.format(template, name));
	// }

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

	@RequestMapping(value = "/rooms/{roomId}", method = RequestMethod.GET)
	public Room getRoomById(@PathVariable("roomId") int roomId) {
		
		Room room1 = roomDAO.findByRoomId(roomId);
		return room1;
	}

	@RequestMapping(value = "/rooms/{roomId}/users", method = RequestMethod.GET)
	public ResponseEntity<Set<User>> findUsersInRoom(@PathVariable("roomId") int roomId) {
		Set<User> users = userDAO.findUsersInRoom(roomId);
		
		return new ResponseEntity<Set<User>>(users, responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/room/make", method = RequestMethod.POST)
	public Room makeRoom(
			@RequestParam("title") String title,
			@RequestParam("limit_num") int limit_num,
			@RequestParam("host") int host_id)
		{
		Room room = roomDAO.makeRoom(new Room(0, title, limit_num, 1, 0));
//		User(Host)의 current_room을 생성했던 방의 id로 업데이트해준다.
		userDAO.enterRoom(room.getId(), host_id);
		return room;
	}
	
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
//		입장한 방의 id를 사용자의 current_room 필드에 저장한다.
	}
	
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
	
	

	// @RequestParam은 쿼리 스트링 파라메터 "name"의 값을 greeting() 메서드의 "name" 파라메터로 바인드한다.
	// 이 쿼리 스트링 파라메터는 "required"가 아니다. 만약 요청에서 없다면 World가 "defaultValue"로 사용된다.

	// 메소드 본문의 구현은 counter로부터 오는 다음 값에 기반해 새로운 "Greeting"객체를 id와 content 속성들로
	// 생성하고 반환하
	// greeting "template"을 사용함으로써 주어진 "name"을 형식화한다.

	// 일반적인 MVC 컨트롤러와 위와 같은 RESTful 웹 서비스 컨트롤러의 가장 큰 차이점은 HTTP response body가
	// 생성되는 방법에 있다.
	// greeting 데이터의 서버-측 렌더링을 HTML로 수행하기위해 view technology에 의존하기 보다
	// 이 RESTful 웹 서비스 컨트롤러는 단순히 "Greeting" 객체를 할당(Populates)하고 반환한다.
	// 객체 데이터는 JSON으로 HTTP response에 바로 작성될 것이다.

	// 이 코드는 스프링의 네 번째로 나온 "@RestController" 어노테이션을 사용한다.
	// 이것은 클래스를 모든 메서드가 뷰 대신 도메인 객체를 반환하는 컨트롤러로서 표시한다.
	// 이것은 "@Controller"와 "@ResponseBody" 역할을 동시에 한.

	// "Greeting" 객체는 JSON으로 변환되어야 한다. 스프링의 HTTP 메세지 변환 지원에 감사하며, 당신은 이 변환을 신경 쓸
	// 필요가 없다.
	// 왜냐하면 "Jackson 2"는 클래스패스이고, 스프링의 "MappingJackson2HttpMessageConverter"는
	// 자동으로 "Greeting" 인스턴스를 JSON으로 변환하는 것을 선택하기 떄문이다.

}