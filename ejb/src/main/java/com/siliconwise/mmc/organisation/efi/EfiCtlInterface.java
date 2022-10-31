package com.siliconwise.mmc.organisation.efi;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author ALzouma Moussa Mahamadou
 *
 */
public interface EfiCtlInterface {
	

	/**
	 * @return
	 */
	public List<EFi> tousLesEfis() ;
	
	
	
	public Boolean confirmerCreationUnCompteEfi(
					  String idEfi,
					  boolean mustUpdateExistingNew,
					  String namedGraph, boolean isFetchGraph, 
					  Locale locale,  User loggedInUser, 
					  List<NonLocalizedStatusMessage> msgList);
	
	
	
	public EFi demandeCreationUnCompteEFi(
					  EFi entity,
					  boolean mustUpdateExistingNew,
					  String namedGraph, boolean isFetchGraph, 
					  Locale locale,  User loggedInUser, 
					  List<NonLocalizedStatusMessage> msgList);
	

}
