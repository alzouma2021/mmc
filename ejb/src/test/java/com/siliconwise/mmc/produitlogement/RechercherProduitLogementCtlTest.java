package com.siliconwise.mmc.produitlogement;


import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.siliconwise.common.VilleDAO;
import com.siliconwise.common.VilleDAOInterface;
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
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.organisation.efi.EFi;
import com.siliconwise.mmc.organisation.efi.EfiCtl;
import com.siliconwise.mmc.organisation.efi.EfiCtlInterface;
import com.siliconwise.mmc.organisation.efi.EfiDAO;
import com.siliconwise.mmc.organisation.efi.EfiDAOInterface;
import com.siliconwise.mmc.organisation.promoteur.Promoteur;
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
import com.siliconwise.mmc.produitlogement.critere.ValeurFloat;

import com.siliconwise.mmc.produitlogement.critere.ValeurDateTime;
import com.siliconwise.mmc.produitlogement.critere.ValeurDouble;

import com.siliconwise.mmc.produitlogement.critere.ValeurInteger;
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
import com.siliconwise.mmc.produitlogement.critere.ValeurLong;
import com.siliconwise.mmc.produitlogement.critere.ValeurReference;
/*
import com.siliconwise.mmc.produitlogement.critere.ValeurTexte;
import com.siliconwise.mmc.produitlogement.critere.ValeurTime;
*/
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class RechercherProduitLogementCtlTest {
	
	
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
						SupprimerModeFinancementCtlInterface.class,
						SupprimerModeFinancementCtl.class,
						RechercherModeFinancementCtlInterface.class,
						RechercherModeFinancementCtl.class,
						EmailServiceException.class,
						EmailService.class,
						DocumentEntityTransfert.class
						)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
	}
	
	
	@Inject
	RechercherProduitLogementCtlInterface rechercherProduitLogementCtl ;
	
	@Inject 
	ReferenceCtlInterface referenceCtl ;
	
	//private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
	
	CritereRechercheProduitLogement critereRechercheProduitLogement = new  CritereRechercheProduitLogement();
	ProprieteProduitLogement proprieteProduitLogement = new ProprieteProduitLogement() ;
	Valeur<Object> valeurCritere = new Valeur<Object>();
	Reference type = new Reference();
	OperateurCritere operateurCritere = new OperateurCritere();
	List<ProduitLogement> rtn =  new ArrayList<ProduitLogement>();
	List<CritereRechercheProduitLogement> list =  new ArrayList<CritereRechercheProduitLogement>();
	List<NonLocalizedStatusMessage> msgList ;
	Locale locale;
	 
	
	
		@Test
		public void testRechercherUnProduitLogementIdNonExiste() {
			
			//Execution du test et Verification du sortant
			assertEquals( null , rechercherProduitLogementCtl.rechercherUnProduitLogementParId("1", null, true, ProduitLogement.class));
		}
		
		
		@Test
		@UsingDataSet("datasets/produitlogement.yml")
		public  void testRechercherUnProduitLogementParIdDataset() {
			//Execution et Verification
			assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherUnProduitLogementParId("produit02", null, true, ProduitLogement.class).getCode());
		}
		
		@Test
		public  void testRechercherUnProduitLogementParIdDataSet1() {
			//Execution et Verification
			assertEquals( "CCD" , rechercherProduitLogementCtl.rechercherUnProduitLogementParId("produit01", null, true, ProduitLogement.class).getProgrammeImmobilier().getCode());
			
			//Verification de la taille de ImageList et VideoList
				//assertEquals(2 , rechercherProduitLogementCtl.rechercherUnProduitLogementParId("produit01", null, true, ProduitLogement.class).getImageList().size());
			
			//assertEquals(2 , rechercherProduitLogementCtl.rechercherUnProduitLogementParId("produit01", null, true, ProduitLogement.class).getVideosList().size());
		}
		*/
		
	/**
	 * Differents tests en fonction des types de propriété : Cas normaux et d'exceptions
	 * 
	 * Objectifs de tests : 
	 * 		Analyser les requêtes JPQL et SQL générées pour chaque Test effectué
	 * 		Constater le résultat de chaque Test effectué 
	 */
	
		
		
/**
 * Differents tests sur le type propriéte FLOAT
 * 
 */
		
		/**
		 *Test de recherche des produits logements  dont le prix est compris 100000 et 150000
		 */
    
