package com.siliconwise.mmc.organisation.promoteur;

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
import com.siliconwise.common.Ville;
import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.programmeimmobilier.SupprimerUnProgrammeImmobilierCtlInterface;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class PromoteurDAO implements PromoteurDAOInterface, Serializable {

	
	private static final long serialVersionUID = 1L;

	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Inject SupprimerUnProgrammeImmobilierCtlInterface supprimerUnProgrammeImmobilierCtl ;
	
	private static transient Logger logger = LoggerFactory.getLogger(PromoteurDAO.class) ;
	
	// evenement d'historisation

	@Inject Event<HistoryEventPayload<Promoteur>> historyEvent ;
	
	
	@Override
	public boolean valider(
			        Promoteur entity, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph, 
			        boolean isFetchGraph,
			        Locale locale, 
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		//Verification de la raison sociale du promoteur
		
		if(entity.getRaisonSociale() == null || entity.getRaisonSociale().isEmpty() ) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_RAISON_SOCIALE_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_RAISON_SOCIALE_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
		
			return false ;
			
		}
		
		//Verification de l'identifiant legal du promoteur
		

		if(entity.getIdentifiantLegal() == null || entity.getIdentifiantLegal().isEmpty()) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_IDENTIFIANT_LEGAL_NON_DEFINI,
					 AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_IDENTIFIANT_LEGAL_NON_DEFINI, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
		
			return false ;
			
		}
		
		//Verification de l'email du promoteur
		
		if(entity.getEmail() == null || entity.getEmail().isEmpty()) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_EMAIL_NON_DEFINI,
					 AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_EMAIL_NON_DEFINI, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
		
			return false ;
			
		}
		
		
		//Verification du telephone  du promoteur
		
		if(entity.getTel() == null || entity.getTel().isEmpty()) {
					
					
			Map<String,String> msgVarMap =  new HashMap<String,String>();
					
			String msg  = MessageTranslationUtil.translate(locale,
				     AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_TELEPHONE_NON_DEFINI,
					 AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_TELEPHONE_NON_DEFINI, 
							 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
			return false ;
					
		}
		
		
		//Verification de l'email de l'administrateur de l'organisation
		
		if(entity.getEmailAdmin() == null || entity.getEmailAdmin().isEmpty()) {
							
							
			Map<String,String> msgVarMap =  new HashMap<String,String>();
							
			   String msg  = MessageTranslationUtil.translate(locale,
					  AppMessageKeys.CODE_TRADUCTION_EMAIL_ADMINSTRATEUR_ORGANISATION_NON_DEFINIE,
					  AppMessageKeys.CODE_TRADUCTION_EMAIL_ADMINSTRATEUR_ORGANISATION_NON_DEFINIE, 
							msgVarMap) ;
			   msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
							
		    return false ;
							
		}
		
		//Verifier le nom de l'administrateur

		if(entity.getNomAdmin() == null || entity.getNomAdmin().isEmpty()) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
							
			   String msg  = MessageTranslationUtil.translate(locale,
					  AppMessageKeys.CODE_TRADUCTION_NOM_ADMINSTRATEUR_ORGANISATION_NON_DEFINIE,
					  AppMessageKeys.CODE_TRADUCTION_NOM_ADMINSTRATEUR_ORGANISATION_NON_DEFINIE, 
							msgVarMap) ;
			   msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
							

		    return false ;
							
		}
		
		//Verifier le prenom de l'administrateur

		if(entity.getPrenomAdmin() == null || entity.getPrenomAdmin().isEmpty()) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
							
			   String msg  = MessageTranslationUtil.translate(locale,
					  AppMessageKeys.CODE_TRADUCTION_PRENOM_ADMINSTRATEUR_ORGANISATION_NON_DEFINIE,
					  AppMessageKeys.CODE_TRADUCTION_PRENOM_ADMINSTRATEUR_ORGANISATION_NON_DEFINIE, 
							msgVarMap) ;
			   msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
							
						
		    return false ;
							
		}
		
		
		// recherche de doublon
		
		boolean isEntityDuplictedOrNotFound = new EntityUtil<Promoteur>().isEntityDuplicatedOrNotFound(
					entityManager, entity, mustUpdateExistingNew, "promoteurIdParIdentifiantLegal", 
					new String[] {"identifiantLegal"}, new String[] {entity.getIdentifiantLegal()},
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
						
			
			EntityUtil<Promoteur> entityUtil = new EntityUtil<Promoteur>(Promoteur.class) ;
						
			Map<String,String> msgVarMap = entity.getVille() == null 
												|| entity.getVille().getMsgVarMap() == null
												?  new HashMap<String,String>() : entity.getVille().getMsgVarMap() ;
						
						
			boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
						entity, entity.getVille(), 
						entity.getClass().getDeclaredMethod("setVille", Ville.class), null, true, 
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
		Set<ConstraintViolation<Promoteur>> constraintViolationList = validator.validate(entity) ;
				
		for (ConstraintViolation<Promoteur> violation : constraintViolationList) {
					
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
			
			logger.info("_229 Affichage de MsgList="+msgList.toString());
					
		}
						
		
		if (!constraintViolationList.isEmpty()) return false ;
				
		logger.info("_236 fin  Verification des contraintes"); //TODO A effacer
				
		
	   return true;
		
	}

	@Override
	public Promoteur validerEtEnregistrer(
			          Promoteur entity, 
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
		
		boolean estValide = valider(entity, mustUpdateExistingNew, 
				             namedGraph, isFetchGraph,locale, msgList) ;
		
		if (!estValide) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return null ; 
		}
		
		
		// est une creation ?
		
		boolean estCreation = entity.getId() == null ;
		
	
		//Methode de persistence de l'entité correspondante
		
		Promoteur rtn = EntityUtil.persistOrMerge(
							entityManager, Promoteur.class, entity, 
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
			        String idPromoteur, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, User loggedInUser, 
			        List<NonLocalizedStatusMessage> msgList) {
	
		
			if(idPromoteur == null || idPromoteur.isEmpty() ) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return false ;
			}
			
			
			//Remettre l'entité  dans le contexte de persistence
			
			Promoteur entity = entityManager.find(Promoteur.class, idPromoteur) ;
		
			
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
			
			
			//Verifier si le promoteur est actif
			
			if(entity.getEstActive() != null && entity.getEstActive()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_ACTIVE,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_ACTIVE, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				
				return false ;
				
			}
			
		
			//TODO Activation de tous les comptes utilisateurs liés au promoteur
			
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

			HistoryEventType historyEventType =  HistoryEventType.ACTIVATION ;
			
			HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
							loggedInUserId, loggedInUserFullname,observation, locale) ;
			
			//Retourne le resultat
			
		    return true;
		    
	}

	
	@Override
	public boolean validerEtDesactiver(
			        String idPromoteur,
			        boolean mustUpdateExistingNew,
			        String namedGraph,
		         	boolean isFetchGraph, 
		         	Locale locale, 
		         	User loggedInUser, 
		         	List<NonLocalizedStatusMessage> msgList) {
	
		
		//Vérifier l'id du promoteur

		if(idPromoteur == null || idPromoteur.isEmpty() ) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
			
		}
		
		
		//Remettre l'entité  dans le contexte de persistence
		
		Promoteur entity = entityManager.find(Promoteur.class, idPromoteur) ;
		
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
		
		
		//Verifier si le promoteur est actif
		
		if(entity.getEstActive() == null || !entity.getEstActive()) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_NON_ACTIVE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_PROMOTEUR_NON_ACTIVE, // Message par defaut
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

		HistoryEventType historyEventType =  HistoryEventType.DESACTIVATION ;
		
		HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
						loggedInUserId, loggedInUserFullname,observation, locale) ;
		
		
		return true ;
		
		
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public boolean validerEtSupprimer(
			         String idPromoteur, 
			         boolean mustUpdateExistingNew,
			         String namedGraph,
			         boolean isFetchGraph, 
			         Locale locale, 
			         User loggedInUser, 
			         List<NonLocalizedStatusMessage> msgList) {
		
		
		//Verifier l'id du promoteur
		
		if(idPromoteur == null || idPromoteur.isEmpty() ) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ; 
			
		}
	
		//Remettre l'entite dans le contexte de persistence avec utilisation de namedGraph

		Promoteur entity  = EntityUtil
				                .findEntityById(entityManager, idPromoteur, namedGraph,  isFetchGraph, Promoteur.class) ;
		
		
		//Vérification de l'entiité mis en contexte de persistence
		
		if (entity == null ) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
		   return false ;
		   
		}
		
		
		//TODO Vérifier aucune transaction ne soit associée au promoteur

		
		//Suppression de tous les programmes immobiliers appartenant au promoteur
		
		//TODO A revoir j'ajout des exceptions
		
		List<ProgrammeImmobilier> rtnList 
									  =  entityManager
										   .createNamedQuery("rechercherProgrammeImmobilierParPromoteur")
										   .setParameter("idPromoteur", entity.getId())
										   .getResultList();
		

		
		//Suppression de la liste de programmes immobiliers
		
		boolean result = supprimerUnProgrammeImmobilierCtl
				         .supprimerProgrammeImmobilierList(rtnList, mustUpdateExistingNew, 
				                     namedGraph, isFetchGraph, locale, loggedInUser, msgList);

		logger.info("_528 Resulat de la suppression de la liste de programme immobiliers="+result); //TOOD A effacer
		
		//TODO Supprimer tous les comptes user associés au promoteur
		
		
	    entityManager.remove(entity);
		
		//Historisation de l'action
	    
	    String loggedInUserId = loggedInUser.getId() != null 
					                ? loggedInUser.getId() 
					                : null ;

        String loggedInUserFullname = loggedInUser.getFullname() != null 
					                ? loggedInUser.getFullname() 
					                : null ;
					                
		String observation = entity.getIdentifiantLegal() != null
				             ? entity.getIdentifiantLegal() 
				             : null ;

		HistoryEventType historyEventType =  HistoryEventType.SUPPRESSION ;
		
		HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, entity,
					      loggedInUserId, loggedInUserFullname,observation, locale) ;
				
		return true ; 
		
		
	}


	@Override
	public Promoteur rechercherUnPromoteurParId(
			          String id, 
			          String namedGraph, 
			          boolean isFetchGraph,
			          Class<Promoteur> entityClass) {
		
		return  EntityUtil
					.findEntityById(entityManager, id, namedGraph, isFetchGraph, entityClass);

	}
	
	
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Promoteur> tousLesPromoteurs() {
		
		
		
		List<Promoteur> rtnListe = new ArrayList<>() ;
		
		try {
			
			rtnListe = (List<Promoteur>) entityManager
				.createNamedQuery("tousLesPromoteurs")
				.getResultList();
			
		} catch (NoResultException e) {

			return null ;
		}
		
		return rtnListe ;
	}
	
	
	
}
