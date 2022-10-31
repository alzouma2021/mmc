package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * Entity implementation class for Entity: GlobalParameterBoolean
 *
 */
@Table(name = "globalparameterlongtext")
@Entity
@DiscriminatorValue("LONG_TEXT")
public class GlobalParameterLongText extends GlobalParameter<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message="entity.global-parameter.value.not_null") // variable :1: id delavariable
	@Column(nullable=false)
	private String value = null ;

	public GlobalParameterLongText() {
		super();
	}
	
	@Override @Lob
	public String getValue() {
		return this.value;
	}

	@Override 
	public void setValue(String value) {
		this.value = value;
	}

	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	
	
}
