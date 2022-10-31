package com.siliconwise.mmc.oldSecurity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.common.AppUtil;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.user.User;
import com.siliconwise.mmc.user.UserDAO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Stateless
public class TokenManagement implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private transient Logger logger =
			LoggerFactory.getLogger(getClass().getName()) ;
	
	@PersistenceContext private EntityManager entityManager ;
	
	@Inject private SessionDAO sessionDAO ;
	//@Inject private com.siliconwise.mmc.user.UserDAO userDAO ;
	//@Inject private QuestionsSecretesDAO questionsSecretesDAO ;

	public String refreshToken(
			String token, List<NonLocalizedStatusMessage> msgList) {

		if(token == null) return null ;
		
		// Récupération de l'id de la session
		SessionBag currentSession = validateToken(token) ;

		// Récupérer la session courante
		//SessionBag currentSession = sessionDAO.getSession(sessionId, msgList) ;
		
		token = buildToken(currentSession, msgList);

		return token ;
	}

	/**
	 * Vérifie que le token est valide 
	 * c-a-d 
	 * 	1- qu'il a une date d'expiration en cour de validité 
	 *  2- le session existe dans la BD  
	 * @param token
	 * @param secret
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JWTVerificationException
	 */
	 public String validateTokenOld(String token)  {       
	        
		 logger.info(getClass().getName()
				 +":: Entrée  ::validateToken  token = "+token);
		 		 
	     String  sessionId = null ;
		
		 try {
			
			 // Récupération du SID
			 String secret = getClass().getCanonicalName() ;
			 sessionId = Jwts
						.parser()
						.setSigningKey(DatatypeConverter.parseBase64Binary(secret))
						.parseClaimsJws(token).getBody().get("sid", String.class) ;
	
			 logger.info(getClass().getName()
					 +":: Entrée  ::validateToken  username = "+sessionId);
			 
		} catch (ExpiredJwtException eje) {
			
			return null ;
			
		}catch (SignatureException  se) {
			
			return null ;
			
		} catch (MalformedJwtException mje) {
						
			return null ;
			
		}
		 // Verifier que la session existe dans la BD 
		// List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		 SessionBag currentSession =  sessionDAO.findSessionBagById(sessionId) ;
		 
		 if (currentSession != null) {
			 sessionId = currentSession.getId() ;
		 }
		 
		 logger.info(getClass().getName()
				 +"::validateToken currentSession.toString() "
				 	+currentSession.toString());
		 
		 return sessionId ;
	  }
	 
	 public SessionBag validateToken(String token)  {       
	        
		 logger.info(" Entrée  ::validateToken  token = " + token);
		 		 
	     String  sessionId = null ;
		
		 try {
			
			 // Récupération du SID
			 
			 String secret = getClass().getCanonicalName() ;
			 sessionId = Jwts
						.parser()
						.setSigningKey(DatatypeConverter.parseBase64Binary(secret))
						.parseClaimsJws(token).getBody().get("sid", String.class) ;
	
			 logger.info("Entrée  ::validateToken  username = " + sessionId);
			 
		} catch (ExpiredJwtException eje) {
			
			logger.error("_149 Le Token " + token + " a expiré." );
			return null ;
			
		}catch (SignatureException  se) {
			
			logger.error("_154 Le Token " + token + " a une signature incorrecte." );
			return null ;
			
		} catch (MalformedJwtException mje) {
						
			logger.error("_159 Le Token " + token + " est mal formé." );
			return null ;
			
		}
		 // Verifier que la session existe dans la BD 
		// List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		 SessionBag currentSession =  sessionDAO.findSessionBagById(sessionId) ;
		 
		 if (currentSession == null) {
			 
			 logger.error("_169 L'Id de session " + sessionId + " du token " + token + "n'a pas de session associée.");
			 return null ;
		 }
		 
		 
		 logger.info("ValidateToken currentSession.toString() "	+ currentSession);
		 
		 return currentSession ;
	  }
	
	
	/**
	 *  
	 * @param secret
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws JWTCreationException
	 * @throws JsonProcessingException 
	 */
	public String buildToken(
			SessionBag currentSession, List<NonLocalizedStatusMessage> msgList) {
		
		logger.info(getClass().getName()+" :: buildToken:: Entree ");
		
		String secret = getClass().getCanonicalName() ;
		String token = null ;
		
		Locale locale = SessionUtil.getLocale(currentSession) ;
		
		if (currentSession.getDateHeureExpiration() == null) {
			
			long tokenLifeTime = sessionDAO.getSessionExpirationDelay() ;
			currentSession.setDateHeureExpiration(LocalDateTime.now().plusMinutes(tokenLifeTime)) ;
		
			// sauvegarder la session si neccessaire
			if (entityManager.contains(currentSession)) entityManager.flush();
		}
		
		Date expirationDate = Date.from(currentSession.getDateHeureExpiration()
				.atZone(ZoneId.systemDefault()).toInstant()) ;
		
		logger.info(getClass().getName() 
				+" :: buildToken:: expirationDate = "+expirationDate);
		
		//Personne personne = 
		//		currentSession.getLoggedInUser().getPersonne() ;	
		//IntermediaireFinancier intermediaire =
		//		currentSession.getIntermediaireFinancier() ;
		
		User loggedInUser = EntityUtil.findEntityById(
								entityManager, currentSession.getUserId(), 
								"graph.user.person.nom-prenom-email", true, 
								 User.class);
		
	//	String uid = loggedInUser != null ? loggedInUser.getId() : null ;
		
		//userDAO.findUserbyId(currentSession.getUserId(), msgList) ;
		
	/*	Personne loggedInPersonne =  loggedInUser != null ? loggedInUser.getPersonne() : null ;
		
		String nomPrenoms = loggedInPersonne != null 
							? (loggedInPersonne.getPrenom() != null ? loggedInPersonne.getPrenom() : "") 
								+ " " + (loggedInPersonne.getNom() != null ? loggedInPersonne.getNom() : "") 
							: null ;
		
		String ifId = null; String ifName = null ;
		
		IntermediaireFinancier intermediaireFinancier = EntityUtil.findEntityById(
				entityManager, currentSession.getIntermediaiareFinancierId(), 
				"graph.if.minimum", true, IntermediaireFinancier.class) ; */
		
	/*	if (intermediaireFinancier == null) {
			
			ifId = null ; ifName = null ;
		} "deployment.mmc-parent.ear.mmc-ejb.jar" 
		else {
			
			ifId = intermediaireFinancier.getId() ; 
			ifName = intermediaireFinancier.getSigle() ;			
		}
		
		boolean mustChangePassword = userDAO.mustUserChangePassword(uid, 
										locale, msgList) ;
		boolean mustDefineSecretQuestions = questionsSecretesDAO
				.mustUserDefineSecretQuestions(uid, locale, msgList) ;
		
		try {
			SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;
			
			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
			
			Key signingKey = new SecretKeySpec(apiKeySecretBytes, sigAlg.getJcaName());

			// Construction du jeton
			
			JwtBuilder builder = Jwts.builder()
					.setSubject(nomPrenoms)
					.setExpiration(expirationDate)
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_SESSION_ID, currentSession.getId())	
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_USER_ID, uid)	
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_USER_ROLE_IDS, currentSession.getRoleIds())	
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_IF_ID, ifId)	
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_MUST_CHANGE_PASSWORD, mustChangePassword)	
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_MUST_DEFINE_SECRETE_QUESTIONS, mustDefineSecretQuestions)	
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_IF_NAME, ifName)	
					.claim(AppUtil.LOGIN_TOKEN_FIELD_NAME_LANGUAGE, currentSession.getLanguage())
					.signWith(sigAlg, signingKey);
					
					
			token = builder.compact() ;
			
		} catch (JWTCreationException e) {
			
			String message = SessionBag.SESSION_ERREUR ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, message));
			e.printStackTrace();
		}
		// logger.info(getClass().getName() +" :: buildToken:: Sortie :: token = "+token);*/
		return token;
	}
	
	public String extractToken(HttpServletRequest servletRequest) {
		
		  if (servletRequest == null) return null ;
		  
		 String  rtn = servletRequest.getHeader(HttpHeaders.AUTHORIZATION) ;
		 
		 return rtn == null || rtn.isEmpty() ? null : rtn ;
	}
	
	/*public SessionBag getSessionBagByToken(String token) {

		String sessionId = null ;
		try {

			// Récupération du login
			String secret = getClass().getCanonicalName() ;
			/*sessionId = Jwts
					.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(secret))
					.parseClaimsJws(token).getBody().getSubject();* /
			sessionId = Jwts
					.parser()
					.setSigningKey(DatatypeConverter.parseBase64Binary(secret))
					.parseClaimsJws(token).getBody().get("sid", String.class) ;

			logger.info(getClass().getName()
					+":: Entrée  ::validateToken  username = "+sessionId);

		} catch (ExpiredJwtException eje) {
			
			return null ;

		}catch (SignatureException  se) {
			
			return null ;

		} catch (MalformedJwtException mje) {

			return null ;

		}
		
		List<NonLocalizedStatusMessage> msgList =  new ArrayList<>() ;
		
		return sessionDAO.getSession(sessionId, msgList) ;
	}*/
}
