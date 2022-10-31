package com.siliconwise.mmc.produitlogement;

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
 * @author Alzouma Moussa Mahamadou
 *
 */
//TODO Optimisation de la creation de la classe d'aasociation EntityDocument
@Stateless
public class CreerModifierUnProduitLogementCtl implements CreerModifierUnProduitLogementCtlInterface {

	
	@Resource private EJBContext ejbContext;
	
	@Inject ProduitLogementDAOInterface produitLogementDAO ;
	
	@Inject DocumentCtlInterface documentCtl ;

	@Inject ProduitLogementDocumentCtlInterface produitLogementDocumentCtl ;
	
	boolean updateStringContentDocument = false ;
	
	@Inject CreerModifierCaracteristiqueProduitLogementCtlInterface  creerModifierCaracteristiqueProduitLogementCtl ;
	
	private static transient Logger logger = LoggerFactory.getLogger(CreerModifierUnProduitLogementCtl.class) ;
	
	@Override
	public ProduitLogement creerModifierUnProduitLogement(
			                 ProduitLogementTransfert entityTransfert, 
							 boolean mustUpdateExistingNew,
							 String namedGraph, boolean isFetchGraph,
							 Locale locale, User loggedInUser,
							 List<NonLocalizedStatusMessage> msgList,
							 Boolean estCreation) {
		
			 //Verification de la variable entity
			
			if (entityTransfert == null || entityTransfert.getProduitLogement() == null) {
				
				Map<String,String> msgVarMap =  new HashMap<String,String>();
				
				String msg  = MessageTranslationUtil.translate(locale,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
						 AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						 msgVarMap) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				return null ;
			}
			
		
		 //Verification si il s'agit d'une creation ou d'une modification
		   
	     //  S'il s'agit d'une creation et l'Id renseigné alors renvoie un message d'erreurs
	     
		 ProduitLogement entity = entityTransfert.getProduitLogement() ;
	   
	     if(estCreation  && entity.getId() != null) {
	    	 
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_PRODUITLOGMENT_ID_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_CREATION_PRODUITLOGMENT_ID_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		  
		    return null ;
		    
	     }
	   
	   
	    //  S'il s'agit d'une modification et que l'Id et La version non renseignée alors renvoyer un message d'erreurs
	    
	    if(!estCreation  && entity.getId() == null) {
	    	
	    	 String msg  = MessageTranslationUtil.translate(locale,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_PRODUITLOGMENT_ID_NON_RENSEIGNE,
				 		AppMessageKeys.CODE_TRADUCTION_MODIFICATION_PRODUITLOGMENT_ID_NON_RENSEIGNE, 
				 	entity.getMsgVarMap() ) ;
		  
		     msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		  
		     return null ;
		     
	    }
	    
	    
	    //Appel de la methode validerEtEnregistrer pour enregistrer le produit logement
		
	    ProduitLogement rtn =  produitLogementDAO
	    		                  .validerEtEnregistrer(entity, true, null, 
	    								 true,locale, loggedInUser,  msgList);
		
		 
	//Creation des caracteristiques
		  
	   if(entity.getCaracteristiqueProduitLogementList() != null 
				 && entity.getCaracteristiqueProduitLogementList().size() > 0) {
		   		
			   
			   //Initialisation  des variables
			   
			   List<CaracteristiqueProduitLogement> rtnList = new ArrayList<CaracteristiqueProduitLogement>();
			   
			   Set<CaracteristiqueProduitLogement> entityCaracterisitqueList = new HashSet<CaracteristiqueProduitLogement>();
			   
			   entityCaracterisitqueList =  entity.getCaracteristiqueProduitLogementList();
			   
			   //Appel à la methode de creation des caracteristiques si le produit est crée
			   
		   		if(rtn != null && rtn.getId() != null ) {
		   			
			   		rtnList = creerModifierCaracteristiqueProduitLogementCtl
			   							.creerModifierCaracteristiqueProduitLogementList(
			   								entityCaracterisitqueList, mustUpdateExistingNew, 
			   								rtn, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			   		
			   	
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
		   
		   
		    //Création de l'image de consultation du produit logement si elle existe
		   
		    if(entityTransfert.getImageConsultation() !=  null &&
		    		    entityTransfert.getImageConsultation().getContenu() != null ) {
			
				Document imageConsultation = documentCtl
									 .creerModifierDocument(
									    entityTransfert.getImageConsultation() , 
										mustUpdateExistingNew,updateStringContentDocument,
										namedGraph,isFetchGraph, locale, loggedInUser, msgList);
				
				if (imageConsultation == null) {
					
					try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
					
					return null ;
					
				}
				
				//Creation de la classe d'aasociation
				
				ProduitLogementDocument produitLogementDocument =  new ProduitLogementDocument();
   				
   				produitLogementDocument.setDocument(imageConsultation);
   				produitLogementDocument.setProduitLogement(rtn);
   					
   				ProduitLogementDocument produitLogementDocumentCree = produitLogementDocumentCtl
						   						                       .validerEtEnregistrer(
						   						                    	  produitLogementDocument, 
						   						                    	  mustUpdateExistingNew, namedGraph, 
						   						                    	  isFetchGraph, locale, loggedInUser, msgList) ;
   				
   				if (produitLogementDocumentCree == null) {
					
					try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
					
					return null ; 
					
			    }
   				
   			logger.info("_218 Creation de la classe d'association ProduitLogementDocument reussie !!! ="+produitLogementDocumentCree.toString());
   				
			}
		  
		   
		   // Creation des documents de type Image
		   
		   if(entityTransfert.getImagesList() != null 
				   && entityTransfert.getImagesList().size() > 0) {
			   

			   List<Document> rtnList = new ArrayList<Document>();
			   
			   rtnList = documentCtl.creerModifierDocumentList(
					                  entityTransfert.getImagesList(), 
					                  mustUpdateExistingNew,updateStringContentDocument, 
					                  namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			   
			   //Creation de la classe d'aasociation 
			   
			   if(!rtnList.isEmpty() && rtnList.size() > 0) {
				   
				     
				   for(Document image: rtnList) {
					   
		   				ProduitLogementDocument produitLogementDocument =  new ProduitLogementDocument();
		   				
		   				produitLogementDocument.setDocument(image);
		   				produitLogementDocument.setProduitLogement(rtn);
		   					
		   				ProduitLogementDocument produitLogementDocumentCree 
		   				                            = produitLogementDocumentCtl
								   						 .validerEtEnregistrer(
								   						    produitLogementDocument, 
								   						    mustUpdateExistingNew, 
								   						    namedGraph, 
								   						    isFetchGraph, 
								   						    locale, 
								   						    loggedInUser, msgList) ;
		   				
		   				
		   				
		   				if (produitLogementDocumentCree == null) {
							
							try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
							
							return null ; 
							
					    }
		   				
		   				
		   			    logger.info("_270 Creation de la classe d'association ProduitLogementDocument reussie !!! ="+produitLogementDocumentCree.toString());
		   					
				   }
				   
			   }
			     
		    }
		   
		   
		   // Creation des documents de type video
		   
		   if(entityTransfert.getVideosList() != null 
						&& entityTransfert.getVideosList().size() > 0) {

			   	
			    List<Document> rtnList = new ArrayList<Document>();
			   
			   	rtnList = documentCtl.creerModifierDocumentList(
							             entityTransfert.getVideosList(), 
							             mustUpdateExistingNew,updateStringContentDocument, 
							             namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			   
			   
			   if(!rtnList.isEmpty() && rtnList.size() > 0) {
				   
				     
				   for(Document video: rtnList) {
					   
		   				ProduitLogementDocument produitLogementDocument =  new ProduitLogementDocument();
		   				
		   				produitLogementDocument.setDocument(video);
		   				produitLogementDocument.setProduitLogement(rtn);
		   					
		   				ProduitLogementDocument produitLogementDocumentCree 
		   				                              = produitLogementDocumentCtl
								   						   .validerEtEnregistrer(
								   						      produitLogementDocument, 
								   						      mustUpdateExistingNew, 
								   						      namedGraph, 
								   						      isFetchGraph,
								   						      locale, loggedInUser, msgList) ;
		   				
		   				
		   				if (produitLogementDocumentCree == null) {
							
							try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
							
							return null ; 
							
					    }
		   				
		   			    logger.info("_323 Creation de la classe d'association ProduitLogementDocument reussie !!! ="+produitLogementDocumentCree.toString());
		
				   }
				   
			   }
				   
		   }
		   
	   return rtn;
	   
	 }

	@Override
	public ProduitLogement creerModifierUnProduitLogementBis(
			               ProduitLogement entity, 
			               boolean mustUpdateExistingNew,
			               String namedGraph, boolean 
			               isFetchGraph, Locale locale, 
			               User loggedInUser,
			               List<NonLocalizedStatusMessage> msgList,
			               Boolean estCreation) {
							
		

		   if(entity.getCaracteristiqueProduitLogementList() != null 
				   		&& entity.getCaracteristiqueProduitLogementList().size() > 0) {
		   		

			   //Initialisation  des variables
			   
			   List<CaracteristiqueProduitLogement> rtnList = new ArrayList<CaracteristiqueProduitLogement>();
			   
			   Set<CaracteristiqueProduitLogement> entityCaracterisitqueList = new HashSet<CaracteristiqueProduitLogement>();
			   
			   entityCaracterisitqueList =  entity.getCaracteristiqueProduitLogementList();
			   
			   //Appel à la methode de creation des caracteristiques si le produit est crée
			   
		   		if(entity != null && entity.getId() != null && !entity.getId().isEmpty()) {
		   			
			   		rtnList = creerModifierCaracteristiqueProduitLogementCtl
			   						.creerModifierCaracteristiqueProduitLogementList(
			   						   entityCaracterisitqueList,
			   						   mustUpdateExistingNew, 
			   						   entity, namedGraph,
			   						   isFetchGraph, locale, 
			   						   loggedInUser, msgList);
			   		
			   		
			   		if(rtnList == null || rtnList.size() == 0) {
				   		
					   	 String msg  = MessageTranslationUtil.translate(locale,
								 		AppMessageKeys.CODE_TRADUCTION_CARACTERISTIQUE_PRODUITLOGEMENT_NON_CREES,
								 		AppMessageKeys.CODE_TRADUCTION_CARACTERISTIQUE_PRODUITLOGEMENT_NON_CREES, 
								entity.getMsgVarMap() ) ;
						  
						  msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						  
						  entity = null ;
						  
						  try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
					   		
					}
			   		
			   	}
		   		 	
	      }
		
		
		return produitLogementDAO
				     .validerEtEnregistrer(entity, mustUpdateExistingNew, namedGraph, 
									       isFetchGraph, locale, loggedInUser, msgList);
		
	}
	
}
