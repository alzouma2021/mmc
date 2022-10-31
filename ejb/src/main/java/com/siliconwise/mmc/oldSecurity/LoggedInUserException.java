/**
 * 
 */
package com.siliconwise.mmc.oldSecurity;

/**
 * Aucun utilisteur connecté
 * @author GNAKALE Bernardin
 *
 */
public class LoggedInUserException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoggedInUserException() {
	}

	/**
	 * @param arg0
	 */
	public LoggedInUserException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public LoggedInUserException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LoggedInUserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public LoggedInUserException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
	

}
