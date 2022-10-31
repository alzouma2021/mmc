package com.siliconwise.mmc.logement;

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
import com.siliconwise.mmc.produitlogement.caracteristique.CaracterisitqueProduitLogementDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * @author Alzouma Moussa Mahamadou
 *
 */

@Stateless
public class CreerModifierOptionLogementCtl  implements CreerModifierOptionLogementCtlInterface{

	
	@Inject  CaracterisitqueProduitLogementDAOInterface caracteristiqueProduitLogementDAO ;
	
	private static transient Logger logger = LoggerFactory.getLogger(CreerModifierOptionLogementCtl.class) ;
	
	
	@Override
	public List<CaracteristiqueProduitLogement> creerModifierOptionLogementList(
									Set<CaracteristiqueProduitLogement> entityList,
									boolean mustUpdateExistingNew,Logement logement,
									String namedGraph,boolean isFetchGraph, 
									Locale locale, User loggedInUser,
									List<NonLocalizedStatusMessage> msgList) {
		
		
		List<CaracteristiqueProduitLogement> rtnList = new ArrayList<CaracteristiqueProduitLogement>();
		
		for(CaracteristiqueProduitLogement entity: entityList) {
			
				CaracteristiqueProduitLogement rtn = null ;
				
				if(entity != null) {
					
					
					entity.setLogement(logement);
					  
					rtn = creerModifierUneOptionLogement(entity, mustUpdateExistingNew,
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
	public CaracteristiqueProduitLogement creerModifierUneOptionLogement(
						CaracteristiqueProduitLogement entity, 
						boolean mustUpdateExistingNew, String namedGraph,
						boolean isFetchGraph, Locale locale, User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList) {
		
			  CaracteristiqueProduitLogement rtn = caracteristiqueProduitLogementDAO
					                         .validerEtEnregistrer(entity, mustUpdateExistingNew, 
					  					 namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
			  
			 
			  return rtn;
			  
	}
	
}
