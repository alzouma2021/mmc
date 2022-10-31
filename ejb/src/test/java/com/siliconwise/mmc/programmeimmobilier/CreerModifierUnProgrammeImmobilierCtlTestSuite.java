package com.siliconwise.mmc.programmeimmobilier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDate;
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
import com.siliconwise.common.config.file.AppConfigKeys;
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
import com.siliconwise.common.event.oldhistorique.HistoriqueDAO;
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
import com.siliconwise.mmc.modefinancement.Temperament;
import com.siliconwise.mmc.modefinancement.TemperamentDAO;
import com.siliconwise.mmc.modefinancement.TemperamentDAOInterface;
import com.siliconwise.mmc.modefinancement.Tiers;
import com.siliconwise.mmc.modefinancement.TiersCollecteur;
import com.siliconwise.mmc.modefinancement.TiersCollecteurDAO;
import com.siliconwise.mmc.modefinancement.TiersCollecteurDAOInterface;
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
import com.siliconwise.mmc.produitlogement.critere.CritereRechercheProduitLogement;
import com.siliconwise.mmc.produitlogement.critere.Valeur;
import com.siliconwise.mmc.produitlogement.critere.ValeurBoolean;
import com.siliconwise.mmc.produitlogement.critere.ValeurDate;
import com.siliconwise.mmc.produitlogement.critere.ValeurDateTime;
import com.siliconwise.mmc.produitlogement.critere.ValeurDouble;
import com.siliconwise.mmc.produitlogement.critere.ValeurFloat;
import com.siliconwise.mmc.produitlogement.critere.ValeurInteger;
import com.siliconwise.mmc.produitlogement.critere.ValeurLong;
import com.siliconwise.mmc.produitlogement.critere.ValeurReference;
import com.siliconwise.mmc.produitlogement.critere.ValeurString;
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class CreerModifierUnProgrammeImmobilierCtlTestSuite {

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
						TiersCollecteur.class,
						Temperament.class,
						TemperamentDAOInterface.class,
						TemperamentDAO.class,
						TiersCollecteurDAOInterface.class,
						TiersCollecteurDAO.class,
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class,
						Tiers.class,
						DocumentCtlInterface.class,
						DocumentCtl.class,
						DocumentDAOInterface.class,
						DocumentDAO.class,
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class,
						ProduitLogementDocument.class,
						ProduitLogementDocumentCtlInterface.class,
						ProduitLogementDocumentCtl.class,
						ProduitLogementDocumentDAOInterface.class,
						ProduitLogementDocumentDAO.class,
						ProgrammeImmobilierDocument.class,
						ProgrammeImmobilierDocumentCtl.class,
						ProgrammeImmobilierDocumentCtlInterface.class,
						ProgrammeImmobilierDocumentDAOInterface.class,
						ProgrammeImmobilierDocumentDAO.class,
						AppUtil.class,
						AppConfigUtil.class
						
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	CreerModifierUnProgrammeImmobilierCtlInterface creerModifierUnProgrammeImmobilierCtl ;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	
	//Test de creation d'un programme immobilier 
	/*
	@Test
	public void testCreerUnProgrammeImmobilierSansModeFinancements() {
		
		  //Iniatilisation des entrants
		   Boolean estCreation = true ;
		   
		   
		   ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
				   
		   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		   
		   programmeimmobilier.setDesignation("programme04") ;
		   programmeimmobilier.setDescription("Programme SIWISE") ;
		   programmeimmobilier.setCode("SIWISEUP");
		   
		   Promoteur promoteur = new Promoteur();
		   
		   promoteur.setId("promoteur01");
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   //Initialisation de la classe de transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   //Execution  du test et verification des sortants
		    
		   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList ,estCreation) ;
		  
		   assertEquals("SIWISEUP" , rtn.getCode());
		   assertEquals("programme04" , rtn.getDesignation());
		   
	}
	
	
	//Test de modification portant les informations generales 
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testModifierUnProgrammeImmobilierSansModeFinancements() {
		
		   //Iniatilisation des entrants
		
		   Boolean estCreation = false ;
		   
		   ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
		   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		
		   programmeimmobilier.setId("programme01") ;
		   programmeimmobilier.setDesignation("Emergence") ;
		   programmeimmobilier.setCode("CCD");
		   programmeimmobilier.setVersion(0);
		   
		   Promoteur promoteur = new Promoteur();
		   
		   promoteur.setId("promoteur01");
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   //Initilisation de la classe de transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   //Execution  du test et verification des sortants
		    
		   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		   assertEquals("programme01" , rtn.getId());
		   assertEquals("Emergence" , rtn.getDesignation());
		   
	}

    //Test de creatio d'un programme immobilier avec mode de financements
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnProgrammeImmobilierAvecModeFinancements() {
		
		  
		  //Iniatilisation des entrants
			
	       Boolean estCreation = true ;
	       
	       ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
	       ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		   
	       programmeimmobilier.setDesignation("programme05") ;
	       programmeimmobilier.setDescription("Programme SIWISETOP") ;
	       programmeimmobilier.setCode("SIWISETOP");
		   
		   Promoteur promoteur = new Promoteur();
		   
		   promoteur.setId("promoteur01");
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   	//Iniatilisation de la liste des modes de financement
		   
		   Set<ModeFinancement> modeFinancementList = new HashSet<ModeFinancement>();
		   
		   
		  // TypeFinancement typeFinancement = new TypeFinancement();
		   
		   		//Mode de financement de type Comptant
		   
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   modeFinancement1.setDesignation("Comptant");
		   modeFinancement1.setDescription("Paiement comptant");
		   
		   TypeFinancement typeFinancement1 = new TypeFinancement();
		   
		   typeFinancement1.setId("ref.element.valeur.comptant");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement1);
		   
	      // modeFinancementList.add(modeFinancement1);
		   
		   
		   		//Mode de financement de type Credit bancaire
		   
		   CreditBancaire creditBancaire = new CreditBancaire();
		   ModeFinancement modeFinancement2 = new ModeFinancement();
		   modeFinancement2.setDesignation("Credit Bancaire SGBCI");
		   modeFinancement2.setDescription("Paiement Credit Bancaire");
		   
		   TypeFinancement typeFinancement2 = new TypeFinancement();
		   typeFinancement2.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement2.setTypeFinancement(typeFinancement2);
		   
		   creditBancaire.setDuree(60);
		   creditBancaire.setTaux(6d);
		   
		   EFi sgbci = new EFi();
		   sgbci.setId("sgbci");
		   creditBancaire.setEfi(sgbci);
		   
		   modeFinancement2.setCreditBancaire(creditBancaire);
		   
		   modeFinancementList.add(modeFinancement2);
		   
		   
		   ModeFinancement modeFinancement3 = new ModeFinancement();
		   modeFinancement3.setDesignation("Credit Bancaire BICICI");
		   modeFinancement3.setDescription("Paiement Credit Bancaire");
		   
		   TypeFinancement typeFinancement3 = new TypeFinancement();
		   typeFinancement3.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement3.setTypeFinancement(typeFinancement3);
		   
		   creditBancaire.setDuree(72);
		   creditBancaire.setTaux(8d);
		   
		   EFi bicici = new EFi();
		   bicici.setId("bicici");
		   creditBancaire.setEfi(bicici); 
		   
		   modeFinancement3.setCreditBancaire(creditBancaire);
		   
		  // modeFinancementList.add(modeFinancement3);
		   
		   
		   		//Mode de financement de type Comptant sur situation
		   
		   ModeFinancement modeFinancement4 = new ModeFinancement();
		   modeFinancement4.setDesignation("Comptant sur situation");
		   modeFinancement4.setDescription("Paiement comptant sur situation");
		   
		   TypeFinancement typeFinancement4 = new TypeFinancement();
		   
		   typeFinancement4.setId("ref.element.valeur.comptantsursituation");
		   
		   modeFinancement4.setTypeFinancement(typeFinancement4);
		   
		   Set<PallierComptantSurSituation> pallierComptantSurSituationList = new HashSet<PallierComptantSurSituation>();
		   Reference pallierRef1 = new Reference();
		   
		   PallierComptantSurSituation pallier1 = new PallierComptantSurSituation();
		   pallierRef1.setId("ref.element.valeur.fondation");
		   pallier1.setPallier(pallierRef1);
		   pallier1.setValeur(20d);
		   pallierComptantSurSituationList.add(pallier1);
		   
		   Reference pallierRef2 = new Reference();
		   PallierComptantSurSituation pallier2 = new PallierComptantSurSituation();
		   pallierRef2.setId("ref.element.valeur.chainage");
		   pallier2.setPallier(pallierRef2);
		   pallier2.setValeur(50d);
		   pallierComptantSurSituationList.add(pallier2);
		   
		   Reference pallierRef3 = new Reference();
		   PallierComptantSurSituation pallier3 = new PallierComptantSurSituation();
		   pallierRef3.setId("ref.element.valeur.cle");
		   pallier3.setPallier(pallierRef3);
		   pallier3.setValeur(30d);
		   pallierComptantSurSituationList.add(pallier3);
		   
		   modeFinancement4.setPallierComptantSurSituationList(pallierComptantSurSituationList);
		   
		   modeFinancementList.add(modeFinancement4);
		   
		   modeFinancementList.add(modeFinancement1);
		  
		   //Initilisation de la classe de transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   programmeimmobilierTransfert.setModeFinancementList(modeFinancementList);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   //Execution  du test et verification des sortants
		    
		  ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		   assertEquals("SIWISETOP" , rtn.getCode());
		   assertEquals("programme05" , rtn.getDesignation());
		 //  assertEquals(1 , rtn.getModefinancementList().size());
		   
	}

	
	//Test de modification d'un programme immobilier avec l'ajout d'un mode de financement
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testModifierUnProgrammeImmobilierAjoutModeFinancements() {
		
		   //Iniatilisation des entrants
		
		   Boolean estCreation = false ;
		   
		   ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
		   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		
		   programmeimmobilier.setId("programme01") ;
		   programmeimmobilier.setDesignation("Emergence") ;
		   programmeimmobilier.setCode("CCD");
		   programmeimmobilier.setVersion(0);
		   
		   Promoteur promoteur = new Promoteur();
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   promoteur.setId("promoteur01");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   //Iniatilisation de la liste des modes de financement
		   
		   Set<ModeFinancement> modeFinancementList = new HashSet();
		   TypeFinancement typeFinancement = new TypeFinancement();
		   
		   		//Mode de financement de type Comptant
		   
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   modeFinancement1.setDesignation("Comptant");
		   modeFinancement1.setDescription("Paiement comptant");
		   
		   typeFinancement.setId("ref.element.valeur.comptant");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement);
		   
		   modeFinancementList.add(modeFinancement1);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   //Initialisation de la classe de transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   
		   programmeimmobilierTransfert.setModeFinancementList(modeFinancementList);
		   
		   //Execution  du test et verification des sortants
		    
		   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		   assertEquals("programme01" , rtn.getId());
		 //  assertEquals(1 , rtn.getModefinancementList().size());
		   
	}*/
	
	/*
	
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnProgrammeImmobilierAvecModeFinancements() {
		
		  
		  //Iniatilisation des entrants
			
	       Boolean estCreation = true ;
	       
	       ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
	       ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		   
	       programmeimmobilier.setDesignation("programme05") ;
	       programmeimmobilier.setDescription("Programme SIWISETOP") ;
	       programmeimmobilier.setCode("SIWISETOP");
		   
		   Promoteur promoteur = new Promoteur();
		   
		   promoteur.setId("promoteur01");
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   //Iniatilisation de la liste des modes de financement
		   
		   Set<ModeFinancement> modeFinancementList = new HashSet<ModeFinancement>();
		  
		   
		   //Mode de financement de type temperament
		   
		   Temperament temperament = new Temperament();
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   modeFinancement1.setDesignation("Temperament");
		   modeFinancement1.setDescription("Paiement par temperament");
		   
		   TypeFinancement typeFinancement1 = new TypeFinancement();
		   typeFinancement1.setId("ref.element.valeur.temperament");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement1);
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.jour");
		   
		   temperament.setNombrePeriode(100) ;
		   temperament.setApportPersonnel(0.50);
		   temperament.setPeriodicite(reference);
		   temperament.setDelaiRelance(12);
		   temperament.setDateDebut(LocalDate.parse("2021-12-12"));
		   
		   modeFinancement1.setTemperament(temperament);
		
		   modeFinancementList.add(modeFinancement1);
		   
		   
		   //ModeFinancement de type tiers collecteur
		   
		   TiersCollecteur tiersCollecteur = new TiersCollecteur();
		   ModeFinancement modeFinancement2 = new ModeFinancement();
		   modeFinancement2.setDesignation("tierscollecteur");
		   modeFinancement2.setDescription("Paiement par tiers collecteur");
		   
		   TypeFinancement typeFinancement2 = new TypeFinancement();
		   typeFinancement2.setId("ref.element.valeur.tierscollecteur");
		   
		   modeFinancement2.setTypeFinancement(typeFinancement2);
		   
		   Tiers tiers = new Tiers();
		   
		   tiers.setId("tiers01") ;
		   tiers.setDesignation("Etat");
		   tiersCollecteur.setTiers(tiers);
		   
		   modeFinancement2.setTierscollecteur(tiersCollecteur);
		   
		   
		  // tiers.setTiers("Etat de Côte d'Ivoire");
		   
		   modeFinancementList.add(modeFinancement2);
		   
		   //Initilisation de la classe de transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   programmeimmobilierTransfert.setModeFinancementList(modeFinancementList);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   //Execution  du test et verification des sortants
		    
		  ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		   assertEquals("SIWISETOP" , rtn.getCode());
		   assertEquals("programme05" , rtn.getDesignation());
	
		   
	}*/
	
	
	//Test de creation d'un programme immobilier comportant : au moins un mode de financement et au moins une video
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnProgrammeImmobilierAvecVideoListAvecModeFinancements() {
		
		  
		  //Iniatilisation des entrants
			
	       Boolean estCreation = true ;
	       
	       ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
	       ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		   
	       programmeimmobilier.setDesignation("programme05") ;
	       programmeimmobilier.setDescription("Programme SIWISETOP") ;
	       programmeimmobilier.setCode("SIWISETOP");
		   
		   Promoteur promoteur = new Promoteur();
		   
		   promoteur.setId("promoteur01");
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   	//Iniatilisation de la liste des modes de financement
		   
		   Set<ModeFinancement> modeFinancementList = new HashSet<ModeFinancement>();
		   
		   
		  // TypeFinancement typeFinancement = new TypeFinancement();
		   
		   		//Mode de financement de type Comptant
		   
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   modeFinancement1.setDesignation("Comptantnnnnnnnnnnnnnnn");
		   modeFinancement1.setDescription("Paiement comptant");
		   
		   TypeFinancement typeFinancement1 = new TypeFinancement();
		   
		   typeFinancement1.setId("ref.element.valeur.comptant");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement1);
		   
	     
		   
		   
		   		//Mode de financement de type Credit bancaire
		   
		   CreditBancaire creditBancaire = new CreditBancaire();
		   ModeFinancement modeFinancement2 = new ModeFinancement();
		   modeFinancement2.setDesignation("Credit Bancaire SGBCI");
		   modeFinancement2.setDescription("Paiement Credit Bancaire");
		   
		   TypeFinancement typeFinancement2 = new TypeFinancement();
		   typeFinancement2.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement2.setTypeFinancement(typeFinancement2);
		   
		   creditBancaire.setDuree(60);
		   creditBancaire.setTaux(6d);
		   
		   EFi sgbci = new EFi();
		   sgbci.setId("sgbci");
		   creditBancaire.setEfi(sgbci);
		   
		   modeFinancement2.setCreditBancaire(creditBancaire);
		   
		   modeFinancementList.add(modeFinancement2);
		   
		   
		   ModeFinancement modeFinancement3 = new ModeFinancement();
		   modeFinancement3.setDesignation("Credit Bancaire BICICI");
		   modeFinancement3.setDescription("Paiement Credit Bancaire");
		   
		   TypeFinancement typeFinancement3 = new TypeFinancement();
		   typeFinancement3.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement3.setTypeFinancement(typeFinancement3);
		   
		   creditBancaire.setDuree(72);
		   creditBancaire.setTaux(8d);
		   
		   EFi bicici = new EFi();
		   bicici.setId("bicici");
		   creditBancaire.setEfi(bicici); 
		   
		   modeFinancement3.setCreditBancaire(creditBancaire);
		   
		   modeFinancementList.add(modeFinancement3);
		   
		   
		   		//Mode de financement de type Comptant sur situation
		   
		   ModeFinancement modeFinancement4 = new ModeFinancement();
		   modeFinancement4.setDesignation("Comptant sur situation");
		   modeFinancement4.setDescription("Paiement comptant sur situation");
		   
		   TypeFinancement typeFinancement4 = new TypeFinancement();
		   
		   typeFinancement4.setId("ref.element.valeur.comptantsursituation");
		   
		   modeFinancement4.setTypeFinancement(typeFinancement4);
		   
		   Set<PallierComptantSurSituation> pallierComptantSurSituationList = new HashSet<PallierComptantSurSituation>();
		   Reference pallierRef1 = new Reference();
		   
		   PallierComptantSurSituation pallier1 = new PallierComptantSurSituation();
		   pallierRef1.setId("ref.element.valeur.fondation");
		   pallier1.setPallier(pallierRef1);
		   pallier1.setValeur(20d);
		   pallierComptantSurSituationList.add(pallier1);
		   
		   Reference pallierRef2 = new Reference();
		   PallierComptantSurSituation pallier2 = new PallierComptantSurSituation();
		   pallierRef2.setId("ref.element.valeur.chainage");
		   pallier2.setPallier(pallierRef2);
		   pallier2.setValeur(50d);
		   pallierComptantSurSituationList.add(pallier2);
		   
		   Reference pallierRef3 = new Reference();
		   PallierComptantSurSituation pallier3 = new PallierComptantSurSituation();
		   pallierRef3.setId("ref.element.valeur.cle");
		   pallier3.setPallier(pallierRef3);
		   pallier3.setValeur(30d);
		   pallierComptantSurSituationList.add(pallier3);
		   
		   modeFinancement4.setPallierComptantSurSituationList(pallierComptantSurSituationList);
		   
		  modeFinancementList.add(modeFinancement4);
		   
		  modeFinancementList.add(modeFinancement1);
		   
		   
		   

		   //Initialisation de la liste des videos
		   
		  
		   
		   Document document7 =  new Document();
		   
		   document7.setDesignation("VideoList7");
		   document7.setContenu("Première video de la liste");
		   document7.setDescription("VideoList7");
		   
		   DocumentFormat format7 = new DocumentFormat();
		   format7.setId("documentformat03");
		  
		   document7.setFormat(format7);
		   
		   Reference typeDocument7 = new Reference();
		   typeDocument7.setId("ref.element.typeValeur.video");
		   
		   document7.setTypeDocument(typeDocument7);
		   
		   
		   //Image liste 8
		   
		   Document document8 =  new Document();
		   
		   document8.setDesignation("VideoList8");
		   document8.setContenu("deuxième video de la liste ");
		   document8.setDescription("VideoList8");
		   
		   DocumentFormat format8 = new DocumentFormat();
		   format8.setId("documentformat03");
		  
		   document8.setFormat(format8);
		   
		   Reference typeDocument8 = new Reference();
		   typeDocument8.setId("ref.element.typeValeur.video");
		   
		   document8.setTypeDocument(typeDocument8);
		   
		   
		   //Image liste 3
		   
		   Document document9 =  new Document();
		   
		   document9.setDesignation("ImageList9");
		   document9.setContenu("troisième video de la liste  ");
		   document9.setDescription("ImageList9");
		   
		   DocumentFormat format9 = new DocumentFormat();
		   format9.setId("documentformat03");
		  
		   document9.setFormat(format9);
		   
		   Reference typeDocument9 = new Reference();
		   typeDocument9.setId("ref.element.typeValeur.video");
		   
		   document9.setTypeDocument(typeDocument9);
		   
		   //Iniatialisation d'une collection Set d'image
		   
		   Set<Document> VideoList1 = new HashSet();
		   
		   VideoList1.add(document7);
		   VideoList1.add(document8);
		   VideoList1.add(document9);
		  
		   //Initilisation de la classe de transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   programmeimmobilierTransfert.setModeFinancementList(modeFinancementList);
		   
		   programmeimmobilierTransfert.setVideosList(VideoList1);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   

		   //Execution  du test 
		    
		  ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		  
		  
		  //Vérification des resultats
		  
		   assertEquals("SIWISETOP" , rtn.getCode());
		   assertEquals("programme05" , rtn.getDesignation());
		   
		   
		 //  assertEquals(1 , rtn.getModefinancementList().size());
		   
	}*/
	
	
	
	/**
	 * 
	 * Date de créatio du test: 27/07/2021
	 * 
	 * Création d'un programme immobilier comportant une liste de videos
	 * avec une liste de financements dont:
	 * 
	 * un financement au comptant
	 * deux financements au paiment crédit bancaire dont chacun comportant une lettre de confort
	 * un financement au comptant sur situation comportant trois niveaux de pallier
	 * 
	 */
	
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnProgrammeImmobilierAvecVideoListAvecModeFinancementsAvecCreditBancireAyantLettredeConfort() {
		
		  
		  //Iniatilisation des entrants
		   
		   //Instance de la classe de transfert
		
		   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		   
		   //Variable indiquant qu'il s'agit d'une création
		   
	       Boolean estCreation = true ;
	       
	       //Instance de la classe Programme Immobilier et renseignements des informations sur le programme
	       
	       ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
	       
		   
	       programmeimmobilier.setDesignation("programme05") ;
	       programmeimmobilier.setDescription("Programme SIWISETOP") ;
	       programmeimmobilier.setCode("SIWISETOP");
		   
		   Promoteur promoteur = new Promoteur();
		   
		   promoteur.setId("promoteur01");
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   //Instance de la liste de mode de financements
		   
		   Set<ModeFinancement> modeFinancementList = new HashSet<ModeFinancement>();
		   
		   
		  
		   // Initialisation du mode de financement comptant
		   
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   modeFinancement1.setDesignation("Comptantnnnnnnnnnnnnnnn");
		   modeFinancement1.setDescription("Paiement comptant");
		   
		   TypeFinancement typeFinancement1 = new TypeFinancement();
		   
		   typeFinancement1.setId("ref.element.valeur.comptant");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement1);
		   
	     
		 
		   //Initilisation du mode financement credit bancaire 1
		   
		   CreditBancaire creditBancaire = new CreditBancaire();
		   ModeFinancement modeFinancement2 = new ModeFinancement();
		   modeFinancement2.setDesignation("Credit Bancaire SGBCI");
		   modeFinancement2.setDescription("Paiement Credit Bancaire");
		   
		   TypeFinancement typeFinancement2 = new TypeFinancement();
		   typeFinancement2.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement2.setTypeFinancement(typeFinancement2);
		   
		   creditBancaire.setDuree(60);
		   creditBancaire.setTaux(6d);
		   
		   EFi sgbci = new EFi();
		   sgbci.setId("sgbci");
		   creditBancaire.setEfi(sgbci);
		  
		  
			Document lettreConfort = new Document();
			
			lettreConfort.setDesignation("Lettre de confort du credit bancaire SGBCI");
			lettreConfort.setDescription("Ceci est une lettre de recommandation d'une personne pour une autre personne auprés d'une autre banque");
			lettreConfort.setContenu("Nous vous recommendons M Alzouma dans la prespective de pouvoir obtenir des prets bancaires");
			
			DocumentFormat format = new DocumentFormat();
			format.setId("documentformat03");
			  
			lettreConfort.setFormat(format);
		    Reference typeDocument = new Reference();
			typeDocument.setId("ref.element.typeValeur.image");
			   
		   lettreConfort.setTypeDocument(typeDocument);
			
		   creditBancaire.setLettreConfort(lettreConfort);
		
		   
		   modeFinancement2.setCreditBancaire(creditBancaire);
		   
		   modeFinancementList.add(modeFinancement2);
		   
		   //Initilisation du mode financement credit bancaire 2
		   
		   ModeFinancement modeFinancement3 = new ModeFinancement();
		   modeFinancement3.setDesignation("Credit Bancaire BICICI");
		   modeFinancement3.setDescription("Paiement Credit Bancaire");
		   
		   TypeFinancement typeFinancement3 = new TypeFinancement();
		   typeFinancement3.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement3.setTypeFinancement(typeFinancement3);
		   
		   CreditBancaire creditBancaire1 = new CreditBancaire();
		   
		   creditBancaire1.setDuree(72);
		   creditBancaire1.setTaux(8d);
		   
		   EFi bicici = new EFi();
		   bicici.setId("bicici");
		   creditBancaire1.setEfi(bicici); 
			
		   Document lettreConfort1 = new Document();
				
		   lettreConfort.setDesignation("Lettre de confort du credit bancaire bicici");
		   lettreConfort.setDescription("Ceci est une lettre de recommandation d'une personne pour une autre personne auprés d'une autre banque");
		   lettreConfort.setContenu("Nous vous recommendons M Alzouma dans la prespective de pouvoir obtenir des prets bancaires");
				
		   DocumentFormat format1 = new DocumentFormat();
		   format.setId("documentformat03");
				  
		   lettreConfort.setFormat(format1);
		   Reference typeDocument1 = new Reference();
		   typeDocument1.setId("ref.element.typeValeur.image");
				   
		   lettreConfort.setTypeDocument(typeDocument);
				
		   creditBancaire.setLettreConfort(lettreConfort1);
			
		   
		   modeFinancement3.setCreditBancaire(creditBancaire1);
		   
		   modeFinancementList.add(modeFinancement3);
		   
		   
		   //Mode de financement de type Comptant sur situation
		   
		   ModeFinancement modeFinancement4 = new ModeFinancement();
		   modeFinancement4.setDesignation("Comptant sur situation");
		   modeFinancement4.setDescription("Paiement comptant sur situation");
		   
		   TypeFinancement typeFinancement4 = new TypeFinancement();
		   
		   typeFinancement4.setId("ref.element.valeur.comptantsursituation");
		   
		   modeFinancement4.setTypeFinancement(typeFinancement4);
		   
		   Set<PallierComptantSurSituation> pallierComptantSurSituationList = new HashSet<PallierComptantSurSituation>();
		   Reference pallierRef1 = new Reference();
		   
		   //Initialisation du premier pallier
		   
		   PallierComptantSurSituation pallier1 = new PallierComptantSurSituation();
		   pallierRef1.setId("ref.element.valeur.fondation");
		   pallier1.setPallier(pallierRef1);
		   pallier1.setValeur(20d);
		   pallierComptantSurSituationList.add(pallier1);
		   
		   //Initialisation du deuxiéme pallier
		   
		   Reference pallierRef2 = new Reference();
		   PallierComptantSurSituation pallier2 = new PallierComptantSurSituation();
		   pallierRef2.setId("ref.element.valeur.chainage");
		   pallier2.setPallier(pallierRef2);
		   pallier2.setValeur(50d);
		   pallierComptantSurSituationList.add(pallier2);
		   
		   //Initialisation du troisiéme pallier
		   
		   Reference pallierRef3 = new Reference();
		   PallierComptantSurSituation pallier3 = new PallierComptantSurSituation();
		   pallierRef3.setId("ref.element.valeur.cle");
		   pallier3.setPallier(pallierRef3);
		   pallier3.setValeur(30d);
		   pallierComptantSurSituationList.add(pallier3);
		   
		   modeFinancement4.setPallierComptantSurSituationList(pallierComptantSurSituationList);
		   
		   modeFinancementList.add(modeFinancement4);
		   
		   modeFinancementList.add(modeFinancement1);
		   
		   
		   

		   //Initialisation de la liste de videos du programme immobilier
		  
		   Document document7 =  new Document();
		   
		   document7.setDesignation("VideoList7");
		   document7.setContenu("Première video de la liste");
		   document7.setDescription("VideoList7");
		   
		   DocumentFormat format7 = new DocumentFormat();
		   format7.setId("documentformat03");
		  
		   document7.setFormat(format7);
		   
		   Reference typeDocument7 = new Reference();
		   typeDocument7.setId("ref.element.typeValeur.video");
		   
		   document7.setTypeDocument(typeDocument7);
		   
		   
		   //Image liste 8
		   
		   Document document8 =  new Document();
		   
		   document8.setDesignation("VideoList8");
		   document8.setContenu("deuxième video de la liste ");
		   document8.setDescription("VideoList8");
		   
		   DocumentFormat format8 = new DocumentFormat();
		   format8.setId("documentformat03");
		  
		   document8.setFormat(format8);
		   
		   Reference typeDocument8 = new Reference();
		   typeDocument8.setId("ref.element.typeValeur.video");
		   
		   document8.setTypeDocument(typeDocument8);
		   
		   
		   //Image liste 3
		   
		   Document document9 =  new Document();
		   
		   document9.setDesignation("ImageList9");
		   document9.setContenu("troisième video de la liste  ");
		   document9.setDescription("ImageList9");
		   
		   DocumentFormat format9 = new DocumentFormat();
		   format9.setId("documentformat03");
		  
		   document9.setFormat(format9);
		   
		   Reference typeDocument9 = new Reference();
		   typeDocument9.setId("ref.element.typeValeur.video");
		   
		   document9.setTypeDocument(typeDocument9);
		   
		   //Iniatialisation d'une collection Set d'image
		   
		   Set<Document> VideoList1 = new HashSet();
		   
		   VideoList1.add(document7);
		   VideoList1.add(document8);
		   VideoList1.add(document9);
		  
		   
		   //Initilisation de la classe de transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   programmeimmobilierTransfert.setModeFinancementList(modeFinancementList);
		   
		   programmeimmobilierTransfert.setVideosList(VideoList1);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   

		   //Execution  du test 
		    
		   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		  
		  
		  //Vérification des resultats
		  
		   assertEquals("SIWISETOP" , rtn.getCode());
		   assertEquals("programme05" , rtn.getDesignation());
		   
		   
	}
	
}*/
