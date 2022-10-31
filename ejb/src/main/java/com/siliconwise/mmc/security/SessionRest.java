package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.beanvalidation.LoggerUtil;
import com.siliconwise.common.locale.LocaleUtil;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.ProduitLogement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;



@Stateless
@Path(SessionRest.BASE_REST_URL)
public class SessionRest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String BASE_REST_URL = "/session" ;
	
	@Inject private SessionCtrl sessionCtrl ;
	
	@Inject private RestResponseCtrl responseCtrl ;	
	
//	@Context private HttpServletRequest servletRequest ;
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	/**
	 * Create a new anonymous session 
	 * 
	 * @param request
	 * @param localeCode locale following the pattern
	 *  3LetterLanguageCode-2LettersCountryCode
	 * @return
	 */
	@POST
	@Path("locale/{locale}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Ouverture d'une ouvertue de session anonyme echouée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Ouverture d'une ouvertue de session anonyme reussie",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "string")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Ouverture d'une ouvertue de session anonyme",
			description = "Ouverture d'une ouvertue de session anonyme"
			)
	public Response createAnonymousSession(
					 @Parameter(
					     description = "Code de la langue et du pays de l'utilisateur",
					     required = true,
					     example = "3LetterLanguageCode-2LettersCountryCode",
					     schema = @Schema(type = "string")
					       )
			          @Context HttpServletRequest request,
			          @PathParam("locale") String localeCode) {

		List<NonLocalizedStatusMessage> msgList = new ArrayList<>();

		Locale locale = LocaleUtil.deserialiseLocale(localeCode);
		
		if (locale == null) locale = LocaleUtil.getDefaultLocale();

		boolean isEjbException = false;
		Response.Status status = Response.Status.OK;

		SessionBag rtn = null;

		try {
			
			rtn = sessionCtrl.createOrUpdateSession(null, null, locale);
			
		} 
		catch (Exception ex) {

			String msg = MessageTranslationUtil.translate(locale,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>) new HashMap<String, String>());
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));

			LoggerUtil.log(StatusMessageType.ERROR, logger, ex, 
					"_85 createAnonymousSession", AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>)new HashMap<String, String>());

			status = Response.Status.INTERNAL_SERVER_ERROR;
			isEjbException = true;

			rtn = null;
		}

		return responseCtrl.sendResponse(rtn == null ? null : rtn.getId(), isEjbException, status, msgList);
	}
	
	/** authenticate a user against his credentials. If success return a session token
	 * @param request
	 * @param loginToken
	 * @return
	 */
	@HasSessionId // must already have a session
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Authentification de l'utilisateur éechouée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Authentification de l'utilisateur  reussié",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "string")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Authentification d'un utilisateur par ses parametrres de connexion",
			description = "Authentification d'un utilisateur par ses parametrres de connexione"
			)
	public Response login (
			@RequestBody(
			   description = "Login d'un utilisateur ", 
			   required = true,
			   content = @Content(schema = @Schema(implementation = LoginToken.class)))
			@Context HttpServletRequest request, 
			LoginToken loginToken)  {
		
		
		logger.info("_177 login :: loginToken =" + loginToken);
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;

		SessionBag sessionBag = sessionCtrl.extractSession(request, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean isEjbException = false ;
		Response.Status status = Response.Status.OK ;

		String rtn = null;

		try {
			
		 rtn = sessionCtrl.login(loginToken, sessionBag, msgList);
			
		} 
		catch (Exception ex) {

			String msg = MessageTranslationUtil.translate(locale,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>) new HashMap<String, String>());
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));

			LoggerUtil.log(StatusMessageType.ERROR, logger, ex, "_135 login", 
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>)new HashMap<String, String>());

			status = Response.Status.INTERNAL_SERVER_ERROR;
			isEjbException = true;

			rtn = null;
		}

		return responseCtrl.sendResponse(rtn, isEjbException, status, msgList);
	}
	
	/** Log user out
	 * @param request
	 * @return
	 */
	@UserIsLoggedIn 
	@POST
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Déconnexion de l'utilisateur échouée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Déconnexion de l'utilisateur  reussie",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "string")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Déconnexion d'un utilisateur",
			description = "Déconnexion d'un utilisateur "
			)
	public Response logout (
			@Context HttpServletRequest request)  {
		
		logger.info("_161 logout :: logout ");
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;

		SessionBag sessionBag = sessionCtrl.extractSession(request, null,  msgList) ;
		
		Locale locale =  SessionUtil.getLocale(sessionBag) ;
		
		boolean isEjbException = false ;
		Response.Status status = Response.Status.OK ;

		Boolean rtn = null;

		try {
			rtn = sessionCtrl.logout(sessionBag, msgList);
		} 
		catch (Exception ex) {

			String msg = MessageTranslationUtil.translate(locale,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>) new HashMap<String, String>());
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));

			LoggerUtil.log(StatusMessageType.ERROR, logger, ex, "_135 login", 
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>)new HashMap<String, String>());

			status = Response.Status.INTERNAL_SERVER_ERROR;
			isEjbException = true;

			rtn = null;
		}

		return responseCtrl.sendResponse(rtn, isEjbException, status, msgList);
	}
	
	/** Update current session locale. Locale is of pattern 3LettersLanguageCode-2LettersCountruCode
	 * @param request
	 * @param localeCode
	 * @return true if successful otherwise false
	 */
	@UserIsLoggedIn // use must be logged in
	@PUT
	@Path("locale/{locale}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Mise à jour de la langue d'une session echouée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Mise à jour de la langue d'une session  reussie",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "string")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Mise à jour de la langue de la session",
			description = "Mise à jour de la langue de la session"
			)
	public Response updateSessionLanguage(
			@Parameter(
				description = "Code de la langue et du pays de l'utilisateur",
				required = true,
				example = "3LetterLanguageCode-2LettersCountryCode",
			    schema = @Schema(type = "string")
				       )
			@Context HttpServletRequest request,
			@PathParam("locale") String localeCode){
		
		logger.info("_150 login :: localeCode =" + localeCode);
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;

		SessionBag sessionBag = sessionCtrl.extractSession(request, null,  msgList) ;
	
		Locale localeToUpdate = LocaleUtil.deserialiseLocale(localeCode) ;
		
		boolean isEjbException = false ;
		Response.Status status = Response.Status.OK ;

		boolean rtn = false ;

		String sessionId = sessionBag != null ? sessionBag.getId() : null ;
		
		try {
			rtn = sessionCtrl.updateSessionLocale(sessionId, localeToUpdate, msgList) ;
		} 
		catch (Exception ex) {

			Locale locale = SessionUtil.getLocale(sessionBag) ;
			
			String msg = MessageTranslationUtil.translate(locale,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>) new HashMap<String, String>());
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));

			LoggerUtil.log(StatusMessageType.ERROR, logger, ex, "_135 login", 
					AppMessageKeys.TRANSLATION_MESSAGE_GENERAL_ERROR_RETRY_RECOMMENDED,
					(Map<String, String>)new HashMap<String, String>());

			status = Response.Status.INTERNAL_SERVER_ERROR;
			isEjbException = true;

			rtn = false ;
		}

		return responseCtrl.sendResponse(rtn, isEjbException, status, msgList);
		
	}
	
}
