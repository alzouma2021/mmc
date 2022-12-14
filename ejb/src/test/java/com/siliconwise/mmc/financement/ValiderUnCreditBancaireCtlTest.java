package com.siliconwise.mmc.financement;


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
import com.siliconwise.common.config.file.AppConfigUtil;
import com.siliconwise.common.document.Document;
import com.siliconwise.common.document.DocumentCtl;
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.common.document.DocumentDAO;
import com.siliconwise.common.document.DocumentDAOInterface;
import com.siliconwise.common.document.DocumentFormat;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.oldhistorique.Historique;
import com.siliconwise.common.event.oldhistorique.HistoriqueEventCallback;
import com.siliconwise.common.event.oldhistorique.HistoriserEventPayload;
import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
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
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.produitlogement.FamilleProprieteProduitLogement;
import com.siliconwise.mmc.produitlogement.OperateurCritere;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProduitLogementDAO;
import com.siliconwise.mmc.produitlogement.ProduitLogementDAOInterface;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocument;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentCtl;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentCtlInterface;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentDAO;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentDAOInterface;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogement;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogementDAO;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogementDAOInterface;
import com.siliconwise.mmc.produitlogement.RechercherProduitLogementCtl;
import com.siliconwise.mmc.produitlogement.RechercherProduitLogementCtlInterface;
import com.siliconwise.mmc.produitlogement.ValiderUnProduitLogementCtl;
import com.siliconwise.mmc.produitlogement.ValiderUnProduitLogementCtlInterface;
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
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocument;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentCtl;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentCtlInterface;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentDAO;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentDAOInterface;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierTransfert;
import com.siliconwise.mmc.produitlogement.critere.ValeurLong;
import com.siliconwise.mmc.produitlogement.critere.ValeurReference;
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class ValiderUnCreditBancaireCtlTest {
	
	
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
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class,
						ValiderUnCreditBancaireCtlInterface.class,
						ValiderUnCreditBancaireCtl.class,
						ValiderModeFinancementCtl.class,
						ValiderModeFinancementCtlInterface.class,
						TemperamentDAOInterface.class,
						TemperamentDAO.class,
						Temperament.class,
						TiersCollecteur.class,
						TiersCollecteurDAOInterface.class,
						TiersCollecteurDAO.class,
						EfiDAOInterface.class,
						EfiDAO.class,
						EfiCtlInterface.class,
						EfiCtl.class,
						DocumentCtlInterface.class,
						Tiers.class,
						DocumentCtl.class,
						DocumentDAO.class,
						DocumentDAOInterface.class,
						ProduitLogementDocument.class,
						ProduitLogementDocumentDAOInterface.class,
						ProduitLogementDocumentCtlInterface.class,
						ProduitLogementDocumentCtl.class,
						ProduitLogementDocumentDAO.class,
						ProgrammeImmobilierDocument.class,
						ProgrammeImmobilierDocumentDAOInterface.class,
						ProgrammeImmobilierDocumentCtlInterface.class,
						ProgrammeImmobilierDocumentCtl.class,
						ProgrammeImmobilierDocumentDAO.class,
						SupprimerModeFinancementCtlInterface.class,
						SupprimerModeFinancementCtl.class,
						RechercherModeFinancementCtlInterface.class,
						RechercherModeFinancementCtl.class,
						EmailServiceException.class,
						EmailService.class,
						AppConfigUtil.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	
		
		Locale locale;
	
		@Inject
		ValiderUnCreditBancaireCtlInterface ValiderUnCreditBancaireCtl ;
		
	
	/**
	 * Tests portant sur les cas normaux
	 * 
	 */
		
		/**
		 * 
		 * Test de validation du mode de financement cr??dit bancaire: Cas Normal
		 * 
		 */

/*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testValiderUnModeFinancementCasNormal() {

			
			//Initialisation des entrants
			
			ModeFinancement entity = new ModeFinancement();
			
			entity.setId("modefinancement2");
			entity.setDesignation("Credit Bancaire");
			entity.setVersion(0);
			entity.setEstValide(true);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme01");
		    programmeImmobilier.setCode("CCD");
		    
		    entity.setProgrammeImmobilier(programmeImmobilier);
		    
		    TypeFinancement financement = new TypeFinancement();
		    financement.setId("ref.element.valeur.creditbancaire");
		    
		    entity.setTypeFinancement(financement);
		    
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des r??sultats
			
			ModeFinancement rtn = ValiderUnCreditBancaireCtl
										.validerUnCreditBancaire(
											entity, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			assertEquals("modefinancement2" , rtn.getId());
			assertEquals(true , rtn.getEstValide());
			
		}
		
		*/
		
		/**
		 * Tests de validation du mode de financement
		 * cr??dit bancaire portant sur les cas anormaux
		 * 
		 */		
		
		
		
		
		/**
		 * 
		 * Test de validation du mode de financement credit bancaire
		 * 
		 * Avec la propri??t?? estValide ?? null 
		 * 
		 */
/*
		@Test
		public  void testValiderUnModeFinancementAvecProprieteEstValideANull() {

		   
			
			//Initialisation des entrants
			
			ModeFinancement entity = new ModeFinancement();
			
			entity.setId("modefinancement2");
			entity.setDesignation("Credit Bancaire");
			entity.setVersion(0);
			//entity.setEstValide(true);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme01");
		    programmeImmobilier.setCode("CCD");
		    
		    entity.setProgrammeImmobilier(programmeImmobilier);
		    
		    TypeFinancement financement = new TypeFinancement();
		    financement.setId("ref.element.valeur.creditbancaire");
		    
		    entity.setTypeFinancement(financement);
		    
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des r??sultats
			ModeFinancement rtn = ValiderUnCreditBancaireCtl
										.validerUnCreditBancaire(
											entity, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			
			 assertEquals(null , rtn);
			//assertEquals("modefinancement2" , rtn.getId());
			//assertEquals(true , rtn.getEstValide());
			 
			
		}
	*/	
		
		/**
		 * 
		 * Test de validation du mode de financement credit bancaire
		 * 
		 * Avec la propri??t?? estValide ?? false 
		 * 
		 */

/*
		@Test
		public  void testValiderUnModeFinancementAvecProprieteEstValideAFalse() {

		   
			
			//Initialisation des entrants
			
			ModeFinancement entity = new ModeFinancement();
			
			entity.setId("modefinancement2");
			entity.setDesignation("Credit Bancaire");
			entity.setVersion(0);
			entity.setEstValide(false);
			
			
		    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
		    
		    programmeImmobilier.setId("programme01");
		    programmeImmobilier.setCode("CCD");
		    
		    entity.setProgrammeImmobilier(programmeImmobilier);
		    
		    TypeFinancement financement = new TypeFinancement();
		    financement.setId("ref.element.valeur.creditbancaire");
		    
		    entity.setTypeFinancement(financement);
		    
		    User loggedInUser = new User();
			   
			loggedInUser.setId("user1");
			loggedInUser.setNom("alzouma");
			loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    //Execution de la methode et verification des r??sultats
			
			ModeFinancement rtn = ValiderUnCreditBancaireCtl
										.validerUnCreditBancaire(
											entity, 
											true, 
											null, 
											true, 
											locale, loggedInUser, msgList);
		    
			
			assertEquals(null , rtn);
	
		}
	*/	
		
		
				/**
				 * 
				 * Test de validation du mode de financement credit bancaire
				 * 
				 * Sans Programme immobilier
				 * 
				 */
/*
				@Test
				public  void testValiderUnModeFinancementSansProgrammeImmobilier() {

		
					
					//Initialisation des entrants
					
					ModeFinancement entity = new ModeFinancement();
					
					entity.setId("modefinancement2");
					entity.setDesignation("Credit Bancaire");
					entity.setVersion(0);
					entity.setEstValide(true);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				    programmeImmobilier.setCode("CCD");
				    
				   // entity.setProgrammeImmobilier(programmeImmobilier);
				    
				    TypeFinancement financement = new TypeFinancement();
				    financement.setId("ref.element.valeur.creditbancaire");
				    
				    entity.setTypeFinancement(financement);
				    
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des r??sultats
					ModeFinancement rtn = ValiderUnCreditBancaireCtl
												.validerUnCreditBancaire(
													entity, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					 assertEquals(null , rtn);
					
					
				}
		*/	
				
				/**
				 * 
				 * Test de validation du mode de financement credit bancaire
				 * 
				 * Sans l'identit?? domaniale : Propri??t?? Code
				 * 
				 */
/*
				@Test
				public  void testValiderUnModeFinancementSansProgrammeImmobilierCode() {

		
					
					//Initialisation des entrants
					
					ModeFinancement entity = new ModeFinancement();
					
					entity.setId("modefinancement2");
					entity.setDesignation("Credit Bancaire");
					entity.setVersion(0);
					entity.setEstValide(true);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				    //programmeImmobilier.setCode("CCD");
				    
				    entity.setProgrammeImmobilier(programmeImmobilier);
				    
				    TypeFinancement financement = new TypeFinancement();
				    financement.setId("ref.element.valeur.creditbancaire");
				    
				    entity.setTypeFinancement(financement);
				    
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des r??sultats
					
					ModeFinancement rtn = ValiderUnCreditBancaireCtl
												.validerUnCreditBancaire(
													entity, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					 assertEquals(null , rtn);
					
					
				}
*/
				
				
				/**
				 * 
				 * Test de validation du mode de financement credit bancaire
				 * 
				 * Avec l'Id et sans version
				 * 
				 */

/*
				@Test
				public  void testValiderUnModeFinancementSansVersion() {

		
					
					//Initialisation des entrants
					
					ModeFinancement entity = new ModeFinancement();
					
					entity.setId("modefinancement2");
					entity.setDesignation("Credit Bancaire");
					//entity.setVersion(0);
					entity.setEstValide(true);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				    programmeImmobilier.setCode("CCD");
				    
				    entity.setProgrammeImmobilier(programmeImmobilier);
				    
				    TypeFinancement financement = new TypeFinancement();
				    financement.setId("ref.element.valeur.creditbancaire");
				    
				    entity.setTypeFinancement(financement);
				    
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des r??sultats
					
					ModeFinancement rtn = ValiderUnCreditBancaireCtl
												.validerUnCreditBancaire(
													entity, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					 assertEquals(null , rtn);
					
					
				}


*/
				
				/**
				 * 
				 * Test de validation d'un credit bancaire
				 * 
				 * Sans le type du financement
				 * 
				 */

/*
				@Test
				public  void testValiderUnModeFinancementSansTypeFinancement() {

					
					//Initialisation des entrants
					
					ModeFinancement entity = new ModeFinancement();
					
					entity.setId("modefinancement2");
					entity.setDesignation("Credit Bancaire");
					entity.setVersion(0);
					entity.setEstValide(true);
					
					
				    ProgrammeImmobilier   programmeImmobilier = new ProgrammeImmobilier();
				    
				    programmeImmobilier.setId("programme01");
				    programmeImmobilier.setCode("CCD");
				    
				    entity.setProgrammeImmobilier(programmeImmobilier);
				    
				    TypeFinancement financement = new TypeFinancement();
				    financement.setId("ref.element.valeur.creditbancaire");
				    
				    //entity.setTypeFinancement(financement);
				    
				    User loggedInUser = new User();
					   
					loggedInUser.setId("user1");
					loggedInUser.setNom("alzouma");
					loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				    
				    //Execution de la methode et verification des r??sultats
					
					ModeFinancement rtn = ValiderUnCreditBancaireCtl
												.validerUnCreditBancaire(
													entity, 
													true, 
													null, 
													true, 
													locale, loggedInUser, msgList);
				    
					
					 assertEquals(null , rtn);
					 
					
				}
				
	
}*/

				
				
				

