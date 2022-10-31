package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.siliconwise.common.reference.Reference;
import com.siliconwise.mmc.message.AppMessageKeys;

/**
 * Entity implementation class for Entity: GlobalParameterBoolean
 *
 */
@Table(name = "globalparameterreference")
@Entity
@DiscriminatorValue("Reference")
public class GlobalParameterReference extends GlobalParameter<Reference> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@NotNull(message="entity.global-parameter.value.not_null") // variable :1: id delavariable
	@JoinColumn(nullable=false)
	private Reference value = null ;

	public GlobalParameterReference() {
		super();
	}
	
	@Override
	public Reference getValue() {
		return this.value;
	}

	@Override 
	public void setValue(Reference value) {
		this.value = value;
	}

	
	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	
   
}
