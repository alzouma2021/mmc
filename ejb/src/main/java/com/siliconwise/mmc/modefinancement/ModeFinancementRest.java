package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.security.SessionCtrl;
import com.siliconwise.mmc.security.SessionUtil;
import com.siliconwise.mmc.user.User;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Stateless
@Path("/modefinancements")
@OpenAPIDefinition(info = @Info(title = "Creation des modes financements: ", version = "1.0", description = "Creation d'un mode financement", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class ModeFinancementRest implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	@Inject private CreerModifierModeFinancementCtlInterface  creerModifierModeFinancementCtl ;
	//@Inject private SessionDAO sessionDAO ;
	@Inject private SessionCtrl sessioncrtl ;
	@Inject private RestResponseCtrl responseCtrl ;
	@Inject private SupprimerModeFinancementCtlInterface supprimerModeFinancementCtl ;
	@Inject private RechercherModeFinancementCtlInterface rechercherModeFinancementCtl ;
	
	
	@Secured
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Creation n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Création  effectuée",
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
			summary = "Création d'un mode de financement",
			description = "Création d'un mode de financement"
			)
	public Response creerModeFinancement(
						@RequestBody(
							description = "Créer un mode fiancement", 
							required = true,
							content = @Content(schema = @Schema(implementation = ModeFinancement.class)))
						@Context HttpServletRequest servletRequest,
						ModeFinancement entity) {
		
		
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
		
		CreerModifierModeFinancementCtl creerModifierModeFinancement = new CreerModifierModeFinancementCtl();
		
		ProgrammeImmobilier programme = entity.getProgrammeImmobilier();
		
		ModeFinancement rtn = null ;
		
		entity = creerModifierModeFinancement.retournerModeFinancementEnFonctionDuType(entity);
		
		entity.setProgrammeImmobilier(programme);

		try {
		
			
			// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
			
			rtn =  creerModifierModeFinancementCtl
					 .creerModifierUnModeFinancement(
						  entity, mustUpdateExistingNew,  namedGraph, 
						  isFetchGraph, locale,loggedInUser, msgList) ;
			
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
		
				rtn = null ;
				
		}
		

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	
	}
	
/************************************ Modification d'un mode de financement *******************************************************/
	
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Modification n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Modification  effectuée",
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
			summary = "Modification d'un mode de financement",
			description = "Modification d'un mode de financement"
			)
	public Response modifierModeFinancement(
						@RequestBody(
							description = "Modifier un mode fiancement", 
							required = true,
							content = @Content(schema = @Schema(implementation = ModeFinancement.class)))
						@Context HttpServletRequest servletRequest,
						ModeFinancement entity) {
		
		
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
		
		CreerModifierModeFinancementCtl creerModifierModeFinancement = new CreerModifierModeFinancementCtl();
		
		ProgrammeImmobilier programme = entity.getProgrammeImmobilier();
		
		ModeFinancement rtn = null ;
		
		entity = creerModifierModeFinancement.retournerModeFinancementEnFonctionDuType(entity);
		
		entity.setProgrammeImmobilier(programme);

		try {
		
			
			// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
			
			rtn =  creerModifierModeFinancementCtl
					 .creerModifierUnModeFinancement(
						  entity, mustUpdateExistingNew,  namedGraph, 
						  isFetchGraph, locale,loggedInUser, msgList) ;
			
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
		
				rtn = null ;
				
		}
		

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	
	}
	

	
/************************************Recherche Mode de financements par programme immobilier **************************************/


	@Secured
	@GET  
	@Path("/programmeImmobilier/{idProgrammeImmobilier}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun mode de financement trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "mode  logement(s) trouvé(s)",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = ModeFinancement.class) )
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Recherche  de mode de financements par programme immobilier",
			description = "Recherche  de mode de financements par programme immobilier"
			)
	public Response rechercherModeFinancementParProgrammeImmobilier(
			 @Parameter(
			            description = "Identifiant Id Unique au programme immobilier",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idProgrammeImmobilier") String idProgrammeImmobilier){
		
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		SessionBag sessionBag = sessioncrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;

		boolean estEjbException = false ;
		
		Response.Status status = Response.Status.OK ;
		
		List<ModeFinancement> rtnList = null;
		
		User loggedInUser = new User() ;
		
		// appel de la method du controleur

		try {
			
			
			rtnList =  rechercherModeFinancementCtl
					        .rechercherModeFinancementListParProgrammeImmobilier(idProgrammeImmobilier, true, null, true, locale, loggedInUser, msgList) ;						
		
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
	

/************************************Recherche Mode de financements par programme efi**************************************/


	@Secured
	@GET  
	@Path("{idEfi}/efi")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun mode de financement trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "mode  logement(s) trouvé(s)",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = ModeFinancement.class) )
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Recherche  de mode de financements par efi",
			description = "Recherche  de mode de financements par efi"
			)
	public Response rechercherModeFinancementParEfi(
			 @Parameter(
			            description = "Identifiant Id Unique à l'efi",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idEfi") String idEfi){
		
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		SessionBag sessionBag = sessioncrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean estEjbException = false ;
		
		Response.Status status = Response.Status.OK ;
		
		List<ModeFinancement> rtnList = null;
		
		User loggedInUser = new User() ;
		
		// appel de la method du controleur

		try {
			
			
			rtnList =  rechercherModeFinancementCtl
					        .rechercherModeFinancementListParEfi(idEfi, true, null, true, locale, loggedInUser, msgList) ;						
		
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
	

/************************************Suppresion d'un mode de financement***************************/
	
	@Secured
	@DELETE
	@Path("{idModeFinancement}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La suppression  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La supression effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON//,
			       // schema = @Schema(implementation = ProduitLogement.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "La suppression d'un mode de financement",
			description = "La suppression d'un mode de financement"
			)
	public Response supprimerUnModeFinancement(
			@Parameter(
		            description = "Identifiant Id Unique au mode financement",
		            required = true,
		            example = "1551176445313ABBB1233AZN2331",
		            schema = @Schema(type = "string")
		       )
		 @Context HttpServletRequest servletRequest,
		 @PathParam("idModeFinancement") String idModeFinancement) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessioncrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = "graph.produitLogement.id.estActive" ;
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
			
			// Appel de  la methode  validerEtSupprimier
			
			rtn =  supprimerModeFinancementCtl
					.supprimerUnModeFinancement(idModeFinancement, mustUpdateExistingNew, namedGraph, 
							          isFetchGraph, locale, loggedInUser, msgList)	;				
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
