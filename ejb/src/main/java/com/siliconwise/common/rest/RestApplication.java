  package com.siliconwise.common.rest;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

import com.siliconwise.common.PaysRest;
import com.siliconwise.common.VilleRest;
import com.siliconwise.common.config.data.ParameterCtrlRest;
import com.siliconwise.common.config.file.AppConfigKeys;
import com.siliconwise.common.document.DocumentRest;
import com.siliconwise.common.reference.OperateurCritereRest;
import com.siliconwise.common.reference.ReferenceRest;
import com.siliconwise.mmc.fournisseurauthentifcation.FournisseurAuthentificationRest;
import com.siliconwise.mmc.logement.LogementRest;
import com.siliconwise.mmc.modefinancement.CreditBancaireRest;
import com.siliconwise.mmc.modefinancement.ModeFinancementRest;
import com.siliconwise.mmc.modefinancement.TypeFinancementRest;
import com.siliconwise.mmc.organisation.efi.EFiRest;
import com.siliconwise.mmc.organisation.promoteur.PromoteurRest;
import com.siliconwise.mmc.produitlogement.ProduitLogementDocumentRest;
import com.siliconwise.mmc.produitlogement.ProduitLogementRest;
import com.siliconwise.mmc.produitlogement.ProprieteProduitLogementRest;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracterisitqueProduitLogementRest;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierDocumentRest;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilierRest;
import com.siliconwise.mmc.programmeimmobilier.caracteristiquedemandereservationlogement.CaracteristiqueDemandeReservationLogementRest;
import com.siliconwise.mmc.programmeimmobilier.typedocumentdemandereservationlogement.TypeDocumentDemandeReservationLogementRest;
import com.siliconwise.mmc.security.SessionRest;
import com.siliconwise.mmc.user.UserRest;

//Chemin d'acces des APis
@ApplicationPath(AppConfigKeys.SYSTEM_API_PATH)
public class RestApplication extends Application {
	
	@Override
	 public Set<Class<?>> getClasses(){
		 return Stream.of(
				 ProduitLogementRest.class,
				 UserRest.class,
				 ParameterCtrlRest.class,
				 ReferenceRest.class,
				 ProprieteProduitLogementRest.class,
				 ProgrammeImmobilierRest.class,
				 CaracterisitqueProduitLogementRest.class,
				 VilleRest.class,
				 OperateurCritereRest.class,
				 TypeFinancementRest.class,
				 EFiRest.class,
				 DocumentRest.class,
				 ProduitLogementDocumentRest.class,
				 ProgrammeImmobilierDocumentRest.class,
				 ProgrammeImmobilierRest.class,
				 ModeFinancementRest.class,
				 OpenApiResource.class, 
				 AcceptHeaderOpenApiResource.class,
				 EFiRest.class,
				 PromoteurRest.class,
				 CreditBancaireRest.class,
				 SessionRest.class,
				 FournisseurAuthentificationRest.class,
				 PaysRest.class,
				 CaracteristiqueDemandeReservationLogementRest.class,
				 TypeDocumentDemandeReservationLogementRest.class,
				 LogementRest.class
				 ).collect(Collectors.toSet());
		 
	 }
}