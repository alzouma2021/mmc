package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Stateless
@Path("/proprietes")
@OpenAPIDefinition(info = @Info(title = "Liste des propriétés de Produit Logement: ", version = "1.0", description = "Liste des propriétés de Produit Logement", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class ProprieteProduitLogementRest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject private RechercherProduitLogementCtlInterface rechercherProduitLogementCtlInterface ;
	@Inject private SessionDAO sessionDAO;
	@Inject private RestResponseCtrl responseCtrl;
	
	/**
	 * @param servletRequest
	 * @return toutes les propriétés des produits logements
	 */
	@GET  
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Propriétés Produits logements non trouvés"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Propriétés Produits Logements trouvés",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = ProprieteProduitLogement.class))
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Retourner la liste de propriétés produits logements",
			description = "Retourner la liste de propriétés produits logements"
			)
public Response rechercherProprieteProduitLogementList( @Context HttpServletRequest servletRequest){
				
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
				SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
				Locale locale = SessionUtil.getLocale(sessionBag) ;
				boolean estEjbException = false ;
				Response.Status status = Response.Status.OK ;
				
				List<ProprieteProduitLogement> rtn = null;
		
				try {
					rtn =  rechercherProduitLogementCtlInterface.rechercherProprieteProduitLogementList();
				
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
