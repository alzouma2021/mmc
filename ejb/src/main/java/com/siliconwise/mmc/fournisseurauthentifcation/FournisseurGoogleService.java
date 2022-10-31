package com.siliconwise.mmc.fournisseurauthentifcation;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.user.User;



/**
 * 
 * Cette classe implemente les differentes methodes d'authentification aupres des tiers fournisseur
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
//TODO Ajout des exceptions
@Stateless
public class FournisseurGoogleService implements Serializable,  FournisseurGoogleServiceInterface{

	
	
	private static final long serialVersionUID = 1L;
	
	//public static final String CLIENT_ID_GOOGLE = "google.app.client.id";
	public static final String CLIENT_WEB_ID_GOOGLE = "924860844610-bkih2rv6s35kmfn4ggkuvd40lakd9536.apps.googleusercontent.com";
	public static final String CLIENT_MOBILE_ID_GOOGLE = "621483799841-013723oqi6345v7me1ukltnqkns1il1t.apps.googleusercontent.com";
	
	@Override
	public User validateToKenGoogleProvider(
			      String idTokenString,
			      Locale locale,
			      List<NonLocalizedStatusMessage> msgList)
			      throws GeneralSecurityException, IOException {
		
		
		// Declarationn des variables
		
		HttpTransport transport = Utils.getDefaultTransport() ;
		
		JsonFactory jsonFactory = Utils.getDefaultJsonFactory() ;
		
		
		//Verification du Jeton pour authentifier l'utilisateur
		
		/*GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					    .setAudience(Collections.singletonList(CLIENT_ID_GOOGLE))
					    .build();*/
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
			    .setAudience(Arrays.asList(CLIENT_WEB_ID_GOOGLE, CLIENT_MOBILE_ID_GOOGLE))
			    .build();
		
		GoogleIdToken idToken = verifier.verify(idTokenString);
		
		if (idToken == null) {
		
			 Map<String,String> msgVarMap =new HashMap<String,String>();  
				
			 String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_GOOGLE_TOKEN_ID_NON_VALIDATE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_GOOGLE_TOKEN_ID_NON_VALIDATE, // Message par defaut
					msgVarMap) ;
			 msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 	
			 
			 return null ;
			
		}
		
		//Recuperation du profil 
		
		Payload payload = idToken.getPayload();
		 
		//String userId = payload.getSubject();
		
		String email = payload.getEmail();
		
		//boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		String name = (String) payload.get("name");
		//String pictureUrl = (String) payload.get("picture");
		//String locale1 = (String) payload.get("locale");
		String familyName = (String) payload.get("family_name");
		//String givenName = (String) payload.get("given_name");
		
		User user = new User() ;
		
		user.setEmail(email);
		user.setNom(name);
		user.setPrenom(familyName);

		
		return user ;
		
	
	}



	

}
