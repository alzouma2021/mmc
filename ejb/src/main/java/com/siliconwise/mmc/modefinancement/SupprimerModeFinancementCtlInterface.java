package com.siliconwise.mmc.modefinancement;

import java.util.List;
import java.util.Locale;

import com.siliconwise.common.document.Document;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface SupprimerModeFinancementCtlInterface {
	
	
	
	
	/**
	 * @param idModeFinancement
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean supprimerUnModeFinancement(
					String idModeFinancement,
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
	public boolean supprimerModeFinancementList(
			            List<ModeFinancement> entityList,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser,
						List<NonLocalizedStatusMessage> msgList);
	
	

}
