/**
 * 
 */
package com.siliconwise.mmc.security;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER, LOCAL_VARIABLE, METHOD})
/**
 * Annotation used to load system session bags list 
 * @author sysadmin
 *
 */
public @interface LoadSessionBagList {
}
