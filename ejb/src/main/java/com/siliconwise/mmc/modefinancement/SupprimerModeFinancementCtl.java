package com.siliconwise.mmc.modefinancement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class SupprimerModeFinancementCtl implements Serializable , SupprimerModeFinancementCtlInterface {

	
	private static final long serialVersionUID = 1L;
	
	@Inject ModeFinancementDAOInterface modeFinancementDAO ;

	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	@Resource 
	private EJBContext ejbContext;
	
	@Override
	public boolean supprimerModeFinancementList(
			        List<ModeFinancement> entityList, 
			        boolean mustUpdateExistingNew,
			        String namedGraph, boolean isFetchGraph,
			        Locale locale, User loggedInUser,
			        List<NonLocalizedStatusMessage> msgList) {
	
		
				//Verification de la variable entityList
		
				if(entityList == null || entityList.isEmpty() 
						              || entityList.size() == 0) return false ;
				
				
				//Appel à la methode supprimerModeFinancement
				
				for(ModeFinancement entity: entityList) {
					
					if(entity != null && entity.getId() != null) {
					
								
				    	boolean	rtn =  supprimerUnModeFinancement( entity.getId(),mustUpdateExistingNew, 
				    					namedGraph, isFetchGraph, locale,  loggedInUser,  msgList    ) ;
						
				    	
						//RollBack de la transaction si la suppression est échouée
						
						if(!rtn) {
							
							try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
							
							return false ; 
							
						}
						
					}
					
				}
					
		  return true ;
				
	}
	
	
	
	
	@Override
	public boolean supprimerUnModeFinancement(
			        String idModeFinancement,
			        boolean mustUpdateExistingNew, 
			        String namedGraph,
			        boolean isFetchGraph, 
			        Locale locale,
			        User loggedInUser, 
			        List<NonLocalizedStatusMessage> msgList) {
	
		
		
		boolean rtn = modeFinancementDAO
				       .supprimer(idModeFinancement, mustUpdateExistingNew, 
				    	              namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
	
		return rtn;
		
		
	}

	
}
