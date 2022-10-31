package com.siliconwise.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.message.AppMessageKeys;

@NamedQueries({
	@NamedQuery(
			name = "tousLesPays", 
			query= "SELECT p FROM Pays p")
})
@Entity
@Table(name="pays")
public class Pays implements IEntityStringkey, Serializable, IEntityMsgVarMap {
	
	private static final long serialVersionUID = 1L ;
	
	public static final String CODE_TRADUCTION_PAYS_NON_VALIDE = "entite.pays.nonValide" ;
	public static final String CODE_TRADUCTION_PAYS_NON_TROUVE = "entite.pays.nonTrouve" ;

	@Id @Column(length = 50 )
	@Size(max= 50)
	private String id = null;
	
	@Column(length=50)
	@NotNull(message = "entite.pays.code.not_null")
	@NotEmpty(message = "entite.pays.code.not_empty")
	private String code = null;
	
	@Column(length=150)
	@NotNull(message = "entite.pays.designation.not_null")
	@NotEmpty(message = "entite.pays.designation.not_empty")
	private String designation = null;
	
	@Column(length=150)
	private String nationnalite = null ;
	
	
	@Column(length=150)
	private String codeTrDesignation = null;
	
	
	@Column(length=150)
	private String codeTrNationnalite = null;
		
	@Lob
	private String description = null;

	@Column(length=150)
	@NotNull(message = "entite.pays.prefixe.not_null")
	@NotEmpty(message = "entite.pays.prefixe.not_empty")
	private String indicatifInternationnal = null;
	
	private Boolean estSurListeNoire = null;
	
	private Integer ageMajorite = null;
	
	@Version
	private Integer version = null ;

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
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public String getNationnalite() {
		return nationnalite;
	}
	public void setNationnalite(String nationnalite) {
		this.nationnalite = nationnalite;
	}
	public String getCodeTrDesignation() {
		return codeTrDesignation;
	}
	public void setCodeTrDesignation(String codeTrDesignation) {
		this.codeTrDesignation = codeTrDesignation;
	}
	public String getCodeTrNationnalite() {
		return codeTrNationnalite;
	}
	public void setCodeTrNationnalite(String codeTrNationnalite) {
		this.codeTrNationnalite = codeTrNationnalite;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean getEstSurListeNoire() {
		return estSurListeNoire;
	}
	public void setEstSurListeNoire(Boolean estSurListeNoire) {
		this.estSurListeNoire = estSurListeNoire;
	}
	public Integer getAgeMajorite() {
		return ageMajorite;
	}
	public void setAgeMajorite(Integer ageMajorite) {
		this.ageMajorite = ageMajorite;
	}
	public String getIndicatifInternationnal() {
		return indicatifInternationnal;
	}
	public void setIndicatifInternationnal(String prefixe) {
		this.indicatifInternationnal = prefixe;
	}
	

	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "Pays [designation=" + designation + ", code=" + code + "]";
	}
	
	
	@Override
	public Map<String, String> getMsgVarMap() {
	
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Ville.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}

}
