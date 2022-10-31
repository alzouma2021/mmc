package com.siliconwise.mmc.financement;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
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
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class CreerModifierUnModeFinancementTest {
	
	
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
						DocumentCtlInterface.class,
						DocumentCtl.class,
						DocumentDAOInterface.class,
						DocumentDAO.class,
						TemperamentDAOInterface.class,
						TemperamentDAO.class,
						Tiers.class,
						TiersCollecteur.class,
						TiersCollecteurDAOInterface.class,
						TiersCollecteurDAO.class,
						Temperament.class,
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class,
						AppConfigUtil.class,
						ProduitLogementDocument.class,
						ProduitLogementDocumentCtlInterface.class,
						ProduitLogementDocumentCtl.class,
						ProgrammeImmobilierDocumentCtlInterface.class,
						ProgrammeImmobilierDocumentCtl.class,
						ProduitLogementDocumentDAOInterface.class,
						ProduitLogementDocumentDAO.class,
						ProgrammeImmobilierDocument.class,
						ProgrammeImmobilierDocumentDAOInterface.class,
						ProgrammeImmobilierDocumentDAO.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	CreerModifierModeFinancementCtlInterface CreerModifierModeFinancementCtl;
	
	//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	
	
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnModeFinancementComptant() {
		
		   //Iniatilisation des entrants
		
			ModeFinancement modeFinancement = new ModeFinancement();
			modeFinancement.setDesignation("Comptant");
			modeFinancement.setDescription("Paiement comptant");
	   
			TypeFinancement typeFinancement = new TypeFinancement();
			typeFinancement.setId("ref.element.valeur.comptant");
	   
			modeFinancement.setTypeFinancement(typeFinancement);
	   
			ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
			programmeImmobilier.setId("programme01") ;
			
			modeFinancement.setProgrammeImmobilier(programmeImmobilier);
		   
		    User loggedInUser = new User();
		   
		    loggedInUser.setId("user1");
		    loggedInUser.setNom("alzouma");
		    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		     ModeFinancement rtn = CreerModifierModeFinancementCtl.creerModifierUnModeFinancement(modeFinancement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("Paiement comptant" , rtn.getDescription());
		   
	}

	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnModeFinancementCreditBancaire() {
		
		ModeFinancement modeFinancement = new ModeFinancement();
		modeFinancement.setDesignation("Credit Bancaire");
		modeFinancement.setDescription("Paiement Credit Bancaire");
   
		TypeFinancement typeFinancement = new TypeFinancement();
		typeFinancement.setId("ref.element.valeur.creditbancaire");
   
		modeFinancement.setTypeFinancement(typeFinancement);
		
		CreditBancaire creditBancaire = new CreditBancaire();
		creditBancaire.setDuree(60);
		creditBancaire.setTaux(6d);
		   
		EFi sgbci = new EFi();
		sgbci.setId("sgbci");
		creditBancaire.setEfi(sgbci);
		   
		modeFinancement.setCreditBancaire(creditBancaire);
   
		ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
		programmeImmobilier.setId("programme01") ;
		
		modeFinancement.setProgrammeImmobilier(programmeImmobilier);
	   
	    User loggedInUser = new User();
	   
	   loggedInUser.setId("user1");
	   loggedInUser.setNom("alzouma");
	   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	   
	    //Execution  du test et verification des sortants
	    
	    ModeFinancement rtn = CreerModifierModeFinancementCtl.creerModifierUnModeFinancement(modeFinancement, true, null, true, locale, loggedInUser, msgList) ;
	   
	    assertEquals("Paiement Credit Bancaire" , rtn.getDescription());
	}
	

	@Test
	public void testCreerUnModeFinancementComptantSurSituation() {
		
		   //Iniatilisation des entrants
		
		 	ModeFinancement modeFinancement = new ModeFinancement();
		 	modeFinancement.setDesignation("Comptant sur situation");
		 	modeFinancement.setDescription("Paiement comptant sur situation");
		   
		 	TypeFinancement typeFinancement = new TypeFinancement();
		 	typeFinancement.setId("ref.element.valeur.comptantsursituation");
		   
		 	modeFinancement.setTypeFinancement(typeFinancement);
		   
		 	Set<PallierComptantSurSituation> pallierComptantSurSituationList = new HashSet();
		 	Reference pallier = new Reference();
		   
		 	PallierComptantSurSituation pallier1 = new PallierComptantSurSituation();
		 	pallier.setId("ref.element.valeur.fondation");
		 	pallier1.setPallier(pallier);
		 	pallier1.setValeur(20d);
		 	pallierComptantSurSituationList.add(pallier1);
		   
		 	PallierComptantSurSituation pallier2 = new PallierComptantSurSituation();
		 	pallier.setId("ref.element.valeur.chainage");
		 	pallier2.setPallier(pallier);
		 	pallier2.setValeur(50d);
		 	pallierComptantSurSituationList.add(pallier2);
		   
		   PallierComptantSurSituation pallier3 = new PallierComptantSurSituation();
		   pallier.setId("ref.element.valeur.cle");
		   pallier3.setPallier(pallier);
		   pallier3.setValeur(30d);
		   pallierComptantSurSituationList.add(pallier3);
		   
		   modeFinancement.setPallierComptantSurSituationList(pallierComptantSurSituationList);
		   
		   ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
		   programmeImmobilier.setId("programme01") ;
			
		   modeFinancement.setProgrammeImmobilier(programmeImmobilier);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		   	ModeFinancement rtn = CreerModifierModeFinancementCtl.creerModifierUnModeFinancement(modeFinancement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("Paiement comptant sur situation" , rtn.getDescription());
		   
	}
	
	@Test
	public void testCreerModeFinancementList() {
		   
		   	//Iniatilisation de la liste des modes de financement
		
		   ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
		   programmeImmobilier.setId("programme01") ;
		   programmeImmobilier.setCode("CCD");
		   
		   Set<ModeFinancement> modeFinancementList =  new HashSet();
		
		   TypeFinancement typeFinancement1 = new TypeFinancement();
		   
		   		//Mode de financement de type Comptant
		   
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   modeFinancement1.setDesignation("Comptant");
		   modeFinancement1.setDescription("Paiement comptant");
		   
		   typeFinancement1.setId("ref.element.valeur.comptant");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement1);
		   
		   modeFinancement1.setProgrammeImmobilier(programmeImmobilier);
		   
		 //  modeFinancementList = Collections.unmodifiableSet(modeFinancementList);
		   
		   		//Mode de financement de type Credit bancaire
		   
		   TypeFinancement typeFinancement2 = new TypeFinancement();
		   
		   CreditBancaire creditBancaire = new CreditBancaire();
		   ModeFinancement modeFinancement2 = new ModeFinancement();
		   modeFinancement2.setDesignation("Credit Bancaire");
		   modeFinancement2.setDescription("Paiement Credit Bancaire");
		   
		   typeFinancement2.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement2.setTypeFinancement(typeFinancement2);
		   
		   creditBancaire.setDuree(60);
		   creditBancaire.setTaux(6d);
		   
		   EFi sgbci = new EFi();
		   sgbci.setId("sgbci");
		   creditBancaire.setEfi(sgbci);
		   
		   modeFinancement2.setCreditBancaire(creditBancaire);
		   
		   modeFinancement2.setProgrammeImmobilier(programmeImmobilier);
		   
		 //  modeFinancementList = Collections.unmodifiableSet(modeFinancementList);
		   
		   ModeFinancement modeFinancement3 = new ModeFinancement();
		   modeFinancement3.setDesignation("Credit Bancaire");
		   modeFinancement3.setDescription("Paiement Credit Bancaire");
		   
		   modeFinancement3.setTypeFinancement(typeFinancement2);
		   
		   creditBancaire.setDuree(72);
		   creditBancaire.setTaux(8d);
		   
		   EFi bicici = new EFi();
		   bicici.setId("bicici");
		   creditBancaire.setEfi(bicici);
		   
		   modeFinancement3.setCreditBancaire(creditBancaire);
		   
		   modeFinancement3.setProgrammeImmobilier(programmeImmobilier);
		 
		   
		   		//Mode de financement de type Comptant sur situation
		   
		   TypeFinancement typeFinancement4 = new TypeFinancement();
		   
		   ModeFinancement modeFinancement4 = new ModeFinancement();
		   modeFinancement4.setDesignation("Comptant sur situation");
		   modeFinancement4.setDescription("Paiement comptant sur situation");
		   
		   typeFinancement4.setId("ref.element.valeur.comptantsursituation");
		   
		   modeFinancement4.setTypeFinancement(typeFinancement4);
		   
		   Set<PallierComptantSurSituation> pallierComptantSurSituationList = new HashSet();
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
		   
		   modeFinancement4.setProgrammeImmobilier(programmeImmobilier);
		   
		  modeFinancementList.add(modeFinancement1);
		  modeFinancementList.add(modeFinancement2);
		  modeFinancementList.add(modeFinancement3);
		   modeFinancementList.add(modeFinancement4);
		  
		// modeFinancementList = Collections.;
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   
		   //Execution  du test et verification des sortants
		    
		   List<ModeFinancement> rtnList = CreerModifierModeFinancementCtl.creerModifierModeFinancementList( modeFinancementList, true,programmeImmobilier, null, true, locale, loggedInUser, msgList);
		   
		   assertEquals(3 , rtnList.get(0).getPallierComptantSurSituationList().size());
		
	}*/
	
	
	
	/**
	 * 
	 * Creer un mode de financement avec un credit bancaire
	 * avce une lettre de confort
	 * 
	 */
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnModeFinancementCreditBancaireAvecLettredeConfor() {
		
		//Initialisation du mode de financement
		
		ModeFinancement modeFinancement = new ModeFinancement();
		modeFinancement.setDesignation("Credit Bancaire lettre de confort");
		modeFinancement.setDescription("Paiement Credit Bancaire lettre de confort");
   
		TypeFinancement typeFinancement = new TypeFinancement();
		typeFinancement.setId("ref.element.valeur.creditbancaire");
   
		modeFinancement.setTypeFinancement(typeFinancement);
		
		//Initialisation des informations complementaires du mode de financement
		
		CreditBancaire creditBancaire = new CreditBancaire();
		creditBancaire.setDuree(60);
		creditBancaire.setTaux(6d);
		   
		EFi sgbci = new EFi();
		sgbci.setId("sgbci");
		creditBancaire.setEfi(sgbci);
		
		//Initilisation de la letre de confort
		
		Document lettreConfort = new Document();
		
		lettreConfort.setDesignation("Lettre de confort du credit bancaire");
		lettreConfort.setDescription("Ceci est une lettre de recommandation d'une personne pour une autre personne auprés d'une autre banque");
		lettreConfort.setContenu("Nous vous recommendons M Alzouma dans la prespective de pouvoir obtenir des prets bancaires");
		
		DocumentFormat format = new DocumentFormat();
		format.setId("documentformat03");
		  
		lettreConfort.setFormat(format);
		   
	    Reference typeDocument = new Reference();
	
	    
		typeDocument.setId("ref.element.typeValeur.image");
		   
		lettreConfort.setTypeDocument(typeDocument);
		
		
		creditBancaire.setLettreConfort(lettreConfort);
	
		
		modeFinancement.setCreditBancaire(creditBancaire);
   
		ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
		programmeImmobilier.setId("programme01") ;
		programmeImmobilier.setCode("CCD"); ;
		
		modeFinancement.setProgrammeImmobilier(programmeImmobilier);
	   
	   User loggedInUser = new User();
	   
	   loggedInUser.setId("user1");
	   loggedInUser.setNom("alzouma");
	   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	   
	    //Execution  du test et verification des sortants
	    
	    ModeFinancement rtn = CreerModifierModeFinancementCtl.creerModifierUnModeFinancement(modeFinancement, true, null, true, locale, loggedInUser, msgList) ;
	   
	    assertEquals("Paiement Credit Bancaire lettre de confort" , rtn.getDescription());
	    
	    assertEquals("Lettre de confort du credit bancaire" , rtn.getCreditBancaire().getLettreConfort().getDesignation());
	}
	
	/*
	// Tests des exceptions

	@Test
	public void testCreerUnModeFinancementErreurCreditBancaire() {
		
		ModeFinancement modeFinancement = new ModeFinancement();
		modeFinancement.setDesignation("Credit Bancaire");
		modeFinancement.setDescription("Paiement Credit Bancaire");
   
		TypeFinancement typeFinancement = new TypeFinancement();
		typeFinancement.setId("ref.element.valeur.creditbancaire");
   
		modeFinancement.setTypeFinancement(typeFinancement);
		
		CreditBancaire creditBancaire = new CreditBancaire();
		creditBancaire.setDuree(60);
		//creditBancaire.setTaux(6d); Erreur due au taux non defini
		   
		EFi sgbci = new EFi();
		sgbci.setId("sgbci");
		creditBancaire.setEfi(sgbci);
		   
		modeFinancement.setCreditBancaire(creditBancaire);
   
		ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
		programmeImmobilier.setId("programme01") ;
		
		modeFinancement.setProgrammeImmobilier(programmeImmobilier);
	   
	   User loggedInUser = new User();
	   
	   loggedInUser.setId("user1");
	   loggedInUser.setNom("alzouma");
	   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	   
	    //Execution  du test et verification des sortants
	    
	    assertEquals(null , CreerModifierModeFinancementCtl.creerModifierUnModeFinancement(modeFinancement, true, null, true, locale, loggedInUser, msgList));
	}
	

	@Test
	public void testCreerUnModeFinancementErreurComptantSurSituation() {
		
		   //Iniatilisation des entrants
			// Erreur due au total des valeurs superieur à 100
		
		 	ModeFinancement modeFinancement = new ModeFinancement();
		 	modeFinancement.setDesignation("Comptant sur situation");
		 	modeFinancement.setDescription("Paiement comptant sur situation");
		   
		 	TypeFinancement typeFinancement = new TypeFinancement();
		 	typeFinancement.setId("ref.element.valeur.comptantsursituation");
		   
		 	modeFinancement.setTypeFinancement(typeFinancement);
		   
		 	Set<PallierComptantSurSituation> pallierComptantSurSituationList = new HashSet();
		 	Reference pallier = new Reference();
		   
		 	PallierComptantSurSituation pallier1 = new PallierComptantSurSituation();
		 	pallier.setId("ref.element.valeur.fondation");
		 	pallier1.setPallier(pallier);
		 	pallier1.setValeur(20d);
		 	pallierComptantSurSituationList.add(pallier1);
		   
		 	PallierComptantSurSituation pallier2 = new PallierComptantSurSituation();
		 	pallier.setId("ref.element.valeur.chainage");
		 	pallier2.setPallier(pallier);
		 	pallier2.setValeur(50d);
		 	pallierComptantSurSituationList.add(pallier2);
		   
		   PallierComptantSurSituation pallier3 = new PallierComptantSurSituation();
		   pallier.setId("ref.element.valeur.cle");
		   pallier3.setPallier(pallier);
		   pallier3.setValeur(40d);
		   pallierComptantSurSituationList.add(pallier3);
		   
		   modeFinancement.setPallierComptantSurSituationList(pallierComptantSurSituationList);
		   
		   ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
		   programmeImmobilier.setId("programme01") ;
			
			modeFinancement.setProgrammeImmobilier(programmeImmobilier);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		   assertEquals(null , CreerModifierModeFinancementCtl.creerModifierUnModeFinancement(modeFinancement, true, null, true, locale, loggedInUser, msgList));
	}

}*/
	
	
