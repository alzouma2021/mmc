package com.siliconwise.mmc.produitlogement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementBoolean;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDouble;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementInteger;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementLong;

/*
class ProduitLogementDAOTest {
	
	
	private static transient Logger logger = LoggerFactory.getLogger(ProduitLogementDAOTest.class) ;
	
	 CaracteristiqueProduitLogement caracteristiqueProduitLogement = new CaracteristiqueProduitLogement();
	
	//Cas normal
	@Test
	void testEstCaracteristiqueProduitLogementObligatoireValide() {
		
		
		//Initialisation des entrants
		
		Set<CaracteristiqueProduitLogement> entitySet = new HashSet<CaracteristiqueProduitLogement>();
		
		ProprieteProduitLogement proprieteProduitLogement = new ProprieteProduitLogement();
		proprieteProduitLogement.setEstObligatoire(true);
		
		
		ValeurCaracteristiqueProduitLogementInteger valeurInteger = new ValeurCaracteristiqueProduitLogementInteger();
		valeurInteger.setProprieteProduitLogement(proprieteProduitLogement);
		valeurInteger.setValeur(2);
		entitySet.add(valeurInteger);
		
		ValeurCaracteristiqueProduitLogementBoolean valeurBoolean = new ValeurCaracteristiqueProduitLogementBoolean();
		valeurBoolean.setProprieteProduitLogement(proprieteProduitLogement);
		valeurBoolean.setValeur(true);
		entitySet.add(valeurBoolean);
		
		
		ValeurCaracteristiqueProduitLogementLong valeurLong = new ValeurCaracteristiqueProduitLogementLong();
		valeurLong.setProprieteProduitLogement(proprieteProduitLogement);
		valeurLong.setValeur(2L);
		entitySet.add( valeurLong);
		
		ValeurCaracteristiqueProduitLogementDouble valeurDouble = new ValeurCaracteristiqueProduitLogementDouble();
		valeurDouble.setProprieteProduitLogement(proprieteProduitLogement);
		valeurDouble.setValeur(2D);
		entitySet.add(valeurDouble);
		
		
		//Execution et Verification des sortants
		
		assertEquals(true , caracteristiqueProduitLogement.estCaracteristiqueProduitLogement(entitySet) );
		
	}
	
		//Cas anormal
		@Test
		void testEstCaracteristiqueProduitLogementObligatoireValide1() {
			
			
			//Initialisation des entrants
			
			Set<CaracteristiqueProduitLogement> entitySet = new HashSet<CaracteristiqueProduitLogement>();
			
			ProprieteProduitLogement proprieteProduitLogement = new ProprieteProduitLogement();
			proprieteProduitLogement.setEstObligatoire(true);
			
			
			ValeurCaracteristiqueProduitLogementInteger valeurInteger = new ValeurCaracteristiqueProduitLogementInteger();
			valeurInteger.setProprieteProduitLogement(proprieteProduitLogement);
			valeurInteger.setValeur(2);
			entitySet.add(valeurInteger);
			
			ValeurCaracteristiqueProduitLogementBoolean valeurBoolean = new ValeurCaracteristiqueProduitLogementBoolean();
			valeurBoolean.setProprieteProduitLogement(proprieteProduitLogement);
			valeurBoolean.setValeur(true);
			entitySet.add(valeurBoolean);
			
			
			ValeurCaracteristiqueProduitLogementLong valeurLong = new ValeurCaracteristiqueProduitLogementLong();
			valeurLong.setProprieteProduitLogement(proprieteProduitLogement);
			valeurLong.setValeur(null);
			entitySet.add( valeurLong);
			
			ValeurCaracteristiqueProduitLogementDouble valeurDouble = new ValeurCaracteristiqueProduitLogementDouble();
			valeurDouble.setProprieteProduitLogement(proprieteProduitLogement);
			valeurDouble.setValeur(2D);
			entitySet.add(valeurDouble);
			
			
			//Execution et Verification des sortants
			
			assertEquals(false , caracteristiqueProduitLogement.estCaracteristiqueProduitLogement(entitySet) );
			
			
		}
		
		
				//Cas anormal
				@Test
				void testEstCaracteristiqueProduitLogementObligatoireValide3() {
					
					
					//Initialisation des entrants
					
					Set<CaracteristiqueProduitLogement> entitySet = new HashSet<CaracteristiqueProduitLogement>();
					
					ProprieteProduitLogement proprieteProduitLogement = new ProprieteProduitLogement();
					proprieteProduitLogement.setEstObligatoire(true);
					
					
					ValeurCaracteristiqueProduitLogementInteger valeurInteger = new ValeurCaracteristiqueProduitLogementInteger();
					//valeurInteger.setProprieteProduitLogement(proprieteProduitLogement);
					valeurInteger.setValeur(2);
					entitySet.add(valeurInteger);
					
					ValeurCaracteristiqueProduitLogementBoolean valeurBoolean = new ValeurCaracteristiqueProduitLogementBoolean();
					//valeurBoolean.setProprieteProduitLogement(proprieteProduitLogement);
					valeurBoolean.setValeur(true);
					entitySet.add(valeurBoolean);
					
					
					ValeurCaracteristiqueProduitLogementLong valeurLong = new ValeurCaracteristiqueProduitLogementLong();
					//valeurLong.setProprieteProduitLogement(proprieteProduitLogement);
					valeurLong.setValeur(null);
					entitySet.add( valeurLong);
					
					ValeurCaracteristiqueProduitLogementDouble valeurDouble = new ValeurCaracteristiqueProduitLogementDouble();
				//	valeurDouble.setProprieteProduitLogement(proprieteProduitLogement);
					valeurDouble.setValeur(2D);
					entitySet.add(valeurDouble);
					
					
					//Execution et Verification des sortants
					
					assertEquals(true , caracteristiqueProduitLogement.estCaracteristiqueProduitLogement(entitySet) );
					
					
				}
				
		

}*/
