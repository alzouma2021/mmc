package com.siliconwise.mmc.organisation.efi;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;


import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class CreerModifierUnEfiCtl implements Serializable, CreerModifierUnEfiCtlInterface  {

	
	private static final long serialVersionUID = 1L;
	
    @Resource private EJBContext ejbContext;
	
	@Inject EfiDAOInterface efiDAO ;
	
	
	@Override
	public EFi creerModifierUnEfi(
			          EFi entity, 
			          boolean mustUpdateExistingNew, 
			          String namedGraph,
			          boolean isFetchGraph, 
			          Locale locale, 
			          User loggedInUser, 
			          List<NonLocalizedStatusMessage> msgList) {
		
		EFi rtn  = efiDAO.validerEtEnregistrer(entity, mustUpdateExistingNew, 
						  namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		return rtn ;
		
	}

	@Override
	public EFi modifierUnCompteEFi(
			        EFi entity, 
			        boolean mustUpdateExistingNew,
			        String namedGraph,
			        boolean isFetchGraph, Locale locale,
			        User loggedInUser, 
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		return creerModifierUnEfi(entity, mustUpdateExistingNew,namedGraph,
				               isFetchGraph, locale,  loggedInUser,   msgList);
		
		
	}
	
	
}
