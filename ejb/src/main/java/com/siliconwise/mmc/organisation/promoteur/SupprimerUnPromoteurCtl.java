package com.siliconwise.mmc.organisation.promoteur;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class SupprimerUnPromoteurCtl implements Serializable , SupprimerUnPromoteurCtlInterface  {


	
	private static final long serialVersionUID = 1L;


	@Resource
	private EJBContext ejbContext;


	private static transient Logger logger = LoggerFactory.getLogger(SupprimerUnPromoteurCtl.class) ;
	
	@Inject PromoteurDAOInterface promoteurDAO ;
	
	
	@Override
	public boolean supprimer(
			        String idPromoteur, 
			        boolean mustUpdateExistingNew, 
			        String namedGraph, 
			        boolean isFetchGraph,
			        Locale locale,
			        User loggedInUser, 
			        List<NonLocalizedStatusMessage> msgList) {
	
		boolean rtn = promoteurDAO
				       .validerEtSupprimer(idPromoteur, mustUpdateExistingNew, 
				    	 namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
		
		return rtn;
		
		
	}
	

}
