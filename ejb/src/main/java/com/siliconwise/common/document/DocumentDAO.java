package com.siliconwise.common.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.Base64;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

//import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.AppUtil;
import com.siliconwise.common.config.file.AppConfigUtil;
import com.siliconwise.common.event.oldhistorique.HistoriserEventPayload;
import com.siliconwise.common.event.oldhistorique.Historique.HistoriqueEvent;
import com.siliconwise.common.reference.IReferenceTr;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.EntityInitializer;
import com.siliconwise.mmc.common.entity.EntityInitializerQualifier;
import com.siliconwise.mmc.common.entity.EntityInitializerQualifier.TargetEntityEnum;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.produitlogement.OperateurCritere;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.user.User;

/**
 * @author Mis à jour faite par Alzouma Moussa Mahamadou date: 17/07/2021
 * Utilisation d'une interface
 *
 */
@Stateless
public class DocumentDAO implements Serializable, DocumentDAOInterface {
	
	
	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager ;
	
	// evenement d'historisation
	@Inject
	Event<HistoriserEventPayload<Document>> historiserEVent ;
	
	Locale locale ;
			
			
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	private Locale langue = new Locale(AppUtil.APP_DEFAULT_LANGUAGE) ;

	@Resource private EJBContext ejbContext;
	
	//private final String folderName = AppUtil.TEMPLATE_FOLDER_PATH ;

	//@Inject private MessageDAO messageDao ;
	
	
	