/*
		@Test
		public void testRechercherProduitLogementParCritereListParIntervalledePrix() {
			
			//Initialisation des entrants du test
			ArrayList<Double> value = new ArrayList<Double>();
			value.add(100000D);
			value.add(150000D);
			 
			type.setId("ref.element.typeValeur.double");
			proprieteProduitLogement.setType(type);
			valeurCritere.setValeur(value);
			operateurCritere.setCode("BETWEEN");
			
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout dans la liste du critère
			list.add(critereRechercheProduitLogement);
			
			//Execution du test et verification des sortants
			
				//Verifier la taile de la liste 
				assertEquals(4 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
				
				
				//Verification des informations du 1er  élément de la liste
				assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
				assertEquals( "Villa Duplex 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
				
				//Verification des informations du 2éme  élément de la liste
				assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
				assertEquals( "Appartement de luxe 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getDesignation());
				
				//Verification des informations du 2éme  élément de la liste
				assertEquals( "Serenité" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(2).getCode());
				assertEquals( "Villa basse 3 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(2).getDesignation());
				
				//Verification des informations du 2éme  élément de la liste
				assertEquals( "Somptueux" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(3).getCode());
				assertEquals( "Château" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(3).getDesignation());
				
		}*/
		
		/**
		 *Test de recherche des produits logements  dont le prix est compris 100000 et 120000
		 */

        /*
		@Test
		public void testRechercherProduitLogementParCritereListParIntervalledePrix1() {
			
			//Initialisation des entrants du test
			//Initialisation des entrants du test
			ArrayList<Double> value = new ArrayList<Double>();
			value.add(100000D);
			value.add(120000D);
			 
			type.setId("ref.element.typeValeur.double");
			proprieteProduitLogement.setType(type);
			valeurCritere.setValeur(value);
			operateurCritere.setCode("BETWEEN");
			
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout dans la liste du critère
			list.add(critereRechercheProduitLogement);
			
			//Execution du test et verification des sortants
			
				//Verifier la taile de la liste 
				assertEquals(2 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
				
				
				//Verification des informations du 1er  élément de la liste
				assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
				assertEquals( "Villa Duplex 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
				
				//Verification des informations du 2éme  élément de la liste
				assertEquals( "Serenité" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
				assertEquals( "Villa basse 3 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getDesignation());
				
		}
		*/
		
		/**
		 *Test de recherche des produits logements  à partir d'un prix 120000
		 */

		/*
		@Test
		public void testRechercherProduitLogementParCritereListParDunPrixUnique() {
			
			//Initialisation des entrants du test
		
			ArrayList<Float> value = new ArrayList<Float>();
			value.add(120000f);
			 
			type.setId("ref.element.typeValeur.float");
			proprieteProduitLogement.setType(type);
			valeurCritere.setValeur(value);
			operateurCritere.setCode("BETWEEN");
			
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout dans la liste du critère
			list.add(critereRechercheProduitLogement);
			
			//Execution du test et verification des sortants
			
				//Verifier la taile de la liste 
				assertEquals(2 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
				
				
				//Verification des informations du 1er  élément de la liste
				assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
				assertEquals( "Villa Duplex 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
				
				//Verification des informations du 2éme  élément de la liste
				assertEquals( "Serenité" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
				assertEquals( "Villa basse 3 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getDesignation());
				
		}
		*/

		
		
		/**
		 * Test de recherche produits logements qui ont pour type de propriété Float
		 * Cas d'exception : La valeur du critére n'est pas un Float
		 * Le resultat de la requête devrait être null
		 */
	/*
		@Test
		public void testRechercherProduitLogementParCritereListNonFloat() {
			
			//Initialisation des entrants du Test
			ArrayList<String> value = new ArrayList<String>();
			value.add("nonFloat");
			
			type.setId("ref.element.typeValeur.float");
			proprieteProduitLogement.setType(type);
			valeurCritere.setValeur(value);
			operateurCritere.setCode("BETWEEN");
			
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout du critère dans la liste
			list.add(critereRechercheProduitLogement);
			
			//Exécution du Test et Verification des sortants
			assertEquals(null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
		}*/
	
/**
 * 
 * Fin des test sur le type de propriété FLOAT
 * 
 */

		
/*********************************************SUIVANT***********************************/	
		
/**
 * Differents tests sur le type propriéte INTEGER
 * 
 */

		/**
		 * Test de recherche des produits logements  en fonction du nombre de chambres
		 */
		
/*
		//Test de recherche de produits logements en fonction du nombre de chambres
		@SuppressWarnings("unused")
		@Test
		public void testRechercherProduitLogementEnFonctionDuNombreDeChambre() {
			
			//Initialisation des entrants du test
			ArrayList<Integer> value = new ArrayList<Integer>();
			 value.add(5);
			 value.add(5);
			 value.add(3);
			
			type.setId("ref.element.typeValeur.integer");
			proprieteProduitLogement.setType(type);
			valeurCritere.setValeur(value);
			operateurCritere.setCode("IN");
			
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout dans la liste du critère
			list.add(critereRechercheProduitLogement);
			
			//Execution du test et verification des sortants
				
				//La taille 
				assertEquals( 3 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
				
			assertEquals( "Villa Duplex 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
			assertEquals( "Appartement de luxe 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getDesignation());
			
			assertEquals( "Concorde" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getProgrammeImmobilier().getDesignation());
			assertEquals( "Iris" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getProgrammeImmobilier().getDesignation());
			
		}
		*/

		/**
		 * Cas d'exception : La valeur du critére n'est pas un Integer
		 * Le resultat de la requête devrait être null
		 */

