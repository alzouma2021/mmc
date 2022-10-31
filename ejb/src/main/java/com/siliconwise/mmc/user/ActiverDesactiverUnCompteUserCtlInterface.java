package com.siliconwise.mmc.user;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;


/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface ActiverDesactiverUnCompteUserCtlInterface {
	
	
	

	public boolean activerUnCompteUser(
					  String idUser,
					  boolean mustUpdateExistingNew,
					  boolean compteUserHasPassWord,
					  String namedGraph, boolean isFetchGraph, 
					  Locale locale,  User loggedInUser, 
					  List<NonLocalizedStatusMessage> msgList);
	
	
			
	

	public boolean desactiverUnCompteUser(
					 String idUser,
					 boolean mustUpdateExistingNew,
					 String namedGraph, boolean isFetchGraph, 
					 Locale locale,  User loggedInUser, 
					 List<NonLocalizedStatusMessage> msgList);
	
	
	
	

}
