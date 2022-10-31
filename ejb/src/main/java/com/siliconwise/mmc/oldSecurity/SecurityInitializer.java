package com.siliconwise.mmc.oldSecurity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
@Stateless
public class SecurityInitializer implements Serializable {

	 
	private static final long serialVersionUID = 1L;
	
	private SecurityManager securityManager;
	
	private  Map<String, SessionBag> activeSessionMap = new HashMap<>() ;
	
	Ini ini = new Ini();
	
	private transient Logger logger = LoggerFactory.getLogger(getClass().getName()) ;
	 
	 
	@PostConstruct
	 public void init() {
		
		
		 //TODO Creation du fichier shiro.ini
	  
		 final String iniFile = "classpath:shiro.ini";
		 
	     logger.info("Initializing Shiro INI SecurityManager using " + iniFile);
	  
	     //securityManager = new IniSecurityManagerFactory(iniFile).getInstance();
	     
	     securityManager = new IniSecurityManagerFactory(ini).getInstance();
	 
	     SecurityUtils.setSecurityManager(securityManager);
	    
	  
	 }
	 
	 @Produces
	 @Named("securityManager")
	 public SecurityManager getSecurityManager() {
		 
		 
		 return securityManager;
	 }
	 
	@Produces
	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	public Map<String, SessionBag> getActiveSessionMap() {
		return activeSessionMap;
	}

	public void setActiveSessionMap(Map<String, SessionBag> activeSessionMap) {
		this.activeSessionMap = activeSessionMap;
	}
}
