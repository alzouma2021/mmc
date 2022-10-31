package com.siliconwise.mmc.message;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;

public class CustomizedEjbException extends EJBException {

	private static final long serialVersionUID = 1L;
	
	private List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
	private Response.Status status = null ;
	
	public CustomizedEjbException(List<NonLocalizedStatusMessage> msgList) {
		super();
		this.msgList = msgList;
	}
	
	public CustomizedEjbException(
			Exception ex,  
			Response.Status status, 
			List<NonLocalizedStatusMessage> msgList) {
		
		super(ex);
		this.msgList = msgList;
		this.status = status ;
	}

	public List<NonLocalizedStatusMessage> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<NonLocalizedStatusMessage> msgList) {
		this.msgList = msgList;
	}

	public Response.Status getStatus() {
		return status;
	}

	public void setStatus(Response.Status status) {
		this.status = status;
	}

	public CustomizedEjbException(
			String message, 
			Exception ex, 
			List<NonLocalizedStatusMessage> msgList) {
		
		super(message, ex);
		this.msgList = msgList;
	}

	public CustomizedEjbException(String message, List<NonLocalizedStatusMessage> msgList) {
		super(message);
		this.msgList = msgList;
	}
	
	
	

}
