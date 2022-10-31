package com.siliconwise.mmc.simulation;

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
import com.siliconwise.mmc.demandereservationlogement.financement.EcheanceFinancementTemperament;
import com.siliconwise.mmc.demandereservationlogement.financement.EcheanceFinancementTemperamentDAO;
import com.siliconwise.mmc.demandereservationlogement.financement.EcheanceFinancementTemperamentDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.Financement;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementComptant;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementComptantDAO;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementComptantDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementCreditImmobilier;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementCreditImmobilierDAO;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementCreditImmobilierDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementDAO;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierComptantSurSituation;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierComptantSurSituationDAO;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierComptantSurSituationDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierLink;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierLinkDAO;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierLinkDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementTemperament;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementTemperamentDAO;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementTemperamentDAOInterface;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementTransfert;
import com.siliconwise.mmc.demandereservationlogement.valeurcaracteristiquedemandereservationlogement.*;
import com.siliconwise.mmc.logement.CreerModifierOptionLogementCtl;
import com.siliconwise.mmc.logement.CreerModifierOptionLogementCtlInterface;
import com.siliconwise.mmc.logement.CreerModifierUnLogementCtl;
import com.siliconwise.mmc.logement.CreerModifierUnLogementCtlInterface;
import com.siliconwise.mmc.logement.Logement;
import com.siliconwise.mmc.logement.LogementDAO;
import com.siliconwise.mmc.logement.LogementDAOInterface;
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
import com.siliconwise.mmc.simulationfinancementimmobilier.CreerModifierUneSimulationFinancementImmobilierCtl;
import com.siliconwise.mmc.simulationfinancementimmobilier.CreerModifierUneSimulationFinancementimmobilierCtlInterface;
import com.siliconwise.mmc.simulationfinancementimmobilier.SimulationFinancementImmobilier;
import com.siliconwise.mmc.simulationfinancementimmobilier.SimulationFinancementImmobilierDAO;
import com.siliconwise.mmc.simulationfinancementimmobilier.SimulationFinancementImmobilierDAOInterface;
import com.siliconwise.mmc.simulationfinancementimmobilier.SimulationFinancementImmobilierTransfert;
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


@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class creerModifierUneSimulationFinancementImmobilierTest {
	

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
						CreerModifierUnLogementCtlInterface.class,
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
						HistoryEventPayload.class,
						SimulationFinancementImmobilier.class,
						SimulationFinancementImmobilierTransfert.class,
						SimulationFinancementImmobilierDAOInterface.class,
						SimulationFinancementImmobilierDAO.class,
						CreerModifierUneSimulationFinancementimmobilierCtlInterface.class,
						CreerModifierUneSimulationFinancementImmobilierCtl.class,
						EcheanceFinancementTemperament.class,
						EcheanceFinancementTemperamentDAO.class,
						EcheanceFinancementTemperamentDAOInterface.class,
						Financement.class,
						FinancementComptant.class,
						FinancementComptantDAO.class,
						FinancementComptantDAOInterface.class,
						FinancementCreditImmobilier.class,
						FinancementCreditImmobilierDAO.class,
						FinancementCreditImmobilierDAOInterface.class,
						FinancementDAO.class,
						FinancementDAOInterface.class,
						FinancementPallierComptantSurSituation.class,
						FinancementPallierComptantSurSituationDAO.class,
						FinancementPallierComptantSurSituationDAOInterface.class,
						FinancementPallierLink.class,
						FinancementPallierLinkDAO.class,
						FinancementPallierLinkDAOInterface.class,
						FinancementTemperament.class,
						FinancementTemperamentDAO.class,
						FinancementTemperamentDAOInterface.class,
						FinancementTransfert.class,
						ValeurCaracteristiqueDemandeReservationLogement.class,
						ValeurCaracteristiqueDemandeReservationLogementDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementInteger.class,
						ValeurCaracteristiqueDemandeReservationLogementIntegerDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementIntegerDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementBoolean.class,
						ValeurCaracteristiqueDemandeReservationLogementBooleanDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementBooleanDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementDate.class,
						ValeurCaracteristiqueDemandeReservationLogementDateDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementDateDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementDateTime.class,
						ValeurCaracteristiqueDemandeReservationLogementLong.class,
						ValeurCaracteristiqueDemandeReservationLogementLongDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementLongDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementDouble.class,
						ValeurCaracteristiqueDemandeReservationLogementDoubleDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementDoubleDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementDocument.class,
						ValeurCaracteristiqueDemandeReservationLogementDocumentDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementDocumentDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementReference.class,
						ValeurCaracteristiqueDemandeReservationLogementReferenceDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementReferenceDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementTexte.class,
						ValeurCaracteristiqueDemandeReservationLogementTexteDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementTexteDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementVille.class,
						ValeurCaracteristiqueDemandeReservationLogementVilleDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementVilleDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementString.class,
						ValeurCaracteristiqueDemandeReservationLogementStringDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementStringDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementTime.class,
						ValeurCaracteristiqueDemandeReservationLogementTimeDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementTimeDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementFloat.class,
						ValeurCaracteristiqueDemandeReservationLogementFloatDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementFloatDAOInterface.class,
						CreerModifierOptionLogementCtl.class,
						CreerModifierOptionLogementCtlInterface.class,
						CreerModifierUnLogementCtl.class,
						CreerModifierUnLogementCtlInterface.class,
						CreerModifierUnProduitLogementCtlInterface.class,
						Logement.class,
						LogementDAO.class,
						LogementDAOInterface.class,
						ValeurCaracteristiqueDemandeReservationLogementDateTimeDAO.class,
						ValeurCaracteristiqueDemandeReservationLogementDateTimeDAOInterface.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	CreerModifierUneSimulationFinancementimmobilierCtlInterface  creerModifierUneSimulationFinancementImmobilierCtl ;
	
	//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	Locale locale;
	

	
	//Creation d'une simulation ni valeurCaracteristiqueDemandeReservationLogement ni financement
	
	@Test
	@UsingDataSet("datasets/produitlogement.yml")
	public void creerModifierUneSimulationFinancementImmmobilierUnique() {
		
		
		    //Iniatilisation des entrants
		
			SimulationFinancementImmobilierTransfert simulationTransfert = new SimulationFinancementImmobilierTransfert();
			
		    SimulationFinancementImmobilier simulation = new SimulationFinancementImmobilier() ;
		    
		    
		    simulation.setDesignation("Simulation01");
		    simulation.setDescription("Premiere test de simulation");
		    
		    //User
		    
		    User loggedInUser = new User();
		   
		    loggedInUser.setId("user01");
		    loggedInUser.setNom("alzouma");
		    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
		    
		    simulation.setUser(loggedInUser);
		    
		    //Logement
		    
		    Logement logement = new Logement();
		    logement.setId("logement01");
		    //logement.setDesignation("logement01");
		    
		    simulation.setLogement(logement);
		    
		    //Objet SimulationFinancementImmobilierTransfert
		    
		    simulationTransfert.setSimulationFinancementImmobilier(simulation);
		    
		   
		    //Execution  du test et verification des sortants
		    
		    SimulationFinancementImmobilier rtn = null ;
		    
		    rtn =  creerModifierUneSimulationFinancementImmobilierCtl
		    		.creerModifierUneSimulationFinancementImmobilier(simulationTransfert,
		    				          true, null, true, locale, loggedInUser, msgList, true);
		    		
		    //Vérification des resultats
		    
		    assertEquals("Simulation01" , rtn.getDesignation()) ;
		    
		}
	
}		