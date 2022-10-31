/**
 * 
 */
package com.siliconwise.mmc.message;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GNAKALE Bernardin
 *
 */
public class StatusMessage extends I18Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private StatusMessageType status = StatusMessageType.INFO ;
	
	private Map<String, String> variableValueMap = new HashMap<>();

	public StatusMessage() {
		super() ;
	}

	public StatusMessage(String messageDelimiter) {
		super(messageDelimiter);
	}

	public StatusMessage(StatusMessageType status, String messageKey) {
		super();
		this.status = status;
		setMessagekey(messageKey);
	}

	public StatusMessage(StatusMessageType status, String messageKey, Map<String, String> variableValueMap) {
		super();
		this.status = status;
		setMessagekey(messageKey);
		setVariableValueMap(variableValueMap);
	}

	public StatusMessageType getStatus() {
		return status;
	}

	public void setStatus(StatusMessageType status) {
		this.status = status;
	}
	
	public Map<String, String> getVariableValueMap() {
		return variableValueMap;
	}

	public void setVariableValueMap(Map<String, String> variableValueMap) {
		this.variableValueMap = variableValueMap;
	}

	public static boolean isThereAtLeastAnUnsuccessfullMessage(Collection<StatusMessage> msgList) {
		
		if (msgList == null) return false ;
		
		for (StatusMessage msg : msgList){
			if (msg.status == StatusMessageType.ERROR || msg.status == StatusMessageType.FATAL)
				return true ;
		}
		
		return false ;
	}
}
