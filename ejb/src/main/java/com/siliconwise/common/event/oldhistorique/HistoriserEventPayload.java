package com.siliconwise.common.event.oldhistorique;

import java.io.Serializable;
import java.util.Locale;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.oldhistorique.Historique.HistoriqueEvent;

/** Payload pour l'historisation des evenements 
 * @author sysadmin
 *
 */
public class HistoriserEventPayload<T extends IEntityStringkey> 
	implements Serializable {

	private static final long serialVersionUID = -3112828434922239434L;

	private T entity = null ; 
			
	private Locale locale = null ; // locale de l'initiateur de l'evenement
	private String userId = null ; // user id	
	private String userName = null ; // user first nama and name
	
	private int unsuccessfullRetry = 0 ;
	
	private HistoriqueEvent event = null ;

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

	public HistoriqueEvent getEvent() {
		return event;
	}

	public void setEvent(HistoriqueEvent event) {
		this.event = event;
	} 
}
