package com.siliconwise.mmc.demandereservationlogement.financement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;

import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;


/**
 * 
 * Classe d'association FinancementPallierLink
 * @author Alzouma Moussa Mahamadou
 */
@Entity
@Table(name="financementpallierlink")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class FinancementPallierLink implements IEntityStringkey, Serializable, IEntityMsgVarMap {

	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;
	
	@ManyToOne
	@NotNull(message="LE financement pallier ne doit pas être nul")
	private FinancementPallierComptantSurSituation financementPallierComptantSurSituation = null;

	
	@ManyToOne
	@NotNull(message="Le pallier comptant sur situation  ne doit pas être nul")
	private PallierComptantSurSituation pallierComptantSurSituation = null ;
	
	@NotNull(message="Le montant propose ne doit pas être nul")
	private Double montantProspose ;
	
	private Double montant  ;

	@Version
	private Integer version = null ;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FinancementPallierComptantSurSituation getFinancementPallierComptantSurSituation() {
		return financementPallierComptantSurSituation;
	}

	public void setFinancementPallierComptantSurSituation(
			FinancementPallierComptantSurSituation financementPallierComptantSurSituation) {
		this.financementPallierComptantSurSituation = financementPallierComptantSurSituation;
	}

	public PallierComptantSurSituation getPallierComptantSurSituation() {
		return pallierComptantSurSituation;
	}

	public void setPallierComptantSurSituation(PallierComptantSurSituation pallierComptantSurSituation) {
		this.pallierComptantSurSituation = pallierComptantSurSituation;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Double getMontantProspose() {
		return montantProspose;
	}

	public void setMontantProspose(Double montantProspose) {
		this.montantProspose = montantProspose;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	@Override
	public String toString() {
		return "FinancementPallierLink [financementPallierComptantSurSituation="
				+ financementPallierComptantSurSituation + ", pallierComptantSurSituation="
				+ pallierComptantSurSituation + "]";
	}
	

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Reference.CODE_TRADUCTION_NOM_USUEL) ;
		//rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
	

}
