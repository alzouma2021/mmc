package com.siliconwise.mmc.organisation.efi;

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
public class RechercherUnEFiCtl implements Serializable , RechercherUnEFiCtlInterface {

	
	
	private static final long serialVersionUID = 1L;
	
	@Resource private EJBContext ejbContext;
		
	@Inject EfiDAOInterface EfiDAO ;
		
    private static transient Logger logger = LoggerFactory.getLogger(RechercherUnEFiCtl.class) ;

    
    
    
	@Override
	public EFi rechercherUnEFiParId(
			           String id, 
			           String namedGraph, 
			           boolean isFetchGraph,
			           Class<EFi> entityClass) {
		
		return EfiDAO
				 .rechercherUnEFiParId(id, namedGraph, isFetchGraph, entityClass);
		
		
	}

	
}
