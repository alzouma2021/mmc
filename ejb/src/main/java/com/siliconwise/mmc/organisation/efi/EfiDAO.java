package com.siliconwise.mmc.organisation.efi;

import java.io.Serializable;
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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.Pays;
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
 * @author ALzouma Moussa Mahamadou
 * 
 * Date : 22/07/2021
 *
 */
@Stateless
public class EfiDAO implements Serializable , EfiDAOInterface {

	
	private static final long serialVersionUID = 1L;
	

	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject Event<HistoryEventPayload<EFi>> historyEvent ;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	@SuppressWarnings("unchecked")
	@Override
	public List<EFi> tousLesEfis() {
	
		
		List<EFi> rtnList = new ArrayList<>() ;
		
		try {
			
			rtnList = entityManager
						.createNamedQuery("toutesLesEfis")
						.getResultList();
			
		} catch (NoResultException e) {

			return null ;
		}
		
		return rtnList ;
		
	}
	
	
	
	@Override
	public boolean valider(
			        EFi entity, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph, 
			        boolean isFetchGraph,
			        Locale locale, 
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		//Verification de l'email de l'EFi
		
		if(entity.getEmail() == null || entity.getEmail().isEmpty()) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_EFI_EMAIL_NON_DEFINI,
					 AppMessageKeys.CODE_TRADUCTION_EFI_EMAIL_NON_DEFINI, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			
		}
		
		
		// recherche de doublon
		
		boolean isEntityDuplictedOrNotFound = new EntityUtil<EFi>().isEntityDuplicatedOrNotFound(
					entityManager, entity, mustUpdateExistingNew, "efiIdParEmaill", 
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
			
			EntityUtil<EFi> entityUtil = new EntityUtil<EFi>(EFi.class) ;
						
						
			Map<String,String> msgVarMap = entity.getPays() == null 
												|| entity.getPays().getMsgVarMap() == null
												?  new HashMap<String,String>() : entity.getPays().getMsgVarMap() ;
						
			boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
						entity, entity.getPays(), 
						entity.getClass().getDeclaredMethod("setPays", Pays.class), null, true, 
						locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
						
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
				
		logger.info("_1141 debut  Verification des contraintes"); //TODO A effacer
				
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		Set<ConstraintViolation<EFi>> constraintViolationList = validator.validate(entity) ;
				
		for (ConstraintViolation<EFi> violation : constraintViolationList) {
					
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
					
					
		}
						
		
		if (!constraintViolationList.isEmpty()) return false ;
				
		logger.info("_236 fin  Verification des contraintes"); //TODO A effacer
				
		
	   return true;
		
	}

	@Override
	public EFi validerEtEnregistrer(
				  EFi entity, 
			      boolean mustUpdateExistingNew, 
			      String namedGraph,
			      boolean isFetchGraph, 
			      Locale locale, 
			      User loggedInUser, 
			      List<NonLocalizedStatusMessage> msgList) {
		
		
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
		
		EFi rtn = EntityUtil.persistOrMerge(
					entityManager, EFi.class, entity, 
					namedGraph, isFetchGraph, 
					AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
					AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
					AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
					locale, msgList);
		
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		}
		
		
		//Historisation de l'action
		
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



	@Override
	public boolean validerEtActiver(
			        String idEfi, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph,
			        boolean isFetchGraph,
			        Locale locale, 
			        User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		Map<String,String> msgVarMap =  new HashMap<String,String>();
		
		//Verifier l'identifiant du efi
		
		if(idEfi == null || idEfi.isEmpty() ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			
		}
		
		
		//Remettre l'entité produit logement dans le contexte de persistence
		
		EFi entity = entityManager.find(EFi.class, idEfi) ;
		
		
		//Verification de l'entité
		
		if (entity == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			
		}
		
		
		//Verifier si l'Efi est actif
		
		if(entity.getEstActive() != null && entity.getEstActive()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_EFI_ACTIVE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_EFI_ACTIVE, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
			
		}
		
		
		//TODO Activation de tous les comptes utilisateurs liés à l'EFi
		
		//Modification de la propriété estActive dans le contexte de persistence 
		
		entity.setEstActive(true) ;
		
		//Historisation de l'action
		
		String loggedInUserId = loggedInUser.getId() != null 
				                ? loggedInUser.getId() 
				                : null ;

		String loggedInUserFullname = loggedInUser.getFullname() != null 
				                ? loggedInUser.getFullname() 
				                : null ;
				                
	    String observation = null ;
		
		HistoryEventType historyEventType = HistoryEventType.ACTIVATION ;
		
		HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
				            loggedInUserId, loggedInUserFullname,observation, locale) ;
		
