package com.siliconwise.mmc.produitlogement;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;


import javax.servlet.http.HttpServletRequest;


import com.siliconwise.common.AppUtil;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.common.rest.RestResponseCtrl;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.produitlogement.caracteristique.CaracteristiqueProduitLogement;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementBoolean;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDate;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDateTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementDouble;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementFloat;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementInteger;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementLong;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementReference;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementString;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTexte;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementTime;
import com.siliconwise.mmc.produitlogement.caracteristique.ValeurCaracteristiqueProduitLogementVille;
import com.siliconwise.mmc.produitlogement.critere.*;
import com.siliconwise.mmc.security.SessionBag;
import com.siliconwise.mmc.security.SessionCtrl;
import com.siliconwise.mmc.security.SessionUtil;
import com.siliconwise.mmc.user.User;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


/**
 * Cette classe ecrit les APis Rest portant sur les produits logements
 * 
 * @author Alzouma Moussa Mahamadou
 * @date 02/02/2021
 *
 */
@Stateless
@Path("/produitlogements")
@OpenAPIDefinition(info = @Info(title = "Produit Logement Service", version = "1.0", description = "Produits Logements Service APIs : Le Format utilisé pour les Date , DateTime et Time est seul de ISO", contact = @Contact(url = "https://bitbucket.org/siliconwise/", name = "Silicon Wise"), license = @License(name = "License", url = "https://bitbucket.org/siliconwise/")))
public class ProduitLogementRest implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	@Inject private RechercherProduitLogementCtlInterface rechercherProduitLogementCtl ;
	@Inject private CreerModifierUnProduitLogementCtlInterface  creerModifierUnProduitLogementCtlInterface ;
	//@Inject private SessionDAO sessionDAO ;
	@Inject private SessionCtrl sessionCrtl ;
	@Inject private RestResponseCtrl responseCtrl ;
	@Inject private ActiverDesactiverUnProduitLogementCtlInterface activerDesactiverUnProduitLogementCtl ;
	@Inject private ValiderUnProduitLogementCtlInterface validerUnProduitLogementCtl ;
	@Inject private SupprimerUnProduitLogementCtlInterface supprimerUnProduitLogementCtl ;
	
	private static transient Logger logger =  LoggerFactory.getLogger(ProduitLogementRest.class) ;
	
	
	/**
	 * Obtenir les propriétés d'un produit logement
	 * à partir de son Id
	 * @param servletRequest
	 * @param idLogement
	 * @return 
	 */
	@GET  
	@Path("{idLogement}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun produit logement"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Produit logement trouvé",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = ProduitLogement.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Recherche de produit logement par Id",
			description = "Rechercher un produit logement pour pouvoir  consulter ses spécificités"
			)
	public Response rechercherUnProduitLogementParId(
			 @Parameter(
			            description = "Identifiant Id Unique au produit logement",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idLogement") String idLogement){
		
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		/**
		 * Ces deux instructions ci-dessous appartiennent aux anciens codes portant sur la sécurité
		 */
		//SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		//Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		/**
		 * Modification faite Alzouma Moussa Mahamadou : 
		 */
		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		ProduitLogement rtn = null;
		
		// appel de la method du controleur
		
		try {
			
			rtn =  rechercherProduitLogementCtl
					 .rechercherUnProduitLogementParId(idLogement, null, true, ProduitLogement.class);
			
			
             if(rtn != null) {
				
					 if(rtn != null &&  rtn.getCaracteristiqueProduitLogementList() != null) 
						 
						 rtn
						  .setCaracteristiqueProduitLogementList( convertirValeurCaracteristiqueProduitLogementTypeEnCaracteristiqueProduitLogement( rtn.getCaracteristiqueProduitLogementList())); 
			}
				
		
		}
		catch(Exception ex) {
	
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
	
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = null ;
				
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
		
	}

/********************************Recherche de produit logements par promoteur***************************************************/

	@GET  
	@Path("/promoteur/{idPromoteur}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun produit logement trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Produit(s) logement(s) trouvé(s)",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = ProduitLogement.class) )
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Recherche  de produits logements par promoteur",
			description = "Rechercher tous les produits logements appartenant à un promoteur donné "
			)
	public Response rechercherProduitLogementsParPromoteur(
			 @Parameter(
			            description = "Identifiant Id Unique au promoteur",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idPromoteur") String idPromoteur){
		
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;

		boolean estEjbException = false ;
		
		Response.Status status = Response.Status.OK ;
		
		List<ProduitLogement> rtnList = null;
		
		List<ProduitLogement> rtnResponseList = new ArrayList<ProduitLogement>();
		
		// appel de la method du controleur

		try {
			
			
			rtnList =  rechercherProduitLogementCtl
						.rechercherProduitLogementParPromoteur(
						  idPromoteur, null, true, ProduitLogement.class);
			
			if(rtnList != null) {
				
				for(ProduitLogement produit: rtnList) {
					 
					 if(produit != null &&  produit.getCaracteristiqueProduitLogementList() != null) {
						 
						produit
						.setCaracteristiqueProduitLogementList( convertirValeurCaracteristiqueProduitLogementTypeEnCaracteristiqueProduitLogement( produit.getCaracteristiqueProduitLogementList())); 
						
						 rtnResponseList.add(produit);
						 
					 }
					 
				}
				
			}
								
		
		}
		catch(Exception ex) {
	
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
	
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtnResponseList = null ;
		}

		return responseCtrl.sendResponse(rtnResponseList, estEjbException, status, msgList) ;
	}
	

/********************************Retourne la liste de produits logements disponible**********************************************************************************/
	
	/**
	 * @param servletRequest
	 * @return la liste  des produitds logements
	 */
	@GET  
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun produit logement trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Produits Logements trouvés",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = ProduitLogement.class) )
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Obtenir la liste de produits logements crées",
			description = "Retourner la liste de produits logements disponibles"
			)
	public Response rechercherProduitLogementList( @Context HttpServletRequest servletRequest){
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;

		// appel de la method du controleur

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		List<ProduitLogement> rtnList = null;
		
		List<ProduitLogement> rtnResponseList = new ArrayList<ProduitLogement>();

		try {
			
			
			rtnList =  rechercherProduitLogementCtl.rechercherProduitLogementList();
			
			//Conversion des valeurs des caracteristiques des produits logements trouvés en String
			
			if(rtnList != null) {
				
				for(ProduitLogement produit: rtnList) {
					 
					 if(produit != null &&  produit.getCaracteristiqueProduitLogementList() != null) {
						 
						produit
						.setCaracteristiqueProduitLogementList( convertirValeurCaracteristiqueProduitLogementTypeEnCaracteristiqueProduitLogement( produit.getCaracteristiqueProduitLogementList())); 
						
						 rtnResponseList.add(produit);
						 
					 }
					 
				}
				
			}
			
		}
		catch(Exception ex) {
	
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
	
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtnResponseList = null ;
		}
		

		return responseCtrl.sendResponse(rtnResponseList, estEjbException, status, msgList) ;
		
	}
	
	
