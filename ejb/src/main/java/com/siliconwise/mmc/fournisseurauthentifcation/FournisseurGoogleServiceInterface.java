package com.siliconwise.mmc.fournisseurauthentifcation;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.user.User;

/***
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface FournisseurGoogleServiceInterface {
	
	
	
	public User validateToKenGoogleProvider(
					String tokenStringGoogle,
					Locale locale,
					List<NonLocalizedStatusMessage> msgList)  
					throws GeneralSecurityException, IOException ;
	


}
