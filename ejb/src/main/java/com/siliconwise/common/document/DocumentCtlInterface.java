package com.siliconwise.common.document;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouma Moussa Mahamadou
 * @param <E>
 *
 */
public interface DocumentCtlInterface {
	
	
	
	/**
	 * @param <E>
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public  Document creerModifierDocument(
						Document entity,
						boolean mustUpdateExistingNew,
						boolean updateStringContentDocument,
						String namedGraph, 
						boolean isFetchGraph, 
						Locale locale,  User loggedInUser,
						List<NonLocalizedStatusMessage> msgList);
	
	
	
	
	/**
	 * @param <E>
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */

	public List<Document>  creerModifierDocumentList(
								Set<Document> entityList,
								boolean mustUpdateExistingNew,
								boolean updateStringContentDocument,
								String namedGraph, 
								boolean isFetchGraph, 
								Locale locale,  User loggedInUser, 
								List<NonLocalizedStatusMessage> msgList);
	
	
	
	
	
	
	/**
	 * @param id
	 * @param isContentSkipped
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public Document findStringDocumentById(String id, boolean isContentSkipped,
						Locale locale, List<NonLocalizedStatusMessage> msgList) ;
	
	
	
	/**
	 * @param id
	 * @param isContentSkipped
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public byte[] findBytesDocumentById(String id, boolean isContentSkipped,
						    Locale locale, List<NonLocalizedStatusMessage> msgList) ;
	

	
	
	/**
	 * @param path
	 * @param msgList
	 * @return
	 */
	public String  findStringContentDocumentParPath(String path,List<NonLocalizedStatusMessage> msgList);
	

	
	
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
	public String updateStringContentDocument(
						Document entity,
						boolean mustUpdateExistingNew,
						boolean updateStringContentDocument,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser, 
						List<NonLocalizedStatusMessage> msgList);
	
	
	/**
	 * @param entity
	 * @param mustUpdateExistingNew
	 * @param updateStringContentDocument
	 * @param namedGraph
	 * @param isFetchGraph
	 * @param locale
	 * @param loggedInUser
	 * @param msgList
	 * @return
	 */
	public boolean  appendEntityDocument(
							DocumentEntityTransfert entity,
							boolean mustUpdateExistingNew,
							String namedGraph, boolean isFetchGraph, 
							Locale locale,  User loggedInUser, 
							List<NonLocalizedStatusMessage> msgList);
	
	
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
	public boolean supprimerUnDocument(
						String idDocument,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser,
						List<NonLocalizedStatusMessage> msgList);
	
	
	
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
	public boolean supprimerDocumentList(
			            List<Document> entityList,
						boolean mustUpdateExistingNew,
						String namedGraph, boolean isFetchGraph, 
						Locale locale,  User loggedInUser,
						List<NonLocalizedStatusMessage> msgList);
	
	
	
	/**
	 * @param namedGraph
	 * @param isFetchGraph
	 * @return
	 */
	public List<DocumentFormat> findAllDocumentFormats(String namedGraph, boolean isFetchGraph);

	

	
}
