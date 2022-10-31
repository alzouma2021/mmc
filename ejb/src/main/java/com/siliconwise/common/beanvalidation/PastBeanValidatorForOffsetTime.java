/**
 * 
 */
package com.siliconwise.common.beanvalidation;

import java.time.OffsetTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;


/**  PastBeanValidatorForJodaLocalDate
 * @author TOSHIBA
 *
 */
public class PastBeanValidatorForOffsetTime implements
	ConstraintValidator<Past, OffsetTime> {

	public void initialize(Past constraintAnnotation) {}

	public boolean isValid(OffsetTime value,
			ConstraintValidatorContext constraintValidatorContext) {

		if(value == null) {
			return true;
		}

		return value.isBefore(OffsetTime.now());
	}
} 