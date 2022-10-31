package com.siliconwise.mmc.user;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.AppUtil;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.Secured;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.security.SessionCtrl;
import com.siliconwise.mmc.security.SessionUtil;

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
@Path("users")
@OpenAPIDefinition(info = @Info(title = "Gestion des APis de Produit Logement: Les Types DATE , DATETIME et TIME sont au format ISO", version = "1.0", description = "Compte Utilisateur Service APIs", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class UserRest implements Serializable {
	

		private static final long serialVersionUID = 1L;

		@Inject private	CreerModifierUnCompteUserCtlInterface creerModifierUnCompteUserCtl;
		//@Inject private SessionDAO sessionDAO;
		@Inject private SessionCtrl sessionCtrl ;
		@Inject private RestResponseCtrl responseCtrl;
		@Inject private ActiverDesactiverUnCompteUserCtlInterface activerDesactiverUnCompteUserCtl ;
		@Inject private RechercherUnUserCtlInterface rechercherUnUserCtl;
		@Inject private SupprimerUnUserCtlInterface supprimerUnUserCtl ;
		@Inject private CompteUserCtlInterface compteUserCtl ;
		
		private static transient Logger logger = LoggerFactory.getLogger(UserRest.class) ;
		
		
		
		/**
		 * Creation d'un compte User
		 * @param servletRequest
		 * @param user
		 * @return
		 */
		@POST
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Creation de compte non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "Le compte a été bien crée",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = User.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
		public Response creerModifierUser( 
				@RequestBody(
						description = "New article", 
						required = true,
						content = @Content(schema = @Schema(implementation = User.class)))
				@Context HttpServletRequest servletRequest,
				User entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User() ;

			User rtn = null ;
			
			try {
				
				 rtn =  creerModifierUnCompteUserCtl
						 .CreerModifierUnCompteUser(entity, true, null, true, locale, loggedInUser, msgList);
			
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

/********************************** Modifier un compte user *********************************************/

		
		/**
		 * Modification d'un compte User
		 * @param servletRequest
		 * @param user
		 * @return
		 */
		@PUT
		@Secured
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
			    responseCode = "204",
			    description = "Modification du compte user non effectuée"
	        )
		@ApiResponse(
			    responseCode = "201",
			    description = "Le compte user a été bien modifié",
			    content = @Content(
			              mediaType = MediaType.APPLICATION_JSON,
			              schema = @Schema(implementation = User.class)
			                )
	        )
		@ApiResponse(
			    responseCode = "500",
			    description = "Erreur côté Serveur"
	        )
		public Response ModifierUser( 
				@RequestBody(
						description = "New article", 
						required = true,
						content = @Content(schema = @Schema(implementation = User.class)))
				@Context HttpServletRequest servletRequest,
				User entity ) {
		
			List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User loggedInUser = new User() ;

			User rtn = null ;
			
			try {
				
				 rtn =  creerModifierUnCompteUserCtl
						 .CreerModifierUnCompteUser(entity, true, null, true, locale, loggedInUser, msgList);
			
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

		
/********************************** Activer un compte user **********************************************/
		
		@Secured
		@PUT
		@Path("{idUser}/activer")
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
				summary = "Activation d'un compte user",
				description = "Activation d'un compte user"
				)
		public Response activerUnCompteUser(
							@RequestBody(
								description = "Activer un compte user", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idUser") String idUser) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			boolean mustUpdateExistingNew = true ;
			String namedGraph = null ;
			boolean isFetchGraph = true ;
			
			boolean compteUserHasPassWord = true ;
			
		    User loggedInUser = new User();
		    loggedInUser.setId("11aabbbdfgg");
		    loggedInUser.setNom("alzouma");
		    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    loggedInUser.setMotDePasse("alzouma2021");

			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			boolean rtn = false  ;
			

			try {
			
				rtn =  activerDesactiverUnCompteUserCtl
							.activerUnCompteUser(idUser, mustUpdateExistingNew, compteUserHasPassWord, 
									            namedGraph,isFetchGraph, locale, loggedInUser, msgList) ;
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
		
/***********************************Confirmer la creation d'un compte user ********************************************/


		@Secured
		@PUT
		@Path("{code}/confirmer")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "La confirmation non effectuée"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "La confirmation effectuée",
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
				summary = "Confirmer la creation d'un compte user",
				description = "Confirmer la creation d'un compte user"
				)
		public Response confirmerUnCompteUser(
							@RequestBody(
								description = "Confirmer la creation d'un compte user", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("code") String code) {
			
			//Declaration des variables 
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			boolean mustUpdateExistingNew = true ;
			String namedGraph = null ;
			boolean isFetchGraph = true ;
			
			boolean compteUserHasPassWord = true ;
			
		    User loggedInUser = new User();
		    loggedInUser.setId("11aabbbdfgg");
		    loggedInUser.setNom("alzouma");
		    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    loggedInUser.setMotDePasse("alzouma2021");

			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			boolean rtn = false  ;
			

			try {
				
				logger.info("_367 confirmerUnCompteUser debut Rest"); //TODO A effacer
			
				rtn =  compteUserCtl.confirmerUnCompteUser(code, mustUpdateExistingNew, 
						      						compteUserHasPassWord,  namedGraph,
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
		
/***********************************Desactiver un compte user**********************************************************/

		@Secured
		@PUT
		@Path("{idUser}/desactiver")
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
				summary = "Desactivation d'un compte user",
				description = "Desactivation d'un compte user"
				)
		public Response desactiverUnPromoteur(
							@RequestBody(
								description = "Desactiver un compte user", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("idUser") String idUser) {
			
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
			
				rtn =  activerDesactiverUnCompteUserCtl
							.desactiverUnCompteUser(idUser, mustUpdateExistingNew,namedGraph,
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
		
/********************************************Recherche de User par son Id ************************************************/

		
		@Secured
		@GET  
		@Path("{idUser}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Aucun user trouvé"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "User trouvé",
				content = @Content(
				        mediaType = MediaType.APPLICATION_JSON,
				        schema = @Schema(implementation = User.class)
				                )
		        )
		@ApiResponse(
				responseCode = "500",
				description = "Erreur côté Serveur"
		        )
		@Operation(
				summary = "Recherche de promoteurpar Id",
				description = "Rechercher un user"
				)
		public Response rechercherUnUserParId(
				 @Parameter(
				            description = "Identifiant Id Unique au user",
				            required = true,
				            example = "1551176445313ABBB1233AZN2331",
				            schema = @Schema(type = "string")
				       )
				 @Context HttpServletRequest servletRequest,
				 @PathParam("idUser") String idUser){
			
			
			List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
			
			SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
			Locale locale =  SessionUtil.getLocale(sessionBag);
		
			boolean estEjbException = false ;
			Response.Status status = Response.Status.OK ;
			
			User rtn = null;
			
			// appel de la method du controleur
			
			try {
				
				
				rtn =  rechercherUnUserCtl
						 .rechercherUnUserParId(idUser, null, true, User.class);
			
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

		
/**********************************Régénérer le mot de passe d'un compte user **********************************************/
		
		@Secured
		@PUT
		@Path("{email}/genererMotDePasse")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Le mot de passe n'a pas été régénéré"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Le mot de passe a été régénéré",
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
				summary = "Régénération du mot de passe d'un compte user",
				description = "Régénération du mot de passe d'un compte user"
				)
		public Response regenererMotDePasseCompteUser(
							@RequestBody(
								description = "Régénérer du mot de passe d'un compte user", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
							@PathParam("email") String email) {
			
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
			
				rtn = compteUserCtl.regenererUnMotDePasseCompteUser(email, mustUpdateExistingNew, 
						                                 namedGraph, isFetchGraph, locale, msgList);
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
		

/**********************************Modification le mot de passe d'un compte user **********************************************/
		
		@Secured
		@PUT
		@Path("modifierMotDePasseCompteUser")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@ApiResponse(
				responseCode = "204",
				description = "Le mot de passe n'a pas été modifié"
		        )
		@ApiResponse(
				responseCode = "200",
				description = "Le mot de passe a été modifié",
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
				summary = "Modification du mot de passe d'un compte user",
				description = "Modification du mot de passe d'un compte user"
				)
		public Response ModifierMotDePasseCompteUser(
							@RequestBody(
								description = "Modifier du mot de passe d'un compte user", 
								required = true,
								content = @Content(schema = @Schema(type = "string")))
							@Context HttpServletRequest servletRequest,
						    @QueryParam("currentPassWord") String currentPassWord,
							@QueryParam("newPassWord") String newPassWord) {
			
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
			
				rtn = creerModifierUnCompteUserCtl
						 .modifierUnMotDePasseCompteUser(currentPassWord, newPassWord, sessionBag,
								   mustUpdateExistingNew, namedGraph, isFetchGraph, locale, msgList);
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
		
/********************************Suppression d'un user *****************************************************************/
		

		@Secured
		@DELETE
		@Path("{idUser}")
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
				summary = "La suppression d'un compte user",
				description = "La suppression d'un compte user"
				)
		public Response supprimerUnProduitLogement(
				@Parameter(
			            description = "Identifiant Id Unique au compte user",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idUser") String idUser) {
			
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
				
				rtn =  supprimerUnUserCtl.supprimer(idUser, mustUpdateExistingNew, 
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
