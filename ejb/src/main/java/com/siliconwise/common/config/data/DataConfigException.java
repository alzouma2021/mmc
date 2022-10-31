/**
 * 
 */
package com.siliconwise.common.config.data;

/**
 * Erreur dans la config des données du système
 * @author GNAKALE Bernardin
 *
 */
public class DataConfigException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataConfigException() {
		super();
	}

	public DataConfigException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public DataConfigException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DataConfigException(String arg0) {
		super(arg0);
	}

	public DataConfigException(Throwable arg0) {
		super(arg0);
	}

	
}
