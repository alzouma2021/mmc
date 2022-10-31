/**
 * 
 */
package com.siliconwise.mmc.produitlogement;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.IReference;
import com.siliconwise.common.reference.IReferenceTr;
import com.siliconwise.common.reference.ReferenceFamille;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

@Table(name="operateurcritere",
	uniqueConstraints = {
			@UniqueConstraint(columnNames={"designation"}),
			@UniqueConstraint(columnNames={"code"})
	})
@NamedQueries(value = {
		@NamedQuery(
				name = "tousLesOperateurCriteres", 
				query = "SELECT o FROM OperateurCritere o ORDER BY o.designation" ),
})
@Entity 
@EntityListeners(UUIDGeneratorEntityListener.class)
public class OperateurCritere 
	implements Serializable, IEntityStringkey, IReference, IReferenceTr, Cloneable {

	private static final long serialVersionUID = 1L;
	
	// Id des références 
	
	//public static final String REFERENCE_ID_CIVILITE_MLLE = "ref.element.mlle" ;
	

	public OperateurCritere() {}
	public OperateurCritere(String param) {}
	
	// Id n'est pas un UUID. 
	@Id
	@Column(length = 50 )
	@Size(max= 50)
	private String id = null ;
	
	@Lob
	private String description = null ;
	
	@Column(length = 150) 
	private String codeTrDescription = null ;
	

	@Column(length = 150)
	@Size(max= 150, message = "")
	private String codeTrDesignation = null ; 
	
	@NotNull
	@NotEmpty
	@Column(length = 150 )
	@Size(max= 150)
	private String designation = null ;
	
	@NotNull
	@NotEmpty
	@Column(length = 150 )
	@Size(max= 150)
	private String code = null ;
	
	
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
	
	
	public String getCodeTrDescription() {
		return codeTrDescription;
	}
	
	public void setCodeTrDescription(String codeTrDescription) {
		this.codeTrDescription = codeTrDescription;
	}

	public String getCodeTrDesignation() {
		return codeTrDesignation;
	}

	public void setCodeTrDesignation(String designationTr) {
		this.codeTrDesignation = designationTr;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		OperateurCritere other = (OperateurCritere) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Reference [id=" + id + ", designation=" + designation + "]";
	}
	
	@Override
	public Object clone() {
		
		Object o = null;
		
		try {
			o = super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons 
			// l'interface Cloneable
			cnse.printStackTrace();
		}
		
		// on renvoie le clone
		return o;
	}


	
}
