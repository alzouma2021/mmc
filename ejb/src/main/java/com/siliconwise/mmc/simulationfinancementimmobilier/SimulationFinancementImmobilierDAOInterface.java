package com.siliconwise.mmc.simulationfinancementimmobilier;

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
public interface SimulationFinancementImmobilierDAOInterface {
	
	
	
	
	
	

	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public  boolean  valider(SimulationFinancementImmobilier entity, 
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
	public SimulationFinancementImmobilier validerEtEnregistrer(
			SimulationFinancementImmobilier entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser, 
			List<NonLocalizedStatusMessage> msgList);
	
	

	
	
}