/****************Recherche de produits logements par critere liste**************************************************************************************************/
	
	
	/**
	 * Obtenir la liste des produits logements correspondant
	 * aux criteres predefins
	 * @param servletRequest
	 * @QueryParam critereList
	 * @return
	 */
	@POST
	@Path("/critereList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun produit logement trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Produits logements trouvés correspondant aux critères",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = ProduitLogement.class))
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Recherche de produits logements par liste de critères",
			description = "Rechercher de produits logements en fonction des criteres prédefinis"
			)
	public Response rechercherProduitLogementParCritereList(
						@RequestBody(
							description = "Recherche de Produits Logements", 
							required = true,
							content = @Content(schema = @Schema(implementation = CritereRechercheProduitLogement.class)))
						@Context HttpServletRequest servletRequest,
						List<CritereRechercheProduitLogement> critereList) {
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean estEjbException = false ;
		
		Response.Status status = Response.Status.OK ;

		List<ProduitLogement> rtnList = null  ;
		
		List<ProduitLogement> rtnResponseList = new ArrayList<ProduitLogement>()  ;
		
		// Appel de la methode: convertirValeurCritereEnFonctionDuTypeDeLaPropriete
		
		this.convertirValeurCritereEnFonctionDuTypeDeLaPropriete(critereList);

		// appel de la method du controleur

		try {
			
				rtnList =  rechercherProduitLogementCtl.rechercherProduitLogementParCritereList(critereList , msgList);
			
				//Conversion des valeurs de caracteristiques en valeur texte
				
				if(rtnList != null) {
					
					for(ProduitLogement produit: rtnList) {
						 
						 if(produit != null &&  produit.getCaracteristiqueProduitLogementList() != null) {
							 
							produit
							.setCaracteristiqueProduitLogementList( convertirValeurCaracteristiqueProduitLogementTypeEnCaracteristiqueProduitLogement( produit.getCaracteristiqueProduitLogementList())); 
							
							 rtnResponseList.add(produit);
							 
						 }
						 
					}
				
				}
				
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtnResponseList = null ;
				
		}

		return responseCtrl.sendResponse(rtnResponseList, estEjbException, status, msgList) ;
	}
	
