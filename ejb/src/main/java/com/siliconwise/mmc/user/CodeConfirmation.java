package com.siliconwise.mmc.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.Ville;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * Cette classe renferme les informations sur le compte utilisateur
 * 
 * Identité Domaniale : email
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 7/10/2021
 *
 */
@NamedQueries(value = {
		
		//Rechercher de l'identifiant d'un code de confirmation par l'identité naturelle : idUser
		
		@NamedQuery(
			name= "codeConfirmationIdParIdUser", 
			query = "SELECT DISTINCT cc.id FROM CodeConfirmation cc WHERE cc.idUser = :idUser " )

	})
@Entity
@Table(name = "codeconfirmation")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class CodeConfirmation implements Serializable, IEntityStringkey , IEntityMsgVarMap {

	
	private static final long serialVersionUID = 1L;
	
	public static final  long NOMBRE_JOUR_AU_BOUT_DU_QUEL_LA_CONFIRMATION_EXPIRE = 2 ;

	public static final String CODE_TRADUCTION_COMPTE_USER_CONFIRME = "entite.compte.user.confirme";

	public static final String CODE_TRADUCTION_CODE_CONFIRMATION_NOMATCH = "entite.code.confirmation.nomatch";


	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La Taille de l'Id ne doit pas depasser 50 caratères")
	private String id;
	

	@NotNull(message = "L'id user ne doit pas être nul")
	@NotEmpty(message = "L'id user ne doit pas être vide")
	@Column(length = 50, unique = true)
	@Size(max = 50, message = "L'id user ne doit pas dépasser 50 caratères")
	private String idUser;

	
	private OffsetDateTime dateExpiration;
	

	@Version
	private Integer version;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public OffsetDateTime getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(OffsetDateTime dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateExpiration == null) ? 0 : dateExpiration.hashCode());
		result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
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
		CodeConfirmation other = (CodeConfirmation) obj;
		if (dateExpiration == null) {
			if (other.dateExpiration != null)
				return false;
		} else if (!dateExpiration.equals(other.dateExpiration))
			return false;
		if (idUser == null) {
			if (other.idUser != null)
				return false;
		} else if (!idUser.equals(other.idUser))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "CodeConfirmation [idUser=" + idUser + ", dateExpiration=" + dateExpiration + "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, getIdUser()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE,getIdUser()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	
	
}
