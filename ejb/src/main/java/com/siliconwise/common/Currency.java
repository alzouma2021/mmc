package com.siliconwise.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.IReferenceTr;


@NamedQueries(value = {
		
	@NamedQuery(
		name= "allCurrencyList", 
		query = "SELECT c FROM Currency c  "),
})
@Table(name="currency")
@Entity @EntityListeners(com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener.class)
public class Currency implements IEntityStringkey, IReferenceTr, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 50) 
	private String id = null ;
	
	@NotNull
	@NotEmpty
	@Size(max= 150)
	@Column(name="codeBanque", length = 150, unique=true)
	private String code = null ;
	
	// Contains translation code
	@NotNull
	@NotEmpty
	@Column(length = 150 )
	@Size(max= 150)
	private String designation = null ;
	
	// Contains translation code
	@Lob
	private String description = null ;

	@NotNull
	@Column(length = 150) 
	@Size(max=150)
	private String codeTrDescription = null ;


	@Column(length = 150)
	@Size(max=150)
	private String codeTrDesignation = null ; 


	@Version
	private Integer version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCodeTrDescription() {
		return codeTrDescription;
	}

	public void setCodeTrDescription(String codeTrDescription) {
		this.codeTrDescription = codeTrDescription;
	}

	public String getCodeTrDesignation() {
		return codeTrDesignation;
	}

	public void setCodeTrDesignation(String codeTrDesignation) {
		this.codeTrDesignation = codeTrDesignation;
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Currency [id=" + id + ", code=" + code + ", designation=" + designation + ", version="
				+ version + "]";
	}
}
