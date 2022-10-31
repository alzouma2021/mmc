package com.siliconwise.common.rest;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;

@Path("/")
@Stateless
public class RestResponseCtrl implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * contruire la reponse a envoyer au  front
	 * @param data
	 * @param msgList
	 * @return Response http 1.1 ok
	 */
	public Response sendResponse(
			Object data, 
			List<NonLocalizedStatusMessage> msgList ) {
		
		CustomizedResponse response = null ; 
		
		if(data != null) {
			
			String msg = "Success" ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.INFO, msg)) ;
			response = new CustomizedResponse(data, msgList) ;
			
		}else
			response = new CustomizedResponse(null, msgList) ;
		
		return  Response.ok(response, MediaType.APPLICATION_JSON).build() ;

	}
	
	
	/**
	 * Permet de construire la reponse a envoyé au client
	 * @param Object data
	 * @param Boolean estEjbException
	 * @param Response.Status status http response
	 * @param List<NonLocalizedStatusMessage> msgList
	 * @return Response
	 */
	public Response sendResponse(
			Object data, 
			boolean estEjbException, 
			Response.Status status, 
			List<NonLocalizedStatusMessage> msgList) {

		CustomizedResponse response =  new CustomizedResponse(data, msgList) ; 
		ResponseBuilder builder = null ;

		/*
		 * Optimisation de la methode (debut 23/06/21 E. EZAN)
		 * Si data est null
		 *     et EjbException est vrai alors status = INTERNAL_SERVER_ERROR (500)
		 *     sinon si ejbException pas vrai alors satus = NO_CONTENT (204)
		 * Sinon satus = OK (200)
		 * 
		 */
		
		if(data == null) {
			
			builder = Response.status(estEjbException ? Response.Status.INTERNAL_SERVER_ERROR : Response.Status.NO_CONTENT) ;
			
			return builder.entity(response).build() ;
		
		}
		
		// Ancien code (fin 21/06/21 E.EZAN)
		/*
		if(estEjbException) {
			
			if(status != null ) builder = Response.status(status) ;
			else builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR) ;
			
			return builder.entity(response).build() ;
		
		}
		
		if(data == null) {
			
			builder = Response.status(status == null ? Response.Status.INTERNAL_SERVER_ERROR : status) ;
			
			return builder.entity(response).build() ;
		
		}
		
		*/
		
		builder = Response.ok() ; 
			
		return builder.entity(response).build() ;

	}
	

}