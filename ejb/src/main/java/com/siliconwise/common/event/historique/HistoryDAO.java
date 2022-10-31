package com.siliconwise.common.event.historique;

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
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.beanvalidation.LoggerUtil;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;

@Stateless
public class HistoryDAO implements Serializable , HistoryDAOInterface {

	
	
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Resource private EJBContext ejbContext;

	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;

	// ============== Persistence ========================
		
	public boolean  valider(History entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale,
			List<NonLocalizedStatusMessage> msgList) {
		
		
		// verifiy that version is defined if entity id si not null
		
		if (entity.getId() != null && entity.getVersion() == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
		}
		
		// verification d'integrite complexes le cas echeant
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		Set<ConstraintViolation<History>> constraintViolationList = validator.validate(entity) ;
		
		
		for (@SuppressWarnings("unused") ConstraintViolation<History> violation : constraintViolationList) {
			
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
		}
		
				
		if (!constraintViolationList.isEmpty()) return false ;
		
		return true;
		
	}
	
	public History validerEtEnregistrer(
			History entity,
			boolean mustUpdateExistingNew,
			String namedGraph,
			boolean isFetchGraph, 
			Locale locale,
			List<NonLocalizedStatusMessage> msgList){
	
		
		logger.info("_99 Debut validerEtEnregistrer"); //TODO A effacer
		
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
		
		logger.info("_117 Appel de la methode valider"); //TODO A effacer
		
		boolean estValide = valider(entity, mustUpdateExistingNew,
				             namedGraph, isFetchGraph,locale, msgList) ;
		
		logger.info("_122 Fin Appel de la methode valider ="+estValide ); //TODO A effacer
		
		
		if (!estValide) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return null ; 
			
		}
		
		//Methode de persistence de l'historisation
		
		History rtn = EntityUtil.persistOrMerge(
				entityManager, History.class, entity, 
				namedGraph, isFetchGraph, 
				AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE,entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR , entity.getMsgVarMap(), 
				locale, msgList);
		
		logger.info("_145 Fin de persistence ="+rtn); //TODO A effacer
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		}
		
		logger.info("_151 Persistence debut d'historisation="+rtn.toString()); //TODO A effacer
		
		return rtn ;
		
	}

	
	// ============== Envent management =============
	
	// Creation des evenement
	//Event<HistoryEventPayload<IEntityStringkey>> historiserEvent ; // evenement d'historisation
	
	
	/** effectively historize an event
	 * @param payload données de l'evenement
	 * @param callback classe clallback de propagation de l'evenement
	 */
	//@SuppressWarnings("cdi-observer") @Observes(notifyObserver = Reception.IF_EXISTS)
	public  <T extends IEntityStringkey> void historize(
			@ObservesAsync  HistoryEventPayload<T> payload) {
		
		
		logger.info("_158 debut d'historisation"); //TODO A effacer
		
		if (payload == null) return ;
		
		// creer un instance d'hitorique
		
		History history = new History() ;
		
		HistoryEventType eventType = payload.getEventType() ;
		
		history.setEventType(eventType);

		history.setUserId(payload.getUserId());
		
		history.setDateEtHeureEvenement(payload.getDateEtHeureEvenement());
		
		history.setObservation(payload.getObservation());
		
		history.setUserName(payload.getUserName());
		
		IEntityStringkey entity = payload.getEntity() ;
		
		if (entity != null) {
			
			history.setClassAbsuluteNAme(entity.getClass().getName());
			
			history.setEntityId(entity.getId());
		}
		
		HistoryEventCallback callback = payload.getCallback() ;

		//enregistrer l'instance d'historique
		
		Locale locale = payload.getLocale() ;
		//if (locale == null) locale = SessionUtil.getLocale(null)  ;
				
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		
		History savedHistorique = null ;
		
		logger.info("_195 Appel à la methode validerEtEnregistrer pour historiser="+history.toString());//TODO A effacer
		
		try {
			
			savedHistorique = validerEtEnregistrer(history, true,null, true, 
						locale, msgList) ;
			
			logger.info("_202 fin d'historisation"); //TODO A effacer
			
		}
		catch (Exception ex) {
			
			// set that there is an error
			savedHistorique = null ;
			
			// log exception
			LoggerUtil.log(StatusMessageType.ERROR, logger, ex, " _187 historize ", null, 
					(Map<String, String>)new HashMap<String, String>());
			
		}
		
		if (savedHistorique == null) { // an error has occured
			
			String msg  = MessageTranslationUtil.translate(locale ,
					History.TRANSLATION_MESSAGE_ENTITY_PERSISTENCE_UNKNOWN_ERROR,// venant du fichier
					History.TRANSLATION_MESSAGE_ENTITY_PERSISTENCE_UNKNOWN_ERROR, // Message par defaut
					history.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
			
			if (callback != null) callback.onFailure(payload, msgList);
			
			return ;
		}
		
		
		//appeler le callback pour propager les résultats de l'evenement
		
		if (callback != null) callback.onSucess(msgList);
		
	}
	
	
}
