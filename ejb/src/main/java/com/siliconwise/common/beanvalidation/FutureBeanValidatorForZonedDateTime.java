/**
 * 
 */
package com.siliconwise.common.beanvalidation;

import java.time.ZonedDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;


/**  PastBeanValidatorForJodaLocalDate
 * @author TOSHIBA
 *
 */
public class FutureBeanValidatorForZonedDateTime implements
	ConstraintValidator<Past, ZonedDateTime> {

	public void initialize(Past constraintAnnotation) {}

	public boolean isValid(ZonedDateTime value,
			ConstraintValidatorContext constraintValidatorContext) {

		if(value == null) return true;
		
		return value.isAfter(ZonedDateTime.now());
	}
	
} 