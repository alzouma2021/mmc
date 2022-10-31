package com.siliconwise.common;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class VilleCtl implements Serializable , VilleCtlInterface{

	
	private static final long serialVersionUID = 1L;
	
	@Inject
	VilleDAOInterface villeDAO ;
	
	@Override
	public List<Ville> toutesLesVilles() {
		
		
		List<Ville> rtnList = villeDAO.toutesLesVilles() ;
		
		return rtnList ;
		
	}

	
	
	@Override
	public List<Pays> tousLesPays() {
		

		List<Pays> rtnList = villeDAO.tousLesPays() ;
		
		return rtnList ;
		
	}
	
	
	

}
