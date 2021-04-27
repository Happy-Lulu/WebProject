package com.springstudy.ch03.declaration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/* Advice가 위빙(Weaving)될 대상 객체(Target)
 * @Component annotation을 사용해 이 클래스가 스프링 
 * 컴포넌트임을 선언하고 messageBean 이라는 이름을 지정하고 있다.
 **/
@Component("messageBean")
public class MessageBeanAspectJAnnotation {
		
	private String name;	
	
	@Autowired
	public void setName(String name) {
		this.name = name;
	}
	public void messageDisplay() {
		System.out.println(
				"messageBean의 messageDisplay() : 안녕하세요 " + name + "님!");
	}
	public void messageDisplay(String name) {
		System.out.println(
				"messageBean의 messageDisplay(name) : 안녕하세요 " + name + "님!");
	}
	public void messageHello() {
		System.out.println(
				"messageBean의 messageHello() : 안녕하세요 " + name + "님!");
	}
	public void messagePrint(String name) {
		System.out.println(
				"messageBean의 messageHello(name) : 안녕하세요 " + name + "님!");
	}
}
