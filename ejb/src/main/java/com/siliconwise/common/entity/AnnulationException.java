/**
 * 
 */
package com.siliconwise.common.entity;

/**
 * Probleme d'annulation
 * @author GNAKALE Bernardin
 *
 */
public class AnnulationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AnnulationException() {
	}

	/**
	 * @param arg0
	 */
	public AnnulationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public AnnulationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public AnnulationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public AnnulationException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