/*
		@Test
		public void testRechercherProduitLogementParCritereListProprieteNonInteger() {
			
			
			//Initialisation des entrants du Test
			ArrayList<String> value = new ArrayList<String>();
			 value.add("nonInteger");
			 value.add("nonInteger");
			 value.add("nonInteger");
			 
			type.setId("ref.element.typeValeur.integer");
			proprieteProduitLogement.setType(type);
			valeurCritere.setValeur(value);
			operateurCritere.setCode("IN");
		
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout du critère dans la liste
			list.add(critereRechercheProduitLogement);
			
			//Execution du Test et Verification des sortants
			assertEquals(null ,rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
		}
	*/
		
		/**
		 * Test de recherche des produits logements  à partir d'une valeur null
		 */
	
		/*
		@Test
		public void testRechercherProduitLogementParCritereListProprieteValeurNull() {
			
			//Initialisation des entrants du test
			//Initialisation des entrants du Test
			 
			type.setId("ref.element.typeValeur.integer");
			proprieteProduitLogement.setType(type);
			valeurCritere.setValeur(null);
			operateurCritere.setCode("=");
			
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout dans la liste du critère
			list.add(critereRechercheProduitLogement);
			
			//Execution du test et verification des sortants
			//assertEquals( "Villa basse 3 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
			//assertEquals( "Concorde" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getProgrammeimmobilier().getDesignation());
			assertEquals(null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
			
		}
		*/
		
/**
 * 
 * Fin des test sur le type de propriété INTEGER
 * 
 */
		

/*********************************************SUIVANT***********************************/
		
		
/**
 * Differents tests sur les type propriétes STRING et REFERENCE
 * 
 */

		/**
		 *Test de recherche des produits logements  à partir de la localisation
		 */
	
/*
		//Test de recherche de produits logements en fonction de leur localisation
		@Test
		public void testRechercherProduitLogementEnFonctionDeLaLocalisation() {
			
			//Initialisation des entrants du test
			ArrayList<String> value = new ArrayList<String>();
			 value.add("Cocody");
			 value.add("TreichVille");
			
			type.setId("ref.element.typeValeur.string");
			proprieteProduitLogement.setType(type);
			
			valeurCritere.setValeur(value);
			operateurCritere.setCode("IN");
			
			critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
			critereRechercheProduitLogement.setValeurCritere(valeurCritere);
			critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
			//Ajout du critere dans la liste
			list.add(critereRechercheProduitLogement);
			
			//Execution du test et verification des sortants
			
				//Verification de la taille de la liste tournée
			assertEquals( 2 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
			
			assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
			assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
		}
	*/

		/**
		 *Test de recherche des produits logements  en fonction du type de logement
		 */
/*
				
				//Test de recherche des produits logements de type Villa
				@Test
				public void testRechercherProduitLogementDeTypeVilla() {
					
					//Initialisation des entrants du test
					ArrayList<String> value1 = new ArrayList<String>();
					value1.add("ref.element.valeur.villa");*/
					
					/*
					Reference reference = new Reference();
					reference.setId("ref.element.valeur.villa");
					
					ArrayList<Reference> valueList = new ArrayList<Reference>();
					valueList.add(reference);
				   */
					
            /*
					type.setId("ref.element.typeValeur.reference");
					proprieteProduitLogement.setType(type);
					valeurCritere.setValeur(value1);
					operateurCritere.setCode("IN");
					
					critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
					critereRechercheProduitLogement.setValeurCritere(valeurCritere);
					critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
					
					
					//Ajout du critere dans la liste
					list.add(critereRechercheProduitLogement);
					
					//Execution du test et verification des sortants
					assertEquals( 1 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
					assertEquals( "Villa Duplex 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
				    assertEquals( "produit01" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getId());
				    assertEquals( "CCD" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getProgrammeImmobilier().getCode());
					
				}
		*/
	
				
				//Rechercher des produits logements de type appartement
		
		/*
				@Test
				public void testRechercherProduitLogementDeTypeAppartement() {
					
					//Initialisation des entrants du test*/
					
					/*
					Reference reference = new Reference();
					reference.setId("ref.element.valeur.appartement");

					ArrayList<Reference> value = new ArrayList<Reference>();
					value.add(reference);
					*/
			/*
					ArrayList<String> value = new ArrayList<String>();
					value.add("ref.element.valeur.appartement");
					
					type.setId("ref.element.typeValeur.reference");
					proprieteProduitLogement.setType(type);
					valeurCritere.setValeur(value);
					operateurCritere.setCode("IN");
					
					critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
					critereRechercheProduitLogement.setValeurCritere(valeurCritere);
					critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
					
					//Ajout dans la liste du critère
					list.add(critereRechercheProduitLogement);
					
					//Execution du test et verification des sortants
					assertEquals( 1 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
					assertEquals( "Appartement de luxe 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
					assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
					
				}
			*/
				/**
				 * Test de recherche produits logements qui ont pour type de propriété String
				 * Cas d'exception : La valeur du critére n'est pas un String
				 * Le resultat de la requête devrait être null
				 */
				
				/*
				//Test de recherche dans le cas ou la valeur du critere ne correspondant au type 
				@Test
				public void testRechercherProduitLogementPortantSurUnTypeDiffrentdePropriete() {
					
					
					//Initialisation des entrants du Test
					
					ArrayList<Integer> value = new ArrayList<Integer>();
					
					value.add(12000);
					value.add(13000);
					 
					type.setId("ref.element.typeValeur.string");
					proprieteProduitLogement.setType(type);
					operateurCritere.setCode("IN");
					
					critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
					critereRechercheProduitLogement.setValeurCritere(valeurCritere);
					critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
					
					//Ajout du critère dans la liste
					list.add(critereRechercheProduitLogement);
					
					//Exécution du Test et Vérification des sortants
					assertEquals( null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
				}
*/
				
