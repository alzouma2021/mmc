
package com.siliconwise.mmc.oldSecurity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.config.data.DataConfigKeys;
import com.siliconwise.common.config.data.Parameter;
import com.siliconwise.common.config.data.ParameterDAO;
import com.siliconwise.mmc.common.entity.EntityInitializer;
import com.siliconwise.mmc.common.entity.EntityInitializerQualifier;
import com.siliconwise.mmc.common.entity.EntityInitializerQualifier.TargetEntityEnum;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.common.AppUtil;


// @Named
@Stateless
public class SessionDAO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	@Inject private ParameterDAO parameterDAO ;
	//@Inject private IntermediaireFinancierDAO ifDAO ;

	@Inject private  SecurityInitializer securityInitializer ;

	@PersistenceContext
	private EntityManager entityManager ;
	
	@Inject private TokenManagement tokenManagement ;
	
	@EntityInitializerQualifier(value=TargetEntityEnum.ALL) 
	private EntityInitializer entityInitializer ;

	@Resource private EJBContext ejbContext;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	/**
	 * 
	 * @param  String id de l'utilisateur
	 * @return User user Object
	 */
	public SessionBag getSessionByToken(String token, List<NonLocalizedStatusMessage> msgList){
		
		SessionBag rtn = null ;
		
		try {
			rtn = tokenManagement.validateToken(token) ;
		 }
		catch (NoResultException e) {
			
			 String msg = "Session Inexistant" ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR ,msg)) ;
			 return rtn ;
		}
		return rtn ;
		
	}
	
	public SessionBag save(
			SessionBag entity, List<NonLocalizedStatusMessage> msgList){
		
		Locale langue = new Locale(entity.getLanguage() != null && !entity.getLanguage().isEmpty()
									? entity.getLanguage() : AppUtil.APP_DEFAULT_LANGUAGE) ;
		SessionBag rtn = null ;
		
		
		try {
			 rtn = (SessionBag) EntityUtil.persistOrMerge(entityManager, SessionBag.class, entity) ;
		}  
		catch (Exception ex) {
			
			// une erreur c'est produite pendant l'enregistremement des info perso. Recommencer plutard
			
			String translatedMessage = MessageTranslationUtil.translate(langue,
					SessionBag.CODE_TRADUCTION_PERISTENCE_ERREUR,// venant du fichier
					SessionBag.CODE_TRADUCTION_PERISTENCE_ERREUR, // Message par defaut
					new String[]{});

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));

			logger.error(getClass().getName() + "_106 " + translatedMessage + " " + ex.getMessage());
			return null;
		}
		
		return rtn ;
	}

	public Boolean validate(SessionBag entity, List<NonLocalizedStatusMessage> msgList)  {
	
		if (entity == null) {
			
			String msg = SessionBag.SESSION_INVALIDE ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			return false ;
		}
		
		Locale langue = SessionUtil.getLocale(entity);
		
		try {
			// initialiser le spropriétés automatiques	
			entityInitializer.initializeBeforeSaving(SessionBag.class, entity) ;
		} 
		catch (Exception ex) {
		
			String translatedMessage  = MessageTranslationUtil.translate(langue,
				SessionBag.CODE_TRADUCTION_BEFORE_SAVING_ERREUR ,// venant du fichier
				SessionBag.CODE_TRADUCTION_BEFORE_SAVING_ERREUR, // Message par defaut
				new String[] {});

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
		
			// Logger
			logger.error(getClass().getName() + "_222 Erreur initialisation entité avant enregistrement "+ ex.getMessage());
		
			return false ;
		}
		
		//logger.info(getClass().getName()+"::validate auterudernieremodif="+entity.getAuteurDerniereModification());
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		Set<ConstraintViolation<SessionBag>> constraintViolationList = validator.validate(entity) ;
		
		for (ConstraintViolation<SessionBag> violation : constraintViolationList) {
			
			String translatedMessage = MessageTranslationUtil.translate(langue ,
					violation.getMessage(), violation.getMessage(), 
						/*(entity).getMessageVarList()*/ new String[] {});
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
		}
				
		if (!constraintViolationList.isEmpty()) return false ;
		
		return true ;
	}

	public SessionBag validateAndSave(
			SessionBag entity, List<NonLocalizedStatusMessage> msgList) {
	
		boolean estValide = validate(entity, msgList) ;
		
		if (!estValide) {
			
			ejbContext.setRollbackOnly() ; return null ; 
		}
	
		SessionBag rtn = save(entity, msgList) ;
		
		entityManager.flush(); 
		
		return rtn ;
	}
	
	public void logOut(SessionBag session) {

		if(session == null) return ;
		
		entityManager.remove(session);
		
		entityManager.flush(); 
		
	}

	// private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	
	/**
	 * 
	 * @param  String id de l'utilateur
	 * @return User user Object
	 */
	@SuppressWarnings("static-access")
	public SessionBag findSessionBagById(String id){
		
		if (id == null || id.isEmpty()) return null ;
		
		SessionBag rtn = (new EntityUtil<SessionBag>()).findEntityById(entityManager, 
					id, null, true, SessionBag.class) ;
		
		return rtn ;
	}
	
	/** Extract session id from token or sid key in url headers
	 * @param servletRequest
	 * @param msgList
	 * @return
	 */
	public SessionBag getSession(HttpServletRequest servletRequest, List<NonLocalizedStatusMessage> msgList) {
		
		if (servletRequest == null) return null ;
		
		String token = tokenManagement.extractToken(servletRequest) ;
		
		if (token != null && !token.isEmpty()) {
			
			SessionBag sessionBag = getSessionByToken(token, msgList);
			return sessionBag ;
		}
		
		// Récupérer le sid dans les entêtes 
		String sessionId = servletRequest.getHeader("sid") ;
		
		SessionBag sessionBag = findSessionBagById(sessionId) ;
		
		return sessionBag ;
	}


	@SuppressWarnings("unused")
	public SessionBag createAnonymousSession(String hostName, 
			List<NonLocalizedStatusMessage> msgList) {
		
		String sessionId = UUID.randomUUID().toString() ;
		
		SessionBag session = new SessionBag() ;
		
		session.setId(sessionId);
		session.setLanguage(AppUtil.APP_DEFAULT_LANGUAGE);
		session.setDateHeureCreation(LocalDateTime.now());
		
		long sessionLifeTime = getSessionExpirationDelay() ;
		LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(sessionLifeTime) ;	
		session.setDateHeureExpiration(expirationDateTime);
		
		// trouver l'IF
		String interFinancierId = null; 
		//ifDAO.trouverIfIdparUrlHote(hostName) ;
		
		if (interFinancierId == null) { // non trouve 
			
			String translatedMessage  = MessageTranslationUtil.translate(
					SessionUtil.getLocale(null),
					null, //IntermediaireFinancier.CODE_TRADUCTION_IF_WITH_URL_NOT_FOUBD ,
					null, //IntermediaireFinancier.CODE_TRADUCTION_IF_WITH_URL_NOT_FOUBD, 
					new String[] {});
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			return null ;
		}
		
		session.setIntermediaiareFinancierId(interFinancierId);
		
		SessionBag savedSession = validateAndSave(session, msgList) ;
		
		if (savedSession == null) {
			
			String translatedMessage  = MessageTranslationUtil.translate(
					SessionUtil.getLocale(null),
					SessionBag.CODE_TRADUCTION_SAVE_ERROR ,
					SessionBag.CODE_TRADUCTION_SAVE_ERROR, 
					new String[] {});
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			return null ;
		}
		
		Map<String, SessionBag> sessionMap = securityInitializer.getActiveSessionMap() ;
		
		if (sessionMap != null && !sessionMap.containsKey(savedSession.getId()))
			sessionMap.put(savedSession.getId(), savedSession) ;
		
		return session;
	}

	public long nbrActiveSessionByUserId(String userId, LocalDateTime dateHeureExpiraton) {
			
		// pas de test de valeur null pour userId 

		if (dateHeureExpiraton == null) dateHeureExpiraton = LocalDateTime.now() ;
		
		Long rtn = (Long) entityManager.createNamedQuery("nbrActiveSessionByUserId")
		  			.setParameter("userId", userId)
		  			.setParameter("dateHeureExpiraton", dateHeureExpiraton)
		  			.getSingleResult();
			
		return rtn ;
	}
	
	/** Get session expiration delay in minutes
	 * @return
	 */
	public long getSessionExpirationDelay() {
		
		Long rtn = null ; 
		
		try {
			@SuppressWarnings("unchecked")
			Parameter<Long> parameter = (Parameter<Long>)parameterDAO.findParameterByDesignation(
					false, DataConfigKeys.GLOBAL_CONFIG_DATA_KEY_SESSION_EXPIRATION_DELAY) ;
			
			rtn = parameter !=  null ? parameter.getValue() : null  ;
		}
		catch (Exception e) {
			rtn = null ;
		}
		
		return rtn != null ? rtn : DataConfigKeys.GLOBAL_CONFIG_DATA_DEFAULT_SESSION_EXPIRATION_DELAY  ;
	}
	
	/** Get maximum allowable number of sessions per user
	 * @return
	 */
	public long getSessionMaximumNbrePerUser() {
		
		Long rtn = null ; 
		
		try {
			@SuppressWarnings("unchecked")
			Parameter<Long> parameter = (Parameter<Long>)parameterDAO.findParameterByDesignation(
					false, DataConfigKeys.GLOBAL_CONFIG_DATA_KEY_SESSION_MAX_NUMBER_PER_USER) ;
			
			rtn = parameter !=  null ? parameter.getValue() : null  ;
		}
		catch (Exception e) {
			rtn = null ;
		}
		
		return rtn != null ? rtn : DataConfigKeys.GLOBAL_CONFIG_DATA_DEFAULT_SESSION_MAX_NUMBER_PER_USER  ;
	}

}