	public DocumentFormat trouverUnFormatParSonId(String idFormat) {
		
		return entityManager.find(DocumentFormat.class, idFormat) ;		
		
	}
	
	
	public boolean  valider(Document entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph,
			Locale locale, 
			List<NonLocalizedStatusMessage> msgList) {
		
		
		//Verification des doublon
		
		if(entity.getDesignation() ==  null) {
			
			String msg  = MessageTranslationUtil.translate(langue ,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
			
			return false ;
			
		}
		
		
		// recherche de doublon
		
		boolean isEntityDuplictedOrNotFound = new EntityUtil<Document>().isEntityDuplicatedOrNotFound(
				entityManager, entity, mustUpdateExistingNew, "documentIdParPath", 
				new String[] {"path"}, new String[] {entity.getPath()},
				AppMessageKeys.CODE_TRADUCTION_NOUVELLE_ENTITE_DUPLIQUEE, entity.getMsgVarMap(),
				AppMessageKeys.CODE_TRADUCTION_DUPLICATION_AVEC_ID_DIFFERENT,entity.getMsgVarMap(),
				AppMessageKeys.CODE_TRADUCTION_NOT_FOUND, entity.getMsgVarMap(), 
				langue, msgList) ;
				
	
		if (isEntityDuplictedOrNotFound) return false ;
		
		
		// verifiy that version is defined if entity id si not null
		
		if (entity.getId() != null && entity.getVersion() == null) {
			
			String msg  = MessageTranslationUtil.translate(langue ,
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VERSION_NON_DEFINIE, // Message par defaut
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
			
			return false ;
		}

		// association
		
		try {
			
			
			EntityUtil<Document> entityUtil = new EntityUtil<Document>(Document.class) ;
			
			//Document  vers Reference
			//Verifie si le typedocument est non nul 
			
			Map<String,String>  msgVarMap = entity.getTypeDocument() == null 
							 || entity.getTypeDocument().getMsgVarMap() == null
										   ?  new HashMap<String,String>() : entity.getTypeDocument().getMsgVarMap() ;
						
			boolean isAttached = entityUtil.attachLinkedEntity(entityManager, entity, 
					entity.getTypeDocument(), 
					entity.getClass().getDeclaredMethod("setTypeDocument", Reference.class), 
					null,true, locale, AppMessageKeys.CODE_TRADUCTION_TYPE_DOCUMENTE_NON_TROUVE, 
					msgVarMap, msgList) ;
			
			if (!isAttached) return false ;
			
			// Document vers Format de document
			
			msgVarMap = entity.getFormat() == null 
					 || entity.getFormat().getMsgVarMap() == null
								   ?  new HashMap<String,String>() : entity.getFormat().getMsgVarMap() ;
								   
			isAttached = entityUtil.attachLinkedEntity(entityManager, entity, 
					entity.getFormat(), 
					entity.getClass().getDeclaredMethod("setFormat", DocumentFormat.class), 
					null, true,  locale, AppMessageKeys.CODE_TRADUCTION_FORMAT_NOT_FOUND, 
					msgVarMap, msgList) ;
			
			
			if (!isAttached) return false ;
			
		} 
		catch(Exception ex) {
			
			String msg  = MessageTranslationUtil.translate(langue ,
					AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_ERREUR_ATTACHEMENT_ENTITES_LIEES, // Message par defaut
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
			
			logger.error("_198 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
			ex.printStackTrace();
			
			return false ;
		}
		
		
		//Validation des contraintes simples portant sur la validation des annotations des propriétés de classe
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		
		Set<ConstraintViolation<Document>> constraintViolationList = validator.validate(entity) ;
		
		for (@SuppressWarnings("unused") ConstraintViolation<Document> violation : constraintViolationList) {
			
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_VALIDATION_ERREUR, // Message par defaut
					entity.getMsgVarMap()) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ;
		}
				
		if (!constraintViolationList.isEmpty()) return false ;
		
		
		return true;
		
		
	}
	
	
	public Document validerEtEnregistrer(
				Document entity,
				boolean mustUpdateExistingNew,
				boolean updateStringContentDocument,
				String namedGraph, boolean isFetchGraph, 
				Locale locale,  User loggedInUser,
				List<NonLocalizedStatusMessage> msgList){

		
		
		if (entity == null) {
			
			 String msg  = MessageTranslationUtil.translate(langue,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE,
					 	AppMessageKeys.CODE_TRADUCTION_NON_DEFINIE, 
						new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		}
		
		
		boolean estValide = valider(entity, mustUpdateExistingNew,
				              namedGraph, isFetchGraph,locale, msgList) ;
		
		// est une creation ?
		
		boolean estCreation = entity.getId() == null ;
		
		if (!estValide) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return null ; 
		}
	
		// save file on disk
		
		if(entity.getId() == null || updateStringContentDocument) {
		    
			String filePath = saveDocumentInAppDocumentFolder(entity, langue,  msgList) ;
			
			
			if (filePath == null) {
				
				try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
				return null ; 
			}
			
			String relativeFilePath = AppConfigUtil.computeAppFolderBasedRelativePath(filePath) ;
			
			entity.setPath(relativeFilePath);
	
		}
		
		//Enregistrement des MetaData
		
		Document rtn = EntityUtil.persistOrMerge(
						entityManager, Document.class, entity, 
						namedGraph, isFetchGraph, 
						AppMessageKeys.CODE_TRADUCTION_EXISTE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE, entity.getMsgVarMap(), 
						AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR, entity.getMsgVarMap(), 
						langue, msgList);
		
		if (rtn == null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){}
			
			return null ;
		}

		
		// lever les evenement d'histrorisation
		
		HistoriserEventPayload<Document> payload = new HistoriserEventPayload<>() ;
		payload.setEntity(entity);
		payload.setLocale(locale);
					
		payload.setUserId(loggedInUser != null ? loggedInUser.getId(): null);
					
		payload.setUserName(loggedInUser != null ? loggedInUser.getFullname(): null);
					
		payload.setEvent(estCreation 
					? HistoriqueEvent.CREATION : HistoriqueEvent.MODIFICATION);
					
		historiserEVent.fire(payload);
					
		return rtn ;
		
	}
	
	
	
	/** Save document on disk and return absolute file path
	 * @param entity
	 * @param langue
	 * @param msgList
	 * @return
	 */
	public String saveDocumentInAppDocumentFolder(
						Document entity, Locale langue, 
						List<NonLocalizedStatusMessage> msgList) {
		
		
		//Verifier l'entite recue
		
		if (entity == null) return null ;
		
		
		//Verifier si le fromat du document est nul
		
		if (entity.getFormat() == null ) {
			
			String translatedMessage  = MessageTranslationUtil.translate(langue,
					AppMessageKeys.CODE_TRADUCTION_FORMAT_NON_DEFINI ,
					AppMessageKeys.CODE_TRADUCTION_FORMAT_NON_DEFINI, 
					new String[] {});

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
			
			return null ;
			
		}
		
		
		// file name
		
		String designation = entity.getTypeDocument() != null
				                && entity.getTypeDocument()
				                         .getDesignation() != null
								?  entity.getTypeDocument()
										 .getDesignation()  
							    : "" ;
		
								
		//Remplacer les espaces de la designation par _
								
		designation = designation.trim().replaceAll(" ", "_") ;
		
		//Creation du nom du fichier dans lequel sera crée le contenu de notre document
		
		String fileName =  designation + UUID.randomUUID().toString()
									   + entity.getFormat().getExtension() ;
		
		// save on disk
		
		String filePath = null ;
		
		try {
		
			
			//filePath = AppConfigUtil.savedFileOnHardDisk(AppUtil.DOCUMENT_FOLDER_PATH, entity.getContenu(), fileName) ;
			
			filePath = AppConfigUtil.savedStringFileOnHardDisk(AppUtil.DOCUMENT_FOLDER_PATH, entity.getContenu(), fileName);
			
			
		} 
		catch (IOException ex) {
			
			String translatedMessage = MessageTranslationUtil.translate(langue,
					AppMessageKeys.CODE_TRADUCTION_DOCUMENT_CREATION_ERROR,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DOCUMENT_CREATION_ERROR, // Message par defaut
					entity.getMsgVarMap());

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
					LocalDateTime.now().toString() + " " + translatedMessage));
			
			// Logger
			logger.error("_405 saveDocumentOnDisk :: "+translatedMessage + ex + ":" + ex.getMessage()
						+" Cause:"+ex.getCause());
			ex.printStackTrace();
			
			return null ;	
			
		}
		
		return filePath ;
		
	}
	
	
	
