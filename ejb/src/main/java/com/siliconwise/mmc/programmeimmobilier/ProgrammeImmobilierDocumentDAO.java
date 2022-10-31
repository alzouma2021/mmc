package com.siliconwise.mmc.programmeimmobilier;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentDAOInterface;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author ALzouma Moussa Mahaamadou
 * 
 *
 */
@Stateless
public class ProgrammeImmobilierDocumentDAO implements Serializable , ProgrammeImmobilierDocumentDAOInterface{

	
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager ;
	
	@Inject DocumentDAOInterface  documentDAO ;
			
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	@Resource 
	private EJBContext ejbContext;
	
	
	
	public boolean  valider(ProgrammeImmobilierDocument entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale, 
			List<NonLocalizedStatusMessage> msgList) {
		
	
		
		// verifiy that version is defined if entity id si not null
		
		if (entity.getId() != null && entity.getVersion() == null) {
			
			String msg  = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
			
			return false ;
		}
	
		// association
	
		try {
			
			
			EntityUtil<ProgrammeImmobilierDocument> entityUtil = new EntityUtil<ProgrammeImmobilierDocument>(ProgrammeImmobilierDocument.class) ;
			
			//ProduitLogementDocument vers document
			
			Map<String,String>  msgVarMap = entity.getDocument() == null 
							 || entity.getDocument().getMsgVarMap() == null
										   ?  new HashMap<String,String>() :entity.getDocument().getMsgVarMap() ;
						
			boolean isAttached = entityUtil.attachLinkedEntity(entityManager, entity, 
					entity.getDocument(), 
					entity.getClass().getDeclaredMethod("setDocument", Document.class), 
					null,true, locale, AppMessageKeys.CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE, 
					msgVarMap, msgList) ;
		
			
			if (!isAttached) return false ;
		
		
			// ProduitLogementDocument vers Produit logement
			
			msgVarMap = entity.getProgrammeImmobilier() == null 
					 || entity.getProgrammeImmobilier().getMsgVarMap() == null
								   ?  new HashMap<String,String>() : entity.getProgrammeImmobilier().getMsgVarMap() ;
								   
			isAttached = entity.getProgrammeImmobilier() == null ? true
			             : entityUtil.attachLinkedEntity(entityManager, entity, 
							entity.getProgrammeImmobilier(), 
							entity.getClass().getDeclaredMethod("setProgrammeImmobilier", ProgrammeImmobilier.class), 
							null, true,  locale, AppMessageKeys.CODE_TRADUCTION_FORMAT_NOT_FOUND, 
							msgVarMap, msgList) ;
		
			
			if (!isAttached) return false ;
			
			
		} 
		catch(Exception ex) {
			
			String msg  = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
			
			logger.error("_198 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
			ex.printStackTrace();
			
			return false ;
		}
		
		
		//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		Set<ConstraintViolation<ProgrammeImmobilierDocument>> constraintViolationList = validator.validate(entity) ;
		
		for (@SuppressWarnings("unused") ConstraintViolation<ProgrammeImmobilierDocument> violation : constraintViolationList) {
			
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
		}
				
		if (!constraintViolationList.isEmpty()) return false ;
		
		
		//TODO verification d'integrite complexes le cas echeant
		
		return true;
	}
	
	
	public ProgrammeImmobilierDocument validerEtEnregistrer(
			ProgrammeImmobilierDocument entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser,
			List<NonLocalizedStatusMessage> msgList){
	
		//	Locale langue = SessionUtil.getLocale(currentSession) ;
		
		if (entity == null) {
			
			 String msg  = MessageTranslationUtil.translate(locale,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		}
	
		
		boolean estValide = valider(entity, mustUpdateExistingNew,
				             namedGraph, isFetchGraph,locale, msgList) ;
		

		if (!estValide) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return null ; 
		}
	
		
		//Enregistrement des MetaData
		
		ProgrammeImmobilierDocument rtn = EntityUtil.persistOrMerge(
				entityManager, ProgrammeImmobilierDocument.class, entity, 
				namedGraph, isFetchGraph, 
				AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
				locale, msgList);
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		}
	
					
		return rtn ;
		
	}


	
	@SuppressWarnings("unchecked")
	@Override
	public List<Document> rechercherDocumentListParProgrammeImmobilierParTypeDocument(
				                String idProgrammeImmobilier,
								String typeDocument, 
								boolean mustUpdateExistingNew, 
								String namedGraph,
								boolean isFetchGraph, 
								Locale locale,
								List<NonLocalizedStatusMessage> msgList) {
		
		 
			//Verification du l'id produit logement
			
		    if (idProgrammeImmobilier == null || idProgrammeImmobilier.isEmpty())  return  null ;
		    
		    //Verification de la variable type document 
		    
		    if (typeDocument == null || typeDocument.isEmpty())  return  null ;
		
			
		    //Try catch
		    
		    List<Document>   rtnList = new ArrayList<Document>();
		
			try {
				
			  //Appel à la requete nommée
			  
			  rtnList = (List<Document>) entityManager
		 		           .createNamedQuery("rechercherDocumentListParProgrammeImmobilierParTypeDocument")
		 		           .setParameter("idProgrammeImmobilier", idProgrammeImmobilier)
		 		           .setParameter("idTypeDocument", typeDocument)
		 		           .getResultList() ;
				
			} catch (NoResultException e) {
	
				return null ;
			}
		
	     	return rtnList;
			
		}


