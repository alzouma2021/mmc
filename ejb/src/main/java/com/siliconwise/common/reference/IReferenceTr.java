/**
 * 
 */
package com.siliconwise.common.reference;

/**
 * @author bgnakale
 *
 */
public interface IReferenceTr {

	public abstract String getId();
	public abstract void setId(String id)  ;

	public abstract String getDescription();
	public abstract void setDescription(String description);

	public abstract String getDesignation();
	public abstract void setDesignation(String designation);
	
	public abstract String getCodeTrDescription() ;
	public abstract void setCodeTrDescription(String codeTr) ;
	
	public abstract String getCodeTrDesignation() ;
	public abstract void setCodeTrDesignation(String codeTrDesignation) ;
}
