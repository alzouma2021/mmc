
package com.siliconwise.common.config.data;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.siliconwise.common.entity.IEntityStringkey;

//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
/**
 * Paretre de configuration de nivgeau global (configuré au niveau du serveur central est ensuite transféré à tous es sites)
 * @author GNAKALE Bernardin
 *
 * @param <T>
 */
@MappedSuperclass
public abstract class Parameter<T>  implements IEntityStringkey, IParameter<T>   {

	private static final long serialVersionUID = 1L;

	public Parameter() {
		super();
	}
	
	public Parameter(T value) {
		super();
		setValue(value);
	}

	@Id @Column(length=50) // @Size(max=50) @NotNull @NotEmpty
	private String id = null ;

	@NotNull @Column(unique=true, length=150, nullable=false) @Size(max=150)
	private String designation = null ;
	
	private String description = null ;

	// data can only be accessed by te support from Silicon Wise
	@NotNull
	private Boolean isOnlySupport = false ;
	
	@Version
	protected Integer version;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
		
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the value
	 * Must be implemented by sub classes
	 */
	@Transient
	public abstract T getValue() ; 
	public abstract void setValue(T myValue) ;

		
	public Boolean getIsOnlySupport() {
		return isOnlySupport;
	}
	public void setIsOnlySupport(Boolean isOnlySupport) {
		this.isOnlySupport = isOnlySupport;
	}
		
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((designation == null) ? 0 : designation.hashCode());
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
		if (!(obj instanceof Parameter)) {
			return false;
		}
		Parameter<?> other = (Parameter<?>) obj;
		if (designation == null) {
			if (other.designation != null) {
				return false;
			}
		} else if (!designation.equals(other.designation)) {
			return false;
		}
		return true;
	}
	
}
