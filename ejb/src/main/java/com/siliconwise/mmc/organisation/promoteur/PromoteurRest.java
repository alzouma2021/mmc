package com.siliconwise.mmc.organisation.promoteur;

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
import com.siliconwise.common.Ville;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.Secured;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
/**
 * 
 * @author	Alzouma Moussa Mahamadou
 * @date		
 *
 */
@Stateless
@Path("promoteurs")
@OpenAPIDefinition(info = @Info(title = "Gestion des APis de Produit Logement: Les Types DATE , DATETIME et TIME sont au format ISO", version = "1.0", description = "Compte Promoteur Service APIs", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class PromoteurRest implements Serializable {
	

		private static final long serialVersionUID = 1L;

		@Inject private	CreerModifierUnPromoteurCtlInterface creerModifierUnPromoteurCtl;
		
		@Inject private ActiverDesactiverUnPromoteurCtlInterface activerDesactiverUnPromoteurCtl ;
		
		@Inject private SupprimerUnPromoteurCtlInterface SupprimerUnPromoteurCtl ;
		
		@Inject private RechercherUnPromoteurCtlInterface rechercherUnPromoteurCtl ;
		
		@Inject private PromoteurCtlInterface promoteurCtl ; 
		
		//@Inject private SessionDAO sessionDAO ;
		
		@Inject private SessionCtrl  sessionCtrl ;
		
		@Inject private RestResponseCtrl responseCtrl ;
		
		private static transient Logger logger = LoggerFactory.getLogger(PromoteurRest.class) ;
		
		/**
		 * Creation d'un  promoteur
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
			    description = "Creation de promoteur non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "Le promoteur a été bien crée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = Promoteur.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
		public Response creerUnPromoteur( 
				@RequestBody(
						description = "New Promoteur", 
						required = true,
						content = @Content(schema = @Schema(implementation = Promoteur.class)))
				@Context HttpServletRequest servletRequest,
				Promoteur entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			Promoteur rtn = null ;
			
			try {
				
				 rtn = creerModifierUnPromoteurCtl
						   .creerModifierUnPromoteur(entity, true, null, true, locale, loggedInUser, msgList) ;
			
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
	
		
/*******************************Modifier un promoteur *************************************************************/
		
		/**
		 * Modification d'un  promoteur
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
			    description = "La modification du compte promoteur non effectuée"
	        )
		@ApiResponse(
			    responseCode = "200",
			    description = "La modification du compte promoteur a été bien effectuée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = Promoteur.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
		public Response modifierUnPromoteur( 
				@RequestBody(
						description = "New Promoteur", 
						required = true,
						content = @Content(schema = @Schema(implementation = Promoteur.class)))
				@Context HttpServletRequest servletRequest,
				Promoteur entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			Promoteur rtn = null ;
			
			try {
				
				 rtn = creerModifierUnPromoteurCtl
						   .creerModifierUnPromoteur(entity, true, null, true, locale, loggedInUser, msgList) ;
			
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
		
		
/*********************************  Modification d'un compte promoteur ********************************/
		/**
		 * Modification d'un  promoteur
		 * @param servletRequest
		 * @param Promoteur
		 * @return
		 */
		@PUT
		@Path("modifierUnComptePromoteur")
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Modification du compte  promoteur non effectuée"
	        )
		@ApiResponse(
			    responseCode = "200",
			    description = "Modification du compte promoteur a été bien effectuée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = Promoteur.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
		public Response modifierUnComptePromoteur( 
				@RequestBody(
						description = "New Promoteur", 
						required = true,
						content = @Content(schema = @Schema(implementation = Promoteur.class)))
				@Context HttpServletRequest servletRequest,
				Promoteur entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			Promoteur rtn = null ;
			
			try {
				
				 rtn = creerModifierUnPromoteurCtl
						 .modifierUnComptePromoteur(entity, true, null, true, locale, loggedInUser, msgList);
			
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
		 * Demande de creation  d'un compte  promoteur
		 * @param servletRequest
		 * @param Promoteur
		 * @return
		 */
		@POST
		@Path("demandeCreationComptePromoteur")
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Demande de creation du compte promoteur non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "La demande de creation du compte promoteur  a été bien créée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = Promoteur.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
		public Response demanderCreationComptePromoteur( 
				@RequestBody(
				    description = "Demande de creation d'un compte promoteur", 
					required = true,
					content = @Content(schema = @Schema(implementation = Promoteur.class)))
				@Context HttpServletRequest servletRequest,
				Promoteur entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User();

			Promoteur rtn = null ;
			
			try {
				
				 rtn = promoteurCtl
						 .demandeCreationUnComptePromoteur(entity, true, null, true, locale, loggedInUser, msgList) ;
			
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
	
/***********************************Confirmation de creation d'un compte Promoteur ***********/

		@Secured
		@PUT
		@Path("{idPromoteur}/confirmerCreationComptePromoteur")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "La confirmation de la creation du compte promoteur  n'a  pas été effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "La confirmation de la creation du compte promoteur a été effectuée",
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
				summary = "Confirmation de creation d'un compte promoteur",
				description = "Confirmation de creation d'un compte promoteur"
				)
		public Response cofirmerCreationCompteEFi(
							@RequestBody(
								description = "Confirmation de creation d'un compte promoteur", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idPromoteur") String idPromoteur) {
			
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
			
				rtn =  promoteurCtl.confirmerCreationUnComptePromoteur(idPromoteur, mustUpdateExistingNew, 
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
		
		
/********************************** Activer un promoteur **********************************************/
		
		@Secured
		@PUT
		@Path("{idPromoteur}/activer")
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
				summary = "Activation d'un promoteur",
				description = "Activation d'un promoteur"
				)
		public Response activerUnPromoteur(
							@RequestBody(
								description = "Activer un promoteur", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idPromoteur") String idPromoteur) {
			
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
			
				rtn =  activerDesactiverUnPromoteurCtl
							.activerUnPromoteur(idPromoteur, mustUpdateExistingNew,namedGraph,
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
		
		
/***********************************Desactiver un promoteur immobilier **********************************/

		@Secured
		@PUT
		@Path("{idPromoteur}/desactiver")
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
				summary = "Desactivation d'un promoteur",
				description = "Desactivation d'un promoteur"
				)
		public Response desactiverUnPromoteur(
							@RequestBody(
								description = "Desactiver un promoteur", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idPromoteur") String idPromoteur) {
			
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
			
				rtn =  activerDesactiverUnPromoteurCtl
							.desactiverUnPromoteur(idPromoteur, mustUpdateExistingNew,namedGraph,
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
		
		
/********************************Suppression d'un promoteur *****************************************************************/
		

		@Secured
		@DELETE
		@Path("{idPromoteur}")
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
				summary = "La suppression d'un promoteur",
				description = "La suppression d'un promoteur"
				)
		public Response supprimerUnProduitLogement(
				@Parameter(
			            description = "Identifiant Id Unique au promoteur",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idPromoteur") String idPromoteur) {
			
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
				
				
				rtn =  SupprimerUnPromoteurCtl
						 .supprimer(idPromoteur, mustUpdateExistingNew, 
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


/*******************************************Recherche d'un promoteur par son Id *********************************************/

		
		@Secured
		@GET  
		@Path("{idPromoteur}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Aucun promoteur trouvét"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Promoteur trouvé",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        schema = @Schema(implementation = Promoteur.class)
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Recherche de promoteurpar Id",
				description = "Rechercher un promoteur pour amener d'autres operations"
				)
		public Response rechercherUnProduitLogementParId(
				 @Parameter(
				            description = "Identifiant Id Unique au promoteurt",
				            required = true,
				            example = "1551176445313ABBB1233AZN2331",
				            schema = @Schema(type = "string")
				       )
				 @Context HttpServletRequest servletRequest,
				 @PathParam("idPromoteur") String idPromoteur){
			
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			Promoteur rtn = null;
			
			// appel de la method du controleur
			
			try {
				
				
				rtn =  rechercherUnPromoteurCtl
						 .rechercherUnPromoteurParId(idPromoteur, null, true, Promoteur.class);
			
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
		
		
		
/***********************************************Liste de promoteurs **********************************************************/
		

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
				description = "Liste de promoteurs",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        array = @ArraySchema ( schema = @Schema(implementation = Promoteur.class))
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Liste de promoteurs",
				description = "Liste de promoteurs"
				)
		public Response tousLesPromoteurs(
							@RequestBody(
								description = "Liste de promoteurs", 
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
			
			List<Promoteur> rtn = null  ;
			

			try {
				
				// Appel de  la methode  toutesLesVilles
				
				rtn =  promoteurCtl.tousLesPromoteurs() ;
				
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




}
