/**
 * 
 */
package com.siliconwise.common.config.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.ejb.Timer;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.MessageTranslationUtil;


/**
 * Global application utility
 * @author GNAKALE Bernardin
 *
 */
public class AppConfigUtil implements Serializable  {

	private static final long serialVersionUID = 1L;

	public static final String CODE_TRADUCTION_CONFIG_KEY_SYSTEM_URL_RETRIEVAL_ERROR_1V = "config-key.system-url.retrieval-error.1v" ;

	
	public AppConfigUtil() {
	}
	
	/**
	 * Reference to SLF4J logger
	 * @return
	 */
	@Produces @ApplicationScoped
	public static final Logger logger = LoggerFactory.getLogger(AppConfigUtil.class.getName());
	
	private static Properties appConfig = null ;
	private static final File configFolder = null;
	
	public static Timer transfertTimer = null ;
	public static Timer logMaintenanceTimer = null ;
	
	// translation
	// key: 2 letters language code
	private static Map<String, Properties> translateMap = new HashMap<>() ;
	private static Map<String, Properties> countryTranslateMap = new HashMap<>() ;
	
	/**
	 * Load property file
	 * @param filePath: relative path from config folder
	 * @return
	 */
	private static Properties loadPropertyFile(String fileName) {

		Properties configProps = new Properties() ;
		
		
		FileReader configFileReader = null ;

		if (fileName == null || fileName.equals("")) {
			return null ;
		}
		
		File configFile = null ;
		
		try {
			
			logger.info(fileName);
			configFile = getFileInFolder( getFolderDefinedInAppConfig(AppConfigKeys.CONFIG_KEY_TRANSLATE_FOLDER), fileName);
			configFileReader = new FileReader(configFile) ;
			configProps.load(configFileReader);
			
		}
		catch(FileNotFoundException ex) {
			configProps = null ;
			String msg = "Fichier "  + configFile.getName() + " non trouvé." ;
			logger.error(msg);
		}
		catch(IOException ex) {
			configProps = null ;
			String msg = "Erreur d'e/s sur le fichier "  + configFile.getName() + "." ;
			logger.error(msg);
		}
		
		return configProps ;
	}
	
	/*
	 * Load application configuration file as property object
	 * @param fileName
	 * @return
	 */
	private static Properties loadAppConfig()
	{			
		Properties configProps = new Properties() ;
		
		FileReader configFileReader = null ;

		try{
			File configFile = getFileInFolder( getConfigFolder(), AppConfigKeys.SYSTEM_CONFIG_FILE_NAME);
								
			configFileReader = new FileReader(configFile) ;
			configProps.load(configFileReader);
		}
		catch(FileNotFoundException ex)
		{		
			String msg = "FATAL --- Le fichier de configuration du serveur 'a pas été trouvé. " +
					"Le serveur ne marchera pas correctement!!!!!!!";	
			logger.error(msg);	ex.printStackTrace();

		}
		catch(Exception ex)
		{		
			String msg = "FATAL --- Erreur à l'initialisation du fichier de configuration du système. " +
					"Le serveur ne marchera pas correctement!!!!!!!";	
			logger.error(msg); 	ex.printStackTrace();			
		}
		finally
		{		
			if (configFileReader != null)
				try {
					configFileReader.close() ;
				} 
				catch (IOException e) 
				{
					String msg = "Erreur à la fermeture du fichier de configuration.";	
					logger.error(msg); 	e.printStackTrace();
				}			
		}
		
		return(configProps);
	}
	
	/**
	 * Load get application global config 
	 * @return config
	 */	
	@Produces @ApplicationScoped @AppConfig 
	public static Properties getAppConfig()
	{
		if (appConfig == null) appConfig = loadAppConfig();	
		
		/*logger.info(AppConfigUtil.class.getName()+"::getAppConfig():: appConfig.size="+appConfig.entrySet().size());
		for (Entry<Object, Object> entry : appConfig.entrySet()) {
			logger.info(AppConfigUtil.class.getName()+"::getAppConfig():: entry key="+entry.getKey().toString()+" value=" + entry.getValue().toString());
		}*/
		
		return(appConfig);
	}
	
