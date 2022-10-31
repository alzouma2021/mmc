/**
 * 
 */
package com.siliconwise.mmc.oldSecurity;

import java.io.Serializable;

/**
 * @author Mr Ishyirimbere
 *
 */
public class TokenPayLoad implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6618760936057053604L;
	
	private String userMail = null ;
	private String userLanguage = null;
	private String userFullName = null ;
	
	private String ifAddress = null ;
	private String ifSigle = null ;
	
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getUserLanguage() {
		return userLanguage;
	}
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getIfAddress() {
		return ifAddress;
	}
	public void setIfAddress(String ifAddress) {
		this.ifAddress = ifAddress;
	}
	public String getIfSigle() {
		return ifSigle;
	}
	public void setIfSigle(String ifSigle) {
		this.ifSigle = ifSigle;
	}
	
	
	
	
	
	

}
