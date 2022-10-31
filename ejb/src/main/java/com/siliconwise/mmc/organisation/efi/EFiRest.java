package com.siliconwise.mmc.organisation.efi;

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
@Path("/efis")
@OpenAPIDefinition(info = @Info(title = "Creation des modes financements: ", version = "1.0", description = "Creation d'un mode financement", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class EFiRest implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
//	@Inject private SessionDAO sessionDAO ;
	@Inject private SessionCtrl sessionCtrl ;
	@Inject private RestResponseCtrl responseCtrl ;
	@Inject private EfiCtlInterface  EFiCtl ;
	@Inject private CreerModifierUnEfiCtlInterface creerModifierUnEfiCtl ;
	@Inject private ActiverDesactiverUnEfiCtlInterface activerDesactiverUnEfiCtl ;
	@Inject private SupprimerUnEfiCtlInterface supprimerUnEfiCtl ;
	@Inject private RechercherUnEFiCtlInterface rechercherUnEFiCtl ;
	@Inject private EfiCtlInterface efiCtl ;
	
	
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
				description = "Liste des efis ",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        array = @ArraySchema ( schema = @Schema(implementation = EFi.class))
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Liste des Efis",
				description = "Liste des EFis"
				)
		public Response tousLesProgrammesImmobiliers(
							@RequestBody(
								description = "Liste des Efis", 
								required = true
								)
							@Context HttpServletRequest servletRequest) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag)  ;

			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			List<EFi> rtn = null  ;
			
		
			try {
				
				
				// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
				
				rtn = EFiCtl.tousLesEfis()  ;
				
				
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
		

/********************************************Creation d'Efi *****************************************************************************************************/

		/**
		 * Creation d'un  efi
		 * @param servletRequest
		 * @param Promoteur
		 * @return
		 */
		@POST
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Creation de l'efi non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "L'efir a été bien crée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = EFi.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
	public Response creerUnEfi( 
				@RequestBody(
						description = "New Efi", 
						required = true,
						content = @Content(schema = @Schema(implementation = EFi.class)))
				@Context HttpServletRequest servletRequest,
				EFi entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag)  ;
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			EFi rtn = null ;
			
			try {
				
				 rtn = creerModifierUnEfiCtl
						   .creerModifierUnEfi(entity, true, null, true, locale, loggedInUser, msgList) ;
			
			 } catch (Exception ex) {
					
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
	

/********************************************Modifier un Efi **************************************************************************/
		
		/**
		 * modifier d'un  efi
		 * @param servletRequest
		 * @param Promoteur
		 * @return
		 */
		@PUT
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Modification de l'efi non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "L'efir a été bien modifié",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = EFi.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
	public Response moodifierUnEfi( 
				@RequestBody(
						description = "New Efi", 
						required = true,
						content = @Content(schema = @Schema(implementation = EFi.class)))
				@Context HttpServletRequest servletRequest,
				EFi entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag)  ;
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			EFi rtn = null ;
			
			try {
				
				 rtn = creerModifierUnEfiCtl
						   .creerModifierUnEfi(entity, true, null, true, locale, loggedInUser, msgList) ;
			
			 } catch (Exception ex) {
					
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

		

/********************************************Modification d'un compte Efi par un Admin Organisation **************************************************************************/
		
		/**
		 * modifier d'un compte efi
		 * @param servletRequest
		 * @param Promoteur
		 * @return
		 */
		@PUT
		@Path("modifierUnCompteEfi")
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Modification du compte Efi non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "Modification du compte Efi  effectuée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = EFi.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
	public Response moodifierUnCompteEfi( 
				@RequestBody(
						description = "New Efi", 
						required = true,
						content = @Content(schema = @Schema(implementation = EFi.class)))
				@Context HttpServletRequest servletRequest,
				EFi entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag)  ;
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			EFi rtn = null ;
			
			try {
				
				 rtn = creerModifierUnEfiCtl
						.modifierUnCompteEFi(entity, true, null, true, locale, loggedInUser, msgList);
			
			 } catch (Exception ex) {
					
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

/********************************** Demande de creation d'un compte promoteur**************************/
		/**
		 * Demande de creation  d'un compte  efi
		 * @param servletRequest
		 * @param Promoteur
		 * @return
		 */
		@POST
		@Path("demandeCreationCompteEfi")
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Demande de creation du compte efi non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "La demande de creation du compte efi  a été bien créée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = EFi.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
		public Response demanderCreationComptePromoteur( 
				@RequestBody(
				    description = "Demande de creation d'un compte EFi", 
					required = true,
					content = @Content(schema = @Schema(implementation = EFi.class)))
				@Context HttpServletRequest servletRequest,
				EFi entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag)  ;
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			EFi rtn = null ;
			
			try {
				
				 rtn = efiCtl
						.demandeCreationUnCompteEFi(entity, true, null, true, locale, loggedInUser, msgList);
						  
			
			 } catch (Exception ex) {
					
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
	
		
/********************************** Activer un efi **********************************************/
		
		@Secured
		@PUT
		@Path("{idEfi}/activer")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "L'activation  n'a  pas été effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Activation effectuée",
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
				summary = "Activation d'un efi",
				description = "Activation d'un efi"
				)
		public Response activerUnEfi(
							@RequestBody(
								description = "Activer un efi", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idEfi") String idEfi) {
			
			//Declaration des variables 
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
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
			
				rtn =  activerDesactiverUnEfiCtl
							.activerUnEfi(idEfi, mustUpdateExistingNew,namedGraph,
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
	
/***********************************Confirmation de creation d'un compte Efi ***********/

		
		@Secured
		@PUT
		@Path("{idEfi}/confirmerCreationCompteEfi")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "La confirmation de la creation du compte Efi  n'a  pas été effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "La confirmation de la creation du compte Efi a été effectuée",
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
				summary = "Confirmation de creation d'un compte Efi",
				description = "Confirmation de creation d'un compte Efi"
				)
		public Response cofirmerCreationCompteEFi(
							@RequestBody(
								description = "Confirmation de creation d'un compte Efi", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idEfi") String idEfi) {
			
			//Declaration des variables 
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
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
			
				rtn =  efiCtl.confirmerCreationUnCompteEfi(idEfi, mustUpdateExistingNew, 
						            namedGraph, isFetchGraph, locale, loggedInUser, msgList);
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
		
		
/***********************************Desactiver un Efi **********************************/

		@Secured
		@PUT
		@Path("{idEfi}/desactiver")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "La desactivation  n'a  pas été effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "La desactivation effectuée",
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
				summary = "Desactivation d'un efi",
				description = "Desactivation d'un efi"
				)
		public Response desactiverUnEfi(
							@RequestBody(
								description = "Desactiver un efi", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idEfi") String idEfi) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
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
			
				rtn =  activerDesactiverUnEfiCtl
							.desactiverUnEfi(idEfi, mustUpdateExistingNew,namedGraph,
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

/************************************Suppresion d'un EFi *************************************************************/

		@Secured
		@DELETE
		@Path("{idEfi}")
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
				        mediaType = MediaType.APPLICATION_JSON,
				        schema = @Schema(type = "boolean")
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "La suppression d'un Efi",
				description = "La suppression d'un Efi"
				)
		public Response supprimerUnProduitLogement(
				@Parameter(
			            description = "Identifiant Id Unique à l'Efi",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idEfi") String idEfi) {
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
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
				
				rtn =  supprimerUnEfiCtl
						 .supprimer(idEfi, mustUpdateExistingNew, 
						    namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
			}
			catch(Exception ex) {
			
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				
				logger.info("529_MessageTranslationUtil ::  msg="+msg);
				
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
					status = Response.Status.INTERNAL_SERVER_ERROR ;
					estEjbException = true ;
			
					rtn = false ;
					
			}
			

			return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
			
			
		}

/*******************************************Recherche d'un Efi par son Id *********************************************/

		
		@Secured
		@GET  
		@Path("{idEfi}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Aucun efi trouvé"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "EFi trouvé",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        schema = @Schema(implementation = EFi.class)
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Recherche d'Efi par Id",
				description = "Rechercher un Efi pour amener d'autres operations"
				)
		public Response rechercherUnProduitLogementParId(
				 @Parameter(
				            description = "Identifiant Id Unique au Efi",
				            required = true,
				            example = "1551176445313ABBB1233AZN2331",
				            schema = @Schema(type = "string")
				       )
				 @Context HttpServletRequest servletRequest,
				 @PathParam("idEfi") String idEfi){
			
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			EFi rtn = null;
			
			// appel de la method du controleur
			
			try {
				
				
				rtn =  rechercherUnEFiCtl
						 .rechercherUnEFiParId(idEfi, null, true, EFi.class);
			
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

