package com.siliconwise.mmc.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.mail.internet.InternetAddress;

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

import com.siliconwise.common.config.file.AppConfigKeys;
import com.siliconwise.common.config.file.AppConfigUtil;
import com.siliconwise.common.entity.IEntityMsgVarMap;
import com.siliconwise.common.entity.IEntityStringkey;
import com.siliconwise.common.mail.EmailService;
import com.siliconwise.common.mail.EmailServiceException;
import com.siliconwise.mmc.common.entity.EntityUtil;
import com.siliconwise.mmc.common.entity.UUIDGeneratorEntityListener;

import com.siliconwise.mmc.message.AppMessageKeys;
import com.siliconwise.mmc.message.MessageTranslationUtil;
import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.message.StatusMessageType;
import com.siliconwise.mmc.oldSecurity.SessionBag;
import com.siliconwise.mmc.oldSecurity.SessionUtil;
import com.siliconwise.mmc.user.User;

/*
@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class SendEmailTest {
	
	

	@Deployment
	public static JavaArchive createDeployment(){
		 
				
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(
					
						UUIDGeneratorEntityListener.class,
						EntityUtil.class,
						NonLocalizedStatusMessage.class,
						StatusMessageType.class,
						SessionBag.class,
						AppMessageKeys.class,
						MessageTranslationUtil.class,
						SessionUtil.class,
						AppUtil.class,
						EntityUtil.class,
						AppConfigUtil.class,
						EmailServiceException.class,
						AppConfigKeys.class
							)
				.addAsManifestResource("test-persistence.xml","persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
			
		System.out.println(jar.toString(true));
		//.addAsResource("META-INF/persistence.xml")
		return jar ;
		
		
	}
	
	
	
	List<NonLocalizedStatusMessage> msgList = new ArrayList<NonLocalizedStatusMessage>() ;
	
	Locale locale;
	
	@Inject EmailService emailService ;

	
	@Test
	//@UsingDataSet("datasets/produitlogement.yml")
	public void testSendEmail() {
		
		 //Initialisation des entrants pour l'envoyer mail
	
		 String subject = "Test d'envoyer de mail" ;
		
		//Addresses emaim des destinataires
		
		InternetAddress to1 = new InternetAddress();
		to1.setAddress("moussa.alzouma@siliconwise.biz");
		
		InternetAddress to2 = new InternetAddress();
		to2.setAddress("emmanuel.ezan@siliconwise.biz");
		
		InternetAddress[] toList = {to1, to2} ;
		
		//Adresse de copie carbone
		
		InternetAddress cc = new InternetAddress();
		cc.setAddress("emmanuel.kouevi@siliconwise.biz");
		
		InternetAddress[] ccList = {cc} ;
		
		String textContent = "" ;
		
		String htmlContent = "Le programme immobilier sur lequel nous avions passé des accords"
				+ "concernant sur le financement de type crédit bancaire vient d'être publié sur la plateforme " ;
		
		
		//Execution du test 
		
		boolean rtn =  emailService.sendEmail(subject, toList, ccList, textContent, htmlContent);

		
		assertEquals(true, rtn );
	
	}

}*/
