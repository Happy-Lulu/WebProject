package com.springstudy.ch03.declaration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/* @Component annotation을 사용해 이 클래스가 스프링 컴포넌트임을
 * 선언하고 @Aspect annotation을 사용해 이 클래스를 애스펙트로 지정하고 있다.
 * 
 * 이 클래스는 AspectJ 기반의 annotation을 사용해 aop 네임스페이스를 
 * 사용할 때와 동일한 애스펙트를 구현하고 있지만 스프링은 어드바이스 대상 메소드에
 * 어드바이스를 적용할 때 AspectJ 위빙 메커니즘을 사용하는 것이 아니라 스프링의
 * 자체 프록시 메커니즘을 사용해 대상 객체의 프록시를 생성한다는 것을 기억해야 한다.
 * 
 * 이 클래스에서 사용하는 JoinPoint, ProceedingJoinPoint 클래스는
 * org.aspectj.lang 패키지의 클래스이고 @Aspect, @Pointcut, @Around, 
 * @Before, @After, @AfterReturning, @AfterThrowing 등의 애노테이션은
 * org.aspectj.lang.annotation 패키지에 커스텀 애노테이션을 정의한 인터페이스이므로 
 * AspectJ 런타임 라이브러리와 위빙 라이브러리인 aspectjrt-1.8.7.jar와 
 * aspectjweaver-1.8.7.jar를 Maven을 통해 라이브러리 의존성을 설정 하였다.
 * 
 * 스프링 AOP에서 애스펙트는 다른 애스펙트의 어드바이스 타겟이 될 수 없다.
 * 클래스에 @Aspect 애노테이션을 적용하게 되면 이 클래스가 애스펙트라는
 * 것을 스프링에게 알려주므로 스프링은 이 클래스를 오토프로싱에서 제외시킨다.
 * 
 * ## AspectJ execution() 지정자를 사용한 포인트컷 설정하기 ##
 * AspectJ는 포인트컷을 지정할 수 있는 다양한 지정자(Pointcut Designator)를
 * 제공하는데 스프링은 메소드 호출과 관련된 지정자만 지원하고 있어 AspectJ의
 * 포인트컷 지정자 중	메소드 호출과 관련된 지정자만 사용할 수 있다.
 * 이 포인트컷 표현식은 AspectJ 포인트컷 표현식이 사용되는 ProxyFactoryBean을
 * 이용한 AOP 구현, aop 네임스페이스를 이용한 AOP구현, AspectJ 애노테이션을
 * 이용한 AOP 구현 등에서 포인트컷을 설정하는데 사용할 수 있다.
 * execution() 포인트컷 지정자는 Advice를 적용할 메소드를 매치시키는데 사용되며
 * 이 지정자를 사용해 포인트컷을 설정하는 방법은 다음과 같다.
 * 
 * execution(메소드접근지정자v메소드리턴타입v패키지.클래스명.메소드이름(파라미터))v예외타입
 * 
 * - v : 공백을 의미한다.
 * - 메소드 접근지정자, 예외타입, 패키지.클래스명은 생략할 수 있다.
 * - 메소드 리턴 타입, 패키지, 클래스명, 메소드 이름에는 와일드카드(*)를 사용할 수 있다.
 * - 와일드카드(*)는 패키지 구분자(.)와 일치하지 않으므로 여러 패키지와 일치 시키려면
 *  .. 을 사용해 현재 패키지와 하부 패키지를 지정할 수 있다.
 * - 메소드 인수에 (..)를 지정하면 파라미터가 0개 이상인 메소드와 일치된다.
 * - 메소드 인수에(*)를 지정하면 파라미터가 1개인 모든 메소드와 일치된다.
 *  메소드 인수에 *를 지정하면 모든 타입의 파라미터와 일치된다.
 *
 * - execution(protected String *Print())
 *  리턴 타입이 String 이고 이름이 Print로 끝나며 파라미터가 없는 메소드가 일치된다.
 *
 * - execution(public void *Display(..))
 *  리턴 타입이 void이고 이름이 Display로 끝나며 0개 이상의 파라미터를 가진 메소드가
 *  일치된다.
 *
 * - execution(* com.springstudy.ch03.*.set*(*))
 *  리턴 타입이 어떤 것이든 상관없고 com.springstudy.ch03 패키지의 모든 클래스에
 *  정의된 이름이 set으로 시작하며 1개의 파라미터를 가진 메소드가 일치된다. 
 *  파라미터 타입은 무엇이든 상관없다.
 *
 * - execution(* com.springstudy.ch03.MessageBean.*(java.lang.String, *))
 *  리턴 타입이 어떤 것이든 상관없고 com.springstudy.ch03.MessageBean 클래스에
 *  정의된 두 개의 파라미터를 가진 메소드 중 첫 번째 파라미터가 String 타입인 메소드가 
 *  일치된다. 두 번째 파라미터 타입은 무엇이든 상관없다.
 * 
 * - execution(* com.springstudy.ch03.*.*Print(*, *))
 *  리턴 타입이 어떤 것이든 상관없고 com.springstudy.ch03 패키지의 모든 클래스에
 *  정의된 이름이 Print로 끝나며 2개의 파라미터를 가진 메소드가 일치된다. 
 *  파라미터 타입은 무엇이든 상관없다.
 * 
 * - execution(* com.springstudy.ch03.*.*(Integer, ..))
 *  리턴 타입이 어떤 것이든 상관없고 com.springstudy.ch03 패키지의 모든 클래스에
 *  정의된 메소드 중 첫 번째 파라미터 타입이 Integer이며 1개 이상의 파라미터를 가진
 *  메소드가 일치된다.
 *  
 * ## AspectJ within() 지정자를 사용한 포인트컷 설정하기 ##
 * within() 포인트컷 지정자는 단순히 메소드 이름으로 매치하는 것이 아닌
 * 특정 타입에 정의한 메소드로 포인트컷을 제한하는데 사용하는 포인트컷
 * 지정자로 이 지정자를 사용해 포인트컷을 설정하는 방법은 다음과 같다.
 * 
 * - within(com.springstudy.ch02.member.MemberService)
 *   MemberService 인터페이스의 모든 메소드가 매치된다.
 *   
 * - within(com.springstudy.ch02.member.*)
 *   com.springstudy.ch02.member 패키지에 있는 모든 메소드가 매치된다.
 *   
 * - within(com.springstudy.ch02.member..*)
 *  com.springstudy.ch02.member 패키지와 그 하위 패키지에 있는
 *  모든 메소드가 매치된다.
 *  
 * ## 스프링 bean() 지정자를 사용한 포인트컷 설정하기 ##
 * bean() 포인트컷 지정자는 스프링 2.5 부터 스프링에서 추가적으로
 * 지원하는 포인트컷 지정자로 스프링 빈 이름(id나 이름)을 사용해 포인트컷을 설정하는데
 * 사용하는 포인트컷 지정자 이다. bean() 포인트컷 지정자에 지정한 이름을 가진 빈으로
 * 포인트컷을 제한하는데 사용되며 이 지정자를 사용해 포인트컷을 설정하는 방법은 다음과 같다.
 * 
 * - bean(memberService)
 *   빈의 이름이 memberService인 빈의 모든 메소드가 매치된다.
 *   
 * - bean(*Service)
 *  빈의 이름이 Service로 끝나는 빈의 모든 메소드가 매치된다.
 *  
 * 이외에도 this(), args(), target(), @args(), @target(), @within(), @annotation()등의
 * 포인트컷 지정자를 사용해 포인트컷을 설정할 수 있으며 논리 연산자[and(&&), or(||), 
 * not(!)]를 사용해 여러 개의 포인트컷 지정자를 조합하여 포인트컷을 설정할 수 있다.  
 **/
