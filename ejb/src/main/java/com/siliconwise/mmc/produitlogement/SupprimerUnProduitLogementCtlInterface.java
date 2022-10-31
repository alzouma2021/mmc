package com.siliconwise.mmc.produitlogement;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface SupprimerUnProduitLogementCtlInterface {
	
	
			
	
			
			/**
			 * @param idProduitLogement
			 * @param mustUpdateExistingNew
			 * @param namedGraph
			 * @param isFetchGraph
			 * @param locale
			 * @param loggedInUser
			 * @param msgList
			 * @return
			 */
			public boolean supprimer(
							String idProduitLogement,
							boolean mustUpdateExistingNew,
							String namedGraph, boolean isFetchGraph, 
							Locale locale,  User loggedInUser, 
							List<NonLocalizedStatusMessage> msgList);
			

}
