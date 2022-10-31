package com.siliconwise.common.event.oldhistorique;

import java.io.Serializable;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.oldhistorique.Historique.HistoriqueEvent;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionUtil;


public  class HistoriqueDAO implements Serializable  {


	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Resource 
	private EJBContext ejbContext;

	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;

	// ============== Persistence ========================
	

	public boolean  valider(
			Historique entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale,
			List<NonLocalizedStatusMessage> msgList) {
		
		
		logger.info("_52 debut de verification d'historisation="+entity.toString()); //TODO A effacer
		
		// verifiy that version is defined if entity id si not null
		
		logger.info("_56 Verification  version"); //TODO
		
		if (entity.getId() != null && entity.getVersion() == null) {
			
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					new String[] {}) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
		}
		
		// verification d'integrite complexes le cas echeant
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		
		Set<ConstraintViolation<Historique>> constraintViolationList = validator.validate(entity) ;
		
		for (@SuppressWarnings("unused") ConstraintViolation<Historique> violation : constraintViolationList) {
			
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
		}
				
		if (!constraintViolationList.isEmpty()) return false ;
		
		return true;
		
	}
	

	public Historique validerEtEnregistrer(
			Historique entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,
			List<NonLocalizedStatusMessage> msgList){

		
		//Locale langue = SessionUtil.getLocale(currentSession) ;
		
		logger.info("_97 debut de validerEtEnregistrer"); //TDO Affacer
		
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
		
		//Methode de persistence de l'historisation
		
		Historique rtn = EntityUtil.persistOrMerge(
				entityManager, Historique.class, entity, 
				namedGraph, isFetchGraph, 
				AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE,entity.getMsgVarMap(), 
				AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR , entity.getMsgVarMap(), 
				locale, msgList);
		
		if (rtn == null) {
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			return null ;
		}
		
		
		return rtn ;
		
	}

	
	// ============== Gestion des evenements =============
	
	// Creation des evenement
	//Event<HistoriserEventPayload<IEntityStringkey>> historiserEvent ; // evenement d'historisation
	
	
	/** historiser l'action
	 * @param payload données de l'evenement
	 * @param callback classe clallback de propagation de l'evenement
	 */
	public void historiser(
			@Observes HistoriserEventPayload<IEntityStringkey> payload,
			HistoriqueEventCallback callback) {
		
		if (payload == null) return ;
		
		// creer un instance d'hitorique
		Historique historique = new Historique() ;
	
		
		HistoriqueEvent event = payload.getEvent() ;
		historique.setEvent(event);

		historique.setUserId(payload.getUserId());
		historique.setUserName(payload.getUserName());
		
		IEntityStringkey entity = payload.getEntity() ;
		
		if (entity != null) {
			
			historique.setClassAbsuluteNAme(entity.getClass().getName());
			historique.setEntityId(entity.getId());
		}
		
		//enregistrer l'instance d'historique
		
		Locale locale = payload.getLocale() ;
		if (locale == null) locale = SessionUtil.getLocale(null)  ;
		
				
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		Historique savedHistorique = null ;
		
		try {
			savedHistorique = validerEtEnregistrer(historique, true,null, true, 
						locale, msgList) ;
		}
		catch (Exception ex) {
			
			// TODO A revoir pour la revue
			String msg = "Une erreur s'est produitre pendant l'historisation "
					+ (event != null 
							? " Evenement:" 
									+ MessageTranslationUtil.translate(locale, 
									AppMessageKeys.CODE_TRADUCTION_HISTORISATION_ERREUR, 
									AppMessageKeys.CODE_TRADUCTION_HISTORISATION_ERREUR,
									savedHistorique.getMsgVarMap()) 
							: "") ;
			
			logger.error(msg + " historiser line 234 "
					+ "payload: " + payload) ;
			
			if (callback != null) callback.onFailure(payload, msgList);
			
			return ;
		}
		
		//appeler le callback pour propager les résultats de l'evenement
		
		if (callback != null) {
			
			if (savedHistorique != null) callback.onSucess(msgList);
			else callback.onFailure(payload, msgList);
		}
		
		
	}
	
	
}
