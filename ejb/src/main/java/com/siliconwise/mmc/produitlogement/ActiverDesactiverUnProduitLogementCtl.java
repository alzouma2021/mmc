package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.caracteristique.CreerModifierCaracteristiqueProduitLogementCtlInterface;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamamdou
 *
 */
@Stateless
public class ActiverDesactiverUnProduitLogementCtl implements Serializable , ActiverDesactiverUnProduitLogementCtlInterface{

	
	private static final long serialVersionUID = 1L;

	@Inject ProduitLogementDAOInterface produitLogementDAO ;
	
	@Inject CreerModifierCaracteristiqueProduitLogementCtlInterface  creerModifierCaracteristiqueProduitLogementCtl ;
	
	@Override
	public boolean activerUnProduitLogement(
					String idProduitLogement, 
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale, User loggedInUser,
					List<NonLocalizedStatusMessage> msgList) {
		

		boolean rtn = produitLogementDAO
						.validerEtActiver(
						  idProduitLogement, mustUpdateExistingNew, namedGraph, 
						  isFetchGraph, locale, loggedInUser, msgList);
		
		return rtn;
		         
	}
	
	
	@Override
	public boolean desactiverUnProduitLogement(
					 String idProduitLogement, 
					 boolean mustUpdateExistingNew,
					 String namedGraph, boolean isFetchGraph, 
					 Locale locale, User loggedInUser,
					 List<NonLocalizedStatusMessage> msgList) {
		
		boolean rtn = produitLogementDAO
						 .validerEtDesactiver(
							idProduitLogement, mustUpdateExistingNew, namedGraph, 
							isFetchGraph, locale, loggedInUser, msgList);

		return rtn;
		
		
	}
	

}
