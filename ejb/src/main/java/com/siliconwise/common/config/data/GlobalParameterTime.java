package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * Entity implementation class for Entity: GlobalParameterBoolean
 *
 */
@SuppressWarnings("unused")
@Table(name = "globalparametertime")
@Entity
@DiscriminatorValue("TIME")
public class GlobalParameterTime extends GlobalParameter<LocalTime> implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message="entity.global-parameter.value.not_null") // variable :1: id delavariable
	@Column(nullable=false)
	//@XmlJavaTypeAdapter( value= LocalDateTimeAdapter.class, type= LocalTime.class)
	private LocalTime value = null ;

	public GlobalParameterTime() {
		super();
	}
	
	@Override 
	public LocalTime getValue() {
		return this.value;
	}

	@Override
	public void setValue(LocalTime value) {
		this.value = value;
	}

	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	

}
