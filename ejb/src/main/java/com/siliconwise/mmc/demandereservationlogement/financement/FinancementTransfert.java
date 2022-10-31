package com.siliconwise.mmc.demandereservationlogement.financement;

import java.util.List;

/**
 * 
 * 
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public class FinancementTransfert {
	
	
	
	private List<Financement> financementList ;
	 
	private  List<EcheanceFinancementTemperament> echeanceFinancementTemperamentList ;

	public List<Financement> getFinancementList() {
		return financementList;
	}

	public void setFinancementList(List<Financement> financementList) {
		this.financementList = financementList;
	}

	public List<EcheanceFinancementTemperament> getEcheanceFinancementTemperamentList() {
		return echeanceFinancementTemperamentList;
	}

	public void setEcheanceFinancementTemperamentList(
			List<EcheanceFinancementTemperament> echeanceFinancementTemperamentList) {
		this.echeanceFinancementTemperamentList = echeanceFinancementTemperamentList;
	}
	 
	 
}
