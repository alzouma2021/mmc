package com.siliconwise.mmc.user;


import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;


/**
 * 
 * Interface pour la classe DAO CodeConfirmation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 07/10/2021
 *
 */
public interface CodeConfirmationDAOInterface {

	
	
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
	public CodeConfirmation validerEtEnregistrer(
					CodeConfirmation entity,
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
	 * @param msgList
	 * @return
	 */
	public boolean valider(
			CodeConfirmation entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, 
			boolean isFetchGraph,
			Locale locale,
			List<NonLocalizedStatusMessage> msgList) ;
	

	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public CodeConfirmation rechercherUnCodeConfirmationParId(
					          String id ,String namedGraph, 
					          boolean isFetchGraph ,Class<CodeConfirmation> entityClass);
	
	
}
