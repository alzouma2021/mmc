package com.siliconwise.mmc.simulationfinancementimmobilier;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import com.siliconwise.mmc.logement.Logement;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author ALzouma Moussa Mahaamadou
 * 
 *
 */
@Stateless
public class SimulationFinancementImmobilierDAO implements Serializable , SimulationFinancementImmobilierDAOInterface{

	
	
	
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager ;
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	@Resource 
	private EJBContext ejbContext;
	
	
	
	public boolean  valider(SimulationFinancementImmobilier entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale, 
			List<NonLocalizedStatusMessage> msgList) {
		
	
		
		logger.info("_62  debut de la methode valider = "+entity.toString());  //TODO A effacer
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
			
			
			EntityUtil<SimulationFinancementImmobilier> entityUtil = new EntityUtil<SimulationFinancementImmobilier>(SimulationFinancementImmobilier.class) ;
			
			//Simulation financement immobilier vers user
			
			Map<String,String>  msgVarMap = entity.getUser() == null 
							 || entity.getUser().getMsgVarMap() == null
										   ?  new HashMap<String,String>() :entity.getUser().getMsgVarMap() ;
						
			boolean isAttached = entityUtil.attachLinkedEntity(entityManager, entity, 
					entity.getUser(), 
					entity.getClass().getDeclaredMethod("setUser", User.class), 
					null,true, locale, AppMessageKeys.CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE, 
					msgVarMap, msgList) ;
		
			logger.info("_95 Attachement de l'entite user="+isAttached); //TOOD A effacer
			
			if (!isAttached) return false ;
			
			msgVarMap = entity.getLogement() == null 
					     || entity.getLogement().getMsgVarMap() == null
						 ?  new HashMap<String,String>() 
						 :entity.getLogement().getMsgVarMap() ;
				
						 
		    isAttached = entityUtil.attachLinkedEntity(entityManager, entity, 
					entity.getLogement(), 
					entity.getClass().getDeclaredMethod("setLogement", Logement.class), 
					null,true, locale, AppMessageKeys.CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE, 
					msgVarMap, msgList) ;
		
			logger.info("_95 Attachement de l'entite user="+isAttached); //TOOD A effacer
			
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
		
		
		logger.info("_116  Debut de validation jpa");//TODO A effacer
		
		//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		Set<ConstraintViolation<SimulationFinancementImmobilier>> constraintViolationList = validator.validate(entity) ;
		
		for (@SuppressWarnings("unused") ConstraintViolation<SimulationFinancementImmobilier> violation : constraintViolationList) {
			
			logger.info("_125 Contrainte de violation="+violation); //TODO A effacer
			
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
		}
		
		
		logger.info("_133  Debut de validation jpa="+constraintViolationList.isEmpty());//TODO A effacer
		
		if (!constraintViolationList.isEmpty()) return false ;
		
		logger.info("_137 Fin de validation de la methode ");  //TODO A effacer
		
		//TODO verification d'integrite complexes le cas echeant
		
		return true ;
		
	}
	
	
	public SimulationFinancementImmobilier validerEtEnregistrer(
			SimulationFinancementImmobilier entity,
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
		
		
		logger.info("_164 Fin de la methode valider = "+estValide); //TODO A effacer
		

		if (!estValide) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return null ; 
		}
	
		
		//Enregistrement des MetaData
		
		logger.info("_177 Debut de la methode persistOrMerge = "); //TODO A effacer
		
		SimulationFinancementImmobilier rtn = EntityUtil.persistOrMerge(
				entityManager, SimulationFinancementImmobilier.class, entity, 
				namedGraph, isFetchGraph, 
				AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
				locale, msgList);
		
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		}
		
		logger.info("_195 fin de la methode persistOrMerge = "+rtn.toString()); //TODO A efface
	
		return rtn ;
		
	}

	
		
}

