package com.siliconwise.mmc.logement;

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
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.CreerModifierCaracteristiqueProduitLogementCtlInterface;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class CreerModifierUnLogementCtl implements CreerModifierUnLogementCtlInterface {

	
	@Resource 
	private EJBContext ejbContext;
	
	@Inject LogementDAOInterface logementDAO ;
	
	@Inject CreerModifierOptionLogementCtlInterface  creerModifierOptionLogementCtl ;
	
	
	private static transient Logger logger = LoggerFactory.getLogger(CreerModifierUnLogementCtl.class) ;
	
	@Override
	public Logement creerModifierUnLogement(
					Logement entity, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph,
					Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList,
					Boolean estCreation) {
	
			
		
		//TODO A mettre à jour les mesages d'erreurs
	   
	     if(estCreation  && entity.getId() != null) {
	    	 
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_PRODUITLOGMENT_ID_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_PRODUITLOGMENT_ID_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		  
		    return null ;
		    
	     }
	   
	   
	    //  S'il s'agit d'une modification et que l'Id et La version non renseignée alors renvoyer un message d'erreurs
	    //TODO A mettre à jour les mesages d'erreurs
	     
	    if(!estCreation  && entity.getId() == null) {
	    	
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_PRODUITLOGMENT_ID_NON_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_PRODUITLOGMENT_ID_NON_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		  
		     return null ;
		     
	    }
	    
	    
	    //Appel de la methode validerEtEnregistrer pour enregistrer le logement
		
	    Logement rtn =  logementDAO.validerEtEnregistrer(entity, true, null, 
	    							    true,locale, loggedInUser,  msgList);
		
		 
	    //Creation des caracteristiques
		  
	    if(entity.getCaracteristiqueLogementList() != null 
				 && !entity.getCaracteristiqueLogementList().isEmpty()) {
		   		
			   
			   //Initialisation  des variables
			   
			   List<CaracteristiqueProduitLogement> rtnList = new ArrayList<CaracteristiqueProduitLogement>();
			   
			   Set<CaracteristiqueProduitLogement> entityCaracterisitqueList = new HashSet<CaracteristiqueProduitLogement>();
			   
			   entityCaracterisitqueList =  entity.getCaracteristiqueLogementList();
			   
			   //Appel à la methode de creation des caracteristiques si le produit est crée
			   
		   		if(rtn != null && rtn.getId() != null ) {
		   			
			   		rtnList = creerModifierOptionLogementCtl
			   					.creerModifierOptionLogementList(entityCaracterisitqueList, 
			   					  mustUpdateExistingNew, rtn, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			   		
			   	
			   		if(rtnList == null || rtnList.size() == 0) {
				   		
					   	 String msg  = MessageTranslationUtil.translate(locale,
								 		AppMessageKeys.CODE_TRADUCTION_CARACTERISTIQUE_PRODUITLOGEMENT_NON_CREES,
								 		AppMessageKeys.CODE_TRADUCTION_CARACTERISTIQUE_PRODUITLOGEMENT_NON_CREES, 
								 	entity.getMsgVarMap() ) ;
						  
						  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						  
						  rtn = null ;
						  
						  try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						  
					}
			   		
			   	}
		   		 	
	      }
		   
	   return rtn;
	   
	 }
	
}
