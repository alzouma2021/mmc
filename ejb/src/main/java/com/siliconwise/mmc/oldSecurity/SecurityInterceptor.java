package com.siliconwise.mmc.oldSecurity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.common.AppUtil;
import com.siliconwise.common.rest.CustomizedResponse;
import com.siliconwise.common.rest.RestResponseCtrl;

@Secured
@Interceptor
public class SecurityInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String CODE_TRADUCTION_GENERAL_ERROR = "interceptor.secured.general.error" ;
	public static final String CODE_TRADUCTION_USER_NOT_LOGGED = "interceptor.secured.user.notLogged" ;
	
	@Inject private SessionDAO sessionDAO ;
	
	@Inject private RestResponseCtrl responseCtrl ;
	@Inject private TokenManagement tokenManagement;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName());

	@AroundInvoke
	public Object interceptGet(InvocationContext ctx) throws Exception {

		logger.info("Securing {}.{}({})",
				new Object[] { ctx.getClass().getName(), ctx.getMethod(), ctx.getParameters() });

		SessionBag sessionBag = null ;
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		
		try {
			logger.info("[security] checking for authenticated user.");

			// Récupérer le jeton dans les entêtes
			
			HttpServletRequest httpRequest = (HttpServletRequest) ctx.getParameters()[0];

			String token = tokenManagement.extractToken(httpRequest);

			logger.info(getClass().getName() + " interceptGet :: token = " + token);

			if (token == null || token.isEmpty()) {
				
				sessionBag = sessionDAO.getSession(httpRequest, msgList) ;
				
				Locale locale = sessionBag != null && sessionBag.getLanguage() != null
										&& !sessionBag.getLanguage().isEmpty()
									? SessionUtil.getLocale(sessionBag) 
									: new Locale(AppUtil.APP_DEFAULT_LANGUAGE) ;
				
				String msg  = MessageTranslationUtil.translate(locale,
						CODE_TRADUCTION_USER_NOT_LOGGED,
						CODE_TRADUCTION_USER_NOT_LOGGED, 
						new String[] {}) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
				//CustomizedResponse response = new CustomizedResponse(null, msgList) ;
				
				return responseCtrl.sendResponse(
						null, false, 
						Response.Status.UNAUTHORIZED, msgList) ;
				
				//throw new WebApplicationException(Response.Status.UNAUTHORIZED);
			}

			sessionBag = tokenManagement.validateToken(token);

			Locale locale = sessionBag != null && sessionBag.getLanguage() != null
					&& !sessionBag.getLanguage().isEmpty()
				? SessionUtil.getLocale(sessionBag) 
				: new Locale(AppUtil.APP_DEFAULT_LANGUAGE) ;
			
			if (sessionBag == null) {

				String msg  = MessageTranslationUtil.translate(locale,
						CODE_TRADUCTION_USER_NOT_LOGGED,
						CODE_TRADUCTION_USER_NOT_LOGGED, 
						new String[] {}) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					
				//CustomizedResponse response = new CustomizedResponse(null, msgList) ;
					
				return responseCtrl.sendResponse(
						null, false, 
						Response.Status.UNAUTHORIZED, msgList) ;
					
				//throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				// throw new AuthorizationException();
			}
			
			if (sessionBag.isExpired() || sessionBag.getUserId() == null || sessionBag.getUserId().isEmpty()) {
				
				// session expirée ou utilisateur deconnecté
				
				String msg = sessionBag.isExpired()
							? "La session a expirée. Id session = " + sessionBag.getId() 
								+ " token=" + token
								+ " Date expiration=" + sessionBag.getDateHeureExpiration()
								+ " Date actuelle=" + LocalDateTime.now() 
							: "utilisateur non connecté. Id session = " + sessionBag.getId() 
								+ " token=" + token ;
				logger.error(" _123 " + msg);
				
				msg  = MessageTranslationUtil.translate(locale,
								CODE_TRADUCTION_USER_NOT_LOGGED,
								CODE_TRADUCTION_USER_NOT_LOGGED, 
								new String[] {}) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
									
				//CustomizedResponse response = new CustomizedResponse(null, msgList) ;
									
				return responseCtrl.sendResponse(null, false, Response.Status.UNAUTHORIZED, msgList) ;
			}
		}
		catch (Exception e) {
		
			Locale locale = sessionBag != null && sessionBag.getLanguage() != null
					&& !sessionBag.getLanguage().isEmpty()
				? SessionUtil.getLocale(sessionBag) 
				: new Locale(AppUtil.APP_DEFAULT_LANGUAGE) ;
				
			String msg  = MessageTranslationUtil.translate(locale,
				CODE_TRADUCTION_GENERAL_ERROR,
				CODE_TRADUCTION_GENERAL_ERROR, 
				new String[] {}) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
			logger.error("_149 " + msg + " " + e.getMessage());
		
			//CustomizedResponse response = new CustomizedResponse(null, msgList) ;
		
			return responseCtrl.sendResponse(null, false, Response.Status.INTERNAL_SERVER_ERROR, msgList) ;
		}

		return ctx.proceed();
	}
}
