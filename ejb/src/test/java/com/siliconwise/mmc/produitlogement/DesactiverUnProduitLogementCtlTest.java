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
import com.siliconwise.mmc.produitlogement.caracteristique.CaracterisitqueProduitLogementDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogementDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.CreerModifierCaracteristiqueProduitLogementCtl;
import com.siliconwise.mmc.produitlogement.caracteristique.CreerModifierCaracteristiqueProduitLogementCtlInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementBoolean;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementBooleanDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementBooleanDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDate;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateTimeDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateTimeDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDocument;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDocumentDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDocumentDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDouble;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDoubleDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDoubleDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementFloat;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementFloatDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementFloatDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementInteger;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementIntegerDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementIntegerDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementLong;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementLongDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementLongDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementReference;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementReferenceDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementReferenceDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementString;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementStringDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementStringDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTexte;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTexteDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTexteDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTimeDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTimeDAOInterface;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVille;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVilleDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVilleDAOInterface;
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
public class DesactiverUnProduitLogementCtlTest {
	
	
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
						ValeurCaracteristiqueProduitLogementIntegerDAOInterface.class,
						ValeurCaracteristiqueProduitLogementIntegerDAO.class,
						ValeurCaracteristiqueProduitLogementDocument.class,
						ValeurCaracteristiqueProduitLogementDocumentDAOInterface.class,
						ValeurCaracteristiqueProduitLogementDocumentDAO.class,
						ValeurCaracteristiqueProduitLogementBoolean.class,
						ValeurCaracteristiqueProduitLogementBooleanDAOInterface.class,
						ValeurCaracteristiqueProduitLogementBooleanDAO.class,
						ValeurCaracteristiqueProduitLogementDate.class , 
						ValeurCaracteristiqueProduitLogementDateDAOInterface.class, 
						ValeurCaracteristiqueProduitLogementDateDAO.class, 
						ValeurCaracteristiqueProduitLogementDateTime.class,
						ValeurCaracteristiqueProduitLogementDateTimeDAOInterface.class,
						ValeurCaracteristiqueProduitLogementDateTimeDAO.class,
						ValeurCaracteristiqueProduitLogementString.class,
						ValeurCaracteristiqueProduitLogementStringDAOInterface.class,
						ValeurCaracteristiqueProduitLogementStringDAO.class,
						ValeurCaracteristiqueProduitLogementTexte.class,
						ValeurCaracteristiqueProduitLogementTexteDAOInterface.class,
						ValeurCaracteristiqueProduitLogementTexteDAO.class,
						ValeurCaracteristiqueProduitLogementTime.class,
						ValeurCaracteristiqueProduitLogementTimeDAOInterface.class,
						ValeurCaracteristiqueProduitLogementTimeDAO.class,
						ValeurCaracteristiqueProduitLogementLong.class,
						ValeurCaracteristiqueProduitLogementLongDAOInterface.class,
						ValeurCaracteristiqueProduitLogementLongDAO.class,
						ValeurCaracteristiqueProduitLogementFloat.class,
						ValeurCaracteristiqueProduitLogementFloatDAOInterface.class,
						ValeurCaracteristiqueProduitLogementFloatDAO.class,
						ValeurCaracteristiqueProduitLogementDouble.class,
						ValeurCaracteristiqueProduitLogementDoubleDAOInterface.class,
						ValeurCaracteristiqueProduitLogementDoubleDAO.class,
						ValeurCaracteristiqueProduitLogementReference.class,
						ValeurCaracteristiqueProduitLogementReferenceDAOInterface.class,
						ValeurCaracteristiqueProduitLogementReferenceDAO.class,
						ValeurCaracteristiqueProduitLogementVilleDAO.class,
						ValeurCaracteristiqueProduitLogementVille.class,
						ValeurCaracteristiqueProduitLogementVilleDAOInterface.class,
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
						ActiverDesactiverUnProduitLogementCtlInterface.class,
						ActiverDesactiverUnProduitLogementCtl.class,
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class,
						CreerModifierCaracteristiqueProduitLogementCtlInterface.class,
						CreerModifierCaracteristiqueProduitLogementCtl.class,
						CaracterisitqueProduitLogementDAOInterface.class,
						CaracteristiqueProduitLogementDAO.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		private static transient Logger logger = LoggerFactory.getLogger(DesactiverUnProduitLogementCtlTest.class) ;
		
		Locale locale;
	
		@Inject
		ActiverDesactiverUnProduitLogementCtlInterface activerDesactiverUnProduitLogementCtl ;
		*/
	
		/**
		 * Tests portant sur les cas normaux
		 * 
		 */
		
		
		/**
		 * 
		 * Désactivation d'un produit logement activé
		 */

		/*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testDesactiverUnProduitLogement() {

			//Initialisation des entrants
			
			ProduitLogement produitLogement = new ProduitLogement();
			
			produitLogement.setId("produit01");
			produitLogement.setCode("Senateur");
			produitLogement.setDesignation(" Villa Duplex 4 Pièces");
			produitLogement.setVersion(0);
			produitLogement.setEstValide(true);
			produitLogement.setEstActive(false);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme01");
		    programmeImmobilier.setCode("CCD");
		    
		    produitLogement.setProgrammeImmobilier(programmeImmobilier);
		    
		   
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des résultats
			ProduitLogement rtn = activerDesactiverUnProduitLogementCtl
										.desactiverUnProduitLogement(
											produitLogement, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			assertEquals("produit01" , rtn.getId());
			
			assertEquals(false , rtn.getEstActive());
			
		}
		
		*/
		
		
		/**
		 * 
		 * Tests cas anormaux
		 * 
		 */
		
	
		/**
		 * Desactivation d'un produit logement avec
		 * la propriété estActive à true
		 */

