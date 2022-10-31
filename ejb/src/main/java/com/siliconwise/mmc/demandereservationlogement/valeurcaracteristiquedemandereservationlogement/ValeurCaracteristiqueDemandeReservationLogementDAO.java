package com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement;

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
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
public class ValeurCaracteristiqueDemandeReservationLogementDAO implements  ValeurCaracteristiqueDemandeReservationLogementDAOInterface{

	
	@Inject ValeurCaracteristiqueDemandeReservationLogementIntegerDAOInterface valeurCaracteristiqueDemandeReservationLogementIntegerDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementBooleanDAOInterface valeurCaracteristiqueDemandeReservationLogementBooleanDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementLongDAOInterface valeurCaracteristiqueDemandeReservationLogementLongDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementDoubleDAOInterface valeurCaracteristiqueDemandeReservationLogementDoubleDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementFloatDAOInterface valeurCaracteristiqueDemandeReservationLogementFloatDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementDateDAOInterface valeurCaracteristiqueDemandeReservationLogementDateDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementTimeDAOInterface valeurCaracteristiqueDemandeReservationLogementTimeDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementDateTimeDAOInterface valeurCaracteristiqueDemandeReservationLogementDateTimeDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementStringDAOInterface valeurCaracteristiqueDemandeReservationLogementStringDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementReferenceDAOInterface valeurCaracteristiqueDemandeReservationLogementReferenceDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementDocumentDAOInterface valeurCaracteristiqueDemandeReservationLogementDocumentDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementTexteDAOInterface valeurCaracteristiqueDemandeReservationLogementTexteDAO ;
	@Inject ValeurCaracteristiqueDemandeReservationLogementVilleDAOInterface valeurCaracteristiqueDemandeReservationLogementVilleDAO ;
	
	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
	Event<HistoriserEventPayload<ProgrammeImmobilier>> historiserEVent  ;
	
	//ProgrammeImmobilier programmeImmobilier  = new ProgrammeImmobilier() ;
			
	private static transient Logger logger = LoggerFactory.getLogger(ValeurCaracteristiqueDemandeReservationLogementDAO.class) ;
	
	@Override
	public boolean valider(
					ValeurCaracteristiqueDemandeReservationLogement entity, 
					boolean mustUpdateExistingNew, String namedGraph,
					boolean isFetchGraph, Locale locale, 
					List<NonLocalizedStatusMessage> msgList) {
		
	
		//Verifier si la designation est non nulle
		
		logger.info("_79 CaracteristiqueProduitLogement :: entity="+entity.getDesignation()+"  mustUpdateExistingNew="+mustUpdateExistingNew
				+"");
		
		if(entity.getDesignation() == null || entity.getCaracteristiqueDemandeReservationLogement() == null ) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DESIGNATION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 

			
			return false ;
		}
		
		
		// recherche de doublon
		
		boolean isEntityDuplictedOrNotFound = new EntityUtil<ValeurCaracteristiqueDemandeReservationLogement>().isEntityDuplicatedOrNotFound(
				entityManager, entity, mustUpdateExistingNew, "valeurCaracteristiqueDemandeReservationLogementIdParDesignation", 
				new String[] {"designation"}, new String[] {entity.getDesignation()},
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
			
			
			EntityUtil<ValeurCaracteristiqueDemandeReservationLogement> entityUtil = new EntityUtil<ValeurCaracteristiqueDemandeReservationLogement>(ValeurCaracteristiqueDemandeReservationLogement.class) ;
			
			
		    //OneToOne CaracteristiqueProduitLogement vers ProduitLogement
			 //Verifie si le produitlogmenet est non nul 
			
			 Map<String,String> msgVarMap = entity.getCaracteristiqueDemandeReservationLogement()== null 
												|| entity.getCaracteristiqueDemandeReservationLogement().getMsgVarMap() == null
										   ?  new HashMap<String,String>() : entity.getCaracteristiqueDemandeReservationLogement().getMsgVarMap() ;
			
			boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
				    entity, entity.getCaracteristiqueDemandeReservationLogement(), 
				    entity.getClass().getMethod("setProduitLogement", ProduitLogement.class), null, true, 
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
	public ValeurCaracteristiqueDemandeReservationLogement validerEtEnregistrer(
					ValeurCaracteristiqueDemandeReservationLogement entity,
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
		
		ValeurCaracteristiqueDemandeReservationLogement rtn = enregistrer(entity, mustUpdateExistingNew,  
								namedGraph,  isFetchGraph,  locale,  loggedInUser, msgList) ; 
		
		
		return rtn ;
		
	}

	
	private  ValeurCaracteristiqueDemandeReservationLogement  enregistrer(ValeurCaracteristiqueDemandeReservationLogement entity , 
								boolean mustUpdateExistingNew, String namedGraph, 
								boolean isFetchGraph, Locale locale, User loggedInUser,
								List<NonLocalizedStatusMessage> msgList) {
		
			
		ValeurCaracteristiqueDemandeReservationLogement rtn = null ;
		
		
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementInteger) {
				
				
				     rtn = valeurCaracteristiqueDemandeReservationLogementIntegerDAO
						          .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementInteger) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementBoolean) {
				
				
				     rtn = valeurCaracteristiqueDemandeReservationLogementBooleanDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementBoolean) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			     
			}  
			
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementLong) {
				
			        rtn = valeurCaracteristiqueDemandeReservationLogementLongDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementLong) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementDouble) {
				
				     rtn = valeurCaracteristiqueDemandeReservationLogementDoubleDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementDouble) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementString) {
				
				    rtn = valeurCaracteristiqueDemandeReservationLogementStringDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementString) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementDocument) {
				
			        rtn = valeurCaracteristiqueDemandeReservationLogementDocumentDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementDocument) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementTexte) {
				
				   rtn = valeurCaracteristiqueDemandeReservationLogementTexteDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementTexte) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementDate) {
				
				  rtn = valeurCaracteristiqueDemandeReservationLogementDateDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementDate) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementDateTime) {
				
				  rtn = valeurCaracteristiqueDemandeReservationLogementDateTimeDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementDateTime) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			
			}  
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementTime) {
				
				  rtn = valeurCaracteristiqueDemandeReservationLogementTimeDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementTime) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
				
				  
			}  
			
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementReference) {
				
				  rtn = valeurCaracteristiqueDemandeReservationLogementReferenceDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementReference) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
				
				  
			}  
			
			
			if(entity instanceof ValeurCaracteristiqueDemandeReservationLogementVille) {
				
				  rtn = valeurCaracteristiqueDemandeReservationLogementVilleDAO
						   .validerEtEnregistrer((ValeurCaracteristiqueDemandeReservationLogementVille) entity, 
								   				mustUpdateExistingNew, namedGraph, 
								   				isFetchGraph, locale, loggedInUser, msgList);
			}  
			
			
			return  rtn;
				 
	}
	
}
