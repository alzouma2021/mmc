package com.siliconwise.common.document;

import java.io.Serializable;

/**
 * 
 *@author Alzouma Moussa Mahamadou
 *
 *DocumentEntityTransfert est une classe de transfert, utilisée pour la creation d'un document
 *
 *appartenant à une entité , créée au préalable.
 *
 */
public class DocumentEntityTransfert implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	
	private Document document ;
	
	private String entityName ;
	
	private String entityId ;
	

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	
}
