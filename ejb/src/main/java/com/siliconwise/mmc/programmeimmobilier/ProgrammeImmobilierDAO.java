package com.siliconwise.mmc.programmeimmobilier;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.Ville;
import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.common.event.oldhistorique.HistoriserEventPayload;
import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.ModeFinancement;
import com.siliconwise.mmc.modefinancement.RechercherModeFinancementCtlInterface;
import com.siliconwise.mmc.modefinancement.SupprimerModeFinancementCtlInterface;
import com.siliconwise.mmc.modefinancement.ValiderModeFinancementCtlInterface;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mamahadou
 *
 */
@Stateless
public class ProgrammeImmobilierDAO implements ProgrammeImmobilierDAOInterface{
	
		
		@Resource
		private EJBContext ejbContext;
	
		@PersistenceContext
		private EntityManager entityManager;
		
	    @Inject ProgrammeImmobilierDocumentCtlInterface programmeImmobilierDocumentCtl ;
		
		@Inject SupprimerModeFinancementCtlInterface supprimerModeFinancementCtl ;
		
		@Inject DocumentCtlInterface  documentCtl ;
		
		@Inject RechercherModeFinancementCtlInterface rechercherModeFinancementCtl ;
		
		@Inject EmailService emailService ;
				
		private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
		
		// evenement d'historisation
		
		@Inject
		Event<HistoriserEventPayload<ProgrammeImmobilier>> historiserEVent ;
		
		HistoriserEventPayload<ProgrammeImmobilier> payload = new HistoriserEventPayload<>() ;
		
		@Inject ValiderModeFinancementCtlInterface validerModeFinancementCtl ;
		
