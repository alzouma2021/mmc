package com.siliconwise.common.event.historique;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;

public interface HistoryDAOInterface {
	
	
	public boolean  valider(History entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale,
			List<NonLocalizedStatusMessage> msgList) ;
	
	
	public History validerEtEnregistrer(
			History entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,
			List<NonLocalizedStatusMessage> msgList);
	
	
	public  <T extends IEntityStringkey> void historize(
			 HistoryEventPayload<T> payload) ;
	
	

}
