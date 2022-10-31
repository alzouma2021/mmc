package com.siliconwise.mmc.programmeimmobilier;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */

public interface RechercherProgrammeImmobilierCtlInterface {
	

	
	
	/**
	 * @return
	 */
	public List<ProgrammeImmobilier> tousLesProgrammesImmobiliers() ;
	
	
	
	/**
	 * @param idPromoteur
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public List<ProgrammeImmobilier> 
					rechercherProgrammeImmobilierParPromoteur(
											String  idPromoteur,
											boolean mustUpdateExistingNew,
											String namedGraph, boolean isFetchGraph, 
											Locale locale,  User loggedInUser, 
											List<NonLocalizedStatusMessage> msgList) ;
	
	
	
}
