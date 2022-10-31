package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.oldSecurity.SessionBag;

@Stateless
public class ParameterCtrl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject private ParameterDAO parameterDAO ;
	
	 public void persistOrMerge(IParameter<?> myParam){
		 parameterDAO.persistOrMerge(myParam);
	 }

	public IParameter<?> validateAndSave(IParameter<?> entity, 
		 		SessionBag currentSession, List<NonLocalizedStatusMessage> msgList) {
		
		return parameterDAO.validateAndSave(entity, currentSession, msgList) ;
	}

	 public GlobalParameterString validerEnregistrer
			(GlobalParameterString entity, List<NonLocalizedStatusMessage> msgList) throws Exception{
		return (GlobalParameterString) parameterDAO.validerEnregistrer(entity, msgList);
	}
	 
	 public GlobalParameterDouble validerEnregistrer(
			 GlobalParameterDouble entity, SessionBag sessionBag, List<NonLocalizedStatusMessage> msgList) throws Exception{
		return (GlobalParameterDouble) parameterDAO.validerEnregistrer(entity, sessionBag, msgList);
	}
	 
	 public List<GlobalParameterString> trouverTousLesGlobalParameterString(){
		 
		 return (List<GlobalParameterString>) parameterDAO.trouverTousLesGlobalparameterString() ;	 
	 }
	 
	 public List<GlobalParameterDouble> trouverTousLesGlobalparameterDouble(){
		 
		 return (List<GlobalParameterDouble>) parameterDAO.trouverTousLesGlobalparameterDouble() ;	 
	 }

}
