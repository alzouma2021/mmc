package com.siliconwise.mmc.fournisseurauthentifcation;

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
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.security.HasSessionId;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.security.SessionCtrl;
import com.siliconwise.mmc.security.SessionUtil;
import com.siliconwise.mmc.user.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 * 
 * correction 13/01/2022 E. EZAN (schema type de String vers Token)
 *
 */
@Stateless
@Path("/fournisseurs")
public class FournisseurAuthentificationRest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String BASE_REST_URL = "/session" ;
	
	@Inject private FournisseurAuthentificationCtlInterface fournisseurAuthentificationCtl ;
	
	@Inject private SessionCtrl sessionCtrl ;
	@Inject private RestResponseCtrl responseCtrl ;	
	
//	@Context private HttpServletRequest servletRequest ;
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	
	/** authenticate a user against his credentials. If success return a session token
	 * @param request
	 * @param loginToken
	 * @return
	 */
	//@HasSessionId // must already have a session
	@POST
	@Path("loginGoogle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Authentification de l'utilisateur par Google échouée"
	        )
	/*@ApiResponse(
			responseCode = "200",
			description = "Authentification de l'utilisateur par Google  reussie",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "string")
			                )
	        )*/
	@ApiResponse(
			responseCode = "200",
			description = "Authentification de l'utilisateur par Google  reussie",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = TokenAccess.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Authentification de l'utilisateur par google",
			description = "Authentification de l'utilisateur par google"
			)
	/*public Response loginGoogle (
			@RequestBody(
			   description = "Token d'access de l'utilisateur", 
			   required = true,
			   content = @Content(schema = @Schema(type = "string")) )
			@Context HttpServletRequest request, 
			String accesTokenGoogle)  {
	*/	
	public Response loginGoogle (
				@RequestBody(
				   description = "Token d'access de l'utilisateur", 
				   required = true,
				   content = @Content(schema = @Schema(type = "string")) )
				@Context HttpServletRequest request, 
				TokenAccess tokenAccess)  {
		
		
		//logger.info("_177 login :: loginToken =" + accesTokenGoogle);//
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;

		SessionBag sessionBag = sessionCtrl.extractSession(request, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean isEjbException = false ;
		Response.Status status = Response.Status.OK ;

		String rtn = null;
		
		String accesTokenGoogle = tokenAccess.tokenAccess ;
		
		logger.info("_177 login :: loginToken =" + accesTokenGoogle);

		try {
			
		 rtn = fournisseurAuthentificationCtl
				 .authentifierByGoogleProvider(accesTokenGoogle, locale, sessionBag, msgList);
			
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

	
/****************************************** Authentification par FaceBook *****************************************/
	
	
	/** authenticate a user against his credentials. If success return a session token
	 * @param request
	 * @param loginToken
	 * @return
	 */
	//@HasSessionId // must already have a session
	@POST
	@Path("loginFaceBook")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Authentification de l'utilisateur par Facebook échouée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Authentification de l'utilisateur par Facebook  reussie",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = TokenAccess.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Authentification de l'utilisateur par Facebook",
			description = "Authentification de l'utilisateur par Facebook"
			)
	public Response loginFaceBook (
			@RequestBody(
			   description = "Token d'access de l'utilisateur", 
			   required = true,
			   content = @Content(schema = @Schema(type = "string")) )
			@Context HttpServletRequest request, 
			TokenAccess tokenAccess)  {
		
		
		//Variables
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;

		SessionBag sessionBag = sessionCtrl.extractSession(request, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean isEjbException = false ;
		Response.Status status = Response.Status.OK ;

		String rtn = null;
		
		String accesTokenFaceBook = tokenAccess.tokenAccess ;
		
		//Try Catch

		try {
			
			
		 rtn = fournisseurAuthentificationCtl
				 .authentifierByFaceBookProvider(accesTokenFaceBook, locale, sessionBag, msgList);
			
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
	

}

