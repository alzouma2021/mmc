package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.document.Document;
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
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
public class CreerModifierModeFinancementCtl implements Serializable , CreerModifierModeFinancementCtlInterface {

	
	private static transient Logger logger = LoggerFactory.getLogger(CreerModifierModeFinancementCtl.class) ;
	
	@Inject ModeFinancementDAOInterface modeFinancementDAO ;
	
	private static final long serialVersionUID = 1L;

	
	
	@Override
	public List<ModeFinancement> creerModifierModeFinancementList(
									Set<ModeFinancement> entityList,
									boolean mustUpdateExistingNew,ProgrammeImmobilier programme, 
									String namedGraph, boolean isFetchGraph, 
									Locale locale, User loggedInUser,
									List<NonLocalizedStatusMessage> msgList) {
				
		
				//Variable ModeFinancement Liste
		
				List<ModeFinancement> rtnList = new ArrayList<ModeFinancement>();
				
				 /**
				  * 
				  * Ajout par Alzouma Moussa Mahamadou
				  * 
				  * Date : 25-07-2021
				  * 
				  * Vu que le front-end envoie toute la structure Json de la classe ModeFinancement
				  * Notre retournerModeFinancementEnFonctionDuType va expurger les données et
				  * récupérer les essentielles en fonction du type de financement
				  * 
				  */
				
				for(ModeFinancement entity: entityList) {
				 
				 if ( entity != null ) {
					 
				
				 //Appel à la methode pour expurger les données non nécessaires
				
				 ModeFinancement entityType = retournerModeFinancementEnFonctionDuType(entity);
				 
				 
				 entityType.setProgrammeImmobilier(programme);
				
				 ModeFinancement rtn = creerModifierUnModeFinancement( entityType, mustUpdateExistingNew,
										     namedGraph, isFetchGraph, locale,  loggedInUser, msgList);
				 
				 
					 if(rtn == null) {
						 
						 String msg  = MessageTranslationUtil.translate(locale,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_CREE,
							 		AppMessageKeys.CODE_TRADUCTION_MODEFINANCEMENT_NON_CREE, 
							 	entity.getMsgVarMap() ) ;
					  
					     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						 
					 }
					 
					  rtnList.add(rtn) ;
				  } 
					
				}
				
				//retoure liste de mode de financements créée
				
				return rtnList;
				
}
	
public ModeFinancement creerModifierUnModeFinancement(ModeFinancement entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
	      	
	
			ModeFinancement rtn = modeFinancementDAO.validerEtEnregistrer(
					                entity, mustUpdateExistingNew, namedGraph, 
					                isFetchGraph, locale, loggedInUser, msgList); 
		
		/*
		ModeFinancement rtn = modeFinancementDAO.validerEtEnregistrerNewTransactional(entity, 
							    mustUpdateExistingNew, namedGraph, 
								isFetchGraph, locale, 
								loggedInUser, msgList); */
		
		
		return rtn ;
		
	}

/**
 * 
 * Methode pour expurger les données non nécessaires au mode de financem
 * @param entity
 * @return
 */
public ModeFinancement retournerModeFinancementEnFonctionDuType(ModeFinancement entity) {
	
	     
	     ModeFinancement entityType = new ModeFinancement();
	     
	   if(entity.getTypeFinancement() != null && entity.getTypeFinancement().getId() != null
	    		                                && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_COMPTANT_SUR_SITUATION))
	     {
		   
	      entityType.setPallierComptantSurSituationList(entity.getPallierComptantSurSituationList());
	      entityType.setTypeFinancement(entity.getTypeFinancement());
	      
	     }
	     
	   else if(entity.getTypeFinancement() != null && entity.getTypeFinancement().getId() != null
                 && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_CREDIT_BANCAIRE))
		{
		   
		//  Verification la lettre de confort est non nulle 
		  
		  entity.getCreditBancaire().setLettreConfort( 
				          	  entity.getCreditBancaire().getLettreConfort() == null 
				           || entity.getCreditBancaire().getLettreConfort().getContenu() == null 
				           || entity.getCreditBancaire().getLettreConfort().getDesignation() == null
								? null
							: entity.getCreditBancaire().getLettreConfort());
		  
		entityType.setCreditBancaire(entity.getCreditBancaire());
		
		entityType.setTypeFinancement(entity.getTypeFinancement());
		
	
		}
	     
	   else if(entity.getTypeFinancement() != null && entity.getTypeFinancement().getId() != null
                 && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_TEMPERAMENT))
		{
		
		entityType.setTemperament(entity.getTemperament());
		entityType.setTypeFinancement(entity.getTypeFinancement());

		}
	     
	   else if(entity.getTypeFinancement() != null && entity.getTypeFinancement().getId() != null
               && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_TIERS_COLLECTEUR))
		{
		
		entityType.setTierscollecteur(entity.getTierscollecteur());
		entityType.setTypeFinancement(entity.getTypeFinancement());

		}
	   else if(entity.getTypeFinancement() != null && entity.getTypeFinancement().getId() != null
               && entity.getTypeFinancement().getId().equals(Reference.REF_ELEMENT_ID_COMPTANT))
		{
		
		 //Aucune information specifique au type de financement comptant
		    
		   entityType.setTypeFinancement(entity.getTypeFinancement());

		}
	   else {
		   
		   // A  mettre à jour
	   }
	     
	     entityType.setId(entity.getId());
	     entityType.setVersion(entity.getVersion());
	     entityType.setEstValide(entity.getEstValide());
	     entityType.setDesignation(entity.getDesignation());
	     entityType.setDescription(entity.getDescription());
			
	     return entityType ;
	     
    }
	
	
}
