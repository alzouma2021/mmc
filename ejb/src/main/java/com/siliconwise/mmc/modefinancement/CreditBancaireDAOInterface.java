package com.siliconwise.mmc.modefinancement;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */


public interface CreditBancaireDAOInterface {
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public  boolean  valider(CreditBancaire entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale,
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
	public CreditBancaire validerEtEnregistrer(
			CreditBancaire entity,
			boolean mustUpdateExistingNew,
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
	public CreditBancaire validerEtConfirmer(
			CreditBancaire entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser, 
			List<NonLocalizedStatusMessage> msgList);
	

}
