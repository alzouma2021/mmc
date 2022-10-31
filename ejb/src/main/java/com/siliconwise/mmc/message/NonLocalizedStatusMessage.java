/**
 * 
 */
package com.siliconwise.mmc.message;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author GNAKALE Bernardin
 *
 */
public class NonLocalizedStatusMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private StatusMessageType status = StatusMessageType.INFO ;
	
	private String message = null;

	public NonLocalizedStatusMessage() {
		super() ;
	}

	public NonLocalizedStatusMessage(StatusMessageType status, String message) {
		super();
		this.status = status;
		setMessage(message);
	}

	public StatusMessageType getStatus() {
		return status;
	}

	public void setStatus(StatusMessageType status) {
		this.status = status;
	}
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static boolean isThereAtLeastAnUnsuccessfullMessage(Collection<NonLocalizedStatusMessage> msgList) {
		
		if (msgList == null) return false ;
		
		for (NonLocalizedStatusMessage msg : msgList){
			if (msg.status == StatusMessageType.ERROR || msg.status == StatusMessageType.FATAL)
				return true ;
		}
		
		return false ;
	}

	@Override
	public String toString() {
		return status + ": " + message;
	}
}