	public List<DocumentFormat> findAllDocumentFormats(String namedGraph, boolean isFetchGraph){
		
		List<DocumentFormat> rtnList = new EntityUtil<DocumentFormat>()
				                           .findListByFieldValues(
				                        	entityManager, "allDocumentFormats",
				                        	new String[] {}, new String[] {}, 
				                        	namedGraph, isFetchGraph, 
				                        	new DocumentFormat()) ;
		
		return rtnList ;
		
	}
	
	

	// ----  Generer document  ------
	
	public String computeDocumentAbsolutePathAndbase64Content(String path) 
			throws IOException {
		
		if (path == null || path.isEmpty()) return  null ;
		
		String absolutePath = AppConfigUtil.computeAppFolderBasedAbsolutePath(path) ;
		
		if (absolutePath == null || path.isEmpty()) {
			
			logger.error("_439 computeDocumentAbsolutePathAndbase64Content :: "
					+ "Erreur de calcul du chemin absolu de :" + path);
			return  null ;
		}
		
		return computeDocumentbase64Content(absolutePath) ;
		
	}

	/**
	 * read a file from disk and base64 encode its content
	 * @param path
	 * @return
	 * @throws IOException 
	 * @throws IOException
	 */
	public String computeDocumentbase64Content(String path) throws IOException {

		if (path == null || path.isEmpty()) return  null ;
		
		logger.info("_458 computeDocumentbase64Content :: path=" + path); 
		
		File filePath = new File(path) ; 
		byte[] bytes = new byte[(int)filePath.length()];
		InputStream is = null ;
		
		is = new FileInputStream(filePath);
		is.read(bytes, 0, bytes.length);
		is.close();
		
		//String content  =  Base64.encodeBase64String(bytes) ;
		
		String content  =  Base64.getEncoder().encodeToString(bytes) ;
		
		return content ;
		
	}
	
	


	// ----  Generer document ------
	
