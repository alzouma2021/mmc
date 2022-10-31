package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

@Stateless
public class SessionCtrl implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject private SessionService sessionService ;
	
	@Inject private TokenService tokenService ;
	
	@Inject Event<HistoryEventPayload<User>> historyEvent ;
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName());

	/** Create or update a session bag when a user is created
	 * If user is null, then an anounymous session will be created
	 * @param sessionBag
	 * @param user
	 * @param defaultLocale locale to use is sessionBag.locale is not defined
	 */
	public SessionBag createOrUpdateSession(SessionBag sessionBag, User user, Locale defaultLocale) {

		 return sessionService.createOrUpdateSession(sessionBag, user, defaultLocale) ;
	}

	/** Extract session id from token or sid key in url headers
	 * @param servletRequest
	 * @param msgList
	 * @return
	 */
	public SessionBag extractSession(HttpServletRequest servletRequest, 
			Locale locale, List<NonLocalizedStatusMessage> msgList) {
		
		return sessionService.extractSession(servletRequest, locale,  msgList) ;
	}
	
	/** login a user and return the new session token and null if authentication fails
	 * @param loginToken
	 * @param sessionBag
	 * @param msgList
	 * @return
	 * @throws NoSuchAlgorithmException error calculating paswword hash
	 */
	public String login(LoginToken loginToken, SessionBag sessionBag, 
			List<NonLocalizedStatusMessage> msgList) throws NoSuchAlgorithmException  {
		
		
		// authenticate 
		
		logger.info("_65 login :: debut de login"); //TODO A effacer
		
		User user = sessionService.authenticate(loginToken, sessionBag, 
				/*"graph.user.with.operator-id"*/null, true, msgList) ;
	
		
		if (user == null) return null ;
		
		// Create new session bag or update existing
		
		logger.info("_76 login :: Creation de la session de l'utilisateur"); //TODO A effacer
		
		sessionBag = sessionService.createOrUpdateSession(sessionBag, user, null) ;
		
		logger.info("_80 login :: Fin de creation de la session="+sessionBag.toString()); //TODO A effacer
		
		// build token 
		
		logger.info("_54 login :: Construction du token"); //TODO A effacer
		
		String rtn = tokenService.buildToken(sessionBag, msgList) ;
		
		logger.info("_88 login ::  Fin de construction du token "); //TODO A effacer
		
		
		//Lever l'historisation
		
		String loggedInUserId = user.getId() != null 
                ? user.getId() 
                : null ;

		String loggedInUserFullname = user.getFullname() != null 
		                ? user.getFullname() 
		                : null ;
		                
		String   observation  = null ;
		
		HistoryEventType historyEventType = HistoryEventType.CONNEXION ;
		
		HistoryEventUtil.fireHistoryEvent(historyEvent, historyEventType, user,
				      loggedInUserId, loggedInUserFullname, observation, sessionBag.getLocale()) ;
		
		
		//Retourne resultat
		
		return rtn ;
		
	}
	
	public boolean updateSessionLocale(String sessionId, Locale locale,
			List<NonLocalizedStatusMessage> msgList) {
		
		return sessionService.updateSessionLocale(sessionId, locale, msgList) ;
	}
	
	/** log a user out
	 * @param sessionBag
	 * @param msgList
	 * @return
	 */
	public boolean logout(SessionBag sessionBag, 
			List<NonLocalizedStatusMessage> msgList) {
	
		
		return sessionService.logout(sessionBag, msgList) ;
		
	}

}
