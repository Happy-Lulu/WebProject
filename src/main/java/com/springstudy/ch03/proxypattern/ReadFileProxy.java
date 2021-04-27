package com.springstudy.ch03.proxypattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReadFileProxy implements ReadFile {
	
	// 프록시 객체는 대상 객체의 프로퍼티와 동일한 타입의 프로퍼티를 갖는다.
	private String filePath;
	
	/* 프록시 객체 내부에 대상 객체와 동일한 타입의 프로퍼티를 갖는다.
	 * 대상 객체는 프록시 객체 안에서 직접 생성할 수 있거나 외부로 부터
	 * 공급 받을 수 있도록 구현해야 대상 객체의 메소드에 접근 할 수 있다.
	 **/
	private ReadFile readFile;
	
	private static final Log logger = LogFactory.getLog(ReadFileProxy.class);
	
	public ReadFileProxy(String filePath) {
		this.filePath = filePath;
	}
	
	// 대상 객체를 외부로 부터 공급 받을 수 있는 생성자
	public ReadFileProxy(String filePath, ReadFile readFile) {
		this.filePath = filePath;
		this.readFile = readFile;		
	}
	
	public void fileDisplay() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Proxy fileDisplay() 시작");		
		}
		
		if(readFile == null) {
			// 대상 객체가 생성되지 않았다면 직접 생성한다.
			readFile = new ReadFileImpl(filePath);
		}
		readFile.fileDisplay();
		
		if(logger.isDebugEnabled()) {
			logger.debug("Proxy fileDisplay() 종료");
		}
	}
}
