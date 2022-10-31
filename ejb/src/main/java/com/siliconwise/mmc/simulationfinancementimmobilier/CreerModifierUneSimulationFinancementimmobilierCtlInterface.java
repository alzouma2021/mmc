package com.siliconwise.mmc.simulationfinancementimmobilier;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */

public interface CreerModifierUneSimulationFinancementimmobilierCtlInterface {
	
	
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
	public SimulationFinancementImmobilier creerModifierUneSimulationFinancementImmobilier(
				SimulationFinancementImmobilierTransfert entity,
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser, 
				List<NonLocalizedStatusMessage> msgList,Boolean estCreation) ;
	
	
	
}
