package com.lablee.client.exception;

public class NewsNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NewsNotFoundException() {
		
	}
	
	public NewsNotFoundException(String message) {
		super(message);
	}

}
