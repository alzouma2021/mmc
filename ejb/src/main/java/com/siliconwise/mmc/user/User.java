package com.siliconwise.mmc.user;

import java.io.Serializable;
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
 * @date 11/01/2021
 *
 */
@NamedQueries(value = {
		
		//Rechercher l'identifiant d'un utilisateur par email
		
		@NamedQuery(
			name= "userIdParEMail", 
			query = "SELECT DISTINCT u.id FROM User u WHERE u.email = :email " ),
		
		//Rechercher un utilisateur par son email
		
		@NamedQuery(
				name= "userParEMail", 
				query = "SELECT DISTINCT u FROM User u WHERE u.email = :email " ),
		
		@NamedQuery(
				name= "countUserIdById", 
				query = "SELECT COUNT(DISTINCT u.id) FROM User u WHERE u.id = :id "),

		// Count nbr of users given email and password 
		
		@NamedQuery(
				name = "countUserByEmailAndPassWord",
				query = "SELECT COUNT(DISTINCT u.id) FROM User u "
						   + "WHERE u.email = :email AND u.motDePasse = :motDePasse"),
				
		// user given email and password 
		
		@NamedQuery(
				name = "userByEmailAndPassword",
				query = "SELECT DISTINCT u FROM User u "
						  + "WHERE u.email = :email AND u.motDePasse = :motDePasse")
		
		
		
	})
@NamedEntityGraphs(value={
		
		@NamedEntityGraph(
			name="graph.user.minimum", 
			attributeNodes={
				@NamedAttributeNode(value="id"),
				@NamedAttributeNode(value="nom"),
				@NamedAttributeNode(value="prenom"),
				@NamedAttributeNode(value="adresse"),
				@NamedAttributeNode(value="email"),
				@NamedAttributeNode(value="estActive"),
				@NamedAttributeNode(value="motDePasse"),
				@NamedAttributeNode(value="locale"),
				@NamedAttributeNode(value="estActive"),
				@NamedAttributeNode(value="version")
			})
})
@Entity
@Table(name = "user")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class User implements Serializable, IEntityStringkey , IEntityMsgVarMap {

	private static final long serialVersionUID = 1L;



	public static final String CODE_TRADUCTION_USER_NOT_CONNECTED = "entite.user.not-connected";

	public static final String CODE_TRADUCTION_CURRENT_PASSWORD_NOT_DEFINED = "entite.current.password.not-defined";

	public static final String CODE_TRADUCTION_NEW_PASSWORD_NOT_DEFINED = "entite.new.password.not-defined";

	public static final String CODE_TRADUCTION_CURRENT_PASSWORD_NOT_CORRECT = "entite.current.password.not-defined";


	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La Taille de l'Id ne doit pas depasser 50 caratères")
	private String id;
	
	private String nom;
	
	private String prenom ;
	
	private String adresse;

	@NotNull(message = "L'email ne doit pas être nul")
	@NotEmpty(message = "L'email ne doit pas être vide")
	@Column(length = 50, unique = true)
	@Size(max = 50, message = "L'email ne doit pas dépasser 50 caratères")
	private String email;

	private String telephone;
	
	private Boolean estActive ;

	@Column(length = 255)
	@Size(max = 50, message = "le mot de passe ne doit pas dépasser 50 caratères")
	private String motDePasse;
	
	// ManyToOne Promoteur vers Ville
	
	@ManyToOne
	private Ville ville = null;

	private Locale locale  ;
	
	@Version
	private Integer version;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Boolean getEstActive() {
		return estActive;
	}

	public void setEstActive(Boolean estActive) {
		this.estActive = estActive;
	}
	
	public Ville getVille() {
		return ville;
	}

	public void setVille(Ville ville) {
		this.ville = ville;
	}
	
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	
	public String getFullname() {
		
		return nom;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", adresse=" + adresse + ", email=" + email
				+ ", telephone=" + telephone + ", estActive=" + estActive + ", motDePasse=" + motDePasse + ", ville="
				+ ville + ", locale=" + locale + ", version=" + version + "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, getNom()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE,getEmail()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	
	
	
}
