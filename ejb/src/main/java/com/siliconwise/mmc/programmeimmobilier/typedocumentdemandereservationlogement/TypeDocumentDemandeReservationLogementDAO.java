package com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement;


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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.common.event.oldhistorique.HistoriserEventPayload;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mamahadou
 *
 */
@Stateless
public class TypeDocumentDemandeReservationLogementDAO implements TypeDocumentDemandeReservationLogementDAOInterface{
	
		
		@Resource
		private EJBContext ejbContext;
	
		@PersistenceContext
		private EntityManager entityManager;
		
				
		private static transient Logger logger = LoggerFactory.getLogger(TypeDocumentDemandeReservationLogementDAO.class) ;
		
		// evenement d'historisation

		
		HistoriserEventPayload<TypeDocumentDemandeReservationLogement> payload = new HistoriserEventPayload<>() ;
		
		@Inject Event<HistoryEventPayload<TypeDocumentDemandeReservationLogement>> historyEvent ;
		
			
		/**
		 * @param entity
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param msgList
		 * @return
		 */
		public boolean  valider(TypeDocumentDemandeReservationLogement entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph,
					Locale locale,
					List<NonLocalizedStatusMessage> msgList) {
			
			
		
			//Verification de  la propri??t?? designation
	
			
			/*
			if(entity.getDesignation() == null ) {
				
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI, // Message par defaut
						entity.getMsgVarMap()) ;
				
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			
			// recherche de doublon
	
			boolean isEntityDuplictedOrNotFound = new EntityUtil<TypeDocumentDemandeReservationLogement>().isEntityDuplicatedOrNotFound(
									entityManager, entity, mustUpdateExistingNew, "typeDocumentDemandeReservationLogementParIdParDesignationParProgramme", 
									new String[] {"designation","code"}, new String[] {entity.getDesignation(),entity.getProgrammeImmobilier().getCode()},
									AppMessageKeys.CODE_TRADUCTION_NOUVELLE_ENTITE_DUPLIQUEE, entity.getMsgVarMap(),
									AppMessageKeys.CODE_TRADUCTION_DUPLICATION_AVEC_ID_DIFFERENT, entity.getMsgVarMap(),
									AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, entity.getMsgVarMap(), 
									locale, msgList);

			if (isEntityDuplictedOrNotFound) return false ; 
			
			*/
			
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
				
				
				EntityUtil<TypeDocumentDemandeReservationLogement> entityUtil = new EntityUtil<TypeDocumentDemandeReservationLogement>(TypeDocumentDemandeReservationLogement.class) ;
				
				
			    //ManyToOne CaracteristiqueDemandeReservationLogement vers ProgrammeImmobilier
				 //Verifie si le promoteur est non nul 
				
				 Map<String,String> msgVarMap = entity.getProgrammeImmobilier() == null 
													|| entity.getProgrammeImmobilier().getMsgVarMap() == null
											   ?  new HashMap<String,String>() : entity.getProgrammeImmobilier().getMsgVarMap() ;

					
				boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
										entity, entity.getProgrammeImmobilier(), 
										entity.getClass().getDeclaredMethod("setProgrammeImmobilier", ProgrammeImmobilier.class), null, true, 
										locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
				
				
				  if (!isAttached) return false ;
				
				
				 //OneToOne CaracteristiqueDemandeReservationLogement vers Reference
				
				  msgVarMap = entity.getTypeDocument() == null 
							|| entity.getTypeDocument().getMsgVarMap() == null
					   ?  new HashMap<String,String>() : entity.getTypeDocument().getMsgVarMap() ;

				  isAttached = entityUtil.attachLinkedEntity(entityManager, 
								entity, entity.getTypeDocument(), 
								entity.getClass().getDeclaredMethod("setTypeDocument", Reference.class), null, true, 
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
			
			
			
			//Validation des contraintes simples portant sur la validation des annotations des propri??t??s de classe
			
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
			Set<ConstraintViolation<TypeDocumentDemandeReservationLogement>> constraintViolationList = validator.validate(entity) ;
			
			for ( ConstraintViolation<TypeDocumentDemandeReservationLogement> violation : constraintViolationList) {
				
				
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
		public TypeDocumentDemandeReservationLogement validerEtEnregistrer(
				TypeDocumentDemandeReservationLogement entity,
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
					
					//Methode de persistence de l'entit?? correspondante
					
					TypeDocumentDemandeReservationLogement rtn = EntityUtil.persistOrMerge(
							entityManager, TypeDocumentDemandeReservationLogement.class, entity, 
							namedGraph, isFetchGraph, 
							AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
							AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
							AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
							locale, msgList);
				
					
					if (rtn == null) {
						
						try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
						
						return null ;
						
					}
					
				
					//Appel ?? la methode d'historisation
					
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


}
