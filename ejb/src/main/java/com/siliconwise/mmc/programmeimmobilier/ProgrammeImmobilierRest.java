package com.siliconwise.mmc.programmeimmobilier;

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
@Path("/programmeimmobiliers")
@OpenAPIDefinition(info = @Info(title = "Creation d'un programme immobilier: ", version = "1.0", description = "Creation d'un programme immobilier", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class ProgrammeImmobilierRest implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
		
		@Inject private CreerModifierUnProgrammeImmobilierCtlInterface  creerModifierUnProgrammeImmobilierCtl ;
		@Inject private RechercherProgrammeImmobilierCtlInterface rechercherProgrammeImmobilierCtl   ;
		//@Inject private SessionDAO sessionDAO ;
		@Inject private SessionCtrl sessionCtrl ;
		@Inject private RestResponseCtrl responseCtrl ;
		@Inject private ValiderUnProgrammeImmobilierCtlInterface validerUnProgrammeImmobilierCtl ;
		@Inject private SupprimerUnProgrammeImmobilierCtlInterface supprimerUnProgrammeImmobilierCtl ;
		
		
		@Secured
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Creation  n'a  pas été effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Création effectuée",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        schema = @Schema(implementation = ProgrammeImmobilierTransfert.class)
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Création  d'un programme immobilier",
				description = "Création  d'un programme immobilier"
				)
		public Response creerProgrammeImmobilier(
							@RequestBody(
								description = "Créer  un programme immobilier", 
								required = true,
								content = @Content(schema = @Schema(implementation = ProgrammeImmobilierTransfert.class)))
							@Context HttpServletRequest servletRequest,
							ProgrammeImmobilierTransfert entity) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
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
			
			Boolean estCreation = true ;
			
			ProgrammeImmobilier rtn = null  ;
			
			
		//	logger.info("111_CreerModifierUnProgrammeImmobilier :: ProgrammeImmobilier="+entity.getCode());

			try {
				
				// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
				
				rtn =  creerModifierUnProgrammeImmobilierCtl
						.creerModifierUnProgrammeImmobilier(entity, mustUpdateExistingNew, 
								namedGraph, isFetchGraph, locale, 
								loggedInUser, msgList,estCreation) ;
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
		
/***********************************Modification**********************************************/
		
		
		@Secured
		@PUT
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Modification  n'a  pas été effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Modification effectuée",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        schema = @Schema(implementation = ProgrammeImmobilier.class)
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Modification  d'un programme immobilier",
				description = "Modification  d'un programme immobilier"
				)
		public Response modifierUnProgrammeImmobilier(
							@RequestBody(
								description = "Modifier  un programme immobilier", 
								required = true,
								content = @Content(schema = @Schema(implementation = ProgrammeImmobilierTransfert.class)))
							@Context HttpServletRequest servletRequest,
							ProgrammeImmobilierTransfert entity) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
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
			
			Boolean estCreation = false ;
			
			ProgrammeImmobilier rtn = null  ;
			
			//logger.info("111_CreerModifierUnProgrammeImmobilier :: ProgrammeImmobilier="+entity.getCode());

			try {
				
				// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
				
				rtn =  creerModifierUnProgrammeImmobilierCtl
						.creerModifierUnProgrammeImmobilier(entity, mustUpdateExistingNew, 
								namedGraph, isFetchGraph, locale, 
								loggedInUser, msgList, estCreation) ;
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
		
/***********************************************************Valider Un Programme Immobilier*****************************************************************/

		@Secured
		@PUT
		@Path("{idProgrammeImmobilier}/valider")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "La validation  n'a  pas été effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Validation effectuée",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        schema = @Schema(type = "boolean")
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Validation  d'un programme immobilier",
				description = "Validation  d'un programme immobilier"
				)
		public Response validerUnProgrammeImmobilierParPromoteur(
							@RequestBody(
								description = "Valider  un programme immobilier", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idProgrammeImmobilier") String idProgrammeImmobilier) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			
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
				
				// Appel de  la methode  valider un programmeimmobilier
				
				rtn =  validerUnProgrammeImmobilierCtl
							.validerUnProgrammeImmobilier(
							  idProgrammeImmobilier, mustUpdateExistingNew,namedGraph, 
							  isFetchGraph, locale, loggedInUser, msgList) ;
				
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
		

/********************************La liste de programmes immobiliers ************************/
		
		@Secured
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Echec de l'operation"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Liste de programmes immobiliers",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        array = @ArraySchema ( schema = @Schema(implementation = ProgrammeImmobilier.class))
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Liste de programmes immobiliers",
				description = "Liste de programmes immobiliers"
				)
		public Response tousLesProgrammesImmobiliers(
							@RequestBody(
								description = "Liste de programmes immobiliers", 
								required = true
								)
							@Context HttpServletRequest servletRequest) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
		    User loggedInUser = new User();
		    loggedInUser.setId("11aabbbdfgg");
		    loggedInUser.setNom("alzouma");
		    loggedInUser.setEmail("alzoumamoussa1NonLocalizedStatusMessage8@univmetiers.ci");
		    loggedInUser.setMotDePasse("alzouma2021");

			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			List<ProgrammeImmobilier> rtn = null  ;
			

			try {
				
				// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
				
				rtn =  rechercherProgrammeImmobilierCtl.tousLesProgrammesImmobiliers()  ;
				
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
		
/****************************************Recherche de programme immobiliers par promoteur **************************************/

		@Secured
		@GET  
		@Path("promoteur/{idPromoteur}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Aucun programme immobilier trouvé"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "programme(s) immobilier(s) trouvé(s)",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        array = @ArraySchema ( schema = @Schema(implementation = ProgrammeImmobilier.class) )
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Recherche  de programme immobilier(s) par promoteur",
				description = "Rechercher tous les programmes immobiliers appartenant à un promoteur donné "
				)
		public Response rechercherProduitLogementsParPromoteur(
				 @Parameter(
				            description = "Identifiant Id Unique au promoteur",
				            required = true,
				            example = "1551176445313ABBB1233AZN2331",
				            schema = @Schema(type = "string")
				       )
				 @Context HttpServletRequest servletRequest,
				 @PathParam("idPromoteur") String idPromoteur){
			
			
			//Initialisation des variables
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		

			boolean estEjbException = false ;
			
			User loggedInUser = new User();
			
			Response.Status status = Response.Status.OK ;
			
			List<ProgrammeImmobilier> rtnList = null;
			
			
			// appel de la method du controleur

			try {
				
				
				rtnList =  rechercherProgrammeImmobilierCtl
								.rechercherProgrammeImmobilierParPromoteur(
											idPromoteur, true, 
											null, true, locale, 
											loggedInUser, msgList) ;

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
		
		
		
/****************************************Suppression d'un produit logement *****************************************************/
		

		@Secured
		@DELETE
		@Path("{IdProgrammeImmobilier}")
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
				        mediaType = MediaType.APPLICATION_JSON/*,
				        schema = @Schema(implementation = ProgrammeImmobilier.class)*/
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "La suppression d'un programme immobilier",
				description = "La suppression d'un programme immobilier"
				)
		public Response supprimerUnProgrammeImmobilier(
				@Parameter(
			            description = "Identifiant Id Unique au programme immobilier",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("IdProgrammeImmobilier") String idProgrammeImmobilier) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			boolean mustUpdateExistingNew = true ;
			String namedGraph = "graph.programmeImmobilier.id" ;
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
				
				rtn =  supprimerUnProgrammeImmobilierCtl.supprimerUnProgrammeImmobilier(
									idProgrammeImmobilier, mustUpdateExistingNew, 
									namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
						
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
