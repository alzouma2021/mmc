package com.siliconwise.mmc.produitlogement;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.critere.CritereRechercheProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * Interface de ProduitLogement
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 13/01/2021
 *
 */
/**
 * @author sysadmin
 *
 */
/**
 * @author sysadmin
 *
 */
public interface ProduitLogementDAOInterface {

	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return Un produit logement ou null si il y' a une erreur
	 */
	public ProduitLogement rechercherUnProduitLogementParId(String id ,String namedGraph, boolean isFetchGraph , Class<ProduitLogement> entityClass);

	
	
	/**
	 * @param critereList
	 * @param msgList Message de la liste d'erreurs
	 * @return la liste de produits logements ou null si il y'a une erreur
	 * 
	 */
	public List<ProduitLogement> rechercherProduitLogementParCritereList(
			List<CritereRechercheProduitLogement> critereList , List<NonLocalizedStatusMessage> msgList);
	

	/**
	 * @return la liste de produits logements disponibles
	 */
	public List<ProduitLogement> rechercherProduitLogementList();
	
	
	
	/**
	 * @param motCle
	 * @return tous les produits logements dont les proprietes contiennent le motclé renseigné
	 */
	public List<ProduitLogement> rechercherProduitLogementParMotCle(String motCle);
	
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public  boolean  valider(ProduitLogement entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale,
			List<NonLocalizedStatusMessage> msgList); 
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public ProduitLogement validerEtEnregistrer(
			ProduitLogement entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,  User loggedInUser, 
			List<NonLocalizedStatusMessage> msgList);
	
	
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean validerEtConfirmer(
						String idProduitLogement,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList);
	
	
	
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean validerEtActiver(
					String idProduitLogement,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
	
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean validerEtDesactiver(
						String idProduitLogement,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList);
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean verifierLesCaracteristiquesObligatoires(
		   				Set<CaracteristiqueProduitLogement> entity);
	
	
	
	
	/**
	 * @param promoteurId
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public List<ProduitLogement> rechercherProduitLogementParPromoteur(
								String promoteurId ,String namedGraph, 
								boolean isFetchGraph , Class<ProduitLogement> entityClass);
	
	
	
	/**
	 * @param IdProduit
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public List<CaracteristiqueProduitLogement> rechercherCaracteristiquesParProduitLogement(
								String IdProduit ,String namedGraph, 
								boolean isFetchGraph , Class<CaracteristiqueProduitLogement> entityClass);
	
	
	

	/**
	 * @param idProduitLogement
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean supprimer(
					String idProduitLogement,
					boolean mustUpdateExistingNew,
					String namedGraph, boolean isFetchGraph, 
					Locale locale,  User loggedInUser, 
					List<NonLocalizedStatusMessage> msgList);
	
	
}