	/**
	 * Lit le dossier contenant les fichiers de configuration
	 * 
	 * @return 
	 */
	private static File readConfigFolder()
	{
		String configFolderPath = System.getenv(AppConfigKeys.SYSTEM_ENV_VAR_APPLICATION_HOME);
		
		if (configFolderPath == null || configFolderPath.equalsIgnoreCase(""))
		{
			configFolderPath = AppConfigKeys.SYSTEM_ENV_VAR_APPLICATION_HOME_DEFAULT_VALUE ;
		}
		
		File configFolder = new File(configFolderPath);
		
		if (configFolder.exists() && configFolder.isDirectory())return(configFolder);
		else return(null);
	}
	
	/**
	 * Return configuration folder set in environment variable NIMS_HOME
	 * @return
	 */
	public static File getConfigFolder()
	{
		return configFolder == null ? readConfigFolder() : configFolder ;
	}
	
	/**
	 * Crée un dossier dans le repertoire de l'application 
	 * @param nameFolder: folder name
	 * @return
	 */
	public static File getDocumentFolder(String nameFolder) {
		
		logger.info("entrée dans getDocumentFolder()");
		
		File configFolder = getConfigFolder() ;
		File docFolder = new File(configFolder, nameFolder);		
		if (!docFolder.exists()) docFolder.mkdir() ;
		
		return docFolder ;
	}
	
	/** Copute absolute path from a given path. If given path is already absolute, then return it
	 * If path is not absolute, return absolute path considerinf this relative path based on app config folder
	 * If path i snull return null
	 * @param path
	 * @return
	 */
	public static String computeAppFolderBasedAbsolutePath(String path) {
		
		if (path == null) return null ;
		
		path = path.trim() ;
		
		if (path.indexOf('/') == 0) return path ; // absolute path --> return it
		
		File file = new File(getConfigFolder(), path) ;
		
		return file.getAbsolutePath() ;
	}
	
	/** Compute relative path from given absolute path based on app folder
	 * If path i snull return null
	 * @param path
	 * @return
	 */
	public static String computeAppFolderBasedRelativePath(String path) {
		
		if (path == null) return null ;
		
		path = path.trim() ;
		
		String absoluteConfigFolderPath = getConfigFolder().getAbsolutePath() ;
		
		boolean isRelative = path.indexOf(absoluteConfigFolderPath) == 0 ;
		
		String rtn = isRelative
				? path.substring(absoluteConfigFolderPath.length())  // file or folder included in app config folder
				: path ; // not included in app folder or already a relative path
		
		if (isRelative && rtn.indexOf('/') == 0) rtn = rtn.substring(1) ;
		
		return rtn  ;
	}
	
	/**
	 * Créer un fichier sur le DD et recuperer le chemin du fichier crée
	 * @param String folderName
	 * @param nomFichier Nom du fichier
	 * @return String filePath le chemin du fichier créé
	 * @throws IOException 
	 */
	public static String savedFileOnHardDisk(
			String folderName, String contenu, String nomFichier) throws IOException {
		
		
		
		// créer un dossier pour conserver un document
		
		File  createdFolder = AppConfigUtil.getDocumentFolder(folderName) ;
		
		// Construire le nom du fichier
		//String filePath =  createdFolder. +"\""+ nomFichier ;
		File filePath =  new File(createdFolder, nomFichier) ;

		logger.info("entrée dans getFileName() filePath = "+ filePath.getAbsolutePath());
		
				
		if(contenu == null) return null ;
		
		//Conversion du contenu du document en fichier binaire

		
		byte[]  decodedBytes = Base64.getDecoder().decode(contenu);
		
		logger.info("decodedBytes taille= "+ decodedBytes.length);
		
		
		writeFile(decodedBytes, createdFolder , nomFichier) ;
	
		
		return filePath.getAbsolutePath() ;
		
		
	}
	
