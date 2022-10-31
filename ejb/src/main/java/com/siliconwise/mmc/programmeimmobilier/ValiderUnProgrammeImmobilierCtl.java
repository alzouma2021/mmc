package com.siliconwise.mmc.programmeimmobilier;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.modefinancement.ValiderModeFinancementCtlInterface;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class ValiderUnProgrammeImmobilierCtl implements Serializable, ValiderUnProgrammeImmobilierCtlInterface {


	private static final long serialVersionUID = 1L;
	
    @Inject ProgrammeImmobilierDAOInterface programmeImmobilierDAO;
    
    @Inject ValiderModeFinancementCtlInterface validerModeFinancementCtl ;

	@Override
	public boolean validerUnProgrammeImmobilier(
					String idProgrammeImmobilier,
					boolean mustUpdateExistingNew, String namedGraph, 
					boolean isFetchGraph, Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
	
		
			 //Appel de la methode  validerEtConfirmer de la classse ProgrammeImmobilierDAO
		
			 boolean rtn = programmeImmobilierDAO
					         .validerEtConfirmer(
					           idProgrammeImmobilier,
					           mustUpdateExistingNew,
					           namedGraph, 
							   isFetchGraph, locale, 
							   loggedInUser, msgList );
		   
			
		     return rtn  ;
		     
			
	}
	

}
