/**
 * 
 */
package com.siliconwise.common.reference;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;


/**
 * Classe renfermant les informations des familles de reference
 * @identiteNaturelle
 * 		- 
 * @dateCreation 
 * @author Eugène Ishyirimbere
 *
 */
@NamedQueries(value={
		@NamedQuery(
				name="toutesLesFamilles",
				query="SELECT rf FROM ReferenceFamille rf")
})
@Table(name="referencefamille",
	uniqueConstraints = {
		@UniqueConstraint(columnNames={ "designation"})
})
@Entity @EntityListeners(UUIDGeneratorEntityListener.class)
public class ReferenceFamille implements IEntityStringkey, Serializable, IReference {

	private static final long serialVersionUID = 1L;
	
	// Id des famille
	public final static String REF_FAMILLE_ID_TYPE_VALEUR = "ref.famille.typeValeur" ;
	public final static String REF_FAMILLE_ID_LOG_EVENT = "ref.famille.logEvent" ;
	
	//public static final String REFERENCE_FAMILLE_ID_CIVILITE = "ref.fam.civilite" ;

	@Id 
	@Column(length=50)
	@Size(max = 50)
	private String id = null ;
	
	private String description = null ;
	
	@NotNull @NotEmpty
	@Column(length=150, unique=true) @Size(max = 150)
	private String designationTr = null ; 
	
	@NotNull @NotEmpty
	@Column(length=254)
	@Size(max = 254)
	private String absolutePath = null ;
	
	@NotNull @NotEmpty @Column(length=150, unique=true) @Size(max = 150)
	private String designation = null ;


	@Version
	protected Integer version;

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getDesignationTr() {
		return designationTr;
	}


	public void setDesignationTr(String designationTr) {
		this.designationTr = designationTr;
	}


	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
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
		result = prime * result + ((designationTr == null) ? 0 : designationTr.hashCode());
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
		ReferenceFamille other = (ReferenceFamille) obj;
		if (designation == null) {
			if (other.designation != null)
				return false;
		} else if (!designation.equals(other.designation))
			return false;
		if (designationTr == null) {
			if (other.designationTr != null)
				return false;
		} else if (!designationTr.equals(other.designationTr))
			return false;
		return true;
	}

	@Override
	public String getCode() {
		
		return null;
	}


	@Override
	public void setCode(String code) {
		
	}

	
}
