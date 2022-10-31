package com.siliconwise.mmc.demandereservationlogement.financement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.modefinancement.CreditBancaire;


/**
 * Cette classe renferme les informations relatives au financement crédit immobilier
 * @author Alzouma Moussa Mahamadou
 * @date 24/03/2022
 *
 */
@Entity
@Table(name="financementcreditimmobilier")
@DiscriminatorValue("creditimmobilier")
public class FinancementCreditImmobilier extends Financement implements Serializable , IEntityMsgVarMap {
	
	
	private static final long serialVersionUID = 1L;
	
	
	//Durée du crédit immobilier
	@NotNull()
	@Column(nullable=false)
	private Integer duree ;
	
	@NotNull()
	@Column(nullable=false)
	private Double taux ; 
	
	@NotNull
	private CreditBancaire creditBancaire ; 
	
	
	public FinancementCreditImmobilier() {
		super();
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
	
	public CreditBancaire getCreditBancaire() {
		return creditBancaire;
	}

	public void setCreditBancaire(CreditBancaire creditBancaire) {
		this.creditBancaire = creditBancaire;
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}

}
