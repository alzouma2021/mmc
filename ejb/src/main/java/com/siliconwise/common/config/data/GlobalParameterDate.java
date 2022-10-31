package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.siliconwise.mmc.message.AppMessageKeys;


/**
 * Entity implementation class for Entity: GlobalParameterBoolean
 *
 */
@Table(name = "globalparameterdate")
@Entity 
@DiscriminatorValue("DATE")
public class GlobalParameterDate extends GlobalParameter<LocalDate> implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message="entity.global-parameter.value.not_null") // variable :1: id delavariable
	@Column(nullable=false)
	private LocalDate value = null ;

	public GlobalParameterDate() {
		super();
	}
	
	@Override 
	public LocalDate getValue() {
		return this.value;
	}

	@Override
	public void setValue(LocalDate value) {
		this.value = value;
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}

}
