package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

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
public class ValiderUnProduitLogementCtl implements Serializable, ValiderUnProduitLogementCtlInterface {

	
	private static final long serialVersionUID = 1L;

	
	@Inject ProduitLogementDAOInterface produitLogementDAO;
	
	@PersistenceContext 
	private EntityManager entityManager;
	

	@Override
	public boolean validerUnProduitLogement(
			            String idProduitLogement, 
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale, User loggedInUser,
						List<NonLocalizedStatusMessage> msgList) {
		
		boolean rtn = produitLogementDAO
				         .validerEtConfirmer(
				           idProduitLogement, mustUpdateExistingNew, namedGraph, 
				           isFetchGraph, locale, loggedInUser, msgList) ;
		
		
		return rtn ;
		
	
	}
	
	
}
