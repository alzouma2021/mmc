package com.siliconwise.mmc.programmeimmobilier.caracteristiquedemandereservationlogement;

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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Stateless
@Path("/associercaracteristiquesdemandereservationlogement")
@OpenAPIDefinition(info = @Info(title = "Creation ou Modification d'association des caracteristiques de demande reservation logement: ", version = "1.0", description = "Creation des caracteristiques de demande reservation logement", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class CaracteristiqueDemandeReservationLogementRest implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	@Inject private AssocierCaracteristiqueDemandeReservationLogementCtlInterface  associerCaracteristiqueDemandeReservationLogementCtl ;
	
	@Inject private SessionCtrl sessioncrtl ;
	
	@Inject private RestResponseCtrl responseCtrl ;
	
	
	
	@Secured
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Association n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Association  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = CaracteristiqueDemandeReservationLogement.class))
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Association des caracteristiques de demande réservation logement",
			description = "Association des caracteristiques de demande réservation logement"
			)
	public Response creerModeFinancement(
						@RequestBody(
							description = "Association des caracteristiques de demande de réservation logement", 
							required = true,
							content = @Content(schema = @Schema(implementation = CaracteristiqueDemandeReservationLogement.class)))
						@Context HttpServletRequest servletRequest,
						List<CaracteristiqueDemandeReservationLogement>  entityList) {
		
		
		//Declaration des variables 
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessioncrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag) ;
		
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
		
		List<CaracteristiqueDemandeReservationLogement> rtnList =  new ArrayList<CaracteristiqueDemandeReservationLogement>(); ;

		try {
			
			rtnList =  associerCaracteristiqueDemandeReservationLogementCtl
					               .associerCaracteristiqueDemandeReservationLogementList(entityList, mustUpdateExistingNew, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			
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
		
				rtnList = null ;
				
		}
		

		return responseCtrl.sendResponse(rtnList, estEjbException, status, msgList) ;
	
	}
	
/************************************ Modification d'association caracteristiques de demande réservation logement *******************************************************/
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Modification d'association n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Modification d'association effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = CaracteristiqueDemandeReservationLogement.class))
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Modification d'association des caracteristiques de demande réservation logement",
			description = "Modification d'association des caracteristiques de demande réservation logement"
			)
	public Response modifierModeFinancement(
						@RequestBody(
							description = "Modifier associer des caracteristiques de demande réservation logement", 
							required = true,
							content = @Content(schema = @Schema(implementation = CaracteristiqueDemandeReservationLogement.class)))
						@Context HttpServletRequest servletRequest,
						List<CaracteristiqueDemandeReservationLogement>  entityList) {
		
		
		//Declaration des variables 
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessioncrtl.extractSession(servletRequest, null,  msgList) ;
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
		
		List<CaracteristiqueDemandeReservationLogement> rtnList =  new ArrayList<CaracteristiqueDemandeReservationLogement>(); ;
		

		try {
		
			
			rtnList = associerCaracteristiqueDemandeReservationLogementCtl
					        .associerCaracteristiqueDemandeReservationLogementList(entityList, mustUpdateExistingNew, namedGraph, isFetchGraph, locale, loggedInUser, msgList);
			
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtnList = null ;
				
		}
		
		return responseCtrl.sendResponse(rtnList, estEjbException, status, msgList) ;
		
	}
	

}
