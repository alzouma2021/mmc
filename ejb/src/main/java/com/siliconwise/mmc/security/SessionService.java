/**
 * 
 */
package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.siliconwise.common.beanvalidation.LoggerUtil;
import com.siliconwise.common.config.data.DataConfigKeys;
import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.common.locale.LocaleUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.User;
import com.siliconwise.mmc.user.UserDAOInterface;

/**
 * @author sysadmin
 *
 */
@Stateless
public class SessionService implements Serializable {

	private static final long serialVersionUID = 1L;

	// system session bags list
	@LoadSessionBagList private List<SessionBag> sessionBagList = new ArrayList<SessionBag>();
	
	//@Inject ConfigParameterValueDAO configParameterValueDAO ;
	
	@Inject UserDAOInterface userDAO ;
	
	@Inject TokenService tokenService ;

	@PersistenceContext private EntityManager entityManager ;
	
	@Inject Event<HistoryEventPayload<User>> historyEvent ;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	/** Extract session id from token or sid key in url headers
	 * @param servletRequest
	 * @param msgList
	 * @return
	 */
	public SessionBag extractSession(HttpServletRequest servletRequest, 
			Locale locale, List<NonLocalizedStatusMessage> msgList) {
		
		if (servletRequest == null) return null ;
		
		String token = tokenService.extractToken(servletRequest) ;
		
		if (token != null && !token.isEmpty()) {
			
			SessionBag sessionBag = extractSessionBagFromToken(token, locale, msgList);
			
			return sessionBag ;
		}
		
		// Récupérer le sid dans les entêtes 
		String sessionId = servletRequest.getHeader(SecurityConstants.SESSION_TOKEN_FIELD_SESSION_ID) ;
		
		SessionBag sessionBag = SessionUtil.findSessionBagById(sessionId) ;
		
		return sessionBag ;
	}
	
	/*
	 * @param  String id de l'utilisateur
	 * @return User user Object
	 */
	private SessionBag extractSessionBagFromToken(String token, 
			Locale locale, List<NonLocalizedStatusMessage> msgList){
		
		SessionBag rtn = null ;
		
		try {
			rtn = tokenService.validateToken(token, msgList) ;
		 }
		catch (NoResultException ex) {
			
			Map<String,String> msgVarList =  new HashMap<String,String>();
			
			String msg  = MessageTranslationUtil.translate(locale,
				 	SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION,
				 	SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION, 
				 	msgVarList) ;			
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR ,msg)) ;
			 
			 LoggerUtil.log(StatusMessageType.ERROR, logger, ex, 
					 "_104 extractSessionBagFromToken :: ", msg, 
					 (Map<String, String>) new HashMap<String, String>());
			 
