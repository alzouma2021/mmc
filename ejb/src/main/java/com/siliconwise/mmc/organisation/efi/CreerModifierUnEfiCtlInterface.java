package com.siliconwise.mmc.organisation.efi;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface CreerModifierUnEfiCtlInterface {
	
	
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
	public EFi creerModifierUnEfi(
				  EFi entity,
				  boolean mustUpdateExistingNew,
				  String namedGraph, boolean isFetchGraph, 
				  Locale locale,  User loggedInUser, 
				  List<NonLocalizedStatusMessage> msgList );
	
	
	

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
	public EFi modifierUnCompteEFi(
				  EFi entity,
				  boolean mustUpdateExistingNew,
				  String namedGraph, boolean isFetchGraph, 
				  Locale locale,  User loggedInUser, 
				  List<NonLocalizedStatusMessage> msgList );


}
