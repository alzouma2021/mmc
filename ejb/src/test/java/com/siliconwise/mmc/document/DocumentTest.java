package com.siliconwise.mmc.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
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
import com.siliconwise.common.document.DocumentEntityTransfert;
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
import com.siliconwise.mmc.produitlogement.ProduitLogementDocument;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentCtl;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentCtlInterface;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentDAO;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentDAOInterface;
import com.siliconwise.mmc.produitlogement.ProduitLogementTransfert;
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
public class DocumentTest{
	
	
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
					DocumentEntityTransfert.class
					
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	ProduitLogementDocumentCtlInterface  produitLogementDocumentCtl ;
	
	@Inject DocumentCtlInterface documentCtl ;
	
	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;*/
	
	
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void retounerDocumentParProduitLogementParTypeDocument() {
		 
		
		    //Iniatilisation du chemin relatif
	
		   	String idProduitLogement = "produit01" ;
		   	String typeDocument =   "ref.element.typeValeur.imageConsultation" ;
		  
		   //Execution  du test 
		    
			Document rtn =   produitLogementDocumentCtl
					          .rechercherDocumentParProduitLogementParTypeDocument(
					        		  idProduitLogement, typeDocument, 
					        		  true, null, true, locale, msgList) ;
					           
					       
				  
		   //V??rification du tes
		   
		   assertEquals(" Ceci est un test pour r??cuperer une image de consultation d'un produit logement avec ses metadatas et son contenu." , rtn.getContenu() ) ;
		   
		   assertEquals("Image Consultation d'un produit logement" , rtn.getDesignation() ) ;
		   
	}
	*/
	
	/**
	 * 
	 * Test de modification des metadats d'un document donn??
	 *
	 */
	
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	 public void updateMetaDataDocumentTest() {
		 
		
		 //Initialisation des variables entrantes
		
		 Document entity = new Document();
		 
		 entity.setId("document01");
		 entity.setDesignation("DocumentModifie");
		 entity.setDescription("Modification du document 01");
		 entity.setVersion(0);
		 
		 DocumentFormat format = new DocumentFormat();
		 format.setId("documentformat03");
		 
		 entity.setFormat(format);
		 
		 Reference  typeDocument = new Reference();
		 typeDocument.setId("ref.element.typeValeur.image");
		 
		 entity.setTypeDocument(typeDocument);
		 
	
		 User loggedInUser = new User();
		 
		 //Execution du test 
		 
		 Document rtn = documentCtl
				           .creerModifierDocument(entity, true, null, true, locale, loggedInUser, msgList);
		 
		 
		 //Verification des resultats du test
		 
		 assertEquals("DocumentModifie", rtn.getDesignation() );
		 assertEquals("Modification du document 01", rtn.getDescription());
		 
	 }
	*/
	
	/**
	 * 
	 * Modification du contenu d'un document donn??
	 * 
	 */
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void updateContentDocumentTest() {
		 
		
		 //Initialisation des variables entrantes
		
		 Document entity = new Document();
		 
		 entity.setId("document06");
		 entity.setVersion(0);
		 entity.setContenu("Modification du contenu de l'image de consultation");
		 
		 DocumentFormat format = new DocumentFormat();
		 format.setId("documentformat03");
		 
		 entity.setFormat(format);
		 
		 Reference  typeDocument = new Reference();
		 typeDocument.setId("ref.element.typeValeur.imageConsultation");
		 
		 entity.setTypeDocument(typeDocument);
		 
	
		 User loggedInUser = new User();
		 
		 //Execution du test 
		 
		 String rtn = documentCtl
				         .updateStringContentDocument(
				           entity, true, true,null, true, 
				           locale, loggedInUser, msgList) ;
		 
		 
		 //Verification des resultats du test
		 
		 assertEquals(" Modification du contenu de l'image de consultation", rtn );
		 
	 }*/
	
	/**
	 * Suppression d'un document entier ( Metadatas + Contenu )
	 */
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void supprimerUnDocumentTest() {
		
		//Initialisation des entrants du test
		
		String idDocument = "document06" ;
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph =  true ;
		
		User loggedInUser = new User();
		
		//Execution du test
		
		boolean rtn = documentCtl
				       .supprimer(
				    	 idDocument, mustUpdateExistingNew, 
				    	 namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
		
		//Verification du resultat du test
		
		assertEquals(true , rtn) ;
		
	}*/
	
	/**
	 * 
	 * Recuperation de la liste de formats
	 * 
	 */
	
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void rechercherListeDeFormatsTest() {
		
		//Execution du test 
		
		assertEquals(3 , documentCtl.findAllDocumentFormats(null, true).size()) ;
		
		assertEquals(".txt" , documentCtl.findAllDocumentFormats(null, true).get(0).getExtension()) ;
		
	}*/
	
	
	/**
	 * 
	 * Test d'ajout d'un document ?? une entit?? existante
	 * 
	 * Ajout d'une image ?? un produit logement 
	 * 
	 * Date: 17/08/2021
	 * 
	 */
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void appendEntityDocument() {
		
		 //Initialisation des variables entrantes
		
		 DocumentEntityTransfert entityTransfert = new DocumentEntityTransfert();
		 
		 entityTransfert.setEntityName(Reference.REF_ELEMENT_TYPE_PRODUITLOGEMENT);
		 
		 entityTransfert.setEntityId("produit01");
		 
		
		 Document document = new Document();
		 
		 document.setDesignation("appendProduitLogementDocument") ;
		 document.setDescription("Ajout d'une image ?? un produit logement ") ;
		 document.setContenu("Nouvelle Image");
		 
		 DocumentFormat format = new DocumentFormat();
		 format.setId("documentformat03");
		 
		 document.setFormat(format);
		 
		 Reference  typeDocument = new Reference();
		 typeDocument.setId("ref.element.typeValeur.image");
		 
		 document.setTypeDocument(typeDocument);
		 
		 entityTransfert.setDocument(document);
		 
		 User loggedInUser = new User();
		 
		 //Execution du test 
		 
		 boolean rtn = documentCtl
				         .appendEntityDocument(
				           entityTransfert, true,null, true, 
				           locale, loggedInUser, msgList) ;
		 
		
		assertEquals(true, rtn) ;
		
		
	}*/
	
	/*
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void finBytesContentDocumentTest() {
		
		
		//Initialisation et Execution du test
		
		byte[] rtn = documentCtl
				      .findBytesDocumentById("document13", false, locale, msgList) ;
				       
		//Verification du resultat du test
		
		assertEquals(true , rtn) ;
		
		
	}
	
	
	
}	*/
	


