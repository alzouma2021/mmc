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

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Qualifier
@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER, LOCAL_VARIABLE, METHOD})
/**
 * Annotation used to load a session bag given its :
 * 	-  id
 * @author sysadmin
 *
 */
public @interface LoadSessionBag {

	 @Nonbinding String id() default "" ; // session id, required
}
