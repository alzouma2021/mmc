/**
 * 
 */
package com.siliconwise.mmc.message;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.Pays;
import com.siliconwise.common.config.file.AppConfigUtil;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.oldSecurity.SessionUtil;

/**
 * Translatrof status messages
 * @author bgnakale
 *
 */
public class MessageTranslationUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String CODE_TRADUCTION_ENTITY_NOT_FOUND = "entite.nonTrouve" ;
	
	@SuppressWarnings("unused")
	//private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	public static String translate(Locale langue, String codeTraduction, 
			String textParDefaut, String... varListArray ) {
		
		if (langue == null) return codeTraduction != null ? codeTraduction : "" ;
		
		Properties translateMap = AppConfigUtil.getTanslateMap(langue)  ;
		
		if (translateMap == null) return "" ;
				
		String textTr = translateMap.getProperty(codeTraduction) ;
		
		String rtn = textTr == null ? textParDefaut : textTr ;
		
		if (varListArray == null) return rtn ;
		
		for (int i = 0; i < varListArray.length ; i++) {
			
			rtn = rtn.replaceAll(":"+(i+1)+":", varListArray[i]) ;
		}
		
		return rtn ;
	}
	
	public static String translate(Locale langue, String codeTraduction, 
			String textParDefaut, Map<String, String> varMap) {
		
		if (langue == null) return codeTraduction != null ? codeTraduction : "" ;
		
		Properties translateMap =  AppConfigUtil.getTanslateMap(langue) ;
		logger.info("63_translate :: translateMap="+translateMap);//TODO A effacer
		
		if (translateMap == null) return "" ;
				
		String textTr = translateMap.getProperty(codeTraduction) ;
		
		String rtn = textTr == null ? textParDefaut : textTr ;
		
		if (varMap == null || varMap.entrySet() != null  && varMap.entrySet().isEmpty()) return rtn ;
		
		for (Map.Entry<String, String> e : varMap.entrySet()) {
			
			rtn = rtn.replaceAll(":"+(e.getKey())+":", e.getValue()) ;
		}
		
		return rtn ;
	}
	
	public static Pays translate(Pays pays, Locale locale) {
		
		Logger logger = LoggerFactory.getLogger(MessageTranslationUtil.class.getName()) ;
		
		logger.info("_73 translate :: " + pays + " locale=" + locale) ;
		
		if (pays == null) return null ;
		
		// TODO corriger le code pour passe ibilite code pays a 2 lettres
		if (locale == null) locale = SessionUtil.getLocale(null) ;
		
		logger.info("_475 loadTranslateFiles :: locale langue"
				+locale.getLanguage()+ " contry="+locale.getCountry());
		
		Properties translateProps = AppConfigUtil.getCountryTanslateProperties(locale) ;
		
		logger.info("_82 translate :: translateProps.size()" 
				+ (translateProps != null ? translateProps.size(): "null")) ; 
		
		logger.info("_85 translate translateProps=" + translateProps) ;
		
		if (translateProps == null) return null ;
		
		Pays rtn = new Pays() ;
		
		rtn.setId(pays.getId());
		rtn.setAgeMajorite(pays.getAgeMajorite());
		rtn.setCode(pays.getCode());
		rtn.setDescription(pays.getDescription());
		rtn.setEstSurListeNoire(pays.getEstSurListeNoire());
		rtn.setIndicatifInternationnal(pays.getIndicatifInternationnal());
		rtn.setVersion(pays.getVersion());
		rtn.setCodeTrDesignation(pays.getCodeTrDesignation());
		rtn.setCodeTrNationnalite(pays.getCodeTrNationnalite());
		
		// traduire designation
		
		String textTr = pays.getCodeTrDesignation() == null  || pays.getCodeTrDesignation().isEmpty()
							? pays.getDesignation() 
							: translateProps.getProperty(pays.getCodeTrDesignation()) ;
		
		rtn.setDesignation(textTr == null ? pays.getDesignation() : textTr);
		
		// traduire nationalité
		
		textTr = pays.getCodeTrNationnalite() == null  || pays.getCodeTrNationnalite().isEmpty()
				? pays.getNationnalite() 
				: translateProps.getProperty(pays.getCodeTrNationnalite()) ;
				
		rtn.setNationnalite(textTr);
		
		logger.info("_113 translate :: rtn="+rtn)  ;
		return rtn ;
	}

	/** Extract ISO 2 letters language code from locale
	 * @param locale
	 * @return
	 */
	public static String extractLanguageISOCode(Locale locale) {
		
		if (locale == null || locale.getLanguage() == null) return null ;
		
		String s = locale.getLanguage() ;
		
		int max = Math.min(2, s.length()) ;
		
		return s.substring(0, max) ;
	}
	
	/** Create a Localefrom the locale code
	 * @param localeCode Exemples: fr-Fr, en-US, en-CA
	 * @param separator
	 * @return
	 */
	public static Locale createLocale(String localeCode, String separator) {
		
		if (separator == null || separator.isEmpty()
				|| localeCode == null || localeCode.isEmpty())
			return null ;
		
		String[] a = localeCode.split(separator) ;
		
		String lCode = a.length >= 1 ? a[0] : null ;
		String countryCode = a.length >= 2 ? a[1] : null ;
	
		if (lCode == null || lCode.isEmpty()) return null ;
		
		Locale rtn = countryCode == null || countryCode.isEmpty()
						? new Locale.Builder().setLanguage(lCode).setRegion(countryCode).build() 
						: new Locale.Builder().setLanguage(lCode).build()  ;
		
		return rtn ;
		
	}
}
