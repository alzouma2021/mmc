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
public interface ActiverDesactiverUnEfiCtlInterface {
	
	
	public boolean activerUnEfi(
					  String idEfi,
					  boolean mustUpdateExistingNew,
					  String namedGraph, boolean isFetchGraph, 
					  Locale locale,  User loggedInUser, 
					  List<NonLocalizedStatusMessage> msgList);
	
	
	public boolean desactiverUnEfi(
					 String idEfi,
					 boolean mustUpdateExistingNew,
					 String namedGraph, boolean isFetchGraph, 
					 Locale locale,  User loggedInUser, 
					 List<NonLocalizedStatusMessage> msgList);
	
}