	/** Find a document by its id and include the base64
	 * @param id
	 * @param isContentSkipped include base64 ecoded cotent if not true
	 * @param locale
	 * @param msgList
	 * @return
	 */
	public Document findDocumentById(String id, boolean isContentSkipped,
				Locale locale, List<NonLocalizedStatusMessage> msgList)  {
		
		if (id == null || id.isEmpty()) return  null ;
		
		Document rtn = EntityUtil.findEntityById(entityManager, 
				id, null, true, Document.class) ;
		
		if (rtn == null) return null ;
		
		if (isContentSkipped) {
			
			rtn.setContenu(null);
			return rtn ;
		}
		
		if (rtn.getPath() != null && !rtn.getPath().isEmpty()) {
			
			try {
				String content = computeDocumentbase64Content(rtn.getPath()) ;
				rtn.setContenu(content);
			}
			catch (Exception ex) {
				
				String translatedMessage = MessageTranslationUtil.translate(langue,
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR, // Message par defaut
						new String[] {});

				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
						LocalDateTime.now().toString() + " " + translatedMessage));
				
				logger.error("_2110 findDocumentById :: "+translatedMessage + ex + ":" + ex.getMessage()
							+" Cause:"+ex.getCause());
				ex.printStackTrace();
				
				return null ;
				
			}
			
		}
		
		return rtn ;
		
	}
	
	
	/**
	 * 
	 * Methodes ajoutées par Alzouma Moussa pour la gestion spécifique de documents:
	 * 
	 * Date: 19/07/2021
	 * 
	 */
	
	
	
	/**
	 * 
	 * Recuperation d'un fichier de type string sur le disque dur en utilisant un chemin absolu
	 * 
	 */
	
	public String computeStringDocumentAbsolutePathContent(String path) 
			throws IOException {
		
		
		//Variable path
		
		if (path == null || path.isEmpty())  return  null ;
			
		
		String absolutePath = AppConfigUtil.computeAppFolderBasedAbsolutePath(path) ;
		
		
		if (absolutePath == null || absolutePath.isEmpty()) {
			
			logger.error("_563 computeDocumentAbsolutePathAndbase64Content :: "
					+ "Erreur de calcul du chemin absolu de :" + path);
			return  null ;
		}
		
		return computeStringDocumentContent(absolutePath) ;
		
	}
	
	
	
	/**
	 * Recuperation d'un fichier de type string sur le disque dur
	 * 
	 */
	@SuppressWarnings("resource")
	public String computeStringDocumentContent(String path) throws IOException {

		if (path == null || path.isEmpty()) return  null ;
		
		logger.info("_582 computeDocumentbase64Content :: path=" + path); 
		
		
		//Instanciantion du fichier 
		
		File filePath = new File(path) ; 

		//Creating a Scanner object
	    
		Scanner sc = new Scanner(filePath);
	     
	    //StringBuffer to store the contents
	    StringBuffer sb = new StringBuffer();
	    
	    //Appending each line to the buffer
	    while(sc.hasNext())
	         sb.append(" "+sc.nextLine());
	    
		
		return sb.toString() ;
		
		
	}

	
	
	/**
	 * Recherche du contenu d'un document de type string sur le disque dur
	 * @param id
	 * @param isContentSkipped
	 * @param locale
	 * @param msgList
	 * @return
	 */
	
	public Document findStringDocumentById(String id, boolean isContentSkipped,
			Locale locale, List<NonLocalizedStatusMessage> msgList)  {
	
		if (id == null || id.isEmpty()) return  null ;
		
		Document rtn = EntityUtil.findEntityById(entityManager, 
				id, null, true, Document.class) ;
		
		if (rtn == null) return null ;
		
		logger.info("_626 findStringDocumentById :: Document="+rtn.toString()); //TOOD A effacer
		
		if (isContentSkipped) {
			
			rtn.setContenu(null);
			return rtn ;
		}
		
		if (rtn.getPath() != null && !rtn.getPath().isEmpty()) {
			
			try {
				
				//String content = computeStringDocumentContent(rtn.getPath()) ;
				
				String content = computeStringDocumentAbsolutePathContent(rtn.getPath()) ;
				
				rtn.setContenu(content);
				
			}
			catch (Exception ex) {
				
				String translatedMessage = MessageTranslationUtil.translate(langue,
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR, // Message par defaut
						new String[] {});
	
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
						LocalDateTime.now().toString() + " " + translatedMessage));
				
				logger.error("_2110 findDocumentById :: "+translatedMessage + ex + ":" + ex.getMessage()
							+" Cause:"+ex.getCause());
				ex.printStackTrace();
				
				return null ;	
			}
		}
		
		return rtn ;
	}

	
	/**
	 * Recherche du contenu en type String d'un document par le path
	 */
	
	public byte[] findBytesDocumentById(String id, boolean isContentSkipped,
			Locale locale, List<NonLocalizedStatusMessage> msgList)  {
	
		
		byte[] rtnBytes = null ;
		
		//Verfier la variable id
		
		if (id == null || id.isEmpty()) return  null ;
		
		//Remettre le document dans le contexte de persistence
		
		Document rtn = EntityUtil.findEntityById(entityManager, 
				                 id, null, true, Document.class) ;
		
		
		if (rtn == null) return null ;
		
        //Appel à la methode computeBytesDocumentAbsolutePathContent
		
		if (rtn.getPath() != null && !rtn.getPath().isEmpty()) {
			
			try {
			
				
				rtnBytes = computeBytesDocumentAbsolutePathContent(rtn.getPath()) ;
				
			}
			catch (Exception ex) {
				
				String translatedMessage = MessageTranslationUtil.translate(langue,
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR, // Message par defaut
						new String[] {});
	
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
						LocalDateTime.now().toString() + " " + translatedMessage));
				
				logger.error("_2110 findDocumentById :: "+translatedMessage + ex + ":" + ex.getMessage()
							+" Cause:"+ex.getCause());
				ex.printStackTrace();
				
				return null ;	
				
			}
			
			
		}
		
		return rtnBytes ;
		
	}
	
	
	/**
	 * Recherche de contenu d'un document en type bytes
	 * 
	 * Ajout par Alzouma Moussa Mahamadou: Date 07/09/2021
	 * 
	 */
	
	public byte[]  computeBytesDocumentAbsolutePathContent(String path) 
			throws IOException {
		
		
		if (path == null || path.isEmpty()) return  null ;
			
		
		String absolutePath = AppConfigUtil.computeAppFolderBasedAbsolutePath(path) ;
		
		if (absolutePath == null || absolutePath.isEmpty()) {
			
			logger.error("_563 computeDocumentAbsolutePathAndbase64Content :: "
					+ "Erreur de calcul du chemin absolu de :" + path);
			return  null ;
		}
		
		return computeBytesDocumentContent(absolutePath) ;
		
		
	}
	
	@SuppressWarnings("resource")
	public byte[] computeBytesDocumentContent(String path) throws IOException {

		if (path == null || path.isEmpty()) return  null ;
		
		logger.info("_582 computeDocumentbase64Content :: path=" + path); 
		
		
		//Instanciantion du fichier 
		
		File filePath = new File(path) ; 

		//Creating a Scanner object
	    
		Scanner sc = new Scanner(filePath);
	     
	    //StringBuffer to store the contents
	    StringBuffer sb = new StringBuffer();
	    
	    //Appending each line to the buffer
	    while(sc.hasNext())
	         sb.append(" "+sc.nextLine());
	   
	    String[] content = sb.toString().split(",") ;
	   
	    byte[]  decodedBytes = Base64.getDecoder().decode(content[1]);
	    
		logger.info("_789 computeBytesDocumentContent decodedBytes taille= "+ decodedBytes);
		
		return decodedBytes ;
		
		
	}

	

	@Override
	public byte[] findBytesContentDocumentParPath(
			         String path, List<NonLocalizedStatusMessage> msgList) {
		
		
		byte[]  contentBytes = null ;
		 
		if (path != null && !path.isEmpty()) {
			
			try {
				
			      contentBytes = computeBytesDocumentAbsolutePathContent(path) ;
				
			}
			catch (Exception ex) {
				
				String translatedMessage = MessageTranslationUtil.translate(langue,
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR, // Message par defaut
						new String[] {});
	
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
						LocalDateTime.now().toString() + " " + translatedMessage));
				
				logger.error("_693 findDocumentById :: "+translatedMessage + ex + ":" + ex.getMessage()
							+" Cause:"+ex.getCause());
				ex.printStackTrace();
				
				return null ;	
				
			 }
			
		 }
		
		return contentBytes ;
	
	}


	
	
	/**
	 * Ajout par Alzouma Moussa Mahamadou
	 * 
	 * date: 03/08/2021
	 * 
	 * Methode suppression d'un document sur le disque dur
	 * 
	 */
	  
	@Override
	public boolean deleteStringContentDocumentByPath(
			         String path, List<NonLocalizedStatusMessage> msgList) {
		
		
	   if (path == null || path.isEmpty()) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_DOCUMENT_PATH_NON_DEFINI, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_DOCUMENT_PATH_NON_DEFINI, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return  false ;
			
		}
		
	    //Récuperation du chemin  absolu 
	   
		String absolutePath = AppConfigUtil.computeAppFolderBasedAbsolutePath(path) ;
		
		if (absolutePath == null || absolutePath.isEmpty()) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_DOCUMENT_PATH_ABSOLU_NON_DEFINI, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_DOCUMENT_PATH_ABSOLU_NON_DEFINI, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			logger.error("_752 computeDocumentAbsolutePathAndbase64Content :: "
					+ "Erreur de calcul du chemin absolu de :" + path);
			
			return  false ;
			
		}
		
		
		// Instanciation 
		
	   	File filePath = new File(absolutePath) ; 
	   	
	    //Verifier si le fichier existe avant la suppressin
	   	
	   	if(!filePath.exists()) {
	   		
	   		String translatedMessage = MessageTranslationUtil.translate(langue,
					AppMessageKeys.CODE_TRADUCTION_DOCUMENT_CONTENT_NO_EXIST,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DOCUMENT_CONTENT_NO_EXIST, // Message par defaut
					new String[] {});

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
					LocalDateTime.now().toString() + " " + translatedMessage));
	   		
	   		return true ;
	   	}
	   	
	 
		
	   	if(!filePath.delete()) {
	   		
	   		String translatedMessage = MessageTranslationUtil.translate(langue,
					AppMessageKeys.CODE_TRADUCTION_DOCUMENT_CONTENT_NO_DELETE,// venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DOCUMENT_CONTENT_NO_DELETE, // Message par defaut
					new String[] {});

			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
					LocalDateTime.now().toString() + " " + translatedMessage));
	   		
	   		return false ;
	   		
	   	}
	   	
	   	
		return true;
		
	}



	@Override
	public String updateStringContentDocument(
				          Document entity,
				          boolean mustUpdateExistingNew, 
				          boolean updateStringContentDocument,
				          String namedGraph,
			           	  boolean isFetchGraph, Locale locale, 
			           	  User loggedInUser, 
			           	  List<NonLocalizedStatusMessage> msgList) {
		
		
		 //Verification de l'id de l'entité
		 
	     if (entity.getId() == null || entity.getId().isEmpty()) {
	    	 
	    	 Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
				String msg  = MessageTranslationUtil.translate(locale,
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
					ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
					msgVarMap) ;
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
	    	 
	    	  return  null ;
	    	  
	     }
	     
	     //Remettre dans le contexte de persistence si l'entité existe
		
		 Document document = EntityUtil.findEntityById(entityManager, 
				           entity.getId(), null, true, Document.class) ;
		 
		 
		 //Verification de l'entité mise en contexte
		
		 if (document == null) {
			 
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
					msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
				
			return null ;
			 
		 }
		 
		 
		 //Verification du path du document trouvé
		 
		 if (document.getPath() == null || document.getPath().isEmpty()) {
			 
			 Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
			 String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DOUCMENT_PATH_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DOUCMENT_PATH_NON_TROUVE, // Message par defaut
						msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			 
			return null ;
			
		 }
		
		 //Supression du document sur le disque dur
		 
		 boolean value = deleteStringContentDocumentByPath(document.getPath(), msgList) ;
		 
		 
		 //Si la variable est à false , retourne null
		 
		 if(!value) return null ;
		 
		 //Creation du nouveau document et modification de l'ancien path par le nouveau
		
	 	 logger.info("_873 updateStringContentDocument debut validerEtEnregistrer="+document.toString());
		 
		 //Verification du nouveau contenu 
		
		 if (entity.getContenu() == null) return null ;
		
		 //Recuperation du contenu
		 
		 document.setContenu(entity.getContenu());
		 
		 //Appel à la methode validerEtEnregistrer 
		 
		 Document rtn = validerEtEnregistrer(
				 			   document, mustUpdateExistingNew,  
				 			   updateStringContentDocument,namedGraph,
				           	   isFetchGraph,  locale,  loggedInUser,msgList) ;
		 
		 
		 //Recuperation du nouveau contenu crée
		 
		 if(rtn.getPath() == null) return null ;
		 
		 //Appel à la methode pour recuperer le nouveau le contenu
		 
		 
         String content = findStringContentDocumentParPath( rtn.getPath(), msgList) ;
		 
		 return content ;
		
	
	}


	@SuppressWarnings({ "static-access"})
	@Override
	public boolean supprimer(
			         String idDocument, 
			         boolean mustUpdateExistingNew, 
			         String namedGraph, boolean isFetchGraph, 
			         Locale locale, User loggedInUser,
			         List<NonLocalizedStatusMessage> msgList) {
	
		
		// Verification de la variable
		
		if(idDocument == null || idDocument.isEmpty() ) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // venant du fichier
				ProduitLogement.CODE_TRADUCTION_IDENTIFIANT_NON_DEFINI, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
			
		}
		
		//Remettre l'entité dans le contexte de persistence
		
	    EntityUtil<Document> entityUtil = new EntityUtil<Document>(Document.class) ;
		
		//Document document = entityManager.find(Document.class, idDocument) ;
	    
		
	    Document document =  entityUtil
	    		               .findEntityById(entityManager, idDocument, "graph.document.minimum-sans-contenu", isFetchGraph, Document.class) ;
	    
	    		              
		if (document == null) {
			
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
			
			String msg  = MessageTranslationUtil.translate(locale,
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // venant du fichier
				AppMessageKeys.CODE_TRADUCTION_ENTITE_NON_TROUVE, // Message par defaut
				msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			
			return false ;
			
		}
		
		
		//Verification du path du document 
		
		if (document.getPath() == null || document.getPath().isEmpty()) {
			
			Map<String,String> msgVarMap =  new HashMap<String,String>(); ;
				
			String msg  = MessageTranslationUtil.translate(locale,
					AppMessageKeys.CODE_TRADUCTION_DOUCMENT_PATH_NON_TROUVE, // venant du fichier
					AppMessageKeys.CODE_TRADUCTION_DOUCMENT_PATH_NON_TROUVE, // Message par defaut
						msgVarMap) ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ; 
			 
			return false ;
			
		}
		
		
		//Récuperation du path avant suppresion
		
		String pathRelatif = document.getPath() ;
		
		//Suppression des metadatas des documents
		
		entityManager.remove(document);
		
		//Verification de la suppression de l'entité document
		
		logger.info("_990 Debut de suppression ");
		
		Document deleteDocument = entityManager.find(Document.class, idDocument) ;
		
		if(deleteDocument != null) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return false ;
		}
		
		
		//Suppression du contenu du document sur le disque dur 
		
		boolean value = deleteStringContentDocumentByPath(pathRelatif, msgList) ;
		
		
		if(!value) {
			
			try{ ejbContext.setRollbackOnly(); } catch(Exception exx){} 
			
			return false ;
			
		}
		
		
	   return true;
	
	}


	@Override
	public String findStringContentDocumentParPath(
			         String path, List<NonLocalizedStatusMessage> msgList) {
		
		
		String content = null ;
		 
		if (path != null && !path.isEmpty()) {
			
			try {
				
				 content = computeStringDocumentAbsolutePathContent(path) ;
				
			}
			catch (Exception ex) {
				
				String translatedMessage = MessageTranslationUtil.translate(langue,
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR,// venant du fichier
						AppMessageKeys.CODE_TRADUCTION_DOCUMENT_READ_WRITE_ERROR, // Message par defaut
						new String[] {});
	
				msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, 
						LocalDateTime.now().toString() + " " + translatedMessage));
				
				logger.error("_693 findDocumentById :: "+translatedMessage + ex + ":" + ex.getMessage()
							+" Cause:"+ex.getCause());
				ex.printStackTrace();
				
				return null ;	
				
			 }
			
		 }
		
		return content ;
	
	}
	
}
