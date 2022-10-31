package com.siliconwise.common.reference;

import java.io.Serializable;

public class CritereRechercheReference implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String searchDesignation = null ;
	private String searchCode = null ;
	
	

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getSearchDesignation() {
		return searchDesignation;
	}

	public void setSearchDesignation(String searchDesignation) {
		this.searchDesignation = searchDesignation;
	}
 

}
