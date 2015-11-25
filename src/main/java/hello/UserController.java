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
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST) 
	public ResponseEntity<User> loginUser(
			@RequestParam("username") String username,
			@RequestParam("password") String password) 
	{
		User user = userDAO.login(username, password);
		
		if(user != null) {
			if (user.getPassword().equals(password)) // 로그인 성공
				return new ResponseEntity<User>(user, responseHeaders, HttpStatus.OK);
			else // 로그인 실패 (입력한 password 다) 
				return new ResponseEntity<User>(null, responseHeaders, HttpStatus.NOT_FOUND);
		} else { // 로그인 실패 (입력한 username을 가진 사용자없음)
			return new ResponseEntity<User>(null, responseHeaders, HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/user/signup", method = RequestMethod.POST)
	public ResponseEntity<User> signupUser(
			@RequestParam("username") String username,
			@RequestParam("password") String password)
	{
		User user = userDAO.signup(username, password);
		return new ResponseEntity<User>(user, responseHeaders, HttpStatus.OK);
	}
	
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