@Component
@Aspect
public class MessageBeanAspectJAnnotationAspect {
	
	private final static Log logger = 
			LogFactory.getLog(MessageBeanAspectJAnnotationAspect.class);
	
	/* AspectJ 기반의 애노테이션을 사용해 argument 체크 포인트컷을 정의하고 있다.
	 * 아래의 포인트컷 표현식을 보면 aop 네임스페이스에서 사용한 and 연산자를 사용한 
	 * 것이 아니라 && 연산자를 사용해 AND 연산을 한다는 것에 주의를 기울여야 한다. 
	 * 
	 * com.springstudy.ch03.declaration 패키지 하위에 정의된 클래스
	 * (하위 패키지 포함)의 String 타입의 매개 변수가 하나인 Display로 끝나는
	 * 모든 메소드에 어드바이스를 적용하는 포인트컷을 정의하고 있다.
	 * 또한 && 연산자와 args() 포인트컷 지정자를 사용해 어드바이스에 name이라는
	 * 이름의 인수를 넘겨주도록 지정하고 있다.
	 * args 표현식에서 타입 이름 즉 args(MessageBean) 대신 파라미터
	 * 이름을 사용하면 어드바이스를 호출할 때 args 표현식에 지정한 인수명과
	 * 이름이 같은 어드바이스의 매개변수로 값이 전달된다.
	 * args 표현식에 지정한 인수의 이름과 동일한 파라미터의 이름이 어드바이스에
	 * 반드시 존재해야 하며 그렇지 않으면 IllegalArgumentException이 발생한다.
	 * 
	 * 애스펙트 클래스에서 하나의 포인트컷은 아래와 같이 하나의 메소드로 정의할 수
	 * 있으며 포인트컷의 이름은 포인트컷으로 정의한 메소드 이름을 사용하면 된다.
	 **/	
	@Pointcut("execution("
			+ "* com.springstudy.ch03.declaration..*Display(String)) && args(name)")
	public void argPointcut(String name) { }
	