	    return true ;
	    
	}



	@Override
	public boolean validerEtDesactiver(
			        String idEfi, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, 
			        User loggedInUser, 
			        List<NonLocalizedStatusMessage> msgList) {
		
		Map<String,String> msgVarMap =  new HashMap<String,String>();
		
		if(idEfi == null || idEfi.isEmpty()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			
		}
		
		
		//Remettre l'entité dans le contexte de persistence
		
		EFi entity = entityManager.find(EFi.class, idEfi) ;
		
		//Verification de l'entité
		
		if (entity == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			
		}
		
		
		//Verifier si le promoteur est actif
		
		if(entity.getEstActive() == null || !entity.getEstActive()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_EFI_NON_ACTIVE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_EFI_NON_ACTIVE, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			
			return false ;
			
		}
		
		
		//TODO Desactivation de tous les comptes utilisateurs liés au promoteur
		
		
		//Modification de la propriété estActive dans le contexte de persistence 
		
		entity.setEstActive(false) ;
		
		//Historisation de l'action
		
		String loggedInUserId = loggedInUser.getId() != null 
				                ? loggedInUser.getId() 
				                : null ;

	    String loggedInUserFullname = loggedInUser.getFullname() != null 
				                ? loggedInUser.getFullname() 
				                : null ;
				                
	    String observation = null ;

		HistoryEventType historyEventType = HistoryEventType.DESACTIVATION ;
		
		HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
		                     loggedInUserId, loggedInUserFullname,observation,locale) ;
		
		return true ;
	
	}


	@Override
	public boolean validerEtSupprimer(
			         String idEfi, 
			         boolean mustUpdateExistingNew,
			         String namedGraph,
			         boolean isFetchGraph,
			         Locale locale, 
			         User loggedInUser, 
			         List<NonLocalizedStatusMessage> msgList) {
		
		Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
		
		if(idEfi == null || idEfi.isEmpty() ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return false ; 
			
		}
	
		//Remettre l'entite dans le contexte de persistence avec utilisation de namedGraph

		EFi entity  = EntityUtil
				      .findEntityById(entityManager, idEfi, namedGraph,  isFetchGraph, EFi.class) ;
		
		
		//Vérification de l'entiité mis en contexte de persistence
		
		if (entity == null ) {
		
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
		   return false ;
		   
		}
		
		
		//Suppression du credi bancaire de l'EFi
		
		int suppressionCreditBancaireParEfi 
						      =  entityManager
								   .createNamedQuery("supprimerCreditBancaireParEFi")
								   .setParameter("idEfi", entity.getId())
								   .executeUpdate();

				
		if(suppressionCreditBancaireParEfi == 0) {
				
			 try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
		     return false ;
				
		}
		
		
		//TODO Vérifier aucune transaction ne soit associée à l'EFi
		
		//TODO Supprimer tous les comptes user associés à l'Efi
		
		entityManager.remove(entity);
		
		//Historisation de l'action
		
		
		String loggedInUserId = loggedInUser.getId() != null 
				                ? loggedInUser.getId() 
				                : null ;

        String loggedInUserFullname = loggedInUser.getFullname() != null 
				                ? loggedInUser.getFullname() 
				                : null ;
				                
	    String   observation  = entity.getDesignation() != null
							    ? entity.getDesignation()
							    : null;     

		HistoryEventType historyEventType = HistoryEventType.SUPPRESSION ;
		
		HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
		                  loggedInUserId, loggedInUserFullname,observation,  locale) ;
			
		
		return true ; 
		
		
	  }


	@Override
	public EFi rechercherUnEFiParId(
			           String id, 
			           String namedGraph,
			           boolean isFetchGraph,
			           Class<EFi> entityClass) {
		
		
		return  EntityUtil
				.findEntityById(entityManager, id, namedGraph, isFetchGraph, entityClass);

	}
	

}
