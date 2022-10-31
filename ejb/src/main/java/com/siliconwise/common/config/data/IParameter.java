package com.siliconwise.common.config.data;

import java.util.Map;

public interface IParameter<T> {

	/**
	 * @return the id
	 */
	public abstract String getId();

	/**
	 * @param id the id to set
	 */
	public abstract void setId(String id);

	/**
	 * @return the designation
	 */
	public abstract String getDesignation();

	/**
	 * @param designation the designation to set
	 */
	public abstract void setDesignation(String designation);

	/**
	 * @return the description
	 */
	public abstract String getDescription();

	/**
	 * @param description the description to set
	 */
	public abstract void setDescription(String description);

	/**
	 * @return the value
	 * Must be implemented by sub classes
	 */
	public abstract T getValue();

	public abstract void setValue(T myValue);
	
	
	/**
	 * @return la valeur des messages de code traduction 
	 */
	public abstract Map<String, String> getMsgVarMap() ;

}