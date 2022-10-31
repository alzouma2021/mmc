package com.siliconwise.mmc.organisation.efi;

import com.siliconwise.mmc.produitlogement.ProduitLogement;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface RechercherUnEFiCtlInterface {
	
	

	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public EFi rechercherUnEFiParId(
				          String id ,
				          String namedGraph, 
				          boolean isFetchGraph ,
				          Class<EFi> entityClass);
	

}
