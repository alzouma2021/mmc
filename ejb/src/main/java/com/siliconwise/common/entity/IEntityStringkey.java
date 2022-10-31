/**
 * 
 */
package com.siliconwise.common.entity;

import java.io.Serializable;

/**
 * Interface implemented by all entity with a String id
 * @author GNAKALE Bernardin
 *
 */
public interface IEntityStringkey extends Serializable {
	
	public abstract String getId() ;
	public abstract void setId(String id);
	
	public abstract Integer getVersion() ;
	public abstract void setVersion(Integer version) ;
}
