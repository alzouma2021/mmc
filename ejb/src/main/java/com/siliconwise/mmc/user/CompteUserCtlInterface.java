package com.siliconwise.mmc.user;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public interface CompteUserCtlInterface {
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean regenererUnMotDePasseCompteUser(
						String email ,
						boolean mustUpdateExistingNew,
						String namedGraph, 
						boolean isFetchGraph, 
						Locale locale,
						List<NonLocalizedStatusMessage> msgList)
						throws NoSuchAlgorithmException;
	
	
	

	public boolean confirmerUnCompteUser(
					  String code,
					  boolean mustUpdateExistingNew,
					  boolean compteUserHasPassWord,
					  String namedGraph, boolean isFetchGraph, 
					  Locale locale,  User loggedInUser, 
					  List<NonLocalizedStatusMessage> msgList);
	

}
