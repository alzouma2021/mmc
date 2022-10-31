package com.siliconwise.mmc.programmeimmobilier;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.modefinancement.ModeFinancement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author ALzouma Moussa Mahamadou
 *
 */


public interface SupprimerUnProgrammeImmobilierCtlInterface {
	
	
	
		/**
		 * @param idProgrammeImmobilier
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param loggedInUser
		 * @param msgList
		 * @return
		 */
		public boolean supprimerUnProgrammeImmobilier(
					String  idProgrammeImmobilier,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
		
		
		
		/**
		 * @param entity
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param loggedInUser
		 * @param msgList
		 * @return
		 */
		public boolean supprimerProgrammeImmobilierList(
				          List<ProgrammeImmobilier> entityList,
				          boolean mustUpdateExistingNew,
						  String namedGraph, boolean isFetchGraph, 
						  Locale locale,  User loggedInUser,
						  List<NonLocalizedStatusMessage> msgList);
		

}
