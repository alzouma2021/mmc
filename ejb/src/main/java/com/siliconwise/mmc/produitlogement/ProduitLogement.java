package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;


/**
 * Cette classe renferme les informations relatives aux produitslogements
 * 
 * Identite Domaniale : code
 * 
 * @author Alzouma Moussa Mahamadou
 * @date de creation 08/01/2021
 *
 *
 */


@NamedEntityGraphs(value={
		
		//Retourne juste les propriétés nécessaires pour la remise en persistence de ladite entité
		
		/**
		 * 
		 * Ce nameGraph retourne un produit logement avec deux propiétés: id et estActive
		 * Utilisation de ce namedGraph pour remettre un produit logement dans le contexte de persistence
		 *
		 */
		@NamedEntityGraph(
		     name="graph.produitLogement.id.estActive",
			 attributeNodes= {
				  @NamedAttributeNode(value="id"),
				  @NamedAttributeNode(value="estActive")
		     })
		
})

@NamedQueries(value={
		
		/*Recherche d'un produit logement par son Id*/
		
		@NamedQuery(
				name="rechecherUnProduitLogementparId",
				query="SELECT p FROM ProduitLogement p , in(p.caracteristiqueProduitLogementList) cpl "
					+ "WHERE p.id = :idProduitLogement") ,
		
		/*Retourne tous les produits logements disponibles*/
		
		@NamedQuery(
				name="trouverTousLesProduitsLogements",
				query="SELECT pl FROM ProduitLogement pl"),
		
		/*Recherche de produits logements dont les propriétés ont le mot clé donné */
		
		@NamedQuery(
				name="trouverTousLesProduitsLogementsDontLesProprietesOntLeMotCle",
				query="SELECT pl FROM ProduitLogement pl "
					+ "WHERE "
					+ " pl.estActive = true "
					+ " AND ( pl.designation like :motCle "
					+ "	 		OR pl.code like :motCle "
					+ "  		OR pl.description like :motCle ) "),
		
		/*Recherche de produit logement par Id et par ProgrammeImmobilier*/
		
		@NamedQuery(
				name= "produitLogementIdParCodeParProgrammeImmobilier", 
				query = "SELECT DISTINCT pl.id FROM ProduitLogement pl "
						+ "WHERE pl.code = :code and pl.programmeImmobilier.code = :programmeimmobilier"),
		
		/*Recherche de produits logements par promoteur */
		
		@NamedQuery(
				name="rechercheProduitLogementsParPromoteur",
				query="SELECT pl FROM ProduitLogement pl "
						+ "WHERE pl.programmeImmobilier.promoteur.id = :promoteurId"),
		
		/*Verifier si le produit logement est actif */
		
		@NamedQuery(
				name="verifierProduitLogementActif",
				query="SELECT pl FROM ProduitLogement pl "
						+ "WHERE pl.id = :produitLogementId"),
		
		
		/*Requête pour un produit logement en fonction de l'Id*/
		
		@NamedQuery(
				name="supprimerUnProduitLogement",
				query="DELETE  FROM ProduitLogement pl "
						+ "WHERE pl.id = :produitLogementId"),
		
		
	   /* Retourne les metadatas de l'image de consultation d'un produit logement */
		
		@NamedQuery(
				name="retourneLIdDuneImageConsultationEnFonctionProduitLogement",
				query="SELECT pl FROM   ProduitLogement pl "
						+ "WHERE pl.id = :produitLogementId "),
		
		
	
	/* Supprimer des produits logements par promoteur */
		
		@NamedQuery(
				name= "supprimerProduitLogementParPromoteur", 
				query = "DELETE  FROM ProduitLogement pl "
						+ " WHERE pl.programmeImmobilier.promoteur.id = :idPromoteur  ")
	 
		
})
@Entity
@Table(
	name = "produitlogement",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"code" , "programmeimmobilier_id" })
	}
)
@EntityListeners(UUIDGeneratorEntityListener.class)
public class ProduitLogement implements Serializable, 
										IEntityStringkey, IEntityMsgVarMap{

	private static final long serialVersionUID = 1L;
	
	//Creation des constantes pour les valeurs des codes de messages
	
	public static final String CODE_TRADUCTION_NOM_USUEL = "produitLogement.nomUsuel" ;

	public static final String CODE_TRADUCTION_PRODUIT_LOGEMENT_ACTIVE_NON_VALIDE_PAR_PROMOTEUR = "produitLogement.active.nonValide-ParPromoteur";

	public static final String CODE_TRADUCTION_PRODUIT_LOGEMENT_ACTIVE_PROGRAMME_IMMOBILIER_NON_PAR_PROMOTEUR = "produitLogement.active.programmeImmobilier.nonValide-ParPromoteur";

	public static final String CODE_TRADUCTION_PRODUIT_LOGEMENT_DEVALIDE_ACTIF = "produitLogement.devalide-actif";

	public static final String CODE_TRADUCTION_PRODUIT_LOGEMENT_ACTIVE_PROGRAMME_IMMOBILIER_MODE_FINANCEMENT_NON_VALIDE = "produitLogement.active.programmeImmobilier.modeFinancement.non-valide";

	public static final String CODE_TRADUCTION_PRODUIT_LOGEMENT_NON_ACTIVE = "produitLogement.non-active";

	public static final String CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI = "produitLogementId.non-defini";

	public static final String CODE_TRADUCTION_PRODUITLOGEMENT_ACTIF = "produitLogement.actif";

	
	

	@Id
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doproduitLogement.nomUsuelit depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "Svp renseingnez la designation svp")
	@Column(length = 150)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	@Lob
	private String description = null;

	@NotNull(message = "Le code ne doit pas être nul")
	@NotEmpty(message = "Le code ne doit pas être vide")
	@Column(name = "code", length = 150 ,unique=true)
	@Size(max = 150, message = "La taille du code ne doit pas depasser 50 caratères")
	private String code ;


	//ManyToOne ProduitLogement vers ProgrammeImmobilier
	@ManyToOne
	@NotNull(message = "jpa_not_null_VarN")
	//@NotEmpty(message = "Le programme immobilier ne doit pas être vide")
	private ProgrammeImmobilier programmeImmobilier ;

	// OneToMAny ProduitLogement vers CaracteristiqueProduitLogement
    //@OneToMany
	@OneToMany(mappedBy="produitLogement", cascade = (CascadeType.REMOVE) ,fetch = FetchType.EAGER )
	private Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList ;
	

	//Cette propriété de type Boolean permet au promoteur de valider un produit logement 
	@Column(nullable = true )
	private Boolean estValide = null;
	
	//Cette propriété de type Boolean permet au promoteur d'activer le produit logement pour commercialisatio
	@Column(nullable = true )
	private Boolean estActive = null;
	
	@Version
	private Integer version ;

	public ProduitLogement() {
		super();
	}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ProgrammeImmobilier getProgrammeImmobilier() {
		return programmeImmobilier;
	}

	public void setProgrammeImmobilier(ProgrammeImmobilier programmeimmobilier) {
		this.programmeImmobilier = programmeimmobilier;
	}

	public Set<CaracteristiqueProduitLogement> getCaracteristiqueProduitLogementList() {
		return caracteristiqueProduitLogementList;
	}

	public void setCaracteristiqueProduitLogementList(
			Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList) {
		this.caracteristiqueProduitLogementList = caracteristiqueProduitLogementList;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public Boolean getEstValide() {
		return estValide;
	}

	public void setEstValide(Boolean estValide) {
		this.estValide = estValide;
	}

	public Boolean getEstActive() {
		return estActive;
	}

	public void setEstActive(Boolean estActive) {
		this.estActive = estActive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((programmeImmobilier == null) ? 0 : programmeImmobilier.hashCode());
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
		ProduitLogement other = (ProduitLogement) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (programmeImmobilier == null) {
			if (other.programmeImmobilier != null)
				return false;
		} else if (!programmeImmobilier.equals(other.programmeImmobilier))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "ProduitLogement [id=" + id + ", designation=" + designation + ", code=" + code
				+ ", programmeImmobilier=" + programmeImmobilier + ", caracteristiqueProduitLogementList="
				+ caracteristiqueProduitLogementList + ", version=" + version + "]";
	}

	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, ProduitLogement.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}

}
