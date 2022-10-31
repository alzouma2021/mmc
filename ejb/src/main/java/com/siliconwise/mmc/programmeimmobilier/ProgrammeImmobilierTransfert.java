package com.siliconwise.mmc.programmeimmobilier;

import java.io.Serializable;
import java.util.Set;

import com.siliconwise.common.document.Document;
import com.siliconwise.mmc.modefinancement.ModeFinancement;



/**
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public class ProgrammeImmobilierTransfert implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private ProgrammeImmobilier programmeImmobilier ;
	
	private Set<ModeFinancement> modeFinancementList ;
	
	private Set<Document> videosList ; 

	public ProgrammeImmobilier getProgrammeImmobilier() {
		return programmeImmobilier;
	}

	public void setProgrammeImmobilier(ProgrammeImmobilier programmeImmobilier) {
		this.programmeImmobilier = programmeImmobilier;
	}

	public Set<ModeFinancement> getModeFinancementList() {
		return modeFinancementList;
	}

	public void setModeFinancementList(Set<ModeFinancement> modeFinancementList) {
		this.modeFinancementList = modeFinancementList;
	}
	
	public Set<Document> getVideosList() {
		return videosList;
	}

	public void setVideosList(Set<Document> videosList) {
		this.videosList = videosList;
	}

	@Override
	public String toString() {
		return "ProgrammeImmobilierTransfert [programmeImmobilier=" + programmeImmobilier + ", modeFinancementList="
				+ modeFinancementList + "]";
	}

	
}
