package com.siliconwise.mmc.modefinancement;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;
import com.siliconwise.mmc.programmeimmobilier.ProgrammeImmobilier;
import com.siliconwise.mmc.user.User;

public interface CreerModifierModeFinancementCtlInterface {
	
	
	
	public List<ModeFinancement> creerModifierModeFinancementList(
			Set<ModeFinancement> entityList,
			boolean mustUpdateExistingNew,ProgrammeImmobilier programme, 
			String namedGraph, boolean isFetchGraph, 
			Locale locale, User loggedInUser,
			List<NonLocalizedStatusMessage> msgList);
	
	
	public ModeFinancement creerModifierUnModeFinancement(ModeFinancement entity, 
			boolean mustUpdateExistingNew,
			String namedGraph, boolean isFetchGraph, 
			Locale locale, User loggedInUser,
			List<NonLocalizedStatusMessage> msgList);
	
	
	public ModeFinancement retournerModeFinancementEnFonctionDuType(ModeFinancement entity);
	
	

}
