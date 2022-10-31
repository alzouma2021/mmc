package com.siliconwise.mmc.produitlogement.critere;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe DAO Requete Enregistree
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 13/01/2021
 *
 *
 */

@Stateless
public class RequeteEnregistreeDAO implements RequeteEnregistreeInterfaceDAO{

	@PersistenceContext private EntityManager entityManager ;

	@Override
	public RequeteEnregistree selectionnerUneRequeteEnregistree(String requete) {
		
		
		return null;
	}

	@Override
	public String enregistrerLaRequete(RequeteEnregistree requeteEnregistree) {
		
		
		return null;
	}
   
}