/**
 * 
 * Fin des test sur le type de propriété STRING
 * 
 */
		
		
/*********************************************SUIVANT***********************************/
	
	
/**
 * Differents tests sur les types propriétes DATE , DATETIME et TIME  
 * 
 */
		
			/**
			 *Test de recherche des produits logements à partir une intervalle de dates donnée 
			 */
		
		/*
			@Test
			public void testRechercherProduitLogementParCritereListApartirDuneIntervalleDateCreation() {
				
				//Initialisation des entrants du test
				ArrayList<LocalDate> value = new ArrayList<LocalDate>();
				 value.add(LocalDate.parse("2015-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
				 value.add(LocalDate.parse("2017-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
				 
				 
				type.setId("ref.element.typeValeur.date");
				proprieteProduitLogement.setType(type);
				//LocalDate date = LocalDate.parse("2020-12-12" , DateTimeFormatter.ISO_LOCAL_DATE);
				valeurCritere.setValeur(value);
				operateurCritere.setCode("BETWEEN");
			
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout dans la liste du critère
				list.add(critereRechercheProduitLogement);
				
				//Execution du test et verification des sortants
					//Verification de la taille de la liste renvoyée
				assertEquals(3 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
				
				assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
				assertEquals( "Fiabilité" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
				assertEquals( "Somptueux" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(2).getCode());
				
			}*/
			
		
			/**
			 *Test de recherche des produits logements à partir une intervalle de dates donnée 
			 *
			*/
		
		/*
			@Test
			public void testRechercherProduitLogementParCritereListApartirDuneIntervalleDateCreation1() {
				
				//Initialisation des entrants du test
				ArrayList<LocalDate> value = new ArrayList<LocalDate>();
				 value.add(LocalDate.parse("2015-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
				 value.add(LocalDate.parse("2019-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
				 
				type.setId("ref.element.typeValeur.date");
				proprieteProduitLogement.setType(type);
				//LocalDate date = LocalDate.parse("2020-12-12" , DateTimeFormatter.ISO_LOCAL_DATE);
				valeurCritere.setValeur(value);
				operateurCritere.setCode("BETWEEN");
			
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout dans la liste du critère
				list.add(critereRechercheProduitLogement);
				
				//Execution du test et verification des sortants
					//Verification de la taille de la liste renvoyée
				assertEquals(5 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
				
				assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
				assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
				assertEquals( "Serenité" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(2).getCode());
				
			}
			*/
			
			/**
			 *Test de recherche des produits logements à partir une date donnée 
			 */
				
			/*
			@Test
			public void testRechercherProduitLogementParAPartirDuneDateDonnee() {
				
				//Initialisation des entrants du test
				ArrayList<LocalDate> value = new ArrayList<LocalDate>();
				 value.add(LocalDate.parse("2015-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
				 
				type.setId("ref.element.typeValeur.date");
				proprieteProduitLogement.setType(type);
				//LocalDate date = LocalDate.parse("2020-12-12" , DateTimeFormatter.ISO_LOCAL_DATE);
				valeurCritere.setValeur(value);
				operateurCritere.setCode("=");
			
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout dans la liste du critère
				list.add(critereRechercheProduitLogement);
				
				//Execution du test et verification des sortants
					//Verification de la taille de la liste renvoyée
				assertEquals(1 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
				
				assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());

				
			}*/
			
				
			/**
			 * Test de rechercher des recherches produits lorsque la valeur de la propriété ne correspondant pas à une date
			 */
		
		/*
			@Test
			public void testRechercherProduitLogementParCritereListValeurNonDate() {
				
				//Initilisation des entrants du test
				ArrayList<String> value = new ArrayList<String>();
				 value.add("nonLocalDate");
				 value.add("nonLocalDate");
				 
				type.setId("ref.element.typeValeur.date");
				proprieteProduitLogement.setType(type);
				valeurCritere.setValeur(value);
				operateurCritere.setCode("BETWEEN");
				
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout du critere dans la liste critère
				list.add(critereRechercheProduitLogement);
				
				//Execution du test et Verification des sortants
				assertEquals( null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));

			}
			*/

			/**
			 * Test de rechercher des recherches produits à partir d'une liste null
			 */
		
			/*
				
			@Test
			public void testRechercherProduitLogementParCritereListValeurDateNull() {
				
				//Initilisation des entrants du test
				type.setId("ref.element.typeValeur.date");
				proprieteProduitLogement.setType(type);
				valeurCritere.setValeur(null);
				operateurCritere.setCode("BETWEEN");
				
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout du critere dans la liste critère
				list.add(critereRechercheProduitLogement);
				
				//Execution du test et Verification des sortants
				assertEquals( null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));

			}
			*/
			
			/**
			 *Test de recherche des produits logements à partir d'une datetime donnée 
			 */

				/*
			@Test
			public void testRechercherProduitLogementParCritereListValeurDateTime() {
				
				//Initilisation des entrants du Test
				ArrayList<LocalDateTime> value = new ArrayList<LocalDateTime>();
				 value.add(LocalDateTime.of(2019,12,12 ,12 ,12 ,12));
				 value.add(LocalDateTime.of(2021,12,12 ,12 ,12 ,12));
				 
				type.setId("ref.element.typeValeur.datetime");
				proprieteProduitLogement.setType(type);
				valeurCritere.setValeur(value);
				operateurCritere.setCode("BETWEEN");
				
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout dans la liste du critère
				list.add(critereRechercheProduitLogement);
				
				//Execution du Test et Verification des sortants
					//Verification de la longueur de la liste retournée
					assertEquals( 3 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
					
					//Verification des données retournées
					assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
					assertEquals( "Reve" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
					assertEquals( "Somptueux" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(2).getCode());
			}
			
			
			/**
			 * Test de rechercher des produits logements lorsque la valeur de la propriété ne correspondant pas à une datetime
			 */
		
