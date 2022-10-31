/**
 * 
 */
package com.siliconwise.mmc.common.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Inherited
@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
/**
 * Qualifier to inject the right entity initializer
 * @author TOSHIBA
 *
 */
public @interface EntityInitializerQualifier {

	TargetEntityEnum value() ;
	
	@Nonbinding String description() default "" ; 
	
	public enum TargetEntityEnum {
		ALL, POSE_ET_DEPOSE ;
	}
}
