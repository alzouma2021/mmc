package com.siliconwise.mmc.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.common.config.data.DataConfigException;
import com.siliconwise.common.entity.IDerniereModification;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.entity.IPremiereModification;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.user.User;

public class EntityInitializer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private User loggedInUser = null ;
	//@Inject private UserDAO userDAO ;
	
	private IEntityStringkey entity = null ;
	private Class<?> entityClass = null ;
	
	@PersistenceContext private EntityManager entityManager = null ;
	
	@SuppressWarnings("unused")
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName());

	// =========== Accessors ===============

	// =========== Contructors ===============

	/*
	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	*/

	public EntityInitializer() {
	}
 
	/**
	 * permet de mettre a jour les champ de traçabilité
	 */
	private void initialiserDerniereModification() {
		
		if (!(this.entity  instanceof IDerniereModification)) return ;
		
		if (this.loggedInUser == null) return ;
	
		// TODO Actualiser 
		/*
		String auteur = loggedInUser.getFullname() ;
		
		String idAuteur =  loggedInUser.getId() ;
		
		logger.info(getClass().getName() +" :: initialiserDerniereModification() auteur = "+ auteur
				+" :: initialiserDerniereModification() idAuteur ="+idAuteur);
		
		((IDerniereModification) this.entity).setDateHeureDerniereModification(LocalDateTime.now());
		
		((IDerniereModification) this.entity).setAuteurDerniereModification(auteur);
		((IDerniereModification) this.entity).setAuteurIdDerniereModification(idAuteur);
	    */
	}

	/**
	 * permet de mettre a jour les champ de traçabilité
	 */
	private void initialiserPremiereModification() {
		
		if (!(this.entity  instanceof IPremiereModification)) return ;
		
		if (this.loggedInUser == null) return ;
		
		// TODO Actualiser avec 
		/*
	
		String auteur = this.loggedInUser.getFullname() ;
		
		String idAuteur = this.loggedInUser.getId() ;
		
		((IPremiereModification) this.entity).setDateHeureCreation(LocalDateTime.now()) ;
		((IPremiereModification) this.entity).setAuteurCreation(auteur);
		((IPremiereModification) this.entity).setAuteurIdCreation(idAuteur);
		*/
	}

	// =========== Life cycle call back ===============
	/**
	 * permet d'initialiser ou de mettre à jour les champs de la traçabilité
	 * @throws DataConfigException
	 * @throws AutoNumberException
	 */
	public void prePersist() 
		throws DataConfigException {
					
		if (this.entity instanceof IDerniereModification)	initialiserDerniereModification();
		
		if (entity instanceof IPremiereModification) initialiserPremiereModification(); 
	}

	public void preUpdate() {

			if (this.entity instanceof IDerniereModification) initialiserDerniereModification();
	}
	
	public  IEntityStringkey initializeBeforeSaving(
			Class<?> myClass, IEntityStringkey myEntity) throws Exception {

		this.entity = myEntity ;
		this.entityClass = myClass ;
	//	this.loggedInUser = myLoggedInUser ;
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;

		if (this.entity == null) return null ;

		Object savedObject = this.entity.getId() == null 
				? null 
				: entityManager.find(this.entityClass, this.entity.getId())	;


		if (savedObject == null) prePersist();
		else preUpdate(); 

		return this.entity ; 
	}
	/*
	public  IEntityStringkey initializeBeforeSaving(
			Class<?> myClass, IEntityStringkey myEntity, User myLoggedInUser) throws Exception {

		this.entity = myEntity ;
		this.entityClass = myClass ;
		this.loggedInUser = myLoggedInUser ;
		
		List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;

		if (this.entity == null) return null ;

		Object savedObject = this.entity.getId() == null 
				? null 
				: entityManager.find(this.entityClass, this.entity.getId())	;


		if (savedObject == null) prePersist();
		else preUpdate(); 

		return this.entity ; 
	}*/
	
	

}