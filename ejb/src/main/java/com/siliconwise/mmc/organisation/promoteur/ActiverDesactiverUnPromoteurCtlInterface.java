package com.siliconwise.mmc.organisation.promoteur;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface ActiverDesactiverUnPromoteurCtlInterface {
	
	
	

	public boolean activerUnPromoteur(
					  String idPromoteur,
					  boolean mustUpdateExistingNew,
					  String namedGraph, boolean isFetchGraph, 
					  Locale locale,  User loggedInUser, 
					  List<NonLocalizedStatusMessage> msgList);
	
	
	
	
	
	
	public boolean desactiverUnPromoteur(
					 String idPromoteur,
					 boolean mustUpdateExistingNew,
					 String namedGraph, boolean isFetchGraph, 
					 Locale locale,  User loggedInUser, 
					 List<NonLocalizedStatusMessage> msgList);
	

	

}
