package com.siliconwise.mmc.produitlogement.caracteristique;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.event.oldhistorique.HistoriserEventPayload;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.logement.Logement;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * 
 * Modification faite par Alzouma Moussa Mahamadou DATE: 29/03/2022
 * En prenant en compte la relation entre caracteristiqueProduitLogement et Logement
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
public class CaracteristiqueProduitLogementDAO implements  CaracterisitqueProduitLogementDAOInterface{

	
	@Inject ValeurCaracteristiqueProduitLogementIntegerDAOInterface valeurCaracteristiqueProduitLogementIntegerDAO ;
	@Inject ValeurCaracteristiqueProduitLogementBooleanDAOInterface valeurCaracteristiqueProduitLogementBooleanDAO ;
	@Inject ValeurCaracteristiqueProduitLogementLongDAOInterface valeurCaracteristiqueProduitLogementLongDAO ;
	@Inject ValeurCaracteristiqueProduitLogementDoubleDAOInterface valeurCaracteristiqueProduitLogementDoubleDAO ;
	@Inject ValeurCaracteristiqueProduitLogementFloatDAOInterface valeurCaracteristiqueProduitLogementFloatDAO ;
	@Inject ValeurCaracteristiqueProduitLogementDateDAOInterface valeurCaracteristiqueProduitLogementDateDAO ;
	@Inject ValeurCaracteristiqueProduitLogementTimeDAOInterface valeurCaracteristiqueProduitLogementTimeDAO ;
	@Inject ValeurCaracteristiqueProduitLogementDateTimeDAOInterface valeurCaracteristiqueProduitLogementDateTimeDAO ;
	@Inject ValeurCaracteristiqueProduitLogementStringDAOInterface valeurCaracteristiqueProduitLogementStringDAO ;
	@Inject ValeurCaracteristiqueProduitLogementReferenceDAOInterface valeurCaracteristiqueProduitLogementReferenceDAO ;
	@Inject ValeurCaracteristiqueProduitLogementDocumentDAOInterface valeurCaracteristiqueProduitLogementDocumentDAO ;
	@Inject ValeurCaracteristiqueProduitLogementTexteDAOInterface valeurCaracteristiqueProduitLogementTexteDAO ;
	@Inject ValeurCaracteristiqueProduitLogementVilleDAOInterface valeurCaracteristiqueProduitLogementVilleDAO ;
	
	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
	Event<HistoriserEventPayload<ProgrammeImmobilier>> historiserEVent  ;
	
	//ProgrammeImmobilier programmeImmobilier  = new ProgrammeImmobilier() ;
			
	private static transient Logger logger = LoggerFactory.getLogger(CaracteristiqueProduitLogementDAO.class) ;
	
	@Override
	public boolean valider(
					CaracteristiqueProduitLogement entity, 
					boolean mustUpdateExistingNew, String namedGraph,
					boolean isFetchGraph, Locale locale, 
					List<NonLocalizedStatusMessage> msgList) {
		
	
		//Verifier si la designation est non nulle
		
		logger.info("_79 CaracteristiqueProduitLogement :: entity="+entity.getDesignation()+"  mustUpdateExistingNew="+mustUpdateExistingNew
				+"");
		
		if(entity.getDesignation() == null ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 

			
			return false ;
		}
		
		logger.info("_96 Verification des entités logement et produitlogement"); //TODO A effacer
		
		//Verification des entités logement et produitlogement
		
		if(entity.getLogement() == null && entity.getProduitLogement() == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 

			
			return false ;
		}
		
		
		logger.info("_114 Verification des entités logement et produitlogement"); //TODO A effacer
		
		//Verification des entités logement et produitlogement
		
		if(entity.getLogement() != null && entity.getProduitLogement() != null) {
			
			//TODO A mettre à jour les messages d'erreurs
					
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE, // Message par defaut
				 entity.getMsgVarMap()) ;
					
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 

					
			return false ;
			
		}
		
		
		logger.info("_114 Verification de la propriété estCaracteristiqueProduitLogement"); //TODO A effacer
		
		//Verification de la propriété estCaracteristiqueProduitLogement
		
