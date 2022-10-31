package com.siliconwise.mmc.oldSecurity;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;

@Stateless
public class GeolocalisationPays implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;

	
	public String  estBlacliste(String adresseIp, List<NonLocalizedStatusMessage> msgList) 
			throws IOException, GeoIp2Exception {
		
		
		logger.info(getClass().getName() + " Entrée:: GeolocalisationPays ::") ;
		
		File database = new File("C:\\nims\\geolocalisation","GeoLite2-Country.mmdb");
		
		logger.info(getClass().getName() + " Après lecture du fichier :: GeolocalisationPays ::\n"
				+ "filePath = "+database.getAbsolutePath()) ;
		
		DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
		
		InetAddress ipAddress = null;
		 
		ipAddress = InetAddress.getByName(adresseIp);
			logger.info(getClass().getName() 
					+":: GeolocalisationPays :: json= "+ ipAddress.getHostAddress());
		
		CityResponse response = dbReader.city(ipAddress);
		
		String  pays = response.getCountry().getName() ;
		
		return pays ;
		
		
	}
	
	
}
