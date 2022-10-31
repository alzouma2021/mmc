package com.siliconwise.common;

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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.reference.ReferenceCtlInterface;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.Secured;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionDAO;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
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
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
@Path("/villes")
@OpenAPIDefinition(info = @Info(title = "Informations sur les villes: ", version = "1.0", description = "Informations sur les villes", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class VilleRest implements Serializable {


	
	private static final long serialVersionUID = 1L;
	
	@Inject private VilleCtlInterface villeCtl ;
	@Inject private SessionDAO sessionDAO;
	@Inject private RestResponseCtrl responseCtrl;
	
	private static transient Logger logger = LoggerFactory.getLogger(VilleRest.class) ;
	

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
			description = "Liste de villes",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = Ville.class))
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Liste de villes",
			description = "Liste de villes"
			)
	public Response toutesLesVilles(
						@RequestBody(
							description = "Liste de villes", 
							required = true
							)
						@Context HttpServletRequest servletRequest) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
	
		
	    User loggedInUser = new User();
	    loggedInUser.setId("11aabbbdfgg");
	    loggedInUser.setNom("alzouma");
	    loggedInUser.setEmail("alzoumamoussa1NonLocalizedStatusMessage8@univmetiers.ci");
	    loggedInUser.setMotDePasse("alzouma2021");

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		List<Ville> rtn = null  ;
		

		try {
			
			// Appel de  la methode  toutesLesVilles
			
			rtn =  villeCtl.toutesLesVilles() ;
			
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
