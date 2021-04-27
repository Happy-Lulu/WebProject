package com.springstudy.ch03.proxypattern;


public class ReadFileImpl implements ReadFile{
	
	private String filePath;
	
	public ReadFileImpl(String filePath) {
		this.filePath = filePath;
		loadFileFromDisk();
	}	
	
	public void loadFileFromDisk() {
		System.out.println(filePath + " 파일 읽는 중...");
	}
	
	public void fileDisplay() {
		System.out.println(filePath + " 파일 표시 중...");
	}
}
