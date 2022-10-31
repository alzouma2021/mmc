package com.siliconwise.mmc.fournisseurauthentifcation;


import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.security.SessionBag;

/***
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface FournisseurFaceBookServiceInterface {
	
	
	
	public String[] validateToKenFaceBookProvider(
				        String tokenStringFaceBook,
				        Locale locale ,
						List<NonLocalizedStatusMessage> msgList);
	


}
