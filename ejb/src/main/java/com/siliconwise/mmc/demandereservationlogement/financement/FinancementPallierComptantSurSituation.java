package com.siliconwise.mmc.demandereservationlogement.financement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;


/**
 * Cette classe renferme les informations relatives au financement pallier comptant sur situation
 * @author Alzouma Moussa Mahamadou
 * @date 24/03/2022
 *
 */
@Entity
@Table(name="financementpalliercomptantsursituation")
@DiscriminatorValue("palliercomptantsursituation")
public class FinancementPallierComptantSurSituation extends Financement implements Serializable , IEntityMsgVarMap {
	
	
	private static final long serialVersionUID = 1L;

	//Map les pallier et leurs montants proposé par le prospect
	@Transient
	private Map<String,Double> montantProposeList ;
	
	
	@Transient
	private Set<PallierComptantSurSituation> pallierComptantSurSituationList ;
	
	
	public FinancementPallierComptantSurSituation() {
		super();
	}
	
	public Map<String, Double> getMontantProposeList() {
		return montantProposeList;
	}

	public void setMontantProposeList(Map<String, Double> montantProposeList) {
		this.montantProposeList = montantProposeList;
	}

	public Set<PallierComptantSurSituation> getPallierComptantSurSituationList() {
		return pallierComptantSurSituationList;
	}

	public void setPallierComptantSurSituationList(Set<PallierComptantSurSituation> pallierComptantSurSituationList) {
		this.pallierComptantSurSituationList = pallierComptantSurSituationList;
	}


	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}
	
	
}
