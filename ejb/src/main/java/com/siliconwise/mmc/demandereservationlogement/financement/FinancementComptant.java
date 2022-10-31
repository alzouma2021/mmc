package com.siliconwise.mmc.demandereservationlogement.financement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.mmc.message.AppMessageKeys;


/**
 * Cette classe renferme les informations relatives au financement comptant
 * @author Alzouma Moussa Mahamadou
 * @date 24/03/2022
 *
 */
@Entity
@Table(name="financementcomptant")
@DiscriminatorValue("comptant")
public class FinancementComptant extends Financement implements Serializable , IEntityMsgVarMap {
	
	
	private static final long serialVersionUID = 1L;

	
	public FinancementComptant() {
		super();
	}

	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return   rtn;
	}
	

}
