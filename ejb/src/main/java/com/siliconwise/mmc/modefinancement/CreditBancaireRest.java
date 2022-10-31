package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.siliconwise.common.AppUtil;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.Secured;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.security.SessionCtrl;
import com.siliconwise.mmc.security.SessionUtil;
import com.siliconwise.mmc.user.User;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@Stateless
@Path("/creditbancaires")
@OpenAPIDefinition(info = @Info(title = "Creation des modes financements: ", version = "1.0", description = "Creation d'un mode financement", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class CreditBancaireRest implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	@Inject private ValiderUnCreditBancaireCtlInterface  validerUnCreditBancaireCtl ;
	//@Inject private SessionDAO sessionDAO ;
	@Inject private SessionCtrl sessionCrtl ;
	@Inject private RestResponseCtrl responseCtrl ;
	
	

	@Secured
	@PUT
	@Path("{idCreditBancaire}/valider")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "validation n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "validation  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = ModeFinancement.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "validation d'un credit bancaire",
			description = "validation d'un credit bancaire"
			)
	public Response validerCreditBancaire(
						@RequestBody(
							description = "validation d'un mode fiancement", 
							required = true,
							content = @Content(schema = @Schema(implementation = ModeFinancement.class)))
						@Context HttpServletRequest servletRequest,
						@PathParam("idCreditBancaire") String idCreditBancaire) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
		
	    User loggedInUser = new User();
	    loggedInUser.setId("11aabbbdfgg");
	    loggedInUser.setNom("alzouma");
	    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	    loggedInUser.setMotDePasse("alzouma2021");

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		boolean rtn = false  ;
	

		try {
			
			rtn =  validerUnCreditBancaireCtl
						.validerUnCreditBancaire(idCreditBancaire, mustUpdateExistingNew, 
							namedGraph, isFetchGraph, locale,loggedInUser, msgList) ;
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			logger.info("129_MessageTranslationUtil ::  msg="+msg);
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = false ;
		}
		

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
		
	}
	

}
