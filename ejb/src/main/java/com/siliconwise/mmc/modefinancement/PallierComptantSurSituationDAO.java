package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
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

import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class PallierComptantSurSituationDAO implements Serializable, PallierComptantSurSituationDAOInterface{


	private static final long serialVersionUID = 1L;
	

	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;

	@Override
	public boolean valider(PallierComptantSurSituation entity,
							boolean mustUpdateExistingNew, String namedGraph,
							boolean isFetchGraph, Locale locale, 
							List<NonLocalizedStatusMessage> msgList) {
		
		
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
				try {
					
					
					EntityUtil<PallierComptantSurSituation> entityUtil = new EntityUtil<PallierComptantSurSituation>(PallierComptantSurSituation.class) ;
					
					
				    //ManyToOne PallierComptantSurSituation To Reference
					 //Verifie si le Efi est non nul 
					 Map<String,String> msgVarMap = entity.getPallier() ==  null 
														|| entity.getPallier().getMsgVarMap() == null
												   ?  new HashMap<String,String>() : entity.getPallier().getMsgVarMap() ;
					
					boolean  isAttached = entityUtil.attachLinkedEntity(entityManager, 
						    entity, entity.getPallier(), 
						    entity.getClass().getMethod("setPallier", Reference.class), null, true, 
							locale, AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, msgVarMap, msgList);
					
					if (!isAttached) return false ;
					
					
				} 
				catch(Exception ex) {
					
					
					String msg  = MessageTranslationUtil.translate(locale ,
							AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
							AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
							entity.getMsgVarMap()) ;
					
			        msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
					
			
					//logger.error("_1 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
					ex.printStackTrace();
					
					return false ;
				}
				
				//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
				
				Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
				Set<ConstraintViolation<PallierComptantSurSituation>> constraintViolationList = validator.validate(entity) ;
				
				for ( ConstraintViolation<PallierComptantSurSituation> violation : constraintViolationList) {
					
					
					String translatedMessage = MessageTranslationUtil.translate(locale ,
							AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
							AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
							entity.getMsgVarMap()) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
					
				}
						
				if (!constraintViolationList.isEmpty()) return false ;
				
				// verification d'integrite complexes(règles metiers) 
				
				return true;
				
	}

	@Override
	public PallierComptantSurSituation validerEtEnregistrer(
			                            PallierComptantSurSituation entity,
										boolean mustUpdateExistingNew,
										String namedGraph, 
										boolean isFetchGraph,
										Locale locale, 
										User loggedInUser,
										List<NonLocalizedStatusMessage> msgList) {
		

		//Locale langue = SessionUtil.getLocale(currentSession) ;
		
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
		
		PallierComptantSurSituation rtn = EntityUtil.persistOrMerge(
				entityManager, PallierComptantSurSituation.class, entity, 
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
