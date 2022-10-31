package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.locale.LocaleUtil;

/** Session information bag.
 * Should remain in server memory
 * 
 * Natural Identity:
 * 	- Szt 1 : not applicable
 * 
 * @author sysadmin
 *
 */
/*@NamedQueries(value = {
		
	// utilisateurpar login
	@NamedQuery(
		name = "nbrActiveSessionByUserId",
		query = "SELECT COUNT(s.id) FROM SessionBag s "
				+ "WHERE s.userId = :userId "
				+	"AND s.expirationDateTime > :expirationDateTime ")
})
@Table(name = "sessionbag")
@Entity @EntityListeners(UUIDGeneratorEntityListener.class)*/
public class SessionBag implements Serializable {


	private static final long serialVersionUID = 1L;
	
	//Variables : nothing
	public static final String TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED  = "session.hasExpired" ;
	
	//Variables : nothing
	public static final String TRANSLATION_MESSAGE_SESSION_NO_SESSION  = "session.noSession" ;
	
	//Variables : nothing
	public static final String TRANSLATION_MESSAGE_USER_NOT_LOGGEDIN = "session.userNotLoggedIn" ;
	
	//Variables : nothing
	public static final String TRANSLATION_MESSAGE_AUTHENTICATION_ALREADY_AUTHENTICATED = "session.authentication.userAlreadyAuthenticated" ;
	
	//Variables : nothing
	public static final String TRANSLATION_MESSAGE_AUTHENTICATION_LOGIN_CREDENTIAL_NOT_DEFINED = "session.authentication.loginCredentialNotDefined" ;
	
	public static final String SESSION_EXPIRED_OR_NON_EXISTANT = "session.expired-or-non-existant" ;
	public static final String SESSION_INVALIDE = "session.nonValide" ;
	public static final String SESSION_NOT_DEFINED = "session.not-defined" ;
	public static final String SESSION_ERREUR = "session.erreurSession" ;
	public static final String SESSION_NOT_FOUND = "session.notFound" ;
//	public static final String SESSION_IFID_NOT_DEFINED = "session.ifId.notDefined" ;
	public static final String SESSION_IF_NOT_FOUND = "session.if.notFound" ;
	public static final String CODE_TRADUCTION_PERISTENCE_ERREUR = "session.persistenceErreur" ;
	public static final String CODE_TRADUCTION_AUTHENTICATION_FAILIURE_USER_ALREADY_AuTHENTIFIED = "session.authentificationFailed.userAlreadyAuthentified" ;
	public static final String CODE_TRADUCTION_MAXIMUM_SESSIONS_PER_USER_EXCEEDED = "session.maxiSessionPerUserExceeded" ;
	public static final String CODE_TRADUCTION_BEFORE_SAVING_ERREUR = "session.beforeSaving.erreur" ;	
	public static final String CODE_TRADUCTION_SAVE_ERROR = "session.save.error" ;	
	public static final String CODE_TRADUCTION_IF_LOGIN_URL_NO_MATCH = "session.if-and-login-urlhote-no-match" ;	
	
	public SessionBag() {
		
		// set id as UUI
		this.id = UUID.randomUUID().toString() ;
		
		// set creation date
		if (this.creationDateTime == null) this.creationDateTime = OffsetDateTime.now() ;
	}
	
	//UUID
	private String id = null ;
	
	// locale code
	@NotNull
	//@Column(columnDefinition = "locale varchar(50)")
	private Locale locale = null ;	
	
	@NotNull
	private OffsetDateTime creationDateTime =  OffsetDateTime.now() ;
	
	// if null, then no expiration 
	private OffsetDateTime expirationDateTime =  null ;
	
	//@Column(length = 50)
	private String userId = null ;
	
	private String fullname = null ;
	
	// logged in user operator
	// if null, then the operator is the Platform manager
	//@Column(length = 50)
	private String operatorId = null ;
	
	// delimited list of logged user role ids
	// delimitor: AppUtil.SESSION_ROLES_STRING_DELIMITER
	//@Lob
	//private String roleIds = null ;
	
	// delimited list of logged user group ids
	// delimitor: AppUtil.SESSION_ROLES_STRING_DELIMITER
	//@Lob
	//private String groupIds = null ;
	
	//@Version
	//protected Long version = null ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Locale getLocale() {
		return locale;
	}

	/** locale code as laguageISOAplha3-2letterCountryCode
	 * @return
	 */
	public String getLocaleCode() {	
		return LocaleUtil.serialiseLocale(locale) ;
	}

	public void setLocaleCode(String s) {	
		this.locale = LocaleUtil.deserialiseLocale(s) ;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public OffsetDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(OffsetDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public OffsetDateTime getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(OffsetDateTime expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	/*public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}*/

	public boolean isExpired() {

		if (expirationDateTime == null) return false ;
		
		return expirationDateTime.isBefore(OffsetDateTime.now()) ;
	}

	
	public Map<String, String> getMsgTranslationVarMap() {
	
		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionBag other = (SessionBag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SessionBag [id=" + id + ", locale=" + locale + ", creationDateTime=" + creationDateTime
				+ ", expirationDateTime=" + expirationDateTime + ", userId=" + userId + ", operatorId=" + operatorId
				 + "]";
	}

}
