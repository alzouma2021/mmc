package com.siliconwise.mmc.programmeimmmobilier.caracteristiquedemandereservation;

import static org.junit.Assert.assertEquals;


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
import com.siliconwise.common.document.DocumentCtl;
import com.siliconwise.common.document.DocumentCtlInterface;
import com.siliconwise.common.document.DocumentDAO;
import com.siliconwise.common.document.DocumentDAOInterface;
import com.siliconwise.common.document.DocumentEntityTransfert;
import com.siliconwise.common.document.DocumentFormat;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.event.historique.History;
import com.siliconwise.common.event.historique.History.HistoryEventType;
import com.siliconwise.common.event.historique.HistoryDAO;
import com.siliconwise.common.event.historique.HistoryDAOInterface;
import com.siliconwise.common.event.historique.HistoryEventCallback;
import com.siliconwise.common.event.historique.HistoryEventPayload;
import com.siliconwise.common.event.historique.HistoryEventUtil;
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
import com.siliconwise.mmc.common.entity.EntityInitializer;
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
import com.siliconwise.mmc.modefinancement.CreerModifierModeFinancementCtlInterface;
import com.siliconwise.mmc.modefinancement.ModeFinancement;
import com.siliconwise.mmc.modefinancement.ModeFinancementDAO;
import com.siliconwise.mmc.modefinancement.ModeFinancementDAOInterface;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituationDAO;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituationDAOInterface;
import com.siliconwise.mmc.modefinancement.RechercherModeFinancementCtl;
import com.siliconwise.mmc.modefinancement.RechercherModeFinancementCtlInterface;
import com.siliconwise.mmc.modefinancement.SupprimerModeFinancementCtl;
import com.siliconwise.mmc.modefinancement.SupprimerModeFinancementCtlInterface;
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
import com.siliconwise.mmc.security.*;
import com.siliconwise.mmc.oldSecurity.LoginPasswordToken;
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
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDAO;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDAOInterface;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocument;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentCtl;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentCtlInterface;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentDAO;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentDAOInterface;
import com.siliconwise.mmc.programmeimmobilier.SupprimerUnProgrammeImmobilierCtl;
import com.siliconwise.mmc.programmeimmobilier.SupprimerUnProgrammeImmobilierCtlInterface;
import com.siliconwise.mmc.programmeimmobilier.caracteristiquedemandereservationlogement.*;
import com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement.AssocierTypeDocumentDemandeReservationLogementCtlInterface;
import com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement.AssocierTypeDocumentDemandeReservationLogementCtl;
import com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement.TypeDocumentDemandeReservationLogement;
import com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement.TypeDocumentDemandeReservationLogementDAO;
import com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement.TypeDocumentDemandeReservationLogementDAOInterface;
import com.siliconwise.mmc.user.* ;

