package com.siliconwise.mmc.produitlogement;

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
import com.siliconwise.common.VilleDAO;
import com.siliconwise.common.VilleDAOInterface;
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
import com.siliconwise.mmc.organisation.efi.EfiCtl;
import com.siliconwise.mmc.organisation.efi.EfiCtlInterface;
import com.siliconwise.mmc.organisation.efi.EfiDAO;
import com.siliconwise.mmc.organisation.efi.EfiDAOInterface;
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
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVille;
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
public class RechercherProduitLogementDocumentTest {
	
	 
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
						ValeurCaracteristiqueProduitLogementVille.class,
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
						VilleDAOInterface.class,
						VilleDAO.class,
						ValiderModeFinancementCtlInterface.class,
						ValiderModeFinancementCtl.class,
						Temperament.class,
						TemperamentDAOInterface.class,
						TemperamentDAO.class,
						TiersCollecteurDAOInterface.class,
						TiersCollecteur.class,
						TiersCollecteurDAO.class,
						EfiDAOInterface.class,
						EfiDAO.class,
						EfiCtlInterface.class,
						EfiCtl.class,
						DocumentCtlInterface.class,
						Tiers.class,
						DocumentCtl.class,
						DocumentDAO.class,
						DocumentDAOInterface.class,
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
						AppConfigUtil.class
						
						)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
		@Inject
		ProduitLogementDocumentCtlInterface produitLogementDocumentCtl ;
		
		//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
		
		List<Document> rtnList =  new ArrayList<Document>();
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>();
		Locale locale;
		*/
		
		/**
		 * 
		 * Recherche d'une image de consultation d'un produit logement donné 
		 * 
		 */
		/*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testRechercherDocumentParProduitLogementParImageConsultation() {
			
			
			String idProduitLogement = "produit01" ;
			
			String typeDocument = "ref.element.typeValeur.imageConsultation";
			
			
			assertEquals( " Ceci est un test pour récuperer une image de consultation d'un produit logement avec ses metadatas et son contenu." 
					                          , produitLogementDocumentCtl.rechercherDocumentParProduitLogementParTypeDocument(idProduitLogement, typeDocument, true, null, true, locale, msgList).getContenu());
			
			
			
		}*/
		
		/**
		 * 
		 * Recherche d'une liste d'images par produit logement et par type document
		 * 
		 */
		/*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testRechercherDocumentListParProduitLogementParImagesList() {
			
			
			//Initialisation des entrants
			
			String idProduitLogement = "produit01" ;
			
			String typeDocument = "ref.element.typeValeur.image";
			
			//Execution du test
			
			rtnList = produitLogementDocumentCtl.rechercherDocumentListParProduitLogementParTypeDocument(idProduitLogement, typeDocument, true, null, true, locale, msgList);
			
			//Verification des differents resultats possibles du tets
			
			assertEquals( 3 , rtnList.size() );
			
			assertEquals( "imageList1.txt" , rtnList.get(0).getPath() );
			
			assertEquals( "imageList2.txt" , rtnList.get(1).getPath() );
			
			assertEquals( "imageList3.txt" , rtnList.get(2).getPath() );
			
		}*/
		
		
		/**
		 * 
		 * Recherche d'une liste de videos par produit logement et par type document
		 * 
		 */
		/*
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testRechercherDocumentListParProduitLogementParVideosList() {
			
			
			//Initialisation des entrants
			
			String idProduitLogement = "produit01" ;
			
			String typeDocument = "ref.element.typeValeur.video";
			
			//Execution du test
			
			rtnList = produitLogementDocumentCtl.rechercherDocumentListParProduitLogementParTypeDocument(idProduitLogement, typeDocument, true, null, true, locale, msgList);
			
			//Verification des differents resultats possibles du tets
			
			assertEquals( 3 , rtnList.size() );
			
			assertEquals( "videoList1.txt" , rtnList.get(0).getPath() );
			
			assertEquals( "videoList2.txt" , rtnList.get(1).getPath() );
			
			assertEquals( "videoList3.txt" , rtnList.get(2).getPath() );
			
		}
		

}*/
