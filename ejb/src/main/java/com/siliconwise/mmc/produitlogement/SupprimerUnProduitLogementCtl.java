package com.siliconwise.mmc.produitlogement;

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

import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class SupprimerUnProduitLogementCtl implements Serializable , SupprimerUnProduitLogementCtlInterface {

	
		private static final long serialVersionUID = 1L;
		
		@Inject ProduitLogementDAOInterface produitLogementDAO;
	
		@Resource 
		private EJBContext ejbContext;

	    @PersistenceContext 
		private EntityManager entityManager;

		@Override
		public boolean supprimer(
				String idProduitLogement, 
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph, 
				Locale locale, User loggedInUser,
				List<NonLocalizedStatusMessage> msgList) {
				
			
				return produitLogementDAO
					      .supprimer(idProduitLogement, mustUpdateExistingNew, 
					    	namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
					 
		}
			
			
}
