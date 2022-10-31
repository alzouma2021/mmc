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
import com.siliconwise.mmc.modefinancement.Temperament;
import com.siliconwise.mmc.modefinancement.TemperamentDAO;
import com.siliconwise.mmc.modefinancement.TemperamentDAOInterface;
import com.siliconwise.mmc.modefinancement.Tiers;
import com.siliconwise.mmc.modefinancement.TiersCollecteur;
import com.siliconwise.mmc.modefinancement.TiersCollecteurDAO;
import com.siliconwise.mmc.modefinancement.TiersCollecteurDAOInterface;
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
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVille;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVilleDAO;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVilleDAOInterface;
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
public class CreerModifierUnProduitLogementCtlTest {
	
	
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
					ValeurCaracteristiqueProduitLogementVille.class,
					ValeurCaracteristiqueProduitLogementVilleDAOInterface.class,
					ValeurCaracteristiqueProduitLogementVilleDAO.class,
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
					DocumentCtlInterface.class,
					DocumentDAOInterface.class,
					DocumentCtl.class,
					DocumentDAO.class,
					ProduitLogementTransfert.class,
					TemperamentDAOInterface.class,
					TemperamentDAO.class,
					Temperament.class,
					TiersCollecteurDAOInterface.class,
					TiersCollecteurDAO.class,
					TiersCollecteur.class,
					Tiers.class,
					ProduitLogement.class,
					ProduitLogementDocument.class,
					ProduitLogementDocumentCtlInterface.class,
					ProduitLogementDocumentCtl.class,
					ProduitLogementDocumentDAOInterface.class,
					ProduitLogementDocumentDAO.class,
					ProgrammeImmobilierDocument.class,
					ProgrammeImmobilierDocumentDAOInterface.class,
					ProgrammeImmobilierDocumentDAO.class,
					ProgrammeImmobilierDocumentCtl.class,
					ProgrammeImmobilierDocumentCtlInterface.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	CreerModifierUnProduitLogementCtlInterface CreerModifierUnProduitLogementCtl ;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	
	//Test de creation d'un produit logement sans caracteristiques ni image de conusltation , ni imageList , ni videoList
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerProduitLogementSansCaracteristiques() {
		 
		 //Iniatilisation des entrants
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setDesignation("Villa Duplex 6 Pièces") ;
		   produitLogement.setDescription("Villa Duplex 6 Pièces");
		   produitLogement.setCode("Adjoint");
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeImmobilier(programmeImmbilier);
		   
		   ProduitLogementTransfert produitLogementTransfert = new ProduitLogementTransfert();
		   
		   produitLogementTransfert.setProduitLogement(produitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		   assertEquals("Adjoint" , rtn.getCode());
		   assertEquals("Villa Duplex 6 Pièces" , rtn.getDesignation());
	}
	
	//Test de modification d'un produit logement sans caracterisitiques ni image consultation , ni imageList , ni videoList
	@Test 
	public void testModifierProduitLogementSansCaracteristiques() {
		 //Iniatilisation des entrants
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setId("produit05");
		   produitLogement.setDesignation("Villa Duplex 5 Pièces") ;
		   produitLogement.setCode("Somptueux");
		   produitLogement.setVersion(0);
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeImmobilier(programmeImmbilier);
		   
		   ProduitLogementTransfert produitLogementTransfert = new ProduitLogementTransfert();
		   
		   produitLogementTransfert.setProduitLogement(produitLogement);
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = false ;
		   
		   //Execution  du test et verification des sortants
		    
		    ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		    assertEquals("produit05" , rtn.getId());
		    assertEquals("Villa Duplex 5 Pièces" , rtn.getDesignation());
	}
	
	
	//Test de creation d'un produit logement avec des caracterisitiques définies
	@Test
	public void testCreerProduitLogementAvecCaracteristiques() {
		
		 //Iniatilisation des entrants
		
			//Iniatilisation du produit logement de base
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setDesignation("Villa Triplex 6 Pièces") ;
		   produitLogement.setDescription("Villa Triplex 6 Pièces");
		   produitLogement.setCode("Superieur");
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeImmobilier(programmeImmbilier);
		   
		   	//Iniatilisation de la liste des caracteristiques
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet();
		   
		   		//caracteristique de type Reference: villa
		   
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement1 = new ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement1.setDesignation("Triplex") ;
		   caracteristiqueProduitLogement1.setDescription("Statut");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.villa");
		   
		   caracteristiqueProduitLogement1.setValeur(reference);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete01");
		   caracteristiqueProduitLogement1.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement1);
		   
		   		//caracteristique de type Integer: nombre de pièces
		   
		   ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement2 = new ValeurCaracteristiqueProduitLogementInteger();
		   
		   caracteristiqueProduitLogement2.setDesignation("Nombre Pieces") ;
		   caracteristiqueProduitLogement2.setDescription("Confort");
		   caracteristiqueProduitLogement2.setValeur(6);
		   
		   proprieteProduitLogement.setId("propriete02");
		   caracteristiqueProduitLogement2.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement2);
		   
