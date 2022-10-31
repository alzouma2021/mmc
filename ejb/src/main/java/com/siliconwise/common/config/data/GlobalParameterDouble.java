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
@NamedQueries(value={
		@NamedQuery(
				name="trouverTousLesGlobalesParameterDouble",
				query="SELECT gpd FROM GlobalParameterDouble gpd "
						+ "WHERE "
						+ " gpd.isOnlySupport =:estSupport "
						+ " ORDER BY gpd.designation"),
})
@Entity @Table(name = "globalparameterdouble")
@DiscriminatorValue("DOUBLE")
public class GlobalParameterDouble extends GlobalParameter<Double> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message="entity.global-parameter.value.not_null") // variable :1: id delavariable
	@Column(nullable=false)
	private Double value = null ;

	public GlobalParameterDouble() {
		super();
	}
	
	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public Map<String, String> getMsgVarMap() {
		
		Map<String,String> rtn =  new HashMap<String,String>();
		
		rtn.put(AppMessageKeys.CODE_TRADUCTION_VARIABLE_ENTITE_IDENTIFIANT, getId()) ;
		
		return rtn;
	}
	

}
