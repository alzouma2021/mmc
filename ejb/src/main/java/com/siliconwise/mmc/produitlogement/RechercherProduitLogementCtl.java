package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.critere.CritereRechercheProduitLogement;

/**
 * TODO decrisption de la classe
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 18/01/2021
 *
 */
@Stateless
public class RechercherProduitLogementCtl implements Serializable, RechercherProduitLogementCtlInterface {

	private static final long serialVersionUID = 1L;

	@Inject ProduitLogementDAOInterface produitLogementDAO;
	
	@Inject ProprieteProduitLogementDAOInterface proprieteProduitLogementDAOInterface ;
	
	private static transient Logger logger = LoggerFactory.getLogger(RechercherProduitLogementCtl.class) ;
	
	@PersistenceContext 
	private EntityManager entityManager;

	@Override
	public List<ProduitLogement> rechercherProduitLogementParCritereList(
			List<CritereRechercheProduitLogement> critereList ,  List<NonLocalizedStatusMessage> msgList) {
		
		 
		List<ProduitLogement> rtnList = produitLogementDAO.rechercherProduitLogementParCritereList(critereList, msgList);

		return rtnList;
	}

	@Override
	public ProduitLogement rechercherUnProduitLogementParId(
								String id, 
								String namedGraph, 
								boolean isFetchGraph, 
								Class<ProduitLogement> entityClass) {
		
		return    produitLogementDAO
				   .rechercherUnProduitLogementParId(id,null,true,entityClass);
	}

	@Override
	public List<ProduitLogement> rechercherProduitLogementList() {
		
		List<ProduitLogement> rtnList = produitLogementDAO
				                         .rechercherProduitLogementList();
		
		return rtnList;
		
	}

	@Override
	public List<ProduitLogement> rechercherProduitLogementParMotCle(String motCles) {
		
		List<ProduitLogement> rtnList = produitLogementDAO
				                         .rechercherProduitLogementParMotCle(motCles);
		
		return rtnList;
		
	}

	@Override
	public List<ProprieteProduitLogement> rechercherProprieteProduitLogementList() {
		
		List<ProprieteProduitLogement> rtnList = proprieteProduitLogementDAOInterface
				                                  .rechercherProprieteProduitLogementList();
		
		return rtnList ; 
		
	}

	
	@Override
	public List<ProduitLogement> rechercherProduitLogementParPromoteur(
								String promoteurId, 
								String namedGraph,
								boolean isFetchGraph, 
								Class<ProduitLogement> entityClass) {
		
		
		List<ProduitLogement> rtnList = produitLogementDAO
										.rechercherProduitLogementParPromoteur(
												promoteurId, namedGraph, 
												isFetchGraph, entityClass);
		
		return rtnList;
		
	}

	@Override
	public List<CaracteristiqueProduitLogement> 
					rechercherCaracteristiquesParProduitLogement(
				    String IdProduit,
				    String namedGraph, 
				    boolean isFetchGraph, 
				    Class<CaracteristiqueProduitLogement> entityClass) {
		
		
		List<CaracteristiqueProduitLogement>  rtnList = 
								produitLogementDAO
									.rechercherCaracteristiquesParProduitLogement(
												IdProduit, namedGraph, isFetchGraph, entityClass);
	
		
		return rtnList;
		
		
	}
	
	
}
