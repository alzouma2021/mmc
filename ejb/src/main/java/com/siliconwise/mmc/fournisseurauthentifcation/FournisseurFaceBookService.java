package com.siliconwise.mmc.fournisseurauthentifcation;


import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;
import com.siliconwise.common.Ville;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.security.SessionBag;



/**
 * 
 * Authentification par FaceBook
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class FournisseurFaceBookService implements Serializable,  FournisseurFaceBookServiceInterface{

	
	
	private static final long serialVersionUID = 1L;
	
	public static final String CLIENT_ID_GOOGLE = "facebook.app.client.id";
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	
	
	@Override
	public String[] validateToKenFaceBookProvider(
			            String tokenStringFaceBook,
			            Locale locale ,
						List<NonLocalizedStatusMessage> msgList) {
		
		
		 //Verification du token recu
		
		 if(tokenStringFaceBook == null) return null ;
		 
		 
		 //Verifier et supprimer les doubles quotes dans la chaine de caracteres
		 
		 if (tokenStringFaceBook.charAt(0) == 34 
				   && tokenStringFaceBook.charAt(tokenStringFaceBook.length()-1) == 34)
			 
			 	tokenStringFaceBook = tokenStringFaceBook.substring(1, tokenStringFaceBook.length()-1);
		 
		 
		 //Declaration des variables
		
		 FacebookClient fbClient = null ;
		 
		 User user = null ;
		 
		 Map<String,String> msgVarMap = new  HashMap<String,String>();
		 
		 
		 //try
		 
		 try {
			  
		  fbClient =  new DefaultFacebookClient(tokenStringFaceBook.trim() , Version.LATEST);
				
		  user =  fbClient.fetchObject("me", User.class, Parameter.with("fields", "email,first_name,last_name,gender"));
							
						
		 }catch(NullPointerException ex) {
			
			 
			String msg  = MessageTranslationUtil.translate(locale ,
						AppMessageKeys.CODE_TRADUCTION_FACEBOOK_API_GRAPH_NON_CREE,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_FACEBOOK_API_GRAPH_NON_CREE, // Message par defaut
							msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
							
			return null ; 
				
		 }catch(FacebookException ex) {
			 
			String msg  = MessageTranslationUtil.translate(locale ,
					    AppMessageKeys.CODE_TRADUCTION_ERROR_FACEBOOK,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_ERROR_FACEBOOK, // Message par defaut
							msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return null ;
			 
		 }catch(IllegalArgumentException ex) {
			 
			String msg  = MessageTranslationUtil.translate(locale ,
					    AppMessageKeys.CODE_TRADUCTION_ACCESS_TOKEN_INCORRECT,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_ACCESS_TOKEN_INCORRECT, // Message par defaut
							msgVarMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			 
			return null ;
			
		 }
				
		 //Remplissage du tableau				
		 
		 String[] rtnTable = { user.getEmail(), user.getFirstName(), user.getLastName()};
		 
		 for(String element: rtnTable)
		 logger.info("_128 validateToKenFaceBookProvider :: tableau="+element); //TODO A effacer
		
		 return rtnTable ;
		
		
	}


}
