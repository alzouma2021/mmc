package com.siliconwise.mmc.user;

import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface RechercherUnUserCtlInterface {
	
	
	/**
	 * @param id
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param entityClass
	 * @return
	 */
	public User rechercherUnUserParId(
				          String id ,
				          String namedGraph, 
				          boolean isFetchGraph ,
				          Class<User> entityClass);
	
	
	
	

}
