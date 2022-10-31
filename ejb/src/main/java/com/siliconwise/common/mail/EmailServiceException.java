
package com.siliconwise.common.mail;

/**
 * @author GNAKALE Bernardin
 *
 */
public class EmailServiceException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EmailServiceException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public EmailServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public EmailServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EmailServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public EmailServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
