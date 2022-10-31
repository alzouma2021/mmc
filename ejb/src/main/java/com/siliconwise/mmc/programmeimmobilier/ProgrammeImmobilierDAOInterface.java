package com.siliconwise.mmc.programmeimmobilier;

import java.util.List;
import java.util.Locale;

import com.siliconwise.common.reference.ReferenceFamille;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */


/**
 * @author sysadmin
 *
 */
public interface ProgrammeImmobilierDAOInterface {
	

	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public  boolean  valider(ProgrammeImmobilier entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale,
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
	public ProgrammeImmobilier validerEtEnregistrer(
			ProgrammeImmobilier entity,
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
	public boolean validerEtConfirmer(
					String idProgrammmeImmobilier,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList) ;
	
	
	/**
	 * @return
	 */
	public List<ProgrammeImmobilier> tousLesProgrammesImmobiliers() ;
	
	
	
	
	/**
	 * @param idPromoteur
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public List<ProgrammeImmobilier> 
					rechercherProgrammeImmobilierParPromoteur(
					  String  idPromoteur,
					  boolean mustUpdateExistingNew,
					  String namedGraph, boolean isFetchGraph, 
					  Locale locale,  User loggedInUser, 
					  List<NonLocalizedStatusMessage> msgList) ;
	
	
	
	
	
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
	public boolean supprimer(
				String  idProgrammeImmobilier,
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser, 
				List<NonLocalizedStatusMessage> msgList);
		
	
}
