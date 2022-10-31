package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;


/**
 * @author bgnakale
 *
 */
@HasSessionId @Interceptor
public class HasSessionIdInterceptor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject private TokenService tokenUtil ;
	
	List<NonLocalizedStatusMessage> msgList  = new ArrayList<NonLocalizedStatusMessage>();
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	/** Ensure that a valide session id is supplied
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object hasSessionIdInterceptor(InvocationContext ctx) throws Exception{

		logger.info("Securing {}.{}({})", new Object[] { ctx.getClass().getName(),
				ctx.getMethod(), ctx.getParameters() });
		
		final Class<? extends Object> runtimeClass = ctx.getTarget().getClass();
		boolean hasSessionId = false ;
		
		try { // check method first

			HasSessionId annotation = ctx.getMethod().getAnnotation(HasSessionId.class);
			hasSessionId = annotation != null ;

		} catch (NullPointerException e) {

			hasSessionId = false;
		}
		
		if (!hasSessionId) { // check class level
			
			try {

				HasSessionId annotation = runtimeClass.getAnnotation(HasSessionId.class);
				hasSessionId =  annotation != null ;

			} catch (NullPointerException e) {

				hasSessionId = false;
			}
		}
		
		if (hasSessionId) {
			
			// Récupérer le 1er parametre de la méthode
			HttpServletRequest httpRequest = (HttpServletRequest) ctx.getParameters()[0] ;
		
			// Récupérer le jeton dans les entêtes 
			
			String token = tokenUtil.extractToken(httpRequest) ;
			
			if (token != null && !token.isEmpty()) {
				
				SessionBag sessionBag = tokenUtil.validateToken(token,msgList) ;
				
				Locale locale = SessionUtil.getLocale(sessionBag) ;
				
				if (sessionBag == null) {
					
					// session not found, user is not authorized
					
					String msg = MessageTranslationUtil.translate(locale,
							SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION,
							SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION, 
							(Map<String, String>)new HashMap<String, String>()) ;
					
					logger.error(" _102 sessionInterceptor :: " + msg);
					
					throw new WebApplicationException(msg, Response.Status.UNAUTHORIZED) ;
				}
				else { 
					
					// check if sesion is valid
					if (sessionBag.isExpired()) {
						
						String msg = MessageTranslationUtil.translate(locale,
								SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
								SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED, 
								(Map<String, String>)new HashMap<String, String>()) ;
						
						logger.error(" _95 sessionInterceptor :: " + msg + " " 
										+ "Token=" + token + " id session="+sessionBag.getId() + " "
										+ "Expiration date ="+ sessionBag.getCreationDateTime() + " "
										+ "Current date =" + OffsetDateTime.now());
						
						throw new WebApplicationException(msg, Response.Status.UNAUTHORIZED) ;
					}
					
					// found session  is valid, proceed with method
					return ctx.proceed() ;
				}
			}
			else {
				
				// Récupérer le sid dans les entêtes 
				String sessionId = httpRequest.getHeader(SecurityConstants.SESSION_TOKEN_FIELD_SESSION_ID) ;
				
				if (sessionId == null || sessionId.isEmpty()) {

					Locale locale = SessionUtil.getLocale(null) ;

					String msg = MessageTranslationUtil.translate(locale, 
							SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION, 
							SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION, 
							(Map<String, String>)new HashMap<String, String>()) ;
					
					logger.error(" 144 sessionInterceptor ::  " + msg + " sessionId = " + sessionId);
					throw new WebApplicationException(msg, Response.Status.UNAUTHORIZED) ;
				}
				
				// Check if session exists in memory
				// In cluster environment, sessions must be stored in a distributed cache.
				
				SessionBag sessionBag = SessionUtil.findSessionBagById(sessionId);
				
				Locale locale = SessionUtil.getLocale(sessionBag) ;
				
				if (sessionBag == null || sessionBag.isExpired()) {
					
					String msgCode = sessionBag == null
								? SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION 
								: SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED ;
					
					String msg = MessageTranslationUtil.translate(locale, msgCode, msgCode, 
									(Map<String, String>)new HashMap<String, String>()) ;
					
					logger.error(" _134 sessionInterceptor :: " + msg + " Session ID=" + sessionId 
									+ " Session expiration date = " + sessionBag.getExpirationDateTime()
									+ " Current date = " + OffsetDateTime.now()) ;
					
					throw new WebApplicationException(msg, Response.Status.UNAUTHORIZED) ;
				}
			}
		}
				
		return ctx.proceed();
	}
}
