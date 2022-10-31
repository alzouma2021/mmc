package com.siliconwise.mmc.user;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.security.SessionBag;


/**
 * Cette interface contient les methodes relatives à la classe controle Compte
 * Utilisateur
 * 
 * @author Alzouma moussa Mahamadou
 * @date 18/01/2021
 *
 */
public interface CreerModifierUnCompteUserCtlInterface {

	
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param currentSession
	 * @param msgList
	 * @return
	 * @throws Exception
	 */
	public User CreerModifierUnCompteUser(
					User entity,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList );
	
	
	/**
	 * @param idPromoteur
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean modifierUnMotDePasseCompteUser(
						String currentPassword, 
				        String newPassword, 
						SessionBag sessionBag,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,   
						List<NonLocalizedStatusMessage> msgList) ;
	
	
	
}
