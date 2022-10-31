package com.siliconwise.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless 
public class VilleDAO implements Serializable , VilleDAOInterface {

	
	private static final long serialVersionUID = 1L;

	@Resource
	private EJBContext ejbContext;

	@PersistenceContext
	private EntityManager entityManager;
	
			
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Ville> toutesLesVilles() {
		
		List<Ville> rtnListe = new ArrayList<>() ;
		
		try {
			
			rtnListe = (List<Ville>) entityManager
				.createNamedQuery("toutesLesVilles")
				.getResultList();
			
		} catch (NoResultException e) {

			return null ;
		}
		
		return rtnListe ;
		
	
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pays> tousLesPays() {
		
		List<Pays> rtnListe = new ArrayList<>() ;
		
		try {
			
			rtnListe = (List<Pays>) entityManager
				.createNamedQuery("tousLesPays")
				.getResultList();
			
		} catch (NoResultException e) {

			return null ;
		}
		
		return rtnListe ;
		
	
	}
	
	

}
