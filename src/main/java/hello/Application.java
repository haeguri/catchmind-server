package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import socket.ServerBackground;

// 외부 어플리케이션 서버로 배포하기 위해선 보통 "WAR" 파일처럼 이 서비스를 패키지화 하는 것이 가능해야 하지만,
// 아래에 설명된 더 간단한 접근법은 독립적인 어플리케이션을 만드는 것이다.
// 실행가능한 JAR 파일과 정말 오래된 자바 "main()" 메서드에 의해 당신은 모든것을 하나로 패키지화 한다.
// 더불어 당신은 외부 인스턴스로 반환하는 것 대신에 HTTP 런타임에 스프링의 Tomcat 서블릿 컨테이너를 내장하는 지원을 사용한다. 




@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
		ServerBackground server = new ServerBackground();
		server.setting();
		
		System.out.println("Let's inspect the beans provided by Spring Boot:");
	}
}