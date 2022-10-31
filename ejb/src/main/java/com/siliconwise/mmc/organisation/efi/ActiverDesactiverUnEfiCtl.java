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
public class ActiverDesactiverUnEfiCtl implements Serializable , ActiverDesactiverUnEfiCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@Resource private EJBContext ejbContext;
		
	@Inject EfiDAOInterface efiDAO ;
		

	@Override
	public boolean activerUnEfi(
			        String idPromoteur, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		boolean rtn = efiDAO.validerEtActiver(idPromoteur, mustUpdateExistingNew, 
				    	     namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
		
		return rtn;
		
	}

	@Override
	public boolean desactiverUnEfi(
			        String idEfi, 
			        boolean mustUpdateExistingNew,
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
		
		boolean rtn = efiDAO.validerEtDesactiver(idEfi, mustUpdateExistingNew, 
				    	    namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
	
		return rtn;
		
	}
	
	
}