/*
			@Test
			public void testRechercherProduitLogementParCritereListValeurNonDateTime() {
				
				//Initilisation des entrants
				ArrayList<String> value = new ArrayList<String>();
				 value.add("nonLocalDateTime");
				 value.add("nonLocalDateTime");
				 
				type.setId("ref.element.typeValeur.datetime");
				proprieteProduitLogement.setType(type);
				valeurCritere.setValeur(value);
				operateurCritere.setCode("BETWEEN");
				
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
				list.add(critereRechercheProduitLogement);
				
				//Execution du Test et Verification des sortants
				assertEquals( null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));

			}
			
			
			/**
			 * Test de rechercher des produits logements lorsque la valeur LocalDateTime est nulle
			 */
		
				/*
			@Test
			public void testRechercherProduitLogementParCritereListValeurDateTimeNull() {
				
				//Initilisation des entrants
				type.setId("ref.element.typeValeur.datetime");
				proprieteProduitLogement.setType(type);
				valeurCritere.setValeur(null);
				operateurCritere.setCode("BETWEEN");
				
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
			
				list.add(critereRechercheProduitLogement);
				
				//Execution du Test et Verification des sortants
				assertEquals( null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));

			}*/
			
		
			/**
			 *Test de recherche des produits logements à partir de l'heure auxquelles sont enregistrés
			 */
		
			/*
			@Test
			public void testRechercherProduitLogementParDUneHeureEnregistrementDonee() {
			
				//Initilisation des entrants du Test
				type.setId("ref.element.typeValeur.time");
				proprieteProduitLogement.setType(type);
				LocalTime time = LocalTime.of(12, 12, 12) ;
				valeurCritere.setValeur(time);
				operateurCritere.setCode("=");
				
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout dans la liste du critère
				list.add(critereRechercheProduitLogement);
				
				//Execution du Test et Verification des sortants
					//Verification de la longueur de la liste retournée
					assertEquals( 1 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
					//Verification des données retournées
					assertEquals( "Somptueux" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
					assertEquals( "Château" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
					
			}
			
			
			/**
			 * Test de recherche produits logements qui ont pour type de propriété Time
			 * Cas d'exception : La valeur du critére n'est pas un Time
			 * Le resultat de la requête devrait être null
			 */
      /*
			@Test
			public void testRechercherProduitLogementParCritereListNonTime() {
				
				//Initilisation des entrants du Test
				type.setId("ref.element.typeValeur.time");
				proprieteProduitLogement.setType(type);
				valeurCritere.setValeur("nonTime");
				operateurCritere.setCode("=");
				
				critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
				critereRechercheProduitLogement.setValeurCritere(valeurCritere);
				critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
				
				//Ajout du critère dans la liste
				list.add(critereRechercheProduitLogement);
				
				//Exécution du Test et Vérification des sortants 
				assertEquals( null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
			
			}*/
			
/**
 * 
 * Fin des test sur les types propriétes DATE , DATETIME et TIME  
 * 
 */
			
			
