package com.siliconwise.mmc.security;

import java.io.Serializable;

public class SecurityConstants implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// Token build secret phrase
	public static final String SESSION_TOKEN_SECRET_PHRASE = "siliconwisevotrepartenairepourtoujours#2020" ;

	// session id
	public static final String SESSION_TOKEN_FIELD_SESSION_ID = "sid" ;

	public static final String SESSION_TOKEN_FIELD_USER_ID = "uid" ;
	public static final String SESSION_TOKEN_FIELD_EMAIL = "eml" ;
	public static final String SESSION_TOKEN_FIELD_USER_FULLNAME = "fnm" ;
	public static final String SESSION_TOKEN_FIELD_OPERATOR_ID = "oid" ;
	
	// comma delimited user groups list
	public static final String SESSION_TOKEN_FIELD_ROLE_IDS_LIST = "rol" ;
	
	// 3 letters code language
	public static final String SESSION_TOKEN_FIELD_LOCALE_CODE = "loc" ;
	
	public static final String SESSION_TOKEN_FIELD_LIST_SEPARATOR = "," ;
	
	// ----- Session token header fields

}
