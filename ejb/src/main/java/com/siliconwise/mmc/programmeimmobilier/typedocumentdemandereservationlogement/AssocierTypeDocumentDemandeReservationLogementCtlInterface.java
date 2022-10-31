package com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement;

import java.util.List;
import java.util.Locale;


import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */


public interface AssocierTypeDocumentDemandeReservationLogementCtlInterface {
	
	
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
	public List<TypeDocumentDemandeReservationLogement>  AssocierTypeDocumentDemandeReservationLogementList(
			List<TypeDocumentDemandeReservationLogement> entityList,
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
	public  TypeDocumentDemandeReservationLogement  AssocierUnTypeDocumentDemandeReservationLogement(
					TypeDocumentDemandeReservationLogement entity,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);

}