		   	//caracteristique de type Booleen: piscine
		   
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement3 = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement3.setDesignation("Piscine") ;
		   caracteristiqueProduitLogement3.setDescription("Piscine chauffante");
		   caracteristiqueProduitLogement3.setValeur(true);
		   
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement3.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement3);
		   
		   produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   
		   ProduitLogementTransfert produitLogementTransfert = new ProduitLogementTransfert();
		   
		   produitLogementTransfert.setProduitLogement(produitLogement);
		   
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		   assertEquals("Superieur" , rtn.getCode());
		   assertEquals("Villa Triplex 6 Pièces" , rtn.getDesignation());
		   assertEquals(3 , rtn.getCaracteristiqueProduitLogementList().size());

	}
	*/
	
	
	/**
	 * Test Modifié
	 * 
	 * Creation d'un produit logement avec une image consultation + des caracteristiques
	 * 
	 * Date: 29/07/2021
	 * 
	 */
	
	//Test de creation d'un produit logement : caracteristiques + Image de consultation 
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerProduitLogementAvecImageConsultationAvecCaracteristiques() {
		
		 //Iniatilisation des entrants
		
			//Iniatilisation du produit logement de base
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setDesignation("Villa Triplex 6 Pièces") ;
		   produitLogement.setDescription("Villa Triplex 6 Pièces");
		   produitLogement.setCode("Superieur");
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeImmobilier(programmeImmbilier);
		   
		   	//Iniatilisation de la liste des caracteristiques
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet();
		   
		   		//caracteristique de type Reference: villa
		   
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement1 = new ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement1.setDesignation("Triplex") ;
		   caracteristiqueProduitLogement1.setDescription("Statut");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.villa");
		   
		   caracteristiqueProduitLogement1.setValeur(reference);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete01");
		   caracteristiqueProduitLogement1.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement1);
		   
		   		//caracteristique de type Integer: nombre de pièces
		   
		   ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement2 = new ValeurCaracteristiqueProduitLogementInteger();
		   
		   caracteristiqueProduitLogement2.setDesignation("Nombre Pieces") ;
		   caracteristiqueProduitLogement2.setDescription("Confort");
		   caracteristiqueProduitLogement2.setValeur(6);
		   
		   proprieteProduitLogement.setId("propriete02");
		   caracteristiqueProduitLogement2.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement2);
		   
		   	//caracteristique de type Booleen: piscine
		   
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement3 = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement3.setDesignation("Piscine") ;
		   caracteristiqueProduitLogement3.setDescription("Piscine chauffante");
		   caracteristiqueProduitLogement3.setValeur(true);
		   
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement3.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement3);
		   
		   produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   
		   
		   //Information sur l'image de consultation du produit logement
		   
		   Document imageConsultation  = new Document();
		   
		  
		   imageConsultation.setDesignation("Image Consultation");
		   imageConsultation.setContenu("Creation d'une image de consultation du produit logement");
		   imageConsultation.setDescription("Cette image represente de facon globale le produit logement");
		   
		   DocumentFormat format = new DocumentFormat();
		   format.setId("documentformat03");
		  
		   imageConsultation.setFormat(format);
		   
		   Reference typeDocument = new Reference();
		   typeDocument.setId("ref.element.typeValeur.imageConsultation");
		   
		   imageConsultation.setTypeDocument(typeDocument);
		   
		   
		   //Initialisation de la classe de Transfert de produit logement
		   
		   ProduitLogementTransfert produitLogementTransfert = new ProduitLogementTransfert();
		   
		   produitLogementTransfert.setProduitLogement(produitLogement);
		   
		   produitLogementTransfert.setImageConsultation(imageConsultation);
		   
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		   assertEquals("Superieur" , rtn.getCode());
		   assertEquals("Villa Triplex 6 Pièces" , rtn.getDesignation());
		   assertEquals(3 , rtn.getCaracteristiqueProduitLogementList().size());
		   
		 
	}*/

	
	/**
	 * 
	 * Creation d'un produit logement + une image consultation + des caracteristiques + une liste d'images
	 * 
	 * Date: 29/07/2021
	 * 
	 */
	
    //Tets de creation d'un produit logement : caracteristiques + Image de consulltation + une imageList
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerProduitLogementAvecImageConsultationAvecCaracteristiquesAvecImageList() {
		
		 //Iniatilisation des entrants
		
			//Iniatilisation du produit logement de base
		
		   ProduitLogement produitLogement = new ProduitLogement();
		   
		   produitLogement.setDesignation("Villa Triplex 6 Pièces") ;
		   produitLogement.setDescription("Villa Triplex 6 Pièces");
		   produitLogement.setCode("Superieur");
		   
		   ProgrammeImmobilier programmeImmbilier = new ProgrammeImmobilier();
		   
		   programmeImmbilier.setId("programme01");
		   
		   produitLogement.setProgrammeImmobilier(programmeImmbilier);
		   
		   	//Iniatilisation de la liste des caracteristiques
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet();
		   
		   
		   //caracteristique de type Reference: villa
		   
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement1 = new ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement1.setDesignation("Triplex") ;
		   caracteristiqueProduitLogement1.setDescription("Statut");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.villa");
		   
		   caracteristiqueProduitLogement1.setValeur(reference);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete01");
		   caracteristiqueProduitLogement1.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement1);
		   
		   	//caracteristique de type Integer: nombre de pièces
		   
		   ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement2 = new ValeurCaracteristiqueProduitLogementInteger();
		   
		   caracteristiqueProduitLogement2.setDesignation("Nombre Pieces") ;
		   caracteristiqueProduitLogement2.setDescription("Confort");
		   caracteristiqueProduitLogement2.setValeur(6);
		   
		   proprieteProduitLogement.setId("propriete02");
		   caracteristiqueProduitLogement2.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement2);
		   
		   	//caracteristique de type Booleen: piscine
		   
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement3 = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement3.setDesignation("Piscine") ;
		   caracteristiqueProduitLogement3.setDescription("Piscine chauffante");
		   caracteristiqueProduitLogement3.setValeur(true);
		   
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement3.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement3);
		   
		   produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   
		   
		   //Information sur l'image de consultation du produit logement
		   
		   Document imageConsultation =  new Document();
		   
		   imageConsultation.setDesignation("ImageConsultation");
		   
		   imageConsultation.setContenu("Ceci est l'image de consultation");
		   
		   imageConsultation.setDescription("ImageConsultation");
		   
		   DocumentFormat format = new DocumentFormat();
		   
		   format.setId("documentformat03");
		  
		   imageConsultation.setFormat(format);
		   
		   Reference typeDocument = new Reference();
		   typeDocument.setId("ref.element.typeValeur.imageConsultation");
		   
		   imageConsultation.setTypeDocument(typeDocument);
		   
		   //Initialisation des images list
		   
		   //Image liste 1
		   
		   Document document1 =  new Document();
		   
		   document1.setDesignation("ImageList1");
		   document1.setContenu("Première image de la liste");
		   document1.setDescription("ImageList1");
		   
		   DocumentFormat format1 = new DocumentFormat();
		   format1.setId("documentformat03");
		  
		   document1.setFormat(format1);
		   
		   Reference typeDocument1 = new Reference();
		   typeDocument1.setId("ref.element.typeValeur.image");
		   
		   document1.setTypeDocument(typeDocument1);
		   
		   
		   //Image liste 2
		   
		   Document document2 =  new Document();
		   
		   document2.setDesignation("ImageList2");
		   document2.setContenu("deuxième image de la liste");
		   document2.setDescription("ImageList2");
		   
		   DocumentFormat format2 = new DocumentFormat();
		   format2.setId("documentformat03");
		  
		   document2.setFormat(format2);
		   
		   Reference typeDocument2 = new Reference();
		   typeDocument2.setId("ref.element.typeValeur.image");
		   
		   document2.setTypeDocument(typeDocument2);
		   
		   
		   //Image liste 3
		   
		   Document document3 =  new Document();
		   
		   document3.setDesignation("ImageList3");
		   document3.setContenu("troisième image de la liste");
		   document3.setDescription("ImageList3");
		   
		   DocumentFormat format3 = new DocumentFormat();
		   format3.setId("documentformat03");
		  
		   document3.setFormat(format3);
		   
		   Reference typeDocument3 = new Reference();
		   typeDocument3.setId("ref.element.typeValeur.image");
		   
		   document3.setTypeDocument(typeDocument3);
		   
		   //Iniatialisation d'une collection Set d'image
		   
		   Set<Document> DocumentList = new HashSet<Document> ();
		   
		   DocumentList.add(document1);
		   DocumentList.add(document2);
		   DocumentList.add(document3);
		 
		   
		   //Initialisation de la classe de Transfert de produit logement
		  
		   ProduitLogementTransfert produitLogementTransfert = new ProduitLogementTransfert();
		   
		   produitLogementTransfert.setProduitLogement(produitLogement);
		   
		   produitLogementTransfert.setImagesList(DocumentList);
		   
		   produitLogementTransfert.setImageConsultation(imageConsultation);
		   
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		   assertEquals("Superieur" , rtn.getCode());
		   assertEquals("Villa Triplex 6 Pièces" , rtn.getDesignation());
		   assertEquals(3 , rtn.getCaracteristiqueProduitLogementList().size());
		   
		 
	}*/
	
	/**
	 * 
	 * Creation d'un produit logement sans image consultation
	 * avec une liste d'image
	 * 
	 */
    //
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerProduitLogementSansImageConsultatioAvecImageList() {
		
		 //Iniatilisation des entrants
		
			//Iniatilisation du produit logement de base
		
		   ProduitLogement produitLogement1 = new ProduitLogement();
		   
		   produitLogement1.setDesignation("Villa Triplex 7 Pièces") ;
		   produitLogement1.setDescription("Villa Triplex 7 Pièces");
		   produitLogement1.setCode("Superieur01");
		   
		   ProgrammeImmobilier programmeImmbilier1 = new ProgrammeImmobilier();
		   
		   programmeImmbilier1.setId("programme01");
		   
		   produitLogement1.setProgrammeImmobilier(programmeImmbilier1);
		   
		   	//Iniatilisation de la liste des caracteristiques
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet();
		   
		   
		   //caracteristique de type Reference: villa
		   
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement1 = new ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement1.setDesignation("Triplex") ;
		   caracteristiqueProduitLogement1.setDescription("Statut");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.villa");
		   
		   caracteristiqueProduitLogement1.setValeur(reference);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete01");
		   caracteristiqueProduitLogement1.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement1);
		   
		   	//caracteristique de type Integer: nombre de pièces
		   
		   ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement2 = new ValeurCaracteristiqueProduitLogementInteger();
		   
		   caracteristiqueProduitLogement2.setDesignation("Nombre Pieces") ;
		   caracteristiqueProduitLogement2.setDescription("Confort");
		   caracteristiqueProduitLogement2.setValeur(6);
		   
		   proprieteProduitLogement.setId("propriete02");
		   caracteristiqueProduitLogement2.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement2);
		   
		   	//caracteristique de type Booleen: piscine
		   
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement3 = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement3.setDesignation("Piscine") ;
		   caracteristiqueProduitLogement3.setDescription("Piscine chauffante");
		   caracteristiqueProduitLogement3.setValeur(true);
		   
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement3.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement3);
		   
		    ///produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   
		   
		   //Information sur l'image de consultation du produit logement
		   
		   Document imageConsultation = new Document();
		   
		   imageConsultation.setDesignation("ImageConsultation");
		   
		   imageConsultation.setContenu("Ceci est l'image de consultation");
		   
		   imageConsultation.setDescription("ImageConsultation");
		   
		   DocumentFormat format = new DocumentFormat();
		   format.setId("documentformat03");
		  
		   imageConsultation.setFormat(format);
		   
		   Reference typeDocument = new Reference();
		   typeDocument.setId("ref.element.typeValeur.image");
		   
		   imageConsultation.setTypeDocument(typeDocument);
		   
		   //Initialisation des images list
		   
		   //Image liste 4
		   
		   Document document4 =  new Document();
		   
		   document4.setDesignation("ImageList1");
		   document4.setContenu("Première image de la liste de test2");
		   document4.setDescription("ImageList1");
		   
		   DocumentFormat format4 = new DocumentFormat();
		   format4.setId("documentformat03");
		  
		   document4.setFormat(format4);
		   
		   Reference typeDocument4 = new Reference();
		   typeDocument4.setId("ref.element.typeValeur.image");
		   
		   document4.setTypeDocument(typeDocument4);
		   
		   
		   //Image liste 5
		   
		   Document document5 =  new Document();
		   
		   document5.setDesignation("ImageList2");
		   document5.setContenu("deuxième image de la liste de test2");
		   document5.setDescription("ImageList2");
		   
		   DocumentFormat format5 = new DocumentFormat();
		   format5.setId("documentformat03");
		  
		   document5.setFormat(format5);
		   
		   Reference typeDocument5 = new Reference();
		   typeDocument5.setId("ref.element.typeValeur.image");
		   
		   document5.setTypeDocument(typeDocument5);
		   
		   
		   //Image liste 3
		   
		   Document document6 =  new Document();
		   
		   document6.setDesignation("ImageList3");
		   document6.setContenu("troisième image de la liste de test2");
		   document6.setDescription("ImageList3");
		   
		   DocumentFormat format6 = new DocumentFormat();
		   format6.setId("documentformat03");
		  
		   document6.setFormat(format6);
		   
		   Reference typeDocument6 = new Reference();
		   typeDocument6.setId("ref.element.typeValeur.image");
		   
		   document6.setTypeDocument(typeDocument6);
		   
		   //Iniatialisation d'une collection Set d'image
		   
		   Set<Document> DocumentList1 = new HashSet();
		   
		   DocumentList1.add(document4);
		   DocumentList1.add(document5);
		   DocumentList1.add(document6);
		  
		   
		   //Initialisation de la classe de Transfert de produit logement
		  
		   
		   ProduitLogementTransfert produitLogementTransfert1 = new ProduitLogementTransfert();
		   
		   produitLogementTransfert1.setProduitLogement(produitLogement1);
		   
		   produitLogementTransfert1.setImagesList(DocumentList1);
		   
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert1, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		   assertEquals("Superieur01" , rtn.getCode());
		   assertEquals("Villa Triplex 7 Pièces" , rtn.getDesignation());
		   assertEquals(null , rtn.getCaracteristiqueProduitLogementList());
		   
	}*/
	
	
	/**
	 * 
	 * Creation d'un produit logement : Caracteristiques +  une liste de videos + une image de consultation
	 * 
	 */
    //
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testCreerProduitLogementAvecVideoList() {
		
		 //Iniatilisation des entrants
		
			//Iniatilisation du produit logement de base
		
		   ProduitLogement produitLogement1 = new ProduitLogement();
		   
		   produitLogement1.setDesignation("Villa Triplex 8 Pièces") ;
		   produitLogement1.setDescription("Villa Triplex 8 Pièces");
		   produitLogement1.setCode("Superieur02");
		   
		   ProgrammeImmobilier programmeImmbilier1 = new ProgrammeImmobilier();
		   
		   programmeImmbilier1.setId("programme02");
		   
		   produitLogement1.setProgrammeImmobilier(programmeImmbilier1);
		   
		   	//Iniatilisation de la liste des caracteristiques
		   
		   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet();
		   
		   
		   //caracteristique de type Reference: villa
		   
		   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement1 = new 				      ValeurCaracteristiqueProduitLogementReference();
		   
		   caracteristiqueProduitLogement1.setDesignation("Triplex") ;
		   caracteristiqueProduitLogement1.setDescription("Statut");
		   
		   Reference reference = new Reference();
		   reference.setId("ref.element.valeur.villa");
		   
		   caracteristiqueProduitLogement1.setValeur(reference);
		   
		   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
		   proprieteProduitLogement.setId("propriete01");
		   caracteristiqueProduitLogement1.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement1);
		   
		   	//caracteristique de type Integer: nombre de pièces
		   
		   ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement2 = new ValeurCaracteristiqueProduitLogementInteger();
		   
		   caracteristiqueProduitLogement2.setDesignation("Nombre Pieces") ;
		   caracteristiqueProduitLogement2.setDescription("Confort");
		   caracteristiqueProduitLogement2.setValeur(6);
		   
		   proprieteProduitLogement.setId("propriete02");
		   caracteristiqueProduitLogement2.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement2);
		   
		   	//caracteristique de type Booleen: piscine
		   
		   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement3 = new ValeurCaracteristiqueProduitLogementBoolean();
		   
		   caracteristiqueProduitLogement3.setDesignation("Piscine") ;
		   caracteristiqueProduitLogement3.setDescription("Piscine chauffante");
		   caracteristiqueProduitLogement3.setValeur(true);
		   
		   proprieteProduitLogement.setId("propriete05");
		   caracteristiqueProduitLogement3.setProprieteProduitLogement(proprieteProduitLogement);
		   
		   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement3);
		   
		    ///produitLogement.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
		   
		   
		   
		   //Information sur l'image de consultation du produit logement
		   
		   Document  imageConsultation = new Document();
		   
		   imageConsultation.setDesignation("ImageConsultation");
		   
		   imageConsultation.setContenu("Ceci est l'image de consultation");
		   
		   imageConsultation.setDescription("ImageConsultation");
		   
		   DocumentFormat format = new DocumentFormat();
		   
		   format.setId("documentformat03");
		  
		   imageConsultation.setFormat(format);
		   
		   Reference typeDocument = new Reference();
		   typeDocument.setId("ref.element.typeValeur.imageConsultation");
		   
		   imageConsultation.setTypeDocument(typeDocument);
	
		   
		   //Initialisation des images list
		   
		   //Image liste 4
		   
		   Document document4 =  new Document();
		   
		   document4.setDesignation("ImageList1");
		   document4.setContenu("Première image de la liste de test2");
		   document4.setDescription("ImageList1");
		   
		   DocumentFormat format4 = new DocumentFormat();
		   format4.setId("documentformat03");
		  
		   document4.setFormat(format4);
		   
		   Reference typeDocument4 = new Reference();
		   typeDocument4.setId("ref.element.typeValeur.image");
		   
		   document4.setTypeDocument(typeDocument4);
		   
		   
		   //Image liste 5
		   
		   Document document5 =  new Document();
		   
		   document5.setDesignation("ImageList2");
		   document5.setContenu("deuxième image de la liste de test2");
		   document5.setDescription("ImageList2");
		   
		   DocumentFormat format5 = new DocumentFormat();
		   format5.setId("documentformat03");
		  
		   document5.setFormat(format5);
		   
		   Reference typeDocument5 = new Reference();
		   typeDocument5.setId("ref.element.typeValeur.image");
		   
		   document5.setTypeDocument(typeDocument5);
		   
		   
		   //Image liste 3
		   
		   Document document6 =  new Document();
		   
		   document6.setDesignation("ImageList3");
		   document6.setContenu("troisième image de la liste de test2");
		   document6.setDescription("ImageList3");
		   
		   DocumentFormat format6 = new DocumentFormat();
		   format6.setId("documentformat03");
		  
		   document6.setFormat(format6);
		   
		   Reference typeDocument6 = new Reference();
		   typeDocument6.setId("ref.element.typeValeur.image");
		   
		   document6.setTypeDocument(typeDocument6);
		   
		   //Iniatialisation d'une collection Set d'image
		   
		   Set<Document> ImageList1 = new HashSet();
		   
		   ImageList1.add(document4);
		   ImageList1.add(document5);
		   ImageList1.add(document6);
		   
		   
		   
		   //Initialisation de la liste des videos
		   
		   
		   
		   //Image liste 7
		   
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
		   
		 
		   //Initialisation de la classe de Transfert de produit logement
		  
		   
		   ProduitLogementTransfert produitLogementTransfert = new ProduitLogementTransfert();
		   
		   produitLogementTransfert.setProduitLogement(produitLogement1);
		   
		   produitLogementTransfert.setImageConsultation(imageConsultation);
		   
		   //produitLogementTransfert1.setImagesList(ImageList1)
		   
		   produitLogementTransfert.setVideosList(VideoList1);
		   
		 
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		   Boolean estCreation = true ;
		   
		   //Execution  du test et verification des sortants
		    
		   ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
		   
		   assertEquals("Superieur02" , rtn.getCode());
		   assertEquals("Villa Triplex 8 Pièces" , rtn.getDesignation());
		   assertEquals(null , rtn.getCaracteristiqueProduitLogementList());
		   
		}*/
	
		
		/**
		 * Creation d'un produit logement avec :
		 * 
		 * Une image de consultation
		 * Des caracteristiques
		 * liste d'image
		 * liste de videos
		 * 
		 */
	
	/*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public void testCreerProduitLogementAvecUneImageConsultationAvecUneImageListAvecVideoList() {
			
			 //Iniatilisation des entrants
			
				//Iniatilisation du produit logement de base
			
			   ProduitLogement produitLogement1 = new ProduitLogement();
			   
			   produitLogement1.setDesignation("Villa Triplex 8 Pièces") ;
			   produitLogement1.setDescription("Villa Triplex 8 Pièces");
			   produitLogement1.setCode("Superieur02");
			   
			   ProgrammeImmobilier programmeImmbilier1 = new ProgrammeImmobilier();
			   
			   programmeImmbilier1.setId("programme02");
			   
			   produitLogement1.setProgrammeImmobilier(programmeImmbilier1);
			   
			   	//Iniatilisation de la liste des caracteristiques
			   
			   Set<CaracteristiqueProduitLogement> caracteristiqueProduitLogementList = new HashSet();
			   
			   
			   //caracteristique de type Reference: villa
			   
			   ValeurCaracteristiqueProduitLogementReference caracteristiqueProduitLogement1 = new 				      ValeurCaracteristiqueProduitLogementReference();
			   
			   caracteristiqueProduitLogement1.setDesignation("Triplex") ;
			   caracteristiqueProduitLogement1.setDescription("Statut");
			   
			   Reference reference = new Reference();
			   reference.setId("ref.element.valeur.villa");
			   
			   caracteristiqueProduitLogement1.setValeur(reference);
			   
			   ProprieteProduitLogement  proprieteProduitLogement = new ProprieteProduitLogement();
			   proprieteProduitLogement.setId("propriete01");
			   caracteristiqueProduitLogement1.setProprieteProduitLogement(proprieteProduitLogement);
			   
			   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement1);
			   
			   	//caracteristique de type Integer: nombre de pièces
			   
			   ValeurCaracteristiqueProduitLogementInteger caracteristiqueProduitLogement2 = new ValeurCaracteristiqueProduitLogementInteger();
			   
			   caracteristiqueProduitLogement2.setDesignation("Nombre Pieces") ;
			   caracteristiqueProduitLogement2.setDescription("Confort");
			   caracteristiqueProduitLogement2.setValeur(6);
			   
			   proprieteProduitLogement.setId("propriete02");
			   caracteristiqueProduitLogement2.setProprieteProduitLogement(proprieteProduitLogement);
			   
			   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement2);
			   
			   	//caracteristique de type Booleen: piscine
			   
			   ValeurCaracteristiqueProduitLogementBoolean caracteristiqueProduitLogement3 = new ValeurCaracteristiqueProduitLogementBoolean();
			   
			   caracteristiqueProduitLogement3.setDesignation("Piscine") ;
			   caracteristiqueProduitLogement3.setDescription("Piscine chauffante");
			   caracteristiqueProduitLogement3.setValeur(true);
			   
			   proprieteProduitLogement.setId("propriete05");
			   caracteristiqueProduitLogement3.setProprieteProduitLogement(proprieteProduitLogement);
			   
			   caracteristiqueProduitLogementList.add(caracteristiqueProduitLogement3);
			   
			   produitLogement1.setCaracteristiqueProduitLogementList(caracteristiqueProduitLogementList);
			   
			   
			   
			   //Information sur l'image de consultation du produit logement
			   
			   Document imageConsultation = new Document();
			   
			   imageConsultation.setDesignation("ImageConsultation");
			   
			   imageConsultation.setContenu("Ceci est l'image de consultation");
			   
			   imageConsultation.setDescription("ImageConsultation");
			   
			   DocumentFormat format = new DocumentFormat();
			   format.setId("documentformat03");
			  
			   imageConsultation.setFormat(format);
			   
			   Reference typeDocument = new Reference();
			   
			   typeDocument.setId("ref.element.typeValeur.imageConsultation");
			   
			   imageConsultation.setTypeDocument(typeDocument);
			   
			   //Initialisation des images list
			   
			   //Image liste 4
			   
			   Document document4 =  new Document();
			   
			   document4.setDesignation("ImageList1");
			   document4.setContenu("Première image de la liste de test2");
			   document4.setDescription("ImageList1");
			   
			   DocumentFormat format4 = new DocumentFormat();
			   format4.setId("documentformat03");
			  
			   document4.setFormat(format4);
			   
			   Reference typeDocument4 = new Reference();
			   typeDocument4.setId("ref.element.typeValeur.image");
			   
			   document4.setTypeDocument(typeDocument4);
			   
			   
			   //Image liste 5
			   
			   Document document5 =  new Document();
			   
			   document5.setDesignation("ImageList2");
			   document5.setContenu("deuxième image de la liste de test2");
			   document5.setDescription("ImageList2");
			   
			   DocumentFormat format5 = new DocumentFormat();
			   format5.setId("documentformat03");
			  
			   document5.setFormat(format5);
			   
			   Reference typeDocument5 = new Reference();
			   typeDocument5.setId("ref.element.typeValeur.image");
			   
			   document5.setTypeDocument(typeDocument5);
			   
			   
			   //Image liste 3
			   
			   Document document6 =  new Document();
			   
			   document6.setDesignation("ImageList3");
			   document6.setContenu("troisième image de la liste de test2");
			   document6.setDescription("ImageList3");
			   
			   DocumentFormat format6 = new DocumentFormat();
			   format6.setId("documentformat03");
			  
			   document6.setFormat(format6);
			   
			   Reference typeDocument6 = new Reference();
			   typeDocument6.setId("ref.element.typeValeur.image");
			   
			   document6.setTypeDocument(typeDocument6);
			   
			   //Iniatialisation d'une collection Set d'image
			   
			   Set<Document> ImageList1 = new HashSet();
			   
			   ImageList1.add(document4);
			   ImageList1.add(document5);
			   ImageList1.add(document6);
			   
			   
			   
			   //Initialisation de la liste des videos
			   
			   //Image liste 7
			   
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
			   
			 
			   //Initialisation de la classe de Transfert de produit logement
			  
			   
			   ProduitLogementTransfert produitLogementTransfert = new ProduitLogementTransfert();
			   
			   produitLogementTransfert.setProduitLogement(produitLogement1);
			   
			   produitLogementTransfert.setImagesList(ImageList1);
			   
			   produitLogementTransfert.setVideosList(VideoList1);
			   
			   produitLogementTransfert.setImageConsultation(imageConsultation);
			   
			   
			   User loggedInUser = new User();
			   
			   loggedInUser.setId("user1");
			   loggedInUser.setNom("alzouma");
			   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
			   
			   Boolean estCreation = true ;
			   
			   //Execution  du test et verification des sortants
			    
			   ProduitLogement rtn = CreerModifierUnProduitLogementCtl.creerModifierUnProduitLogement(produitLogementTransfert, true, null, true, locale, loggedInUser, msgList,estCreation) ;
			   
			   assertEquals("Superieur02" , rtn.getCode());
			   assertEquals("Villa Triplex 8 Pièces" , rtn.getDesignation());
			   assertEquals(3 , rtn.getCaracteristiqueProduitLogementList().size());
			   
			   
			}
		
}	*/
	


