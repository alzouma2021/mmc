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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.siliconwise.common.AppUtil;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.TypeFinancement;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionDAO;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.OperateurCritere;

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
@Path("/operateurcriteres")
@OpenAPIDefinition(info = @Info(title = "Informations sur les operateurs critère: ", version = "1.0", description = "Informations sur les operateurs crirters", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class OperateurCritereRest implements Serializable {

	
			private static final long serialVersionUID = 1L;
		
			@Inject private ReferenceCtlInterface referenceCtlInterface ;
			@Inject private SessionDAO sessionDAO;
			@Inject private RestResponseCtrl responseCtrl;
			
			
			@GET  
			@Produces(MediaType.APPLICATION_JSON)
			@Consumes(MediaType.APPLICATION_JSON)
			@ApiResponse(
					responseCode = "204",
					description = "Aucun operateur trouvé"
			        )
			@ApiResponse(
					responseCode = "200",
					description = "Operateurs critères trouvés",
					content = @Content(
					        mediaType = MediaType.APPLICATION_JSON,
					        array = @ArraySchema ( schema = @Schema(implementation = OperateurCritere.class) )
					                )
			        )
			@ApiResponse(
					responseCode = "500",
					description = "Erreur côté Serveur"
			        )
			@Operation(
					summary = "Trouver les operateurs critères",
					description = "Rechercher des operateurs criètres"
					)
			public Response tousLesOperateursCritere(
					 @Context HttpServletRequest servletRequest ){
				
				
				List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
				
				SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
				Locale locale = SessionUtil.getLocale(sessionBag) ;
				//Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
				// appel de la method du controleur

				boolean estEjbException = false ;
				Response.Status status = Response.Status.OK ;
				
				List<OperateurCritere> rtnList = null;

				try {
				
					rtnList =  referenceCtlInterface.touslesOperateursCriteres();
				
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