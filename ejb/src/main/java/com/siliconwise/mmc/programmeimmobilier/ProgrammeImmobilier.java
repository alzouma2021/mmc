package com.siliconwise.mmc.programmeimmobilier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siliconwise.common.Ville;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;


/**
 * Cette classe renferme les informations relatives à un programme immobilier
 * 
 * Identitté Domaniale :
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 11/01/2021
 *
 *
 */

@NamedEntityGraphs(value={
		
		//Retourne juste les propriétés nécessaire pour la remise en persistence de ladite entité
		
		/**
		 * Ce nameGraph retourne un programme immobilier avec une propriété: id
		 * Utilisation de ce namedGraph pour remettre le programme immobilier  dans le contexte de persistence
		 *
		 */
		@NamedEntityGraph(
		     name="graph.programmeImmobilier.id",
			 attributeNodes= {
				  @NamedAttributeNode(value="id"),
		     })
		
})



@NamedQueries(value={
		
		/* Programme immobilier par promoteur*/
		
		@NamedQuery(
				name= "programmeImmobilierIdParCodeParPromoteur", 
				query = "SELECT DISTINCT pi.id FROM ProgrammeImmobilier pi "
						+ "WHERE pi.code = :code "
						+ "AND pi.promoteur.identifiantLegal = :promoteur"),
		
		/* Liste de produits logements par programme immobilier*/
		
		@NamedQuery(
				name= "produitLogementIdParProgrammeImmobilier", 
				query = "SELECT DISTINCT pl FROM ProduitLogement pl "
						+ "WHERE  pl.programmeImmobilier.code = :programmeimmobilier "
						+ "       AND pl.estValide = true "),
		
		
		/*Liste de modes de financements validés , rattachés à un programme immobilier*/
		
		@NamedQuery(
				name= "nombreModeFinancementValideParProgrammeImmobilier", 
				query = "SELECT COUNT(DISTINCT mf)  FROM  ModeFinancement mf "
						+ "WHERE   mf.programmeImmobilier.id = :idProgrammeImmobilier "
						+ "		   AND mf.estValide = true "),
		
		/*La liste de programmes immobiliers créés*/
		
		@NamedQuery(
				name= "toutesLesProgrammesImmobiliers", 
				query = "SELECT pi FROM ProgrammeImmobilier pi"),
		
		/*Recherche de programme immobiliers par promoteur*/
		
		@NamedQuery(
				name= "rechercherProgrammeImmobilierParPromoteur", 
				query = "SELECT pi FROM ProgrammeImmobilier pi "
						+ "WHERE pi.promoteur.id = :promoteurId "),
		
		/*Requête pour vérifier si le programme immobilier est validé */
		
		@NamedQuery(
				name= "programmeImmobilierValide", 
				query = "SELECT COUNT(DISTINCT pi) FROM ProgrammeImmobilier pi "
						+ "WHERE pi.id = :idProgrammeImmobilier "
						+ "AND pi.estValide = true "),
		
	   /*Requête pour retourner tous les produits logements par programme immobilier*/
		
		@NamedQuery(
				name= "produitLogementsParProgrammeImmobilierId", 
				query = "SELECT COUNT(DISTINCT pl) FROM ProduitLogement pl "
						+ "WHERE pl.programmeImmobilier.id = :idProgrammeImmobilier " ),
		
	  /*Requête pour supprimer un programme immobilier en fonction de son Id */
	  
		@NamedQuery(
				name="supprimerUnProgrammeImmobilier",
				query="DELETE FROM ProgrammeImmobilier pi "
						+ "WHERE pi.id = :programmeImmobilierId"),
		
	
	 /* Supprimer des produits logements par promoteur */
		
		@NamedQuery(
				name= "supprimerProgrammeImmobilierParPromoteur", 
				query = "DELETE  FROM ProgrammeImmobilier pi "
						+ " WHERE pi.promoteur.id = :idPromoteur  ")
	 
		
})
@Entity
@Table(name = "programmeimmobilier",
		uniqueConstraints = {
		@UniqueConstraint(columnNames = {"code" , "promoteur_id" })
	})
@EntityListeners(UUIDGeneratorEntityListener.class)
public class ProgrammeImmobilier implements Serializable, IEntityStringkey, IEntityMsgVarMap {

	private static final long serialVersionUID = 1L;

	//Creation des constantes pour les valeurs des codes de messages
	
	public static final String CODE_TRADUCTION_NOM_USUEL = "programmeImmobilier.nomUsuel" ;

	public static final String CODE_TRADUCTION_PROGRAMME_IMMOBILIER_VALIDE_PAR_PROMOTEUR_PRODUIT_LOGEMENT_NON_TROUVE = "programmeImmobilier.valideParPromoteur.ProduitLogement.non-trouve";

	public static final String CODE_TRADUCTION_PROGRAMME_IMMOBILIER_VALIDE_PAR_EFI_NON_PAR_PROMOTEUR = "programmeImmobilier.valideParEFi.nonValide-ParPrmoteur";

	public static final String CODE_TRADUCTION_PROGRAMMEIMMOBILIER_NON_DEFINIE = "programmeImmobilier.demande.non-valide";

	public static final String CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI = "programmeImmobilier.identifiant-non-defini";

	public static final String CODE_TRADUCTION_PROGRAMMEIMMOBILIER_A_AU_MOINS_UN_PRODUILOGEMENT = "programmeImmobilier.avoir-produitLogement";


	@Id
	//@NotNull(message = "L'Id ne doit pas être nul")
	//@NotEmpty(message = "L'Id ne doit pas être vide")
	@Column(length = 50)
	@Size(max = 50, message = "La taille de l'Id ne doit depasser 50")
	private String id;

	@NotNull(message = "La designation ne doit pas être nulle")
	@NotEmpty(message = "La designation ne doit pas être vide")
	@Column(length = 150)
	@Size(max = 150, message = "La taille max est dépassée")
	private String designation ;

	@Lob
	private String description = null;

	@NotNull(message = "Le code ne doit pas être nul")
	@NotEmpty(message = "Le code ne doit pas être vide")
	@Column(name = "code", length = 50 , unique=true)
	@Size(max = 50, message = "La taille du code ne doit pas depasser 50 caratères")
	private String code ;

	//ManyToOne ProgrammeImmobilier vers Promoteur
	@ManyToOne
	@NotNull(message = "Le promoteur ne doit pas être nul")
	private Promoteur promoteur ;
	
	
	//Cette propriété de type de Boolean permet au promoteur de valider un programme immobilier. 
	private Boolean estValide = null ;
		
	//ManyToOne
	@NotNull(message = "La ville ne doit pas être nulle")
	@ManyToOne
	private Ville ville ;
	
	@Version
	private Integer version ;

	public ProgrammeImmobilier() {
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

	public Promoteur getPromoteur() {
		return promoteur;
	}

	public void setPromoteur(Promoteur promoteur) {
		this.promoteur = promoteur;
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

	public Ville getVille() {
		return ville;
	}

	public void setVille(Ville ville) {
		this.ville = ville;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((promoteur == null) ? 0 : promoteur.hashCode());
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
		ProgrammeImmobilier other = (ProgrammeImmobilier) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (promoteur == null) {
			if (other.promoteur != null)
				return false;
		} else if (!promoteur.equals(other.promoteur))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProgrammeImmobilier [code=" + code + ", promoteur=" + promoteur + "]";
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_NOM_USUEL, ProgrammeImmobilier.CODE_TRADUCTION_NOM_USUEL) ;
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTITE_NATURELLE_USUELLE, getDesignation()) ;
		
		return rtn;
		
	}
}
