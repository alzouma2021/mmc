package com.siliconwise.mmc.produitlogement;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
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
import com.siliconwise.mmc.modefinancement.ModeFinancement;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;
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
import com.siliconwise.mmc.programmeimmobilier.*;
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class CreerModifierUnProduitLogementCtlTestSuite {
	
	
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
					ValeurCaracteristiqueProduitLogementBoolean.class,
					ValeurCaracteristiqueProduitLogementBooleanDAOInterface.class,
					ValeurCaracteristiqueProduitLogementBooleanDAO.class,
					ValeurCaracteristiqueProduitLogementDate.class , 
					ValeurCaracteristiqueProduitLogementDateDAOInterface.class , 
					ValeurCaracteristiqueProduitLogementDateDAO.class , 
					ValeurCaracteristiqueProduitLogementDateTime.class,
					ValeurCaracteristiqueProduitLogementTime.class,
					ValeurCaracteristiqueProduitLogementTimeDAO.class,
					ValeurCaracteristiqueProduitLogementTimeDAOInterface.class,
					ValeurCaracteristiqueProduitLogementDateTimeDAOInterface.class,
					ValeurCaracteristiqueProduitLogementDateTimeDAO.class,
					ValeurCaracteristiqueProduitLogementString.class,
					ValeurCaracteristiqueProduitLogementStringDAOInterface.class,
					ValeurCaracteristiqueProduitLogementStringDAO.class,
					ValeurCaracteristiqueProduitLogementTexte.class,
					ValeurCaracteristiqueProduitLogementTexteDAO.class,
					ValeurCaracteristiqueProduitLogementTexteDAOInterface.class,
					ValeurCaracteristiqueProduitLogementLong.class,
					ValeurCaracteristiqueProduitLogementLongDAO.class,
					ValeurCaracteristiqueProduitLogementLongDAOInterface.class,
					ValeurCaracteristiqueProduitLogementFloat.class,
					ValeurCaracteristiqueProduitLogementFloatDAO.class,
					ValeurCaracteristiqueProduitLogementFloatDAOInterface.class,
					ValeurCaracteristiqueProduitLogementDouble.class,
					ValeurCaracteristiqueProduitLogementDoubleDAO.class,
					ValeurCaracteristiqueProduitLogementDoubleDAOInterface.class,
					ValeurReference.class,
					ValeurCaracteristiqueProduitLogementReference.class,
					ValeurCaracteristiqueProduitLogementReferenceDAOInterface.class,
					ValeurCaracteristiqueProduitLogementReferenceDAO.class,
					ValeurCaracteristiqueProduitLogementDocument.class,
					ValeurCaracteristiqueProduitLogementDocumentDAOInterface.class,
					ValeurCaracteristiqueProduitLogementDocumentDAO.class,
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
					EntityUtil.class,
					AppMessageKeys.class,
					AppConfigUtil.class,
					AppConfigKeys.class,
					CreerModifierUnProduitLogementCtlInterface.class,
					CreerModifierUnProduitLogementCtl.class,
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
	
	
	@Inject
	CreerModifierUnProduitLogementCtlInterface CreerModifierUnProduitLogementCtl ;
	
	//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	
	
	//Test de modification d'u  produit logement avec l'ajout d'une nouvelle caracteristique optionnelle ou usuelle
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testModifierProduitLogementAjouterUneCaracteristique() {
		 //Iniatilisation des entrants
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit05");
		   produitLogement.setDesignation("Château") ;
		   produitLogement.setCode("Somptueux");
		   produitLogement.setVersion(0);
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeimmobilier(programmeImmbilier);
		   
		   		//Ajout de caracteristique de type Booleen: piscine
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet<CaracteristiqueProduitLogement>();
		   
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement.setDesignation("Piscine") ;
		   caracteristiqueProduitLogement.setDescription("Piscine chauffante");
		   caracteristiqueProduitLogement.setValeur(true);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement);
		   
		   produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = false ;
		   
		   //Execution  du test et verification des sortants
		    
		    ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogement, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		    assertEquals("produit05" , rtn.getId());
		    assertEquals(1 , rtn.getCaracteristiqueProduitLogementList().size());
	}
	
	
	//Test de modification d'une caracteristique d'un produit loogement donné
	 
	@Test
	public void testModifierProduitLogementModifiererUneCaracteristique() {
		 //Iniatilisation des entrants
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit03");
		   produitLogement.setDesignation("Villa basse 3 Pièces") ;
		   produitLogement.setCode("Serenite");
		   produitLogement.setVersion(0);
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeimmobilier(programmeImmbilier);
		   
		   		//Modification de caracteristique de type Booleen: piscine de true à false
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet<CaracteristiqueProduitLogement>();
		   
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement.setId("caracteristique15") ;
		   caracteristiqueProduitLogement.setDesignation("caracteristique15") ;
		   caracteristiqueProduitLogement.setValeur(false);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement);
		   
		   caracteristiqueProduitLogementList = Collections.unmodifiableSet(caracteristiqueProduitLogementList);
		   
		   produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = false ;
		   
		   //Execution  du test et verification des sortants
		    
		    ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogement, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		    assertEquals("produit03" , rtn.getId());
		    assertEquals(1 , rtn.getCaracteristiqueProduitLogementList().size());
	}
	
	
	//Tests des exceptions
	
	//Test de creation d'un produit logement sans définir le code
	@Test
	public void testCreerProduitLogementAvecViolationscontraintes() {
		
		 //Iniatilisation des entrants sans définition du code du produit logement (violation de contrainte)
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setDesignation("Villa Duplex 7 Pièces") ;
		   produitLogement.setDescription("Villa Duplex 7 Pièces");
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeimmobilier(programmeImmbilier);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   assertEquals(null , CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogement, true, null, true, locale, loggedInUser, msgList, estCreation));
		   assertFalse (msgList.isEmpty());
	}
	
	
	//Test de creation d'un produit logement rattaché à un programme immobilier non défini
	 
	@Test
	public void testCreerProduitLogementAvecErreursAssociations() {
		
		 //Iniatilisation des entrants avec specification du programme immobilier non definie (erreur association)
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setDesignation("Villa Duplex 7 Pièces") ;
		   produitLogement.setDescription("Villa Duplex 7 Pièces");
		   produitLogement.setCode("GrandLuxe");
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme00");
		   
		   produitLogement.setProgrammeimmobilier(programmeImmbilier);
		  
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   assertEquals(null , CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogement, true, null, true, locale, loggedInUser, msgList,estCreation));
		   assertFalse (msgList.isEmpty());
	}
	
	
	//Test de creation d'un produit logement avec des caracteristiques dont certaines propriétés n'existent pas 
	@Test
	public void testCreerProduitLogementAvecErreursCaracteristiques() {
		
		 //Iniatilisation des entrants 
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setDesignation("Villa Duplex 7 Pièces") ;
		   produitLogement.setDescription("Villa Duplex 7 Pièces");
		   produitLogement.setCode("GrandLuxe");
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeimmobilier(programmeImmbilier);
		   
		   		//Iniatilisation de la liste des caracteristiques sans définition de la propriété
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet<>();
		   
		   		//caracteristique de type Reference: villa
		   
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement.setDesignation("Triplex") ;
		   caracteristiqueProduitLogement.setDescription("Statut");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.villa");
		   
		   caracteristiqueProduitLogement.setValeur(reference);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement);
		  
		   produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   
		   Boolean estCreation = true ;
	
		   //Execution  du test et verification des sortants
		    
		   assertEquals(null , CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogement, true, null, true, locale, loggedInUser, msgList, estCreation));
		   assertFalse (msgList.isEmpty());
	}
	
}	*/
	

