package com.siliconwise.mmc.oldSecurity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

@NamedQueries(value = {
		
	// utilisateurpar login
	@NamedQuery(
		name = "nbrActiveSessionByUserId",
		query = "SELECT COUNT(s.id) FROM SessionBag s "
				+ "WHERE s.userId = :userId "
				+	"AND s.dateHeureExpiration > :dateHeureExpiraton ")
})
@Table(name = "sessionbag")
@Entity @EntityListeners(UUIDGeneratorEntityListener.class)
public class SessionBag implements IEntityStringkey, Serializable {

	private static final long serialVersionUID = 1L;
	
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
	
	@Id @Column(length = 50)
	private String id = null ;
	
	@NotNull(message = "La langue de l'utilisateur ne peut être null" )
	private String language = null ;	
	
	private LocalDateTime dateHeureCreation =  null ;
	private LocalDateTime dateHeureExpiration =  null ;
	
	@Column(name="loggedInUser_id")
	private String userId = null ;
	
	@NotNull(message="entity.sessionbag.ifId.notnull")
	@Column(name="if_id")
	private String intermediaiareFinancierId = null ;
	
	// delimited list of logged user role ids
	// delimitor: AppUtil.SESSION_ROLES_STRING_DELIMITER
	@Lob
	private String roleIds = null ;
	
	@Version
	protected Integer version = null ;

	public SessionBag () {
		this.dateHeureCreation = LocalDateTime.now() ;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getIntermediaiareFinancierId() {
		return intermediaiareFinancierId;
	}

	public void setIntermediaiareFinancierId(String intermediaiareFinancierId) {
		this.intermediaiareFinancierId = intermediaiareFinancierId;
	}

	public LocalDateTime getDateHeureCreation() {
		return dateHeureCreation;
	}

	public void setDateHeureCreation(LocalDateTime dateHeureCreation) {
		this.dateHeureCreation = dateHeureCreation;
	}

	public LocalDateTime getDateHeureExpiration() {
		return dateHeureExpiration;
	}

	public void setDateHeureExpiration(LocalDateTime dateHeureExpiration) {
		this.dateHeureExpiration = dateHeureExpiration;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	@Transient
	public boolean isExpired() {

		return this.dateHeureExpiration != null 
				&& this.dateHeureExpiration.isBefore(LocalDateTime.now()) ;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


	@Override
	public String toString() {
		return "SessionBag [id=" + id + ", language=" + language + ", dateHeureCreation=" + dateHeureCreation
				+ ", dateHeureExpiration=" + dateHeureExpiration + "]";
	}

	/*@Override
	public String[] getMessageVarList() {
		return new String[] {} ;
   	}
	*/
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
	
	
}
