package com.siliconwise.mmc.produitlogement.critere;



/**
 * Interface Requete Enregistree
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 13/01/2021
 *
 */
public interface RequeteEnregistreeInterfaceDAO {

	
	//Methode pour selectionner une requete enregistree
	
	public RequeteEnregistree selectionnerUneRequeteEnregistree(String requete);
	
	//Mehode pour enregistrer la recherche
	
	public String enregistrerLaRequete(RequeteEnregistree requeteEnregistree) ;
	
}