/**************************Recherche de produits logements par mot cle*********************************************************************************/
	
	/**
	 * @param servletRequest
	 * @param motCles
	 * @return une liste de produits logements
	 */
	@GET  
	@Path("/motcle/{motCles}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucun produit logement trouvé"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Produit(s) logement(s) trouvé(s) ",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = ProduitLogement.class))
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Recherche de produit(s) logement(s) par mot clé",
			description = "Recherche de produit(s) logement(s) par mot clé"
			)
	public Response rechercherProduitLogementParMotCle(
			 @Parameter(
			            description = "Mots clés de la recherche de produits logements",
			            required = true,
			            example = "Villa Duplex 4 piéces",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("motCles") String motCles){
		
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		List <ProduitLogement>  rtnList = null;
		
		List <ProduitLogement>  rtnResponseList = new ArrayList<ProduitLogement>();

		try {
				
				//Appel à la methode
				
				rtnList =  rechercherProduitLogementCtl
									.rechercherProduitLogementParMotCle(motCles);
				
				//Conversion des valuers des caracteristiques du produit logement en valeur de type String
				
				if(rtnList != null) {
					
					for(ProduitLogement produit: rtnList) {
						 
						 if(produit != null &&  produit.getCaracteristiqueProduitLogementList() != null) {
							 
							produit
							.setCaracteristiqueProduitLogementList( convertirValeurCaracteristiqueProduitLogementTypeEnCaracteristiqueProduitLogement( produit.getCaracteristiqueProduitLogementList())); 
							
							 rtnResponseList.add(produit);
							 
						 }
						 
					}
					
				}
		
			}
			catch(Exception ex) {
		
				String msg  = MessageTranslationUtil.translate(locale,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
						AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
						new String[] {}) ;
				
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				
					status = Response.Status.INTERNAL_SERVER_ERROR ;
					estEjbException = true ;
			
					rtnResponseList = null ;
			}

		return responseCtrl.sendResponse(rtnResponseList, estEjbException, status, msgList) ;
	}

	
/******************************Création d'un produit logement**************************************************************************************/
	
	/**
	 * @param servletRequest
	 * @param entity
	 * @return un produit logement créé
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La création  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La création  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = ProduitLogement.class)
			           )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Création  des produits logements",
			description = "Création   des produits logements "
			)
	public Response creerUnProduitLogement(
						@RequestBody(
							description = "Créer  un produit logement", 
							required = true,
							content = @Content(schema = @Schema(implementation = ProduitLogementTransfert.class)))
						@Context HttpServletRequest servletRequest,
						ProduitLogementTransfert entityTransfert) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
	    User loggedInUser = new User();
		Boolean estCreation = true  ;
	    
		// appel de la method du controleur

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
	
		
		//Conversion des valeurs caracteristique
		logger.info("_625 Creation d'un produit logemen="+entityTransfert.getProduitLogement().toString());
		
		
		Set<CaracteristiqueProduitLogement> rtnList = null ;
		
		ProduitLogement entity = entityTransfert.getProduitLogement() ;
		
		if(entity != null && entity.getCaracteristiqueProduitLogementList() != null) {
			
		      rtnList = convertirValeurCaracteristiqueProduitLogementEnFonctionDuTypeDeLaPropriete(entity.getCaracteristiqueProduitLogementList()) ;
			
			entity.setCaracteristiqueProduitLogementList(rtnList);
			
			entityTransfert.setProduitLogement(entity);
			
		}
		
		logger.info("_642 Creation d'un produit logemen="+entityTransfert.getProduitLogement().toString());
	
		
		ProduitLogement rtn = null  ;

		try {
			
			logger.info("_650 execution de la methode creerModifierUnProduitLogementCtlInterface");
			
			rtn =  creerModifierUnProduitLogementCtlInterface
					.creerModifierUnProduitLogement(entityTransfert, mustUpdateExistingNew, 
							namedGraph, isFetchGraph, locale, 
							loggedInUser, msgList,estCreation) ;
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}
	
	
	
/******************************Modification d'un produit logement**************************************************************************************/
	
	/**
	 * @param servletRequest
	 * @param entity
	 * @return un produit logement créé
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La modification  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La modification  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = ProduitLogement.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Modification  des produits logements",
			description = "Modification   des produits logements "
			)
	public Response modifierUnProduitLogement(
						@RequestBody(
							description = "Créer  un produit logement", 
							required = true,
							content = @Content(schema = @Schema(implementation = ProduitLogement.class)))
						@Context HttpServletRequest servletRequest,
						ProduitLogement entity) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
	    User loggedInUser = new User();
		Boolean estCree = false ;
	    
		// appel de la method du controleur

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		
		//Conversion des valeurs caracteristique
		Set<CaracteristiqueProduitLogement> rtnList = null ;
		
		if(entity != null && entity.getCaracteristiqueProduitLogementList() != null) {
			
		    rtnList = convertirValeurCaracteristiqueProduitLogementEnFonctionDuTypeDeLaPropriete(entity.getCaracteristiqueProduitLogementList()) ;
			
			entity.setCaracteristiqueProduitLogementList(rtnList);
			
		}
		

		ProduitLogement rtn = null  ;

		try {
			
			rtn =  creerModifierUnProduitLogementCtlInterface
					.creerModifierUnProduitLogementBis(
					  entity, mustUpdateExistingNew,namedGraph, 
					  isFetchGraph, locale, loggedInUser, msgList,
					  estCree) ;
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}


/*********************************************************Valider un produit logement****************************/

	@PUT
	@Path("{idProduitLogement}/valider")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La validation n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Validation effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "boolean")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Validation  d'un produit logement",
			description = "Validation d'un produit logement"
			)
	public Response validerUnProduitLogement(
						@RequestBody(
						  description = "Valider  un produit logement", 
						  required = true,
						  content = @Content(schema = @Schema(type = "string")))
						@Context HttpServletRequest servletRequest,
						@PathParam("idProduitLogement") String idProduitLogement) {
		
		//Declaration des variables 
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
		
	    User loggedInUser = new User();
	    loggedInUser.setId("11aabbbdfgg");
	    loggedInUser.setNom("alzouma");
	    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	    loggedInUser.setMotDePasse("alzouma2021");

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
	
		boolean rtn = false  ;
		
		try {
			
			// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
			
			rtn =  validerUnProduitLogementCtl
					 .validerUnProduitLogement(
					   idProduitLogement,mustUpdateExistingNew, namedGraph, 
					   isFetchGraph, locale,loggedInUser, msgList) ;
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			logger.info("129_MessageTranslationUtil ::  msg="+msg);
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = false ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}

	
/********************************************************Activer un produit logement****************************/

	@PUT
	@Path("{idProduitLogement}/activer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "L'activation  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Activation effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "boolean")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Activation d'un produit logement",
			description = "Activation d'un produit logement"
			)
	public Response activerUnProduitLogement(
						@RequestBody(
							description = "Activer un produit logement", 
							required = true,
							content = @Content(schema = @Schema(type = "string")))
						@Context HttpServletRequest servletRequest,
						@PathParam("idProduitLogement") String idProduitLogement) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
		
	    User loggedInUser = new User();
	    loggedInUser.setId("11aabbbdfgg");
	    loggedInUser.setNom("alzouma");
	    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	    loggedInUser.setMotDePasse("alzouma2021");

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		boolean rtn = false  ;
		

		try {
			
			// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
			
			rtn =  activerDesactiverUnProduitLogementCtl
							.activerUnProduitLogement(
							  idProduitLogement, mustUpdateExistingNew,namedGraph,
							  isFetchGraph, locale, loggedInUser, msgList) ;
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			logger.info("129_MessageTranslationUtil ::  msg="+msg);
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
		   rtn = false ;
		   
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}

	
/********************************************************Desactiver un produit logement****************************/
	@PUT
	@Path("{idProduitLogement}/desactiver")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La desactivation  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La desactivation effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(type = "boolean")
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "La desactivation d'un produit logement",
			description = "La desactivation d'un produit logement"
			)
	public Response desactiverUnProduitLogement(
						@RequestBody(
							description = "Desactiver un produit logement", 
							required = true,
							content = @Content( schema = @Schema(type = "string")))
						@Context HttpServletRequest servletRequest,
						@PathParam("idProduitLogement") String idProduitLogement) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
		
	    User loggedInUser = new User();
	    loggedInUser.setId("11aabbbdfgg");
	    loggedInUser.setNom("alzouma");
	    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	    loggedInUser.setMotDePasse("alzouma2021");

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		boolean rtn = false  ;
		

		try {
			
			// Appel de  la methode  CreerModifierProgrammeimmobilier du controleur Inject
			
			rtn =  activerDesactiverUnProduitLogementCtl
					      .desactiverUnProduitLogement(idProduitLogement, mustUpdateExistingNew,
							           namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
	
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			logger.info("129_MessageTranslationUtil ::  msg="+msg);
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = false ;
				
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
		
	}

/****************************************Suppression d'un produit logement *****************************************************/
	
	@DELETE
	@Path("{IdProduitLogement}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La suppression  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La supression effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON//,
			       // schema = @Schema(implementation = ProduitLogement.class)
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "La suppression d'un produit logement",
			description = "La suppression d'un produit logement"
			)
	public Response supprimerUnProduitLogement(
			@Parameter(
		            description = "Identifiant Id Unique au produit logement",
		            required = true,
		            example = "1551176445313ABBB1233AZN2331",
		            schema = @Schema(type = "string")
		       )
		 @Context HttpServletRequest servletRequest,
		 @PathParam("IdProduitLogement") String idProduitLogement) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		boolean mustUpdateExistingNew = true ;
		String namedGraph = "graph.produitLogement.id.estActive" ;
		boolean isFetchGraph = true ;
		
	    User loggedInUser = new User();
	    loggedInUser.setId("11aabbbdfgg");
	    loggedInUser.setNom("alzouma");
	    loggedInUser.setEmail("alzoumamoussa18@univmetiers.ci");
	    loggedInUser.setMotDePasse("alzouma2021");

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
		
		boolean rtn = false  ;
		

		try {
			
			// Appel de  la methode  validerEtSupprimier
			
			rtn =  supprimerUnProduitLogementCtl.supprimer(
								idProduitLogement, mustUpdateExistingNew, 
								namedGraph, isFetchGraph, locale, loggedInUser, msgList) ;
					
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			logger.info("129_MessageTranslationUtil ::  msg="+msg);
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = false ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
		
	}

	
	
/****************************************Caracteristiques par produit logement**************************************************/

	@SuppressWarnings("unchecked")
	@GET  
	@Path("{idProduit}/caracteristiques")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "Aucune caracteristique trouvée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "Caracteristique(s) trouvée(s)",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        array = @ArraySchema ( schema = @Schema(implementation = CaracteristiqueProduitLogement.class))
			                )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Recherche  des caracteristiques par produit logement",
			description = "Rechercher toutes les caracteristiques d'un produit logement "
			)
	public Response rechercheCaracteristiquesParProduitLogement(
			 @Parameter(
			            description = "Identifiant Id Unique au produit logement",
			            required = true,
			            example = "1551176445313ABBB1233AZN2331",
			            schema = @Schema(type = "string")
			       )
			 @Context HttpServletRequest servletRequest,
			 @PathParam("idProduit") String idProduit){
		
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
		
		SessionBag sessionBag = sessionCrtl.extractSession(servletRequest, null,  msgList) ;
		Locale locale =  SessionUtil.getLocale(sessionBag)  ;
		
		//Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;
		// appel de la method du controleur

		boolean estEjbException = false ;
		
		Response.Status status = Response.Status.OK ;
		
		List<CaracteristiqueProduitLogement> rtnList = null;

		try {
			
			rtnList =  rechercherProduitLogementCtl
							.rechercherCaracteristiquesParProduitLogement(idProduit, null, true, CaracteristiqueProduitLogement.class);			
			
			//Conversion de valeurcaracteristiques en Caracteristiques 
			
			rtnList = (List<CaracteristiqueProduitLogement>) convertirValeurCaracteristiqueProduitLogementTypeEnCaracteristiqueProduitLogement((Set<CaracteristiqueProduitLogement>) rtnList);
		
		}
		catch(Exception ex) {
	
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
	
			status = Response.Status.INTERNAL_SERVER_ERROR ;
			estEjbException = true ;
		
			rtnList = null ;
				
		}

		return responseCtrl.sendResponse(rtnList, estEjbException, status, msgList) ;
		
	}

