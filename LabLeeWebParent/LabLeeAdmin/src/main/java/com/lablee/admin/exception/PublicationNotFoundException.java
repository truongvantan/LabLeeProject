package com.lablee.admin.exception;

public class PublicationNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PublicationNotFoundException() {
		
	}
	
	public PublicationNotFoundException(String message) {
		super(message);
	}
}
