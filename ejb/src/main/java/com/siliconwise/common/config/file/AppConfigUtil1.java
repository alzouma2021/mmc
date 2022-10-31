/**
 * 
 */
package com.siliconwise.common.config.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.ejb.Timer;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global application utility
 * @author GNAKALE Bernardin
 *
 */
public class AppConfigUtil1 implements Serializable  {

	private static final long serialVersionUID = 1L;

	public AppConfigUtil1() {
		
	}
	
	/**
	 * Reference to SLF4J logger
	 * @return
	 */
	@Produces @ApplicationScoped
	public static final Logger logger = LoggerFactory.getLogger(AppConfigUtil.class.getPackage().getName());
	
	private static Properties appConfig = null ;
	private static final File configFolder = null;
	
	public static Timer transfertTimer = null ;
	public static Timer logMaintenanceTimer = null ;
	
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
			//logger.info(AppConfigUtil.class.getName()+"::loadAppConfig():: configFile.name="+configFile.getName());					
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
	
	public static Properties loadTranslateConfigFile()
	{			
		Properties configProps = new Properties() ;
		
		FileReader configFileReader = null ;

		try{
			File configFile = getFileInFolder( getConfigFolder(), AppConfigKeys.SYSTEM_CONFIG_FILE_NAME);
			//logger.info(AppConfigUtil.class.getName()+"::loadAppConfig():: configFile.name="+configFile.getName());					
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
	
	
	private static File readConfigFolder()
	{
		String configFolderPath = System.getenv(AppConfigKeys.SYSTEM_ENV_VAR_APPLICATION_HOME);
		
		if (configFolderPath == null || configFolderPath.equalsIgnoreCase(""))
		{
			configFolderPath = AppConfigKeys.SYSTEM_ENV_VAR_APPLICATION_HOME_DEFAULT_VALUE ;
		}
		
		File configFolder = new File(configFolderPath);
		
		if (configFolder.exists() && configFolder.isDirectory()) return(configFolder) ;
		else return(null);
	}
	
	/**
	 * Return configuration folder set in environment variable EPiSYS_HOME
	 * @return
	 */
	public static File getConfigFolder()
	{
		return configFolder == null ? readConfigFolder() : configFolder ;
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
	


}
