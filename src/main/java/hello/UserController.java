package hello;

import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import room.model.Room;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


import user.dao.UserDAO;
import user.model.User;

@RestController
public class UserController {
	private static final ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
	private static final UserDAO userDAO = (UserDAO) context.getBean("userDAO");
	private static final HttpHeaders responseHeaders = new HttpHeaders();
	
//	사용자 이름, 암호를 받아 로그인을 처리하는 함수.
	@RequestMapping(value = "/user/login", method = RequestMethod.POST) 
	public ResponseEntity<User> loginUser(
			@RequestParam("username") String username,
			@RequestParam("password") String password) 
	{
		User user = userDAO.login(username, password);
		
		if(user != null && user.getPassword().equals(password)) {
			return new ResponseEntity<User>(user, responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(null, responseHeaders, HttpStatus.NOT_FOUND);
		}
	}
	
//	사용자 이름, 암호를 받아 회원가입을 처리해주는 함수.
	@RequestMapping(value = "/user/signup", method = RequestMethod.POST)
	public ResponseEntity<User> signupUser(
			@RequestParam("username") String username,
			@RequestParam("password") String password)
	{
		User user = userDAO.signup(username, password);
		return new ResponseEntity<User>(user, responseHeaders, HttpStatus.OK);
	}
	
//	로비에 대기중인 사용자의 목록을 반환하는 함수.
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<Set<User>> getUsers() {
		System.out.println(userDAO.findWatingUsers());
		
		Set<User> users = userDAO.findWatingUsers();
		if( users.isEmpty() == false) {
			return new ResponseEntity<Set<User>> (users, responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<Set<User>> (users, responseHeaders, HttpStatus.NOT_FOUND);
		}
	}
	
//	특정 사용자의 정보를 반환하는 함수.
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(
			@PathVariable("userId") int userId) 
	{
		System.out.println(userDAO.getUser(userId));
		
		User user = userDAO.getUser(userId);
		if( user != null) {
			return new ResponseEntity<User> (user, responseHeaders, HttpStatus.OK);
		} else {
			return new ResponseEntity<User> (null, responseHeaders, HttpStatus.NOT_FOUND);
		}
	}
}
