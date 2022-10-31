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
public interface PromoteurCtlInterface {
	
	
	

	public Boolean confirmerCreationUnComptePromoteur(
						  String idPromoteur,
						  boolean mustUpdateExistingNew,
						  String namedGraph, boolean isFetchGraph, 
						  Locale locale,  User loggedInUser, 
						  List<NonLocalizedStatusMessage> msgList);
	
	
	
	
	public Promoteur demandeCreationUnComptePromoteur(
						  Promoteur entity,
						  boolean mustUpdateExistingNew,
						  String namedGraph, boolean isFetchGraph, 
						  Locale locale,  User loggedInUser, 
						  List<NonLocalizedStatusMessage> msgList);
	
	
	
	/**
	 * @return
	 */
	public List<Promoteur> tousLesPromoteurs() ;
	

	

}
