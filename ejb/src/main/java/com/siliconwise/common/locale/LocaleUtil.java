/**
 * 
 */
package com.siliconwise.common.locale;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author sysadmin
 *
 */
public class LocaleUtil implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAUL_COUNTRY_CODE_ALPHA2 = "CI"  ; // Cote d'Ivoire
	public static final String DEFAULT_LANGUAGE_CODE_ALPHA3 = Locale.FRENCH.getISO3Language() ;
	
	private static final String LOCALE_SERIALISATION_SEPARATOR = "-" ;
	
	public static String serialiseLocale(Locale locale) {
		
		if (locale == null) return null ;
		return locale.getISO3Language() + LOCALE_SERIALISATION_SEPARATOR + locale.getCountry();
		
	}
	
	public static Locale deserialiseLocale(String s) {
		
		if (s == null || s.isEmpty()) return null ;
		
		String[] a = s.split(LOCALE_SERIALISATION_SEPARATOR) ;
		
		if (a == null || a.length > 2) return null ;
		
		if (a[0] == null || a[0].isEmpty()) return null ;
		
		Locale rtn = null ;
		
		try {
			if (a.length == 2 && a[1] != null && !a[1].isEmpty()) 
				rtn = new Locale(a[0], a[1]) ;
			else if (a.length == 1)
				rtn = new Locale(a[0]) ;
		}
		catch (Exception e) {
			rtn = null ;
		}
		
		return rtn ;
	}

	static public Locale getDefaultLocale() {
		
		return new Locale(DEFAULT_LANGUAGE_CODE_ALPHA3, DEFAUL_COUNTRY_CODE_ALPHA2);
	}
}
