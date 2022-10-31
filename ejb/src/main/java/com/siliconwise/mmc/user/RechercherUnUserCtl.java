package com.siliconwise.mmc.user;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class RechercherUnUserCtl implements Serializable , RechercherUnUserCtlInterface {

	
	private static final long serialVersionUID = 1L;
	
	@Resource private EJBContext ejbContext;
		
	@Inject UserDAOInterface userDAO ;
		
    private static transient Logger logger = LoggerFactory.getLogger(RechercherUnUserCtl.class) ;

    
    
	@Override
	public User rechercherUnUserParId(
			           String id, 
			           String namedGraph, 
			           boolean isFetchGraph,
			           Class<User> entityClass) {
	
		return userDAO.rechercherUnUserParId(id, namedGraph, isFetchGraph, entityClass) ;
		
		
	}

	
}
