/**
 * 
 */
package com.siliconwise.common.event.historique;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.beanvalidation.LoggerUtil;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.user.UserDAOInterface;


/** History event management utility
 * @author sysadmin
 *
 */
public class HistoryEventUtil implements Serializable {
	
	
	
	private static final long serialVersionUID = 1L;


	private static transient Logger logger = LoggerFactory.getLogger(HistoryEventUtil.class) ;
	
	
	@Inject HistoryDAOInterface historyDAO ;
	

	/** Fire an history event; Event will not be fred if passed historyEventToFire is null.
	 * @param <T>
	 * @param historyEventToFire
	 * @param entity
	 * @param historyEventType
	 * @param loggedInUserId
	 * @param loggedInUserFullname
	 * @param locale
	 * @return fired event payload
	 */
	public static <T extends IEntityStringkey> HistoryEventPayload<T> fireHistoryEvent(
			      Event<HistoryEventPayload<T>> historyEventToFire,
			      HistoryEventType historyEventType,
			      T entity, String loggedInUserId, 
			      String loggedInUserFullname, 
			      String observation ,
		          Locale locale) {
		
		
		logger.info("_46 debut fireHistoryEvent"); //TODO A effacer
		
		HistoryEventPayload<T> payload = new HistoryEventPayload<>();
		
		payload.setEntity(entity);
		
		payload.setLocale(locale);
		
		payload.setDateEtHeureEvenement(OffsetDateTime.now()) ;
		
		payload.setObservation(observation != null && !observation.isEmpty() ? observation :  null);

		payload.setUserId(loggedInUserId != null && !loggedInUserId.isEmpty() ? loggedInUserId : null);

		payload.setUserName(loggedInUserFullname != null && !loggedInUserFullname.isEmpty() ? loggedInUserFullname : null);

		payload.setEventType(historyEventType);
		
		
		if (historyEventToFire != null) {
			
			logger.info("_70 debut d'evenement"); //TODO A effacer

			historyEventToFire.fireAsync(payload);
			
			logger.info("_74 fin d'evenement :: payload="+payload.toString()); //TODO A effacer
			
			return payload;
			
		}
		
		return null ;
		
	}
	

}
