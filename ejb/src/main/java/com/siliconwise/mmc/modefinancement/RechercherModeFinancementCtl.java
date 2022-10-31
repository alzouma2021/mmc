package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class RechercherModeFinancementCtl implements Serializable , RechercherModeFinancementCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@Inject ModeFinancementDAOInterface modeFinancementDAO ;

	@Override
	public List<ModeFinancement> rechercherModeFinancementListParProgrammeImmobilier(
									String idPrgrammeImmmobilier,
									boolean mustUpdateExistingNew, 
									String namedGraph, boolean isFetchGraph, 
									Locale locale, User loggedInUser,
									List<NonLocalizedStatusMessage> msgList) {
		
		
		List<ModeFinancement> rtnList = modeFinancementDAO
				                           .rechercherModeFinancementListParProgrammeImmobilier(
				                        	 idPrgrammeImmmobilier, mustUpdateExistingNew, 
				                        	 namedGraph, isFetchGraph, locale, loggedInUser, msgList);
		
		 return rtnList;
		 
	}

	
	@Override
	public List<ModeFinancement> rechercherModeFinancementListParEfi(
			                       String idEfi,
			                       boolean mustUpdateExistingNew,
			                       String namedGraph, boolean isFetchGraph,
			                       Locale locale, User loggedInUser,
			                       List<NonLocalizedStatusMessage> msgList) {
		

		List<ModeFinancement> rtnList = modeFinancementDAO
				                           .rechercherModeFinancementListParEfi(
				                        	  idEfi, mustUpdateExistingNew,namedGraph, 
				                        	  isFetchGraph, locale, loggedInUser, msgList);
		
		 return rtnList;
		 
		 
		
	}
	
	
	
}
