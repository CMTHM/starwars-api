package com.example.starwars.exception;

//Custom exception for when a resource is not found
public class ResourceNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}

//Custom exception for general service errors
 class ServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
