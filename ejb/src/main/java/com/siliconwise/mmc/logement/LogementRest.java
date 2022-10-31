package com.siliconwise.mmc.logement;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;


import javax.servlet.http.HttpServletRequest;


import com.siliconwise.common.AppUtil;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.security.SessionCtrl;
import com.siliconwise.mmc.security.SessionUtil;
import com.siliconwise.mmc.user.User;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


/**
 * Cette classe ecrit les APis Rest portant sur les  logements
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 29/03/2022
 *
 */
@Stateless
@Path("/logements")
@OpenAPIDefinition(info = @Info(title = "Logement Service", version = "1.0", description = "Produits Logements Service APIs : Le Format utilisé pour les Date , DateTime et Time est seul de ISO", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class LogementRest implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Inject private CreerModifierUnLogementCtlInterface  creerModifierUnLogementCtlInterface ;
	
	@Inject private SessionCtrl sessionCrtl ;
	@Inject private RestResponseCtrl responseCtrl ;
	
	
	private static transient Logger logger =  LoggerFactory.getLogger(LogementRest.class) ;
	

	
	/**
	 * @param servletRequest
	 * @param entity
	 * @return un  logement créé
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La création  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La création  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = Logement.class)
			           )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Création  des  logements",
			description = "Création   des  logements "
			)
	public Response creerUnLogement(
						@RequestBody(
							description = "Créer  un  logement", 
							required = true,
							content = @Content(schema = @Schema(implementation = Logement.class)))
						@Context HttpServletRequest servletRequest,
						Logement entity) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
	    User loggedInUser = new User();
		Boolean estCreation = true  ;
	    
		// appel de la method du controleur

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
	
	
		Logement rtn = null  ;
		

		try {
			
			
			rtn =  creerModifierUnLogementCtlInterface
					.creerModifierUnLogement(entity, mustUpdateExistingNew, 
						namedGraph, isFetchGraph, locale,loggedInUser, msgList,estCreation) ;
			
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
	
	
	
/******************************Modification d'un  logement**************************************************************************************/
	
	/**
	 * @param servletRequest
	 * @param entity
	 * @return un produit logement créé
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La modification  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La modification  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = Logement.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Modification  des  logements",
			description = "Modification   des  logements "
			)
	public Response modifierUnProduitLogement(
						@RequestBody(
							description = "Modifier  un  logement", 
							required = true,
							content = @Content(schema = @Schema(implementation = Logement.class)))
						@Context HttpServletRequest servletRequest,
						Logement entity) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
	    User loggedInUser = new User();
		Boolean estCreation = false ;
	    
		// appel de la method du controleur

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		Logement rtn = null  ;

		try {
			
			
			rtn =  creerModifierUnLogementCtlInterface
					.creerModifierUnLogement(entity, mustUpdateExistingNew, 
						namedGraph, isFetchGraph, locale,loggedInUser, msgList,estCreation) ;
			
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
		

}
	