/**************************************************Modification d'un produit logement transfert *********************************************************/
	
	/**
	 * @param servletRequest
	 * @param entity
	 * @return un produit logement créé
	 */
	/*
	@Secured
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiResponse(
			responseCode = "204",
			description = "La modification  n'a  pas été effectuée"
	        )
	@ApiResponse(
			responseCode = "200",
			description = "La modification  effectuée",
			content = @Content(
			        mediaType = MediaType.APPLICATION_JSON,
			        schema = @Schema(implementation = ProduitLogement.class)
			           )
	        )
	@ApiResponse(
			responseCode = "500",
			description = "Erreur côté Serveur"
	        )
	@Operation(
			summary = "Modification  des produits logements",
			description = "Modification   des produits logements "
			)
	public Response modifierUnProduitLogement(
						@RequestBody(
							description = "modifier  un produit logement", 
							required = true,
							content = @Content(schema = @Schema(implementation = ProduitLogementTransfert.class)))
						@Context HttpServletRequest servletRequest,
						ProduitLogementTransfert entityTransfert) {
		
		//Declaration des variables 
		List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;

		SessionBag sessionBag = sessionDAO.getSession(servletRequest, msgList) ;
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		boolean mustUpdateExistingNew = true ;
		String namedGraph = null ;
		boolean isFetchGraph = true ;
	    User loggedInUser = new User();
		Boolean estCreation = false  ;
	    
		// appel de la method du controleur

		boolean estEjbException = false ;
		Response.Status status = Response.Status.OK ;
	
		
		//Conversion des valeurs caracteristique
		
		logger.info("_1281 modification d'un produit logemen="+entityTransfert.getProduitLogement().toString());
		
		
		Set<CaracteristiqueProduitLogement> rtnList = null ;
		
		ProduitLogement entity = entityTransfert.getProduitLogement() ;
		
		if(entity != null && entity.getCaracteristiqueProduitLogementList() != null && !entity.getCaracteristiqueProduitLogementList().isEmpty()) {
			
		      rtnList = convertirValeurCaracteristiqueProduitLogementEnFonctionDuTypeDeLaPropriete(entity.getCaracteristiqueProduitLogementList()) ;
			
			entity.setCaracteristiqueProduitLogementList(rtnList);
			
			entityTransfert.setProduitLogement(entity);
			
		}
		
		logger.info("_1297 modification d'un produit logement="+entityTransfert.getProduitLogement().toString());
	
		
		ProduitLogement rtn = null  ;

		try {
			
			logger.info("_1304 execution de la methode creerModifierUnProduitLogementCtlInterface");
			
			rtn =  creerModifierUnProduitLogementCtlInterface
					.creerModifierUnProduitLogement(entityTransfert, mustUpdateExistingNew, 
							namedGraph, isFetchGraph, locale, 
							loggedInUser, msgList,estCreation) ;
		}
		catch(Exception ex) {
		
			String msg  = MessageTranslationUtil.translate(locale,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY,
					AppUtil.DYNAMIC_MSG_CODE_TRADUCTION_ERROR_RETRY, 
					new String[] {}) ;
			
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
		
				status = Response.Status.INTERNAL_SERVER_ERROR ;
				estEjbException = true ;
		
				rtn = null ;
		}

		return responseCtrl.sendResponse(rtn, estEjbException, status, msgList) ;
	}*/
	
	