			 return null ;
			 
		}
		
		return rtn ;
		
	}

	/** Authenticate email and password and return the authenticated user instance
	 * @param loginToken
	 * @param sessionBag
	 * @param msgList
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public User authenticate(LoginToken loginToken, SessionBag sessionBag, 
			String userNamedGraph, boolean isFetchGraph, 
			List<NonLocalizedStatusMessage> msgList) throws NoSuchAlgorithmException  {
		
		// login credetial not defined 
		
		logger.info("_151 debut authenticate"); //TODO A effacer
		
		if (loginToken == null) { 
			
			Locale locale = SessionUtil.getLocale(sessionBag) ;
			
			String msg  = MessageTranslationUtil.translate(locale ,
					SessionBag.TRANSLATION_MESSAGE_AUTHENTICATION_LOGIN_CREDENTIAL_NOT_DEFINED,
					SessionBag.TRANSLATION_MESSAGE_AUTHENTICATION_LOGIN_CREDENTIAL_NOT_DEFINED,
					sessionBag.getMsgTranslationVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
			
			return null ;
		}
		
		// a user is already logged.
		
		logger.info("_148 Verification si l'utilisateur est deja connecté");//TODO Affacer
			
		if (sessionBag != null && !sessionBag.isExpired() 
				&& sessionBag.getUserId() != null && !sessionBag.getUserId().isEmpty()) {
			
			Locale locale = SessionUtil.getLocale(sessionBag) ;
			
			String msg  = MessageTranslationUtil.translate(locale ,
					SessionBag.TRANSLATION_MESSAGE_AUTHENTICATION_ALREADY_AUTHENTICATED,
					SessionBag.TRANSLATION_MESSAGE_AUTHENTICATION_ALREADY_AUTHENTICATED,
					sessionBag.getMsgTranslationVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 

			return null ;
		}
		
		// check email and password 
	
		logger.info("_166 debut findUserByEmailAndPlainPassword"); //TODO A effacer
		
		User rtn = userDAO.findUserByEmailAndPlainPassword(loginToken.getEmail(), 
				loginToken.getPassword(), userNamedGraph, isFetchGraph) ;
		
		
		return rtn ;
		
	}
	
	/** Create or update a session bag when a user is created
	 * If user is null, then an anounymous session will be created
	 * @param sessionBag
	 * @param user
	 * @param defaultLocale locale to use is sessionBag.locale is not defined
	 */
	public SessionBag createOrUpdateSession(SessionBag sessionBag, User user, Locale defaultLocale) {
		
		
		//logger.info("_186 createOrUpdateSession :: SessionBag="+sessionBag +" User="+user + " Locale="+defaultLocale + " sessionBagList="+sessionBagList); //TODO A effacer
		
		SessionBag rtn =  sessionBag ;
		
		if (rtn == null) {
			
			rtn = new SessionBag() ;
			
			sessionBagList.add(rtn);
			
			sessionBag = rtn ;
			
		}
		
		//TODO A effacer

		//logger.info("_199 createOrUpdateSession ::  Verification du user="+user.getId()); //TODO A effacer
		
		if (user != null) {
			
			logger.info("_203 createOrUpdateSession :: recuperation de l'id du user"); //TODO A effacer
			
			sessionBag.setUserId(user.getId());
			
			logger.info("_207 createOrUpdateSession :: recuperation de l'id du user="+sessionBag.getId()); //TODO A effacer
			
		}
		
		
		logger.info("_207 createOrUpdateSession :: debut getCreationDateTime"); //TODO A effacer
		
		if (sessionBag.getCreationDateTime() == null) 
			sessionBag.setCreationDateTime(OffsetDateTime.now());
		
		logger.info("_212  createOrUpdateSession :: fin getCreationDateTime="+sessionBag.getCreationDateTime());//TODO A effacer
		
		if (user != null || 
				(sessionBag != null && sessionBag.getExpirationDateTime() == null)) {
			
			// reset expiration date if new user
			// or not already defined()
			
			
			//TODO A revoir dans les prochaines iteration
			/*
			ConfigParameterValue<Long> configParameterValue = configParameterValueDAO.findConfigParameterValueByConfigParameterIdAndOperatorId (
					DataConfigKeys.GLOBAL_CONFIG_DATA_KEY_SESSION_EXPIRATION_DELAY, null, 
					null, true) ;
			
			ConfigParameterValue<Long> configParameterValue = null ;
			
			Long tokenLifeTime = configParameterValue != null 
									? (Long) configParameterValue.getValue() 
									: DataConfigKeys.GLOBAL_CONFIG_DATA_DEFAULT_SESSION_EXPIRATION_DELAY ;*/
			
			Long tokenLifeTime = DataConfigKeys.GLOBAL_CONFIG_DATA_DEFAULT_SESSION_EXPIRATION_DELAY ;
			
			sessionBag.setExpirationDateTime(OffsetDateTime.now().plusMinutes(tokenLifeTime));
		}

		// locale 
		
		if (sessionBag.getLocale() == null) {
			
			if (defaultLocale != null) sessionBag.setLocale(defaultLocale);
			else {
				
				sessionBag.setLocale(
					user == null || user.getLocale() == null 
						? LocaleUtil.getDefaultLocale() : user.getLocale());
			}
		}
		
		return rtn ;

	}
	
	public boolean updateSessionLocale(String sessionId, Locale locale,
						List<NonLocalizedStatusMessage> msgList) {
		
		if (sessionId == null || sessionId.isEmpty() || locale == null) 
			return false ;
		
		SessionBag sessionBag = SessionUtil.findSessionBagById(sessionId) ;
		
		if (sessionBag == null) {
			
			String msg = MessageTranslationUtil.translate(locale,
					SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION,
					SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION,
					(Map<String, String>) new HashMap<String, String>());
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));
		
			return false ;
		}
		
		sessionBag.setLocale(locale);
		
		return true ;
	}

	/** log a user out
	 * @param sessionBag
	 * @param msgList
	 * @return
	 */
	public boolean logout(SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) {
		
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		if (sessionBag == null) {
			
			String msg = MessageTranslationUtil.translate(locale,
					SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION,
					SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION,
					(Map<String, String>) new HashMap<String, String>());
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return false ;
		}
		
		
		//Historisation de l'evenement avant deconnexion
		
	    String loggedInUserId = sessionBag.getId() != null 
		                ? sessionBag.getId() 
		                : null ;

	    String loggedInUserFullname = sessionBag.getFullname() != null 
				        ? sessionBag.getFullname() 
				        : null ;
				                
		String   observation  = null ;
				
	    HistoryEventType historyEventType = HistoryEventType.DECONNEXION ;
				
		HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, null,
			            loggedInUserId, loggedInUserFullname, observation, locale) ;
		
		
		sessionBag.setUserId(null); 
		
		return true ;
		
	}

}
