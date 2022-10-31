package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.locale.LocaleUtil;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * Check that the current user is logged in
 * @author bgnakale
 *
 */
@UserIsLoggedIn @Interceptor
public class UserIsLoggedInInterceptor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject private TokenService tokenService ;
	
	@PersistenceContext private EntityManager entityManager ;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	/** Ensure that a valide session id is supplied
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object isUserLoggedInInterceptor(InvocationContext ctx) throws Exception{

		logger.info("_44 isUserLoggedInInterceptor ", new Object[] { ctx.getClass().getName(),
				ctx.getMethod(), ctx.getParameters() });
		
		final Class<? extends Object> runtimeClass = ctx.getTarget().getClass();
		boolean isUserLoggedIn = false ;
		
		try { // check method first

			UserIsLoggedIn annotation = ctx.getMethod().getAnnotation(UserIsLoggedIn.class);
			isUserLoggedIn = annotation != null ;

		} catch (NullPointerException e) {

			isUserLoggedIn = false;
		}
		
		if (!isUserLoggedIn) { // check class level
			
			try {

				UserIsLoggedIn annotation = runtimeClass.getAnnotation(UserIsLoggedIn.class);
				isUserLoggedIn =  annotation != null ;

			} catch (NullPointerException e) {

				isUserLoggedIn = false;
			}
		}
		
		if (isUserLoggedIn) {
			
			// if user is logged in , then a token must be supplied with the request header
			// Then retrieve the corresponding session bag
			
			HttpServletRequest httpRequest = (HttpServletRequest) ctx.getParameters()[0] ;
		
			String token = tokenService.extractToken(httpRequest) ;
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
			
			SessionBag sessionBag = token != null ? tokenService.validateToken(token, msgList) : null ;
			
			if (sessionBag == null) {
				
				// session not found, user is not authorized
				
				String msg = com.siliconwise.mmc.message.MessageTranslationUtil.translate(LocaleUtil.getDefaultLocale(),
						SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION,
						SessionBag.TRANSLATION_MESSAGE_SESSION_NO_SESSION, 
						(Map<String, String>)new HashMap<String, String>()) ;
				
				logger.error(" _98 isUserLoggedInInterceptor :: " + msg);
				
				throw new WebApplicationException(msg, Response.Status.UNAUTHORIZED) ;
			}
			
			// if no userId is store in the session then user is not logged
			
			Locale locale = SessionUtil.getLocale(sessionBag) ;
			
			String userId = sessionBag.getUserId() ; 
			
			if (userId == null || userId.isEmpty()) {
				
				// user is not logged in
				
				String msg = MessageTranslationUtil.translate(locale,
						SessionBag.TRANSLATION_MESSAGE_USER_NOT_LOGGEDIN,
						SessionBag.TRANSLATION_MESSAGE_USER_NOT_LOGGEDIN, 
						(Map<String, String>)new HashMap<String, String>()) ;
				
				throw new WebApplicationException(msg, Response.Status.UNAUTHORIZED) ;
				
			}
			
			// there is a user if in the session bag. Check that it is a valid user
			
			/*Long nbr =  EntityUtil.findSingleResultByFieldValues(entityManager, "countUserIdById", 
					new String[]{"id"}, new String[]{sessionBag.getUserId()}, 
					"graph.any-entity.id-version-only", true , User.class) ;*/
			
			Long nbr = EntityUtil.nbrOfEntityInstancesById(entityManager, User.class, sessionBag.getUserId());
			
			if (nbr == 0) {
				
				// User ifd is not valid. User is not logged in
				
				String msg = com.siliconwise.mmc.message.MessageTranslationUtil.translate(locale,
						SessionBag.TRANSLATION_MESSAGE_USER_NOT_LOGGEDIN,
						SessionBag.TRANSLATION_MESSAGE_USER_NOT_LOGGEDIN, 
						(Map<String, String>)new HashMap<String, String>()) ;
				
				throw new WebApplicationException(msg, Response.Status.UNAUTHORIZED) ;
			}
			
			
		}
				
		return ctx.proceed();
	}
}
