package com.siliconwise.common.event.oldhistorique;

import java.util.List;
import java.util.Locale;

import javax.enterprise.event.Observes;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface HistoriqueDAOInterface {
	
	
	public boolean  valider(Historique entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale,
			List<NonLocalizedStatusMessage> msgList);
	
	
	public Historique validerEtEnregistrer(
			Historique entity,
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale,
			List<NonLocalizedStatusMessage> msgList);
	
	
	/*
	public void historiser(
			@Observes HistoriserEventPayload<IEntityStringkey> payload,
			HistoriqueEventCallback callback);*/
	
	

}
