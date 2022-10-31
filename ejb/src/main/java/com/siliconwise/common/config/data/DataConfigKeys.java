/**
 * 
 */
package com.siliconwise.common.config.data;

/**
 * Global and local data configuration keys definition
 * @author GNAKALE Bernardin
 *
 */

public class DataConfigKeys {

	// Session and token lifetime in minute. Defaul is 20 min
	// long
	public static String GLOBAL_CONFIG_DATA_KEY_SESSION_EXPIRATION_DELAY = 
							"global.key.session.expiration-delay";
	public static long GLOBAL_CONFIG_DATA_DEFAULT_SESSION_EXPIRATION_DELAY = 20L ;
	
	// Nombre maxi de session simultanées par utilisateur
	// Si la valeur est 0, c'est à dire qu'il n'y a pas de restriction
	//Type: long
	public static String GLOBAL_CONFIG_DATA_KEY_SESSION_MAX_NUMBER_PER_USER = 
							"global.key.session.max-number-per-user";
	public static long GLOBAL_CONFIG_DATA_DEFAULT_SESSION_MAX_NUMBER_PER_USER = 1L ;

	// SIgnature
	
	// Url pattern to sign or validate
	// pattern example signvalidate/:idCompteTitre:
	public static final String GLOBAL_CONFIG_DATA_KEY_SIGNATURE_PATH_PATTERN = "global.key.signvalidate.path.pattern" ;
	public static final String GLOBAL_CONFIG_DATA_SIGNATURE_PATH_VARIABLE_COMPTE_TITRE_ID = ":idCompteTitre:" ;

	// Ecobank card payement
	
	// VPC payment URL (type: string)
	public static final String GLOBAL_CONFIG_DATA_PAYMENT_CARD_ECOBANK_VIRTUAL_CLIENT_PAYMENT_URL = "payement.card.ecobank.virtual-client-payment-url" ;
	
	// VPC payment URL, default value
	public static final String GLOBAL_CONFIG_DATA_DEFAULT_PAYMENT_CARD_ECOBANK_VIRTUAL_CLIENT_PAYMENT_URL	= "https://migs-mtf.mastercard.com.au/vpcpay" ;
	
	// VPC payment return URL (type: string)
	//public static String GLOBAL_CONFIG_DATA_KEY_PAYMENT_CARD_VIRTUAL_CLIENT_PAYMENT_RETURN_URL = "payement.card.ecobank.vpc-return-url" ;
	
	// redirected front end path after payment has been made thru VPC_URL
	public static final String GLOBAL_CONFIG_DATA_KEY_PAYMENT_CARD_VPC_RETURN_FRONT_END_REDIRECTION_PATH = "payment.card.ecobank.vpc-return.redirection-path" ;
	
	public static final String PAYMENT_CARD_VPC_RETURN_FRONT_END_REDIRECTION_PATH_VARIABLE_TRANSACTION_ID = ":transactionId:" ;
}
