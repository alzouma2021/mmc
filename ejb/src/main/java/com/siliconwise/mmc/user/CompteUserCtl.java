package com.siliconwise.mmc.user;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;


import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import com.siliconwise.common.mail.EmailService;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;


/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class CompteUserCtl implements Serializable , CompteUserCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@Resource private EJBContext ejbContext;
	
	@PersistenceContext
	EntityManager entityManager ;
		
	@Inject UserDAOInterface userDAO ;
	
	@Inject EmailService emailService ;
		

    
	@Override
	public boolean regenererUnMotDePasseCompteUser(
			         String email,
			         boolean mustUpdateExistingNew,
			         String namedGraph,
			         boolean isFetchGraph, 
			         Locale locale, 
			         List<NonLocalizedStatusMessage> msgList)
			         throws NoSuchAlgorithmException{
		
		
			return userDAO.regenererUnMotDePasseCompteUser(email, mustUpdateExistingNew,
				                                namedGraph, isFetchGraph, locale, msgList);
			
		
	}
	
	
	@Override
	public boolean confirmerUnCompteUser(
			        String code, 
			        boolean mustUpdateExistingNew,
			        boolean compteUserHasPassWord,
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale, User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
		
		
		boolean rtn = userDAO.confirmerUnCompteUser(
				                code,mustUpdateExistingNew,
							    compteUserHasPassWord,namedGraph,
							    isFetchGraph,locale, loggedInUser, msgList) ;
		
		return rtn;
		
		
	}

    
}
