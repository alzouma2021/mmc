package com.siliconwise.mmc.user;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.Utility;
import com.siliconwise.common.Ville;
import com.siliconwise.common.config.file.AppConfigKeys;
import com.siliconwise.common.config.file.AppConfigUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.*;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.security.SecurityService;
import com.siliconwise.mmc.security.SessionBag;

import kong.unirest.UnirestException;


/**
 * 
 * Classe DAO Compte Utilisateur
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 13/01/2021
 * 
 */
@Stateless
public class UserDAO implements Serializable, UserDAOInterface {

	
		private static final long serialVersionUID = 1L;
		
		@PersistenceContext
		private  EntityManager entityManager ;
	
		@Resource
		private EJBContext ejbContext;
	
		private transient Logger logger = LoggerFactory.getLogger(getClass().getName());
		
		@Inject 
		EmailService emailService ;
		
		@Inject 
		SecurityService securityService ;
		
		@Inject 
		CodeConfirmationDAOInterface codeConfirmationDAO ;
		
		private Properties appConfig = AppConfigUtil.getAppConfig();

		
		@Inject private Event<HistoryEventPayload<User>> historyEvent ;
		
		
		@Override
		public boolean valider(
				 User entity, 
				 boolean mustUpdateExistingNew, 
				 String namedGraph, 
				 boolean isFetchGraph, 
				 Locale locale,
				 List<NonLocalizedStatusMessage> msgList) {
		
			
			logger.info("_106	affichage des informations="+entity.toString()); //TODO A effacer
			
			if(entity.getEmail() == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_USER_EMAIl_NON_DEFINI,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_USER_EMAIl_NON_DEFINI, // Message par defaut
						entity.getMsgVarMap()) ;
				
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			 
			// recherche de doublon
			
			boolean isEntityDuplictedOrNotFound = new EntityUtil<User>().isEntityDuplicatedOrNotFound(
						entityManager, entity, mustUpdateExistingNew, "userIdParEMail", 
						new String[] {"email"}, new String[] {entity.getEmail()},
						AppMessageKeys.CODE_TRADUCTION_NOUVELLE_ENTITE_DUPLIQUEE, entity.getMsgVarMap(),
						AppMessageKeys.CODE_TRADUCTION_DUPLICATION_AVEC_ID_DIFFERENT, entity.getMsgVarMap(),
						AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, entity.getMsgVarMap(), 
					locale, msgList);
			
			
			if (isEntityDuplictedOrNotFound) return false ; 
			
			
			// verify that version is defined if entity id si not null
			
			if (entity.getId() != null && entity.getVersion() == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
			}
			

			// association
			
			try {
							
				EntityUtil<User> entityUtil = new EntityUtil<User>(User.class) ;
							
							
						    
				Map<String,String> msgVarMap = entity.getVille() == null 
													|| entity.getVille().getMsgVarMap() == null
													?  new HashMap<String,String>() : entity.getVille().getMsgVarMap() ;
							
							
				boolean  isAttached = entity.getVille() == null 
						            ? true 
						            :
								    entityUtil.attachLinkedEntity(entityManager, 
										entity, entity.getVille(), 
										entity.getClass().getDeclaredMethod("setVille", Ville.class), null, true, 
										locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, 
									msgVarMap, msgList);
							
							
				if (!isAttached) return false ;
							
						
			}catch(Exception ex) {
							
				String msg  = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
							entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
							
				logger.error("_209 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
							
				ex.printStackTrace();
							
				return false ;
		   }
			

			//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
		
			
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
			Set<ConstraintViolation<User>> constraintViolationList = validator.validate(entity) ;
			
			for (ConstraintViolation<User> violation : constraintViolationList) {
				
				String translatedMessage = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
				
				
			}
					
			if (!constraintViolationList.isEmpty()) return false ;
			
			logger.info("_126 fin  Verification des contraintes"); //TODO A effacer
			
			return true;
			
		
		}

		
		@Override
		public User validerEtEnregistrer(
				     User entity, 
				     boolean mustUpdateExistingNew, 
				     String namedGraph,
				     boolean isFetchGraph, 
				     Locale locale, User loggedInUser,
				     List<NonLocalizedStatusMessage> msgList) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			if (entity == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return null ;
				
			}
			
			// Appel de la methode Valider
			
			boolean estValide = valider(entity, mustUpdateExistingNew, 
					             namedGraph, isFetchGraph,locale, msgList) ;
			
			if (!estValide) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return null ; 
			}
			
