package com.siliconwise.common.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocument;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentCtlInterface;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocument;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentCtlInterface;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class DocumentCtl implements Serializable , DocumentCtlInterface {

	
	private static final long serialVersionUID = 1L;
	
	@Inject DocumentDAOInterface documentDAO ;
	
	@Inject ProduitLogementDocumentCtlInterface produitLogementDocumentCtl ;
	
	@Inject ProgrammeImmobilierDocumentCtlInterface programmeImmobilierDocumentCtl ;
	
	@Resource private EJBContext ejbContext;
	
	@PersistenceContext
	EntityManager entityManager ;

	private static transient Logger logger = LoggerFactory.getLogger(DocumentCtl.class) ;
	
	
	@Override
	public List<Document> creerModifierDocumentList(
								Set<Document> entityList, 
								boolean mustUpdateExistingNew,
								boolean updateStringContentDocument,
								String namedGraph, 
								boolean isFetchGraph, 
								Locale locale, User loggedInUser,
								List<NonLocalizedStatusMessage> msgList) {
		
			
		   List<Document> rtnList = new ArrayList<Document>();
		
		   // Creation d'une liste de document
		
		   for(Document entitydoc: entityList) {
			
			   
			   	if(entitydoc != null) {
			   			
			   	     Document document = creerModifierDocument(
			   	    		                entitydoc, mustUpdateExistingNew, 
			   	    		                updateStringContentDocument,
			   	    		                namedGraph, isFetchGraph, 
			   	    		                locale, loggedInUser, msgList) ;
			   			
			   	     
			   			if (document == null) {
							
							try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
							
							return null ; 
							
						}
			   			
			   			
			   			rtnList.add(document) ;
			   			
			   	}
			 
		   }
			   			
		return rtnList ;
		
	}
	
	
	//Modification d'un document 
	
	@Override
	public  Document creerModifierDocument(
						Document entity, boolean mustUpdateExistingNew, 
						boolean updateStringContentDocument ,
					    String namedGraph,
						boolean isFetchGraph,
						Locale locale, User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList) {
		
	 
	 	Document rtn = documentDAO
	 			         .validerEtEnregistrer(
							entity, mustUpdateExistingNew, 
							updateStringContentDocument,
							namedGraph,isFetchGraph, locale, 
							loggedInUser, msgList);
	
		return rtn;
		
	}
	
	
	
    //Récuperation d'un document par id
	
	@Override
	public Document findStringDocumentById(
			  String id, boolean isContentSkipped, Locale locale,
		      List<NonLocalizedStatusMessage> msgList) {
		
		
		Document rtn = documentDAO
				        .findStringDocumentById(id, isContentSkipped, locale, msgList) ;
		
		
		return rtn ;
		
		
	}
	
	
	@Override
	public byte[] findBytesDocumentById(
			        String id, boolean isContentSkipped,
			        Locale locale,
			        List<NonLocalizedStatusMessage> msgList) {
	
	
		return  documentDAO
				  .findBytesDocumentById(id, isContentSkipped, locale, msgList) ;
		
	}


	@Override
	public String findStringContentDocumentParPath(
			          String path, List<NonLocalizedStatusMessage> msgList) {
		
		
		String rtn = documentDAO
				       .findStringContentDocumentParPath(path, msgList) ;
		
		return rtn;
		
		
	}


	@Override
	public boolean supprimerDocumentList(
			        List<Document> entityList, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph,
		        	boolean isFetchGraph, Locale locale, 
		        	User loggedInUser, 
		        	List<NonLocalizedStatusMessage> msgList) {
		
		
		//Verification de la variable entityList
		
		if(entityList == null || entityList.isEmpty() 
				              || entityList.size() == 0) return false ;
		
		
		//Appel à la methode supprimerUnDocument
		
		for(Document entity: entityList) {
			
			if(entity != null && entity.getId() != null) {
			
				
				boolean rtn =  supprimerUnDocument( entity.getId(),mustUpdateExistingNew, namedGraph, 
						                             isFetchGraph, locale,  loggedInUser,  msgList  ) ;
				
				//RollBack de la transaction si la suppression est échouée
				
				if(!rtn) {
					
					try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
					
					return false ; 
					
				}
				
			}
			
		}
			
		return true;
		
	}


	@Override
	public boolean supprimerUnDocument(
				       String idDocument, 
				       boolean mustUpdateExistingNew, 
				       String namedGraph, boolean isFetchGraph,
				       Locale locale, User loggedInUser, 
				       List<NonLocalizedStatusMessage> msgList) {
		
		return documentDAO.supprimer(idDocument, mustUpdateExistingNew, 
				     namedGraph, isFetchGraph, locale, loggedInUser, msgList);
	
	}


	@Override
	public String updateStringContentDocument(
						Document entity,
						boolean mustUpdateExistingNew,
						boolean updateStringContentDocument,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList){
			
		
			String rtn = documentDAO
					      .updateStringContentDocument(entity, mustUpdateExistingNew,
					    	updateStringContentDocument,namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
			
			return rtn;
		
	}
	
	
	
	@Override
	public boolean appendEntityDocument(
					 DocumentEntityTransfert entity, 
			         boolean mustUpdateExistingNew,
			         String namedGraph, 
			         boolean isFetchGraph, Locale locale,
			         User loggedInUser, List<NonLocalizedStatusMessage> msgList) {
		
		
		//Vérification des entités 
		
		if(entity == null || entity.getDocument() == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			
			return false ;
			
		}
		
		
		//Veérification du nom et de  l'identifiant de l'entité
		
		if(entity.getEntityId() == null || entity.getEntityName() == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			
			return false ;
			
		}
		
		
		//Appel à la methode de création d'un document 
		
		 boolean updateStringContentDocument = false ;
		
		 Document rtn = creerModifierDocument(entity.getDocument(), mustUpdateExistingNew, 
	                       				updateStringContentDocument,namedGraph, isFetchGraph, 
	                       				locale, loggedInUser, msgList) ;
		
		
		    if (rtn == null) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return false ;
				
			}
		  
		
		 //Création de la classe d'association en fonction de l'entité
		    
		 if(entity.getEntityName().equals(Reference.REF_ELEMENT_TYPE_PRODUITLOGEMENT)) {
			 
			 // Remettre l'entité dans le contexte de persistence
			 
			 ProduitLogement produitLogement 
			                     = entityManager.find(ProduitLogement.class, entity.getEntityId()) ;
			 
			 
				 if (produitLogement == null) {
						
						String msg  = MessageTranslationUtil.translate(locale,
							 	AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE,
								AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE,
								new String[] {}) ;
					    msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					   
					    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
						
						return false ;
						
				}
				 
				
			    ProduitLogementDocument produitLogementDocument =  new ProduitLogementDocument();
	   				
	   			produitLogementDocument.setDocument(rtn);
	   			produitLogementDocument.setProduitLogement(produitLogement);
	   					
	   			ProduitLogementDocument  produitLogementDocumentCree
	   			                                   = produitLogementDocumentCtl
							   						   .validerEtEnregistrer(
							   						       produitLogementDocument,  mustUpdateExistingNew, namedGraph, 
							   						       isFetchGraph, locale, loggedInUser, msgList) ;
	   				
	   		    if (produitLogementDocumentCree == null) {
						
				    try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						
				     return false ; 
						
				 }
			 
			 
		 }else if (entity.getEntityName().equals(Reference.REF_ELEMENT_TYPE_PROGRAMMEIMMOBILIER)) {
			 
			 
			 // Remettre l'entité dans le contexte de persistence
			 
			 ProgrammeImmobilier programmeImmobilier 
			                     = entityManager.find(ProgrammeImmobilier.class, entity.getEntityId()) ;
			 
			 
				 if (programmeImmobilier == null) {
						
					   String msg  = MessageTranslationUtil.translate(locale,
								 	AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE,
									AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE,
									new String[] {}) ;
					   msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
						   
					   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
							
					   return false ;
							
				}
				 
				
			    ProgrammeImmobilierDocument programmeImmobilierDocument =  new ProgrammeImmobilierDocument();
	   				
			    programmeImmobilierDocument.setDocument(rtn);
			    programmeImmobilierDocument.setProgrammeImmobilier(programmeImmobilier);
	   					
			    ProgrammeImmobilierDocument  programmeImmobilierDocumentCree
	   			                                   = programmeImmobilierDocumentCtl
							   						   .validerEtEnregistrer(
							   							   programmeImmobilierDocument,  mustUpdateExistingNew, 
							   							   namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
	   				
	   	        if (programmeImmobilierDocumentCree == null) {
						
					try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						
					return false ; 
						
				}
			 
			 
		 }else {
			 
			 //Ajouter au fur à mesure
			 
		 }
		 
		return true;
		
	}


	@Override
	public List<DocumentFormat> findAllDocumentFormats(String namedGraph, boolean isFetchGraph) {
		
		return documentDAO.findAllDocumentFormats(namedGraph, isFetchGraph);
		
		
	}
	
	
}
