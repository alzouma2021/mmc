package com.siliconwise.mmc.fournisseurauthentifcation;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.security.SessionService;
import com.siliconwise.mmc.security.TokenService;
import com.siliconwise.mmc.user.ActiverDesactiverUnCompteUserCtlInterface;
import com.siliconwise.mmc.user.CreerModifierUnCompteUserCtlInterface;
import com.siliconwise.mmc.user.User;
import com.siliconwise.mmc.user.UserDAOInterface;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
@Stateless
public class FournisseurAuthentificationCtl implements Serializable, FournisseurAuthentificationCtlInterface {

	
	private static final long serialVersionUID = 1L;

	
	@Resource
	private EJBContext ejbContext;
	
	@Inject UserDAOInterface userDAO ;
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName());
	
	@Inject CreerModifierUnCompteUserCtlInterface creerModifierUnCompteUserCtl ;
	
    @Inject  SessionService sessionService ;
	
	@Inject  TokenService tokenService ;
	
	@Inject FournisseurFaceBookServiceInterface fournisseurFaceBookService ;

	@Inject FournisseurGoogleServiceInterface fournisseurGoogleService ;
	
	@Inject ActiverDesactiverUnCompteUserCtlInterface activerDesactiverUnCompteUserCtl ;

	@Override
	public String authentifierByGoogleProvider(
			         String tokenStringGoogle, 
			         Locale locale, 
			         SessionBag sessionBag,
			         List<NonLocalizedStatusMessage> msgList)
			         throws GeneralSecurityException, IOException {
		
		if(tokenStringGoogle == null) return null ;
		
		//Verification du token et recuperation du profil de l'utilisateur
		
		User userGoogle = fournisseurGoogleService
							.validateToKenGoogleProvider(tokenStringGoogle,locale,msgList) ;
		
		//Si l'authentification echoue, le front-edn doit rédiriger l'utilisateur vers la,page de creation d'un compte user
		
		if (userGoogle == null) {
			
		   Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			
			return null ;
			
		}
		
		//Vérification de l'utilisateur dans la base de données
		
		User  user = userDAO.rechercherUnUserParEmail(userGoogle.getEmail(), null,true, User.class); 
		
		//Si l'utilisateur n'existe pas , faire appel à la fonction de creation d'un user
		
		if (user == null) {
			
		   
		    User userCreate = creerModifierUnCompteUserCtl
				               .CreerModifierUnCompteUser(userGoogle, true, null, true, locale, null, msgList);
		  
		    if (userCreate == null) return null ;
		    
		    logger.info("_104 authentificationGoogleProvider :: User crée="+userCreate.toString());
		    
		    user = userCreate ;
		    
		    
		    //Activation du compte user crée
		    
		    boolean estActive = activerDesactiverUnCompteUserCtl
				    		      .activerUnCompteUser(userCreate.getId(), true,
				    		    		      false, null, true, locale, null, msgList);
		    
		    if(!estActive) {
		    	
		    	try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
		    	
		    	return null ; 
		    	
		   }
		    
		    	
		}
		
		//Ouverture de la session pour l'utilisateur
	
		String rtn = buildTokenForUser(user,locale, sessionBag, msgList);
		
		return rtn ;
		
	}
	
	
	@Override
	public String authentifierByFaceBookProvider(
			         String tokenStringFaceBook,
			         Locale locale, 
			         SessionBag sessionBag,
			         List<NonLocalizedStatusMessage> msgList) {
		
		
	   //Vérifier la variable tokenStringFaceBook
		
	   if(tokenStringFaceBook == null )	return null ;
	   
	   
	   //Verification et recuperation du profil de l'utilisateur
	   
	   String[] tableau = fournisseurFaceBookService
			               .validateToKenFaceBookProvider(tokenStringFaceBook,locale, msgList);
		
		/**
		 * 
		 * Si l'authentification de l'utilisateur echoue , le Front-end doit le 
		 * rédigrer vers la page de creation d'un compte user
		 * 
		 */
	   
		if (tableau  == null || tableau.length == 0
				|| tableau[0] == null  || tableau[0].isEmpty()  ) {
			
		   Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
		   return null ;
			
		}
		
		logger.info("_180 authentifierByFaceBookProvider rechercher utilisateur"); //TODO A effacer
		
		//Vérification de l'utilisateur dans la base de données
		
		User  user = userDAO.rechercherUnUserParEmail( tableau[0], null,true, User.class); 
		
	    logger.info("_189 fin authentifierByFaceBookProvider rechercher utilisateur="+user); //TODO A effacer
				
	    //Si l'utilisateur n'existe pas , faire appel à la fonction de creation d'un user
				
		if (user == null ) {
			
			
			 logger.info("_196 Creation de l'utilisateur "); //TODO
					
		     User userFaceBook = new User();
		     
		     userFaceBook.setEmail(tableau[0]);
		     
		     userFaceBook.setNom(tableau[1] != null 
		    		                && !tableau[1].isEmpty()
		    		                ?   tableau[1]
		    		                :   null );
		     
		     userFaceBook.setPrenom(tableau[2] != null 
						                && !tableau[2].isEmpty()
						                ?   tableau[2]
						                :   null );
		     
		     User loggedInUser = new User();
				   
		     User userCreate = creerModifierUnCompteUserCtl
						         .CreerModifierUnCompteUser(userFaceBook, true, null, true, locale, loggedInUser, msgList);
				  
		     if (userCreate == null) return null ;
				    
			 logger.info("_225 authentificationGoogleProvider :: User crée="+userCreate.toString());
			 
			 user = userCreate ;
			 
			 //Activation du compte user crée
			    
			 boolean estActive = activerDesactiverUnCompteUserCtl
			    		           .activerUnCompteUser(userCreate.getId(), 
			    		              true, false, null, true, locale, loggedInUser, msgList);
			 
			 
			 //Activation du compte user crée
			    
			 if(!estActive) {
			    	
			   try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			    	
			    return null ; 
			    	
			 }
			 
				    	
		}
				
		//Ouverture de la session pour l'utilisateur
				
		String rtn = buildTokenForUser(user,locale, sessionBag, msgList);
				
		return rtn ;
		
		
	}


	@Override
	public String buildTokenForUser(
			        User user, 
			        Locale locale,
			        SessionBag sessionBag,
			        List<NonLocalizedStatusMessage> msgList) {
		
		//Creation de la session
		
	    sessionBag = sessionService.createOrUpdateSession(sessionBag, user, locale) ;
				
	    //Construction du token
				
		String rtn = tokenService.buildToken(sessionBag, msgList) ;
				
		return rtn ;
		
		
	}
	

}
