/**
 * 
 */
package com.siliconwise.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class
 * @author GNAKALE Bernardin
 *
 */
public class Utility {

	private static final char[] charactersTable = new char[]
			{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'} ;
	
	private static final String TEXTE_MILLE = "mille" ;
	private static final String TEXTE_CENT = "cent" ;
	private static final String TEXTE_MILLION = "million" ;
	private static final String TEXTE_ONZE = "onze" ;
	
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger("com.siliconwise.common.Utility") ; 
	
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
 
       // logger.info("Utility::encryptToMD5 plain="+word+" crypted="+ sb.toString());
        return(sb.toString());
	}
	
	
	/**
	 * Generate a readable random String
	 * @param length password length
	 * @return
	 */
	public static String generateReadableRandomString(int length)
	{

		char[] buffer = new char[8];
		
		Random random = new Random(Calendar.getInstance().get(Calendar.MILLISECOND));
		
		for (int i = 0 ; i < buffer.length; i++)
		{
			buffer[i] = charactersTable[random.nextInt(charactersTable.length)];
		}
		
		return(new String(buffer));
	}

	/**
	 * Convert non printable  to whitespace and trim the string
	 * @param s
	 * @return
	 */
	public static String replaceNonPrintableWithWhiteSpace(String s) {
		
		if (s == null || s.equals("")) return s ;
		
		char[] cTab = s.toCharArray() ;
		
		String otherCars = "'\".,:!/*/\\@#$£%ù=+-()[]{})" ;
			
		for (int i = 0 ; i < cTab.length ; i++) {
			
			if (!Character.isLetterOrDigit(cTab[i]) && !Character.isWhitespace(cTab[i])
					&&  otherCars.indexOf(cTab[i]) == -1) 
				cTab[i] = ' ' ;
		}

		String rtn = (new String(cTab)).replaceAll("  ", "&nbsp;&nbsp;")
				.replaceAll(" ", "").replaceAll("&nbsp;&nbsp;", " ");
		return rtn ;
	}


	/*
	* Extract designation from Adress
	* Address is companydesignation [address part]
	*   address part begins with un digit or the word "BP"
	*/
	public static String extractNameFromNameAddress(String text) {
	
		String rtn = "" ;
		
		if (text == null) return null ;
		
		if (text.equals("")) return "" ;
	  		
		int bpPosition = text.toLowerCase().indexOf("bp") ;
		//logger.info("Utility :: extractNameFromNameAddress :: text.toLowerCase()=  "+text.toLowerCase());
		//	logger.info("Utility :: extractNameFromNameAddress :: bpPosition=  "+bpPosition);
	  		
		// position du 1er chiffre
	  		
		int digitPosition = -1 ;
	  		
		for (int i = 0 ; i < text.length() ; i++) {
	  			
			Character c = text.charAt(i) ;
	  			
			if (Character.isDigit(c)) {
	  				
				digitPosition = i ;
				break ;
			}
		}
		// logger.info("Utility :: extractNameFromNameAddress :: text.toLowerCase()=  "+text.toLowerCase());
		//logger.info("Utility :: extractNameFromNameAddress :: digitPosition=  "+digitPosition);
		int position = -1 ;
		
		if (digitPosition == -1) position = bpPosition ;
		else if (bpPosition == -1) position = digitPosition;
		else position = Math.min(digitPosition, bpPosition) ;
		
		///logger.info("Utility :: extractNameFromNameAddress :: position=  "+position);
		rtn = position != -1 ? text.substring(0, position) : text ;
	  		
		return rtn ;
	}
	
	// ------------- Traduction nombre en lettre --------------------------
	
	
}
