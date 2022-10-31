package com.siliconwise.mmc.logement;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mamahadou
 *
 */

public interface CreerModifierOptionLogementCtlInterface {
	
	
	
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
	public List<CaracteristiqueProduitLogement>  creerModifierOptionLogementList(
									Set<CaracteristiqueProduitLogement> entity,
									boolean mustUpdateExistingNew,Logement logement,
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
	public  CaracteristiqueProduitLogement  creerModifierUneOptionLogement(
									CaracteristiqueProduitLogement entity,
									boolean mustUpdateExistingNew,
									String namedGraph, boolean isFetchGraph, 
									Locale locale,  User loggedInUser, 
									List<NonLocalizedStatusMessage> msgList);
	

}