			// est une creation ?
			
			boolean estCreation = entity.getId() == null ;
			
			
			logger.info("_261 Les informations de l'utilisatuer="+entityManager); //TODO A effcaer
			
			User rtn = EntityUtil.persistOrMerge(
						entityManager, User.class, entity, 
						namedGraph, isFetchGraph, 
						AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
					  locale, msgList);
			
			//Verifier si l'entité a été persistée
			
			if (rtn == null || rtn.getId() == null) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return null ;
			}
			
			logger.info("_282 Utilisateur crée="+rtn.getId()); //TODO A effacer
			
		    //Persistence du code de confirmation du compte user
			
			CodeConfirmation codeInstance = new  CodeConfirmation();
			
			//LocalDate dateHeureExpiration = LocalDate.now().plusDays(CodeConfirmation.NOMBRE_JOUR_AU_BOUT_DU_QUEL_LA_CONFIRMATION_EXPIRE);
			
			OffsetDateTime dateHeureExpiration = OffsetDateTime.now()
				.plusDays(CodeConfirmation.NOMBRE_JOUR_AU_BOUT_DU_QUEL_LA_CONFIRMATION_EXPIRE);
			
			codeInstance.setDateExpiration(dateHeureExpiration);
			codeInstance.setIdUser(rtn.getId());
			
			CodeConfirmation  code = codeConfirmationDAO
					                   .validerEtEnregistrer(codeInstance, 
					                	 mustUpdateExistingNew, namedGraph, 
					                	 isFetchGraph, locale, loggedInUser, msgList);
			
			
			//Vérifier si le code de confirmation  est persistée
			
			if(code == null ) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_CODE_CONFIRMATION_NON_CREE,
						 AppMessageKeys.CODE_TRADUCTION_CODE_CONFIRMATION_NON_CREE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return null ;
				
			}
			
			//Envoie de mail de confirmation de creation de compte user
			
			String fullNameUser = rtn.getNom() != null && 
					                      rtn.getPrenom() != null
					                      ? rtn.getNom()+" "+rtn.getPrenom()
					                      : "" ;
			
			logger.info("_318 debut sendConfirmationEmail"); //TODO A effacer
					                      
			boolean send = sendConfirmationEmail( fullNameUser, rtn.getEmail(), 
					                             code.getId(),  locale,  msgList);
			
			
			logger.info("_323 fin sendConfirmationEmail="+send); //TODO A effacer
			
			
			//Vérification de l'envoi de mail de confirmation
			
			if(!send) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI,
						 AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return null ;
				
			}
	
			
			//Historisation de la creation ou modification d'un  utilisateur 
			
			String loggedInUserId = loggedInUser.getId() != null 
					                               ? loggedInUser.getId() 
					                               : null ;
			
			String loggedInUserFullname = loggedInUser.getFullname() != null 
					                               ? loggedInUser.getFullname() 
					                               : null ;
		    
			String observation = null ;		                               
			
			HistoryEventType historyEventType = estCreation 
					? HistoryEventType.CREATION : HistoryEventType.MODIFICATION ;
			
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
									loggedInUserId, loggedInUserFullname,observation, locale) ;
				
		
			//Retourne le resultat
			
			return rtn ;
			
		}

	
		
		// compteUserHasPassWord permet de specifier si le compte doit avoir un mot de passe ou passe
	
		@Override
		public boolean validerEtActiver(
				        String idUser, 
				        boolean mustUpdateExistingNew,
				        boolean compteUserHasPassWord,
				        String namedGraph,
				        boolean isFetchGraph,
				        Locale locale, 
				        User loggedInUser, 
				        List<NonLocalizedStatusMessage> msgList) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			//Vérifier la variable idUser
			
			if(idUser == null || idUser.isEmpty() ) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;		
				
			}
			
			//Rémettre l'entité dans le contexte de persistence

			User rtn = entityManager.find(User.class, idUser) ;
			
			
			//Vérification de l'entité
			
			if (rtn == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			//Vérifier si le user est actif
			
			if(rtn.getEstActive() != null && rtn.getEstActive()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_USER_ACTIVE,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_USER_ACTIVE, // Message par defaut
						rtn.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			
			//TODO Vérification des contraintes

		
			//Modification de la propriété estActive dans le contexte de persistence 
			
			rtn.setEstActive(true) ;
			
			//Appel à la methode hisotriser pour lever les evenement d'historisation
			
			
			String loggedInUserId = loggedInUser.getId() != null 
					                               ? loggedInUser.getId() 
					                               : null ;
			
			String loggedInUserFullname = loggedInUser.getFullname() != null 
					                               ? loggedInUser.getFullname() 
					                               : null ;
			
			
		    String observation = null ;
			
			HistoryEventType historyEventType = HistoryEventType.ACTIVATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, rtn,
							loggedInUserId, loggedInUserFullname,observation, locale) ;
			
		    return true;
		    
		    
		}
		
		
	
		@Override
		public boolean confirmerUnCompteUser(
				         String code, 
				         boolean mustUpdateExistingNew, 
				         boolean compteUserHasPassWord,
				         String namedGraph, boolean isFetchGraph, 
				         Locale locale, User loggedInUser,
				         List<NonLocalizedStatusMessage> msgList) {
			
			
			//Declaration des variables
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			//Verification de la variable: code
			
			logger.info("_443 verification du code="+code); //TODO A effacer
			
			if(code == null || code.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			//Rechercher des informations portant sur le code d'information
			
			logger.info("_459 Rémettre en contexte le code de confirmation"); //TODO A effacer
			
			CodeConfirmation entityCode = entityManager
					                       .find(CodeConfirmation.class, code) ;
	
			//Verification de l'entité
			
			if (entityCode == null || entityCode.getId() == null 
					               || entityCode.getId().isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			logger.info("_479 fin Remettre en contexte le code de confirmation="+entityCode.toString()); //TODO A effacer
			
			//Vérification de l'id du user
			
			if (entityCode.getIdUser() == null || 
					         entityCode.getIdUser().isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			//Rémettre l'utilisateur dans le contexte de persistence
			
			User user = entityManager.find(User.class, entityCode.getIdUser()) ;
			
			//Verification de l'entite
			
			if (user == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			logger.info("_585 Utilisateur mis en contexte de persistence="+user.toString()); //TODO A effacer
			
			
			//Vérifier si le code correspondant au bon code de confirmation du compte
			
			if (!code.equals(entityCode.getId())) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						CodeConfirmation.CODE_TRADUCTION_CODE_CONFIRMATION_NOMATCH,
						CodeConfirmation.CODE_TRADUCTION_CODE_CONFIRMATION_NOMATCH,
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				return false ;
				
			}
			
			//Verifier si le code de confirmation est expiré
		    	//Si le code est expiré , renvoie une nouvelle notification avec une nouvelle date d'expiration
			
		    logger.info("_569 Verification du code de confirmation"); //TODO A effacer
			
			if(entityCode.getDateExpiration() != null && 
			       entityCode.getDateExpiration().isBefore(OffsetDateTime.now())) {
				
				
			// Générer des nouvelles informations pour  le code de confirmation
				
			entityCode.setId(UUID.randomUUID().toString());
			   
			entityCode.setDateExpiration(OffsetDateTime.now()
					  .plusDays(CodeConfirmation.NOMBRE_JOUR_AU_BOUT_DU_QUEL_LA_CONFIRMATION_EXPIRE));
			   
			   
			   //Envoie de notification
				
			   String fullNameUser = user.getNom() != null && 
					   						  user.getPrenom() != null
						                      ? user.getNom()+" "+user.getPrenom()
						                      : "" ;
				
			   boolean send = sendConfirmationEmail( fullNameUser, user.getEmail(), 
											   entityCode.getId(),  locale,  msgList);
				
				//Verification de l'envoi de mail de confirmation
				
				if(!send) {
					
					try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
					
					String msg  = MessageTranslationUtil.translate(locale,
							 AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI,
							 AppMessageKeys.CODE_TRADUCTION_NOTIFICATION_NON_ENVOI, 
							 msgVarMap) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
					return false ;
					
				}
				
				
			}
			
			
		   logger.info("_580 fin Verification du code de confirmation"); //TODO A effacer
			
           //Vérifier si le user est actif
			
		   if(user.getEstActive() != null && user.getEstActive()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_USER_ACTIVE,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_USER_ACTIVE, // Message par defaut
						user.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
		   }
			
		
			//Modification de la propriété estActive dans le contexte de persistence 
		   
			user.setEstActive(true) ;
			
			//Appel à la methode pour générer le mot de passe
			
			String[] TPassWords = null ;
					
		    try {
					
		        TPassWords = securityService.generateNewPasword() ;
					
			} catch (NoSuchAlgorithmException e) {
					
				return false ;
					
			}
		    
		    logger.info("_615 Mot de passe envoyé="+TPassWords[0]); //TODO A effacer
					
			user.setMotDePasse(TPassWords[1]);
			
			entityManager.flush();
			    
		    //Envoie de notification de validation compte user
				
			String email = user.getEmail() != null 
						 ? user.getEmail()
						 : null ;
					 			
			//Verification de la variable mail
								 			
			if(email == null ||  email.isEmpty()) {
								
			   String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE,
						AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE, 
									user.getMsgVarMap()) ;
			   msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
					
			   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
								
			   return false ;
								
			}
								  
		    //Initialisation de l'objet de mail
							
			String subject  = MessageTranslationUtil.translate(locale,
				    AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_VALIDATION_COMPTE_USER, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_VALIDATION_COMPTE_USER, // Message par defaut
			   user.getMsgVarMap()) ;
			
			
			if(subject == null || subject.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return false ;
				
			}
							
		    //Addresse de l'organisation
						   
			InternetAddress to  ;
						    
			to = new InternetAddress() ;
			to.setAddress(email);
					    	
			InternetAddress[] toList = {to} ;
					
			//Adresse de copie carbone 
				
			//InternetAddress cc = new InternetAddress();
							
			//cc.setAddress("info@siliconwise.biz");
							
			InternetAddress[] ccList = {} ; 
				
			//Mot de passe non crypté à envoyer à l'utilisateur
				
			String sendPassWordNonCrypte = TPassWords[0] ;
							
			String htmlContent = "" ;
							
			//Initialisation du contenu du message
						    
			String textContent  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_VALIDATION_COMPTE_USER, // venant du fichier
						AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_VALIDATION_COMPTE_USER, // Message par defaut
			    user.getMsgVarMap()) + " " + sendPassWordNonCrypte ;
			
			
			if(textContent == null || textContent.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return false ;
				
			}
			
			//Envoie de la notification
							
		   try {
			   
			   //Appel à la methode d'envoi de notification
						    	   
				emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
						    	   	
			} catch (UnsupportedEncodingException | IllegalArgumentException 
					            | MessagingException | EmailServiceException e) {
					
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION,
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION, 
							 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
			    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
									
				return false ;
									
		    }
		   
		   
			String loggedInUserId = loggedInUser.getId() != null 
				                    ? loggedInUser.getId() 
				                    : null ;

            String loggedInUserFullname = loggedInUser.getFullname() != null 
				                    ? loggedInUser.getFullname() 
				                    : null ;
			
		    String observation = null ;

			HistoryEventType historyEventType =  HistoryEventType.CONFIRMATION ;
			
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, user,
							loggedInUserId, loggedInUserFullname,observation, locale) ;

			return true ;
			
			
		}


		@Override
		public boolean validerEtModifier(
				        String currentPassword,
				        String newPassword, 
				        SessionBag sessionBag,
				        boolean mustUpdateExistingNew, 
				        String namedGraph, 
				        boolean isFetchGraph, Locale locale,
				        List<NonLocalizedStatusMessage> msgList) {
			
			
			//Declaration de la variable Map
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			boolean rtn = false ;
			
			try {
				
				//Appel à la methode pour changer de mot de passe 
				
				rtn =  securityService.changerPasswordUser(currentPassword, 
						                 newPassword, locale, sessionBag, msgList);
				
			} catch (NoSuchAlgorithmException | IllegalArgumentException e) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
						AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED, 
							 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
			    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return false ; 
				
			}
			
			return rtn ;
			
		}
		
		@Override
		public boolean validerEtDesactiver(
				        String idUser, 
				        boolean mustUpdateExistingNew, 
				        String namedGraph,
				        boolean isFetchGraph, 
				        Locale locale, User loggedInUser, 
				        List<NonLocalizedStatusMessage> msgList) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			//Vérification de la variable idUser
			
			if(idUser == null || idUser.isEmpty() ) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;	
				
				return false ;
				
			}
			
			//Remettre l'entité  dans le contexte de persistence
			
			User entity = entityManager.find(User.class, idUser) ;
			
			//Verification de l'entité
			
			if (entity == null) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
				
			}
			
			//Verifier si user est actif
			
			if(entity.getEstActive() == null || !entity.getEstActive() ) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_USER_NON_ACTIVE,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_USER_NON_ACTIVE, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				
				return false ;
				
			}
			
			//TODO Desactivation de tous les comptes utilisateurs liés au promoteur
			
			//Modification de la propriété estActive dans le contexte de persistence 
			
			entity.setEstActive(false) ;
			
			//Appel à la methode hisotriser pour lever les evenement d'historisation
			

			String loggedInUserId = loggedInUser.getId() != null 
					                               ? loggedInUser.getId() 
					                               : null ;
			
			String loggedInUserFullname = loggedInUser.getFullname() != null 
					                               ? loggedInUser.getFullname() 
					                               : null ;
			
		    String observation = null ;
			
			HistoryEventType historyEventType = HistoryEventType.DESACTIVATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
							   loggedInUserId, loggedInUserFullname,observation, locale) ;
		
			
			return true ;
		
		}

		@Override
		public User rechercherUnUserParId(
				      String id, 
				      String namedGraph,
				      boolean isFetchGraph,
				      Class<User> entityClass) {
			
			return  EntityUtil
					.findEntityById(entityManager, id, namedGraph, isFetchGraph, entityClass);
			
		}


		@Override
		public boolean regenererUnMotDePasseCompteUser(
						 String email ,
				         boolean mustUpdateExistingNew,
				         String namedGraph,
				         boolean isFetchGraph,
				         Locale locale, 
				         List<NonLocalizedStatusMessage> msgList)throws NoSuchAlgorithmException {
			
			
			//Variable Map
			
			Map<String,String> msgVarMap = new HashMap<String, String>();
			
			//Vérification de la variable email
	
			if(email == null || email.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
			}
			
			
			//Recherche du compte user par email
			
			User rtn = (new EntityUtil<User>(User.class))
					        .findSingleResultByFieldValues(
								entityManager, "userParEMail", 
								new String[] {"email"}, 
								new String[] {email}, 
						     namedGraph, isFetchGraph, new User() ) ;
			
			
			if(rtn == null) {
				
			  String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_NOT_FOUND,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_NOT_FOUND, // Message par defaut
					msgVarMap ) ;
			  
			  msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			  return false ;
				
			}
			
			//Appel à la methode pour générer un nouveau mot de passe
			
			String[] TPassWords = securityService.generateNewPasword() ;
			
			if( TPassWords == null || TPassWords.length == 0 ) return false ;
			
			rtn.setMotDePasse(TPassWords[1]);
			
			entityManager.flush();
			
			//Envoie de notification de validation compte user
			
			String sendEmail = rtn.getEmail() != null 
					 ? rtn.getEmail()
					 : null ;
				 			
			
			//Verification de la variable mail
							 			
			if(sendEmail == null ||  sendEmail.isEmpty()) {
							
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE, 
								rtn.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
							
				return false ;
							
			}
							  
						
			//Initialisation de l'objet de mail
						
			String subject  = MessageTranslationUtil.translate(locale,
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_PASSWORD_COMPTE_USER, // venant du fichier
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_PASSWORD_COMPTE_USER, // Message par defaut
			rtn.getMsgVarMap()) ;
						
			//Addresse de l'organisation
					   
			InternetAddress to  ;
					    
			to = new InternetAddress() ;
			to.setAddress(email);
				    	
			InternetAddress[] toList = {to} ;
				
			//Adresse de copie carbone 
			
		
			//InternetAddress cc = new InternetAddress();
						
			//cc.setAddress("info@siliconwise.biz");
						
			InternetAddress[] ccList = {} ; 
			
			//Mot de passe à envoyer
			
			String sendPassWordNonCrypte = TPassWords[0] ;
						
			String htmlContent = "" ;
						
			//Initialisation du contenu du message
					    
			String textContent  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_PASSWORD_COMPTE_USER, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_PASSWORD_COMPTE_USER, // Message par defaut
			rtn.getMsgVarMap()) ;
			
			//Vérification de la variable 
			
			if(textContent == null ||  textContent.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE, 
								rtn.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
							
				return false ;
							
			}
			
			//Ajout du mot de passe généré au contenu de l'email
			
			textContent =  textContent +" "+sendPassWordNonCrypte ;
			
			//Envoi de la notification
						
			try {
					    	   
				emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
					    	   	
			} catch (UnsupportedEncodingException | IllegalArgumentException
					         | MessagingException | EmailServiceException e) {
				
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION,
						 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
			    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
								
			    return false ;
			    	
			}
			
			return true ;
			
		}

		
		@Override
		public boolean validerEtSupprimer(
				         String idUser, 
				         boolean mustUpdateExistingNew, 
				         String namedGraph,
				         boolean isFetchGraph, 
				         Locale locale, User loggedInUser,
				         List<NonLocalizedStatusMessage> msgList) {
			
			
			// Vérification de la variable idUser
			
			if(idUser == null || idUser.isEmpty() ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return false ; 
				
			}
		
			//Remettre l'entite dans le contexte de persistence avec utilisation de namedGraph

			User entity  =  EntityUtil
					        .findEntityById(entityManager, idUser, namedGraph,  isFetchGraph, User.class) ;
			
			
			//Vérification de l'entiité mis en contexte de persistence
			
			if (entity == null || entity.getId() == null 
					           || entity.getId().isEmpty()) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
			   return false ;
			   
			}
			
			
			//TODO Vérifier aucune transaction ne soit associée au promoteur
			

			entityManager.remove(entity);
			
			//Appel à la methode hisotriser pour lever les evenement d'historisation
			
			String loggedInUserId = loggedInUser.getId() != null 
					                  ? loggedInUser.getId() 
					                  : null ;
			
			String loggedInUserFullname = loggedInUser.getFullname() != null 
					                  ? loggedInUser.getFullname() 
					                  : null ;
			
			
		   String   observation  = entity.getNom() != null && entity.getPrenom() != null
						              ? entity.getNom() + " " + entity.getPrenom()
						        	  : null ;                             
			
			HistoryEventType historyEventType = HistoryEventType.SUPPRESSION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
							   loggedInUserId, loggedInUserFullname,observation, locale) ;
			
			return true ; 
		
		}
		
		
	   @Override
	   public boolean sendConfirmationEmail(
			            String fullNameUser, 
						String emailUser, 
						String codeConfirmatation, 
						Locale locale, List<NonLocalizedStatusMessage> msgList) 
					  throws UnirestException {
			
		   
			//Récuperation du contenu du message
			
			String emailContent = null ;
			
			Map<String,String> msgVarMap = new HashMap<String,String>();
			
		
			//Initialisation de l'objet du message
		
			String subject  = MessageTranslationUtil.translate(locale,
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_USER, // venant du fichier
				 AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_USER, // Message par defaut
			msgVarMap) ;
			
			//Vérifier si l'objet est bien chargé sinon annule toute l'operation par un rollBack
			
			if (subject == null || subject.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 

				logger.info("sendConfirmationEmail() :: Erreur lecture fichier modele email. msg=" + msg);
				
			    return false ;
				 
			}
						
			//Initialisation du contenu du message
		    
			emailContent  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_USER, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_CONFIRMATION_CREATION_COMPTE_USER, // Message par defaut
			msgVarMap) ;
			
			//Vérifier si l'objet est bien chargé sinon annule toute l'operation par un rollBack
					
			if (emailContent == null || emailContent.isEmpty()) {
						
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				logger.info("sendConfirmationEmail() :: Erreur lecture fichier modele email. msg=" + msg);
				
			    return false ;
				 
			}
			
			// Verifier que l'url front end de confirmation est définie
			
			String urlFrontEndConfirmationPattern = appConfig
				.getProperty(AppConfigKeys.CREATION_COMPTE_USER_CONFIRMATION_FRONT_END_PATH_PATTERN);
					
			//Vérifier si l'url est non et non vide sinon faire un rollbakc pour annuler toute la transaction
			
			if (urlFrontEndConfirmationPattern == null || urlFrontEndConfirmationPattern.isEmpty()) {
						
				String msg  = MessageTranslationUtil.translate(locale,
						AppConfigKeys.CREATION_COMPTE_USER_CONFIRMATION_FRONT_END_PATH_PATTERN_NOT_DEFINED,// venant du fichier
						AppConfigKeys.CREATION_COMPTE_USER_CONFIRMATION_FRONT_END_PATH_PATTERN_NOT_DEFINED, // Message par defaut
						msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
		
				logger.info("sendConfirmationEmail() :: Front end url non defini dan sinfo specifique. msg=" + msg);
				
				return false ;
				
			}
			
			// Construction de l'url de call back envoyé dans le courriel de confirmation
			
			urlFrontEndConfirmationPattern = urlFrontEndConfirmationPattern
					.replaceAll(AppConfigKeys.CONFIRMATION_URL_VARIABLE_USER_CODE, codeConfirmatation) ;


			//Récuperation de base URL
			
			String urlCallBack = appConfig
								 .getProperty(AppConfigKeys.CONFIRMATION_COMPTE_USER_BASE_URL);
			
			
			//Vérification de l'url de base 
			
			if (urlCallBack == null || urlCallBack.isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				logger.info("sendConfirmationEmail() :: Erreur lecture fichier modele email. msg=" + msg);
				
			    return false ;
				 
			}
			
			
			urlCallBack += (urlCallBack.lastIndexOf("/") == urlCallBack.length() - 1 ? "" : "/") // Pas de caractere "/" à la fin	
							+ urlFrontEndConfirmationPattern ;
			
			
			//TODO A mettre à jour le lien dans le fichier de configuration 
			
			String urlCallBack1 = "http://10.153.100.18:8080/#/ActiverCompteUtilisateur?code="+codeConfirmatation;
			
			
			logger.info("_1232 Affichage urlCallBack="+urlCallBack); //TODO A effacer
			
			// construction du contenu de l'email et de son sujet

			emailContent = emailContent
					.replaceAll(AppConfigKeys.CREATION_COMPTE_USER_CONFIRMATION_EMAILVARIABLE_NAME, 
						    	fullNameUser)
					.replaceAll(AppConfigKeys.CREATION_COMPTE_USER_CONFIRMATION_EMAILVARIABLE_URL,
							urlCallBack1);
			
			//Verification de l'adresse email de l'utilisateur
							 			
			if(emailUser == null ||  emailUser.isEmpty()) {
							
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE,
					AppMessageKeys.CODE_TRADUCTION_USER_ADRESSE_MAIL_NON_DEFINIE, 
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
							
				return false ;
							
			}
							  
				
			InternetAddress to  ;
					    
			to = new InternetAddress() ;
			to.setAddress(emailUser);
				    	
			InternetAddress[] toList = {to} ;
				
			//Adresse de copie carbone 
			
			InternetAddress[] ccList = {} ; 
						
			// Envoyer de la notification pour la confirmation de compte
			
		   logger.info("_1353 debut d'envoie de mail "); //TODO A effacer
			
		   try {
				
			   // Appel à la methode proprement dite pour l'envoie de la notification
			   
			   logger.info("_1359 debut d'envoie de mail "); //TODO A effacer
		    	   
			   emailService.sendEmail(subject, toList, ccList, emailContent, null);
					    	   	
			} catch (UnsupportedEncodingException | IllegalArgumentException | MessagingException | EmailServiceException e) {
				
				
			   String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION,
						 AppMessageKeys.CODE_TRADUCTION_ERREUR_ENVOI_NOTIFICATION, 
						 msgVarMap) ;
			   msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			   
			   logger.info("_1372 fin catch d'envoie de mail "); //TODO A effacer
				
			   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
								
			   return false ;
								
			}
			
			return true ;
			
		}
	   
	   

		/**
		 * 
		 * Revue des methodes faite par Alzouma Moussa Mahamadou
		 * 
		 * Date: 24/09/2021
		 * 
		 */

		/**
		 * Check user encrypted password against a given plain (on ecrypted) password. 
		 * Returns true if these passwords match
		 * @param user
		 * @param plainPassword
		 * @return
		 * @throws NoSuchAlgorithmException
		 * @throws IllegalArgumentException
		 */
		@Override
		public boolean checkPassword(  
				        User user, 
				        String plainPassword,
				        Locale locale ,
				        List<NonLocalizedStatusMessage> msgList)
						throws NoSuchAlgorithmException, IllegalArgumentException {
			
			
			if (user == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
			   return false ;
			}
			
			if (plainPassword == null) return false;
			
			String md5password = Utility.encryptToMD5(plainPassword);
			
			return(user.getMotDePasse() != null && user.getMotDePasse().equals(md5password));
			
		}
		
		
		@Override
		public User findUserByEmailAndPlainPassword(
				       String email,
				       String plainPassword,
				       String namedGraph,
				       boolean isFetchGraph) throws NoSuchAlgorithmException {
			
			
			//Verification de l'email et le mot de passe
			
			if (email == null || email.isEmpty() || 
					plainPassword == null || plainPassword.isEmpty()) return null ;
			
			
			// compare password
			
			String motDePasse = plainPassword != null 
					? Utility.encryptToMD5(plainPassword)
					: null ;
					
			logger.info("_1348 Mot de passe crypté="+motDePasse);

			User rtn = (new EntityUtil<User>(User.class))
						   .findSingleResultByFieldValues(
							  entityManager, "userByEmailAndPassword", 
							    new String[] {"email", "motDePasse"}, 
								new String[] {email, motDePasse},  
						      namedGraph, isFetchGraph, new User() ) ;
			
			
			logger.info("_1358 Utilisateur trouvé="+rtn.toString()); //TODO A effacer
			
			return rtn ;
			
			
		}
		
		
		@Override
		public User rechercherUnUserParEmail(
				       String email, 
				       String namedGraph, 
				       boolean isFetchGraph,
				       Class<User> entityClass) {
			
			
			//Vérification de la variable
			
			if (email == null || email.isEmpty() ) return null ;
			
			//Appelle à la requếte nommée

			User rtn = (new EntityUtil<User>(User.class))
						   .findSingleResultByFieldValues(
							  entityManager, "userParEMail", 
							  new String[] {"email"}, 
							  new String[] {email},  
		    "graph.user.minimum", isFetchGraph, new User() ) ;
			
			return rtn ;
			
		}
		
}
