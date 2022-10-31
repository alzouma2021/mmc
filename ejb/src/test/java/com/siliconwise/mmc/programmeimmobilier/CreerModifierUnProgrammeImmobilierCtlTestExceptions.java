package com.siliconwise.mmc.programmeimmobilier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.organisation.efi.EFi;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.produitlogement.FamilleProprieteProduitLogement;
import com.siliconwise.mmc.produitlogement.OperateurCritere;
import com.siliconwise.mmc.produitlogement.ProduitLogement;
import com.siliconwise.mmc.produitlogement.ProduitLogementDAO;
import com.siliconwise.mmc.produitlogement.ProduitLogementDAOInterface;
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
public class CreerModifierUnProgrammeImmobilierCtlTestExceptions {

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
						ProgrammeImmobilierTransfert.class
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
	
	
	
		//Tests des exceptions
	
		//Test de creation d'un programme immobilier sans l'identite domaniale 
		 
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public void testCreerUnProgrammeImmobilierSansModeFinancementsAvecErreurIdentiteNaturelle() {
			
			  //Iniatilisation des entrants
			
		       Boolean estCreation = true ;
		       
		       ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
			
			   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
			   
			   programmeimmobilier.setDesignation("programme04") ;
			   programmeimmobilier.setDescription("Programme SIWISE") ;
			   //programmeImmobilier.setCode("SIWISEUP");// Code non défini
			   
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
			   
			   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
			   
			   //assertEquals(null , CreerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeImmobilier, true, null, true, locale, loggedInUser, msgList));
			   assertEquals(null , rtn);
			//   assertFalse (msgList.isEmpty());
			   
		}
		 
		//Test de creation d'un programme immobilier avec un promoteur qui n'existe pas
		@Test
		public void testModifierUnProgrammeImmobilierSansModeFinancementsAvecErreurAssociation() {
			
			  //Iniatilisation des entrants
				
			  Boolean estCreation = false ;
				
			   ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
			   
			   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
			   
			   programmeimmobilier.setId("programme01") ;
			   programmeimmobilier.setDesignation("Emergence") ;
			   programmeimmobilier.setCode("CCD");
			   programmeimmobilier.setVersion(0);
			   
			   Promoteur promoteur = new Promoteur();
			   
			   promoteur.setId("promoteur10");
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
			   
			   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
			   
			   assertEquals(null , rtn);
			   assertFalse (msgList.isEmpty());
			   
		}
		
	//Test de creation d'un programme immobilier avec un mode de financement de type pallier sur sitaution dont la somme des palliers est inferieure ou superieure 100
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUnProgrammeImmobilierAvecErreurModeFinancements() {
		
		  //Iniatilisation des entrants
		
		   Boolean estCreation = true ;
		   
		   ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
		   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		   
		   programmeimmobilier.setDesignation("programme05") ;
		   programmeimmobilier.setDescription("Programme SIWISETOP") ;
		   programmeimmobilier.setCode("SIWISETOP");
		   
		   Promoteur promoteur = new Promoteur();
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   promoteur.setId("promoteur01");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   	//Iniatilisation de la liste des modes de financement
		   
		   Set<ModeFinancement> modeFinancementList = new HashSet<ModeFinancement>();
		   
		   TypeFinancement typeFinancement1 = new TypeFinancement();
		   
		   		//Mode de financement de type Comptant
		   
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   modeFinancement1.setDesignation("Comptant");
		   modeFinancement1.setDescription("Paiement comptant");
		   
		   
		   typeFinancement1.setId("ref.element.valeur.comptant");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement1);
		   
		//   modeFinancementList.add(modeFinancement1);
		   
		   		//Mode de financement de type Credit bancaire
		   
		   CreditBancaire creditBancaire = new CreditBancaire();
		   ModeFinancement modeFinancement2 = new ModeFinancement();
		   modeFinancement2.setDesignation("Credit Bancaire SGBCI");
		   modeFinancement2.setDescription("Paiement Credit Bancaire ");
		   modeFinancement2.setVersion(0);
		   
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
		   modeFinancement3.setVersion(0);
		   
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
		   
		   		//Erreur sur Mode de financement de type Comptant sur situation
		   		//Total des valeurs des palliers different de 100
		   
		   ModeFinancement modeFinancement4 = new ModeFinancement();
		   modeFinancement4.setDesignation("Comptant sur situation");
		   modeFinancement4.setDescription("Paiement comptant sur situation");
		   
		   TypeFinancement typeFinancement4 = new TypeFinancement();
		   
		   typeFinancement4.setId("ref.element.valeur.comptantsursituation");
		   
		   modeFinancement4.setTypeFinancement(typeFinancement4);
		   
		   Set<PallierComptantSurSituation> pallierComptantSurSituationList = new HashSet<PallierComptantSurSituation> ();
		   
