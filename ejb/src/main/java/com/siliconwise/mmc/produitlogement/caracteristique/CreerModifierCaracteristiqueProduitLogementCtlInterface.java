package com.siliconwise.mmc.produitlogement.caracteristique;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mamahadou
 *
 */

public interface CreerModifierCaracteristiqueProduitLogementCtlInterface {
	
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public List<CaracteristiqueProduitLogement>  creerModifierCaracteristiqueProduitLogementList(
			Set<CaracteristiqueProduitLogement> entity,
			boolean mustUpdateExistingNew,ProduitLogement produitLogement,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser, 
			List<NonLocalizedStatusMessage> msgList);
	

	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public  CaracteristiqueProduitLogement  creerModifierUneCaracteristiqueProduitLogement(
			CaracteristiqueProduitLogement entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser, 
			List<NonLocalizedStatusMessage> msgList);

}