		/*
		@Test
		public  void testActiverUnProduitLogementAvecEstActiveAtrue() {

			//Initialisation des entrants
			
			ProduitLogement produitLogement = new ProduitLogement();
			
			produitLogement.setId("produit01");
			produitLogement.setCode("Senateur");
			produitLogement.setDesignation(" Villa Duplex 4 Pièces");
			produitLogement.setVersion(0);
			produitLogement.setEstValide(true);
			produitLogement.setEstActive(true);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme01");
		    programmeImmobilier.setCode("CCD");
		    
		    produitLogement.setProgrammeImmobilier(programmeImmobilier);
		    
		   
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des résultats
			ProduitLogement rtn = activerDesactiverUnProduitLogementCtl
										.activerUnProduitLogement(
											produitLogement, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			
			assertEquals(null, rtn);
			
			//assertEquals("produit01" , rtn.getId());
			//assertEquals(true , rtn.getEstActive());
			
		}
		*/
				
			
				/**
				 * 
				 * Desactivation d'un produit logement 
				 * Avec la propriété Id sans la version
				 * 
				 */

/*
				@Test
				public  void testActiverUnProduitLogementAvecIdEtSansVersion() {

					//Initialisation des entrants
					
					ProduitLogement produitLogement = new ProduitLogement();
					
					produitLogement.setId("produit01");
					produitLogement.setCode("Senateur");
					produitLogement.setDesignation(" Villa Duplex 4 Pièces");
					//produitLogement.setVersion(0);
					produitLogement.setEstValide(true);
					produitLogement.setEstActive(false);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				    programmeImmobilier.setCode("CCD");
				    
				    produitLogement.setProgrammeImmobilier(programmeImmobilier);
				    
				   
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des résultats
					ProduitLogement rtn = activerDesactiverUnProduitLogementCtl
												.activerUnProduitLogement(
													produitLogement, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					assertEquals(null , rtn);
					
				}
				*/
				
				
				/**
				 * 
				 * Desactivation d'un produit logement 
				 * Sans la l'identité domaniale
				 * 
				 */

/*
				@Test
				public  void testActiverUnProduitLogementAvecIdEtSansIdentiteDomaniale() {

					
					//Initialisation des entrants
					
					ProduitLogement produitLogement = new ProduitLogement();
					
					produitLogement.setId("produit01");
					//produitLogement.setCode("Senateur");
					produitLogement.setDesignation(" Villa Duplex 4 Pièces");
					produitLogement.setVersion(0);
					produitLogement.setEstValide(true);
					produitLogement.setEstActive(false);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				    programmeImmobilier.setCode("CCD");
				    
				    produitLogement.setProgrammeImmobilier(programmeImmobilier);
				    
				   
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des résultats
					
					ProduitLogement rtn = activerDesactiverUnProduitLogementCtl
												.activerUnProduitLogement(
													produitLogement, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					assertEquals(null , rtn);
					
				}

*/

				/**
				 * 
				 * Desactivation d'un produit logement sans un programme immobilier
				 * 
				 */

/*
				@Test
				public  void testActiverUnProduitLogementSansProgrammeImmobilier() {

					//Initialisation des entrants
					
					ProduitLogement produitLogement = new ProduitLogement();
					
					produitLogement.setId("produit01");
					produitLogement.setCode("Senateur");
					produitLogement.setDesignation(" Villa Duplex 4 Pièces");
					produitLogement.setVersion(0);
					produitLogement.setEstValide(true);
					produitLogement.setEstActive(false);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				    programmeImmobilier.setCode("CCD");
				    
				    //produitLogement.setProgrammeImmobilier(programmeImmobilier);
				    
				   
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des résultats
					ProduitLogement rtn = activerDesactiverUnProduitLogementCtl
												.activerUnProduitLogement(
													produitLogement, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					assertEquals(null , rtn);
					
				}*/
				
				
				
				/**
				 * 
				 * Desactivation d'un produit logement
				 * Sans l'indetité domaniale du programme immobilier auquel est
				 * rattaché le produit logement
				 * 
				 */

/*
				@Test
				public  void testActiverUnProduitLogementSansCodeProgrammeImmobilier() {

					//Initialisation des entrants
					
					ProduitLogement produitLogement = new ProduitLogement();
					
					produitLogement.setId("produit01");
					produitLogement.setCode("Senateur");
					produitLogement.setDesignation(" Villa Duplex 4 Pièces");
					produitLogement.setVersion(0);
					produitLogement.setEstValide(true);
					produitLogement.setEstActive(false);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				   // programmeImmobilier.setCode("CCD");
				    
				    produitLogement.setProgrammeImmobilier(programmeImmobilier);
				    
				   
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des résultats
					ProduitLogement rtn = activerDesactiverUnProduitLogementCtl
												.activerUnProduitLogement(
													produitLogement, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					assertEquals(null , rtn);
					
				}
}		*/

				
				
				

