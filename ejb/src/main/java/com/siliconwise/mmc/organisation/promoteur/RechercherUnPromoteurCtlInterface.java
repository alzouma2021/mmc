package com.siliconwise.mmc.organisation.promoteur;

import com.siliconwise.mmc.produitlogement.ProduitLogement;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface RechercherUnPromoteurCtlInterface {
	
	

	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public Promoteur rechercherUnPromoteurParId(
				          String id ,
				          String namedGraph, 
				          boolean isFetchGraph ,
				          Class<Promoteur> entityClass);
	

}
