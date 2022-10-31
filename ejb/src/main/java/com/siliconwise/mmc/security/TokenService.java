package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.siliconwise.common.beanvalidation.LoggerUtil;
import com.siliconwise.common.locale.LocaleUtil;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Stateless
public class TokenService implements Serializable {

	private static final long serialVersionUID = 1L;

	
	//@Inject
	//ConfigParameterValueDAO configParameterValueDAO;
	
	/*
	@Inject
	UserOrGroupHasRoleLinkDAO userOrGroupHasRoleLinkDAO;*/

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName());

	@PersistenceContext
	private EntityManager entityManager;

	// @Inject private SessionDAO sessionDAO ;
	// @Inject private UserDAO userDAO ;
	// @Inject private QuestionsSecretesDAO questionsSecretesDAO ;

	public String refreshToken(String token, List<NonLocalizedStatusMessage> msgList) {

		if (token == null)
			return null;

		// Récupération de l'id de la session
		SessionBag currentSession = validateToken(token,msgList);

		// Récupérer la session courante
		// SessionBag currentSession = sessionDAO.getSession(sessionId, msgList) ;

		token = buildToken(currentSession, msgList);

		return token;
	}

	public SessionBag validateToken(String token, List<NonLocalizedStatusMessage> msgList) {

		 
		logger.info("_78 validateToken  :: begin  token = " + token);

		String sessionId = null;
		
		Locale locale = LocaleUtil.getDefaultLocale() ;

		try {
			
			// extract session id from token

			String secret = SecurityConstants.SESSION_TOKEN_SECRET_PHRASE;

			sessionId = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(token)
					.getBody().get(SecurityConstants.SESSION_TOKEN_FIELD_SESSION_ID, String.class);

			logger.info("_96 validateToken  ::  session id = " + sessionId);
			
			
		} catch (ExpiredJwtException  eje) {

			String msg = MessageTranslationUtil.translate(locale, SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
					SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
					(Map<String, String>) new HashMap<String, String>());

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));

			LoggerUtil.log(StatusMessageType.ERROR, logger, eje, "_98 isUserLoggedInInterceptor",
					SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
					(Map<String, String>) new HashMap<String, String>());

			return null;

		} catch (SignatureException se) {

			logger.error("_108 validateToken :: Token signature is not valid ");
			return null;

		} catch (MalformedJwtException mje) {

			logger.error("_113 validateToken :: Token is not badly formed");
			return null;
		}
		
		// Check if session exists and extract it
		
		SessionBag rtn = SessionUtil.findSessionBagById(sessionId);

		if (rtn == null) {
			
			Exception eje = null ;

			String msg = MessageTranslationUtil.translate(locale, SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
					SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
					(Map<String, String>) new HashMap<String, String>());

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));

			LoggerUtil.log(StatusMessageType.ERROR, logger, eje, "_98 isUserLoggedInInterceptor",
					SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
					(Map<String, String>) new HashMap<String, String>());
			
			return null;
		}

		return rtn;
		
	}

	/**
	 * 
	 * @param secret
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JWTCreationException
	 * @throws JsonProcessingException
	 */
	public String buildToken(SessionBag currentSession, List<NonLocalizedStatusMessage> msgList) {

		logger.info("_152 buildToken:: Entree ");

		if (currentSession == null) return null ;
		
		String secret = SecurityConstants.SESSION_TOKEN_SECRET_PHRASE;
		String token = null;

		// Locale locale = SessionUtil.getLocale(currentSession) ;

		Date expirationDate = Date.from(currentSession.getExpirationDateTime().toInstant());
		
		
		User user = EntityUtil.findEntityById(entityManager, currentSession.getUserId(), 
				null /*"graph.user.with.operator-id"*/, true, User.class);
		
		//TODO A de commenter une fois arrivé au niveau des roles 
		/*
		List<RoleEnum> roleIdList = userOrGroupHasRoleLinkDAO.findRoleIdListByUserId(uid);

		String roleIds = roleIdList != null && !roleIdList.isEmpty()
				? String.join(SecurityConstants.SESSION_TOKEN_FIELD_LIST_SEPARATOR, roleIdList)
				: null;

		String operatorId = user != null && user.getOperator() != null ? user.getOperator() .getId() : null ;
		 */
		
		try {
			
			SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;

			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);

			Key signingKey = new SecretKeySpec(apiKeySecretBytes, sigAlg.getJcaName());

			// Construction du jeton

			JwtBuilder builder = Jwts.builder().setSubject(user != null ? user.getFullname() : null)
					.setExpiration(expirationDate)
					.claim(SecurityConstants.SESSION_TOKEN_FIELD_SESSION_ID, currentSession.getId())
					.claim(SecurityConstants.SESSION_TOKEN_FIELD_USER_ID, user != null ? user.getId() : null)
					//.claim(SecurityConstants.SESSION_TOKEN_FIELD_ROLE_IDS_LIST, roleIds)
					.claim(SecurityConstants.SESSION_TOKEN_FIELD_EMAIL, user != null ? user.getEmail() : null)
					.claim(SecurityConstants.SESSION_TOKEN_FIELD_LOCALE_CODE, currentSession.getLocaleCode()) ;
					//.claim(SecurityConstants.SESSION_TOKEN_FIELD_OPERATOR_ID, operatorId).signWith(sigAlg, signingKey);

			token = builder.compact();

		} catch (JWTCreationException e) {

			String message = SessionBag.SESSION_ERREUR;

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, message));

			logger.error("_211 buildToken :: " + message + " " + e.getMessage() + " Cause: " + e.getCause());

			e.printStackTrace();

			throw e;
		}

		return token;
	}

	/**
	 * Extract token from request header
	 * 
	 * @param servletRequest
	 * @return
	 */
	public String extractToken(HttpServletRequest servletRequest) {

		if (servletRequest == null)
			return null;

		String rtn = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);

		return rtn == null || rtn.isEmpty() ? null : rtn;
	}
	
}