/*********************************************SUIVANT***********************************/
			
			
/**
 * Differents tests sur le type propriéte BOOLEAN
 * 
 */
					

	/**
	 *Test de recherche des produits logements ayant une piscine
	 */
	
			/*
	@Test
	public void testRechercherProduitLogementAyantUnePiscine() {
		
		
		//Initialisation des entrants du test
		ArrayList<Boolean> value = new ArrayList<Boolean>();
		 value.add(true);
		 value.add(true);
		 
		type.setId("ref.element.typeValeur.boolean");
		proprieteProduitLogement.setType(type);
		valeurCritere.setValeur(value);
		operateurCritere.setCode("IN");
		
		critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		critereRechercheProduitLogement.setValeurCritere(valeurCritere);
		critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
		
		//Ajout dans la liste du critère
		list.add(critereRechercheProduitLogement);
		
		//Execution du test et Verification des sortants
		  //Verifier la taille de la liste
		assertEquals( 2 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
		assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
	}
	
	/**
	 * Test de recherche produits logements qui ont pour type de propriété Boolean
	 * Cas d'exception : La valeur du critére n'est pas un Boolean
	 * Le resultat de la requête devrait être null
	 */

	/*
	@Test
	public void testRechercherProduitLogementParCritereListNonBoolean() {
		
		//Initilisation des entrants du Test
		ArrayList<Integer> value = new ArrayList<Integer>();
		 value.add(1);
		 value.add(2);
		type.setId("ref.element.typeValeur.boolean");
		type.setVersion(0);
		proprieteProduitLogement.setType(type);
		valeurCritere.setValeur(value);
		operateurCritere.setCode("IN");
	
		critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		critereRechercheProduitLogement.setValeurCritere(valeurCritere);
		critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
		
		//Ajout du critère dans la liste
		list.add(critereRechercheProduitLogement);
		
		//Exécution du Test et Verification des sortants
		assertEquals(null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
		
	}

	/**
	 * Test de recherche des produits logements à partir d'une valeur boolean null
	 */
/*

	@Test
	public void testRechercherProduitLogementValeurNull() {
		
		//Initialisation des entrants du test
		type.setId("ref.element.typeValeur.boolean");
		proprieteProduitLogement.setType(type);
		valeurCritere.setValeur(null);
		operateurCritere.setCode("=");
		
		critereRechercheProduitLogement.setProprieteProduitLogement(proprieteProduitLogement);
		critereRechercheProduitLogement.setValeurCritere(valeurCritere);
		critereRechercheProduitLogement.setOperateurCritere(operateurCritere);
		
		//Ajout dans la liste du critère
		list.add(critereRechercheProduitLogement);
		
		//Execution du test et Verification des sortants
		//assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
		//assertEquals( 1 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
		assertEquals(  null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
	}
	
		
/**
 * 
 * Fin des test sur le type de propriété BOOLEAN 
 * 
 */
	
	

