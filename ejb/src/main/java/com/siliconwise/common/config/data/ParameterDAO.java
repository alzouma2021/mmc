package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siliconwise.mmc.message.AppMessageKeys;
//import com.siliconwise.common.document.DocumentPlateForme;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.common.entity.EntityUtil;

/**
 * @author GNAKALE Bernardin
 *
 */
@SuppressWarnings("unused")
@Stateless
public class ParameterDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext private EntityManager entityManager ;
	@Resource private EJBContext ejbContext;

	@SuppressWarnings("unused")
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	
	private static Map<String, IParameter<?>> allGlobalParametersMap = new HashMap<>() ;
	private static Map<String, IParameter<?>> allLocalParametersMap = new HashMap<>() ;
	
	/** get a parameter from memory Map
	 * isLocalDataConfig: is a parameter from local data configuration
	 */
	public Object getParameterValuefromMemory(boolean isLocalDataConfig, String parameterName) 
	{
		Map<String, IParameter<?>> allParameterMap = getAllParameters(isLocalDataConfig) ;
		
		return(allParameterMap == null || !allParameterMap.containsKey(parameterName)
					? null : allParameterMap.get(parameterName));
	}
	
	public Map<String, IParameter<?>> getAllParameters(boolean isLocalDataConfig)
	{
		if (isLocalDataConfig && allLocalParametersMap.isEmpty()) 
		{
			allLocalParametersMap = extractAllParameters(isLocalDataConfig);
		}
		else if (!isLocalDataConfig && allGlobalParametersMap.isEmpty()) {
			allGlobalParametersMap = extractAllParameters(isLocalDataConfig);
		}
		
		return isLocalDataConfig ? allLocalParametersMap : allGlobalParametersMap ;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, IParameter<?>> extractAllParameters(boolean isLocalDataConfig)
	{
		 String queryName = isLocalDataConfig ? "findAllLocalParameters" : "findAllGlobalParameters";
		 
		List<IParameter<?>> list = entityManager.createNamedQuery(queryName)
				.getResultList() ;
		
		Map<String, IParameter<?>> map = new HashMap<>();
		
		for(IParameter<?> p : list) map.put(p.getDesignation(), p) ;
		
		return(map);
	}
	
	 public IParameter<?> findParameterByDesignation(boolean isLocalDataConfig, String designation)
	 {
		 IParameter<?> rtn = null ;
		 
		 String queryName = isLocalDataConfig ? "findLocalParameterByDesignation" : "findGlobalParameterByDesignation";
		 
		 try
		 {
			rtn = (IParameter<?>) entityManager.createNamedQuery(queryName)
					.setParameter("designation", designation)
					.getSingleResult() ;
		 }
		 catch (NoResultException nre){
			rtn = null ;
		}
		 
		return(rtn) ;
	 }
	 
	 private boolean validate(@SuppressWarnings("rawtypes") IParameter entity, 
			 	SessionBag currentSession, List<NonLocalizedStatusMessage> msgList) {

		 final String CODE_TRADUCTION_GLOBAL_PARAMETER_VALUE_NOT_NULL = "entity.global-parameter.value.not_null" ;
		 final String CODE_TRADUCTION_LOCAL_PARAMETER_VALUE_NOT_NULL = "entity.local-parameter.value.not_null" ;

		 Locale langue = SessionUtil.getLocale(currentSession) ;
		 
		 // variable 1  -> global parameter id
		 // translation code defined in GobalParaleter class
		 
		 // recerche de doublon :: non applicable
		 

			// association
			
			/*try {
				if (entity instanceof GlobalParameterReference) {

					GlobalParameterReference paramReference = (GlobalParameterReference) entity ;
					
					EntityUtil<GlobalParameterReference> entityUtil = new EntityUtil<GlobalParameterReference>(GlobalParameterReference.class) ;

					String s = paramReference.getValue() != null
						? paramReference.getValue().getDesignation() : "" ;
						
					boolean isAttached = entityUtil.attachLinkedReference(entityManager, paramReference, 
							paramReference.getValue(), 
							paramReference.getClass().getDeclaredMethod("setValue", Reference.class), 
							currentSession.getIntermediaiareFinancierId(), 
							langue, GlobalParameter.CODE_TRADUCTION_REFERENCE_NON_TROUVE, 
							new String[]{s}, msgList) ; 
					
					if (!isAttached) return false ;
				}
			} 
			catch(Exception ex) {
				
				String msg  = MessageTranslationUtil.translate(langue ,
						GlobalParameter.CODE_TRADUCTION_LINKED_REFERENCE_ATTACHMENT_ERROR,// venant du fichier
						GlobalParameter.CODE_TRADUCTION_LINKED_REFERENCE_ATTACHMENT_ERROR, // Message par defaut
						new String[] {}) ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, LocalDateTime.now().toString() + " " + msg)) ; 
				
				logger.error("_135 valider :: " + msg + " " + ex + ":" + ex.getMessage()+" Cause:"+ex.getCause());
				ex.printStackTrace();
				
				return false ;
			}*/
		 
		 boolean isGlobal = entity instanceof GlobalParameter ;
		 
		 // does entity to  update exists
		 
		 if (entity.getId() == null || entity.getId().isEmpty()) {
			 
			 boolean isInDb = EntityUtil.doesEntityExistInDB(entityManager,  
		 				entity.getClass(), entity.getId()) ;
			 
			 if (!isInDb) {
				
				 String trCode = isGlobal 
						 		? GlobalParameter.CODE_TRADUCTION_NOT_FOUND_1V:null;
				 
				String msg = MessageTranslationUtil.translate(langue ,
						trCode, trCode, entity.getId() != null ? entity.getId() : "") ;
				msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
					 
				return false ;
			 }
		 }
		 
		 // field integrity
		 
		 if (entity.getValue() == null) {
				
			 String trCode = isGlobal 
					 		? CODE_TRADUCTION_GLOBAL_PARAMETER_VALUE_NOT_NULL
					 		: CODE_TRADUCTION_LOCAL_PARAMETER_VALUE_NOT_NULL ;
			 
			 String msg = MessageTranslationUtil.translate(langue ,
						trCode, trCode, entity.getId() != null ? entity.getId() : "") ;
			 msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			 
			 return false ;
		 }
		 
		 return true ;
	 }
	 
	 private IParameter<?> saveToDb(IParameter<?> entity,
			 	String entityExistsExceptionTrCode, Map<String,String>  entityExistsExceptionVariablesMap,
				String persistenceExceptionTrCode, Map<String,String>  persistenceExceptionVariablesMap,
				String generalExceptionTrCode, Map<String,String>  generalExceptionVariablesMap,
				Locale langue, List<NonLocalizedStatusMessage> msgList) {
		
		 IParameter<?> rtn = null ;
		 
		 if (entity instanceof GlobalParameterInteger) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterInteger.class, (GlobalParameterInteger)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 else if (entity instanceof GlobalParameterLong) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterLong.class, (GlobalParameterLong)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 
		 else if (entity instanceof GlobalParameterDouble) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterDouble.class, (GlobalParameterDouble)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 
		 else if (entity instanceof GlobalParameterFloat) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterFloat.class, (GlobalParameterFloat)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 
		 else if (entity instanceof GlobalParameterBoolean) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterBoolean.class, (GlobalParameterBoolean)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList);
		 
		 
		 else if (entity instanceof GlobalParameterReference) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterReference.class, (GlobalParameterReference)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList);
		 
		 
		 else if (entity instanceof GlobalParameterString) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterString.class, (GlobalParameterString)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 
		 else if (entity instanceof GlobalParameterLongText) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterLongText.class, (GlobalParameterLongText)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 
		 else if (entity instanceof GlobalParameterDate) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterDate.class, (GlobalParameterDate)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 
		 else if (entity instanceof GlobalParameterTime) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterTime.class, (GlobalParameterTime)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 
		 else if (entity instanceof GlobalParameterDateTime) 
			 rtn = EntityUtil.persistOrMerge(entityManager, 
					 GlobalParameterDateTime.class, (GlobalParameterDateTime)entity, null, true, 
					 entityExistsExceptionTrCode, entityExistsExceptionVariablesMap, 
					 persistenceExceptionTrCode, persistenceExceptionVariablesMap, 
					 generalExceptionTrCode, generalExceptionVariablesMap, 
					 langue, msgList); 
		 
		 return rtn ;
	 }
	 
	 /** Validate and save local or global parameter
	 * @param entity
	 * @param currentSession
	 * @param msgList
	 * @return
	 */
	public IParameter<?> validateAndSave(IParameter<?> entity, 
			 		SessionBag currentSession, List<NonLocalizedStatusMessage> msgList) {
		 
		 Locale langue = SessionUtil.getLocale(currentSession) ;
		 
		// boolean isGlobal = entity instanceof GlobalParameter ;

		 if (entity == null) {
				
			 String msg  = MessageTranslationUtil.translate(langue,
					 GlobalParameter.CODE_TRADUCTION_NOT_DEFINED,
					 GlobalParameter.CODE_TRADUCTION_NOT_DEFINED,
						new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			
			return null ;
		 }
		 
		 boolean isValid = validate(entity, currentSession, msgList) ;
			
		if (!isValid) {
			try{ ejbContext.setRollbackOnly(); } catch(Exception ex){}
			return null ; 
		}
		
		/*
		String entityExistsExceptionTrCode = GlobalParameter.CODE_TRADUCTION_IF_EXISTE ;
		String persistenceExceptionTrCode = GlobalParameter.CODE_TRADUCTION_PERSISTENCE_INTIGRITY_ERROR ;
		String generalExceptionTrCode = GlobalParameter.CODE_TRADUCTION_PERISTENCE_ERREUR ;
		
		
		 
		String id = entity.getId() != null ? entity.getId() : "" ;
		*/
		
		String entityExistsExceptionTrCode = AppMessageKeys.CODE_TRADUCTION_EXISTE ;
		String persistenceExceptionTrCode = AppMessageKeys.CODE_TRADUCTION_ERREUR_INTEGRITE_PERSISTENCE ;
		String generalExceptionTrCode = AppMessageKeys.CODE_TRADUCTION_PERSISTENCE_ERREUR ;
		
		IParameter<?> rtn = saveToDb(entity,
				entityExistsExceptionTrCode, entity.getMsgVarMap(), 
				persistenceExceptionTrCode, entity.getMsgVarMap(), 
				generalExceptionTrCode,  entity.getMsgVarMap(), 
				langue, msgList);
		
		if (rtn == null) {
			try{ ejbContext.setRollbackOnly(); } catch(Exception ex){}
		}

		return rtn ;
	 }
	 
	 public void persistOrMerge(IParameter<?> myParam)
	 {
		 if (myParam.getId() == null) // new entity
		 {
			 if (myParam instanceof GlobalParameterInteger) 
				 entityManager.persist((GlobalParameterInteger)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterLong) 
				 entityManager.persist((GlobalParameterLong)myParam) ;
			 
			 else if (myParam instanceof GlobalParameterDouble) 
				 entityManager.persist((GlobalParameterDouble)myParam) ;
			 
			 else if (myParam instanceof GlobalParameterFloat) 
				 entityManager.persist((GlobalParameterFloat)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterString) 
				 entityManager.persist((GlobalParameterString)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterLongText) 
				 entityManager.persist((GlobalParameterLongText)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterDate) 
				 entityManager.persist((GlobalParameterDate)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterTime) 
				 entityManager.persist((GlobalParameterTime)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterDateTime) 
				 entityManager.persist((GlobalParameterDateTime)myParam) ;
			 
		
		 }
		 else
		 {
			 if (myParam instanceof GlobalParameterInteger) 
				 entityManager.merge((GlobalParameterInteger)myParam) ;
			 
			
			 else if (myParam instanceof GlobalParameterLong) 
				 entityManager.merge((GlobalParameterLong)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterDouble) 
				 entityManager.merge((GlobalParameterDouble)myParam) ;
			
			 else if (myParam instanceof GlobalParameterFloat) 
				 entityManager.merge((GlobalParameterFloat)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterString) 
				 entityManager.merge((GlobalParameterString)myParam) ;
			
			 else if (myParam instanceof GlobalParameterLongText) 
				 entityManager.merge((GlobalParameterLongText)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterDate) 
				 entityManager.merge((GlobalParameterDate)myParam) ;
			 
			 
			 else if (myParam instanceof GlobalParameterTime) 
				 entityManager.merge((GlobalParameterTime)myParam) ;
			 

			 else if (myParam instanceof GlobalParameterDateTime) 
				 entityManager.merge((GlobalParameterDateTime)myParam) ;
			 

		 }
		
	 }
	  
	 @SuppressWarnings("unchecked")
	 public Parameter<Long> trouverLocalParametterLongParDeignation(String designation){
		 
		 Parameter<Long> rtn = null ;
		 try {
			 rtn = (Parameter<Long>) entityManager.createNamedQuery("localParametterLongParDeignation")
			  			.setParameter("designation", designation)
						.getSingleResult();		
		 }
		 catch (NoResultException nre){
			rtn = null ;
		 }
		 
		 return rtn ;
	 }
	 
	 @SuppressWarnings("unchecked")
	 public Parameter<Integer> trouverLocalParameterIntegerParDeignation(String designation){
	
		 Parameter<Integer> rtn = null ;
		 
		 try {
			 rtn =  (Parameter<Integer>) entityManager.createNamedQuery("localParameterIntegerParDeignation")
			  			.setParameter("designation", designation)
						.getSingleResult();
		 }
		 catch (NoResultException nre){
			 rtn = null ;
		 }
		 
		 return	rtn ;	
	 }
	 
	 /**
	  * Get local parameter fromdatabase 
	  * Return null if parameter not found
	 * @param designation
	 * @return
	 */
	 public Parameter<?> extractFromDBGlobalParameterByDesignation(String designation){
			
		 List<?> list = entityManager.createNamedQuery("findGlobalParameterByDesignation")
			  			.setParameter("designation", designation)
						.getResultList();	
		 
		 //logger.info(getClass().getName()+"::extractFromDBLocalParameterByDesignation list.size="+list.size());
		 return	list.isEmpty() ? null : (Parameter<?>) list.get(0) ;	
	 }
	 
	 /**
	  * Get local parameter fromdatabase 
	  * Return null if parameter not found
	 * @param designation
	 * @return
	 */
	 public Parameter<?> extractFromDBLocalParameterByDesignation(String designation){
			
		 List<?> list = entityManager.createNamedQuery("findLocalParameterByDesignation")
			  			.setParameter("designation", designation)
						.getResultList();	
		 
		 //logger.info(getClass().getName()+"::extractFromDBLocalParameterByDesignation list.size="+list.size());
		 return	list.isEmpty() ? null : (Parameter<?>) list.get(0) ;	
	 }

	 /**
	  * Get local parameter. If parameter is not in memeory it is extracted from data base
	  * Return null if parameter not found
	 * @param designation
	 * @return
	 */
	public Parameter<?> getLocalParameterByDesignation(String designation){
		 
		Parameter<?> rtn = allLocalParametersMap != null 
				 ? (Parameter<?>) allLocalParametersMap.get(designation) : null ;
		
		if (rtn != null) return rtn ;
		
		return extractFromDBLocalParameterByDesignation(designation) ;
	 }

	 /**
	  * Get global parameter. If parameter is not in memeory it is extracted from data base
	  * Return null if parameter not found
	 * @param designation
	 * @return
	 */
	public Parameter<?> getGlobalParameterByDesignation(String designation){
		 
		Parameter<?> rtn = allGlobalParametersMap != null 
				 ? (Parameter<?>) allGlobalParametersMap.get(designation) : null ;
		
		if (rtn != null) return rtn ;
		
		return extractFromDBGlobalParameterByDesignation(designation) ;
	 }

	 public synchronized Long getAndIncrementParameterLong(String parameterDesighation) {
			
		@SuppressWarnings("unchecked")
		Parameter<Long> param = (Parameter<Long>) extractFromDBLocalParameterByDesignation(parameterDesighation) ;
		
		Long valeur = param != null ? param.getValue() : null ;
		
			if (valeur == null) return null ;
		
			valeur++ ;
			
		// sauver le nouveau sufixe		
		param.setValue(valeur); persistOrMerge(param) ;
			
		return(valeur);
	}

	private Boolean valider(GlobalParameterString entity, List<NonLocalizedStatusMessage> msgList) throws Exception {
	
		if (entity == null) {
			
			String msg = "Aucune donnée n'est définie. " ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			return false ;
		}
	
		// initialiser le spropriétés automatiques	
		//entityInitializer.initializeBeforeSaving(GlobalParameterString.class, entity) ;
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		Set<ConstraintViolation<GlobalParameterString>> constraintViolationList = validator.validate(entity) ;
		
		for (ConstraintViolation<GlobalParameterString> violation : constraintViolationList) {
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, violation.getMessage())) ;
		}
				
		if (!constraintViolationList.isEmpty()) return false ;
	
		//if (!EntityUtils.valider(entity, msgList)) return false ;
		
		return true ;
	}

	private GlobalParameterString enregistrer(GlobalParameterString entity, List<NonLocalizedStatusMessage> msgList) 
			throws Exception {
		
		GlobalParameterString rtn = null ;
		//rtn = (GlobalParameterString) EntityUtil.persistOrMerge(entityManager, GlobalParameterString.class, entity) ;
		return rtn ;
	}

	public GlobalParameterString validerEnregistrer(GlobalParameterString entity, List<NonLocalizedStatusMessage> msgList)
			throws Exception {
				
		if (entity == null) {
	
			String msg = "Aucune donnée n'est définie. " ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
	
			return null ;
		}
	
		boolean estValide = valider(entity, msgList) ;
	
		if (!estValide) return null ; 
	
		GlobalParameterString rtn = enregistrer(entity, msgList) ;
		
		//entityManager.flush(); 
		
		return rtn ;
	}

	private Boolean valider(GlobalParameterDouble entity, SessionBag sessionBag,
			List<NonLocalizedStatusMessage> msgList) throws Exception {
	
		if (entity == null) {
			
			String msg = "Aucune donnée n'est définie. " ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
			return false ;
		}
	
		Locale locale = SessionUtil.getLocale(sessionBag) ;
		
		// initialiser le spropriétés automatiques	
		//entityInitializer.initializeBeforeSaving(GlobalParameterString.class, entity) ;
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator() ;
		Set<ConstraintViolation<GlobalParameterDouble>> constraintViolationList = validator.validate(entity) ;
		
		for (ConstraintViolation<GlobalParameterDouble> violation : constraintViolationList) {
			String translatedMessage = MessageTranslationUtil.translate(locale ,
					violation.getMessage(), violation.getMessage(), 
					new String[] {}) ;
			msgList.add( new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage)) ; ;
		}
				
		if (!constraintViolationList.isEmpty()) return false ;
		
		return true ;
	}

	private GlobalParameterDouble enregistrer(GlobalParameterDouble entity, List<NonLocalizedStatusMessage> msgList) 
			throws Exception {
		GlobalParameterDouble rtn = null ;
		// rtn = (GlobalParameterDouble) EntityUtil.persistOrMerge(entityManager, GlobalParameterDouble.class, entity)  ;
		return rtn ;
	}

	public GlobalParameterDouble validerEnregistrer(GlobalParameterDouble entity, 
			SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList)
			throws Exception {
				
		if (entity == null) {
	
			String msg = "Aucune donnée n'est définie. " ;
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, msg)) ;
	
			return null ;
		}
	
		boolean estValide = valider(entity, sessionBag, msgList) ;
	
		if (!estValide) return null ; 
	
		GlobalParameterDouble rtn = enregistrer(entity, msgList) ;
		
		//entityManager.flush(); 
		
		return rtn ;
	}

	@SuppressWarnings("unchecked")
	public List<GlobalParameterString> trouverTousLesGlobalparameterString(){
		
		 boolean estSupport = false ;
		 List<GlobalParameterString> rtn = null ;
		 try {
			rtn = (List<GlobalParameterString>) entityManager
					.createNamedQuery("trouverTousLesGlobalesParameterString")
					.setParameter("estSupport", estSupport)
					.getResultList() ;
		} catch (NoResultException nre) {
			return null;
		}
		return rtn ; 
	 }

	@SuppressWarnings("unchecked")
	public List<GlobalParameterDouble> trouverTousLesGlobalparameterDouble(){
		
		 boolean estSupport = false ;
		 List<GlobalParameterDouble> rtn = null ;
		 try {
			rtn = (List<GlobalParameterDouble>) entityManager
					.createNamedQuery("trouverTousLesGlobalesParameterDouble")
					.setParameter("estSupport", estSupport)
					.getResultList() ;
		} catch (NoResultException nre) {
			return null;
		}
		return rtn ; 
	 }
	
	
	public <E> E extractLocalOrtGlobalParameterValue(boolean isLocal, String designation, 
			String errorRetrievalTranslationCode, String errorNotDefinedTranslationCode, 
			Locale locale, E instance, List<NonLocalizedStatusMessage> msgList) {
		
		if (designation == null || designation.isEmpty()) return null ;
		
		E rtn = null;

		try {
			@SuppressWarnings("unchecked")
			Parameter<E> parameter = (Parameter<E>) findParameterByDesignation(isLocal, designation);

			rtn = parameter != null ? parameter.getValue() : null;
		} catch (Exception e) {

			String translatedMessage = MessageTranslationUtil.translate(locale,
					errorRetrievalTranslationCode,  errorRetrievalTranslationCode,
					new String[] {designation});
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));

			return null;
		}

		if (rtn == null || (rtn instanceof String && ((String)rtn).isEmpty())) {

			String translatedMessage = MessageTranslationUtil.translate(locale,
					errorNotDefinedTranslationCode, errorNotDefinedTranslationCode,
					new String[] {});
			msgList.add(new NonLocalizedStatusMessage(StatusMessageType.ERROR, translatedMessage));

			return null;
		}

		return rtn ;
	}

}