	/**
	 * Crée  un fichier sur le DD
	 * @param content
	 * @param fileName
	 * @throws IOException 
	 */
	
	public static void writeFile (byte[] content , File parent, String fileName  ){
		
		logger.info("entrée dans writeFile() ");
		
		if(content == null ) return ;
		
		File file = new File(parent, fileName) ;

		try(FileOutputStream fop = new FileOutputStream(file)){
			
			// IOUtils.write(content, fop);
			
			if (!file.exists()) {
				logger.info("entrée dans file.exists() "+ file.exists());
				file.createNewFile();
			}
			//FileOutputStream fop = new FileOutputStream(file) ;
			if(content != null) {
				
				fop.write(content);
				fop.flush();
				fop.close();
			}
			
		}catch (IOException e) {
			e.printStackTrace();
			return  ;
		}
	}
	
	
	/**
	 * Réecriture de la methode writeFile par Alzouma Moussa Mahamadou
	 * Date: 17/07/2021 à 13:02
	 * permet d'ecrire le contenu du document de type string dans un fichier
	 */
	
	
	public static void writeStringFile (String content , File parent, String fileName  ){
		
		logger.info("entrée dans writeFile() ");
		
		if(content == null ) return ;
		
		File file = new File(parent, fileName) ;
		
		//TODO Utilisation du PrintWriter pour enregistrer un fichier de type string

		try(PrintWriter pwr = new PrintWriter(file)){
			
			
			if (!file.exists()) {
				logger.info("entrée dans file.exists() "+ file.exists());
				file.createNewFile();
			}
			
			if(content != null) {
				pwr.print(content);
				pwr.flush();
				pwr.close();
			}
			
		}catch (IOException e) {
			e.printStackTrace();
			return  ;
		}
		
	}
	
	
	/**
	 * 
	 * @param folderName
	 * @param contenu
	 * @param nomFichier
	 * @return
	 * @throws IOException
	 */
	public static String savedStringFileOnHardDisk(
			String folderName, String contenu, String nomFichier) throws IOException {
		
		
		
		File  createdFolder = AppConfigUtil.getDocumentFolder(folderName) ;
		
		// Construire le nom du fichier
		//String filePath =  createdFolder. +"\""+ nomFichier ;
		File filePath =  new File(createdFolder, nomFichier) ;

		logger.info("entrée dans getFileName() filePath = "+ filePath.getAbsolutePath());
		
				
		if(contenu == null) return null ;
		
		
		writeStringFile(contenu, createdFolder , nomFichier) ;
		
		return filePath.getAbsolutePath() ;
		
		
	}
	
	
	
	/**
	 * Retrieve application temprary folder
	 * @return
	 */
	public static File getTemporaryFolder() {
		
		File configFolder = getConfigFolder() ;
		File tempFolder = new File(configFolder, AppConfigKeys.SYSTEM_TEMPORARY_FOLDER_NAME);		
		if (!tempFolder.exists()) tempFolder.mkdir() ;
		
		return tempFolder ;
	}
	
	/**
	 * Retrieve folder wich key is defined in app configuration
	 * @param configFolderKey
	 * @return
	 */
	public static File getFolderDefinedInAppConfig(String configFolderKey) {
		
		File configFolder = getConfigFolder() ;
		Properties appConfig = getAppConfig() ;
		
		
		String docName = appConfig.getProperty(configFolderKey) ;
		//File folder = new File(configFolder, AppConfigKeys.TRANSLATE_FOLDER);	
		File folder = new File(configFolder, docName);	
		if (!folder.exists()) folder.mkdir() ;
		
		logger.info(folder.getAbsolutePath());
		
		return folder ;
	}
	
	/*
	 * Return a file in a directory or null if file has not been found
	 * @param fileName
	 * @return
	 */
	public static File getFileInFolder(File folder, String fileName)
	{
		
		if (folder == null || !folder.isDirectory()) return(null); // folder does not exist
		
		for (File myFile : folder.listFiles())
		{
			if (myFile.getName().equalsIgnoreCase(fileName)) return(myFile);
		}
		
		return(null) ; // not found
	}
	
