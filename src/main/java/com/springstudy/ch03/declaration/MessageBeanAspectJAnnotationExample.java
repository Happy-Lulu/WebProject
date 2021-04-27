package com.springstudy.ch03.declaration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// @AspectJ 방식의 애노테이션을 이용한 AOP
public class MessageBeanAspectJAnnotationExample {
	
	public static void main(String[] args) {		

		/* 스프링 설정 파일에 정의한 bean을 생성해 빈 컨테이너에 담는다.
		 * 이 예제는 @AspectJ annotation 기반 bean 설정을 사용하므로 @Autowired와
		 * @Componrnt에 관련된 bean을 생성해 빈 컨테이너에 담는다.
		 **/
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"config/MessageAspectJAnnotationContext.xml");		
		
		/* 빈 컨테이너로 부터 클래스에 @Component 애노테이션이 설정된 
		 * messageBean이라는 id 또는 name을 가진 Advice 대상 객체(Target)의
		 * 프록시 객체를 얻어와 메시지를 출력한다.
		 **/
		MessageBeanAspectJAnnotation bean1 = 
				ctx.getBean("messageBean", MessageBeanAspectJAnnotation.class);
		
		System.out.println("## messageBean ##");
		bean1.messageDisplay();
		System.out.println("");
		bean1.messageDisplay("홍길동");
		System.out.println("");
		bean1.messageHello();
		System.out.println("");
		bean1.messagePrint("강감찬");
		System.out.println();
		
		/* 빈 컨테이너로 부터 클래스에 @Component 애노테이션이 설정된 
		 * messageAnnotation이라는 id 또는 name을 가진 Advice 대상 객체(Target)의
		 * 프록시 객체를 얻어와 메시지를 출력한다.
		 **/
		MessageBeanAnnotation bean2 = 
				ctx.getBean("messageAnnotation", MessageBeanAnnotation.class);
		
		System.out.println("## messageAnnotation ##");
		bean2.messageDisplay();
		System.out.println("");
		bean2.messageDisplay("홍길동");
		System.out.println("");
		bean2.messageHello();
		System.out.println("");
		bean2.messagePrint("강감찬");
	}
}
