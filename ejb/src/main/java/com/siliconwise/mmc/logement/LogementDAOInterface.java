package com.siliconwise.mmc.logement;

import java.util.List;
import java.util.Locale;


import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * Interface de LogementDAO
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 29/03/2022
 *
 */
public interface LogementDAOInterface {

	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public  boolean  valider(Logement entity, 
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
	public Logement validerEtEnregistrer(
			Logement entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser, 
			List<NonLocalizedStatusMessage> msgList);
	
	

	
	
}
