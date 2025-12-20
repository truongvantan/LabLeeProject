package com.lablee.admin.exception;

public class NewsNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NewsNotFoundException() {
		
	}
	
	public NewsNotFoundException(String message) {
		super(message);
	}

}
