package com.siliconwise.mmc.programmeimmobilier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.CreerModifierModeFinancementCtlInterface;
import com.siliconwise.mmc.modefinancement.ModeFinancement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class CreerModifierUnProgrammeImmobilierCtl implements CreerModifierUnProgrammeImmobilierCtlInterface{

	
	@Resource
	private EJBContext ejbContext;
	
	@Inject  ProgrammeImmobilierDAOInterface programmeImmobilierDAO ;
	
	@Inject  CreerModifierModeFinancementCtlInterface CreerModifierModeFinancementCtl ;
	
	@Inject DocumentCtlInterface documentCtl ;
	
	@Inject ProgrammeImmobilierDocumentCtlInterface programmeImmobilierDocumentCtl ;
	
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	@Override
	public ProgrammeImmobilier creerModifierUnProgrammeImmobilier(
					ProgrammeImmobilierTransfert entityTransfert,
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
		
		
		//Instanciation des classes ProgrammeImmobilier ModeFinancement
		
		ProgrammeImmobilier entity = new ProgrammeImmobilier();
		
		Set<ModeFinancement>   entityFinancementList = new  HashSet<>();
		
		//Initialisation des classes ProgrammeImmobilier et Modefinancement à partir de  ProgrammeImmobilierTransfert
				
		if(entityTransfert.getProgrammeImmobilier() == null) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
					 AppMessageKeys.CODE_TRADUCTION_PROGRAMME_IMMOBILIER_NON_RENSEIGNE,
					 AppMessageKeys.CODE_TRADUCTION_PROGRAMME_IMMOBILIER_NON_RENSEIGNE, 
					 msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
			
		}
		
		
		entity = entityTransfert.getProgrammeImmobilier() ;
	
		entityFinancementList = entityTransfert.getModeFinancementList() != null ? 
										entityTransfert.getModeFinancementList() : null ;
		
		
		//Verification si il s'agit d'une creation ou d'une modification
		   
	    //  S'il s'agit d'une creation et l'Id renseigné alors renvoie un message d'erreurs
	   
	     if(estCreation  && entity.getId() != null) {
	    	 
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_PROGRAMMEIMMOBILIER_ID_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_PROGRAMMEIMMOBILIER_ID_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		    
		  
		    return null ;
		    
	     }
	   
	   
	    //  S'il s'agit d'une modification et que l'Id et La version non renseignée alors renvoyer un message d'erreurs
	    
	    if(!estCreation  && entity.getId() == null) {
	    	
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_PROGRAMMEIMMOBILIER_ID_NON_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_PROGRAMMEIMMOBILIER_ID_NON_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		  
		     return null ;
	    }
	    
	    
	    // Persistence du programme immobilier
		
		ProgrammeImmobilier rtn = programmeImmobilierDAO.validerEtEnregistrer(
											entity,
											mustUpdateExistingNew, 
											namedGraph, isFetchGraph, locale, 
											loggedInUser, msgList);
		
		
		if(rtn == null ) return null ;
	
		 // Creation des documents de type video
		
		   if(entityTransfert.getVideosList() != null 
						&& entityTransfert.getVideosList().size() > 0) {
			   
			   List<Document> rtnList = new ArrayList<Document>();
			   
			   boolean updateStringContentDocument = false;
			   
			   rtnList = documentCtl.creerModifierDocumentList(
							           entityTransfert.getVideosList(), 
							           mustUpdateExistingNew,updateStringContentDocument,
							           namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			   

			   for(Document video: rtnList) {
				   
	   				ProgrammeImmobilierDocument programmeImmobilierDocument =  new ProgrammeImmobilierDocument();
	   				
	   				programmeImmobilierDocument.setDocument(video);
	   				programmeImmobilierDocument.setProgrammeImmobilier(rtn);
	   					
	   				ProgrammeImmobilierDocument programmeImmobilierDocumentCree 
	   				                                         = programmeImmobilierDocumentCtl
							   						                       .validerEtEnregistrer(
							   						                    	  programmeImmobilierDocument, 
							   						                    	  mustUpdateExistingNew, namedGraph, 
							   						                    	  isFetchGraph, locale, loggedInUser, msgList) ;
	   				
	   				if (programmeImmobilierDocumentCree == null) {
						
						try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						
						return null ; 
						
				    }
	   				
	   			    logger.info("_160 Creation de la classe d'association ProduitLogementDocument reussie !!! ="+programmeImmobilierDocumentCree.toString());
	   					
		        }
			   
		    }
		   
	
		/**
		 * 
		 * Creation des mode de financements du programme immobilier
		 * 
		 */
		 
		 // Appel à la methode de creation des modes de financement

			   	if( entityFinancementList != null 
			   			           && entityFinancementList.size() > 0) {
	
			   	  
			   	  List<ModeFinancement>	rtnList = CreerModifierModeFinancementCtl
			   										.creerModifierModeFinancementList(
			   												entityFinancementList, 
			   												mustUpdateExistingNew,rtn, namedGraph, 
			   												isFetchGraph, locale, loggedInUser, 
			   												msgList);
			   	  
			   		
			   		if(rtnList == null || rtnList.size() == 0) {
			   			
					   	  String msg  = MessageTranslationUtil.translate(locale,
								 		AppMessageKeys.CODE_TRADUCTION_MODE_FINANCEMENT_NON_CREES,
								 		AppMessageKeys.CODE_TRADUCTION_MODE_FINANCEMENT_NON_CREES, 
								 	entity.getMsgVarMap() ) ;
						  
						  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					   		
					}
			   		
			
			   	}
		
		return rtn ;
		
	}

	
}
