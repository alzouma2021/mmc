package com.siliconwise.mmc.simulationfinancementimmobilier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.demandereservationlogement.financement.EcheanceFinancementTemperament;
import com.siliconwise.mmc.demandereservationlogement.financement.EcheanceFinancementTemperamentDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.Financement;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierComptantSurSituation;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierLink;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierLinkDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementTemperament;
import com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement.ValeurCaracteristiqueDemandeReservationLogement;
import com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement.ValeurCaracteristiqueDemandeReservationLogementDAOInterface;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class CreerModifierUneSimulationFinancementImmobilierCtl implements CreerModifierUneSimulationFinancementimmobilierCtlInterface{

	
	@Resource
	private EJBContext ejbContext;
	
	@Inject ValeurCaracteristiqueDemandeReservationLogementDAOInterface  valeurCaracteristiqueDemandeReservationLogementDAO ;
	
	@Inject SimulationFinancementImmobilierDAOInterface  simulationFinancementImmobilierDAO ;
	
	@Inject FinancementDAOInterface  financementDAO ;
	
	@Inject EcheanceFinancementTemperamentDAOInterface  echeanceFinancementTemperamentDAO ;
	
	@Inject FinancementPallierLinkDAOInterface  financementPallierLinkDAO ;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SimulationFinancementImmobilier creerModifierUneSimulationFinancementImmobilier(
						SimulationFinancementImmobilierTransfert entityTransfert,
						boolean mustUpdateExistingNew, String namedGraph, 
						boolean isFetchGraph, Locale locale, User loggedInUser,
						List<NonLocalizedStatusMessage> msgList , Boolean estCreation) {
		
		
		//Verification de la variable entity
		
		if (entityTransfert == null) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		}
		
		
		//Instance de classe SimulationFinancementImmmobilier
		
		SimulationFinancementImmobilier entity = new SimulationFinancementImmobilier();
		
		//	Set<ModeFinancement>   entityFinancementList = new  HashSet<>();
	    
		logger.info("_91 Vérification de l'entity simulationFinancementImmobilier"); //TODO A effacer
				
		if(entityTransfert.getSimulationFinancementImmobilier() == null ) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_SIMULATION_FINANCEMENT_IMMOBILIER_NON_RENSEIGNE,
					 AppMessageKeys.CODE_TRADUCTION_SIMULATION_FINANCEMENT_IMMOBILIER_NON_RENSEIGNE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
			
		}
		
		//Initialisation de l'entité SimulationFinancementImmobilier
		
		entity = entityTransfert.getSimulationFinancementImmobilier() ;
		
		//Verification si il s'agit d'une creation ou d'une modification
		   
	    //  S'il s'agit d'une creation et l'Id renseigné alors renvoie un message d'erreurs
	   
	     if(estCreation  && entity.getId() != null) {
	    	 
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_SIMULATION_FINANCEMENT_IMMOBILIER_ID_NON_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_SIMULATION_FINANCEMENT_IMMOBILIER_ID_NON_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		    
		  
		    return null ;
		    
	     }
	   
	   
	    //  S'il s'agit d'une modification et que l'Id et La version non renseignée alors renvoyer un message d'erreurs
	    
	    if(!estCreation  && entity.getId() == null) {
	    	
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_SIMULATION_FINANCEMENT_IMMOBILIER_ID_NON_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_SIMULATION_FINANCEMENT_IMMOBILIER_ID_NON_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		  
		     
		     return null ;
		     
	    }
	    
	    
	    //Persistence de la simulation de financement immobilier 
	    
	    logger.info("_147 Appel de la methode validerEtEnregistrer :: simulation="+entity.toString()); //TODO A effacer
		
		SimulationFinancementImmobilier rtn 
					= simulationFinancementImmobilierDAO.validerEtEnregistrer(
					    entity,mustUpdateExistingNew,namedGraph,isFetchGraph,locale,loggedInUser,msgList);
		
		
		if(rtn == null ) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return rtn ;
		}
		
		
		// Creation de la liste de valeurs de caracteristiques demande de reservation logement de la simulation
		
		if(entityTransfert.getValeurCaracteristiqueDemandeReservationLogementList() != null 
				&& entityTransfert.getValeurCaracteristiqueDemandeReservationLogementList().isEmpty()) {
			
			
			List<ValeurCaracteristiqueDemandeReservationLogement> 
				valeurCaracteristiqueList = new ArrayList<ValeurCaracteristiqueDemandeReservationLogement>();
			
			
			for(ValeurCaracteristiqueDemandeReservationLogement valeurCaracteristique: valeurCaracteristiqueList) {
				
				
				if(valeurCaracteristique != null) {
					
				   //Appel à la methode de persistence 
					
				   ValeurCaracteristiqueDemandeReservationLogement 
				   rtnValeurCaracteristique = valeurCaracteristiqueDemandeReservationLogementDAO
				   					    .validerEtEnregistrer(valeurCaracteristique, mustUpdateExistingNew, 
				   								  namedGraph, isFetchGraph, locale, loggedInUser, msgList);
				   
				   
				   if(rtnValeurCaracteristique == null) {
					   
					   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
					   
					   return null ;
					   
				   }
					
				}
				
			}
			
		}
		
		
		
		//Creation des financement de la simulation
		
		
		if(entityTransfert.getFinancementTransfert() != null 
				&& entityTransfert.getFinancementTransfert().getFinancementList() != null
				&& !entityTransfert.getFinancementTransfert().getFinancementList().isEmpty()) {
			
			
			List<Financement> 
					financementList = new ArrayList<Financement>();
			
			//Initialisation de la liste de financements
			
			financementList = entityTransfert.getFinancementTransfert().getFinancementList();
			
			
			for(Financement financement: financementList) {
				
				
				if(financement != null) {
					
				   //Appel à la methode de persistence 
					
				   Financement rtnFinancement 
				   			= financementDAO.validerEtEnregistrer(financement, mustUpdateExistingNew, 
				   					   namedGraph, isFetchGraph, locale, loggedInUser, msgList);
				   
				   
				   if(rtnFinancement == null) {
					   
					   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
					   
					   return null ;
					   
				   }
				   
				   
				   //Creation de la liste d'echeance de financement temperament	
				   
				   if(rtnFinancement instanceof FinancementTemperament) {
					   
					   
					   //Creation des écheances de financement  temperament
					   
					   if(entityTransfert.getFinancementTransfert().getEcheanceFinancementTemperamentList() != null
							   && !entityTransfert.getFinancementTransfert().getEcheanceFinancementTemperamentList().isEmpty() ) {
						   
						   
						   for(EcheanceFinancementTemperament echeance : entityTransfert
								   		.getFinancementTransfert().getEcheanceFinancementTemperamentList() ) {
							   
							   
							   if(echeance != null) {
								   
								   //Initiliasation 
								   
								   echeance.setFinancementTemparement((FinancementTemperament) rtnFinancement);
								   
								   EcheanceFinancementTemperament  rtnEcheance = echeanceFinancementTemperamentDAO
									.validerEtEnregistrer(echeance,mustUpdateExistingNew,namedGraph, isFetchGraph, locale, loggedInUser, msgList);
								   
								   if(rtnEcheance == null) {
									   
									   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
									   
									   return null ;
									   
								   }
								   
							    }
							   
						    }
						   
					    }
					   
				    }
				   
				   
				   //Creation de la classe d'association FinancementPallierLink
				   
				   //vérifier si le financement crée est un financementPallierComptantSurSituation
				   //Si le financement est de type financementPallierComptantSurSituation alors
				   //Comparer les clés de la collectionMap (MontantProposé pour chaque pallier) aux identifiants des différents palliers de la collection liste PallierComptantSurSitutaion
				   //dans la comparaison si une clé correspond a un identifiant pallier alors
				   //On enregistre la classe FinancementPallierLink
				   
				   if(rtnFinancement  instanceof FinancementPallierComptantSurSituation) {
					   
					   
					   Map<String,Double> montantProsposeList = ((FinancementPallierComptantSurSituation) rtnFinancement).getMontantProposeList() ;
					   
					   List<PallierComptantSurSituation> pallierComptantSurSituationList = (List<PallierComptantSurSituation>) ((FinancementPallierComptantSurSituation) rtnFinancement).getPallierComptantSurSituationList();
					   
					   //Vérification de la variable montantProsposeList
					   
					   if(montantProsposeList == null || montantProsposeList.isEmpty()) {
						   
						   //TODO Ajouter les messages d'erreurs 
						   
						   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						   
						   return null ;
					   }
					   
					   //Vérification de la variable pallierComptantSurSituationList
					   
					   if(pallierComptantSurSituationList == null 
							         || pallierComptantSurSituationList.isEmpty()) {
						   
						   //TODO Ajouter les messages d'erreurs 
						   
						   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						   
						   return null ;
						   
					   }
					   
					   //Vérification de la taille pallierComptantSurSituationList  et montantProsposeList
					  
					   if(pallierComptantSurSituationList.size() != montantProsposeList.size() ) {
						   
						   //TODO Ajouter les messages d'erreurs 
						   
						   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						   
						   return null ;
						   
					   }
					   
					
					   List<String> keySet = new ArrayList();
					   
					   //Recuperation de la liste de clé et de valeurs de la variablemontantProsposeList
					   
					   if(montantProsposeList.keySet() != null )  keySet = (List<String>) montantProsposeList.keySet() ;
					   
					   //Comparaison de clé et idenitifiant
					   
					   for(int i = 0 ; keySet.size() > 0 ; i++  ) {
						   
						   
						   if(keySet.get(i) != null && pallierComptantSurSituationList.get(i) != null
									   && pallierComptantSurSituationList.get(i) != null
									   && pallierComptantSurSituationList.get(i).getPallier() != null
									   && pallierComptantSurSituationList.get(i).getPallier().getId() != null
									   && pallierComptantSurSituationList.get(i).getPallier().getId().equals(keySet.get(i))) {
								
							   
							   //Initialisation de l'entity de type financementPallierLink
							   
							   FinancementPallierLink financementPallierLink = new FinancementPallierLink() ;
							   
							   financementPallierLink.setFinancementPallierComptantSurSituation((FinancementPallierComptantSurSituation) rtnFinancement);
							   financementPallierLink.setPallierComptantSurSituation(pallierComptantSurSituationList.get(i));
							   financementPallierLink.setMontantProspose(montantProsposeList.get(i).doubleValue());
							   
							   //Creation de l'entité financementPallierLink
							   
							   FinancementPallierLink rtnPallier = financementPallierLinkDAO.validerEtEnregistrer(financementPallierLink,mustUpdateExistingNew, 
									                       			namedGraph, isFetchGraph, locale, loggedInUser, msgList);
							   
							   
							   if(rtnPallier == null) {
							   
								   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
								   
								   return null ;
							   
							   }
							   
						   }
						   
					    }
					   
				    }
				   
				}
				
			}
			
		}
		
		return rtn ;
		
	}
	
}
