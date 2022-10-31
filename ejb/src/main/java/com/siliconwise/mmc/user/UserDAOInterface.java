package com.siliconwise.mmc.user;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import javax.enterprise.event.Observes;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.security.SessionBag;

import kong.unirest.UnirestException;

/**
 * 
 * Interface pour la classe DAO Compte Utilisateur
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 14/01/2021
 *
 */
public interface UserDAOInterface {

	

	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public User validerEtEnregistrer(
				User entity,
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser, 
				List<NonLocalizedStatusMessage> msgList);
	



	/**
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public boolean valider(
			User entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, 
			boolean isFetchGraph,
			Locale locale,
			List<NonLocalizedStatusMessage> msgList) ;
	
	
	

	/**
	 * @param idUser
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean validerEtActiver(
					String idUser,
					boolean mustUpdateExistingNew,
					boolean compteUserHasPassWord,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
	
	
	

	/**
	 * @param code
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean confirmerUnCompteUser(
					String code,
					boolean mustUpdateExistingNew,
					boolean compteUserHasPassWord,
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
						String idUser,
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
	public User rechercherUnUserParId(
				          String id ,
				          String namedGraph, 
				          boolean isFetchGraph ,
				          Class<User> entityClass);
	
	
	
	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public User rechercherUnUserParEmail(
				          String email ,
				          String namedGraph, 
				          boolean isFetchGraph ,
				          Class<User> entityClass);
	
	
	
	

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
					String idUser,
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
	public boolean validerEtModifier(
					String currentPassword, 
			        String newPassword, 
					SessionBag sessionBag,
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
	public boolean regenererUnMotDePasseCompteUser(
						String email ,
						boolean mustUpdateExistingNew,
						String namedGraph, 
						boolean isFetchGraph, 
						Locale locale,
						List<NonLocalizedStatusMessage> msgList)
						throws NoSuchAlgorithmException;
	
	
	
	
	/**
	 * @param user
	 * @param plainPassword
	 * @param locale
	 * @param msgList
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalArgumentException
	 */
	public boolean checkPassword(
			          User user, String plainPassword,
			          Locale locale ,
			          List<NonLocalizedStatusMessage> msgList ) 
			throws NoSuchAlgorithmException, IllegalArgumentException ;
	
	
	
	
	
	/**
	 * @param email
	 * @param plainPassword
	 * @param namedGraph
	 * @param isFetchGraph
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public User findUserByEmailAndPlainPassword(
			         String email, 
			         String plainPassword,
			         String namedGraph, 
			         boolean isFetchGraph) throws NoSuchAlgorithmException ;
	
	
	
	
	
	 /**
	 * @param fullNameUser
	 * @param emailUser
	 * @param codeConfirmatation
	 * @param locale
	 * @param msgList
	 * @return
	 * @throws UnirestException
	 */
	public boolean sendConfirmationEmail(
	            String fullNameUser, 
				String emailUser, 
				String codeConfirmatation, 
				Locale locale, List<NonLocalizedStatusMessage> msgList) 
			  throws UnirestException ;
	
	
	
	
}
