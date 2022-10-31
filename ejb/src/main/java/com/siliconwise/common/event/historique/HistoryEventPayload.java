package com.siliconwise.common.event.historique;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Locale;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.historique.History.HistoryEventType;


/** Payload pour l'historisation des evenements 
 * @author sysadmin
 *
 */
public class HistoryEventPayload<T extends IEntityStringkey> 
	implements Serializable {

	private static final long serialVersionUID = 1L;

	private T entity = null ; 
			
	private Locale locale = null ; // locale de l'initiateur de l'evenement
	private String userId = null ; // user id	
	private String userName = null ; // user first nama and name
	
	private String observation = null ; // Informations importantes : par exemple sur l'entité supprimée 
	
	private OffsetDateTime dateEtHeureEvenement ;
	
	private int unsuccessfullRetry = 0 ;
	
	private HistoryEventType eventType = null ;
	
	private HistoryEventCallback callback = null ;

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUnsuccessfullRetry() {
		return unsuccessfullRetry;
	}

	public void setUnsuccessfullRetry(int unsuccessfullRetry) {
		this.unsuccessfullRetry = unsuccessfullRetry;
	}

	public HistoryEventType getEventType() {
		return eventType;
	}

	public void setEventType(HistoryEventType eventType) {
		this.eventType = eventType;
	}

	public HistoryEventCallback getCallback() {
		return callback;
	}

	public void setCallback(HistoryEventCallback callback) {
		this.callback = callback;
	}
	
	public OffsetDateTime getDateEtHeureEvenement() {
		return dateEtHeureEvenement;
	}

	public void setDateEtHeureEvenement(OffsetDateTime dateEtHeureEvenement) {
		this.dateEtHeureEvenement = dateEtHeureEvenement;
	}
	
	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	@Override
	public String toString() {
		return "HistoryEventPayload [entity=" + entity + ", locale=" + locale + ", userId=" + userId + ", userName="
				+ userName + ", unsuccessfullRetry=" + unsuccessfullRetry + ", eventType=" + eventType + ", callback="
				+ callback + "]";
	} 
	

}
