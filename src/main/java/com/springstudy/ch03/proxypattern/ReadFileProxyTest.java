package com.springstudy.ch03.proxypattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReadFileProxyTest {
	
	private static final Log logger = LogFactory.getLog(ReadFileProxyTest.class);
	
	public static void main(String[] args) {
		
		if(logger.isInfoEnabled()) {
			logger.debug("main() 시작");		
		}
		
		// 원본 객체를 생성하고 있다.
		ReadFile readFile1 = new ReadFileImpl("d:\\ReadFileImpl.txt");
		
		// 원본 객체의 프록시 객체를 생성하고 있다.
		ReadFile readFile2 = new ReadFileProxy("d:\\ReadFileProxy.txt");
		ReadFile readFile3 = new ReadFileProxy("d:\\ReadFileProxy.txt", readFile1);
		
		// 원본 객체의 메소드를 호출하고 있다.
		readFile1.fileDisplay();
		
		/* 프록시 객체의 메소드를 호출하고 있다.
		 * 프록시 객체의 메소드에서 대상 객체의 메소드를 호출하게 된다.
		 **/
		readFile2.fileDisplay();
		readFile3.fileDisplay();
		
		if(logger.isInfoEnabled()) {
			logger.debug("main() 종료");		
		}
	}
}
