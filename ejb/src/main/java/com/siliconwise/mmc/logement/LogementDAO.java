package com.siliconwise.mmc.logement;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * Cette classe renferme les informations relatives à la classe d'acces Produit
 * Logement Ses mehodes sont implementées par l'interface ProduitLogementInt
 * 
 * @author Alzouma Moussa Mahamadou @ date 29/03/2022
 */
@Stateless
public class LogementDAO implements LogementDAOInterface {

		
		@Resource
		private EJBContext ejbContext;
	
		@PersistenceContext
		EntityManager entityManager;
		
				
		private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
		
		// evenement d'historisation
		
		
		@Inject Event<HistoryEventPayload<Logement>> historyEvent ;
		
	
		
		public boolean  valider(Logement entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph,
					Locale locale,
					List<NonLocalizedStatusMessage> msgList) {
			
		
			//Vérifier la designation du logement
			
			if(entity.getDesignation() == null  ){
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI, // Message par defaut
						entity.getMsgVarMap()) ;
				
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			
			//Vérifier si le produit logement auquel devrait rattacher le logement est defini
			
			if(entity.getProduitLogement() == null || entity.getProduitLogement().getCode() == null  ){
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_CODE_NON_DEFINI, // Message par defaut
						entity.getMsgVarMap()) ;
				
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			 
			
			// recherche de doublon
			
			boolean isEntityDuplictedOrNotFound = new EntityUtil<Logement>().isEntityDuplicatedOrNotFound(
					entityManager, entity, mustUpdateExistingNew, "logementIdParDesignationParProduitLogementCode", 
					new String[] {"designation","produitLogement"}, new String[] {entity.getDesignation(),entity.getProduitLogement().getCode()},
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
				
				EntityUtil<Logement> entityUtil = new EntityUtil<Logement>(Logement.class) ;
				
				
			    //ManyToOne ProduitLogement vers ProgrammeImmobilier
				//Verifie si le programmeimmobilier est non nul 
				
				Map<String,String> msgVarMap = entity.getProduitLogement() == null 
													|| entity.getProduitLogement().getMsgVarMap() == null
											   ?  new HashMap<String,String>() : entity.getProduitLogement().getMsgVarMap() ;
				
				
				boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
						entity, entity.getProduitLogement(), 
						entity.getClass().getDeclaredMethod("setProduitLogement", ProduitLogement.class), null, true, 
						locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
				
				
				if (!isAttached) return false ;
				
				
			} 
			catch(Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				logger.error("_960 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
				
				ex.printStackTrace();
				
				return false ;
			}
			
			//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
			
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
			Set<ConstraintViolation<Logement>> constraintViolationList = validator.validate(entity) ;
			
			for (ConstraintViolation<Logement> violation : constraintViolationList) {
				
				String translatedMessage = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
				
				
			}
					
			if (!constraintViolationList.isEmpty()) return false ;
		
			
			return true;
			
		}


		public Logement validerEtEnregistrer(
							Logement entity,
							boolean mustUpdateExistingNew,
							String namedGraph, boolean isFetchGraph, 
							Locale locale,  User loggedInUser, 
							List<NonLocalizedStatusMessage> msgList){
						
			
			//Verifier l'entité transmise
			
			if (entity == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return null ;
			}
			
			// Appel de la methode Valider
			
			boolean estValide = valider(entity, mustUpdateExistingNew, namedGraph, isFetchGraph,
									locale, msgList) ;
			
			
			if (!estValide) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return null ; 
			}
			
			
			// est une creation ?
			
			boolean estCreation = entity.getId() == null ;
		
			
			//Methode de persistence de l'entité correspondante
			
			Logement rtn = EntityUtil.persistOrMerge(
					entityManager, Logement.class, entity, 
					namedGraph, isFetchGraph, 
					AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
					AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
					AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
					locale, msgList);
			
			if (rtn == null) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return null ;
			}
			
			//Historisation de la creation ou de la modification d'un produit logement
			
			String loggedInUserId = loggedInUser.getId() != null 
				                    ? loggedInUser.getId() 
				                    : null ;
				                    
		    String  observation  = null ;

            String loggedInUserFullname = loggedInUser.getFullname() != null 
				                    ? loggedInUser.getFullname() 
				                    : null ;

			HistoryEventType historyEventType = estCreation 
						? HistoryEventType.CREATION : HistoryEventType.MODIFICATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
								loggedInUserId, loggedInUserFullname,observation, locale) ;
		
			//Retourne le resultat
			
			return rtn ;
			
		}

}