	/** Retrieve file content from translation folder
	 *  The file name will be adjusted by the language code fr, en, etc.
	 *  Example supplied file name is filename.ext
	 *  	if langage code is fr then file will be filename_fr.ext
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static String extractTranslationFileContent(String fileName, Locale lang) 
			throws IOException {
		
		if (fileName == null || fileName.isEmpty() || lang == null) return null ;
		
		
		// get translation folder
		File translationFolder =  getFolderDefinedInAppConfig(AppConfigKeys.CONFIG_KEY_TRANSLATE_FOLDER) ;
		
		String langCode =  MessageTranslationUtil.extractLanguageISOCode(lang)  ;   // lang.getLanguage() ;
		
		
		int index = fileName.lastIndexOf('.') ;
		
		String realFileName = null ;
		
		if (index == -1) { // no extension
			
			realFileName = fileName + "_" + langCode ;
		}
		else if (index == fileName.length() - 1) { 
			// chaine terminé par un .:  "root."  -> relname: root_langCode 
			realFileName = fileName.substring(0, index)  + "_" + langCode ;
		}
		else { // fileName= root.ext --> real name = root_langCode.ext
			realFileName = fileName.substring(0, index)  + "_" + langCode + "." + fileName.substring(index+1) ;
		}
		
		logger.info("extractTranslationFileContent :: fileName=" + fileName + " realFileName="+ realFileName); ;
		
		File file = getFileInFolder(translationFolder, realFileName) ;
		logger.info("extractTranslationFileContent :: extracted file=" + (file == null ? "null" : file.getAbsolutePath())); ;
		
		if (file == null) return null ;
		
		String rtn = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), 
										StandardCharsets.UTF_8) ;
		logger.info("extractTranslationFileContent :: extracted file content=" + rtn) ;

		return rtn ;
	}
	
	public static String encryptToMD5(String word) throws NoSuchAlgorithmException
	{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(word.getBytes());
 
        byte byteData[] = md.digest();
 
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        return(sb.toString());
	}
	
	// translation

	public static Properties getTanslateMap(Locale locale) {
	
		if (locale == null) return null ;
		
		String lCode = MessageTranslationUtil.extractLanguageISOCode(locale) ;
		
		if (lCode == null || lCode.isEmpty()) return null ;
		
 		if (AppConfigUtil.translateMap.isEmpty() || translateMap.get(lCode) == null)
 			loadTranslateFiles();
		
		return AppConfigUtil.translateMap.get(lCode);
	}

	public static Properties getCountryTanslateProperties(Locale locale) {
		
		//logger.info("_475 getCountryTanslateProperties :: locale langue"
		//		+locale.getLanguage()+ " contry="+locale.getCountry());
		
		if (locale == null) return null ;
		
		String lCode = MessageTranslationUtil.extractLanguageISOCode(locale) ;
		
		if (lCode == null || lCode.isEmpty()) return null ;

		Properties props  = AppConfigUtil.countryTranslateMap.get(locale.getLanguage()) ;
		
		logger.info("_413 getCountryTanslateProperties avnt prosp==null :: locale="+locale
				+" props.size()="+(props != null ? props.size():"null"));
		
		if (props == null) {
			
			loadTranslateFiles();
			props = AppConfigUtil.countryTranslateMap.get(locale.getLanguage()) ;
		}
		
		logger.info("_409 getCountryTanslateProperties avant return :: locale="+locale
				+" props.size()="+(props != null ? props.size():"null"));
		
		return props ;
	}

	
	/*
	 * load  translation files
	 */
	private static void loadTranslateFiles() {
		
		Properties appConfig = getAppConfig() ;
		
		// extract translated languages
		
		String s = appConfig.getProperty(AppConfigKeys.CONFIG_KEY_TRANSLATE_LOCALE) ;
		
		logger.info("loadTranslateFiles "+s);
		
		if (s == null || s.equals("")) {
			
			String msg = "FATAL --- Les langues de traductions ne sont pas défini dans le fichier de configuration systeme!!!!!!!";	
			logger.error(msg);
		}
		
		// AppConfigkeys.TRANSLATE_LANGUAGES_SEPARATOR = ","
		String[] sLanguageList = s.split(AppConfigKeys.TRANSLATE_LOCALE_LIST_SEPARATOR) ;
		
		logger.info("sLanguageList taille"+sLanguageList.length);
			
		String defaultTranslateFilePath = 
				AppConfigKeys.TRANSLATE_FILE_PREFIX + "."  + AppConfigKeys.TRANSLATE_EXTENSIONS ;
		
		logger.info("defaultTranslateFilePath = "+defaultTranslateFilePath);
		
		
		for (String sLanguage :  sLanguageList) {
			
			Locale locale = MessageTranslationUtil.createLocale(
					sLanguage, AppConfigKeys.TRANSLATE_LOCALE_LANGUAGE_COUNTRY_SEPARATOR) ;
		
			if (locale == null) continue ;
			
			logger.info("_475 loadTranslateFiles :: locale langue"
					+locale.getLanguage()+ " contry="+locale.getCountry());
			
			String dynamicFilePath = AppConfigKeys.TRANSLATE_FILE_PREFIX + "_" 
											+ locale.getLanguage() + "."  
											+ AppConfigKeys.TRANSLATE_EXTENSIONS ;
			
			
			if (AppConfigUtil.translateMap.get(locale.getLanguage())  == null) { //dynamic language not yet loaded
				
				Properties props = loadPropertyFile(dynamicFilePath) ;
				
				if (props == null) props = loadPropertyFile(defaultTranslateFilePath) ;
				
				if (props == null) {
					
					String msg =  "Erreur FATAL: Impossible de charge le fichier de traduction dynamique de la langue " + locale ;
					logger.error(msg);
					return ;
				}
				else {
					AppConfigUtil.translateMap.put(locale.getLanguage(), props);
				}
			}
			
			// load country translation files  countryTranslateMap
			
			String countryFileName = AppConfigKeys.TRANSLATE_COUNTRY_FILE_NAME_PATTERN
								.replaceAll(":_locale:", "_" + locale.getLanguage()) ;
			
	
			if (countryTranslateMap.get(locale.getLanguage())  == null) { //country language not yet loaded
				
				Properties props = loadPropertyFile(countryFileName) ;
				
				logger.info("_523 loadTranslateFiles countryTranslateMap.size="
								+ (props != null ? props.size(): "null"));
				
				if (props == null) props = loadPropertyFile(AppConfigKeys.TRANSLATE_COUNTRY_FILE_NAME_DEFAULT) ;

				if (props == null) {
					
					String msg =  "Erreur FATAL: Impossible de charger le fichier de traduction des pays pour la langue " + locale ;
					logger.error(msg);
					return ;
				}
				else {
					AppConfigUtil.countryTranslateMap.put(locale.getLanguage(), props);
					Properties p = AppConfigUtil.countryTranslateMap.get(locale.getLanguage()) ;
					logger.info("_537 loadTranslateFiles :: apres countryTranslateMap.put :: countryTranslateMap.get(locale) size="
								+(p != null ? p.size(): "null"));
				}
				
			}
		}
	}
	

	/** Return platform url (protocole://host:port)
	 * Exemple: http://nims.nafs.global:8080
	 * @return
	 */
	public static String getSystemUrl() {
		
		Properties config = getAppConfig() ;
		
		String rtn = config != null 
						? config.getProperty(AppConfigKeys.CONFIG_KEY_SYSTEM_URL, 
										AppConfigKeys.CONFIG_DEFAULT_VALUE_SYSTEM_URL)
						: AppConfigKeys.CONFIG_DEFAULT_VALUE_SYSTEM_URL ;
		
		return rtn ;
	}
}
