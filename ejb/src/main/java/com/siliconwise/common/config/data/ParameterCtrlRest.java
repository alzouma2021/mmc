package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.Secured;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionDAO;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.common.AppUtil;
import com.siliconwise.common.rest.RestResponseCtrl;

/**
 * @author sysadmin
 *
 */
@Stateless
@Path("/parameter")
public class ParameterCtrlRest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject private ParameterCtrl parameterCtrl ;
	
	@Inject private SessionDAO sessionDAO ;
	@Inject private RestResponseCtrl responseCtrl ;
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	@POST
	@Secured
	@Path("global/string")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterString( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterString parameter) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterString rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterString) parameterCtrl.validateAndSave(
					 	(IParameter<String>)parameter, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_87 validerEtEnregistrerGlobalParameterString :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	@POST
	@Secured
	@Path("global/long")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterLong( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterLong parameterLong) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterLong rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterLong) parameterCtrl.validateAndSave(
					 	(IParameter<Long>)parameterLong, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_88 validerEtEnregistrerGlobalParameterLong :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	
	@POST
	@Secured
	@Path("global/longtext")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterLongText( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterLongText parameterLongText) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterLongText rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterLongText) parameterCtrl.validateAndSave(
					 	(IParameter<String>)parameterLongText, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_89 validerEtEnregistrerGlobalParameterLongText :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	
	@POST
	@Secured
	@Path("global/integer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterInteger( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterInteger parameterInt) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterInteger rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterInteger) parameterCtrl.validateAndSave(
					 	(IParameter<Integer>)parameterInt, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_90 validerEtEnregistrerGlobalParameterInteger :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	
	@POST
	@Secured
	@Path("global/boolean")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterBoolean( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterBoolean parameterBoolean) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterBoolean rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterBoolean) parameterCtrl.validateAndSave(
					 	(IParameter<Boolean>)parameterBoolean, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_91 validerEtEnregistrerGlobalParameterBoolean :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	
	@POST
	@Secured
	@Path("global/double")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterDouble( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterDouble parameterDouble) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterDouble rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterDouble) parameterCtrl.validateAndSave(
					 	(IParameter<Double>)parameterDouble, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_91 validerEtEnregistrerGlobalParameterBoolean :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	@POST
	@Secured
	@Path("global/localdatetime")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterDateTime( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterDateTime parameterDateTime) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterDateTime rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterDateTime) parameterCtrl.validateAndSave(
					 	(IParameter<LocalDateTime>)parameterDateTime, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_92 validerEtEnregistrerGlobalParameterTime :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getCause());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	@POST
	@Secured
	@Path("global/time")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterTime( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterTime parameterTime) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterTime rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterTime) parameterCtrl.validateAndSave(
					 	(IParameter<LocalTime>)parameterTime, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_92 validerEtEnregistrerGlobalParameterTime :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	
	@POST
	@Secured
	@Path("global/float")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterFloat( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterFloat parameterFloat) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterFloat rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterFloat) parameterCtrl.validateAndSave(
					 	(IParameter<Float>)parameterFloat, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_92 validerEtEnregistrerGlobalParameterFloat :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
	
	@POST
	@Secured
	@Path("global/reference")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validerEtEnregistrerGlobalParameterReference( 
			@Context HttpServletRequest servletRequest,
			GlobalParameterReference parameterRef) {
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
		SessionBag currentSession = sessionDAO.getSession(servletRequest, msgList) ;
		
		Locale locale = SessionUtil.getLocale(currentSession);
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;

		GlobalParameterReference rtn = null ;
		
		try {
			
			 rtn = (GlobalParameterReference) parameterCtrl.validateAndSave(
					 	(IParameter<Reference>)parameterRef, currentSession, msgList) ;
		
		 } catch (Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;

				logger.error("_95 validerEtEnregistrerGlobalParameterReference :: " + msg + " " + ex + ":"+ ex.getMessage()
							+ " Cause:" + ex.getClass());
				ex.printStackTrace();
				
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
				
				rtn = null ;
		}
		
		 return responseCtrl.sendResponse(rtn != null ? rtn.getId() : null, 
				 estEjbException, status, msgList) ;
	}
	
}
