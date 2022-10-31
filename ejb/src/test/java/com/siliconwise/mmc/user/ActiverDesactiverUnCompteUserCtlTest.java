package com.siliconwise.mmc.user;

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
import com.siliconwise.mmc.organisation.IOrganisation;
import com.siliconwise.mmc.organisation.efi.ActiverDesactiverUnEfiCtl;
import com.siliconwise.mmc.organisation.efi.ActiverDesactiverUnEfiCtlInterface;
import com.siliconwise.mmc.organisation.efi.CreerModifierUnEfiCtlInterface;
import com.siliconwise.mmc.organisation.efi.EFi;
import com.siliconwise.mmc.organisation.efi.EfiDAO;
import com.siliconwise.mmc.organisation.efi.EfiDAOInterface;
import com.siliconwise.mmc.organisation.promoteur.ActiverDesactiverUnPromoteurCtl;
import com.siliconwise.mmc.organisation.promoteur.ActiverDesactiverUnPromoteurCtlInterface;
import com.siliconwise.mmc.organisation.promoteur.CreerModifierUnPromoteurCtl;
import com.siliconwise.mmc.organisation.promoteur.CreerModifierUnPromoteurCtlInterface;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
import com.siliconwise.mmc.organisation.promoteur.PromoteurDAO;
import com.siliconwise.mmc.organisation.promoteur.PromoteurDAOInterface;
import com.siliconwise.mmc.produitlogement.CreerModifierUnProduitLogementCtlInterface;
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
import com.siliconwise.mmc.user.ActiverDesactiverUnCompteUserCtl;
import com.siliconwise.mmc.user.CreerModifierUnCompteUserCtl;
import com.siliconwise.mmc.user.CreerModifierUnCompteUserCtlInterface;
import com.siliconwise.mmc.user.User;
import com.siliconwise.mmc.user.UserDAO;
import com.siliconwise.mmc.user.UserDAOInterface;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class ActiverDesactiverUnCompteUserCtlTest {
	
	
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
						DocumentFormat.class,
						HistoriserEventPayload.class,
						Historique.class,
						HistoriqueEventCallback.class,
						EntityUtil.class,
						AppMessageKeys.class,
						AppConfigUtil.class,
						AppConfigKeys.class,
						CreerModifierUnProduitLogementCtlInterface.class,
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
						ProgrammeImmobilierDocumentCtlInterface.class,
						DocumentEntityTransfert.class,
						ActiverDesactiverUnCompteUserCtlInterface.class,
						ActiverDesactiverUnCompteUserCtl.class,
						CreerModifierUnCompteUserCtlInterface.class,
						CreerModifierUnCompteUserCtl.class,
						ActiverDesactiverUnPromoteurCtlInterface.class,
						ActiverDesactiverUnPromoteurCtl.class,
						CreerModifierUnPromoteurCtlInterface.class,
						CreerModifierUnPromoteurCtl.class,
						PromoteurDAOInterface.class,
						PromoteurDAO.class,
						UserDAOInterface.class,
						UserDAO.class,
						IOrganisation.class,
						EFi.class,
						CreerModifierUnEfiCtlInterface.class,
						com.siliconwise.mmc.organisation.efi.CreerModifierUnEfiCtl.class,
						ActiverDesactiverUnEfiCtlInterface.class,
						ActiverDesactiverUnEfiCtl.class,
						EfiDAOInterface.class,
						EfiDAO.class
					
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	ActiverDesactiverUnCompteUserCtlInterface activerDesactiverUnCompteUserCtl ;
	
	private static transient Logger logger = LoggerFactory.getLogger(ActiverDesactiverUnCompteUserCtlTest.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	
	
	
	//Test d'activation d'un compte user sans erreurs
	 
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testActiverUnCompteUser() {
		 
		   //Iniatilisation des entrants
		
		   String idUser = "user01" ;
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		  
		   //Execution  du test et verification des sortants
		    
		   boolean rtn = activerDesactiverUnCompteUserCtl.activerUnCompteUser(idUser, true, null, true, locale, loggedInUser, msgList) ;
		   
		   assertEquals(true , rtn) ;
		   
	}
	
	
	//Test de desactivation d'un compte user sans erreurs
	
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void testDesactiverUnCompteUser() {
		 
		   //Iniatilisation des entrants
		
		   String idUser = "user02" ;
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		  
		   //Execution  du test et verification des sortants
		    
		   boolean rtn = activerDesactiverUnCompteUserCtl.desactiverUnCompteUser(idUser, true, null, true, locale, loggedInUser, msgList) ;
		   
		   assertEquals(true , rtn) ;
		   
	}
	
	
	// Test sur les cas d'exceptions
	 
	 
	//Test d'activation d'un compte user dejé actif

	@Test
	public void testActiverUnCompteUserDejaActive() {
		 
		   //Iniatilisation des entrants
		
		   String idUser = "user01" ;
		   
		   User loggedInUser = new User();
		   
		   loggedInUser.setId("user1");
		   loggedInUser.setNom("alzouma");
		   loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		  
		   //Execution  du test et verification des sortants
		    
		   boolean rtn = activerDesactiverUnCompteUserCtl.activerUnCompteUser(idUser, true, null, true, locale, loggedInUser, msgList) ;
		   
		   assertEquals(false , rtn) ;
		   
	}
	
	
}*/	
	


