package com.siliconwise.mmc.produitlogement;


import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.AppUtil;
import com.siliconwise.common.Pays;
import com.siliconwise.common.Ville;
import com.siliconwise.common.VilleDAO;
import com.siliconwise.common.VilleDAOInterface;
import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentFormat;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.oldhistorique.Historique;
import com.siliconwise.common.event.oldhistorique.HistoriqueEventCallback;
import com.siliconwise.common.event.oldhistorique.HistoriserEventPayload;
import com.siliconwise.common.reference.CritereRechercheReference;
import com.siliconwise.common.reference.IReference;
import com.siliconwise.common.reference.IReferenceTr;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.common.reference.ReferenceCtl;
import com.siliconwise.common.reference.ReferenceCtlInterface;
import com.siliconwise.common.reference.ReferenceDAO;
import com.siliconwise.common.reference.ReferenceDAOInterface;
import com.siliconwise.common.reference.ReferenceFamille;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;
import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.Compte;
import com.siliconwise.mmc.modefinancement.CreditBancaire;
import com.siliconwise.mmc.modefinancement.CreditBancaireDAO;
import com.siliconwise.mmc.modefinancement.CreditBancaireDAOInterface;
import com.siliconwise.mmc.modefinancement.CreerModifierModeFinancementCtl;
import com.siliconwise.mmc.modefinancement.CreerModifierModeFinancementCtlInterface;
import com.siliconwise.mmc.modefinancement.ModeFinancement;
import com.siliconwise.mmc.modefinancement.ModeFinancementDAO;
import com.siliconwise.mmc.modefinancement.ModeFinancementDAOInterface;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituationDAO;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituationDAOInterface;
import com.siliconwise.mmc.modefinancement.TypeFinancement;
import com.siliconwise.mmc.modefinancement.ValiderModeFinancementCtl;
import com.siliconwise.mmc.modefinancement.ValiderModeFinancementCtlInterface;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.organisation.efi.EFi;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementBoolean;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDate;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDocument;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDouble;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementFloat;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementInteger;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementLong;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementReference;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementString;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTexte;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVille;
import com.siliconwise.mmc.produitlogement.critere.CritereRechercheProduitLogement;
import com.siliconwise.mmc.produitlogement.critere.Valeur;

import com.siliconwise.mmc.produitlogement.critere.ValeurBoolean;
import com.siliconwise.mmc.produitlogement.critere.ValeurDate;
import com.siliconwise.mmc.produitlogement.critere.ValeurFloat;

import com.siliconwise.mmc.produitlogement.critere.ValeurDateTime;
import com.siliconwise.mmc.produitlogement.critere.ValeurDouble;

