package com.siliconwise.mmc.organisation.efi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.Pays;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.organisation.IOrganisation;

@NamedQueries(value={
	
		//La liste de programmes immobiliers créés
		
		@NamedQuery(
				name= "toutesLesEfis", 
				query = "SELECT ef FROM EFi ef "),
		
		
		// Liste des adresses mail des EFis non  partenaires à un programme immmobilier
		
		@NamedQuery(
				name="listeEmailEfisNonPartenairesProgrammeImmbolier",
				query="SELECT  fi.email FROM EFi fi "
					  +   "WHERE fi.email  "
					  +			   "NOT IN "
					  + 			   "(SELECT mf.creditBancaire.eFi.email FROM ModeFinancement mf " 
					  + 					 "WHERE mf.programmeImmobilier.id = :idProgrammeImmobilier "
					  + 							 " AND  mf.typeFinancement.estvalideParEfi = true ) " 
					  +           "AND fi.pays.id = :idPaysPromoteur")  ,
		
		
		
		

		//Recherche de l'identifiant d'un efi par email
		
		@NamedQuery(
				name= "efiIdParEmaill", 
				query = "SELECT DISTINCT e.id FROM EFi e "
							   + "WHERE e.email = :email ")
		
})
@Entity
@Table(name = "efi")
@EntityListeners(UUIDGeneratorEntityListener.class)
public class EFi implements Serializable, IEntityStringkey, IEntityMsgVarMap, IOrganisation{

	
	private static final long serialVersionUID = 1L;
	
	//Creation des constantes pour les valeurs des codes de messages
	
	public static final String CODE_TRADUCTION_NOM_USUEL = "efi.nomUsuel" ;

	public static final String CODE_TRADUCTION_EFI_ADRESSE_MAIL_NON_DEFINIE = "efi.adresse.mail.non-definie";


	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	@Id
	@Column(length = 50)
	private String id = null ;
	
	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;
	
	@Lob
	private String description = null ;
	
	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "La designation ne doit pas être vide")
	@Column(length = 255)
	@Size(max = 255, message = "La taille max est dépassée")
	private String adresse;

	@NotNull(message = "La raison sociale ne doit pas être nulle")
	@NotEmpty(message = "La raison sociale ne doit pas être vide")
	@Column(length = 50)
	@Size(max = 50, message = "La taille max de la raison sociales est de 50 carateres")
	private String raisonSociale;

	@NotNull(message = "Le sigle ne doit pas etre nul")
	@NotEmpty(message = "Le Sigle ne doit pas etre vide")
	@Column(length = 50)
	@Size(max = 50, message = "La taille ne doit pas dépasser 50 caratères")
	private String sigle;

	@NotNull(message = "Le sigle ne doit pas etre nul")
	@NotEmpty(message = "Le Sigle ne doit pas etre vide")
	@Column(length = 150, unique = true)
	@Size(max = 150, message = "La taille ne doit pas dépasser 150 caratères")
	private String identifiantLegal;
	
	
	@NotNull(message = "L'email ne doit pas etre nul")
	@NotEmpty(message = "L'email ne doit pas etre vide")
	@Column(length = 50)
	@Size(max = 50, message = "La taille ne doit pas dépasser 50 caratères")
	private String tel;
	
	@NotNull(message = "L'email ne doit pas être nul")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150, unique = true)
	@Size(max = 150, message = "La taille max est dépassée")
	private String email ;
	
	
	private Boolean estActive ;
	
	@ManyToOne
	private Pays pays ;
	
	
	@NotNull(message = "L'email de l'administrateur de l'organisation ne doit pas etre nul")
	@NotEmpty(message = "L'email de l'administrateur de l'organisation ne doit pas etre vide")
	private String emailAdmin ;
	
	
	@NotNull(message = "Le nom de l'administrateur de l'organisation ne doit pas etre nul")
	@NotEmpty(message = "Le nom de l'administrateur de l'organisation ne doit pas etre vide")
	private String nomAdmin ;
	
	
	@NotNull(message = "Le prenom de l'administrateur de l'organisation ne doit pas etre nul")
	@NotEmpty(message = "Le prenom de l'administrateur de l'organisation ne doit pas etre vide")
	private String prenomAdmin ;
	
	
	@Version
	private Integer version = null ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Boolean getEstActive() {
		return estActive;
	}

	public void setEstActive(Boolean estActif) {
		this.estActive = estActif;
	}
	
	public Pays getPays() {
		return pays;
	}

	public void setPays(Pays pays) {
		this.pays = pays;
	}
	
	public String getEmailAdmin() {
		return emailAdmin;
	}

	public void setEmailAdmin(String emailAdmin) {
		this.emailAdmin = emailAdmin;
	}

	public String getNomAdmin() {
		return nomAdmin;
	}

	public void setNomAdmin(String nomAdmin) {
		this.nomAdmin = nomAdmin;
	}

	public String getPrenomAdmin() {
		return prenomAdmin;
	}

	public void setPrenomAdmin(String prenomAdmin) {
		this.prenomAdmin = prenomAdmin;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getRaisonSociale() {
		return raisonSociale;
	}

	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public String getSigle() {
		return sigle;
	}

	public void setSigle(String sigle) {
		this.sigle = sigle;
	}

	public String getIdentifiantLegal() {
		return identifiantLegal;
	}

	public void setIdentifiantLegal(String identifiantLegal) {
		this.identifiantLegal = identifiantLegal;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
	Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, EFi.CODE_TRADUCTION_NOM_USUEL) ;
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}

	
}