import kong.unirest.UnirestException;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class associerCaracteristiqueDemandeReservationLogementTest {
	

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
						EfiDAO.class,
						AssocierCaracteristiqueDemandeReservationLogementCtl.class,
						AssocierCaracteristiqueDemandeReservationLogementCtlInterface.class,
						AssocierTypeDocumentDemandeReservationLogementCtl.class,
						AssocierTypeDocumentDemandeReservationLogementCtlInterface.class,
						CaracteristiqueDemandeReservationLogementDAOInterface.class,
						TypeDocumentDemandeReservationLogementDAOInterface.class,
						TypeDocumentDemandeReservationLogementDAO.class,
						CaracteristiqueDemandeReservationLogementDAO.class,
						CaracteristiqueDemandeReservationLogement.class,
						TypeDocumentDemandeReservationLogement.class,
						CreerModifierModeFinancementCtlInterface.class,
						EmailService.class,
						SupprimerModeFinancementCtlInterface.class,
						SupprimerModeFinancementCtl.class,
						RechercherModeFinancementCtlInterface.class,
						RechercherModeFinancementCtl.class,
						DocumentEntityTransfert.class,
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class,
						ModeFinancementDAOInterface.class,
						ModeFinancementDAO.class,
						ModeFinancement.class,
						SessionBag.class,
						EmailServiceException.class,
						SecurityService.class,
						CodeConfirmationDAOInterface.class,
						CodeConfirmationDAO.class,
						HistoryDAOInterface.class,
						UnirestException.class,
						EntityInitializer.class,
						LoginPasswordToken.class,
						SupprimerUnProgrammeImmobilierCtlInterface.class,
						SupprimerUnProgrammeImmobilierCtl.class,
						ProgrammeImmobilierDAOInterface.class,
						ProgrammeImmobilierDAO.class,
						HistoryEventPayload.class,
						CodeConfirmation.class,
						CreditBancaireDAOInterface.class,
						CreditBancaireDAO.class,
						CreditBancaire.class,
						PallierComptantSurSituationDAOInterface.class,
						PallierComptantSurSituationDAO.class,
						PallierComptantSurSituation.class,
						HistoryEventType .class,
						HistoryEventUtil.class,
						History.class,
						HistoryDAOInterface.class,
						HistoryDAO.class,
						HistoryEventCallback.class,
						HistoryEventPayload.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject 
    AssocierCaracteristiqueDemandeReservationLogementCtlInterface  creerModifierCaracteristiqueDemandeReservationLogementCtl ;
	
	//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	
	//Test d'associer de deux  caracteristiques demande Reservation logement
	
	
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void associerCaracteristiqueDemandeReservationLogementSansErreur() {
		
		
		
		   //Iniatilisation des entrants
		
			//Caracteristique Fonction
		
			CaracteristiqueDemandeReservationLogement caracteristique1 = new CaracteristiqueDemandeReservationLogement();
	   
			Reference typeCaracteristique = new Reference();
			typeCaracteristique.setId("ref.element.typeCaracteristiqueDemande.Residence");
			
			caracteristique1.setTypeCaracteristique(typeCaracteristique);
	  
	   
			ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
			programmeImmobilier.setId("programme01") ;
			programmeImmobilier.setCode("CCD");
			
			caracteristique1.setProgrammeImmobilier(programmeImmobilier);
			
			
			//Caracteristique Residence permanente
		
			CaracteristiqueDemandeReservationLogement caracteristique2 = new CaracteristiqueDemandeReservationLogement();
			
	   
			Reference typeCaracteristique1 = new Reference();
			typeCaracteristique1.setId("ref.element.typeCaracteristiqueDemande.Fonction");
			
			caracteristique2.setTypeCaracteristique(typeCaracteristique1);
	  
	   
			ProgrammeImmobilier programmeImmobilier1 = new ProgrammeImmobilier();
			programmeImmobilier1.setId("programme01") ;
			programmeImmobilier1.setCode("CCD");
			
			caracteristique2.setProgrammeImmobilier(programmeImmobilier1);
			
			//Liste de caracteristiques de demande de réservation logement
			
			List<CaracteristiqueDemandeReservationLogement> entityList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
			
			entityList.add(caracteristique1);
			entityList.add(caracteristique2);
		   
		    User loggedInUser = new User();
		   
		    loggedInUser.setId("user1");
		    loggedInUser.setNom("alzouma");
		    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		   
		    //Execution  du test et verification des sortants
		    
		    List<CaracteristiqueDemandeReservationLogement> rtnList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
		   	  
		    
		    rtnList =  creerModifierCaracteristiqueDemandeReservationLogementCtl
		    		          .associerCaracteristiqueDemandeReservationLogementList(entityList, true, null, true, locale, loggedInUser, msgList);
		    
		    
		    //Vérification de la taille de la liste retournée
		    
		    assertEquals(2 , rtnList.size())  ;
		    
		    //Vérification de la designation de la 1ere caracteristique créée
		    
		    assertEquals("Residence  de l'acquereur" , rtnList.get(0).getTypeCaracteristique().getDesignation())  ;
		    
		}
	
	
	
		//Test d'associer une caracteristique demande Reservation logement
	
		@Test
		public void testAssocierCaracteristiqueDemandeReservationLogementSansErreur1() {
			
			
			
			   //Iniatilisation des entrants
			
				//Caracteristique Fonction
			
				CaracteristiqueDemandeReservationLogement caracteristique1 = new CaracteristiqueDemandeReservationLogement();
				
				Reference typeCaracteristique2 = new Reference();
				typeCaracteristique2.setId("ref.element.typeCaracteristiqueDemande.Activite");
				
				caracteristique1.setTypeCaracteristique(typeCaracteristique2);
		  
		   
				ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
				programmeImmobilier.setId("programme01") ;
				programmeImmobilier.setCode("CCD");
				
				caracteristique1.setProgrammeImmobilier(programmeImmobilier);
				
				
				//Liste de caracteristiques de demande de réservation logement
				
				List<CaracteristiqueDemandeReservationLogement> entityList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
				
				entityList.add(caracteristique1);
			   
			    User loggedInUser = new User();
			   
			    loggedInUser.setId("user1");
			    loggedInUser.setNom("alzouma");
			    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
			   
			    //Execution  du test et verification des sortants
			    
			    List<CaracteristiqueDemandeReservationLogement> rtnList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
			   	  
			    
			    rtnList =  creerModifierCaracteristiqueDemandeReservationLogementCtl
			    		          .associerCaracteristiqueDemandeReservationLogementList(entityList, true, null, true, locale, loggedInUser, msgList);
			    
			    
			    //Vérification de la taille de la liste retournée
			    
			    assertEquals(1 , rtnList.size())  ;
			    
			    //Vérification de la designation de la 1ere caracteristique créée
			    
			    assertEquals("Activité  de l'acquereur" , rtnList.get(0).getTypeCaracteristique().getDesignation())  ;
			    
		}
		
			
			//Association d'une caracteristique d'une demande de reservation logement pour un programme immmobilier
			//qui n'existe  pas 
			
			@Test
			public void testAssocierCaracteristiqueDemandeReservationLogementSansProgrammeImmobilier() {
				
				
				
				   //Iniatilisation des entrants
				
					//Caracteristique Fonction
				
					CaracteristiqueDemandeReservationLogement caracteristique1 = new CaracteristiqueDemandeReservationLogement();
			
			   
					Reference typeCaracteristique4 = new Reference();
					typeCaracteristique4.setId("ref.element.typeCaracteristiqueDemande.Activite");
					
					caracteristique1.setTypeCaracteristique(typeCaracteristique4);
			  
			   
					ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
					//programmeImmobilier.setId("programme01") ;
					//programmeImmobilier.setCode("CCD");
					
					caracteristique1.setProgrammeImmobilier(programmeImmobilier);
					
					
					//Liste de caracteristiques de demande de réservation logement
					
					List<CaracteristiqueDemandeReservationLogement> entityList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
					
					entityList.add(caracteristique1);
				   
				    User loggedInUser = new User();
				   
				    loggedInUser.setId("user1");
				    loggedInUser.setNom("alzouma");
				    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				   
				    //Execution  du test et verification des sortants
				    
				    List<CaracteristiqueDemandeReservationLogement> rtnList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
				   	  
				    
				    rtnList =  creerModifierCaracteristiqueDemandeReservationLogementCtl
				    		          .associerCaracteristiqueDemandeReservationLogementList(entityList, true, null, true, locale, loggedInUser, msgList);
				    
				    
				    //Vérification du resultat rétourné
				    
				    assertEquals(null , rtnList) ;
			
			}			
			
			
			
			//Association d'une caracteristique de demande de reservation logement dont le type n'est pas défini
			
			@Test
			public void testAssocierCaracteristiqueDemandeReservationLogementSansType() {
				
				
				
				   //Iniatilisation des entrants
				
					//Caracteristique Fonction
				
					CaracteristiqueDemandeReservationLogement caracteristique1 = new CaracteristiqueDemandeReservationLogement();
					
			   
					Reference typeCaracteristique5 = new Reference();
					typeCaracteristique5.setId("ref.element.typeCaracteristiqueDemande.Activite");
					
					//caracteristique1.setType(type1);
			  
			   
					ProgrammeImmobilier programmeImmobilier = new ProgrammeImmobilier();
					programmeImmobilier.setId("programme01") ;
					programmeImmobilier.setCode("CCD");
					
					caracteristique1.setProgrammeImmobilier(programmeImmobilier);
					
					
					//Liste de caracteristiques de demande de réservation logement
					
					List<CaracteristiqueDemandeReservationLogement> entityList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
					
					entityList.add(caracteristique1);
				   
				    User loggedInUser = new User();
				   
				    loggedInUser.setId("user1");
				    loggedInUser.setNom("alzouma");
				    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
				   
				    //Execution  du test et verification des sortants
				    
				    List<CaracteristiqueDemandeReservationLogement> rtnList = new ArrayList<CaracteristiqueDemandeReservationLogement>();
				   	  
				    
				    rtnList =  creerModifierCaracteristiqueDemandeReservationLogementCtl
				    		          .associerCaracteristiqueDemandeReservationLogementList(entityList, true, null, true, locale, loggedInUser, msgList);
				    
				    
				    //Vérification du resultat rétourné
				    
				    assertEquals(null , rtnList) ;
			
			}
			
	
}		*/
	
	
