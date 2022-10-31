/**
 * 
 */
package com.siliconwise.mmc.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author GNAKALE Bernardin
 *
 */
public class I18Message implements Serializable {
		
	private static final long serialVersionUID = -536496542800224197L;

	private static final String DELIMITER_DEFAULT_VALUE = ":" ;
	
	private String messagekey = null ;
	private String messageDelimiter = DELIMITER_DEFAULT_VALUE ;
	
	// key: variable name, value= variable value
	// message: .....:varNname:.....  
	// where : is the delimiter  
	// message encoding is supposed to be UTF8
	// Key:: variable name
	// value: message key of the value. Must be defined in the UI
	// Chaque la valeur est une cle qui doit être définie dans la couche UI
	//private Map<String, String> variableMap = new HashMap<String, String>();

	public I18Message() {}
	
	public I18Message(String messageDelimiter) {
		this.messageDelimiter = messageDelimiter ;
	}
	
	public static Map<String, String> createValueMap(String variableName, String variableValue){
		
		Map<String, String> valueMap = (new HashMap<>());
		valueMap.put(variableName, variableValue);
		
		return valueMap ;
	}
	
	/**
	 * Compute readable message. value map is te map of each variable value associated witn tne corresonding key
	 * @param message
	 * @param valueMap
	 * @return
	 */
	public String computeMessage(String message, Map<String, String> variableValueMap) {
		
		if (message == null) return null ;
		if (message.equals("")) return "" ;
		
		for (Entry<String, String> entry : variableValueMap.entrySet()) {
			
			message.replaceAll(messageDelimiter + entry.getKey() + messageDelimiter, entry.getValue()) ;
		}
		return null;
	}

	public String getMessagekey() {
		return messagekey;
	}

	public void setMessagekey(String messagekey) {
		this.messagekey = messagekey;
	}

	public String getMessageDelimiter() {
		return messageDelimiter;
	}

	public void setMessageDelimiter(String messageDelimiter) {
		this.messageDelimiter = messageDelimiter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messagekey == null) ? 0 : messagekey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof I18Message)) {
			return false;
		}
		I18Message other = (I18Message) obj;
		if (messagekey == null) {
			if (other.messagekey != null) {
				return false;
			}
		} else if (!messagekey.equals(other.messagekey)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "I18Message [messagekey=" + messagekey + "]";
	}
}
