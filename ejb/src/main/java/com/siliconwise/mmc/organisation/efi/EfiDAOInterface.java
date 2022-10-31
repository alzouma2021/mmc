package com.siliconwise.mmc.organisation.efi;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.user.User;


/**
 * 
 * @author Alzouma Moussa Mamahadou
 *
 */
public interface EfiDAOInterface {
	
	
	
	

	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public  boolean  valider(EFi entity, 
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
	public EFi validerEtEnregistrer(
				EFi entity,
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser, 
				List<NonLocalizedStatusMessage> msgList);
	
	

	/**
	 * @param idEfi
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean validerEtActiver(
					String idEfi,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
	
	
	
	
	/**
	 * @param idEfi
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean validerEtDesactiver(
						String idEfi,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList);
	
	
	
	/**
	 * @param idEfi
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean validerEtSupprimer(
					String idEfi,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
	
	
	
	/**
	 * @return
	 */
	public List<EFi> tousLesEfis() ;
	
	
	
	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public EFi rechercherUnEFiParId(
				          String id ,
				          String namedGraph, 
				          boolean isFetchGraph ,
				          Class<EFi> entityClass);
	

}
