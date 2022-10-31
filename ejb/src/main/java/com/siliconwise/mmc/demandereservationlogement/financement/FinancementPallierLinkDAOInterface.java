package com.siliconwise.mmc.demandereservationlogement.financement;

import java.util.List;
import java.util.Locale;

import com.siliconwise.common.document.Document;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface FinancementPallierLinkDAOInterface {
	
	

	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public boolean  valider(FinancementPallierLink entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale, 
			List<NonLocalizedStatusMessage> msgList) ;
	
	
	
	
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
	public FinancementPallierLink validerEtEnregistrer(
					FinancementPallierLink entity,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser,
					List<NonLocalizedStatusMessage> msgList);
	
			
	

}
