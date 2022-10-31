package com.siliconwise.mmc.produitlogement;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */


public interface CreerModifierUnProduitLogementCtlInterface {
	
	
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
	public ProduitLogement creerModifierUnProduitLogement(
				ProduitLogementTransfert entity,
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser, 
				List<NonLocalizedStatusMessage> msgList , Boolean estCreation);
	
	
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
	public ProduitLogement creerModifierUnProduitLogementBis(
								ProduitLogement entity,
								boolean mustUpdateExistingNew,
								String namedGraph, boolean isFetchGraph, 
								Locale locale,  User loggedInUser, 
							    List<NonLocalizedStatusMessage> msgList, Boolean estCreation);
	
}
