package hello;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import question.dao.QuestionDAO;
import question.model.Question;

@RestController
public class QuestionController {
	private static final ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
	private static final QuestionDAO questionDAO = (QuestionDAO) context.getBean("questionDAO");
	private static final HttpHeaders responseHeaders = new HttpHeaders();

	@RequestMapping(value = "/question", method = RequestMethod.GET)
	public ResponseEntity<Question> getQuestion() {
		Question question = questionDAO.getQuestion();

		return new ResponseEntity<Question>(question, responseHeaders, HttpStatus.OK);
	}

}