/**
 * 
 */
package com.siliconwise.mmc.oldSecurity;

import java.util.Locale;

import com.siliconwise.common.AppUtil;
import com.siliconwise.common.config.file.AppConfigKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
/**
 * @author bgnakale
 *
 */
public class SessionUtil {

	/** Get current session locale. if current session is null then default locale
	 * @param sessionBag
	 * @return
	 */
	public static Locale getLocale(SessionBag sessionBag) {
		
		String sLocale = sessionBag != null && sessionBag.getLanguage() != null 
							&& !sessionBag.getLanguage().isEmpty()
						? sessionBag.getLanguage() : AppUtil.APP_DEFAULT_LANGUAGE ;
							
		Locale rtn = MessageTranslationUtil.createLocale(
				sLocale, AppConfigKeys.TRANSLATE_LOCALE_LANGUAGE_COUNTRY_SEPARATOR) ;
		
		return rtn ;
	}
	
}
