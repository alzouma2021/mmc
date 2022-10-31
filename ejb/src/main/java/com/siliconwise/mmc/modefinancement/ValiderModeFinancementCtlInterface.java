package com.siliconwise.mmc.modefinancement;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */


public interface ValiderModeFinancementCtlInterface {

	
	
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
	public  ModeFinancement  validerModeFinancement(
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
	public  boolean  notifierModeFinancement(
								ModeFinancement entity,
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
	public List<ModeFinancement> validerOuNotifierModeFinancementList(
									List<ModeFinancement> entity,
									boolean mustUpdateExistingNew,
									ProgrammeImmobilier programme,
									String namedGraph, boolean isFetchGraph, 
									Locale locale,  User loggedInUser, 
									List<NonLocalizedStatusMessage> msgList);
		
}
	
	
	
		