/******************************************************************************************************************************************************/
	
	

	@SuppressWarnings({ "static-access" })
	public  List<CritereRechercheProduitLogement> convertirValeurCritereEnFonctionDuTypeDeLaPropriete(List<CritereRechercheProduitLogement> listCritere){	 
		
		//Verification de l'argument de la methode
		
		if(listCritere == null || listCritere.isEmpty() ) return new ArrayList<>();
		
		//Convertir la valeur de chaque  critere de la liste en fonction du type de la propriete dudit critere 
		//		Verifier le type de chacune de propriete de la liste et en fonction du type de ladite propriete,convertir la valeur du critere courant
		//		Alimenter la classe valeur correspondante au type de ladite propriete courante à par la valeur convertie  dudit critere
		
		for(CritereRechercheProduitLogement critere: listCritere )
		{
			  
		 if(critere != null ) {
			 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_INTEGER))
				 { 		
					 	
					 	ValeurInteger valeur =  new ValeurInteger();
					 	critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_BOOLEAN))
				 { 
					 
					 	ValeurBoolean valeur = new ValeurBoolean();
					 	critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATE))
				 { 
					 	
					 	ValeurDate valeur = new ValeurDate();
						critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());		
				 }
				 
				 
				
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATETIME))
				 { 
					 
					    ValeurDateTime valeur = new ValeurDateTime();
						critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DOUBLE))
				 { 	
					 	
					 
					   
					   try {
						   
					 		
					 	if( critere.getOperateurCritere().getCode() != "BETWEEN" && critere.getValeurCritere().getValeurTexte().size() != 2) return null ;
					 		
					 	}catch(Exception ex){
					 		
					 		return null ;
					 	}
					   
					 	ValeurDouble valeur = new ValeurDouble();
						critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
						
				 }
				 
			
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_FLOAT))
				 { 		
					 
					 	try {
					 		
					 	if( critere.getOperateurCritere().getCode().equals("BETWEEN") && critere.getValeurCritere().getValeurTexte().size() != 2) return null ;
					 		
					 	}catch(Exception ex){
					 		
					 		return null ;
					 	}
					 	
						ValeurFloat valeur = new ValeurFloat();
						critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_LONG))
				 { 
						ValeurLong valeur = new ValeurLong();
						critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_STRING))
				 { 
					 
						 ValeurString valeur = new ValeurString() ;
						 critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TEXTE))
				 { 
					 	ValeurTexte valeur = new ValeurTexte() ;
					 	critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TIME))
				 { 
						    ValeurTime valeur = new ValeurTime();
							critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_REFERENCE))
				 { 
					 
						 ValeurString valeur = new ValeurString() ;
						 critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
				 if (critere.getProprieteProduitLogement() != null 
						 &&  critere.getProprieteProduitLogement().getType() != null
						 &&	 critere.getProprieteProduitLogement().getType().getId() != null
						 &&  critere.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_VILLE))
				 { 
					 
						 ValeurVille valeur = new ValeurVille() ;
						 critere.getValeurCritere().setValeur( (valeur.from((Valeur<Object>) critere.getValeurCritere())).getValeur());
				 }
				 
		 	}
			 
		}

		
		//Retourner la liste de critere traitée 
		
		return listCritere ;
		
	}


