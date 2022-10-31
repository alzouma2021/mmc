
package com.siliconwise.common.config.data;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
/**
 * Parametre de configuration de nivgeau global (configuré au niveau du serveur central est ensuite transféré à tous es sites)
 * @author GNAKALE Bernardin
 *
 * @param <T>
 */
@NamedQueries(value={
		@NamedQuery(
			name="findGlobalParameterByDesignation",
			query="SELECT p FROM GlobalParameter p WHERE p.designation=:designation"
		),
		@NamedQuery(
			name="findAllGlobalParameters",
			query="SELECT p FROM GlobalParameter p"
		)
})
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "globalparameter")
@Entity @EntityListeners(UUIDGeneratorEntityListener.class)
public abstract class GlobalParameter<T> extends Parameter<T> {

	private static final long serialVersionUID = 1L;
	
	// variable 1: code de la variable
	public static final String CODE_TRADUCTION_GLOBAL_PARAMETER_RETRIEVAL_ERROR_1V = "entity.global-parameter.retrieval.error" ;

	public static final String CODE_TRADUCTION_NOT_DEFINED = "entity.global-parameter.not-defined" ;

	// vriable 1: gloal paraeter id
	public static final String CODE_TRADUCTION_NOT_FOUND_1V = "entity.global-parameter.not-found.1v" ;
	public static final String CODE_TRADUCTION_PERSISTENCE_INTIGRITY_ERROR = "entity.global-parameter.intigrity-error.1v"; 
	public static final String CODE_TRADUCTION_PERISTENCE_ERREUR = "entity.global-parameter.persistence-error.1v" ; 
	public static final String CODE_TRADUCTION_IF_EXISTE = "entity.global-parameter.exist.1v";
	public static final String CODE_TRADUCTION_LINKED_REFERENCE_ATTACHMENT_ERROR = "entity.global-parameter.linked-entities-attachment.error" ;
	public static final String CODE_TRADUCTION_REFERENCE_NON_TROUVE = "entity.global-parameter.reference-not-found" ;


	@Override
	public String toString() {
		return "GlobalParameter [designation=" + getDesignation() + "]";
	}
	
	
}
