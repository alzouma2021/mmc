package com.siliconwise.common.document;

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
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.Secured;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionDAO;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProduitLogementRest;
import com.siliconwise.mmc.produitlogement.ProduitLogementTransfert;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
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
@Path("/documents")
@OpenAPIDefinition(info = @Info(title = "Gestion de documents: ", version = "1.0", description = "Gestion de documents", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class DocumentRest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject private DocumentCtlInterface documentCtl ;
	@Inject private SessionDAO sessionDAO;
	@Inject private RestResponseCtrl responseCtrl;
	
	private static transient Logger logger =  LoggerFactory.getLogger(DocumentRest.class) ;
	
	
	/**
	 * @param servletRequest
	 * @return un content d'un document donné
	 */
	@GET
	@Path("{idDocument}/content")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun contenu trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Contenu du document trouvé",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "String")
			             )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Retourner le contenu d'un document par son id",
			description = "Retourner le contenu d'un document par son id"
			
			)
public Response findStringContentDocumentParId( 
		 @Parameter(
	            description = "Identifiant Id Unique au dicument",
	            required = true,
	            example = "1551176445313ABBB1233AZN2331",
	            schema = @Schema(type = "String")
	       )
		 @Context HttpServletRequest servletRequest,
		 @PathParam("idDocument") String idDocument){
				
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
				SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
				Locale locale = SessionUtil.getLocale(sessionBag) ;
				boolean estEjbException = false ;
				Response.Status status = Response.Status.OK ;
				
				String rtn = null;
		
				try {
					
					rtn  =  documentCtl
							   .findStringDocumentById(idDocument, false, locale, msgList).getContenu();
				
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
	
/***********************************Contenu d'un document en byte***********************************************/

	@GET
	@Path("{idDocument}/contentByte")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun contenu trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Contenu du document trouvé",
			content = @Content(
			        mediaType = MediaType.APPLICATION_OCTET_STREAM,
			        schema = @Schema(type = "byte")
			             )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Retourner le contenu d'un document par son id",
			description = "Retourner le contenu d'un document par son id"
			
			)
public Response findBytesContentDocumentParId( 
		 @Parameter(
	            description = "Identifiant Id Unique du document",
	            required = true,
	            example = "1551176445313ABBB1233AZN2331",
	            schema = @Schema(type = "String")
	       )
		 @Context HttpServletRequest servletRequest,
		 @PathParam("idDocument") String idDocument){
				
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
				SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
				Locale locale = SessionUtil.getLocale(sessionBag) ;
				boolean estEjbException = false ;
				Response.Status status = Response.Status.OK ;
				
				byte[] rtn = null;
		
				try {
					
					rtn  =  documentCtl
							   .findBytesDocumentById(idDocument, false, locale, msgList);
				
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
				
				
				return  Response.ok(rtn, MediaType.APPLICATION_OCTET_STREAM).build() ;
		
				//return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
	
/***********************************Modification des metadatas d'un produit logement****************************/

	/**
	 * @param servletRequest
	 * @param entity
	 * @return un document modifié
	 */
	@Secured
	@PUT
	@Path("updatemetadata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La modification des metadatas  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La modification des metadatas  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = Document.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Modification  des metatdatas d'un document ",
			description = "Modification  des metatdatas d'un document "
			)
	public Response modifierUnProduitLogement(
						@RequestBody(
							description = "Modifier un document", 
							required = true,
							content = @Content(schema = @Schema(implementation = Document.class)))
						@Context HttpServletRequest servletRequest,
						Document entity) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
		boolean updateStringContentDocument = false ;
	    User loggedInUser = new User();
	   
	    
		// appel de la method du controleur

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
	
		Document rtn = null  ;

		try {
			
			rtn =  documentCtl
					  .creerModifierDocument(
							  entity, mustUpdateExistingNew, 
							  updateStringContentDocument,
							  namedGraph, isFetchGraph, locale, 
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
		
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
		
	}
	

/**********************************Modification du contenu d'un document donné************************************/

	/**
	 * @param servletRequest
	 * @param entity
	 * @return un document modifié
	 */
	@Secured
	@PUT
	@Path("updatecontent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La modification du contenu du document n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La modification du contenu du document  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "string")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Modification du contenu d'un document ",
			description = "Modification  ddu contenu d'un document "
			)
	public Response updateStringContentDocument(
							@RequestBody(
								description = "Modifier le contenu d'un document", 
								required = true,
								content = @Content(schema = @Schema(implementation = Document.class)))
							@Context HttpServletRequest servletRequest,
							Document entity) {
		
		
		//Declaration des variables 
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
		boolean updateStringContentDocument = true ;
	    User loggedInUser = new User();
	   
	    
		// appel de la method du controleur

		boolean estEjbException = false ;
		
		Response.Status status = Response.Status.OK ;
		
	
		String rtn = null  ;

		try {
			
			rtn =  documentCtl
					.updateStringContentDocument(
					  entity, mustUpdateExistingNew, updateStringContentDocument, 
					  namedGraph,isFetchGraph, locale, loggedInUser, msgList) ;
					  
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
	
	
/***************************************Suppression complete d'un document par son Id *************************************************/
	
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
	public Response supprimerUnProduitLogement(
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

		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
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
			
			rtn =  documentCtl
					  .supprimerUnDocument(
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


/**************************************Liste de formats de documents disponible*****************************************/

	@Secured
	@GET
	@Path("formats")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun format trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "format(s) document(s) trouvé(s)",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = DocumentFormat.class) )
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "La liste de formats de documents disponibles",
			description = "La liste de formats de documents disponibles"
			)
	public Response findAllDocumentFormats(
						@Context HttpServletRequest servletRequest) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
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
		
		List<DocumentFormat>  rtnList = null ;
		

		try {
			
			// Appel de  la methode  supprimer un document
			
			rtnList =  documentCtl.findAllDocumentFormats(namedGraph, isFetchGraph) ;
					
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
	
	
/*********************************** Append Document to Entity *****************************************************/

	@Secured
	@POST
	@Path("appendentitydocument")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "L'ajout n'a  pas été effectué"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "L'ajout a été effectué",
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
			summary = "L'ajout d'un document à une entité existante",
			description = "L'ajout d'un document à une entité existante"
			)
	public Response appendEntityDocument(
		@RequestBody(
					description = "L'ajout d'un document à une entité existante", 
					required = true,
					content = @Content(schema = @Schema(implementation = DocumentEntityTransfert.class)))
		 @Context HttpServletRequest servletRequest,
		 DocumentEntityTransfert entityTransfert) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
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
	
			
			rtn =  documentCtl
					  .appendEntityDocument(
					    entityTransfert, mustUpdateExistingNew, namedGraph, 
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
