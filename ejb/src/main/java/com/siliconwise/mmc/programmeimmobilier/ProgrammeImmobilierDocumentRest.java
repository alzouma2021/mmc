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
import com.siliconwise.common.document.Document;
import com.siliconwise.common.rest.RestResponseCtrl;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Stateless
@Path("/programmeImmobilierDocuments")
@OpenAPIDefinition(info = @Info(title = "Gestion de documents: ", version = "1.0", description = "Gestion de documents", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class ProgrammeImmobilierDocumentRest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject private ProgrammeImmobilierDocumentCtlInterface programmeImmobilierDocumentCtl ;
	//@Inject private SessionDAO sessionDAO;
	@Inject private SessionCtrl sessionCtrl ;
	@Inject private RestResponseCtrl responseCtrl;
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun document trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "documents trouvés",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = Document.class) )
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Rechercher une liste de documents par programme immobilier et par type document",
			description = "Rechercher une liste de documents par programme immobilier et par type document"
			
			)
public Response rechercherDocumentListParProgrammeImmobilierParTypeDocument( 
		 @Parameter(
	            description = "Identifiant Id Unique du programme immobilier",
	            required = true,
	            example = "1551176445313ABBB1233AZN2331",
	            schema = @Schema(type = "string")
	       )
		 @Context HttpServletRequest servletRequest,
		 @QueryParam("idProgrammeImmobilier") String idProgrammeImmobilier,
		 @QueryParam("typeDocument") String typeDocument){

		        
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
				
				SessionBag sessionBag = sessionCtrl.extractSession(servletRequest, null,  msgList) ;
				Locale locale =  SessionUtil.getLocale(sessionBag);
			
				boolean estEjbException = false ;
				Response.Status status = Response.Status.OK ;
				
				List<Document> rtn = null;
		
				try {
					

					rtn  =  programmeImmobilierDocumentCtl
							   .rechercherDocumentListParProgrammeImmobilierParTypeDocument(
									  idProgrammeImmobilier, typeDocument, 
									  true, null, true, locale, msgList) ;
				
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
	

/************************Suppresssion d'un document par programme immobilier ************************************/
	
	@Secured
	@DELETE
	@Path("{idDocument}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La suppression du document  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La supression du document effectuée",
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
			summary = "La suppression d'un document",
			description = "La suppression d'un document"
			)
	public Response supprimerUnDocumentParProgrammeImmobilier(
			@Parameter(
		            description = "Identifiant Id Unique au document",
		            required = true,
		            example = "1551176445313ABBB1233AZN2331",
		            schema = @Schema(type = "string")
		       )
		 @Context HttpServletRequest servletRequest,
		 @PathParam("idDocument") String idDocument) {
		
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
			
			// Appel de  la methode  supprimer un document
			
			rtn =  programmeImmobilierDocumentCtl
					     .supprimerDocumentParProgrammeImmobilier(
					    	idDocument, mustUpdateExistingNew, namedGraph, 
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
	
	
	
}
