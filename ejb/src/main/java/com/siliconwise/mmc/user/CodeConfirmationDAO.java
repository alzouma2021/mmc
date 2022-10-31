package com.siliconwise.mmc.user;

import java.io.Serializable;
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
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.siliconwise.common.mail.EmailService;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.*;
import com.siliconwise.mmc.security.SecurityService;


/**
 * 
 * Classe DAO CodeConfirmation
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 07/10/2021
 * 
 */
@Stateless
public class CodeConfirmationDAO implements Serializable, CodeConfirmationDAOInterface {

	
		private static final long serialVersionUID = 1L;

		@PersistenceContext
		private EntityManager entityManager;
	
		@Resource
		private EJBContext ejbContext;
	
		private transient Logger logger = LoggerFactory.getLogger(getClass().getName());
		
		@Inject EmailService emailService ;
		
		@Inject SecurityService securityService ;

		@Override
		public boolean valider(
					 CodeConfirmation entity, 
					 boolean mustUpdateExistingNew, 
					 String namedGraph, 
					 boolean isFetchGraph, 
					 Locale locale,
					 List<NonLocalizedStatusMessage> msgList) {
			
			
			//Vérifier l'id du user auquel appartient le code de confirmation
			//Si l'id du user est nul ou vide alors return false
			
			if(entity.getIdUser() == null || entity.getIdUser().isEmpty()) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, // Message par defaut
						entity.getMsgVarMap()) ;
				
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
				return false ;
				
			}
			 
			// recherche de doublon
			
			boolean isEntityDuplictedOrNotFound = new EntityUtil<CodeConfirmation>().isEntityDuplicatedOrNotFound(
						entityManager, entity, mustUpdateExistingNew, "codeConfirmationIdParIdUser", 
						new String[] {"idUser"}, new String[] {entity.getIdUser()},
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
			

			//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
			
			logger.info("_1141 debut  Verification des contraintes"); //TODO A effacer
			
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
			Set<ConstraintViolation<CodeConfirmation>> constraintViolationList = validator.validate(entity) ;
			
			for (ConstraintViolation<CodeConfirmation> violation : constraintViolationList) {
				
				String translatedMessage = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
				
				
			}
					
			if (!constraintViolationList.isEmpty()) return false ;
			
			logger.info("_126 fin  Verification des contraintes"); //TODO A effacer
			
			return true;
			
		
		}

		
		
		@Override
		public CodeConfirmation validerEtEnregistrer(
				     CodeConfirmation entity, 
				     boolean mustUpdateExistingNew, 
				     String namedGraph,
				     boolean isFetchGraph, 
				     Locale locale, User loggedInUser,
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
			
			// Appel de la methode Valider
			
			boolean estValide = valider(entity, mustUpdateExistingNew, namedGraph, isFetchGraph,
									locale, msgList) ;
			
			
			if (!estValide) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return null ; 
			}
			
	
			//Methode de persistence de l'entité correspondante
			
			CodeConfirmation rtn = EntityUtil.persistOrMerge(
						entityManager, CodeConfirmation.class, entity, 
						namedGraph, isFetchGraph, 
						AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
					  locale, msgList);
			
			
			if (rtn == null) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
				
				return null ;
			}
			
		
			//Retourne le resultat
			
			return rtn ;
			
		}

		
		@Override
		public CodeConfirmation rechercherUnCodeConfirmationParId(
				       		String id,
				       		String namedGraph, 
				       		boolean isFetchGraph,
				       		Class<CodeConfirmation> entityClass) {
			
			//Vérification de la variable
			
			if (id == null || id.isEmpty() ) return null ;
			
			//Appelle à la requếte nommée

			CodeConfirmation rtn = (new EntityUtil<CodeConfirmation>(CodeConfirmation.class))
						   .findSingleResultByFieldValues(
							  entityManager, "codeConfirmationParId", 
							  new String[] {"id"}, 
							  new String[] {id},  
		       "graph.user.minimum", isFetchGraph, new CodeConfirmation() ) ;
			
			return rtn ;
			
		}
		
}
