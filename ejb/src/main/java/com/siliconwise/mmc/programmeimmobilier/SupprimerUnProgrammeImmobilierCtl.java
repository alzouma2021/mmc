package com.siliconwise.mmc.programmeimmobilier;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class SupprimerUnProgrammeImmobilierCtl implements Serializable , SupprimerUnProgrammeImmobilierCtlInterface {

	

		private static final long serialVersionUID = 1L;
		
		
		@Inject 
		ProgrammeImmobilierDAOInterface programmeImmobilieDAO ;
		
		@PersistenceContext
		EntityManager  entityManager ;
		
		@Resource private EJBContext ejbContext;
		
		private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
		

		@Override
		public boolean supprimerUnProgrammeImmobilier(
						String idProgrammeImmobilier, 
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph,
						Locale locale, User loggedInUser,
						List<NonLocalizedStatusMessage> msgList) {
		
		    
			return    programmeImmobilieDAO
					    .supprimer(
					      idProgrammeImmobilier, 
					      mustUpdateExistingNew, 
					      namedGraph,isFetchGraph, 
					      locale, loggedInUser, msgList );
	
		}

		
		@Override
		public boolean supprimerProgrammeImmobilierList(
				        List<ProgrammeImmobilier> entityList,
				        boolean mustUpdateExistingNew, 
				        String namedGraph, 
				        boolean isFetchGraph, Locale locale,
				        User loggedInUser, List<NonLocalizedStatusMessage> msgList) {
			
			
			//Verification de la variable entityList
			
			if(entityList == null || entityList.isEmpty() 
					              || entityList.size() == 0) return false ;
			
			
			//Appel à la methode supprimerUnProgrammeImmobilier
			
			for(ProgrammeImmobilier entity: entityList) {
				
				
				if(entity != null && entity.getId() != null) {
			
							
			    	boolean	rtn =  supprimerUnProgrammeImmobilier(entity.getId(),mustUpdateExistingNew, 
			    							   namedGraph, isFetchGraph, locale,  loggedInUser,  msgList ) ;
					
					//RollBack de la transaction si la suppression est échouée
					
					if(!rtn) {
						
						try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
						
						return false ; 
						
					}
					
					
				}
				
				
			}
			
			return false;
			
		}
		
}