/************************************************************CaracteristiqueProduitLogement*********************************************************/
	
	@SuppressWarnings("static-access")
	public  Set<CaracteristiqueProduitLogement> convertirValeurCaracteristiqueProduitLogementEnFonctionDuTypeDeLaPropriete( Set<CaracteristiqueProduitLogement>  caracteristiqueProduitLogementList){	 
		
		
		//Verification de l'argument de la methode
		
		if(caracteristiqueProduitLogementList == null 
				|| caracteristiqueProduitLogementList.isEmpty() ) return new  HashSet<>();
		
		Set<CaracteristiqueProduitLogement> rtnList = new HashSet<CaracteristiqueProduitLogement>() ;
		
		//Convertir la valeur de chaque  critere de la liste en fonction du type de la propriete dudit critere 
		//		Verifier le type de chacune de propriete de la liste et en fonction du type de ladite propriete,convertir la valeur du critere courant
		//		Alimenter la classe valeur correspondante au type de ladite propriete courante à par la valeur convertie  dudit critere
		
		for(CaracteristiqueProduitLogement caracteristique: caracteristiqueProduitLogementList )
		{
			
			if(caracteristique != null) {
				
			
				 if (caracteristique != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_INTEGER))
				 { 		
					    
					 	ValeurCaracteristiqueProduitLogementInteger valeur =  new ValeurCaracteristiqueProduitLogementInteger();
					 	
					 	rtnList.add( valeur.from(caracteristique) ) ;
					 	
				 }
				 
				 
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_BOOLEAN))
				 { 
					 
					 ValeurCaracteristiqueProduitLogementBoolean valeur = new ValeurCaracteristiqueProduitLogementBoolean();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
					  
				 }
				 
				 
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATE))
				 { 
					 	
					  ValeurCaracteristiqueProduitLogementDate valeur = new ValeurCaracteristiqueProduitLogementDate();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
				 
				
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DATETIME))
				 { 
					 
					  ValeurCaracteristiqueProduitLogementDateTime valeur = new ValeurCaracteristiqueProduitLogementDateTime();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
				
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_DOUBLE))
				 { 	
					 	
					  ValeurCaracteristiqueProduitLogementDouble valeur = new ValeurCaracteristiqueProduitLogementDouble();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
			
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_FLOAT))
				 { 		
					 
					  ValeurCaracteristiqueProduitLogementFloat valeur = new ValeurCaracteristiqueProduitLogementFloat();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
				
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_LONG))
				 { 
				      
				      ValeurCaracteristiqueProduitLogementLong valeur = new ValeurCaracteristiqueProduitLogementLong();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_STRING))
				 { 
					 
					 ValeurCaracteristiqueProduitLogementString valeur = new ValeurCaracteristiqueProduitLogementString();
					 
					 rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TEXTE))
				 { 
					  
					  ValeurCaracteristiqueProduitLogementTexte valeur = new ValeurCaracteristiqueProduitLogementTexte();
					
					  rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_TIME))
				 { 
					  
					  ValeurCaracteristiqueProduitLogementTime valeur = new ValeurCaracteristiqueProduitLogementTime();
					
					  rtnList.add( valeur.from(caracteristique) ) ;
				 }
				 
				 
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_REFERENCE))
				 { 
					 
					  ValeurCaracteristiqueProduitLogementReference valeur = new ValeurCaracteristiqueProduitLogementReference();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
					  
				 }
				 
				 if (caracteristique.getProprieteProduitLogement() != null 
						 &&  caracteristique.getProprieteProduitLogement().getType() != null
						 &&	 caracteristique.getProprieteProduitLogement().getType().getId() != null
						 &&  caracteristique.getProprieteProduitLogement().getType().getId().equals(Reference.REF_ELEMENT_ID_TYPE_VALEUR_VILLE))
				 { 
					 
					  ValeurCaracteristiqueProduitLogementVille valeur = new ValeurCaracteristiqueProduitLogementVille();
					 
					  rtnList.add( valeur.from(caracteristique) ) ;
					  
				 }
			
			}
				 
		}

		//Retourner la liste de critere traitée 
		
		return rtnList ;
		
	}
	
	
	    /**
	     *Cette methode de convertir les valeurs de caracteristiques d'un produit logement en
	     *CarateristiqueProduitLogement de sorte à pouvoirn les transmettre au front-end
	     * @return  List caracteristique produit logement
	     */
		@SuppressWarnings("static-access")
		public  Set<CaracteristiqueProduitLogement> convertirValeurCaracteristiqueProduitLogementTypeEnCaracteristiqueProduitLogement( Set<CaracteristiqueProduitLogement>  caracteristiqueProduitLogementList){	 
			
			
			
			//Verification de l'argument de la methode
			
			if(caracteristiqueProduitLogementList == null 
							|| caracteristiqueProduitLogementList.isEmpty() ) return new  HashSet<>();
			
			Set<CaracteristiqueProduitLogement> rtnList = new  HashSet<>();

			for(CaracteristiqueProduitLogement caracteristique: caracteristiqueProduitLogementList )
			{
				if(caracteristique != null) {
					
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementInteger)
					 { 		
						 
						 	ValeurCaracteristiqueProduitLogementInteger valeur =  new ValeurCaracteristiqueProduitLogementInteger();
						 	
						 	rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementInteger) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementBoolean)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementBoolean valeur =  new ValeurCaracteristiqueProduitLogementBoolean();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementBoolean) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementDate)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementDate valeur =  new ValeurCaracteristiqueProduitLogementDate();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementDate) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementDateTime)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementDateTime valeur =  new ValeurCaracteristiqueProduitLogementDateTime();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementDateTime) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementDouble)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementDouble valeur =  new ValeurCaracteristiqueProduitLogementDouble();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementDouble) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementFloat)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementFloat valeur =  new ValeurCaracteristiqueProduitLogementFloat();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementFloat) caracteristique) ) ;
						 	
					 }
					 
					
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementLong)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementLong valeur =  new ValeurCaracteristiqueProduitLogementLong();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementLong) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementString)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementString valeur =  new ValeurCaracteristiqueProduitLogementString();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementString) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementTexte)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementTexte valeur =  new ValeurCaracteristiqueProduitLogementTexte();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementTexte) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementTime)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementTime valeur =  new ValeurCaracteristiqueProduitLogementTime();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementTime) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementReference)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementReference valeur =  new ValeurCaracteristiqueProduitLogementReference();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementReference) caracteristique) ) ;
						 	
					 }
					 
					 if (caracteristique instanceof ValeurCaracteristiqueProduitLogementVille)
					 { 		
						 
						 ValeurCaracteristiqueProduitLogementVille valeur =  new ValeurCaracteristiqueProduitLogementVille();
						 	
						 rtnList.add( valeur.to((ValeurCaracteristiqueProduitLogementVille) caracteristique) ) ;
						 	
					 }
					 
				}
				
			}

			
			//Retourner la liste de critere traitée 
			
			return rtnList ;
			
			
		}
		

}
	
