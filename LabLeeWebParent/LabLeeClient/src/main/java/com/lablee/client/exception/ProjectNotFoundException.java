package com.lablee.client.exception;

public class ProjectNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ProjectNotFoundException() {
		
	}
	
	public ProjectNotFoundException(String message) {
		super(message);
	}

}