		@Inject Event<HistoryEventPayload<ProgrammeImmobilier>> historyEvent ;
		
			
		/**
		 * @param entity
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param msgList
		 * @return
		 */
		public boolean  valider(ProgrammeImmobilier entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph,
					Locale locale,
					List<NonLocalizedStatusMessage> msgList) {
			
		    
			//Verification de l'entity
	
			if(entity.getCode() == null || entity.getPromoteur() == null 
									    || entity.getPromoteur()
									             .getIdentifiantLegal() == null) {
				
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI, // Message par defaut
						entity.getMsgVarMap()) ;
				
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			
			// recherche de doublon
	
			boolean isEntityDuplictedOrNotFound = new EntityUtil<ProgrammeImmobilier>().isEntityDuplicatedOrNotFound(
									entityManager, entity, mustUpdateExistingNew, "programmeImmobilierIdParCodeParPromoteur", 
									new String[] {"code","promoteur"}, new String[] {entity.getCode(),entity.getPromoteur().getIdentifiantLegal()},
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
				
				
				EntityUtil<ProgrammeImmobilier> entityUtil = new EntityUtil<ProgrammeImmobilier>(ProgrammeImmobilier.class) ;
				
				
			    //ManyToOne ProgrammeImmobilier vers Promoteur
				 //Verifie si le promoteur est non nul 
				
				 Map<String,String> msgVarMap = entity.getPromoteur() == null 
													|| entity.getPromoteur().getMsgVarMap() == null
											   ?  new HashMap<String,String>() : entity.getPromoteur().getMsgVarMap() ;

					
				boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
										entity, entity.getPromoteur(), 
										entity.getClass().getDeclaredMethod("setPromoteur", Promoteur.class), null, true, 
										locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
				
				
				  if (!isAttached) return false ;
				
				
				 //ManyToOne ProgrammeImmobilier vers Ville
				
				  msgVarMap = entity.getVille() == null 
							|| entity.getVille().getMsgVarMap() == null
					   ?  new HashMap<String,String>() : entity.getVille().getMsgVarMap() ;

				  isAttached = entityUtil.attachLinkedEntity(entityManager, 
								entity, entity.getVille(), 
								entity.getClass().getDeclaredMethod("setVille", Ville.class), null, true, 
								locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
				  
				  
				  if (!isAttached) return false ;
				  
					
			} 
			catch(Exception ex) {
				
				
				String msg  = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
						entity.getMsgVarMap()) ;
				
		        msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
		
				//logger.error("_1 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
				ex.printStackTrace();
				
				return false ;
				
			}
			
			
			
			//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
			
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
			Set<ConstraintViolation<ProgrammeImmobilier>> constraintViolationList = validator.validate(entity) ;
			
			for ( ConstraintViolation<ProgrammeImmobilier> violation : constraintViolationList) {
				
				
				String translatedMessage = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
				
			}
					
			if (!constraintViolationList.isEmpty()) return false ;
			
			return true;
			
		}
	
	
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
					List<NonLocalizedStatusMessage> msgList){
		
			
					//Locale langue = SessionUtil.getLocale(currentSession) ;
				
					if (entity == null) {
						
						Map<String,String> msgVarMap =  new HashMap<String,String>();
						
						String msg  = MessageTranslationUtil.translate(locale,
								 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
								 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
								 msgVarMap) ;
						msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						
						return null ;
					}
					
					
					boolean estValide = valider(entity, mustUpdateExistingNew, 
							             namedGraph, isFetchGraph,locale, msgList) ;
					
					
					// est une creation ?
					
					boolean estCreation = entity.getId() == null ;
					
					/*
					String action = entity.getId() == null
							              ? Reference.REF_ELEMENT_CREER 
                                          : Reference.REF_ELEMENT_MODIFIER ;*/
					
					
					if (!estValide) {
						
						try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						
						return null ; 
						
					}
					
					//Methode de persistence de l'entité correspondante
					
					ProgrammeImmobilier rtn = EntityUtil.persistOrMerge(
							entityManager, ProgrammeImmobilier.class, entity, 
							namedGraph, isFetchGraph, 
							AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
							AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
							AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
							locale, msgList);
				
					
					if (rtn == null) {
						
						try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
						
						return null ;
						
					}
					
				
					//Appel à la methode d'historisation
					
					String loggedInUserId = loggedInUser.getId() != null 
							                               ? loggedInUser.getId() 
							                               : null ;
					
					String loggedInUserFullname = loggedInUser.getFullname() != null 
							                               ? loggedInUser.getFullname() 
							                               : null ;
							                               
				    String   observation = null ;
					
					HistoryEventType historyEventType = estCreation 
							? HistoryEventType.CREATION : HistoryEventType.MODIFICATION ;
					
					HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
										loggedInUserId, loggedInUserFullname,observation, locale) ;
					
					
					//Retourne le resultat
					
					return rtn ;
					
					
		}


		@SuppressWarnings("unchecked")
		@Override
		public boolean validerEtConfirmer(
						String  idProgrammeImmobilier,
						boolean mustUpdateExistingNew, String namedGraph, 
						boolean isFetchGraph, Locale locale,
						User loggedInUser, List<NonLocalizedStatusMessage> msgList) {
			
				
				//Verification de la variable idProgrammeImmobilier
			
			    if(idProgrammeImmobilier == null) return false ;
			    
			    //Remettre l'entité produit logement dans le contexte de persistence
				
			    ProgrammeImmobilier entity = entityManager
			    		                      .find(ProgrammeImmobilier.class, idProgrammeImmobilier) ;
				
				//Verification de l'entité
				
				if (entity == null) {
					
					Map<String,String> msgVarMap =  new HashMap<String,String>();
					
					String msg  = MessageTranslationUtil.translate(locale,
							 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
							 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
							 msgVarMap) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
					return false ;
					
				}
				
				
				//Appel à la requête pour retourner tous les modes de financements
				
				//Appartenant au programme immobilier 
				//qui devront être valider par le promoteur
				
				List<ModeFinancement> rtnFinancementList =  entityManager
					.createNamedQuery("modeFinancementsDunProgrammeImmobilierNonValides")
					.setParameter("IdProgramme", entity.getId())
					.getResultList() ;
	
				if(rtnFinancementList != null && rtnFinancementList.size() > 0) {
					
					List<ModeFinancement> rtnList = new ArrayList<ModeFinancement>();
					
					rtnList =	validerModeFinancementCtl
									.validerOuNotifierModeFinancementList(
										rtnFinancementList, mustUpdateExistingNew, 
										entity, namedGraph, isFetchGraph, locale, 
										loggedInUser, msgList);
					
					
				}
				
	            //verification d'integrite complexes(règles metiers) le cas echeant
				
				//	Tout programme immobilier validé doit avoir au moins un produit logement validé
				
				//Au moins un produit logement validé doit être rattaché au programme immobilier
			
				// Requête pour obtenir les produits appartenant au ProgrammeImmobilier
						
				List<ProduitLogement> rtnList = entityManager
						.createNamedQuery("produitLogementIdParProgrammeImmobilier")
						.setParameter("programmeimmobilier", entity.getCode())
						.getResultList() ;
				
				
				if (! (rtnList.size() >= 1)) {
								
					String msg  = MessageTranslationUtil.translate(locale,
						ProgrammeImmobilier.CODE_TRADUCTION_PROGRAMME_IMMOBILIER_VALIDE_PAR_PROMOTEUR_PRODUIT_LOGEMENT_NON_TROUVE,// venant du fichier
						ProgrammeImmobilier.CODE_TRADUCTION_PROGRAMME_IMMOBILIER_VALIDE_PAR_PROMOTEUR_PRODUIT_LOGEMENT_NON_TROUVE, // Message par defaut
						entity.getMsgVarMap()) ;
					msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
								
					return false ;
								
				}
				
				//Tout programme immobilier validé doit avoir au moins un mode de financement validé rattaché
				
				//	    Construction de la requête 
				//		Trouver le nombre de mode financement validé du programme immobilier auquel est rattaché le produit logement
				//		ce nombre doit étre superiuer ou égal 1		
					
				Long 	rtnResult =  (Long) entityManager
							.createNamedQuery("nombreModeFinancementValideParProgrammeImmobilier")
							.setParameter("idProgrammeImmobilier", entity.getId())
							.getSingleResult() ;
						
						
			    if (! (rtnResult >= 1) ) {
							
					String msg  = MessageTranslationUtil.translate(locale,
							AppMessageKeys.CODE_TRADUCTION_PROGRAMME_IMMOBILIER_VALIDE_PAR_MODEFINANCEMENT_VALIDE_NON_TROUVE,// venant du fichier
							AppMessageKeys.CODE_TRADUCTION_PROGRAMME_IMMOBILIER_VALIDE_PAR_MODEFINANCEMENT_VALIDE_NON_TROUVE, // Message par defaut
							entity.getMsgVarMap()) ;
					msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
							
					return false ;
							
				}
						
						
			 //Modifier lal propriété estValide dans le contexte de persistence
			
			 entity.setEstValide(true);
			
			
			
			 //Envoie d'une notification aux EFis non partenaires du programme immobilier validé
			
			
			if(entity.getEstValide() != null && entity.getEstValide()) {
				
			
			 //Obtenir la liste d'adresses email des Efis partenaires à un programme immobilier
				
			 //Envoie des notifications aux Efis non parteanires pour les inviter à regarder le nouveau programme immobilier publié
				  
			 //Appel à la requete pour trouver les emails des EFis non  partenaires à un  programme immobilier
				
			 List<String> emailList = new ArrayList<String>() ;
			 
			 String idPaysPromoteur = null ;
					
			 try {
				 
				 
			   //Recherche de l'id du pays auquel appartient le promoteur du dit programme immobilier
				 
			   idPaysPromoteur = entity.getPromoteur() != null 
						               && entity.getPromoteur().getVille() != null 
						               && entity.getPromoteur().getVille().getPays() != null 
						               && entity.getPromoteur().getVille().getPays().getId() != null
						               ?  entity.getPromoteur().getVille().getPays().getId() 
						               : "" ; 
						
				emailList =  entityManager
							  .createNamedQuery("listeEmailEfisNonPartenairesProgrammeImmbolier")
							  .setParameter("idProgrammeImmobilier", entity.getId())
							  .setParameter("idPaysPromoteur", idPaysPromoteur)
							  .getResultList() ;
				
						
			 } catch (NoResultException e) {
						
					
						
			 }
				
				     
			//Vérification de la variable emailList
					
			if(emailList != null && emailList.size() > 0 ) {
					
				for(String email: emailList) {
							
					 if(email != null && !email.isEmpty()) {
							  
					   //Initialisation des entrants pour l'envoyer mail
					
					   String subject  = MessageTranslationUtil.translate(locale,
							   AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_AUX_EFIS_NON_PARTENAIRES, // venant du fichier
							   AppMessageKeys.CODE_TRADUCTION_SUBJECT_ENVOI_NOTIFICATION_AUX_EFIS_NON_PARTENAIRES, // Message par defaut
							 entity.getMsgVarMap()) ;
					   
					   
					   //Vérifier si l'objet a été bien chargé
					   
					   if(subject == null || subject.isEmpty()) {
							
							String msg  = MessageTranslationUtil.translate(locale,
									 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
									 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
									 entity.getMsgVarMap()) ;
							msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
							
						    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
							
							return false ;
							
						}
							
						   
					  //Addresses email des EFis partenaires
						   
					  InternetAddress to  ;
						    
				      to = new InternetAddress() ;
					  to.setAddress(email);
					    	
					  InternetAddress[] toList = {to} ;
					    
					  //Adresse de copie carbone du  promoteur du programme immobilier 
							
					  InternetAddress cc = new InternetAddress();
							
					  cc.setAddress(entity.getPromoteur() != null 
									       && entity.getPromoteur().getEmail() != null
									        ?  entity.getPromoteur().getEmail()
									        : "");
							
					  InternetAddress[] ccList = {cc} ;
							
					  String htmlContent = "" ;
							
							
					  String textContent  = MessageTranslationUtil.translate(locale,
								 AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_AUX_EFIS_NON_PARTENAIRES, // venant du fichier
								 AppMessageKeys.CODE_TRADUCTION_ENVOI_NOTIFICATION_AUX_EFIS_NON_PARTENAIRES, // Message par defaut
						entity.getMsgVarMap()) ;
					  
					  
					  //Verifier si le contenu du mail a été bien chargé 
					  
					  if(textContent == null || textContent.isEmpty()) {
							
							String msg  = MessageTranslationUtil.translate(locale,
									AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
									AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
										 entity.getMsgVarMap()) ;
						    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
								
							try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
								
						    return false ;
						    
						  }
						
						
					     //try catch d'nevoi de notification
					     
					   	  try {
						    	    //Appel à la methode d'envoi de notification
					   		  
					   				emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);
						    	   	
					    	   } catch (UnsupportedEncodingException | IllegalArgumentException 
					    						| MessagingException | EmailServiceException e) {
									
									logger.info("_560 Erreurs d'envoie de la notification aux non  Efis="+e);
									
						       }
						        
						    }   
							   
				        }
				   
			        }
	
			   }
	       
	        
			//Appel à la methode d'historisation
			
			String loggedInUserId = loggedInUser.getId() != null 
					                               ? loggedInUser.getId() 
					                               : null ;
			
			String loggedInUserFullname = loggedInUser.getFullname() != null 
					                               ? loggedInUser.getFullname() 
					                               : null ;
		     
	        String   observation  = null ;                               
			
			HistoryEventType historyEventType =  HistoryEventType.VALIDATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
							   loggedInUserId, loggedInUserFullname,observation, locale) ;
		
			
			//Retourne le resultat
			
			return true  ;
		
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<ProgrammeImmobilier> tousLesProgrammesImmobiliers() {
			
			
			List<ProgrammeImmobilier> rtnList = new ArrayList<>() ;
			
			try {
				
				rtnList = (List<ProgrammeImmobilier>) entityManager
					.createNamedQuery("toutesLesProgrammesImmobiliers")
					.getResultList();
				
			} catch (NoResultException e) {

				return null ;
			}
			
			return rtnList ;
			
	
		}

		
		//Suppression d'un programme immobilier
		@Override
		public boolean supprimer(
							String idProgrammeImmobilier, 
							boolean mustUpdateExistingNew,
							String namedGraph, boolean isFetchGraph, 
							Locale locale, User loggedInUser,
							List<NonLocalizedStatusMessage> msgList) {
			
			
			// Initialisation de la variable rtn
			
			boolean rtn = true ;
			
			//Verification de l'id du programme immobilier à supprimer
			
			if(idProgrammeImmobilier == null ||  idProgrammeImmobilier.isEmpty()) {
							
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
							
				String msg  = MessageTranslationUtil.translate(locale,
					 ProgrammeImmobilier.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
					 ProgrammeImmobilier.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
						msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
							
				return false ;
				
							
			}
			
			//Remettre l'entité dans le contexte de persistence
			//Utilisation de namedGraph
			
			ProgrammeImmobilier programmeImmobilier = 
					                 EntityUtil.findEntityById(entityManager, idProgrammeImmobilier, 
					                		     namedGraph, isFetchGraph, ProgrammeImmobilier.class);
			
			
			//Verification de l'entité mise en contexte de persistenceù
			
			if (programmeImmobilier == null) {
				
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				rtn = false ; 
				
				return rtn ;
				
			}
			
			//Vérification des contraintes d'intégrités fonctionnelles
			
			//Vérifier qu'aucun produit logement n'est rattaché au programme immobilier
			
			Long result = (Long) entityManager
									.createNamedQuery("produitLogementsParProgrammeImmobilierId")
									.setParameter("idProgrammeImmobilier", programmeImmobilier.getId())
									.getSingleResult();
			
			
			if(result > 0 ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					 ProgrammeImmobilier.CODE_TRADUCTION_PROGRAMMEIMMOBILIER_A_AU_MOINS_UN_PRODUILOGEMENT, // venant du fichier
					 ProgrammeImmobilier.CODE_TRADUCTION_PROGRAMMEIMMOBILIER_A_AU_MOINS_UN_PRODUILOGEMENT, // Message par defaut
						msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
							
				rtn = false ; 
				
				return rtn ;
				
			}
			
			
			//Recherche et Suppression des entités de type d'association d'agregation
			   //Ici, cette partie est deja géréé dans la verification des contraintes fonctionnelles
			
			
			//Verification et suppression des associations composites
			
			
			
			//Recherche d'eventuels mode de financements appartenant au programme immobilier
			
			List<ModeFinancement> rtnList = rechercherModeFinancementCtl
					                         .rechercherModeFinancementListParProgrammeImmobilier(
					                           idProgrammeImmobilier, mustUpdateExistingNew,
					                           namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			
			
		    //Si la taille de mode de financements est superieure à 0 et non vide
		    //Appel à la methode supprimerModeFinancementList
		    
			if(rtnList != null  && rtnList.size()>0 ) {
				
				boolean deleteFinancement 
				              = supprimerModeFinancementCtl
								     .supprimerModeFinancementList(rtnList, mustUpdateExistingNew, 
								          	namedGraph, isFetchGraph, locale,loggedInUser, msgList);
			
			  if(!deleteFinancement)  return false ;
			  
			}
			
			
			//Recherche de documents eventuels appartenant au programme immobilier 
			
			List<Document>	entityList 
			                  = programmeImmobilierDocumentCtl
			                     .rechercherDocumentListParIdProgrammeImmobilier(
			                    	idProgrammeImmobilier,  mustUpdateExistingNew, 
			                    	namedGraph, isFetchGraph, locale, msgList) ;
			
			
			//Si la taille de la variable entityList est superieure à 0 et non nulle
			//Alors Appel de la methode supprimerDocumentList pour la suppression de la lkiste de documents
			
			if(entityList != null && entityList.size()> 0) {
				
				
				//Suppresssion des informations des informations de la classe associtaion ProgrammeImmobilierDocument
				//Pour eviter des erreurs d'intégrité qui peuvent être générées par JPA
					
				int   deleteClassProgrammeImmobilierDocument 
							              =  entityManager
											   .createNamedQuery("suppressionInformationsProgrammeImmobilierDocument")
											   .setParameter("idProgrammeImmobilier", idProgrammeImmobilier)
											   .executeUpdate();
				
				//Vérification de la suppression de la classe d'asscoiation ProgrammeImmobilierDocument

			    if(deleteClassProgrammeImmobilierDocument == 0) {
			    	
			    	try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			    	
			    	return false ;
			    	
			    }
			    
			    //Appel à la methode supprimerDocumentList pour supprimer la liste de documents rattachés
			    //au programme immobilier en question
					
		    	boolean	 deleteDocument = documentCtl
										    .supprimerDocumentList(
											  entityList, mustUpdateExistingNew,
											  namedGraph,isFetchGraph, locale,  
											  loggedInUser, msgList);
			  
				if(!deleteDocument)  return false ;
				
				
			}
			
			
			//Suppression définitive du programme immobilier 
			
			entityManager.remove(programmeImmobilier);
			

			//Appel à la methode d'historisation
			
			String loggedInUserId = loggedInUser.getId() != null 
					                    ? loggedInUser.getId() 
					                    : null ;
			
			String loggedInUserFullname = loggedInUser.getFullname() != null 
						                    ? loggedInUser.getFullname() 
						                    : null ;
						                    
            String   observation  = programmeImmobilier.getDesignation() != null
					        			    ? programmeImmobilier.getDesignation() 
					        				: null;
						
			HistoryEventType historyEventType =  HistoryEventType.VALIDATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent,historyEventType,programmeImmobilier,
					                       loggedInUserId, loggedInUserFullname,observation, locale) ;
		
			
			//Retourne le resultat
			
			return rtn;
			
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public List<ProgrammeImmobilier> 
							rechercherProgrammeImmobilierParPromoteur(
									String idPromoteur,
									boolean mustUpdateExistingNew, 
									String namedGraph, boolean isFetchGraph, 
									Locale locale,User loggedInUser, 
									List<NonLocalizedStatusMessage> msgList) {
			
			
			//Verification de la variable idPromoteur récu
			
			if (idPromoteur == null || idPromoteur.isEmpty() ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return null ;
				
			}
			
			//Initialisation de la variable qui sera retournée par la methode
			
			List<ProgrammeImmobilier> rtnList = new ArrayList<>() ;
			
			//Execution de la requête dans un try pour lever l'exeception NoResultException
			
			try {
				
				rtnList = (List<ProgrammeImmobilier>) entityManager
					.createNamedQuery("rechercherProgrammeImmobilierParPromoteur")
					.setParameter("promoteurId", idPromoteur)
					.getResultList();
				
			} catch (NoResultException e) {

				return null ;
			}
			
			
			//Retourne la liste de programme immobiliers trouvés
			
			return rtnList ;
			
		}
		

}
