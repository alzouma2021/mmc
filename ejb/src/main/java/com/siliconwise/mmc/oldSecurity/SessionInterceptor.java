package com.siliconwise.mmc.oldSecurity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bgnakale
 *
 */
@HasSessionId
@Interceptor
public class SessionInterceptor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	@Inject private TokenManagement tokenManagement ;
	//@Inject private SecurityInitializer securityInitializer ;
	@Inject private SessionDAO sessionDAO ;
	
	@AroundInvoke
	public Object sessionInterceptor(InvocationContext ctx) throws Exception{

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
			
			String token = tokenManagement.extractToken(httpRequest) ;
			
			if (token != null && !token.isEmpty()) {
				
				SessionBag sessionBag = tokenManagement.validateToken(token) ;
				
				
				if (sessionBag == null) {
					
					// session not found, user is not authorized
					logger.error(" _88 Aucune session associé au token:" + token);
					throw new WebApplicationException(Response.Status.UNAUTHORIZED) ;
				}
				else { 
					
					// check if sesion is valid
					if (sessionBag.isExpired()) {
						
						logger.error(" _95 La session a expirée. token=" + token + " id session="+sessionBag.getId()
										+ "Date expiration="+sessionBag.getDateHeureExpiration()
										+ " Date actuelle=" + LocalDateTime.now());
						
						throw new WebApplicationException(Response.Status.UNAUTHORIZED) ;
					}
					
					// found session  is valid, proceed with method
					return ctx.proceed() ;
				}
			}
			else {
				
				// Récupérer le sid dans les entêtes 
				String sessionId = httpRequest.getHeader("sid") ;
				
				if (sessionId == null || sessionId.isEmpty()) {
					
					logger.error(" _115 Id de session non defini");
					throw new WebApplicationException(Response.Status.UNAUTHORIZED) ;
				}
				
				// Check if session exists in DB (for ckuster operation compatibility)
				
				SessionBag sessionBag = sessionDAO.findSessionBagById(sessionId);
				
				if (sessionBag == null || sessionBag.isExpired()) {
					
					String msg = sessionBag == null
								? "Session iniexistante. Id session = " + sessionId 
								: "La session a expirée. Id session = " + sessionId 
									+ " Date expiration=" + sessionBag.getDateHeureExpiration()
									+ " Date actuelle=" + LocalDateTime.now() ;
					
					logger.error(" _134 " + msg) ;
					throw new WebApplicationException(Response.Status.UNAUTHORIZED) ;
				}
				
				/*boolean isSessionIdInMap = securityInitializer.getActiveSessionMap().containsKey(sessionId) ;
				
				if (!isSessionIdInMap) {
					throw new WebApplicationException(Response.Status.UNAUTHORIZED) ;
				}
				*/
			}
		}
				
		return ctx.proceed();
	}
}
