package com.siliconwise.common.event.historique;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;


/**
 * Entity event log class.
 * @author sysadmin
 *
 */
@Entity
@Table(name = "history")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class History implements Serializable, IEntityStringkey, IEntityMsgVarMap{

	
	private static final long serialVersionUID = 1L;
	
	// Translation mvariables
	
	// usual designation
	public static final String TRANSLATION_VARIABLE_USUAL_DESIGNATION  = "entity.history.variable.usualDesignation";
	
	// Variable: target entity absolute name
	public static final String TRANSLATION_VARIABLE_TARGET_ENTITY_CLASS_ABSOLUTE_NAME = "entity.history.variable.targetEntity.absoluteName" ;
	
	// Variable: target entity id value
	public static final String TRANSLATION_VARIABLE_TARGET_ENTITY_ID_VALUE = "entity.history.variable.targetEntity.idValue" ;
	
	// codes de messages
		
		
	//Variables : CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL
	
	// Exemple de message: Erreur d'historisation. entité: TRANSLATION_VARIABLE_TARGET_ENTITY_CLASS_ABSOLUTE_NAME, id= TRANSLATION_VARIABLE_TARGET_ENTITY_ID_VALUE
	public static final String TRANSLATION_MESSAGE_ENTITY_PERSISTENCE_UNKNOWN_ERROR = "entite.history.persistence.unknownError";

	
	// Code de traduction
	// TODO mettre dans les fichiers de traduction
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CREATION = "translate.entity.historique.event.creation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_MODIFICATION = "translate.entity.historique.event.modification" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_ACTIVATION = "translate.entity.historique.event.activation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DESACTIVATION = "translate.entity.historique.event.desactivation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CONNEXION = "translate.entity.historique.event.connexion" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DECONNEXION = "translate.entity.historique.event.deconnexion" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_SUPPRESSION = "translate.entity.historique.event.suppression" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_VALIDATION = "translate.entity.historique.event.validation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CONFIRMATION = "translate.entity.historique.event.confirmation" ;
	
	public static final String CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE = "";

	
	public static enum HistoryEventType {
		
		// constante d'enumeration
		
		CREATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CREATION), 
		
		MODIFICATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_MODIFICATION), 
		
		ACTIVATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_ACTIVATION), 
		
		DESACTIVATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DESACTIVATION),
		
		CONNEXION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CONNEXION),
		
		SUPPRESSION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_SUPPRESSION),
		
		VALIDATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_VALIDATION),
		
		CONFIRMATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CONFIRMATION),
		
		DECONNEXION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DECONNEXION); 
		
		// code de traduction
		
		private String codeTr = null ; 
		
		private HistoryEventType(String codeTr){
			this.codeTr = codeTr ;
		}

		public String getCodeTr() {
			return codeTr;
		} 
		
		
	}
	

	//@NotNull(message = "L'Id ne doit pas être nul")
	//@NotEmpty(message = "L'Id ne doit pas être vide")
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private  String id = null ;
	
	//@NotNull(message = "L'Id de l'utilisateur ne doit pas être pas nul")
	//@NotEmpty(message = "L'Id de l'utilisateur ne doit pas être pas vide")
	@Column(length = 50)
	private String userId = null ; // user id
	
	//@NotNull(message = "Le nom de l'utilisateur ne doit pas être pas nul")
	//@NotEmpty(message = "L'Id de l'utilisateur ne doit pas être pas vide")
	@Column(length = 150)
	private String userName = null ; // user first nama and name
	
	//  event
	@NotNull(message = "l'evenement ne doit pas être nul")
	@Column(length = 150)
	@Enumerated(EnumType.STRING)
	private HistoryEventType eventType = null ; 
	
	@Column(length = 254)
	private String classAbsuluteNAme = null ; // class absolute name (with package)
	
	@Column(length = 50)
	private String entityId = null ; // entity instance id
	
	@NotNull
	private OffsetDateTime dateEtHeureEvenement ;
	
	@Lob
	private String observation = null;

	@Version
	private Integer version ;
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public HistoryEventType getEventType() {
		return eventType;
	}
	public void setEventType(HistoryEventType eventType) {
		this.eventType = eventType;
	}
	public String getClassAbsuluteNAme() {
		return classAbsuluteNAme;
	}
	public void setClassAbsuluteNAme(String classAbsuluteNAme) {
		this.classAbsuluteNAme = classAbsuluteNAme;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
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
		History other = (History) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}
	
	
	@Override
	public String toString() {
		return "History [id=" + id + ", userId=" + userId + ", userName=" + userName + ", eventType=" + eventType
				+ ", classAbsuluteNAme=" + classAbsuluteNAme + ", entityId=" + entityId + ", version=" + version + "]";
	}
	
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		// -- general variables --
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, 
				History.TRANSLATION_VARIABLE_USUAL_DESIGNATION) ;
		
		/*rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, 
				getEventType().codeTr) ;*/
		
		rtn.put(AppMessageKeys.GENERAL_VARIABLE_CLASS_ABSOLUTE_NAME, 
				this.getClass().getName()) ;
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, 
				this.getId()) ;
		
		// -- specific variables 
		
		rtn.put(TRANSLATION_VARIABLE_TARGET_ENTITY_CLASS_ABSOLUTE_NAME, 
				this.getClassAbsuluteNAme()) ;
		
		rtn.put(TRANSLATION_VARIABLE_TARGET_ENTITY_ID_VALUE, 
				this.getEntityId()) ;
		
		return rtn ;
		
		
	}
	
	
	
}
