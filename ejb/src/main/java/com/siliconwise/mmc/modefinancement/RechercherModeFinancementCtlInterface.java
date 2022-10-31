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
public interface RechercherModeFinancementCtlInterface {
	

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
	public List<ModeFinancement> rechercherModeFinancementListParProgrammeImmobilier(
				String  idPrgrammeImmmobilier, 
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale, User loggedInUser,
				List<NonLocalizedStatusMessage> msgList) ;
	
	
	
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
	public List<ModeFinancement> rechercherModeFinancementListParEfi(
							String  idEfi, 
							boolean mustUpdateExistingNew,
							String namedGraph, boolean isFetchGraph, 
							Locale locale, User loggedInUser,
							List<NonLocalizedStatusMessage> msgList) ;


}