	/* AspectJ 기반의 애노테이션을 사용해 포인트컷을 정의하고 있다.
	 * bean() 포인트컷 지정자를 사용해 messageBean으로 시작하는
	 * id를 가진 빈에게 만 어드바이스를 적용하는 포인트컷을 정의하고 있다.   
	 **/	
	@Pointcut("bean(messageBean*)")
	public void beanScopePointcut() { }	
	
	/* default 비포 어드바이스를 정의하고 포인트컷 연결
	 * AspectJ 기반의 애노테이션을 사용해 Before Advice를 정의하고 있다.
	 * com.springstudy.ch03.declaration 패키지 하위에 정의된 클래스
	 * 	(하위 패키지 포함)의 String 타입의 매개 변수가 하나인 Display로 끝나는
	 * 모든 메소드에 어드바이스를 적용하는 포인트컷을 연결하고 있다.
	 * 
	 * @Around, @Before, @After, @AfterReturning, @AfterThrowing 등의
	 * 애노테이션 인수에 별도로 선언된 포인트컷을 연결할 수도 있고 아래와 같이
	 * AspectJ 포인트컷 지정자를 사용해 바로 포인트컷을 연결할 수도 있다.
	 **/
	@Before("execution(* com.springstudy.ch03.declaration..*Display(String))")
	public void messageBeforeAdvice(JoinPoint joinPoint) throws Throwable{
		
		if(logger.isDebugEnabled()) {
			logger.debug("Before - default : " 
					+ joinPoint.getSignature().getDeclaringTypeName()
					+ "." + joinPoint.getSignature().getName());			
		}
	}
	
	/* argument 체크 비포 어드바이스를 정의하고 포인트컷 연결
	 * AspectJ 기반의 애노테이션을 사용해 Before Advice를 정의하고 있다.
	 * 위에서 정의한 포인트컷을 사용해 어드바이스에 포인트컷을 연결하고 있다.
	 **/
	@Before("argPointcut(name) && beanScopePointcut()")
	public void messageBeforeAdvice(
			JoinPoint joinPoint, String name)  throws Throwable{
		
		if(name.equals("강감찬")) {
			
			if(logger.isDebugEnabled()) {
				logger.debug("Before - name : " 
					+ joinPoint.getSignature().getDeclaringTypeName()
					+ "." + joinPoint.getSignature().getName() + " - arg : " + name);
			}
		}
	}
	
	/* 어라운드 어드바이스를 정의하고 포인트컷 연결
	 * AspectJ 기반의 애노테이션을 사용해 Around Advice를 정의하고 있다.
	 * 대상 객체에서 String 타입의 매개 변수가 하나인 message로 시작하는
	 * 모든 메소드에 어드바이스를 적용하는 포인트컷을 연결하고 있다.
	 * 그리고 && 연산자와 args() 포인트컷 지정자를 사용해 어드바이스에
	 * name이라는 이름의 인수를 넘겨주도록 지정하고 있으며 위에서 포인트컷으로
	 * 선언한 beanScopePointcut()을 지정해 messageBean으로 시작하는
	 * id를 가진 빈에게 만 어드바이스가 적용되도록 지정하고 있다.
	 * args 표현식에서 타입 이름 즉 args(MessageBean) 대신 파라미터
	 * 이름을 사용하면 어드바이스를 호출할 때 args 표현식에 지정한 인수명과
	 * 이름이 같은 어드바이스의 매개변수로 값이 전달된다.
	 * args 표현식에 지정한 인수의 이름과 동일한 파라미터의 이름이 어드바이스에
	 * 반드시 존재해야 하며 그렇지 않으면 IllegalArgumentException이 발생한다. 
	 **/
	@Around("execution(* message*(String)) "
			+ "&& args(name) && beanScopePointcut()")
	public Object messageAroundAdvice(
			ProceedingJoinPoint joinPoint, String name)  throws Throwable{
		
		Object obj = null;
		if(logger.isDebugEnabled()) {
			logger.debug("Around - Before: " 
					+ joinPoint.getSignature().getDeclaringTypeName()
					+ "." + joinPoint.getSignature().getName());
		}		
		
		/* ProceedingJoinPoint 객체를 통해 호출되는 메소드, 대상 조인포인트, 
		 * AOP 프록시, 메소드의 인수에 대한 정보를 얻을 수 있고 이 객체를 사용하면
		 * 메소드 호출이 실행되는 시점도 제어할 수 있다. 		
		 * 매개변수로 받은 ProceedingJoinPoint 객체의 proceed()를 호출해야 
		 * 조인 포인트의 메소드가 호출되고 그 메소드의 반환 값을 얻을 수 있다. 
		 * 조인 포인트의 메소드 반환 타입이 void면 null이 반환 된다. 
		 **/
		obj = joinPoint.proceed();		
		
		if(logger.isDebugEnabled()) {
			logger.debug("Around - After : " 
					+ joinPoint.getSignature().getDeclaringTypeName()
					+ "." + joinPoint.getSignature().getName());
		}
		
		// Around Advice는 proceed() 메서드의 반환 값을 반환해야 한다.
		return obj;
	}
}
