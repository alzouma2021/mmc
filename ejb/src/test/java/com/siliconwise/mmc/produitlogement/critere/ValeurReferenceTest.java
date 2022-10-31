package com.siliconwise.mmc.produitlogement.critere;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.siliconwise.common.reference.Reference;
import com.siliconwise.common.reference.ReferenceFamille;

class ValeurReferenceTest {

	ValeurReference valeurReference ;
	
	@BeforeEach
	void initialisation() {
		
		//instanciation de la classe ValeurInteger
		valeurReference = new ValeurReference();
	}
	
		//Test des references 
		//Test pour recuperation des valeurs de la reference à partir de la methode from
		@SuppressWarnings({ "static-access", "unchecked" })
		@Test
		void testDeRecuperationDesReferences() {
			
			
			/**
			 * Modification de la methode test par Alzouma : Date 24/06/21
			 * 
			 */
			
			//Initialisation des valeurs entrantes
			/*
			Reference reference =  new Reference();
			reference.setId("reference1");
			reference.setDesignation("Villa");
			reference.setDescription("Villa 4 piéces");
			*/
			
		
			ArrayList<String> val = new ArrayList<String>();
			val.add("reference1");
			
			// val.add("test2");
			// val.add("test3");
			
			Valeur<Object> valeur = new Valeur<Object>();
			valeur.setValeurTexte(val);
					
			//Exection et Verification 
			/*
			ArrayList<Reference> reslt = new ArrayList<Reference>();
			
			Reference reference1 =  new Reference();
			reference1.setId("reference1");
			reference1.setDesignation("Villa");
			reference1.setDescription("Villa 4 piéces");
			
			reslt.add(reference1);
			*/
			
			//assertEquals(reslt.get(0).getDesignation() ,valeurReference.from(valeur).getValeur().get(0).getId());
			assertEquals("reference1" ,valeurReference.from(valeur).getValeur().get(0).getId());
		}

		
				
				//Test pour recuperation des valeurs de la reference à partir de la methode from
				@SuppressWarnings({ "static-access", "unchecked" })
				@Test
				void testReferenceNull() {
					
					//Initialisation des valeurs entrantes
					@SuppressWarnings("rawtypes")
					ArrayList val = new ArrayList<>();
				    val.add(null);
					
					Valeur<Object> valeur = new Valeur<Object>();
				//	valeur.setValeurReference(val);
							
					//Execution et Verification
					assertThrows(NullPointerException.class,()->valeurReference.from(valeur).getValeur());
					
				}
				
/*
				//Test à partir des valeurs de type non reference
				@SuppressWarnings({ "static-access", "unchecked" })
				@Test
				void testdeTypeNonReference() {
					
					//Initialisation des valeurs entrantes
					ReferenceFamille reference =  new ReferenceFamille();
					reference.setId("reference1");
					reference.setDesignation("Villa");
					reference.setDescription("Villa 4 piéces");
					
					@SuppressWarnings("rawtypes")
					ArrayList val = new ArrayList<>();
				    val.add(reference);
					
					Valeur<Object> valeur = new Valeur<Object>();
					valeur.setValeurReference(val);
							
					//Exection et Verification
					assertThrows(ClassCastException.class,()->valeurReference.from(valeur).getValeur());
				}*/
}
