package com.siliconwise.mmc.programmeimmobilier;



import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;


import com.siliconwise.mmc.message.NonLocalizedStatusMessage;


import com.siliconwise.mmc.user.User;

@Stateless
public class RechercherProgrammeImmobilierCtl implements RechercherProgrammeImmobilierCtlInterface{

	
	@Resource
	private EJBContext ejbContext;
	
	@Inject  ProgrammeImmobilierDAOInterface programmeImmobilierDAO ;
	
	@Override
	public List<ProgrammeImmobilier> tousLesProgrammesImmobiliers() {
		
		
		List<ProgrammeImmobilier> rtnList = programmeImmobilierDAO
										     .tousLesProgrammesImmobiliers();
		
		return rtnList;
		
		
	}


	@Override
	public List<ProgrammeImmobilier> 
					rechercherProgrammeImmobilierParPromoteur(
						String idPromoteur,
						boolean mustUpdateExistingNew, 
						String namedGraph, boolean isFetchGraph, 
						Locale locale, User loggedInUser,
						List<NonLocalizedStatusMessage> msgList) {
		
		List<ProgrammeImmobilier> rtnList = programmeImmobilierDAO
											  .rechercherProgrammeImmobilierParPromoteur(
												 idPromoteur,  mustUpdateExistingNew, namedGraph, 
												 isFetchGraph, locale, loggedInUser, msgList);

		return rtnList;
		
		
	}
	
}
