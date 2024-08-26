package com.example.LicenseManagement.exception;

public class EncryptedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EncryptedException(String message) {
		super(message);
	}

	public EncryptedException(String message,Throwable cause) {
		super(message,cause);
	}
}
