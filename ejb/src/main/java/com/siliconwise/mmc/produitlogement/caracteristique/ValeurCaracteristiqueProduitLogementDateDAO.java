package com.siliconwise.mmc.produitlogement.caracteristique;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
public class ValeurCaracteristiqueProduitLogementDateDAO implements Serializable , ValeurCaracteristiqueProduitLogementDateDAOInterface  {

	

	private static final long serialVersionUID = 1L;

	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	
	
	
	@Override
	public boolean valider(ValeurCaracteristiqueProduitLogementDate entity, boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, Locale locale, List<NonLocalizedStatusMessage> msgList) {
		
		
		if(entity == null || entity.getValeur() == null) return false ;
		
		//Verification de la valeur 
		
			
			//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
			
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
			Set<ConstraintViolation<CaracteristiqueProduitLogement>> constraintViolationList = validator.validate(entity) ;
			
			for ( ConstraintViolation<CaracteristiqueProduitLogement> violation : constraintViolationList) {
				
				
				String translatedMessage = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
				
			}
					
			if (!constraintViolationList.isEmpty()) return false ;
	
			return true;
	
	}


	@Override
	public ValeurCaracteristiqueProduitLogementDate validerEtEnregistrer(
			ValeurCaracteristiqueProduitLogementDate entity, boolean mustUpdateExistingNew, String namedGraph,
			boolean isFetchGraph, Locale locale, User loggedInUser, List<NonLocalizedStatusMessage> msgList) {
		
		
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
		
		ValeurCaracteristiqueProduitLogementDate rtn = EntityUtil.persistOrMerge(
				entityManager, ValeurCaracteristiqueProduitLogementDate.class, entity, 
				namedGraph, isFetchGraph, 
				AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
				locale, msgList);
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		}
		
		 return rtn;
	
	}

}
