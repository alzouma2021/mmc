package com.siliconwise.mmc.programmeimmobilier;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;


/**
 * 
 * @author Alzouma Moussa Mahamadou
 * 
 */

public interface ValiderUnProgrammeImmobilierCtlInterface {
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @param estCreation
	 * @return
	 */
	public boolean validerUnProgrammeImmobilier(
					 String  idProgrammeImmobilier,
					 boolean mustUpdateExistingNew,
					 String namedGraph, boolean isFetchGraph, 
					 Locale locale,  User loggedInUser, 
					 List<NonLocalizedStatusMessage> msgList );

	
	
}
