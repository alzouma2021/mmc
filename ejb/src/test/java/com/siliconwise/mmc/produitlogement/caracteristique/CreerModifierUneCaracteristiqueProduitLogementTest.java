package com.siliconwise.mmc.produitlogement.caracteristique;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.siliconwise.mmc.produitlogement.CreerModifierUnProduitLogementCtl;
import com.siliconwise.mmc.produitlogement.CreerModifierUnProduitLogementCtlInterface;
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
import com.siliconwise.mmc.produitlogement.critere.CritereRechercheProduitLogement;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class CreerModifierUneCaracteristiqueProduitLogementTest {
	
	
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
					CaracteristiqueProduitLogementDAO.class,
					CritereRechercheProduitLogement.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	CreerModifierCaracteristiqueProduitLogementCtlInterface CreerModifierUneCaracterisitqueProduitLogementCtl;
	
	//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	
	
	
	// Debut TEST ValeurCaracteristiqueProduitLogementInteger
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementInteger() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementInteger();
		   
		   caracteristiqueProduitLogement.setDesignation("Nombre Pieces") ;
		   caracteristiqueProduitLogement.setDescription("Confort");
		   caracteristiqueProduitLogement.setValeur(3);
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit01");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete02");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete02" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Confort" , rtn.getDescription());
		   
	}

	
	
	
	@Test
	public void testModifierUneCaracterisitqueProduitLogementInteger() {
		
		 //Iniatilisation des entrants
		
		ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementInteger();
		   
		   caracteristiqueProduitLogement.setId("caracteristique02");
	       caracteristiqueProduitLogement.setDesignation("caracteristique02");
		   caracteristiqueProduitLogement.setVersion(0);
		   caracteristiqueProduitLogement.setValeur(6);
		   caracteristiqueProduitLogement.setDescription("Confort");
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		 
		   produitLogement.setId("produit01");
		   
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete02");
		  
		   
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("caracteristique02" , rtn.getId());
		    assertEquals("Confort" , rtn.getDescription());
		    
	}
	
	// FIN  TEST  ValeurCaracteristiqueProduitLogementInteger
	
	
	// DEBUT  TEST  ValeurCaracteristiqueProduitLogementBoolean
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementDouble() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDouble caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDouble();
		   
		   caracteristiqueProduitLogement.setDesignation("Prix de logement") ;
		   caracteristiqueProduitLogement.setDescription("Montant à payer pour le logement");
		   caracteristiqueProduitLogement.setValeur(1000000D);
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit04");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete03");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete03" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Montant à payer pour le logement" , rtn.getDescription());
		   
	}
	
	
	@Test
	public void testModifierUneCaracteristiqueProduitLogementDouble() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDouble caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDouble();
		   
		   caracteristiqueProduitLogement.setId("caracteristique20") ;
		   caracteristiqueProduitLogement.setDesignation("caracteristique20") ;
		   caracteristiqueProduitLogement.setValeur(1000000000D);
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit05");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete03");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete03" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Château" , rtn.getProduitLogement().getDesignation());
		   
	}
	
	
	//FIN TEST ValeurCaracteristiqueProduitLogementDouble
	
	
	//TEST DEBUT ValeurCaracteristiqueProduitLogementDate
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementDate() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDate caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDate();
		   
		   caracteristiqueProduitLogement.setDesignation("Date de création") ;
		   caracteristiqueProduitLogement.setDescription("Date de début de construction");
		   caracteristiqueProduitLogement.setValeur(LocalDate.parse("2018-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
		   caracteristiqueProduitLogement.setVersion(0);
		   //caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit02");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete04");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete04" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Date de début de construction" , rtn.getDescription());
		   
	}
	
	

	@Test
	public void testModifierUneCaracteristiqueProduitLogementDate() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDate caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDate();
		   
		   caracteristiqueProduitLogement.setId("caracteristique23");
		   caracteristiqueProduitLogement.setDesignation("caracteristique23") ;
		   caracteristiqueProduitLogement.setDescription("Date de début de construction");
		   caracteristiqueProduitLogement.setValeur(LocalDate.parse("2020-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
		   caracteristiqueProduitLogement.setVersion(0);
		   //caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit05");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete04");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete04" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Somptueux" , rtn.getProduitLogement().getCode());
		    assertEquals("Date de début de construction" , rtn.getDescription());
		   
	}
	
	
	//FIn DE TEST ValeurCaracteristiqueProduitLogementDate
	
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementBoolean() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement.setDesignation("Terrain de foot") ;
		   caracteristiqueProduitLogement.setDescription("Terrain de FootBall");
		   caracteristiqueProduitLogement.setValeur(true);
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit01");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete05" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Terrain de FootBall" , rtn.getDescription());
		   
	}
	
	//DEBUT TEST ValeurCaracteristiqueProduitLogementString
	@Test
	//@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerUneCaracteristiqueProduitLogementString() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementString caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementString();
		   
		   caracteristiqueProduitLogement.setDesignation("Localisation") ;
		   caracteristiqueProduitLogement.setDescription("Localisation du produit logement");
		   caracteristiqueProduitLogement.setValeur("TreichVille AV16 RUE25");
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit05");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete06");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete06" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Localisation du produit logement" , rtn.getDescription());
		   
	}
	
	
	public void testModifierUneCaracteristiqueProduitLogementString() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementString caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementString();
		   
		   caracteristiqueProduitLogement.setId("caracteristique12") ;
		   caracteristiqueProduitLogement.setDesignation("caracteristique12") ;
		   caracteristiqueProduitLogement.setDescription("Géolocalisation du produit logement");
		   caracteristiqueProduitLogement.setValeur("Angre Château");
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit01");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete06");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete06" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Villa Duplex 4 Pièces" , rtn.getProduitLogement().getDesignation());
		    assertEquals("Géolocalisation du produit logement" , rtn.getDescription());
		   
	}
	
	//FIN TEST ValeurCaracteristiqueProduitLogementString
	
	//DEBUT TEST ValeurCaracteristiqueProduitLogementReference
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementReference() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement.setDesignation("Type Logement") ;
		   caracteristiqueProduitLogement.setDescription("Le type logement du produit logement");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.villa");
		   
		   caracteristiqueProduitLogement.setValeur(reference);
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit05");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete01");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete01" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Le type logement du produit logement" , rtn.getDescription());
		   
	}
	
	
	@Test
	public void testModifierUneCaracteristiqueProduitLogementReference() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement.setId("caracteristique01");
		   caracteristiqueProduitLogement.setDesignation("caracteristique01") ;
		   caracteristiqueProduitLogement.setDescription("Le type logement du produit logement");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.appartement");
		   
		   caracteristiqueProduitLogement.setValeur(reference);
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit01");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete01");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete01" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Senateur" , rtn.getProduitLogement().getCode());
		    assertEquals("Le type logement du produit logement" , rtn.getDescription());
		   
	}
	
	//FIN TEST ValeurCaracteristiqueProduitLogementReference
	
	
	
	// DEBUT TEST ValeurCaracteristiqueProduitLogementDateTime
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementDateTime() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDateTime caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDateTime();
		   
		   caracteristiqueProduitLogement.setDesignation("Date Time") ;
		   caracteristiqueProduitLogement.setDescription("Date et Heure de l'enregistrement du produit logement");
		   caracteristiqueProduitLogement.setValeur(LocalDateTime.of(2020,12,12 ,12 ,12 ,12));
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit03");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete07");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete07" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Date et Heure de l'enregistrement du produit logement" , rtn.getDescription());
		   
	}
	
	
	

	@Test
	public void testModifierUneCaracteristiqueProduitLogementDateTime() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDateTime caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDateTime();
		   
		   caracteristiqueProduitLogement.setId("caracteristique26") ;
		   caracteristiqueProduitLogement.setDesignation("caracteristique26") ;
		   caracteristiqueProduitLogement.setDescription("Date et Heure de livraison du logement à son client");
		   caracteristiqueProduitLogement.setValeur(LocalDateTime.of(2019,12,12 ,12 ,12 ,12));
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit02");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete07");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete07" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Reve" , rtn.getProduitLogement().getCode());
		    assertEquals("Date et Heure de livraison du logement à son client" , rtn.getDescription());
		   
	}
	
	
	// FIN TEST ValeurCaracteristiqueProduitLogementDateTime
	
	
	
	// DEBUT TEST ValeurCaracteristiqueProduitLogementTime
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementTime() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementTime caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementTime();
		   
		   caracteristiqueProduitLogement.setDesignation("Time") ;
		   caracteristiqueProduitLogement.setDescription("Heure de l'enregistrement du produit logement");
		   caracteristiqueProduitLogement.setValeur(LocalTime.of(12,12));
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit03");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete08");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete08" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Heure de l'enregistrement du produit logement" , rtn.getDescription());
		   
	}
	

	@Test
	public void testModifierUneCaracteristiqueProduitLogementTime() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementTime caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementTime();
		   
		   caracteristiqueProduitLogement.setId("Time01") ;
		   caracteristiqueProduitLogement.setDesignation("Time") ;
		   caracteristiqueProduitLogement.setDescription("Heure de livraison du produit logement");
		   caracteristiqueProduitLogement.setValeur(LocalTime.of(13,12));
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit03");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete08");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals(null, rtn.getProprieteProduitLogement().getId());
		   // assertEquals("Heure de livraison du produit logement" , rtn.getDescription());
		   
	}
	
	// FIN TEST ValeurCaracteristiqueProduitLogementTime
	
	
	//DEBUT TEST ValeurCaracteristiqueProduitLogementDocument
	
	@Test
	public void testCreerUneCaracteristiqueProduitLogementDocument() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDocument caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDocument();
		   
		   caracteristiqueProduitLogement.setDesignation("Document") ;
		   caracteristiqueProduitLogement.setDescription("Image de consultation du produit logement");
		   
		   Document document = new Document();
		   document.setId("document01");
		   
		   caracteristiqueProduitLogement.setValeur(document);
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit01");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete09");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete09" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Image de consultation du produit logement" , rtn.getDescription());
		   
	}
	
	
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testModifierUneCaracteristiqueProduitLogementDocument() {
		
		   //Iniatilisation des entrants
		
		   ValeurCaracteristiqueProduitLogementDocument caracteristiqueProduitLogement = new ValeurCaracteristiqueProduitLogementDocument();
		   
		   
		   caracteristiqueProduitLogement.setId("caracteristique27") ;
		   caracteristiqueProduitLogement.setDesignation("caracteristique27") ;
		   caracteristiqueProduitLogement.setDescription("Video d'observation du dit produit logement");
		   
		   Document document = new Document();
		   document.setId("document02");
		   
		   caracteristiqueProduitLogement.setValeur(document);
		   
		   caracteristiqueProduitLogement.setVersion(0);
		   
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit02");
		   caracteristiqueProduitLogement.setProduitLogement(produitLogement);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete09");
		   caracteristiqueProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    CaracteristiqueProduitLogement rtn = CreerModifierUneCaracterisitqueProduitLogementCtl.creerModifierUneCaracteristiqueProduitLogement(caracteristiqueProduitLogement, true, null, true, locale, loggedInUser, msgList) ;
		   
		    assertEquals("propriete09" , rtn.getProprieteProduitLogement().getId());
		    assertEquals("Reve" , rtn.getProduitLogement().getCode());
		    assertEquals("Video d'observation du dit produit logement" , rtn.getDescription());
		   
	}
	
	//FIN TEST ValeurCaracteristiqueProduitLogementDocument
	
}*/