package com.siliconwise.common.event.oldhistorique;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * Entity event log class.
 * @author sysadmin
 *
 */
@Entity
@Table(name = "historique")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class Historique implements Serializable, IEntityStringkey, IEntityMsgVarMap{

	private static final long serialVersionUID = -7564618664205713327L;
	
	
	//Creation des constantes pour les valeurs des codes de messages
		public static final String CODE_TRADUCTION_NOM_USUEL = "historique.nomUsuel";
		
	// codes de messages
	
	
	//TODO actualiser l'internationalisation
	/*
	public static final String CODE_TRADUCTION_VERSION_NOT_DEFINED = "" ;
	public static final String CODE_TRADUCTION_LINKED_REFERENCE_ATTACHMENT_ERROR = "" ;
	public static final String CODE_TRADUCTION_NOT_DEFINED = "" ;
	public static final String CODE_TRADUCTION_EXISTE = "" ;
	public static final String CODE_TRADUCTION_PERSISTENCE_INTIGRITY_ERROR = "" ;
	public static final String CODE_TRADUCTION_PERISTENCE_ERREUR = "" ;
	*/

	// Code de traduction
	// TODO mettre dans les fichiers de traduction
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CREATION = "translate.entity.historique.event.creation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_MODIFICATION = "translate.entity.historique.event.modification" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_VALIDATION = "translate.entity.historique.event.validation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_VALIDATION_EFI = "translate.entity.historique.event.validation-efi" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_ACTIVATION = "translate.entity.historique.event.activation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DESACTIVATION = "translate.entity.historique.event.desactivation" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CONNEXION = "translate.entity.historique.event.connexion" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DECONNEXION = "translate.entity.historique.event.deconnexion" ;
	public static final String CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_SUPPRESSION = "translate.entity.historique.event.suppression" ;
	

	public static final String CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE = "";

	public static enum HistoriqueEvent {
		
		// constante d'enumeration
		
		CREATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CREATION), 
		
		MODIFICATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_MODIFICATION), 
		
		VALIDATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_VALIDATION),
		
		// Activation (mise à la vente) produit logement 
		ACTIVATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_ACTIVATION), 
		
		// Desactivation Produit logement
		DESACTIVATION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DESACTIVATION),
		
		CONNEXION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_CONNEXION),
		
		SUPPRESSION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_SUPPRESSION),
		
		DECONNEXION(CODE_TRADUCTION_ENTITY_HISTORIQUE_EVENT_DECONNEXION); 
		
		
		// code de traduction
		private String codeTr = null ; 
		
		private HistoriqueEvent(String codeTr){
			this.codeTr = codeTr ;
		}

		public String getCodeTr() {
			return codeTr;
		} 
	}
	
	@NotNull(message = "L'Id ne doit pas être nul")
	@NotEmpty(message = "L'Id ne doit pas être vide")
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private  String id = null ;
	
	@NotNull(message = "L'Id de l'utilisateur ne doit pas être pas nul")
	@NotEmpty(message = "L'Id de l'utilisateur ne doit pas être pas vide")
	@Column(length = 50)
	private String userId = null ; // user id
	
	@NotNull(message = "Le nom de l'utilisateur ne doit pas être pas nul")
	@NotEmpty(message = "L'Id de l'utilisateur ne doit pas être pas vide")
	@Column(length = 150)
	private String userName = null ; // user first nama and name
	
	//  event
	@NotNull(message = "l'evenement ne doit pas être nul")
	@Enumerated(EnumType.STRING)
	@Column(length = 150)
	private HistoriqueEvent event = null ; 
	
	@Column(length = 254)
	private String classAbsuluteNAme = null ; // class absolute name (with package)
	
	@Column(length = 50)
	private String entityId = null ; // entity instance id
	
	private Integer version = null ;
	
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
	public HistoriqueEvent getEvent() {
		return event;
	}
	public void setEvent(HistoriqueEvent event) {
		this.event = event;
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
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version ;
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
		Historique other = (Historique) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Historique [id=" + id + ", userId=" + userId + ", userName=" + userName + ", event=" + event
				+ ", classAbsuluteNAme=" + classAbsuluteNAme + ", entityId=" + entityId + ", version=" + version + "]";
	}
	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, Historique.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getEvent().codeTr) ;
		
		return rtn;
		
	}

}
