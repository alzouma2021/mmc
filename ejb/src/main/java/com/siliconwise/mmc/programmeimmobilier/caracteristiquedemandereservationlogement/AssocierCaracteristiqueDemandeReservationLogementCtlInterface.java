package com.siliconwise.mmc.programmeimmobilier.caracteristiquedemandereservationlogement;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */


public interface AssocierCaracteristiqueDemandeReservationLogementCtlInterface {
	
	
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
	public List<CaracteristiqueDemandeReservationLogement>  associerCaracteristiqueDemandeReservationLogementList(
			List<CaracteristiqueDemandeReservationLogement> entity,
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
	public  CaracteristiqueDemandeReservationLogement  associerUneCaracteristiqueDemandeReservationLogement(
			CaracteristiqueDemandeReservationLogement entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser, 
			List<NonLocalizedStatusMessage> msgList);

}
