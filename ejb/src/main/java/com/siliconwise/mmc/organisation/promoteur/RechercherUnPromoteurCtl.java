package com.siliconwise.mmc.organisation.promoteur;

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
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class RechercherUnPromoteurCtl implements Serializable , RechercherUnPromoteurCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@Resource private EJBContext ejbContext;
		
	@Inject PromoteurDAOInterface promoteurDAO ;
		
    private static transient Logger logger = LoggerFactory.getLogger(RechercherUnPromoteurCtl.class) ;

    
    
    
	@Override
	public Promoteur rechercherUnPromoteurParId(
			           String id, 
			           String namedGraph, 
			           boolean isFetchGraph,
			           Class<Promoteur> entityClass) {
		
		return promoteurDAO
				 .rechercherUnPromoteurParId(id, namedGraph, isFetchGraph, entityClass);
		
		
	}

	
}
