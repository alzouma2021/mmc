package com.siliconwise.mmc.demandereservationlogement.financement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.modefinancement.Temperament;


/**
 * Cette classe renferme les informations relatives au financement temperament
 * @author Alzouma Moussa Mahamadou
 * @date 24/03/2022
 *
 */
@Entity
@Table(name="financementtemperament")
@DiscriminatorValue("temperament")
public class FinancementTemperament extends Financement implements Serializable , IEntityMsgVarMap {
	
	
	private static final long serialVersionUID = 1L;
	

	//Nombre de période proposé par le prospect dans sa requete de simulation
	@Column(nullable=false)
	private Integer nombrePeriodePropose ;
	
	
	//Nombre de période effectif de financment déterminé par le simulateur
	@Column(nullable=false)
	private Integer nombrePeriodeEffectif ; 
	
	// OneToMany ProduitLogement vers CaracteristiqueProduitLogement
  
	@OneToMany(mappedBy="financementTemparement", cascade = (CascadeType.REMOVE) ,fetch = FetchType.EAGER )
	private Set<EcheanceFinancementTemperament> echeanceFinancementTemperamentList ;
	
	
	//Mode
	@OneToOne
	private Temperament mode ;
	
	
	public FinancementTemperament() {
		super();
	}

	public Integer getNombrePeriodePropose() {
		return nombrePeriodePropose;
	}

	public void setNombrePeriodePropose(Integer nombrePeriodePropose) {
		this.nombrePeriodePropose = nombrePeriodePropose;
	}

	public Integer getNombrePeriodeEffectif() {
		return nombrePeriodeEffectif;
	}

	public void setNombrePeriodeEffectif(Integer nombrePeriodeEffectif) {
		this.nombrePeriodeEffectif = nombrePeriodeEffectif;
	}
	
	public Temperament getMode() {
		return mode;
	}

	public void setMode(Temperament mode) {
		this.mode = mode;
	}
	
	public Set<EcheanceFinancementTemperament> getEcheanceFinancementTemperamentList() {
		return echeanceFinancementTemperamentList;
	}

	public void setEcheanceFinancementTemperamentList(
			Set<EcheanceFinancementTemperament> echeanceFinancementTemperamentList) {
		this.echeanceFinancementTemperamentList = echeanceFinancementTemperamentList;
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}
	
	

}
