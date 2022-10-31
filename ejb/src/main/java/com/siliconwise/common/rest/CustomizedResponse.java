package com.siliconwise.common.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.siliconwise.mmc.message.NonLocalizedStatusMessage;


/**
 * Permet de construire les reponses aux requètes GET du client
 * @author Mr Ishyirimbere
 *
 */
@XmlRootElement(name = "Reponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomizedResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "data")
	private Object data = null ;
	
	
	@XmlElementWrapper(name = "message")
	@XmlElement(name = "messages")
	private List<NonLocalizedStatusMessage> msgList = new ArrayList<>() ;
	
	public CustomizedResponse() {}
	
	public CustomizedResponse(Object data, List<NonLocalizedStatusMessage> msgList) {
		super();
		this.data = data;
		this.msgList = msgList;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<NonLocalizedStatusMessage> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<NonLocalizedStatusMessage> msgList) {
		this.msgList = msgList;
	}

	@Override
	public String toString() {
		return "CustomizedResponse [data=" + data + ", msgList=" + msgList + "]";
	}
	
}
