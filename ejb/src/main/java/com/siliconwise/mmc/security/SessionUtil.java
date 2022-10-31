/**
 * 
 */
package com.siliconwise.mmc.security;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import com.siliconwise.common.locale.LocaleUtil;



/**
 * @author sysadmin
 *
 */
public class SessionUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	// Thread safe list of application session bags
	@Produces 
	private static List<SessionBag> sessionbagList = new CopyOnWriteArrayList<>() ;

	public static Locale getLocale(SessionBag sessionBag) {
		
		Locale rtn = sessionBag != null && sessionBag.getLocale() != null 
						? sessionBag.getLocale() : LocaleUtil.getDefaultLocale() ;
		
		return rtn ;
	}

	/** find session bag by session id
	 * @param id
	 * @return
	 */
	public static SessionBag findSessionBagById(String id) {
		
		for (SessionBag sessionbag : sessionbagList) {
			
			if (sessionbag != null && sessionbag.getId() != null
					 && sessionbag.getId().equals(id))
				return sessionbag ;
		}
		
		return null ;
	}
	
	@Produces @LoadSessionBag
	public static SessionBag loadSessionBag(final InjectionPoint ip) {
		
		Set<LoadSessionBag> annotationList = ip.getAnnotated().getAnnotations(LoadSessionBag.class) ;
		
		if (annotationList == null || annotationList.isEmpty() || annotationList.size() > 1) return null ;
		
		LoadSessionBag annotation = annotationList.stream().findFirst().get() ;
		
		String id = annotation.id() != null && !annotation.id().isEmpty() 
				? annotation.id() : null ;
		
		SessionBag rtn = findSessionBagById(id)  ;
		
		return rtn ;
	}
}
