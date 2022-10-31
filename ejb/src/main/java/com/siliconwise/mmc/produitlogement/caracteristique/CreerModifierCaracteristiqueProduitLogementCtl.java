package com.siliconwise.mmc.produitlogement.caracteristique;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
public class CreerModifierCaracteristiqueProduitLogementCtl  implements CreerModifierCaracteristiqueProduitLogementCtlInterface{

	
	@Inject  CaracterisitqueProduitLogementDAOInterface caracteristiqueProduitLogementDAO ;
	
	private static transient Logger logger = LoggerFactory.getLogger(CreerModifierCaracteristiqueProduitLogementCtl.class) ;
	
	@Override
	public List<CaracteristiqueProduitLogement> creerModifierCaracteristiqueProduitLogementList(
									Set<CaracteristiqueProduitLogement> entityList,
									boolean mustUpdateExistingNew, ProduitLogement produitLogement,
									String namedGraph,boolean isFetchGraph, 
									Locale locale, User loggedInUser,
									List<NonLocalizedStatusMessage> msgList) {
		
		
		logger.info("_40 entityList="+entityList); //TODO Effacer
		logger.info("_41 produit logement="+produitLogement);  //TODO Effacer
		//Variable Caracteristique Liste
		List<CaracteristiqueProduitLogement> rtnList = new ArrayList<CaracteristiqueProduitLogement>();
		
		for(CaracteristiqueProduitLogement entity: entityList) {
			
				CaracteristiqueProduitLogement rtn = null ;
				
				if(entity != null) {
					
					
					entity.setProduitLogement(produitLogement);
					  
					rtn = creerModifierUneCaracteristiqueProduitLogement(entity, mustUpdateExistingNew,
							 					namedGraph,isFetchGraph, locale, loggedInUser, msgList);
					  
						if(rtn == null) {
							 
							 rtnList = null ;
							 
							 return rtnList ;
							 
						 }
						 
					 
				}
				
					 rtnList.add(rtn) ;
					
		}
		
		//retoure liste caracteristiques produit logement
		
		return rtnList;
		
	}

	@Override
	public CaracteristiqueProduitLogement creerModifierUneCaracteristiqueProduitLogement(
						CaracteristiqueProduitLogement entity, 
						boolean mustUpdateExistingNew, String namedGraph,
						boolean isFetchGraph, Locale locale, User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList) {
		
			  CaracteristiqueProduitLogement rtn = caracteristiqueProduitLogementDAO.validerEtEnregistrer(
					  									entity, mustUpdateExistingNew, 
					  									namedGraph, isFetchGraph, 
					  									locale, loggedInUser, msgList) ;
			  
			 
			  return rtn;
			  
	}
	
}
