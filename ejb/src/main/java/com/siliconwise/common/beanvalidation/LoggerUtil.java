/**
 * 
 */
package com.siliconwise.common.beanvalidation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;

import com.siliconwise.common.locale.LocaleUtil;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.security.SessionBag;

/**
 * @author sysadmin
 *
 */
public class LoggerUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	/** log a message in the application log
	 * @param statusMessageType
	 * @param logger
	 * @param ex Exception
	 * @param msgPrefix Message prefix (contain method name and line nbr
	 * @param msgCode  code of message transmitted to caller
	 * @param msgVariableMap message traslation variable map
	 */
	public static void log(StatusMessageType statusMessageType, Logger logger, Exception ex, 
			String msgPrefix, String msgCode, Map<String, String> msgVariableMap) {
		
		if (logger == null || ex == null || statusMessageType == null) return ;
		
		Locale locale = LocaleUtil.getDefaultLocale() ;
		
		String msg = MessageTranslationUtil.translate(locale,
				SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED,
				SessionBag.TRANSLATION_MESSAGE_SESSION_HAS_EXPIRED, 
				(Map<String, String>)new HashMap<String, String>()) ;

		String  s = (msgPrefix == null ? "" : msgPrefix + " :: ") + " " + msg + " " ;
		
		if (ex != null) {
			
			s += "Exception: " + ex + ": " + ex.getMessage() + " "
					+ "Cause: " + ex.getCause() ;
		}
				
		switch (statusMessageType) {
		
			case ERROR:
				
			case FATAL:			
				logger.error(s) ;	
				break ;
			
			case WARNING:
				logger.warn(s) ;	
				break ;
			
			default:
				logger.info(s) ;	
				break;
		}
		
		ex.printStackTrace() ;
	}
}
