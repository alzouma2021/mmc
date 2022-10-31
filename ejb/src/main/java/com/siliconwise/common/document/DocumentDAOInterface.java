package com.siliconwise.common.document;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * 
 * @author Alzouima Moussa Mahamadou
 * date 17/07/2021
 */
public interface DocumentDAOInterface {
	
	

		/**
		 * @param idFormat
		 * @return
		 */
		public DocumentFormat trouverUnFormatParSonId(String idFormat);
		
		
		
		
		/**
		 * @param entity
		 * @param mustUpdateExistingNew
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param msgList
		 * @return
		 */
		public boolean  valider(Document entity, 
				boolean mustUpdateExistingNew,
				String namedGraph, boolean isFetchGraph,
				Locale locale, 
				List<NonLocalizedStatusMessage> msgList) ;
		
		
		
		
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
		public Document validerEtEnregistrer(
				Document entity,
				boolean mustUpdateExistingNew,
				boolean updateContentDocument,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser,
				List<NonLocalizedStatusMessage> msgList);
		
		
		
		
		/** Save document on disk and return absolute file path
		 * @param entity
		 * @param langue
		 * @param msgList
		 * @return
		 */
		public String saveDocumentInAppDocumentFolder(Document entity, Locale langue, 
				List<NonLocalizedStatusMessage> msgList) ;
		
		
		
		
		/**
		 * @param namedGraph
		 * @param isFetchGraph
		 * @return
		 */
		public List<DocumentFormat> findAllDocumentFormats(String namedGraph, boolean isFetchGraph);

		
		
		
		/**
		 * @param path
		 * @return
		 * @throws IOException
		 */
		public String computeDocumentAbsolutePathAndbase64Content(String path) throws IOException ;

		
		
		/**
		 * read a file from disk and base64 encode its content
		 * @param path
		 * @return
		 * @throws IOException 
		 * @throws IOException
		 */
		public String computeDocumentbase64Content(String path) throws IOException ;
		
		
		
	
		/**
		 * @param path
		 * @return
		 * @throws IOException
		 */
		public String computeStringDocumentContent(String path) throws IOException;
		
		
		
		/**
		 * @param path
		 * @return
		 * @throws IOException
		 */
		public byte[] computeBytesDocumentContent(String path) throws IOException;


		
		
		
		/** Find a document by its id and include the base64
		 * @param id
		 * @param isContentSkipped include base64 ecoded cotent if not true
		 * @param locale
		 * @param msgList
		 * @return
		 */
		public Document findDocumentById(String id, boolean isContentSkipped,
					Locale locale, List<NonLocalizedStatusMessage> msgList)  ;
		
		
		
		
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
		 * @return
		 * @throws IOException
		 */
		public String computeStringDocumentAbsolutePathContent(String path) throws IOException;
		
		
		
		
		
		/**
		 * @param path
		 * @param msgList
		 * @return
		 */
		public String  findStringContentDocumentParPath(String path,List<NonLocalizedStatusMessage> msgList);
		
		
		

		/**
		 * @param path
		 * @param msgList
		 * @return
		 */
		public byte[]  findBytesContentDocumentParPath(String path,List<NonLocalizedStatusMessage> msgList);
		
		
		
		/**
		 * @param path
		 * @return
		 * @throws IOException
		 */
		public byte[] computeBytesDocumentAbsolutePathContent(String path) throws IOException;
		
		
		
	
		/**
		 * @param path
		 * @param msgList
		 * @return
		 */
		public boolean  deleteStringContentDocumentByPath(String path,List<NonLocalizedStatusMessage> msgList);
		
		
		
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
		 * @param namedGraph
		 * @param isFetchGraph
		 * @param locale
		 * @param loggedInUser
		 * @param msgList
		 * @return
		 */
		public boolean supprimer(
							String idDocument,
							boolean mustUpdateExistingNew,
							String namedGraph, boolean isFetchGraph, 
							Locale locale,  User loggedInUser,
							List<NonLocalizedStatusMessage> msgList);
			
		
		
}
