package com.siliconwise.mmc.produitlogement;

import java.util.Set;

import com.siliconwise.common.document.Document;

/**
 * 
 * Classe de transfert de données
 * 
 * @author Alzouma Moussa Mahamadou
 *
 */
public class ProduitLogementTransfert {
	

	private ProduitLogement produitLogement ;
	
	private Document imageConsultation  ;
	
	private Set<Document> imagesList ;
	
	private Set<Document> videosList ;

	public ProduitLogement getProduitLogement() {
		return produitLogement;
	}

	public void setProduitLogement(ProduitLogement produitLogement) {
		this.produitLogement = produitLogement;
	}

	public Set<Document> getImagesList() {
		return imagesList;
	}

	public void setImagesList(Set<Document> imagesList) {
		this.imagesList = imagesList;
	}

	public Set<Document> getVideosList() {
		return videosList;
	}

	public void setVideosList(Set<Document> videosList) {
		this.videosList = videosList;
	}

	public Document getImageConsultation() {
		return imageConsultation;
	}

	public void setImageConsultation(Document imageConsultation) {
		this.imageConsultation = imageConsultation;
	}
	
}
