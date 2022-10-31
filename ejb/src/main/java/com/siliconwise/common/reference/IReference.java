package com.siliconwise.common.reference;

public interface IReference {

	
	public abstract String getId();
	public abstract void setId(String id)  ;
	
	public abstract String getCode();
	public abstract void setCode(String code)  ;

	public abstract String getDescription();
	public abstract void setDescription(String description);

	public abstract String getDesignation();
	
	public abstract void setDesignation(String designation);
	
	/**
	 *Commenter par Alzouma Date:23 Juin 2021
	 *
	 *Pour impossibilité de créer un produit logement
	 */
	//public abstract String getLocalizedDesignation() ;

}