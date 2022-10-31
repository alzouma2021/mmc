/**
 * 
 */
package com.siliconwise.mmc.common.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.demandereservationlogement.financement.FinancementPallierComptantSurSituation;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.modefinancement.PallierComptantSurSituation;



/**
 * Entity utility metods
 * @author GNAKALE Bernardin
 *
 */
@NamedEntityGraphs(value={
		
		// Generic graphwith only id property
		@NamedEntityGraph(
				name="graph.any-entity.id-version-only", 
				attributeNodes={
					@NamedAttributeNode(value="id"),
					@NamedAttributeNode(value="version")})
})
public class EntityUtil<T extends IEntityStringkey> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	// Fetch hint
	
	public static final String PERSISTENCE_HINT_FETCH_GRAPH = "javax.persistence.fetchgraph" ;
	public static final String PERSISTENCE_HINT_LOAD_GRAPH = "javax.persistence.loadgraph" ;

	// persistence optimistic lock exception message
	
	public static final String GENERAl_MSG_PERSISTENCE_OPTIMISTiC_LOCK_ERROR = "general.persistence.optimistic-lock.error" ;

	// Message codes
	
	private static final String NAMED_GRAPH_ANY_ENTITY_ID_VERSION_ONLY = "graph.any-entity.id-version-only" ;
	
	private Class<T> currentClass = null ;

	private static transient Logger logger = LoggerFactory.getLogger(EntityUtil.class) ;

	public EntityUtil() {}
	
	public EntityUtil(Class<T> myClass) {
		this.currentClass = myClass ;
	}


	/**
	 * Exception throws when cuurentClass is needed and it's null.
	 * @author bgnakale
	 *
	 */
	public static class ClassNotInitializedException extends Exception {

		private static final long serialVersionUID = 1L;

		public ClassNotInitializedException() {
			super();
		}

		public ClassNotInitializedException(String message, Throwable cause, boolean enableSuppression,
				boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public ClassNotInitializedException(String message, Throwable cause) {
			super(message, cause);
		}

		public ClassNotInitializedException(String message) {
			super(message);
		}

		public ClassNotInitializedException(Throwable cause) {
			super(cause);
		}
	}
	
	public T findEntityById(EntityManager entityManager, 
			String id, String namedGraphName, boolean isfetchGraph) 
		throws ClassNotInitializedException  {
		
		if (id == null) return null ;
		
		if (this.currentClass == null) throw new ClassNotInitializedException() ;
		
		Map<String, Object> hints = new HashMap<>();
		
		if (namedGraphName != null) {
		
			@SuppressWarnings("unchecked")
			EntityGraph<T> graph = (EntityGraph<T>) entityManager.getEntityGraph(namedGraphName);
			
			hints.put(
				isfetchGraph ? PERSISTENCE_HINT_FETCH_GRAPH : PERSISTENCE_HINT_LOAD_GRAPH,  
						graph) ;
		}
		
		T rtn = hints.isEmpty() 
						?  entityManager.find(this.currentClass, id) 
						:  entityManager.find(this.currentClass, id, hints) ;
		
		return rtn ;
	}
	
	/**
	 * Find entity by id
	 * @param <E>
	 * @param entityManager
	 * @param id
	 * @param namedGraphName
	 * @param isfetchGraph
	 * @param entityClass
	 * @return
	 */
	public static <E> E findEntityById(EntityManager entityManager, 
			String id, String namedGraphName, boolean isfetchGraph, 
			Class<E> entityClass)  {
		
		if (id == null) return null ;//new String[] {}
		
		Map<String, Object> hints = new HashMap<>();
		
		if (namedGraphName != null) {
		
			@SuppressWarnings("unchecked")
			EntityGraph<E> graph = (EntityGraph<E>) entityManager.getEntityGraph(namedGraphName);
			
			hints.put(
				isfetchGraph ? PERSISTENCE_HINT_FETCH_GRAPH : PERSISTENCE_HINT_LOAD_GRAPH, 
				graph) ;
		}

		logger.info("_154 findEntityById :: id="+id+" class="+entityClass.getName()+ " namedGraph="+namedGraphName);
		
		E rtn = hints.isEmpty() 
						?  entityManager.find(entityClass, id) 
						:  entityManager.find(entityClass, id, hints) ;
		
		return rtn ;
	}
		
	/** Compute the number of objects of an entity class given its id
	 * @param entityManager
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static Long nbrOfEntityInstancesById(EntityManager entityManager, @SuppressWarnings("rawtypes") Class entityClass, String id) {
		
		if (id == null || id.isEmpty() || entityClass == null) return 0L ;
		
		String sql = "SELECT COUNT(DISTINCT e.id) FROM :entityName: e WHERE e.id=:id" ;
		
		sql = sql.replaceAll(":entityName:", entityClass.getSimpleName()) ;
		
		Long rtn = (Long) entityManager.createQuery(sql)
						.setParameter("id", id)
						.getSingleResult();

		logger.info("_169 nbrOfEntityInstancesById :: nbr of entities=" + rtn + " sql="+sql);

		return rtn ;
	}
	
	/**
	 * Launch a named query that return a list of objects of type E. E can be an entity or not
	 * @param entityManager
	 * @param namedQuery
	 * @param fieldNameArray
	 * @param fieldValueArray
	 * @param namedGraph for query returning an entity, this is the graph name of the returned entitie
	 * @param isFetchHint
	 * @return
	 */
	public <E> List<E> findListByFieldValues(EntityManager entityManager, String namedQuery, 
			String[] fieldNameArray, Object[] fieldValueArray,
			String namedGraph, boolean isFetchHint, E anyInstanceOfReturnedObect) {
				
		if (namedQuery == null || fieldNameArray == null || fieldValueArray == null
				|| fieldNameArray.length != fieldValueArray.length) 
			throw new IllegalArgumentException()  ;
		
		Query query = entityManager.createNamedQuery(namedQuery) ;
		
		for (int i = 0 ; i < fieldNameArray.length ; i++) 
			query = query.setParameter(fieldNameArray[i], fieldValueArray[i]) ;
		
		if (namedGraph != null) {
			
			@SuppressWarnings("unchecked")
			EntityGraph<T> graph = (EntityGraph<T>) entityManager.getEntityGraph(namedGraph);
			
			query.setHint(isFetchHint ? PERSISTENCE_HINT_FETCH_GRAPH : PERSISTENCE_HINT_LOAD_GRAPH, 
					graph) ;
		}
		
		@SuppressWarnings("unchecked")
		List<E> rtn = (List<E>) query.getResultList() ;
		
		return rtn ;
	}

	/** Launch a named query that return a single object of type E. E can be an entity or not
	 * @param entityManager
	 * @param namedQuery
	 * @param fieldNameArray
	 * @param fieldValueArray
	 * @param namedGraph for query returning an entity, this is the graph name of the returned entitie
	 * @param isFetchHint
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> E findSingleResultByFieldValues(EntityManager entityManager, String namedQuery, 
			String[] fieldNameArray, Object[] fieldValueArray,
			String namedGraph, boolean isFetchHint, E anyInstanceOfReturnedObect)  {
		
		
		logger.info("_254 findSingleResultByFieldValues :: namedQuery="+namedQuery); //TODO A effacer
		logger.info("_255 findSingleResultByFieldValues :: fieldNameArray="+fieldNameArray.toString() + "  "+ fieldNameArray.length); //TODO A effacer
		logger.info("_255 findSingleResultByFieldValues :: fieldValueArray="+fieldValueArray.toString()+ " "+ fieldValueArray.length); //TODO A effacer
		
		if (namedQuery == null || namedQuery.isEmpty() || fieldNameArray == null 
				|| fieldValueArray == null || fieldNameArray.length != fieldValueArray.length) 
			throw new IllegalArgumentException()  ;
		
		logger.info("_256 Execution de la requête ");  //TODO A effacer
		
		Query query = entityManager.createNamedQuery(namedQuery) ;
		
		
		for (int i = 0 ; i < fieldNameArray.length ; i++) 
			 query = query.setParameter(fieldNameArray[i], fieldValueArray[i]) ;
			
			 
		//logger.info("_256 findSingleResultByFieldValues :: query="+query.getSingleResult()); //TODO A effacer
		
		if (namedGraph != null) {
			
			@SuppressWarnings("unchecked")
			EntityGraph<T> graph = (EntityGraph<T>) entityManager.getEntityGraph(namedGraph);
			
			query.setHint(isFetchHint ? PERSISTENCE_HINT_FETCH_GRAPH : PERSISTENCE_HINT_LOAD_GRAPH, 
					graph) ;
		}
		
		E rtn = null ;
		
		try {
			
			rtn = (E) query.getSingleResult() ;
			
		}catch (NoResultException e) {
			
			rtn = null ;
		}
	
		
		return rtn ; 
		
	}
	
	public static boolean doesEntityExistInDB(EntityManager entityManager,  Class<?> myClass, String id) {
		
		if (id == null) return false ;
		
		return  nbrOfEntityInstancesById(entityManager, myClass, id) > 0L;
	}
	
	public static <E extends IEntityStringkey> E persistOrMerge(EntityManager entityManager, Class<E> myClass, E entity)
	{
		return persistOrMerge(entityManager, myClass, entity,  null, true) ;
		
		/*if (entity == null) return null ;
		
		//Object savedObject = entity.getId() != null ? entityManager.find(myClass, entity.getId()) : null ;
	    
		boolean isEntityInDB  = doesEntityExistInDB(entityManager, myClass, entity.getId()) ;
		
		logger.info("_294 persistOrMerge :: isEntityInDB="+isEntityInDB+ " myClass="+myClass) ;
		
		E savedObject = null ;
		
		if (!isEntityInDB) {
			logger.info("_332  persistOrMerge :: avant persist isEntityInDB = "+isEntityInDB + " " + entity) ;
			entityManager.persist(myClass.cast(entity));
		}
		else savedObject = entityManager.merge(myClass.cast(entity));	
		
		logger.info("_301 persistOrMerge :: savedObject="+savedObject) ;
		
		return savedObject == null ? entityManager.find(myClass, entity.getId()) : savedObject ;*/
	}
	
	public static <E extends IEntityStringkey> E persistOrMerge(
			EntityManager entityManager, 
			Class<E> myClass, E entity, 
			String namedGraphName, boolean isfetchGraph)
	{
		
		
		if (entity == null) return null ;
		
		//Object savedObject = entity.getId() != null ? entityManager.find(myClass, entity.getId()) : null ;
	    
		boolean isEntityInDB  = doesEntityExistInDB(entityManager, myClass, entity.getId()) ;
		logger.info("_319 persistOrMerge :: isEntityInDB="+isEntityInDB+ " myClass="+myClass) ;
		
		E savedObject = null ;
		
		if (!isEntityInDB) {
			
			entityManager.persist(myClass.cast(entity)) ;
			
		}
		else {
			
			logger.info("_328  persistOrMerge :: avant merge isEntityInDB = "+isEntityInDB + " " + entity) ;
			//E e = findEntityById(entityManager, entity.getId(), namedGraphName, isfetchGraph, myClass) ;
			savedObject = entityManager.merge(myClass.cast(entity));	
		}
		
		return savedObject == null 
				? findEntityById(entityManager, entity.getId(), namedGraphName, isfetchGraph, myClass) 
				: savedObject ;
				
	}

	public static <E extends IEntityStringkey> E  persistOrMerge(
			EntityManager entityManager, 
			Class<E> myClass, E entity, 
			String namedGraph, boolean isfetchGraph,
			String entityExistsExceptionTrCode, Map<String,String> entityExistsExceptionVariablesMap,
			String persistenceExceptionTrCode, Map<String,String> persistenceExceptionVariablesMap,
			String generalExceptionTrCode, Map<String,String> generalExceptionVariablesMap,
			Locale langue, List<NonLocalizedStatusMessage> msgList) {
		
		E rtn = null ;
		
		try {
			
			rtn = (E) persistOrMerge(entityManager, myClass, entity, namedGraph, isfetchGraph) ;
			
		}
		catch (EntityExistsException ex) {
			
			String translatedMessage = MessageTranslationUtil.translate(langue,
					entityExistsExceptionTrCode, entityExistsExceptionTrCode,
					entityExistsExceptionVariablesMap);
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
			
			logger.error("_354 persistOrMerge :: " + translatedMessage + " " 
							+ ex + ":" + ex.getMessage() + " Cause:"+ex.getCause());
			ex.printStackTrace();
			return null ;
		}
		catch (OptimisticLockException ex) {
			
			String translatedMessage = MessageTranslationUtil.translate(langue,
					GENERAl_MSG_PERSISTENCE_OPTIMISTiC_LOCK_ERROR, 
					GENERAl_MSG_PERSISTENCE_OPTIMISTiC_LOCK_ERROR,
					new String[] {});
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
			
			logger.error("_371 persistOrMerge :: " + translatedMessage + " " 
							+ ex + ":" + ex.getMessage() + " Cause:"+ex.getCause());
			
			ex.printStackTrace();
			
			return null ;
		}
		catch (PersistenceException ex) {
			
			String translatedMessage = MessageTranslationUtil.translate(langue,
					persistenceExceptionTrCode, persistenceExceptionTrCode,
					persistenceExceptionVariablesMap);
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
			
			logger.error("_384 persistOrMerge :: " + translatedMessage + " " 
							+ ex + ":" + ex.getMessage() + " Cause:"+ex.getCause());
			ex.printStackTrace();
			
			return null ;
			
		}
		catch (Exception ex) {
			
			String translatedMessage = MessageTranslationUtil.translate(langue,
					generalExceptionTrCode, generalExceptionTrCode,
					generalExceptionVariablesMap);
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));
			
			logger.error("_397 persistOrMerge :: " + translatedMessage + " " 
							+ ex + ":" + ex.getMessage() + " Cause:"+ex.getCause());
			ex.printStackTrace();
			
			return null ;
			
		}
		
		return rtn ;
	}
	
	public T persisteOrMerge(EntityManager entityManager, T entity, String namedGraphName, boolean isfetchGraph) 
		throws ClassNotInitializedException {
		
		
		if (entity == null) return null ;
		
		Long nbr = nbrOfEntityInstancesById(entityManager, entity.getClass(), entity.getId()) ;
		
		T savedObject = null ;
							
		if (nbr == 0) entityManager.persist(entity);
		else savedObject = entityManager.merge(entity);	
		
		return savedObject == null 
					? findEntityById(entityManager, entity.getId(), namedGraphName, isfetchGraph)
					: savedObject ;
					
	}
	
	/*public static void removeEntity(EntityManager entityManager, IEntityStringkey entity) {
		
		if (entity == null) return ;
		
		entityManager.remove(entity);
	}*/
	
	/**
	 * Attach a linked entity instance with its managed version if the linked entity i snot yet managed
	 * by the entity manager.
	 * @param entityManager
	 * @param entity entity to attache to
	 * @param linkedEntityClass linked entity class. must implements IEntityStringKey
	 * @param getter entity getter that return the linked entity
	 * @param setter entity setter that intialise the association to the the linked entity
	 * @param linkedGraphName linked entity named grah name
	 * @param isfetchGraph linked entity fetch mode 
	 * @param langue
	 * @param codeTraductionMsg code for message translation
	 * @param msgVarArray array of variable for message translation 
	 * @param msgList list of messages return by the current method
	 * @return true if success otherwise false
	 * @throws IllegalArgumentException throws if getter, setter, linkedEntity or entity is null
	 * @throws IllegalAccessException throws by getter or setter execution in reflection mode
	 * @throws InvocationTargetException throws by getter or setter execution in reflection mode
	 */
	@SuppressWarnings({ "static-access", "unchecked", "rawtypes" })
	public <E extends IEntityStringkey> boolean attachLinkedEntity(EntityManager entityManager, T entity, 
					E linkedEntity, Method setter, 
					String linkedGraphName, boolean isfetchGraph,
					Locale langue, String codeTraductionMsg, Map<String,String>  msgVarMap,
					List<NonLocalizedStatusMessage> msgList) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		
		if (setter == null || entity == null) 
			throw new IllegalArgumentException() ;
	
		
		if (linkedEntity != null && !entityManager.contains(linkedEntity.getClass().cast(linkedEntity))) {
			
			E savedLinkedEntity = null ;
			
            logger.info("_478 Id="+ linkedEntity.getId());  //TODO A effacer 
			savedLinkedEntity = (E) (new EntityUtil())
					.findEntityById(entityManager, linkedEntity.getId(), linkedGraphName, isfetchGraph, 
								linkedEntity.getClass());
			
			logger.info("_479 Debut savedLinkedEntity="+savedLinkedEntity); //TODO A effacer 
			if (savedLinkedEntity == null) {
				
				if (codeTraductionMsg != null && !codeTraductionMsg.isEmpty()) {
			
					String msg  = MessageTranslationUtil.translate(langue ,
						codeTraductionMsg , codeTraductionMsg, msgVarMap) ;
					msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				}
				
				return false ;
			}
			
			setter.invoke(entity, (E) savedLinkedEntity) ;
		}
		
		return true ;
	}

	/*
	public <E extends IEntityStringkey> boolean attachLinkedEntityList(EntityManager entityManager, T entity, 
			Set<E> linkedEntityList, Method setter,  
			Locale langue, String codeTraductionMsg, Method[] msgGetterVarArray ,
			List<NonLocalizedStatusMessage> msgList, Class<E> myClass) 
		throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		return attachLinkedEntityList(entityManager, entity, 
				linkedEntityList, setter, null, true,
				langue, codeTraductionMsg, msgGetterVarArray,
				msgList, myClass) ;
	}*/
	
	public <E extends IEntityStringkey> boolean attachLinkedEntityList(EntityManager entityManager, T entity, 
			Set<E> linkedEntityList, Method setter,  
			String linkedGraphName, boolean isfetchGraph,
			Locale langue, String codeTraductionMsg, String getMsgVarMapMethodName /*Method[] msgGetterVarArray*/,
			List<NonLocalizedStatusMessage> msgList, Class<E> myClass) 
		throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		
		if (entity == null || setter == null 
				|| getMsgVarMapMethodName == null || getMsgVarMapMethodName.isEmpty()) 
			throw new IllegalArgumentException() ;

		if (linkedEntityList == null || linkedEntityList.isEmpty()) return true ;
		
		
		
		Set<E> savedList = linkedEntityList ;
		linkedEntityList = new HashSet<>() ;
		
		for (E item : savedList) {
			
			if (item != null && !entityManager.contains(item)) {
				
				logger.info("_528 attachLinkedEntityList ::  item="+item);
				E savedItem = EntityUtil.findEntityById(
						entityManager, item.getId(), 
						linkedGraphName, isfetchGraph, myClass);
				
				if (savedItem == null) {
			
					if (codeTraductionMsg != null && !codeTraductionMsg.isEmpty()) {
		
						// construct Map of msg variable values
						/*
						String[] msgVarArray = msgGetterVarArray.length == 0 
								? new String[] {} : new String[msgGetterVarArray.length] ;*/
						
						//TODO modification faite par alzouma moussa 30/03/2022
						
						Method getMsgVarMapMethod = item.getClass().getDeclaredMethod(getMsgVarMapMethodName, item.getClass());
						
						//Method getMsgVarMapMethod = item.getClass().getDeclaredMethod(getMsgVarMapMethodName, 
								//				item.getClass());
						
						@SuppressWarnings("unchecked")
						Map<String, String> msgVarMap =  (Map<String, String>) getMsgVarMapMethod.invoke(item);
						/*
						 * 
						int i = 0 ;
						for (Method msgVarGetter : msgGetterVarArray) {
							
							logger.info("_511 attachLinkedEntityList :: getter="+msgVarGetter.getName()+" item="+item);
							Object o =  msgVarGetter.invoke(item) ;
							msgVarArray[i] = o != null ? (String) msgVarGetter.invoke(item) : "" ;
							i++ ;
						}*/
						
						String msg  = MessageTranslationUtil.translate(langue ,
								codeTraductionMsg , codeTraductionMsg, msgVarMap) ;
						msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					}
			
					return false ;
				}
		
				if (!linkedEntityList.contains(savedItem)) linkedEntityList.add(savedItem) ;
			}
		}
		
		setter.invoke(entity, linkedEntityList) ;

		return true ;

	}

	public boolean attachNonCustumizableLinkedReference(EntityManager entityManager, T entity, 
			Reference linkedEntity, Method setter, 
			Locale langue, String codeTraductionMsg, String[] msgVarArray,
			List<NonLocalizedStatusMessage> msgList) 
		throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if (setter == null || entity == null) throw new IllegalArgumentException() ;

		if (linkedEntity != null && !entityManager.contains(linkedEntity)) {
	
			@SuppressWarnings("static-access")
			Reference savedLinkedEntity = (new EntityUtil<Reference>())
					.findEntityById(entityManager, linkedEntity.getId(), null, true, Reference.class);
			
			if (savedLinkedEntity == null) {
		
				if (codeTraductionMsg != null && !codeTraductionMsg.isEmpty()) {
	
					String msg  = MessageTranslationUtil.translate(langue ,
							codeTraductionMsg , codeTraductionMsg, msgVarArray) ;
					msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				}
		
				return false ;
			}
	
			setter.invoke(entity, savedLinkedEntity) ;
		}

		return true ;
	}

	/*
	public boolean attachLinkedReference(EntityManager entityManager, T entity, 
			Reference linkedEntity, Method setter, String ifId, 
			Locale langue, String codeTraductionMsg, String[] msgVarArray,
			List<NonLocalizedStatusMessage> msgList) 
		throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if (setter == null || entity == null) throw new IllegalArgumentException() ;

		if (linkedEntity != null && !entityManager.contains(linkedEntity)) {
	
			// if referencefamily is defined as customizable in IF profile
			// THEN retrive reference from profile data
			// IF NOT retrieve from all reference list
			
			String idFamille = linkedEntity.getFamille() != null 
					?  linkedEntity.getFamille().getId() : null ;
					
			boolean isCustomized = isReferenceFamilyCustomizableInIfProfil(
									entityManager, idFamille, ifId, true) ;
			
			
			Reference savedLinkedEntity = isCustomized
					
					? // retrieve from profile data
						(new EntityUtil<ProfilIfData>()).findSingleResultByFieldValues(
								entityManager, "referenceInProfilIfDataByReferenceIdAndIfId", 
								new String[] {"id", "ifId"}, new String[] {linkedEntity.getId(), ifId}, 
								null, true, new Reference()) 
						
					: // retrive by id
						EntityUtil.findEntityById(
								entityManager, linkedEntity.getId(), null, true, Reference.class);
			
			if (savedLinkedEntity == null) {
		
				if (codeTraductionMsg != null && !codeTraductionMsg.isEmpty()) {
	
					String msg  = MessageTranslationUnit.dynamicTranslate(langue ,
							codeTraductionMsg , codeTraductionMsg, msgVarArray) ;
					msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
				}
		
				return false ;
			}
	
			setter.invoke(entity, savedLinkedEntity) ;
		}

		return true ;
	}*/

	/*
	public boolean attachLinkedReferenceList(EntityManager entityManager, T entity, 
			Set<Reference> linkedEntityList, Method setter,  String ifId, 
			Locale langue, String codeTraductionMsg, 
			List<NonLocalizedStatusMessage> msgList) 
		throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if (entity == null || setter == null) throw new IllegalArgumentException() ;

		if (linkedEntityList.isEmpty()) return true ;
		
		Set<Reference> savedList = linkedEntityList ;
		linkedEntityList = new HashSet<>() ;
		
		for (Reference item : savedList) {
			
			if (item != null && !entityManager.contains(item)) {
				
				// if referencefamily is defined as customizable in IF profile
				// THEN retrive reference from profile data
				// IF NOT retrieve from all reference list
				
				String idFamille = item.getFamille() != null 
						?  item.getFamille().getId() : null ;
						
				boolean isCustomized = isReferenceFamilyCustomizableInIfProfil(
										entityManager, idFamille, ifId, true) ;
				
				
				Reference savedItem = isCustomized
						
						? // retrieve from profile data
							(new EntityUtil<ProfilIfData>()).findSingleResultByFieldValues(
									entityManager, "referenceInProfilIfDataByReferenceIdAndIfId", 
									new String[] {"id", "ifId"}, new String[] {item.getId(), ifId}, 
									null, true, new Reference()) 
							
						: // retrive by id
							EntityUtil.findEntityById(
									entityManager, item.getId(), null, true, Reference.class);

				//Reference savedItem = (new EntityUtil<Reference>())
				//		.findEntityById(entityManager, item.getId(), null, true, Reference.class);
				
				if (savedItem == null) {
			
					if (codeTraductionMsg != null && !codeTraductionMsg.isEmpty()) {
		
						String msg  = MessageTranslationUnit.dynamicTranslate(langue ,
								codeTraductionMsg , codeTraductionMsg, item.getDesignation()) ;
						msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					}
			
					return false ;
				}
		
				if (!linkedEntityList.contains(savedItem)) linkedEntityList.add(savedItem) ;
			}
		}
		
		setter.invoke(entity, linkedEntityList) ;

		return true ;
	}
	*/

	/**
	 * Valider une entité avant la suppression
	 * @param entity
	 * @param msgList
	 * @return attached instance of the entity or null if validation fails
	 * @throws Exception
	 */
	public static IEntityStringkey validerPourSuppression(IEntityStringkey entity, 
			List<NonLocalizedStatusMessage> msgList,
			String entityNullMsg, String entityIdNullOrEmpty,
			String msgEntityNotFoundMsg,
			EntityManager entityManager) 
			throws Exception {

		if (entity == null) {
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, entityNullMsg)) ;
			return null ;
		}

		if (entity.getId() == null || entity.getId().equals("")) {
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, entityIdNullOrEmpty)) ;
			return null ;
		}
		
		IEntityStringkey savedObject = entityManager.find(entity.getClass(), entity.getId()) ;		
		
		if (savedObject == null) {
			
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msgEntityNotFoundMsg)) ;
			return null ;
		}
		 
		return savedObject ;
	}
	
	
	public void removeEntity(EntityManager entityManager, T entity, 
			String namedGraphName, boolean isfetchGraph) 
		throws ClassNotInitializedException {
		
		if (entity == null) return ;
		
		// attach entity
		
		T savedEntity = !entityManager.contains(entity)
				?  findEntityById(entityManager, entity.getId(), namedGraphName, isfetchGraph)
				: entity ;
		
		if (entity != null) entityManager.remove(savedEntity);
	}
	
	/*public static void removeEntity(EntityManager entityManager, IEntityStringkey entity) {
		
		if (entity == null) return ;
		
		entityManager.remove(entity);
	}*/
	
	public boolean removeEntity(EntityManager entityManager, T entity, 
					String entityNotDefinedMsgTrCode, String[] entityNotDefinedMsgArray,
					String entityNotFoundMsgTrCode, String[] entityNotFoundMsgArray,
					String removePersistenceErrorMsgTrCode, String[] removePersistenceErrorMsgArray,
					Locale langue, Collection<NonLocalizedStatusMessage> msgList) 
				throws ClassNotInitializedException {
		
		if (entity == null) {
			
			String msg = MessageTranslationUtil.translate(langue, 
					entityNotDefinedMsgTrCode, entityNotDefinedMsgTrCode, entityNotDefinedMsgArray);
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));
			
			return false;
		}
		
		entity = findEntityById(entityManager, entity.getId(), 
					NAMED_GRAPH_ANY_ENTITY_ID_VERSION_ONLY, true) ;
		
		if (entity == null) {
			
			String msg = MessageTranslationUtil.translate(langue, 
					entityNotFoundMsgTrCode, entityNotFoundMsgTrCode, entityNotFoundMsgArray);
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));
			
			return false;
		}
		
		try {
			entityManager.remove(entity);
		}catch (Exception ex) {
			
			String msg = MessageTranslationUtil.translate(langue, 
					removePersistenceErrorMsgTrCode, removePersistenceErrorMsgTrCode, 
					removePersistenceErrorMsgArray);
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg));
			
			return false;
		}
		
		return true ;
	}

	public boolean isEntityDuplicatedOrNotFound(EntityManager entityManager, T entity, 
						boolean mustUpdateExistingNew, String naturalIdNamedQuery, 
						String[] naturalIdNamedQueryParameterNames, Object[] naturalIdNamedQueryParameterValues,
						String duplicatedNewEntityErrorTrCode, Map<String,String> duplicatedNewEntityErrorParameterValuesMap,
						String duplicatedWithDifferentIdErrorTrCode,Map<String,String> duplicatedWithDifferentIdErrorParameterValuesMap,
						String notFoundErrorTrCode,Map<String,String>  notFoundErrorParameterValuesMap,
						Locale langue, List<NonLocalizedStatusMessage> msgList) {
		
		
		// check de l'existence de la nouvel entité par son identité naturelle
		 logger.info("_877  Identifiant ="+naturalIdNamedQueryParameterNames.length); //TODO A effacer 
		 logger.info("_878  Identifiant ="+naturalIdNamedQueryParameterValues[0]+  "   "+naturalIdNamedQueryParameterValues[1]); //TODO A effacer 
		 
		String naturalId = findSingleResultByFieldValues(
				entityManager, naturalIdNamedQuery, 
				naturalIdNamedQueryParameterNames, naturalIdNamedQueryParameterValues, 
				null, true, new String()) ;
		
	    logger.info("_853  Identifiant ="+naturalId); //TODO A effacer 
		if (naturalId != null) {// entity with the same natural id exist
			
			if (entity.getId() == null || entity.getId().isEmpty())  {
				
				if (mustUpdateExistingNew) entity.setId(naturalId);
				else { // dupicate
					
					String msg  = MessageTranslationUtil.translate(langue ,
							duplicatedNewEntityErrorTrCode, duplicatedNewEntityErrorTrCode, 
							duplicatedNewEntityErrorParameterValuesMap) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR,  msg)) ; 
					
					return true ;
				}
			}
			else {
				
				if (!naturalId.equals(entity.getId())){

					// An entity with same natural id exit but with diffrent id ! 
					
					String msg  = MessageTranslationUtil.translate(langue ,
							duplicatedWithDifferentIdErrorTrCode, duplicatedWithDifferentIdErrorTrCode, 
							duplicatedWithDifferentIdErrorParameterValuesMap) ;
					msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR,  msg)) ; 
					
					return true ;
				}
			}	
		}
		
		// check if existing entity exist in DB
		
		if (entity.getId() != null && !entity.getId().isEmpty()
				&& !EntityUtil.doesEntityExistInDB(entityManager, entity.getClass(), entity.getId())) {
			
		
			String msg  = MessageTranslationUtil.translate(langue ,
					notFoundErrorTrCode, notFoundErrorTrCode, notFoundErrorParameterValuesMap) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR,  msg)) ; 
			
			
		    logger.info("_883 msgList="+msgList);  //TODO A effacer
			
			return true ;
		    
		    //return false ;
			
		}
	
		return false ;
		
	}

	
}
