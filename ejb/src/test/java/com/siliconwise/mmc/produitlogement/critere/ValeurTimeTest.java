package com.siliconwise.mmc.produitlogement.critere;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValeurTimeTest {

	

	ValeurTime valeurTime ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurTime
		valeurTime = new ValeurTime();
	}
	
	@SuppressWarnings({ "static-access", "unchecked" })
	@Test
	void testFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		@SuppressWarnings("rawtypes")
		ArrayList time = new ArrayList();
			time.add("10:10") ;
			time.add("11:10") ;
			time.add("12:10") ;
		
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(time);
		
		//Exection et Verification 
			ArrayList<LocalTime> reslt = new ArrayList<LocalTime>();
			
			reslt.add(LocalTime.parse("10:10", DateTimeFormatter.ISO_LOCAL_TIME)) ;
			reslt.add(LocalTime.parse("11:10", DateTimeFormatter.ISO_LOCAL_TIME)) ;
			reslt.add(LocalTime.parse("12:10", DateTimeFormatter.ISO_LOCAL_TIME)) ;
			
		assertEquals(reslt , valeurTime.from(valeur).getValeur());
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	void testNullTimeFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrantes
		Valeur<Object> valeur = null;
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(NullPointerException.class,()->valeurTime.from(valeur).getValeur());
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testNotTimeFrom() {
		
		//fail("Not yet implemented");
				
		//Initialisation des valeurs entrants
		@SuppressWarnings("rawtypes")
		ArrayList<String> time = new ArrayList<String>();
		
			time.add("nonTime") ;
			time.add("nonTime") ;
			time.add("nonTime") ;
			
		Valeur<Object> valeur = new Valeur<Object>();
		valeur.setValeurTexte(time);
		
		//Execution et Verification de l'envoi d'une exception
		assertThrows(DateTimeParseException.class,()->valeurTime.from(valeur).getValeur());
		
	}

}
