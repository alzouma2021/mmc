package com.siliconwise.mmc.produitlogement;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 * 
 * @author Alzouma Moussa Mahamadou
 * @Date   17/03/2021
 *
 */
@Stateless
public class ProprieteProduitLogementDAO implements ProprieteProduitLogementDAOInterface {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
    public List<ProprieteProduitLogement> rechercherProprieteProduitLogementList() {
			
			   return (List<ProprieteProduitLogement>) entityManager
						.createNamedQuery("trouverToutesLesProprietesProduitLogement")
						.getResultList();
		}
	
}
