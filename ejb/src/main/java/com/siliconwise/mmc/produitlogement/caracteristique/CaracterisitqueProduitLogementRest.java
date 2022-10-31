package com.siliconwise.mmc.produitlogement.caracteristique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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
import com.siliconwise.mmc.oldSecurity.Secured;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProduitLogementRest;
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

/**
 * @author Alzouma Moussa Mahamadou
 * @Date 19/05/2021
 *
 */

@Stateless
@Path("caracteristiqueproduitlogements")
@OpenAPIDefinition(info = @Info(title = "Gestion des APis de Produit Logement: Les Types DATE , DATETIME et TIME sont au format ISO", version = "1.0", description = "CaracterisitqueProduitLogement Service APIs", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class CaracterisitqueProduitLogementRest implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Inject private	CreerModifierCaracteristiqueProduitLogementCtlInterface creerModifierCaracteristiqueProduitLogementCtl;
	@Inject private SessionCtrl sessionCtrl ;
	@Inject private RestResponseCtrl responseCtrl;
	
	
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
		    description = "Creation de la caracteristique  non effectuée"
       )
	@ApiResponse(
		    responseCode = "201",
		    description = "La caracteristique a été bien crée",
		    content = @Content(
		              mediaType = MediaType.APPLICATION_JSON,
		            		  array = @ArraySchema ( schema = @Schema(implementation = CaracteristiqueProduitLogement.class) )
		                )
       )
	@ApiResponse(
		    responseCode = "500",
		    description = "Erreur côté Serveur"
       )
	@Operation(
			summary = "Création ou Modification d'une caracteristique produit logement",
			description = "Création ou modification d'une caracteristique produit logement"
			)
	public Response validerEtEnregistrer( 
			@RequestBody(
					description = "Nouvelle Caracterisitque", 
					required = true,
					content = @Content(schema = @Schema(implementation = CaracteristiqueProduitLogement.class)))
			@Context HttpServletRequest servletRequest,
			Set<CaracteristiqueProduitLogement> entity ) {
	
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
		
		ProduitLogementRest produitLogementRest = new ProduitLogementRest() ;
		
		//Conversion des valeurs caracteristique
		
		Set<CaracteristiqueProduitLogement> rtnList = null ;
		
		if(entity != null  ) {
					
		  rtnList = produitLogementRest.convertirValeurCaracteristiqueProduitLogementEnFonctionDuTypeDeLaPropriete(entity) ;
						
		}

		List<CaracteristiqueProduitLogement> rtn = null ;
		
		ProduitLogement produitLogement = new ProduitLogement() ;
		
		try {
			 
			 rtn = creerModifierCaracteristiqueProduitLogementCtl
					 			.creerModifierCaracteristiqueProduitLogementList( rtnList, 
					 					 mustUpdateExistingNew,produitLogement, namedGraph, 
					 					 isFetchGraph, locale, loggedInUser, msgList) ;
		
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

}
