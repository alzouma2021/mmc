package com.siliconwise.mmc.fournisseurauthentifcation;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author sysadmin
 *
 */
public interface FournisseurAuthentificationCtlInterface {
	

	public String authentifierByGoogleProvider(
					String tokenStringGoogle,
					Locale locale,
					SessionBag sessionBag,
					List<NonLocalizedStatusMessage> msgList)  
					throws GeneralSecurityException, IOException ;
	
	
	
	public String authentifierByFaceBookProvider(
			        String tokenStringFaceBook,
			        Locale locale,
			        SessionBag sessionBag,
					List<NonLocalizedStatusMessage> msgList) ;
	
	
	
	public String buildTokenForUser(
			        User user,
			        Locale locale,
			        SessionBag sessionBag,
					List<NonLocalizedStatusMessage> msgList) ;
	

}
