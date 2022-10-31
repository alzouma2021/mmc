package com.siliconwise.mmc.oldSecurity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.security.SecurityService;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.user.User;
import com.siliconwise.mmc.user.UserDAO;

/**
 * Session Bean implementation class SecurityCtrl
 */

@Stateless
public class SecurityCtrl implements Serializable {
   
	private static final long serialVersionUID = 1L;

	public SecurityCtrl() {}
	
	@Inject
	private SecurityService securityService ;
	
	//@Inject private UserDAO userDAO ;
	//@Inject private RoleDAO roleDAO ;
	@Inject private SessionDAO sessionDAO ;
	
	//@Inject private IntermediaireFinancierDAO ifDAO ;
	
	@Inject private TokenManagement tokenManagement ;
	@Inject private  SecurityInitializer securityInitializer ;
	
	@Resource private EJBContext ejbContext;
	
	@PersistenceContext private EntityManager entityManager ;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	//@Inject private MessageDAO messageDao ;
	
	/**
	 * Authentifier un utilisateur pour l'accès à la plateforme.
	 * 
	 * @param user Application user
	 * @param adresseIf address or host name
	 * @param msgList
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception
	 */	
	@SuppressWarnings("unused")
	public String  authenticate(LoginPasswordToken credentials, SessionBag sessionBag,
					List<NonLocalizedStatusMessage> msgList) throws NoSuchAlgorithmException  {
		
		logger.info(getClass().getName() + ":: authenticate() :: Entrée  ");
		
		String interFinancierId = null ;
		
		Locale locale = SessionUtil.getLocale(sessionBag) ; 
		
		if (sessionBag != null) {

			//locale = SessionUtil.getLocale(sessionBag) ;
			
			// Refuser l'authentification, si l'utilisateur est dja authentifié
			
			if (!sessionBag.isExpired() 
					&& sessionBag.getUserId() != null && !sessionBag.getUserId().isEmpty()) {
				
				String msg = MessageTranslationUtil.translate(locale,
					 	SessionBag.CODE_TRADUCTION_AUTHENTICATION_FAILIURE_USER_ALREADY_AuTHENTIFIED ,
					 	SessionBag.CODE_TRADUCTION_AUTHENTICATION_FAILIURE_USER_ALREADY_AuTHENTIFIED, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				return null ;
			}
			
			interFinancierId = sessionBag.getIntermediaiareFinancierId() ;
			
			// supprimer id si  texte vide
			interFinancierId = interFinancierId != null && !interFinancierId.isEmpty()
									? interFinancierId : null ;
			
			// Verifier que les urlhote de l'intermediaire financier et celui fourni dans le token
			// sont identiques
			
			// recherche interFin à partir de l'urlHote fourni dans les credentials
			//	String ifId2  = ifDAO.trouverIfIdparUrlHote(credentials.getUrlHote()) ;
				String ifId2  = null ;
				
			if ((ifId2 == null && interFinancierId != null)
					|| (ifId2 != null && !ifId2.equals(interFinancierId))) {	
				
				String msg = MessageTranslationUtil.translate(locale,
					 	SessionBag.CODE_TRADUCTION_IF_LOGIN_URL_NO_MATCH,
					 	SessionBag.CODE_TRADUCTION_IF_LOGIN_URL_NO_MATCH, 
						new String[] {}) ;
				
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				
				return null ;
			}
		}
		else { // SessionBag == null
			
			if (credentials.getUrlHote() != null && !credentials.getUrlHote().isEmpty()) {
				
				interFinancierId = null;
				//interFinancierId = ifDAO.trouverIfIdparUrlHote(credentials.getUrlHote()) ;
				
				if (interFinancierId == null || interFinancierId.isEmpty()) {
						
					String msg = MessageTranslationUtil.translate(locale,
						 	null,//IntermediaireFinancier.CODE_TRADUCTION_IF_WITH_URL_NOT_FOUBD ,// venant du fichier
						 	null,//IntermediaireFinancier.CODE_TRADUCTION_IF_WITH_URL_NOT_FOUBD, // Message par defaut
							new String[] {credentials.getUrlHote()}) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
					try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
					return null ;
				}
			}
		}

		// Rechercher intermediaiare finanacier, si pas de session existante
		// ou si id intermediaire de la session est incorrect
		// Si intermediaire financier non defini --> Utilisateur de laplateforme
		/*
		if (interFinancierId == null ) {
			
			if (urlHote == null || urlHote.isEmpty()) {
				
				String msg = MessageTranslationUnit.dynamicTranslate(locale,
					 	IntermediaireFinancier.CODE_TRADUCTION_URL_NOT_DEFINED ,// venant du fichier
					 	IntermediaireFinancier.CODE_TRADUCTION_URL_NOT_DEFINED, // Message par defaut
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				return null ;
			}			
		} */
				 
		//  1- Authentification de l'utilisateur
		
		/* User validUser = securityService.authentifier(credentials, interFinancierId, locale,
				 				"graph.user.minimum", true, msgList) ;*/
		   User validUser = null;
		
		logger.info(getClass().getName() + ":: authenticate():: isAuthenticated ="+ (validUser != null));
		
		// Récupérere la langue pae defaut de l'App
		//String[] tab = null ;
		
		if(validUser == null) {
			 
			String message = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_UTILISATEUR_AUTHENTIFICATION_ECHEC ,// venant du fichier
					AppMessageKeys.CODE_UTILISATEUR_AUTHENTIFICATION_ECHEC, // Message par defaut
					 	new String[] {}) ;// 

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, message)) ;
				
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			return null ;
		}
	
		// Actualiser la session 
		
		if (sessionBag == null) sessionBag = new SessionBag() ;
		
		sessionBag.setUserId(validUser.getId());
		sessionBag.setIntermediaiareFinancierId(interFinancierId);
		
		if (sessionBag.getDateHeureCreation() == null) sessionBag.setDateHeureCreation(LocalDateTime.now()); 
		
	//	if (((Locale) validUser).getLanguage() == null || validUser.getLanguage().isEmpty()) 
		//	validUser.setLanguage(locale.getLanguage()) ;
			
	//	sessionBag.setLanguage(validUser.getLanguage());
		
		// La session expire apres le delai d'expiration de session
		
		// dure de validite de du JWT
		// Date expirationDate = Date.from(ZonedDateTime.now()
		//		.plusMinutes(AppUtil.SECURITY_LIFE_TOKEN).toInstant());
				
		long tokenLifeTime = sessionDAO.getSessionExpirationDelay() ;
		LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(tokenLifeTime) ;
				
		sessionBag.setDateHeureExpiration(expirationDateTime);
		
		// Enregistrer les droits de l'utilisateur dans la session
		
	//	String roleIds = roleDAO.computeUserRolesAsString(validUser.getId(), locale, msgList) ;
		sessionBag.setRoleIds(null);
		
		// Enregistrer l'instance dans la BD
		
		sessionBag = sessionDAO.validateAndSave(sessionBag, msgList) ;
	
		if (sessionBag == null) {
			
			// erreur pendant l'enregistrement de la session
			// message est deja dans msgList
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			return null ;
		}
		
		//Creation du jeton
		
		String token =  null ;
	//	token = tokenManagement.buildToken(sessionBag, msgList) ;
		
		// Forcer la sauvegarde des modifications de la session pendant la creation du token
		
		// Verifier si le nombre de sesions autorisées par utilisateur na pas ete excédé 
		
		long nbrMaxiSession = sessionDAO.getSessionMaximumNbrePerUser() ;
		//long nbrSession = 0 ; // nombre de sessions actuel de l'utilisateur courant
		
		if (nbrMaxiSession > 0) {
			
			// chercher le nombre de sessions actuel de l'utilisateur courant
			
			long nbrSession = sessionDAO.nbrActiveSessionByUserId(validUser.getId(), null) ;
			
			if(nbrSession > nbrMaxiSession) {
				
				// Nbre maxi de session excédé
				
				String message = MessageTranslationUtil.translate(locale,
					 	SessionBag.CODE_TRADUCTION_MAXIMUM_SESSIONS_PER_USER_EXCEEDED,// venant du fichier
					 	SessionBag.CODE_TRADUCTION_MAXIMUM_SESSIONS_PER_USER_EXCEEDED, // Message par defaut
					 	new String[] {}) ;// 
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, message)) ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				return null ;
			}
		}
		
		// put session in map if it is not in it
		
		Map<String, SessionBag> sessionMap = securityInitializer.getActiveSessionMap() ;
		
		if (sessionMap != null && !sessionMap.containsKey(sessionBag.getId()))
			sessionMap.put(sessionBag.getId(), sessionBag) ;
		
		logger.info(getClass().getName() + ":: authenticate () :: Sortie  ");
		return token ;
	}

	/*
	public String computeUserRolesAsString(String userId, Locale locale,
			List<NonLocalizedStatusMessage> msgList) {
		
		return roleDAO.computeUserRolesAsString(userId, locale, msgList) ;
	}
	
	public List<Role> findAllRoleList(SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) {
		 
		List<Role> rtnList = new ArrayList<>() ;
		
		rtnList = new EntityUtil<Role>(Role.class).findListByFieldValues(entityManager, 
						"allRoleList", new String[] {}, new String[] {}, 
						null, true, new Role()) ;
		
		// traduire
		
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		for (Role role : rtnList) {
			
			String s = MessageTranslationUnit.staticTranslate(locale,
				 	role.getCodeTrDesignation(), role.getCodeTrDesignation(), new String[] {}) ;
			
			role.setDesignation(s);
			
			s = MessageTranslationUnit.staticTranslate(locale,
				 	role.getCodeTrDescription(), role.getCodeTrDescription(), new String[] {}) ;
			
			role.setDescription(s);
		}
		
		return rtnList ;
	}
	*/
	public boolean logout(SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) {
		
		boolean rtn = true ;/*securityService.logout(sessionBag, msgList) ;*/
		
		if (rtn && sessionBag != null) {
			
			try {
				(new EntityUtil<SessionBag>(SessionBag.class)).removeEntity(entityManager, 
						sessionBag, null, true) ;
			}
			catch (Exception e) {
				// Pas grave sera effacer plus tard par la routine d'effacement des sessions expirées
			}
		}
		
		return rtn ;
	}
	
	
	public String refresh(String oldToken, List<NonLocalizedStatusMessage> msgList) {
		
		return tokenManagement.refreshToken(oldToken, msgList) ;
	}

	/**
	 * Check user encrypted password against a given plain (on ecrypted) password. 
	 * Returns true if these passwords match
	 * @param user
	 * @param plainPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalArgumentException
	 */
	/*
	public boolean checkPassword(User user, String plainPassword) 
			throws NoSuchAlgorithmException, IllegalArgumentException {
		
		return userDAO.checkPassword(user, plainPassword) ;
	}

	public User updatePassword(User entity, String plainPassword, 
					SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) 
		throws Exception {
		
		return userDAO.updatePassword(entity, plainPassword, sessionBag, msgList) ;
	}
	*/
	/*public boolean changePassword(String currentPassword, String newPassword, 
			Locale locale, List<NonLocalizedStatusMessage> msgList) 
		throws NoSuchAlgorithmException, IllegalArgumentException {
		
		return securityService.changePassword(currentPassword, newPassword, locale, msgList) ;
	}*/
	/*
	public String getLanguage(String token, List<NonLocalizedStatusMessage> msgList) {
		return userDAO.getLanguage(token, msgList) ;
	}
	
	public String updateLanguage(String langue,String token, List<NonLocalizedStatusMessage> msgList) {
	
		return userDAO.updateLanguage(langue, token, msgList);
	}
	
	public String updateAnonymousLanguage(String sid, String langue, List<NonLocalizedStatusMessage> msgList) {
		return userDAO.updateAnonymousLanguage(sid, langue, msgList) ;
	}
	
	public String getAnonymousLanguage(String sid, List<NonLocalizedStatusMessage> msgList) {
		return userDAO.getAnonymousLanguage(sid, msgList) ;
	}
	
	public SessionBag createAnonymousSession(String hostName, List<NonLocalizedStatusMessage> msgList) {
		return sessionDAO.createAnonymousSession(hostName, msgList) ;
	}
	*/
    

}
