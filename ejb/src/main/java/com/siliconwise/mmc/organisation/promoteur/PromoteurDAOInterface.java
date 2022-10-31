package com.siliconwise.mmc.organisation.promoteur;

import java.util.List;
import java.util.Locale;

import com.siliconwise.common.Pays;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface PromoteurDAOInterface {
	
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public  boolean  valider(Promoteur entity, 
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
	public Promoteur validerEtEnregistrer(
				Promoteur entity,
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser, 
				List<NonLocalizedStatusMessage> msgList);
	
	

	
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
	public boolean validerEtActiver(
					String idPromoteur,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
	
	
	
	
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
	public boolean validerEtDesactiver(
						String idPromoteur,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList);
	
	
	

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
	public boolean validerEtSupprimer(
					String idPromoteur,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
	
	
	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public Promoteur rechercherUnPromoteurParId(
				          String id ,
				          String namedGraph, 
				          boolean isFetchGraph ,
				          Class<Promoteur> entityClass);
	
	
	
	/**
	 * @return
	 */
	public List<Promoteur> tousLesPromoteurs() ;
	
	

}