	@SuppressWarnings("unchecked")
	@Override
	public List<Document> rechercherDocumentListParIdProgrammeImmobilier(
		                   String idProgrammeImmobilier,
			               boolean mustUpdateExistingNew, 
			               String namedGraph, boolean isFetchGraph,
			               Locale locale,
			               List<NonLocalizedStatusMessage> msgList) {
		
		
		//Verification du l'id programme immobilier
		
	    if (idProgrammeImmobilier == null || idProgrammeImmobilier.isEmpty())  return  null ;
	    
	  
	    //Try catch
	    
	    List<Document>   rtnList = new ArrayList<Document>();
	
		try {
		  
		  rtnList = (List<Document>) entityManager
	 		           .createNamedQuery("rechercherDocumentListParIdProgrammeImmobilier")
	 		           .setParameter("idProgrammeImmobilier", idProgrammeImmobilier)
	 		           .getResultList() ;
			
		} catch (NoResultException e) {

			return null ;
		}
	
     	return rtnList;
     	
	}


	@SuppressWarnings("static-access")
	@Override
	public boolean supprimerDocumentParProgrammeImmobilier(
			         String idDocument,
			         boolean mustUpdateExistingNew, 
			         String namedGraph,
			         boolean isFetchGraph, 
			         Locale locale, User loggedInUser, 
			         List<NonLocalizedStatusMessage> msgList) {
		
		
		// Verification de la variable
		
		if(idDocument == null || idDocument.isEmpty() ) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
			
		}
		
		//Remettre l'entité dans le contexte de persistence
		
	    EntityUtil<Document> entityUtil = new EntityUtil<Document>(Document.class) ;
		

		Document document =  entityUtil
	    		               .findEntityById(entityManager, idDocument, "graph.document.minimum-sans-contenu", isFetchGraph, Document.class) ;
	    
		
	    //Vérification de l'entité trouvée
		
		if (document == null) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
			
		}
		
		
		//Supprimer le document dans la classe d'association programmeImmobilierDocument
		
	    int result = entityManager
	    		        .createNamedQuery("supprimerProgrammeImmobilierDocumentParId")
	    		        .setParameter("idDocument", idDocument)
	    		        .executeUpdate();
	    
	    if(result == 0) {
	    	
	      try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
	      
	      return false ;
	    	
	    }
		
		//Verification du path du document 
		
		if (document.getPath() == null || document.getPath().isEmpty()) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DOUCMENT_PATH_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DOUCMENT_PATH_NON_TROUVE, // Message par defaut
						msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			 
			return false ;
			
		}
	
		
		//Récuperation du path avant suppresion
		
		String pathRelatif = document.getPath() ;
		
		//Suppression des metadatas des documents
		
		entityManager.remove(document);
		
		//Verification de la suppression de l'entité document
		
		logger.info("_409 Vérification de l'entité supprimée ");
		
		Document deleteDocument = entityManager.find(Document.class, idDocument) ;
		
		if(deleteDocument != null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return false ;
		}
		
		
		//Suppression du contenu du document sur le disque dur 
	
		
		boolean value = documentDAO
				            .deleteStringContentDocumentByPath(pathRelatif, msgList) ;
		
		
		if(!value) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return false ;
			
		}
		
		//Retourne vrai si la suppression a été effectuée avec succés
		
		return true;
		
	
	}
		
		
}

