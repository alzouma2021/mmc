package com.siliconwise.common.config.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.siliconwise.mmc.message.AppMessageKeys;
//import com.siliconwise.common.datetime.LocalDateTimeAdapter;
/**
 * Entity implementation class for Entity: GlobalParameterBoolean
 *
 */
@SuppressWarnings("unused")
@Table(name = "globalparameterdatetime")
@Entity
@DiscriminatorValue("DATETIME")
public class GlobalParameterDateTime extends GlobalParameter<LocalDateTime> implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message="entity.global-parameter.value.not_null") // variable :1: id delavariable
	@Column(nullable=false)
	//@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime value = null ;

	public GlobalParameterDateTime() {
		super();
	}
	
	@Override 
	public LocalDateTime getValue() {
		return this.value;
	}

	@Override
	public void setValue(LocalDateTime value) {
		this.value = value;
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	 
	

}