import com.siliconwise.mmc.produitlogement.critere.ValeurInteger;
import com.siliconwise.mmc.produitlogement.critere.ValeurString;
import com.siliconwise.mmc.programmeimmobilier.CreerModifierUnProgrammeImmobilierCtl;
import com.siliconwise.mmc.programmeimmobilier.CreerModifierUnProgrammeImmobilierCtlInterface;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDAO;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDAOInterface;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierTransfert;
import com.siliconwise.mmc.produitlogement.critere.ValeurLong;
import com.siliconwise.mmc.produitlogement.critere.ValeurReference;
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class ValiderUnProduitLogementCtlTest {
	
	
	@Deployment
	public static JavaArchive createDeployment(){
		 
				
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(
						RechercherProduitLogementCtl.class,
						RechercherProduitLogementCtlInterface.class,
						ProduitLogementDAOInterface.class,
						ProduitLogement.class,
						IEntityStringkey.class,
						ProduitLogementDAO.class,
						CritereRechercheProduitLogement.class,
						OperateurCritere.class,
						ProgrammeImmobilier.class,
						Promoteur.class,
						Ville.class,
						Pays.class,
						CaracteristiqueProduitLogement.class,
						ProprieteProduitLogement.class,
						Reference.class,
						IReference.class,
						IReferenceTr.class,
						ReferenceFamille.class,
						FamilleProprieteProduitLogement.class,
						Document.class,
						UUIDGeneratorEntityListener.class,
						EntityUtil.class,
						NonLocalizedStatusMessage.class,
						StatusMessageType.class,
						Valeur.class,
						ValeurInteger.class,
						ValeurBoolean.class,
						ValeurDate.class,
						ValeurString.class,
						ValeurDateTime.class,
						ValeurLong.class,
						ValeurDouble.class,
						ValeurFloat.class,
						ValeurCaracteristiqueProduitLogementInteger.class,
						ValeurCaracteristiqueProduitLogementBoolean.class,
						ValeurCaracteristiqueProduitLogementDate.class , 
						ValeurCaracteristiqueProduitLogementDateTime.class,
						ValeurCaracteristiqueProduitLogementString.class,
						ValeurCaracteristiqueProduitLogementTexte.class,
						ValeurCaracteristiqueProduitLogementTime.class,
						ValeurCaracteristiqueProduitLogementLong.class,
						ValeurCaracteristiqueProduitLogementFloat.class,
						ValeurCaracteristiqueProduitLogementDouble.class,
						ValeurCaracteristiqueProduitLogementDocument.class,
						ValeurCaracteristiqueProduitLogementVille.class,
						ValeurReference.class,
						ValeurCaracteristiqueProduitLogementReference.class,
						Document.class,
						SessionBag.class,
						AppMessageKeys.class,
						MessageTranslationUtil.class,
						SessionUtil.class,
						AppUtil.class,
						ProprieteProduitLogementDAOInterface.class,
						ProprieteProduitLogementDAO.class,
						ReferenceCtlInterface.class,
						ReferenceDAO.class,
						ReferenceDAOInterface.class,
						ReferenceCtl.class,
						ReferenceCtlInterface.class,
						CritereRechercheReference.class,
						IEntityMsgVarMap.class,
						User.class,
						ModeFinancement.class,
						CreditBancaire.class,
						TypeFinancement.class,
						PallierComptantSurSituation.class,
						Compte.class,
						EFi.class,
						DocumentFormat.class,
						HistoriserEventPayload.class,
						Historique.class,
						HistoriqueEventCallback.class,
						ProgrammeImmobilierDAOInterface.class,
						ProgrammeImmobilierDAO.class,
						CreerModifierUnProgrammeImmobilierCtlInterface.class,
						CreerModifierUnProgrammeImmobilierCtl.class,
						EntityUtil.class,
						CreerModifierModeFinancementCtlInterface.class,
						CreerModifierModeFinancementCtl.class,
						ModeFinancementDAOInterface.class,
						ModeFinancementDAO.class,
						CreditBancaireDAOInterface.class,
						CreditBancaireDAO.class,
						PallierComptantSurSituationDAOInterface.class,
						PallierComptantSurSituationDAO.class,
						ProgrammeImmobilierTransfert.class,
						VilleDAOInterface.class,
						VilleDAO.class,
						ValiderUnProduitLogementCtl.class,
						ValiderUnProduitLogementCtlInterface.class,
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		private static transient Logger logger = LoggerFactory.getLogger(ValiderUnProduitLogementCtlTest.class) ;
		
		Locale locale;
	
		@Inject
		ValiderUnProduitLogementCtlInterface validerUnProduitLogementCtl ;
		*/
	
	/**
	 * Tests de validation d'un produit logement  : les cas normaux
	 * 
	 */
		
		/**
		 * 
		 * Test de validation d'un produit logement avec
		 * 
		 * une caracteristique obligatoire renseignée
		 * 
		 */

		
		//Test de validation d'un produit logement normal 

        /*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testValiderUnProduitLogementAvecUneCaracteristiqueObligatoireRenseignee() {

		  
			
			//Initialisation des entrants
			
			ProduitLogement produitLogement = new ProduitLogement();
			
			produitLogement.setId("produit01");
			produitLogement.setCode("Senateur");
			produitLogement.setDesignation(" Villa Duplex 4 Pièces");
			produitLogement.setVersion(0);
			produitLogement.setEstValide(true);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme01");
		    produitLogement.setProgrammeImmobilier(programmeImmobilier);
		    
		    
		    Set<CaracteristiqueProduitLogement> entityList = new HashSet() ;
			
		    ValeurCaracteristiqueProduitLogementInteger vclInteger = new ValeurCaracteristiqueProduitLogementInteger();
		    
		    vclInteger.setId("caracteristique02");
		    vclInteger.setDesignation("caracteristique02");
		    vclInteger.setValeur(4);
		    
		    ProprieteProduitLogement propriete = new ProprieteProduitLogement();
		    propriete.setId("propriete02");
		    
		    Reference reference = new Reference();
		    reference.setId("ref.element.typeValeur.integer");
		    
		    propriete.setType(reference);
		    propriete.setEstObligatoire(true);
		    
		    vclInteger.setProprieteProduitLogement(propriete);
		    
		    entityList.add(vclInteger);
		    
		    produitLogement.setCaracteristiqueProduitLogementList(entityList);
		    
		    
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des résultats
			ProduitLogement rtn = validerUnProduitLogementCtl
										.validerUnProduitLogement(
											produitLogement, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			assertEquals("produit01" , rtn.getId());
			assertEquals(true , rtn.getEstValide());
			
			
		}*/
		

				/**
				 * 
				 * Test de validation d'un produit logement
				 * Avec deux caracteristiques obligatoires renseignées
				 * 
				 */

/*
				@Test
				public  void testValiderUnProduitLogementAvecDeuxCaracteristiquesObligatoiresRenseignees() {

					
					//Initialisation des entrants
					
					ProduitLogement produitLogement = new ProduitLogement();
					
					produitLogement.setId("produit02");
					produitLogement.setCode("Reve");
					produitLogement.setDesignation("Appartement de luxe 4 Pièces");
					produitLogement.setVersion(0);
					produitLogement.setEstValide(true);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme02");
				    produitLogement.setProgrammeImmobilier(programmeImmobilier);
				    
				    
				    Set<CaracteristiqueProduitLogement> entityList = new HashSet() ;
					
				    //Caracteristique 1
				    ValeurCaracteristiqueProduitLogementInteger vclInteger = new ValeurCaracteristiqueProduitLogementInteger();
				    
				    vclInteger.setId("caracteristique02");
				    vclInteger.setDesignation("caracteristique02");
				    vclInteger.setValeur(4);
				    
				    ProprieteProduitLogement propriete = new ProprieteProduitLogement();
				    propriete.setId("propriete02");
				    
				    Reference reference = new Reference();
				    reference.setId("ref.element.typeValeur.integer");
				    
				    propriete.setType(reference);
				    propriete.setEstObligatoire(true);
				    
				    vclInteger.setProprieteProduitLogement(propriete);
				    
				    entityList.add(vclInteger);
				    
				    
				    //Caracteristique2
				    ValeurCaracteristiqueProduitLogementString vclString = new ValeurCaracteristiqueProduitLogementString();
				    
				    vclString.setId("caracteristique01");
				    vclString.setDesignation("caracteristique01");
				    vclString.setValeur("Villa");
				    
				    ProprieteProduitLogement propriete1 = new ProprieteProduitLogement();
				    propriete1.setId("propriete01");
				    
				    Reference reference1 = new Reference();
				    reference1.setId("ref.element.typeValeur.string");
				    
				    propriete1.setType(reference1);
				    propriete1.setEstObligatoire(true);
				    
				    vclString.setProprieteProduitLogement(propriete);
				    
				    entityList.add(vclString);
				    
				    produitLogement.setCaracteristiqueProduitLogementList(entityList);
				    
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des résultats
					ProduitLogement rtn = validerUnProduitLogementCtl
												.validerUnProduitLogement(
													produitLogement, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					assertEquals("produit02" , rtn.getId());
					assertEquals(true , rtn.getEstValide());
					
				}
	*/

	
		/**
		 * Test de validation d'un produit logement avec deux caracteristiques:
		 * 
		 * Une obligatoire renseignée
		 * Une Optionnelle non renseignée
		 * 
		 */

         
/*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testValiderUnProduitLogementAvecDeuxCaracteristiquesUneObligatoireUneOptionnelle() {

			
			//Initialisation des entrants
			
			ProduitLogement produitLogement = new ProduitLogement();
			
			produitLogement.setId("produit02");
			produitLogement.setCode("Reve");
			produitLogement.setDesignation("Appartement de luxe 4 Pièces");
			produitLogement.setVersion(0);  
			produitLogement.setEstValide(true);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme02");
		    produitLogement.setProgrammeImmobilier(programmeImmobilier);
		    
		    
		    Set<CaracteristiqueProduitLogement> entityList = new HashSet() ;
			
		    //Caracteristique 1
		    
		    ValeurCaracteristiqueProduitLogementInteger vclInteger = new ValeurCaracteristiqueProduitLogementInteger();
		    
		    vclInteger.setId("caracteristique02");
		    vclInteger.setDesignation("caracteristique02");
		    vclInteger.setValeur(4);
		    
		    ProprieteProduitLogement propriete = new ProprieteProduitLogement();
		    propriete.setId("propriete02");
		    
		    Reference reference = new Reference();
		    reference.setId("ref.element.typeValeur.integer");
		    
		    propriete.setType(reference);
		    propriete.setEstObligatoire(true);
		    
		    vclInteger.setProprieteProduitLogement(propriete);
		    
		    entityList.add(vclInteger);
		    
		    
		    //Caracteristique2
		    
		    ValeurCaracteristiqueProduitLogementString vclString = new ValeurCaracteristiqueProduitLogementString();
		    
		    vclString.setId("caracteristique01");
		    vclString.setDesignation("caracteristique01");
		    vclString.setValeur("Villa");
		    
		    ProprieteProduitLogement propriete1 = new ProprieteProduitLogement();
		    propriete1.setId("propriete01");
		    
		    Reference reference1 = new Reference();
		    reference1.setId("ref.element.typeValeur.string");
		    
		    propriete1.setType(reference1);
		    propriete1.setEstObligatoire(true);
		    
		    vclString.setProprieteProduitLogement(propriete1);
		    
		    entityList.add(vclString);
		    
		    //Caracteristique3
		    
		    ValeurCaracteristiqueProduitLogementBoolean vclBoolean = new ValeurCaracteristiqueProduitLogementBoolean();
		    
		    vclBoolean.setId("caracteristique11");
		    vclBoolean.setDesignation("caracteristique11");
		    vclBoolean.setValeur(true);
		    
		    ProprieteProduitLogement propriete2 = new ProprieteProduitLogement();
		    propriete2.setId("propriete05");
		    
		    Reference reference2 = new Reference();
		    reference2.setId("ref.element.typeValeur.boolean");
		    
		    propriete2.setType(reference2);
		    propriete2.setEstObligatoire(false);
		    
		    vclBoolean.setProprieteProduitLogement(propriete2);
		    
		    entityList.add(vclBoolean);
		    
		    
		    produitLogement.setCaracteristiqueProduitLogementList(entityList);
		    
		    
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des résultats
			ProduitLogement rtn = validerUnProduitLogementCtl
										.validerUnProduitLogement(
											produitLogement, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			assertEquals("produit02" , rtn.getId());
			assertEquals(true , rtn.getEstValide());
			
		}*/
		
		
		
		/**
		 * 
		 * Validation d'un produit logement sans aucune caracteristique obligatoire
		 * 
		 * 
		 */
		
		/*
		@Test
		//@UsingDataSet("datasets/produitlogement.yml")
		public  void testValiderUnProduitLogementSansCaracteristiqueObligatoire() {

			
			//Initialisation des entrants
			
			ProduitLogement produitLogement = new ProduitLogement();
			
			produitLogement.setId("produit02");
			produitLogement.setCode("Reve");
			produitLogement.setDesignation("Appartement de luxe 4 Pièces");
			produitLogement.setVersion(0);
			produitLogement.setEstValide(true);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme02");
		    produitLogement.setProgrammeImmobilier(programmeImmobilier);
		    
		    
		    Set<CaracteristiqueProduitLogement> entityList = new HashSet() ;
			
		    //Caracteristique 1
		    
		    ValeurCaracteristiqueProduitLogementInteger vclInteger = new ValeurCaracteristiqueProduitLogementInteger();
		    
		    vclInteger.setId("caracteristique02");
		    vclInteger.setDesignation("caracteristique02");
		    vclInteger.setValeur(4);
		    
		    ProprieteProduitLogement propriete = new ProprieteProduitLogement();
		    propriete.setId("propriete02");
		    
		    Reference reference = new Reference();
		    reference.setId("ref.element.typeValeur.integer");
		    
		    propriete.setType(reference);
		    propriete.setEstObligatoire(false);
		    
		    vclInteger.setProprieteProduitLogement(propriete);
		    
		    entityList.add(vclInteger);
		    
		    
		    //Caracteristique2
		    
		    ValeurCaracteristiqueProduitLogementString vclString = new ValeurCaracteristiqueProduitLogementString();
		    
		    vclString.setId("caracteristique01");
		    vclString.setDesignation("caracteristique01");
		    vclString.setValeur("Villa");
		    
		    ProprieteProduitLogement propriete1 = new ProprieteProduitLogement();
		    propriete1.setId("propriete01");
		    
		    Reference reference1 = new Reference();
		    reference1.setId("ref.element.typeValeur.string");
		    
		    propriete1.setType(reference1);
		    propriete1.setEstObligatoire(false);
		    
		    vclString.setProprieteProduitLogement(propriete1);
		    
		    entityList.add(vclString);
		    
		    //Caracteristique3
		    
		    ValeurCaracteristiqueProduitLogementBoolean vclBoolean = new ValeurCaracteristiqueProduitLogementBoolean();
		    
		    vclBoolean.setId("caracteristique11");
		    vclBoolean.setDesignation("caracteristique11");
		    vclBoolean.setValeur(true);
		    
		    ProprieteProduitLogement propriete2 = new ProprieteProduitLogement();
		    propriete2.setId("propriete05");
		    
		    Reference reference2 = new Reference();
		    reference2.setId("ref.element.typeValeur.boolean");
		    
		    propriete2.setType(reference2);
		    propriete2.setEstObligatoire(false);
		    
		    vclBoolean.setProprieteProduitLogement(propriete2);
		    
		    entityList.add(vclBoolean);
		    
		    produitLogement.setCaracteristiqueProduitLogementList(entityList);
		    
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des résultats
			
			ProduitLogement rtn = validerUnProduitLogementCtl
										.validerUnProduitLogement(
											produitLogement, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			assertEquals("produit02" , rtn.getId());
			assertEquals(true , rtn.getEstValide());
			
			
		}
		
}*/

				
				
				