		if(entity.getLogement() != null 
				  && entity.getProprieteProduitLogement() != null 
				  && entity.getProprieteProduitLogement().isEstCaracteristiqueProduitLogement()) {
					
			//TODO A mettre à jour les messages d'erreurs
							
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE, // Message par defaut
				entity.getMsgVarMap()) ;
							
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 

							
			return false ;
					
		 }
		
		
		logger.info("_158 Verification de la propriété estCaracteristiqueProduitLogement"); //TODO A effacer
		
		//Verification de la propriété estCaracteristiqueProduitLogement
		
		if(entity.getProduitLogement() != null 
				  && entity.getProprieteProduitLogement() != null 
				  && !entity.getProprieteProduitLogement().isEstCaracteristiqueProduitLogement()) {
					
			//TODO A mettre à jour les messages d'erreurs
							
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE, // Message par defaut
				entity.getMsgVarMap()) ;
							
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 

							
			return false ;
					
		}
		
		
		// recherche de doublon
		
		boolean isEntityDuplictedOrNotFound = entity.getProduitLogement() != null 
				? new EntityUtil<CaracteristiqueProduitLogement>().isEntityDuplicatedOrNotFound(
					entityManager, entity, mustUpdateExistingNew, "caracteristiqueProduitLogementIdParDesignationParProduitLogement", 
					new String[] {"designation","produitLogementCode"}, new String[] {entity.getDesignation(),entity.getProduitLogement().getCode()},
					AppMessageKeys.CODE_TRADUCTION_NOUVELLE_ENTITE_DUPLIQUEE, entity.getMsgVarMap(),
					AppMessageKeys.CODE_TRADUCTION_DUPLICATION_AVEC_ID_DIFFERENT, entity.getMsgVarMap(),
					AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, entity.getMsgVarMap(), 
				locale, msgList)
				: new EntityUtil<CaracteristiqueProduitLogement>().isEntityDuplicatedOrNotFound(
					entityManager, entity, mustUpdateExistingNew, "caracteristiqueLogementIdParDesignationParLogement", 
					new String[] {"designation","logementDesignation"}, new String[] {entity.getDesignation(),entity.getLogement().getDesignation()},
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
			
			
			EntityUtil<CaracteristiqueProduitLogement> entityUtil = new EntityUtil<CaracteristiqueProduitLogement>(CaracteristiqueProduitLogement.class) ;
			
			
		    //ManyToOne CaracteristiqueProduitLogement vers ProduitLogement
			//Verifie si le produitlogmenet est non nul 
			
			 Map<String,String> msgVarMap = entity.getProduitLogement()== null 
										|| entity.getProduitLogement().getMsgVarMap() == null
										 ?  new HashMap<String,String>() 
										 : entity.getProduitLogement().getMsgVarMap() ;
			
			boolean  isAttached = entity.getProduitLogement() == null ? true : 
						 entityUtil.attachLinkedEntity(entityManager, 
								 entity, entity.getProduitLogement(), 
								 entity.getClass().getMethod("setProduitLogement", ProduitLogement.class), null, true, 
					     locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
			
			
			if (!isAttached) return false ;
			
			
			//ManyToOne CaracteristiqueProduitLogement vers ProprieteProduitLogement
			 //Verifie si la ProprieteProduitLogement est non nulle
			
			msgVarMap = entity.getProprieteProduitLogement()== null 
							 || entity.getProprieteProduitLogement().getMsgVarMap() == null
							 ?  new HashMap<String,String>() 
							 : entity.getProprieteProduitLogement().getMsgVarMap() ;
			
	        isAttached = entityUtil.attachLinkedEntity(entityManager, 
							entity, entity.getProprieteProduitLogement(), 
							entity.getClass().getMethod("setProprieteProduitLogement", ProprieteProduitLogement.class), null, true, 
			locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
			
	       
			if (!isAttached) return false ;
			
			
			
		    //ManyToOne CaracteristiqueProduitLogement vers Logement
			//Verifie si le produitlogmenet est non nul 
			
			msgVarMap = entity.getLogement()== null 
							  || entity.getLogement().getMsgVarMap() == null
							  ?  new HashMap<String,String>() 
						      : entity.getLogement().getMsgVarMap() ;
			
		    isAttached = entity.getLogement() == null ? true : 
						 entityUtil.attachLinkedEntity(entityManager, 
								 entity, entity.getLogement(), 
								 entity.getClass().getMethod("setLogement", Logement.class), null, true, 
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
		
		// verification d'integrite complexes(règles metiers) 
		
		return true;
		
	}

	@Override
	public CaracteristiqueProduitLogement validerEtEnregistrer(
					CaracteristiqueProduitLogement entity,
					boolean mustUpdateExistingNew, String namedGraph, 
					boolean isFetchGraph, Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
	
		
		if (entity == null) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		}
		

		boolean estValide = valider(entity, mustUpdateExistingNew, namedGraph, isFetchGraph,
								locale, msgList) ;
		
		
		if (!estValide) {
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			return null ; 
		}
		
		
		//Appel à la methode enregistrer pour l'aiguillage
		
		CaracteristiqueProduitLogement rtn = enregistrer(entity, mustUpdateExistingNew,  
								namedGraph,  isFetchGraph,  locale,  loggedInUser, msgList) ; 
		
		
		return rtn ;
		
	}

	
	private  CaracteristiqueProduitLogement  enregistrer(CaracteristiqueProduitLogement entity , 
								boolean mustUpdateExistingNew, String namedGraph, 
								boolean isFetchGraph, Locale locale, User loggedInUser,
								List<NonLocalizedStatusMessage> msgList) {
		
			
		CaracteristiqueProduitLogement rtn = null ;
		
		
			if(entity instanceof ValeurCaracteristiqueProduitLogementInteger) {
				
				
				     rtn = valeurCaracteristiqueProduitLogementIntegerDAO
						          .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementInteger) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementBoolean) {
				
				
				     rtn = valeurCaracteristiqueProduitLogementBooleanDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementBoolean) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			     
			}  
			
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementLong) {
				
			        rtn = valeurCaracteristiqueProduitLogementLongDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementLong) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementDouble) {
				
				     rtn = valeurCaracteristiqueProduitLogementDoubleDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementDouble) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementString) {
				
				    rtn = valeurCaracteristiqueProduitLogementStringDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementString) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementDocument) {
				
			        rtn = valeurCaracteristiqueProduitLogementDocumentDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementDocument) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementTexte) {
				
				   rtn = valeurCaracteristiqueProduitLogementTexteDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementTexte) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementDate) {
				
				  rtn = valeurCaracteristiqueProduitLogementDateDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementDate) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementDateTime) {
				
				  rtn = valeurCaracteristiqueProduitLogementDateTimeDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementDateTime) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementTime) {
				
				  rtn = valeurCaracteristiqueProduitLogementTimeDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementTime) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
				
				  
			}  
			
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementReference) {
				
				  rtn = valeurCaracteristiqueProduitLogementReferenceDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementReference) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
				
				  
			}  
			
			
			if(entity instanceof ValeurCaracteristiqueProduitLogementVille) {
				
				  rtn = valeurCaracteristiqueProduitLogementVilleDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueProduitLogementVille) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
				
			}  
			
			
			return  rtn;
				 
	}
	
}
