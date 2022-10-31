package com.siliconwise.mmc.produitlogement;

import java.util.List;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.critere.CritereRechercheProduitLogement;

/**
 * Cette interface contient les methodes de la classe de controle
 * ProduitLogement
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 18/01/2021
 *
 */
public interface RechercherProduitLogementCtlInterface {

	
	/**
	 * @param critereList
	 * @param msgList message de la liste d'erreurs
	 * @return retourne une liste de produitus logements ou null si il y'a une erreur
	 */
	public List<ProduitLogement> rechercherProduitLogementParCritereList(
			List<CritereRechercheProduitLogement> critereList , List<NonLocalizedStatusMessage> msgList);

	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return un prodouit logement ou null s'il y'a une erreur
	 */
	public ProduitLogement rechercherUnProduitLogementParId(String id ,String namedGraph, boolean isFetchGraph , Class<ProduitLogement> entityClass);
	
	
	/**
	 * @return la liste de produits logements disponibles
	 */
	public List<ProduitLogement> rechercherProduitLogementList();
	
	/**
	 * @param motCles
	 * @return tous les produits logements dont les proprietes contiennent le motclé renseigné
	 */
	public List<ProduitLogement> rechercherProduitLogementParMotCle(String motCles);
	
	
	/**
	 * @return la liste de toutes les propriétés produit logement disponibles
	 */
	public List<ProprieteProduitLogement> rechercherProprieteProduitLogementList();
	
	
	/**
	 * @return des produits logements par promoteur
	 */
	public List<ProduitLogement> rechercherProduitLogementParPromoteur(String promoteurId ,String namedGraph, boolean isFetchGraph , Class<ProduitLogement> entityClass);
	
	

	/**
	 *@return toutes les caracteristiques d'un produit logement
	 */
	public List<CaracteristiqueProduitLogement> rechercherCaracteristiquesParProduitLogement(
								String IdProduit ,String namedGraph, 
								boolean isFetchGraph , 
								Class<CaracteristiqueProduitLogement> entityClass);
	

}
