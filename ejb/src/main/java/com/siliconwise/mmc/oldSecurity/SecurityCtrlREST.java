package com.siliconwise.mmc.oldSecurity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
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
import javax.ws.rs.core.Response.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.common.AppUtil;
//import com.siliconwise.nims.common.Personne;
//import com.siliconwise.nims.common.PersonneCtrl;
//import com.siliconwise.nims.common.PersonneRest;
//import com.siliconwise.nims.common.QuestionsSecretes;
//import com.siliconwise.nims.common.QuestionsSecretesCtrl;
import com.siliconwise.common.rest.CustomizedResponse;
import com.siliconwise.common.rest.RestResponseCtrl;
//import com.siliconwise.nims.security.user.Role;
import com.siliconwise.mmc.user.User;
import com.siliconwise.mmc.user.CreerModifierUnCompteUserCtl;

// @Interceptors(value = { SessionInterceptor.class })
/**
 * @author bgnakale
 *
 */
@Stateless
@Path("/")
public class SecurityCtrlREST implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Context private HttpServletRequest servletRequest ;
	@Resource private EJBContext ejbContext;
	
	@Inject private SecurityCtrl securityCtrl ;
	@Inject private TokenManagement tokenManagement ;
	@Inject private SessionDAO sessionDAO ;
	//@Inject private PersonneCtrl personneCtrl ;
	@Inject private RestResponseCtrl responseCtrl ;	
	//@Inject private QuestionsSecretesCtrl questionsSecretesCtrl ;
	//@Inject private OuvrirUnCompteUserCtl userCtrl ;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	
	// Tested
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authentification(
			@Context HttpServletRequest request, LoginPasswordToken user)  {
		
		logger.info("_72 authentification :: user =" + user);
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		// Charger la session existante si elle existe
		SessionBag sessionBag = sessionDAO.getSession(request, msgList) ;
		
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		String token = null ;
	
		try {
			
			token = securityCtrl.authenticate(user, sessionBag, msgList);
			
		 } 
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_97 authentification :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				token = null ;
		}
		
		return responseCtrl.sendResponse(token, estEjbException, status, msgList) ;
	}
	
	/* OLD
	 * // tested
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authentification(
			@Context HttpServletRequest request, LoginPasswordToken user) 
		throws IOException, GeoIp2Exception {
		
		// Determiner url du client
		
		StringBuffer url = request.getRequestURL() ;
		String[] StringArray = url.toString().split("/");
		String hostName = StringArray[2] ;
		
		logger.info("url =" +url +"\n" 	+  "url hote ="+ hostName);
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		// Charger la session existante si elle existe
		SessionBag sessionBag = sessionDAO.getSession(request, msgList) ;
		
		Locale langue = SessionUtil.getLocale(sessionBag) ;
		
		String token = null ;
	
		try {
			token = securityCtrl.authenticate(user, sessionBag, hostName, msgList);
		} catch (Exception e) {
			
			status = Response.Status.INTERNAL_SERVER_ERROR ;
			estEjbException = true ;
		}
		
		return responseCtrl.sendResponse(token, estEjbException, status, msgList) ;
	}*/
	
	// tested
	@Secured 
	@POST 
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(
			@Context HttpServletRequest request) {
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag sessionBag = sessionDAO.getSession(request, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
	
		Boolean rtn = true ;
		
		try {
			rtn = securityCtrl.logout(sessionBag, msgList);
		}
		catch (Exception ex) {
				 
			 String msg  = MessageTranslationUtil.translate(locale,
							AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
							AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
							new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_176 logout :: " + msg + " " + ex.getMessage());
					
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
					
				rtn = null ;
		}
		
		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
	
	@GET @Secured 
	@Path("refreshtoken")
	@Produces(MediaType.APPLICATION_JSON)
	public Response refresh(@Context HttpServletRequest request) {
		

		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		CustomizedResponse response = null ; 
		String previewToken = tokenManagement.extractToken(request);
		
		// TODO Peut etre qu'il faut qu'on prevoit gerer le rafraichissemnt des tokens
		
		previewToken = securityCtrl.refresh(previewToken, msgList)	;
		
		if(previewToken == null) {

			 String msg = AppMessageKeys.CODE_TOKEN_NON_VALIDE ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			 response = new CustomizedResponse("", msgList) ;

		 }else {
			 String msg = "Success" ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.INFO, msg)) ;
			 response = new CustomizedResponse(previewToken, msgList) ;
		 }
		
		ResponseBuilder builder = Response.ok(response, MediaType.APPLICATION_JSON) ;
		
		return builder.build() ;
	}

	@GET @HasSessionId // @Secured 
	@Path("recupererlangue")
	public Response getLanguage(
			@Context HttpServletRequest request){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		CustomizedResponse response = null ; 
		
		// si un utilisateur connecter il ya un jeton dans le token
		// Sinon il  ya un sessionId
		
		SessionBag sessionBag = sessionDAO.getSession(request, msgList) ;

		// 1 
		// TODO Mettre a jour l'interceteur @HasSessionId @Secured en verifiant que la session existe toutjours ou n'a pas expirée. 
		String langue = sessionBag.getLanguage() ; // sessionBag cannot benull because of @HasSessionId
		
		if(langue == null) {

			 String msg = "Langue n'est pas definie " ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			 response = new CustomizedResponse("", msgList) ;

		 }else {
			 String msg = "Success" ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.INFO, msg)) ;
			 response = new CustomizedResponse(langue, msgList) ;
		 }
		
		ResponseBuilder builder = Response.ok(response, MediaType.APPLICATION_JSON) ;
		
		return builder.build() ;
	}

	@PUT @HasSessionId // @Secured 
	@Path("modifierlangue/{langue}")
	public Response updateLanguage(
			@Context HttpServletRequest request ,
			@PathParam("langue") String langue){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		CustomizedResponse response = null ; 
		
		SessionBag sessionBag = sessionDAO.getSession(request, msgList) ;
		
		// 1 
		langue = sessionBag.getLanguage()	;
		
		if(langue == null) {
	
			 String msg = "non valide " ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			 response = new CustomizedResponse("", msgList) ;
	
		 }else {
			 String msg = "Success" ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.INFO, msg)) ;
			 response = new CustomizedResponse(langue, msgList) ;
		 }
		
		ResponseBuilder builder = Response.ok(response, MediaType.APPLICATION_JSON) ;
		
		return builder.build() ;
	}
	
	/*
	// @PUT @HasSessionId  // @Interceptors(value = { SessionInterceptor.class })
	// @Path("annonyme/modifierlangue/{langue}")
	public Response updateAnonymousLanguage(
			@Context HttpServletRequest request,
			@PathParam("langue") String langue){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		CustomizedResponse response = null ;
		
		String sid = request.getHeader("sid") ;
		logger.info(getClass().getName() + "updateAnonymousLanguage :: sid" + sid);
		// 1
		langue = securityCtrl.updateAnonymousLanguage(sid, langue, msgList)	;
		
		if(langue == null) {
	
			 String msg = "non valide " ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			 response = new CustomizedResponse("", msgList) ;
	
		 }else {
			 String msg = "Success" ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.INFO, msg)) ;
			 response = new CustomizedResponse(langue, msgList) ;
		 }
		
		ResponseBuilder builder = Response.ok(response, MediaType.APPLICATION_JSON) ;
		
		return builder.build() ;
	}

	// @GET  @HasSessionId // @Interceptors(value = { SessionInterceptor.class })
	// @Path("annonyme/recupererlangue")
	
	public Response getAnonymousLanguage(
			@Context HttpServletRequest request){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		CustomizedResponse response = null ; 
		
		String sid = request.getHeader("sid") ;
		
		logger.info(getClass().getName() + "getAnonymousLanguage :: sid" + sid);
		String langue = securityCtrl.getAnonymousLanguage(sid, msgList)	;
		
		if(langue == null) {
	
			 String msg = "Langue n'est pas definie " ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			 response = new CustomizedResponse("", msgList) ;
	
		 }else {
			 String msg = "Success" ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.INFO, msg)) ;
			 response = new CustomizedResponse(langue, msgList) ;
		 }
		
		ResponseBuilder builder = Response.ok(response, MediaType.APPLICATION_JSON) ;
		
		return builder.build() ;
	}
	/*
	@GET
	@Path("recupereridsession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAnonymousSession(
			@Context HttpServletRequest request){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		Locale locale = SessionUtil.getLocale(null) ;
		
		// Determiner url du client
		
		StringBuffer url = request.getRequestURL() ;
		String[] StringArray = url.toString().split("/");
		String hostName = StringArray[2] ;
				
		//logger.info("_320 createAnonymousSession :: " + "nom hote DNS ="+ nomHoteDns);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		SessionBag rtn = null ;

		try {
			rtn = securityCtrl.createAnonymousSession(hostName, msgList)	;
		 } 
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_340 createAnonymousSession :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
			
		return responseCtrl.sendResponse(rtn == null ? null : rtn.getId(), 
									estEjbException, status, msgList) ;
	}*/

	// TODO Contacter le front end pour la mise à jour
	/** Cree une session et retourne l'ientifiant e la session
	 * Syntax: recupereridsession?url={urlHote}
	 * @param request
	 * @param urlHote URL Hote de l'IF configuré dans le infos specifique de l'IF 
	 * @return
	 */
	// Tested
	/*
	@GET
	@Path("recupereridsession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAnonymousSession(
			@Context HttpServletRequest request,
			@QueryParam("url") String urlHote){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		Locale locale = SessionUtil.getLocale(null) ;
		
		/* // Determiner url du client
		
		StringBuffer url = request.getRequestURL() ;
		String[] StringArray = url.toString().split("/");
		String hostName = StringArray[2] ;
				
		logger.info("_436 createAnonymousSession :: " + "Url Hote de l'IF ="+ urlHote);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		SessionBag rtn = null ;

		try {
			rtn = securityCtrl.createAnonymousSession(urlHote, msgList)	;
		 } 
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_454 createAnonymousSession :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
			
		return responseCtrl.sendResponse(rtn == null ? null : rtn.getId(), 
									estEjbException, status, msgList) ;
	}*/
	
	/*
	@GET
	@Path("recupereridsession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAnonymousSession(
			@Context HttpServletRequest request){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		
		// Determiner url du client
		
		StringBuffer url = request.getRequestURL() ;
		String[] StringArray = url.toString().split("/");
		String hostName = StringArray[2] ;
				
		logger.info("url =" +url +"\n" 	+  "url hote ="+ hostName);
		
		boolean estEjbException = false ;
		Response.Status ejbExceptionStatus = null ;
		
		Locale langue = SessionUtil.getLocale(null) ;
		
		SessionBag sessionBag = null ;

		try {
			sessionBag = securityCtrl.createAnonymousSession(hostName, msgList)	;
		} catch (Exception e) {
		
			ejbExceptionStatus = Response.Status.INTERNAL_SERVER_ERROR ;
			estEjbException = true ;
		}
			
			if(sessionBag != null) {
		
				String translatedMessage = MessageTranslationUnit.dynamicTranslate(langue,
						AppUtil.GENERAL_MSG_SUCCESS,// venant du fichier
						AppUtil.GENERAL_MSG_SUCCESS, // Message par defaut
						new String[]{});
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.INFO, translatedMessage));
			}
	
			String rtn = sessionBag == null ? null : sessionBag.getId() ;
			
			return responseCtrl.sendResponse(rtn, estEjbException, ejbExceptionStatus, msgList) ;
	}*/
	
	// Tested
	/*
	@Secured
	@GET 
	@Path("security/currentuserrolelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response computeCurrentUserRoleList(
			@Context HttpServletRequest request){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		String rtn = null ;
		
		try {
			rtn = securityCtrl.computeUserRolesAsString(sessionBag.getUserId(), 
									locale, msgList) ;
			logger.info("_476 computeCurrentUserRoleList :: rtn="+rtn) ;
				
		 } catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_375 computeCurrentUserRoleList :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
	*/
	/** Trouver tous les roles
	 * @param request
	 * @return
	 */
	/*
	@Secured
	@GET 
	@Path("security/role/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllRoleList(
			@Context HttpServletRequest request){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		List<Role> rtnList = new ArrayList<>() ;
		
		try {
			rtnList = securityCtrl.findAllRoleList(sessionBag, msgList) ;
				
		 } catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_411 findAllRoleList :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtnList = new ArrayList<>() ;
		}

		return responseCtrl.sendResponse(rtnList, estEjbException, status, msgList) ;
	}

	/** Créer ou modifier un agent d'If ou du Gestionnaire de la plateforme
	 * Si aucun utilisateur correspondant à l'agent n'existe alors créer le compte d'utilisateur correspondant
	 * @param request
	 * @param personnRest
	 * @return
	 */
	
	
	/*
	@Secured
	@POST 
	@Path("security/agent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerAgent(
			@Context HttpServletRequest request,
			PersonneRest personneRest){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		PersonneRest rtn = null ;
		
		try {
			Personne personne = personneRest.toPersonne() ;
			logger.info("_569 validerEtEnregistrerAgent :: apres conversion personne="+personne);
			
			String namedGraph = "graph.personne.rest" ; boolean isFetchGraph = true ;
			String interFiancierNamedGraph = "graph.if.minimum" ; boolean interFiancierIsFetchGraph = true ;
			String infoIdentiteNamedGraph = null ;  boolean infoIdentiteIsFetchGraph = true ;
			String documentNamedGraph = null ; boolean documentIsFetchGraph =  true ;
			
			personne = personneCtrl. validerEtEnregistrerAgent(personne, false, namedGraph, 
					isFetchGraph, interFiancierNamedGraph, interFiancierIsFetchGraph, 
					infoIdentiteNamedGraph, infoIdentiteIsFetchGraph,
					documentNamedGraph, documentIsFetchGraph, sessionBag, msgList)  ;
			
			rtn = PersonneRest.from(personne) ;
			
			logger.info("_577 validerEtEnregistrerAgent :: apres ctrl personne=" +personne);	
			logger.info("_578 validerEtEnregistrerAgent :: apres ctrl personne rest=" +rtn);	
		 } catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_591 validerEtEnregistrerAgent :: " + msg + " " + ex.getClass().getName() 
						+ ": " + ex.getMessage()+ " Cause:" + ex.getCause());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn == null ? null : rtn.getId(), estEjbException, status, msgList) ;
	}*/


	/** Créer ou modifier l'administrateur d'un 
	 * Si aucun utilisateur correspondant à l'administrateur n'existe alors créer le compte d'utilisateur correspondant
	 * Fonctionnalité uniquemet reservé à unadlinistratrateur de la plateforme
	 * @param request
	 * @param personnRest
	 * @return
	 */
	/*
	@Secured
	@POST 
	@Path("security/intermediairefinancier/administrateur")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerAdminIf(
			@Context HttpServletRequest request,
			PersonneRest personneRest){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		PersonneRest rtn = null ;
		
		try {
			Personne personne = personneRest.toPersonne() ;
			logger.info("_569 validerEtEnregistrerAgent :: apres conversion personne="+personne);
			
			String namedGraph = "graph.personne.rest" ; boolean isFetchGraph = true ;
			String interFiancierNamedGraph = "graph.if.minimum" ; boolean interFiancierIsFetchGraph = true ;
			String infoIdentiteNamedGraph = null ;  boolean infoIdentiteIsFetchGraph = true ;
			String documentNamedGraph = null ; boolean documentIsFetchGraph =  true ;
			
			personne = personneCtrl.validerEtEnregistrerAdminIf(personne, false, namedGraph, 
					isFetchGraph, interFiancierNamedGraph, interFiancierIsFetchGraph, 
					infoIdentiteNamedGraph, infoIdentiteIsFetchGraph,
					documentNamedGraph, documentIsFetchGraph, sessionBag, msgList)  ;
			
			rtn = PersonneRest.from(personne) ;
			
			logger.info("_577 validerEtEnregistrerAgent :: apres ctrl personne=" +personne);	
			logger.info("_578 validerEtEnregistrerAgent :: apres ctrl personne rest=" +rtn);	
		} 
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_591 validerEtEnregistrerAgent :: " + msg + " " + ex.getClass().getName() 
						+ ": " + ex.getMessage()+ " Cause:" + ex.getCause());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn == null ? null : rtn.getId(), estEjbException, status, msgList) ;

	}*/
	
	/** Obtenir l'identifiant d'utilisateur d'un agent
	 * @param request
	 * @param agentId
	 * @return
	 */
	
	/*
	@Secured
	@GET
	@Path("security/agent/{agentId}/userid")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findUserIdbyPersonneId(
			@Context HttpServletRequest request,
			@PathParam("agentId") String agentId){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		String rtn = null ;
		
		try {
			rtn = userCtrl.findUserIdbyPersonneId(agentId, msgList)  ;
			
			logger.info("_619 affecterRoleAAgent :: apres ctrl rtn=" +rtn);	
		} 
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_631 affecterRoleAAgent :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}*/
	
	/*
	@Secured
	@PUT 
	@Path("security/agent/{agentId}/role/{idRole}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response affecterRoleAAgent(
			@Context HttpServletRequest request,
			@PathParam("agentId") String agentId,
			@PathParam("idRole") String roleId){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		boolean rtn = false ;
		
		try {
			rtn = personneCtrl.affecterRoleAAgent(agentId, roleId, sessionBag, msgList)  ;
			
			logger.info("_619 affecterRoleAAgent :: apres ctrl rtn=" +rtn);	
		} 
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_631 affecterRoleAAgent :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = false ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}*/
	
	/*
	@Secured
	@DELETE 
	@Path("security/agent/{agentId}/role/{idRole}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retirerRoleAAgent(
			@Context HttpServletRequest request,
			@PathParam("agentId") String agentId,
			@PathParam("idRole") String roleId){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		boolean rtn = false ;
		
		try {
			rtn = personneCtrl.retirerRoleAAgent(agentId, roleId, sessionBag, msgList)  ;
			
			logger.info("_663 retirerRoleAAgent :: apres ctrl rtn=" +rtn);	
		} 
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_673 retirerRoleAAgent :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = false ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
	*/
	
	/** Trouver la liste des agents de l'organisation de l'utilisateur connecté.
	 * Cette organisation est soit un IF si l'utilisateur est un agent d'IF
	 * ou le gestionnaire de la plateforme i l'utilisateur est un agent du Gestionnaire de la plateforme
	 * @param request
	 * @return
	 */
	
	/*
	// Tested
	@Secured
	@GET
	@Path("security/agent/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAgentList(@Context HttpServletRequest request){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ; 
		
		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		List<PersonneRest> rtnList = new ArrayList<>() ;
		
		try {
			List<Personne> persList = personneCtrl.findAgentList("graph.personne.rest", true, 
										sessionBag)  ;
			
			for (Personne pers : persList) {
				
				PersonneRest persRest = PersonneRest.from(pers) ;
				
				if (!rtnList.contains(persRest)) rtnList.add(persRest) ;
			}
			
			logger.info("_711 findAgentList :: apres ctrl rtnList size=" +rtnList.size());	
		}
		catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_729 findAgentList :: " + msg + " " + ex.getMessage());
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtnList = new ArrayList<>() ;
		}

		return responseCtrl.sendResponse(rtnList, estEjbException, status, msgList) ;
	}*/

	// TEsté
	/** Obtenir le pack des questions secretes d'unutilisateur à partir de son identifiant
	 * @param servletRequest
	 * @param idUser
	 * @return
	 */
	
	/*
	@GET @Secured
	@Path("security/user/{idUser}/questionssecretes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response trouverQuestionsSecreteParUtilisateur(
			@Context HttpServletRequest servletRequest,
			@PathParam("idUser") String idUser){
		
		logger.info("_755 trouverQuestionsSecreteParUtilisateur :: Entrée");
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale langue = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		QuestionsSecretes rtn = null ;
		
		try {
			String namedGraph = "graph.questionssecretes.sans-association" ;
			rtn = questionsSecretesCtrl.trouverQuestionsParIdUtilisateur(idUser, namedGraph, true) ;
		 } catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(langue,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_781 trouverQuestionsSecreteParUtilisateur " + msg + " " + ex + ":" + ex.getMessage()
							+ " Cause:" + ex.getCause());
				
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
			
		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
	*/
	/** Obtenir le pack des questions secretes d'unutilisateur à partir de son identifiant
	 * @param servletRequest
	 * @param idUser
	 * @return
	 */
	/*
	@GET @HasSessionId
	@Path("security/login/{login}/questionssecretes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response trouverQuestionsParLoginUtilisateur(
			@Context HttpServletRequest servletRequest,
			@PathParam("login") String login){
		
		logger.info("_968 trouverQuestionsParLoginUtilisateur :: Entrée");
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale langue = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		QuestionsSecretes rtn = null ;
		
		try {
			String namedGraph = "graph.questionssecretes.sans-association" ;
			
			String ifId = currentSession.getIntermediaiareFinancierId() ;
			
			rtn = questionsSecretesCtrl.trouverQuestionsParLoginUtilisateur(
					login, ifId, namedGraph, true) ;
		 } catch (Exception ex) {
			 
			 String msg  = MessageTranslationUnit.dynamicTranslate(langue,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_992 trouverQuestionsParLoginUtilisateur " + msg + " " + ex + ":" + ex.getMessage()
							+ " Cause:" + ex.getCause());
				
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
			
		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}*/
	
	// Testé
	/** Atualise les questions secretes d'un utilisateur à partir de son identifiant
	 * @param servletRequest
	 * @param idUser
	 * @param questionsSecretes
	 * @return true si sucees sinn false
	 * @throws Exception
	 */
	
	/*
	@PUT @Secured
	@Path("security/user/{idUser}/questionssecretes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerQuestionsSecretes(
			@Context HttpServletRequest servletRequest,
			@PathParam("idUser") String idUser,  
			QuestionsSecretes questionsSecretes) throws Exception {
		
		logger.info("_803 validerEtEnregistrerQuestionsSecretes :: Entrée");

		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;

		Locale langue = SessionUtil.getLocale(currentSession);

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		QuestionsSecretes rtn = null ;

		try {
			String namedGraph = "graph.questionssecretes.personne-minimum" ;
			String personneNamedGraph = "graph.personne.idOnly" ;
			
			rtn = questionsSecretesCtrl.validerEtEnregistrer(idUser, questionsSecretes, 
					namedGraph, true, personneNamedGraph, true, currentSession, msgList) ;
		} catch (Exception ex) {
	 
			String msg  = MessageTranslationUnit.dynamicTranslate(langue,
				AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
				AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
				new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

			logger.error("_803 validerEtEnregistrerQuestionsSecretes " + msg + " " + ex + ":" + ex.getMessage()
					+ " Cause:" + ex.getCause());
		
			ex.printStackTrace();
		
			status = Response.Status.INTERNAL_SERVER_ERROR ;
			estEjbException = true ;
		
			rtn = null ;
		}
	
		//String rtnId  =  rtn != null ? rtn.getId() : null ;

		return responseCtrl.sendResponse(rtn != null, estEjbException, status, msgList) ;
	}*/

}
