package com.siliconwise.mmc.produitlogement;

import java.util.List;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 * @date   17/03/2021
 */

public interface ProprieteProduitLogementDAOInterface {
	
	/**
	 * @return la liste de toutes les propriétés produit logement disponibles
	 */
	public List<ProprieteProduitLogement> rechercherProprieteProduitLogementList();

}
