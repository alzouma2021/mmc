/**
 * 
 */
package com.siliconwise.common.config.file;

import java.io.Serializable;

/**
 * Application configuration file keys definition
 * @author GNAKALE Bernardin
 *
 */
//TODO Create system configuration file with tese variables set
public class DataTestKeys implements Serializable {

    // Application parameters
	
	private static final long serialVersionUID = 1L;

	public static final String SYSTEM_CONFIG_FILE_NAME = "nims.config.properties" ;
	
	public static final String SYSTEM_ENV_VAR_APPLICATION_HOME = "NIMS_HOME";
	public static final String SYSTEM_ENV_VAR_APPLICATION_HOME_DEFAULT_VALUE = "c:\\nims";

	//sous un server de type linux 
	//public static final String SYSTEM_ENV_VAR_APPLICATION_HOME_DEFAULT_VALUE = "/opt/smartsafe";

	//
	public static final String DONNEES_TEST_INFOPERSO_NOM = "nom" ;
	public static final String DONNEES_TEST_INFOPERSO_PRENOM = "prenom" ;
	public static final String DONNEES_TEST_INFOPERSO_NOMJEUNEFILLE = "nomjeunefille";
	public static final String DONNEES_TEST_INFOPERSO_EMAIL = "email" ;
	public static final String DONNEES_TEST_INFOPERSO_PAYSNAISSSANCE = "paysnaissance" ;
	public static final String DONNEES_TEST_INFOPERSO_DATENAISSANCE = "datenaissance" ;
	public static final String DONNEES_TEST_INFOPERSO_NATIONNALITE = "nationnalite" ;
	public static final String DONNEES_TEST_INFOPERSO_CIVILITE = "civilite" ;
	public static final String DONNEES_TEST_INFOPERSO_SITUATIONMATRIMONIAL= "situationMatrimoniale" ;
	public static final String DONNEES_TEST_INFOPERSO_NOMPERE = "nompere" ;
	public static final String DONNEES_TEST_INFOPERSO_NOMMERE = "nommere" ;
	public static final String DONNEES_TEST_INFOPERSO_PRENOMPERE = "prenompere" ;
	public static final String DONNEES_TEST_INFOPERSO_PRENOMMERE	= "prenommere" ;
	public static final String DONNEES_TEST_INFOPERSO_NOMBREENFANT = "nombreenfant" ;
	public static final String DONNEES_TEST_INFOPERSO_ESTCONTRIBUABLEAMERICAIN = "estcontribuableamericain" ;
	public static final String DONNEES_TEST_INFOPERSO_NOMPC = "nompc" ;
	public static final String DONNEES_TEST_INFOPERSO_PRENOMPC = "prenompc" ;
	public static final String DONNEES_TEST_INFOPERSO_TELEPHONEPORTABLEPC = "telephonePortablePC" ;
	public static final String DONNEES_TEST_INFOPERSO_PREFIXE = "prefixe" ;
	public static final String DONNEES_TEST_INFOPERSO_INFOIDENTIFICATION = "infoidentification" ;
	public static final String DONNEES_TEST_INFOPERSO_REFERENCEBANCAIRELISTE= "referencebancaireliste" ;
	public static final String DONNEES_TEST_INFOPERSO_EMAILPC= "emailpc" ;
	
	//
	public static final String DONNEES_TEST_CIVILITE_DESIGNATION = "civilite.designation" ; 
	public static final String DONNEES_TEST_CIVILITE_DESIGNATIONTR = "civilite.designationtr" ;
	public static final String DONNEES_TEST_CIVILITE_DESCRIPTION = "civilite.description" ;
	public static final String DONNEES_TEST_CIVILITE_ID = "civilite.id" ;
	
	public static final String DONNEES_TEST_SITUATIONMATRIMONIALE_DESIGNATION = "situationmatrimoniale.designation" ; 
	public static final String DONNEES_TEST_SITUATIONMATRIMONIALE_DESIGNATIONTR = "situationmatrimoniale.designationtr" ;
	public static final String DONNEES_TEST_SITUATIONMATRIMONIALE_DESCRIPTION = "situationmatrimoniale.description" ;
	public static final String DONNEES_TEST_SITUATIONMATRIMONIALE_ID = "situationmatrimoniale.id" ;
	
	
}
