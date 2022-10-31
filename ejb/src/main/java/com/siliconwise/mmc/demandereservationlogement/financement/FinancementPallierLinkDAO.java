package com.siliconwise.mmc.demandereservationlogement.financement;

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
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.common.document.DocumentDAOInterface;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author ALzouma Moussa Mahaamadou
 * 
 *
 */
@Stateless
public class FinancementPallierLinkDAO implements Serializable , FinancementPallierLinkDAOInterface{

	
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager ;
	
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	@Resource 
	private EJBContext ejbContext;
	
	
	public boolean  valider(
					  FinancementPallierLink entity, 
					  boolean mustUpdateExistingNew,
			          String namedGraph,
			          boolean isFetchGraph,
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
			
			
			EntityUtil<FinancementPallierLink> entityUtil = new EntityUtil<FinancementPallierLink>(FinancementPallierLink.class) ;
			
			//ProduitLogementDocument vers document
			
			Map<String,String>  msgVarMap = entity.getFinancementPallierComptantSurSituation() == null 
							 	|| entity.getFinancementPallierComptantSurSituation().getMsgVarMap() == null
								?  new HashMap<String,String>() 
								:entity.getFinancementPallierComptantSurSituation().getMsgVarMap() ;
						
			boolean isAttached = entityUtil.attachLinkedEntity(entityManager, entity, 
					entity.getFinancementPallierComptantSurSituation(), 
					entity.getClass().getDeclaredMethod("setFinancementPallierComptantSurSituation", FinancementPallierComptantSurSituation.class), 
					null,true, locale, AppMessageKeys.CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE, 
					msgVarMap, msgList) ;
			
			
			if (!isAttached) return false ;
		
			
			
			// ProduitLogementDocument vers Produit logement
			
			msgVarMap = entity.getPallierComptantSurSituation() == null 
					 	|| entity.getPallierComptantSurSituation().getMsgVarMap() == null
					     ?  new HashMap<String,String>() 
					     : entity.getPallierComptantSurSituation().getMsgVarMap() ;
								   
			isAttached = entityUtil.attachLinkedEntity(entityManager, entity, 
							entity.getPallierComptantSurSituation(), 
							entity.getClass().getDeclaredMethod("setProduitLogement", PallierComptantSurSituation.class), 
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
		Set<ConstraintViolation<FinancementPallierLink>> constraintViolationList = validator.validate(entity) ;
		
		for (@SuppressWarnings("unused") ConstraintViolation<FinancementPallierLink> violation : constraintViolationList) {
			
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
	
	
	public FinancementPallierLink validerEtEnregistrer(
						FinancementPallierLink entity,
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
		
		
		boolean estValide = valider(entity, mustUpdateExistingNew, namedGraph, isFetchGraph,
								locale, msgList) ;
	
		
		if (!estValide) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return null ; 
		}
	
		
		//Enregistrement des MetaData
		
		FinancementPallierLink rtn = EntityUtil.persistOrMerge(
				entityManager, FinancementPallierLink.class, entity, 
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
	
}
	
