package com.siliconwise.common.reference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.siliconwise.common.AppUtil;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionDAO;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogement;
import com.siliconwise.mmc.produitlogement.RechercherProduitLogementCtlInterface;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
@Path("/references")
@OpenAPIDefinition(info = @Info(title = "Informations sur les references: ", version = "1.0", description = "Informations sur les references", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class ReferenceRest implements Serializable {

	
			private static final long serialVersionUID = 1L;
		
			@Inject private ReferenceCtlInterface referenceCtlInterface ;
			@Inject private SessionDAO sessionDAO;
			@Inject private RestResponseCtrl responseCtrl;
			
			//APi pour la recherche de reference par Designation
			@GET  
			@Path("{designation}")
			@Produces(MediaType.APPLICATION_JSON)
			@Consumes(MediaType.APPLICATION_JSON)
			@ApiResponse(
					responseCode = "204",
					description = "Reference non trouvée"
			        )
			@ApiResponse(
					responseCode = "200",
					description = "Reference trouvée correspondant à la designation",
					content = @Content(
					        mediaType = MediaType.APPLICATION_JSON,
					        schema = @Schema(implementation = Reference.class)
					                )
			        )
			@ApiResponse(
					responseCode = "500",
					description = "Erreur côté Serveur"
			        )
			@Operation(
					summary = "Rechercher une Reference",
					description = "Rechercher une Reference"
					)
			public Response trouverReferenceParDesignationReference(
					 @Parameter(
					            description = "Designation  Unique à la reference",
					            required = true,
					            example = "Villa",
					            schema = @Schema(type = "string")
					       )
					 @Context HttpServletRequest servletRequest,
					 @PathParam("designation") String designation){
				
				
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
				
				SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
				Locale locale = SessionUtil.getLocale(sessionBag) ;
				//Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
				// appel de la method du controleur

				boolean estEjbException = false ;
				Response.Status status = Response.Status.OK ;
				
				Reference rtn = null;

				try {
					
					//Appel de la methode trouverReferenceParDesignationReference
					rtn =  referenceCtlInterface.trouverReferenceParDesignationReference(designation);
				
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
			
			
			
			/**
			 * @param servletRequest
			 * @return toutes les propriétés des produits logements
			 */
			@GET  
			@Path("/parfamille/{idFamille}")
			@Produces(MediaType.APPLICATION_JSON)
			@ApiResponse(
					responseCode = "204",
					description = "references  non trouvées"
			        )
			@ApiResponse(
					responseCode = "200",
					description = "references trouvées",
					content = @Content(
					        mediaType = MediaType.APPLICATION_JSON,
					        array = @ArraySchema ( schema = @Schema(implementation = Reference.class))
					                )
			        )
			@ApiResponse(
					responseCode = "500",
					description = "Erreur côté Serveur"
			        )
			@Operation(
					summary = "Retourner la liste de references par famille",
					description = "Retourner la liste de references par famille"
					)
		public Response trouverToutesLesReferenceParIdFamille(
					@Parameter(
			            description = "Id unique par Famille reference ",
			            required = true,
			            example = "Villa",
			            schema = @Schema(type = "string")
							)
					@Context HttpServletRequest servletRequest ,
					@PathParam("idFamille") String idFamille){
						
						List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
						SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
						Locale locale = SessionUtil.getLocale(sessionBag) ;
						boolean estEjbException = false ;
						Response.Status status = Response.Status.OK ;
						
						List<Reference> rtnList = null;
				
						try {
							
							rtnList =  referenceCtlInterface.trouverToutesLesReferenceParIdFamille(idFamille, msgList, locale);
						
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