		   Reference pallierRef1 = new Reference();
		  
		   PallierComptantSurSituation pallier1 = new PallierComptantSurSituation();
		   pallierRef1.setId("ref.element.valeur.fondation");
		   pallier1.setPallier(pallierRef1);
		   pallier1.setValeur(20d);
		   pallierComptantSurSituationList.add(pallier1);
		   
		   Reference pallierRef2 = new Reference();
		   
		   PallierComptantSurSituation pallier2 = new PallierComptantSurSituation();
		   pallierRef2.setId("ref.element.valeur.chainage");
		   pallier1.setPallier(pallierRef2);
		   pallier1.setValeur(40d);
		   pallierComptantSurSituationList.add(pallier2);
		   
		   Reference pallierRef3 = new Reference();
		   
		   PallierComptantSurSituation pallier3 = new PallierComptantSurSituation();
		   pallierRef3.setId("ref.element.valeur.cle");
		   pallier1.setPallier(pallierRef3);
		   pallier1.setValeur(30d);
		   
		   pallierComptantSurSituationList.add(pallier3);
		   
		   modeFinancement4.setPallierComptantSurSituationList(pallierComptantSurSituationList);
		   
		   modeFinancementList.add(modeFinancement4);
		   
		 
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   //Inittialisation de la classe de Transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   programmeimmobilierTransfert.setModeFinancementList(modeFinancementList);
		   
		   //Execution  du test et verification des sortants
		    
		   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		   assertEquals("SIWISETOP" , rtn.getCode());
		   assertEquals("programme05" , rtn.getDesignation());
		//   assertEquals(3 , rtn.getModefinancementList().size());
		   assertFalse (msgList.isEmpty());
		   
	}
	
	//Test de creation d'un programme immobilier avec un credit bancaire dont l'efi n'existe pas
	 
	@Test
	public void testModifierUnProgrammeImmobilierErreurModeFinancements() {
		
		   //Iniatilisation des entrants
		
		   Boolean estCreation = false ;
		   
		   ProgrammeImmobilier programmeimmobilier = new ProgrammeImmobilier();
		
		   ProgrammeImmobilierTransfert programmeimmobilierTransfert = new ProgrammeImmobilierTransfert();
		
		   programmeimmobilier.setId("programme01") ;
		   programmeimmobilier.setDesignation("Emergence Plus") ;
		   programmeimmobilier.setCode("CCD");
		   programmeimmobilier.setVersion(0);
		   
		   Promoteur promoteur = new Promoteur();
		   
		   promoteur.setId("promoteur01");
		   promoteur.setIdentifiantLegal("Bouake Fofana");
		   
		   Ville ville = new Ville();
		   ville.setId("ville01");
		   
		   programmeimmobilier.setPromoteur(promoteur);
		   programmeimmobilier.setVille(ville);
		   
		   //Iniatilisation de la liste des modes de financement
		   
		   Set<ModeFinancement> modeFinancementList = new HashSet<ModeFinancement>();
		   
		   TypeFinancement typeFinancement = new TypeFinancement();
		   
		   		//Erreur sur Mode de financement de type Credit bancaire
		   		// EFI inexistant (bnp)
		   
		   ModeFinancement modeFinancement1 = new ModeFinancement();
		   CreditBancaire creditBancaire = new CreditBancaire();
		   modeFinancement1.setDesignation("Credit Bancaire");
		   modeFinancement1.setDescription("Paiement Credit Bancaire");
		   
		   
		   typeFinancement.setId("ref.element.valeur.creditbancaire");
		   
		   modeFinancement1.setTypeFinancement(typeFinancement);
		   
		   creditBancaire.setDuree(60);
		   creditBancaire.setTaux(6d);
		   
		   EFi bnp = new EFi();
		   bnp.setId("bnp");
		   creditBancaire.setEfi(bnp);
		   
		   modeFinancement1.setCreditBancaire(creditBancaire);
		   
		   modeFinancementList.add(modeFinancement1);
		   
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   //Initialisation de la classe transfert
		   
		   programmeimmobilierTransfert.setProgrammeImmobilier(programmeimmobilier);
		   
		   programmeimmobilierTransfert.setModeFinancementList(modeFinancementList);
		   
		   //Execution  du test et verification des sortants
		    
		   ProgrammeImmobilier rtn = creerModifierUnProgrammeImmobilierCtl.creerModifierUnProgrammeImmobilier(programmeimmobilierTransfert, true, null, true, locale, loggedInUser, msgList, estCreation) ;
		   
		   assertEquals("programme01" , rtn.getId());
		   assertEquals("Emergence Plus" , rtn.getDesignation());
	//	   assertEquals(0 , rtn.getModefinancementList().size());
		  
	}
	
}*/
