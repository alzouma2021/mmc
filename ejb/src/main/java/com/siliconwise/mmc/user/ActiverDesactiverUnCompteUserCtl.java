package com.siliconwise.mmc.user;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;


/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class ActiverDesactiverUnCompteUserCtl implements Serializable , ActiverDesactiverUnCompteUserCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@Resource private EJBContext ejbContext;
		
	@Inject UserDAOInterface userDAO ;
		
   
	@Override
	public boolean activerUnCompteUser(
			        String idUser, 
			        boolean mustUpdateExistingNew,
			        boolean compteUserHasPassWord,
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		boolean rtn = userDAO.validerEtActiver(
				                idUser, mustUpdateExistingNew,
				          		compteUserHasPassWord,
				          		namedGraph, isFetchGraph,
				          		locale, loggedInUser, msgList) ;
		
		return rtn;
		
	}
	


	@Override
	public boolean desactiverUnCompteUser(
			        String idUser, 
			        boolean mustUpdateExistingNew,
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
		
		boolean rtn = userDAO.validerEtDesactiver(
				              idUser, mustUpdateExistingNew, 
				    	      namedGraph, isFetchGraph, 
				    	      locale, loggedInUser, msgList) ;
	
		return rtn;

	}
	

}