/**
 * Test de recherche des produits logements à partir d'une liste comportant plusieurs criteres
 */
			
			/*
		
	@Test
	public void testRechercherProduitLogementParCritereList() {
		
		//Initialisation des entrants du test
	
		//Initialisation des éléments du premier critère : Produit logement en fonction du nombre de chambres
		ArrayList<Integer> value0 = new ArrayList<Integer>();
		 value0.add(5);
		 value0.add(5);
		 value0.add(3);
		 
		CritereRechercheProduitLogement critereRechercheProduitLogementInteger = new CritereRechercheProduitLogement();
		ProprieteProduitLogement ProprieteProduitLogementInteger = new ProprieteProduitLogement();
		Reference typeInteger = new Reference();
		Valeur<Object> valeurInteger = new Valeur<Object>();
		OperateurCritere operateurCritereInteger = new OperateurCritere();
		typeInteger.setId("ref.element.typeValeur.integer");
		ProprieteProduitLogementInteger.setType(typeInteger);
		valeurInteger.setValeur(value0);
		operateurCritereInteger.setCode("IN");
	

		critereRechercheProduitLogementInteger.setProprieteProduitLogement(ProprieteProduitLogementInteger);
		critereRechercheProduitLogementInteger.setValeurCritere(valeurInteger);
		critereRechercheProduitLogementInteger.setOperateurCritere(operateurCritereInteger);
		
		//Initialisation des éléments du premier critère : Produit logement en fonction du nombre de chambres
				ArrayList<Double> value = new ArrayList<Double>();
				 value.add(100000D);
				 value.add(150000D);
			
				 
				CritereRechercheProduitLogement critereRechercheProduitLogementFloat = new CritereRechercheProduitLogement();
				ProprieteProduitLogement ProprieteProduitLogementFloat = new ProprieteProduitLogement();
				Reference typeFloat = new Reference();
				Valeur<Object> valeurFloat = new Valeur<Object>();
				OperateurCritere operateurCritereFloat = new OperateurCritere();
				typeFloat.setId("ref.element.typeValeur.double");
				ProprieteProduitLogementFloat.setType(typeFloat);
				valeurFloat.setValeur(value);
				operateurCritereFloat.setCode("BETWEEN");
			

				critereRechercheProduitLogementFloat.setProprieteProduitLogement(ProprieteProduitLogementFloat);
				critereRechercheProduitLogementFloat.setValeurCritere(valeurFloat);
				critereRechercheProduitLogementFloat.setOperateurCritere(operateurCritereFloat);
		
		
		//Initialisation des éléements du deuxiéme critère : Produit Logement ayant une piscine
		ArrayList<Boolean> value1 = new ArrayList<Boolean>();
		 value1.add(true);
		 
		CritereRechercheProduitLogement critereRechercheProduitLogementBoolean = new CritereRechercheProduitLogement();
		ProprieteProduitLogement ProprieteProduitLogementBoolean = new ProprieteProduitLogement();
		Reference typeBoolean = new Reference();
		Valeur<Object> valeurBoolean = new Valeur<Object>();
		OperateurCritere operateurCritereBoolean = new OperateurCritere();
	
		typeBoolean.setId("ref.element.typeValeur.boolean");
		ProprieteProduitLogementBoolean.setType(typeBoolean);
		valeurBoolean.setValeur(value1);
		operateurCritereBoolean.setCode("IN");
	
		critereRechercheProduitLogementBoolean.setProprieteProduitLogement(ProprieteProduitLogementBoolean);
		critereRechercheProduitLogementBoolean.setValeurCritere(valeurBoolean);
		critereRechercheProduitLogementBoolean.setOperateurCritere(operateurCritereBoolean);
		

		//Initialisation des éléements du troisiéme critère : Produit Logement en fonction d'une date
		ArrayList<LocalDate> value2 = new ArrayList<LocalDate>();
		 value2.add(LocalDate.parse("2015-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
		 value2.add(LocalDate.parse("2019-12-12" ,  DateTimeFormatter.ISO_LOCAL_DATE));
		 
		CritereRechercheProduitLogement critereRechercheProduitLogementLocalDate = new CritereRechercheProduitLogement();
		ProprieteProduitLogement ProprieteProduitLogementLocalDate = new ProprieteProduitLogement();
		Reference typeLocalDate = new Reference();
		Valeur<Object> valeurLocalDate = new Valeur<Object>();
		OperateurCritere operateurCritereLocalDate = new OperateurCritere();
		
		typeLocalDate.setId("ref.element.typeValeur.date");
		ProprieteProduitLogementLocalDate.setType(typeLocalDate);
		valeurLocalDate.setValeur(value2);
		operateurCritereLocalDate.setCode("BETWEEN");
	
		critereRechercheProduitLogementLocalDate.setProprieteProduitLogement(ProprieteProduitLogementLocalDate);
		critereRechercheProduitLogementLocalDate.setValeurCritere(valeurLocalDate);
		critereRechercheProduitLogementLocalDate.setOperateurCritere(operateurCritereLocalDate);
		
		
		//Ajout des critères dans la liste
		list.add(critereRechercheProduitLogementInteger);
		list.add(critereRechercheProduitLogementBoolean);
		list.add(critereRechercheProduitLogementLocalDate);
		list.add(critereRechercheProduitLogementFloat);
		
		//Execution du teste et Verification des sortants
		   //Vérification de la taille de la liste retournée
		   assertEquals( 2 , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).size());
		   //assertEquals( null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList));
		   
		   //Affichage des informations
		
		  assertEquals( "Senateur" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getCode());
		  assertEquals( "Serenité" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getCode());
		  assertEquals( "Villa Duplex 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(0).getDesignation());
		  assertEquals( "Villa basse 3 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList).get(1).getDesignation());
		
	}

	/**
	 * Test de rechercher des produits logements à partir d'une liste vide ou null envoyée
	 */
		
			/*	
	@Test
	public void testRechercherProduitLogementParCritereListVide() {
		
		List<CritereRechercheProduitLogement> list =  new ArrayList<CritereRechercheProduitLogement>();

		assertEquals(null , rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(list , msgList) );
	
	}*/
	
