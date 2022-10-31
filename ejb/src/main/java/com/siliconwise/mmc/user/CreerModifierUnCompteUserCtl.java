package com.siliconwise.mmc.user;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.security.SessionBag;


/**
 * Cette classe contient des operations sur la creation d'un compte utilisateur
 * 
 * @author Alzouma Moussa Mhamadou
 * @date 18/01/2021
 *
 */
@Stateless
public class CreerModifierUnCompteUserCtl implements Serializable, CreerModifierUnCompteUserCtlInterface {

	
	private static final long serialVersionUID = 1L;
	
	@Inject  UserDAOInterface userDAO;

	
	
	@Override
	public User CreerModifierUnCompteUser(
			     User entity,
			     boolean mustUpdateExistingNew, 
			     String namedGraph,
			     boolean isFetchGraph, 
			     Locale locale, 
			     User loggedInUser, 
			     List<NonLocalizedStatusMessage> msgList) {
		
		
		User  rtn = userDAO.validerEtEnregistrer(entity, mustUpdateExistingNew, 
				            namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		return rtn;
		
		
	}


	@Override
	public boolean modifierUnMotDePasseCompteUser(
			         String currentPassword, 
			         String newPassword, 
			         SessionBag sessionBag,
			         boolean mustUpdateExistingNew, 
			         String namedGraph,
			         boolean isFetchGraph, Locale locale,
			         List<NonLocalizedStatusMessage> msgList) {
		
		return userDAO.validerEtModifier(currentPassword, newPassword, sessionBag,
				        mustUpdateExistingNew, namedGraph, isFetchGraph, locale, msgList);
		
		
		
	}

	
	
	
}
