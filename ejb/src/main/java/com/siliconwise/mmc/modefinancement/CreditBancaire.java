package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.document.Document;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.organisation.efi.EFi;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */
@NamedQueries(value={
		
		
		/* Supprimer des produits logements par promoteur */
		
		@NamedQuery(
				name= "supprimerCreditBancaireParEFi", 
				query = "DELETE  FROM CreditBancaire cb "
						  + " WHERE cb.eFi.id = :idEfi  ")
	 
		
})
@Entity
@Table(name = "creditbancaire")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class CreditBancaire implements Serializable, IEntityStringkey, IEntityMsgVarMap{
	
	
	private static final long serialVersionUID = 1L;
	
	//@NotNull(message = "L'Id ne doit pas être nul")
	//@NotEmpty(message = "L'Id ne doit pas être vide")
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private  String id = null ;
	
	@NotNull(message = "la duree ne doit pas être nulle")
	private Integer  duree = null ;
	
	@NotNull(message = "le taux ne doit pas être nul")
//	@NotEmpty(message = "le tauxe doit pas être vide")
	private Double taux = null ;
	
	
	
	//CreditBancaire vers EFi
	@NotNull(message = "l'EFi ne doit pas être nulle")
	@ManyToOne
	private EFi eFi = null ;
	
	
	//CreditBancaire vers Document
	@OneToOne//(cascade = CascadeType.ALL)
	private Document lettreConfort = null ;
	
	@Version
	private Integer version ;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDuree() {
		return duree;
	}

	public void setDuree(Integer duree) {
		this.duree = duree;
	}

	public Double getTaux() {
		return taux;
	}

	public void setTaux(Double taux) {
		this.taux = taux;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public EFi getEfi() {
		return eFi;
	}

	public void setEfi(EFi eFi) {
		this.eFi = eFi;
	}

	public Document getLettreConfort() {
		return lettreConfort;
	}

	public void setLettreConfort(Document lettreConfort) {
		this.lettreConfort = lettreConfort;
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
		CreditBancaire other = (CreditBancaire) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "CreditBancaire [duree=" + duree + ", taux=" + taux + ", eFi=" + eFi + ", lettreConfort=" + lettreConfort
				+ "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
	Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, ModeFinancement.CODE_TRADUCTION_NOM_USUEL) ;
		
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	
}