/*********************************************************Suivant*************************************************/
	
	/**
	 * Test pour retourner toutes les propriétés produit logement
	 */
		
			/*	
	@Test
	public void testRechercherProprieteProduitLogementList() {
		
		//Execution du Test et Verification des informations
			
			//La taille de la liste retournée
			assertEquals(10 , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().size() );
			
			//Informations sur le premier element
			assertEquals("TypeLogement" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(0).getCode());
			assertEquals("ref.element.typeValeur.reference" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(0).getType().getId());
			
			//Informations sur le 2 element
			assertEquals("NombrePieces" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(1).getCode());
			assertEquals("ref.element.typeValeur.integer" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(1).getType().getId());
			
			//Informations sur le premier element
			assertEquals("Prix" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(2).getCode());
			assertEquals("ref.element.typeValeur.double" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(2).getType().getId());
			
			//Informations sur le premier element
			assertEquals("DateCreation" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(3).getCode());
			assertEquals("ref.element.typeValeur.date" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(3).getType().getId());
			
			//Informations sur le premier element
			assertEquals("Piscine" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(4).getCode());
			assertEquals("ref.element.typeValeur.boolean" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(4).getType().getId());
			
			//Informations sur le premier element
			assertEquals("Localisation" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(5).getCode());
			assertEquals("ref.element.typeValeur.string" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(5).getType().getId());
			
			//Informations sur le premier element
			assertEquals("DateTimeCreation" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(6).getCode());
			assertEquals("ref.element.typeValeur.datetime" , rechercherProduitLogementCtl.rechercherProprieteProduitLogementList().get(6).getType().getId());
			
	}*/
	
	
	/**
	 * Test pour retourner tous les produits logements
	 */
			
			/*
	@Test
	public void testRechercherProduitLogementList() {
		
		//Execution du Test et Verification des informations
			
			//La taille de la liste retournée
			assertEquals(5 , rechercherProduitLogementCtl.rechercherProduitLogementList().size() );
			
			//Informations sur le premier element
			assertEquals("produit01" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(0).getId());
			assertEquals("Villa Duplex 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(0).getDesignation());
			
			//Informations sur le 2 element
			assertEquals("produit02" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(1).getId());
			assertEquals("Appartement de luxe 4 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(1).getDesignation());
			
			//Informations sur le premier element
			assertEquals("produit03" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(2).getId());
			assertEquals("Villa basse 3 Pièces" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(2).getDesignation());
			
			//Informations sur le premier element
			assertEquals("produit04" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(3).getId());
			assertEquals("Villa Duplex" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(3).getDesignation());
			
			//Informations sur le premier element
			assertEquals("produit05" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(4).getId());
			assertEquals("Château" , rechercherProduitLogementCtl.rechercherProduitLogementList().get(4).getDesignation());
			
			
	}
	*/
			
	/**
	 * 
	 * Test pour rechercher des produits logements par pormoteur
	 * 
	 */
	
	  /*
	@Test
	public void testrechecherPrdouitLogementsParPromoteur() {
		
		//Execution du Test et Verification des informations
			
			//La taille de la liste retournée
			assertEquals(4, rechercherProduitLogementCtl.rechercherProduitLogementParPromoteur("promoteur01", null, true, ProduitLogement.class).size());
			
	}*/
	
	/**
	 * 
	 * Test pour trouver des produits logements en fonction des mots clés
	 * 
	 */
			
			/*
	 @Test
	 public void testRechercherProduitLogementParMotCle() {
		 
		 //Initialisation des entrants
		 String motCle = "Somptueux" ;
		 
		 //Execution du test et Verification des sortants
		 assertEquals(1 , rechercherProduitLogementCtl.rechercherProduitLogementParMotCle(motCle).size());
	 }*/

/*************************************************************************************************************/
	 
	 /*
	 //Test de recherche de reference par designation
	 @Test
	 public void testRechercherReferenceParDesignation() {
		 
		 //Initialisation des entrants
		 String designation = "Image Consultation" ;
		 
		 //Execution du test et Verification des sortants
		 assertEquals(designation, referenceCtl.trouverReferenceParDesignationReference(designation).getDesignation());
	 }
	 
	 
	 //test rechercher des references par id famille
	 @Test
	 public void testRechercherToutesReferencesParIdFamille() {
		 
		 //Initialisation des entrants
		 String searchIdFamille = "ref.famille.typeValeur" ;
		 String searchIdFamille1 = "ref.famille.typeLogement" ;
		 
		 String searchIdFamille2 = "ref.famille.typeDocument" ;
		 
		 
		 //Execution du test et Verification des sortants
		 assertEquals(10, referenceCtl.trouverToutesLesReferenceParIdFamille(searchIdFamille, msgList, locale).size());
		 	
		 	assertEquals("ref.element.typeValeur.integer", referenceCtl.trouverToutesLesReferenceParIdFamille(searchIdFamille, msgList, locale).get(0).getId());
		 
		 assertEquals(2, referenceCtl.trouverToutesLesReferenceParIdFamille(searchIdFamille1, msgList, locale).size());
		 
		 assertEquals(3, referenceCtl.trouverToutesLesReferenceParIdFamille(searchIdFamille2, msgList, locale).size());
	 }
	 */
			
	 /**
	  * Test des cas d'exception 
	  */
	 
			/*	
	 //Test de recherche de reference à partir d'une designation nulle
	 @Test
	 public void testRechercherReferenceParDesignationNull() {
		 
		 //Initialisation des entrants
		 String designation = null ;
		 
		 //Execution du test et Verification des sortants
		 assertEquals(designation, referenceCtl.trouverReferenceParDesignationReference(designation));
	 }
	 
	 
	//test rechercher des references par famille à partir d'un Id null
	@Test
	public void testRechercherToutesReferencesParIdFamilleNull() {
			 
			 //Execution du test et Verification des sortants
			 ArrayList<Reference> rtn = new ArrayList<Reference>();
			
			 assertEquals(rtn , referenceCtl.trouverToutesLesReferenceParIdFamille(null, msgList, locale));
			 
			 assertEquals(rtn , referenceCtl.trouverToutesLesReferenceParIdFamille(null, msgList, locale));
		
	}
	

}*/

				
				
				

