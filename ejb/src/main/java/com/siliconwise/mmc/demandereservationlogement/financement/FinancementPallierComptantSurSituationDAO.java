package com.siliconwise.mmc.demandereservationlogement.financement;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.event.oldhistorique.HistoriserEventPayload;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class FinancementPallierComptantSurSituationDAO implements FinancementPallierComptantSurSituationDAOInterface{


	
	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
			
	private static transient Logger logger = LoggerFactory.getLogger(FinancementPallierComptantSurSituationDAO.class) ;
	
	@Override
	public boolean valider(FinancementPallierComptantSurSituation entity, 
					boolean mustUpdateExistingNew, String namedGraph,
					boolean isFetchGraph, Locale locale, 
					List<NonLocalizedStatusMessage> msgList) {
		

		
		
		// association
		
		try {
			
			
			EntityUtil<FinancementPallierComptantSurSituation> entityUtil = new EntityUtil<FinancementPallierComptantSurSituation>(FinancementPallierComptantSurSituation.class) ;
	
			
			// ProduitLogementDocument vers Produit logement
			
			//Map<String,String> msgVarMap =  new HashMap<String,String>() ;
			
			String getMsgVarMapMethodName = "getMsgVarMap" ;
								   
			boolean isAttached = entityUtil.attachLinkedEntityList(entityManager, entity, 
							entity.getPallierComptantSurSituationList(), 
							entity.getClass().getDeclaredMethod("setPallierComptantSurSituationList", Set.class), 
							null, true,  locale, AppMessageKeys.CODE_TRADUCTION_FORMAT_NOT_FOUND, 
				getMsgVarMapMethodName, msgList, PallierComptantSurSituation.class);
		
			
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
				
		Set<ConstraintViolation<FinancementPallierComptantSurSituation>> constraintViolationList = validator.validate(entity) ;
				
		for ( ConstraintViolation<FinancementPallierComptantSurSituation> violation : constraintViolationList) {
					
					
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
						entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
					
		}
				
						
		if (!constraintViolationList.isEmpty()) return false ;
				
		return true;
		
		// verification d'integrite complexes(règles metiers) 
		
	}

	
	
	@Override
	public FinancementPallierComptantSurSituation validerEtEnregistrer(
			FinancementPallierComptantSurSituation entity,
					boolean mustUpdateExistingNew, String namedGraph, 
					boolean isFetchGraph, Locale locale, User loggedInUser,
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
		

		boolean estValide = valider(entity, mustUpdateExistingNew, namedGraph, isFetchGraph,
								locale, msgList) ;
		
		
		if (!estValide) {
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			return null ; 
		}
		
		
		//Persistence EchenaceFinancementTemperament
		
		FinancementPallierComptantSurSituation rtn = EntityUtil.persistOrMerge(
				entityManager, FinancementPallierComptantSurSituation.class, entity, 
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
