/**
 * 
 */
package com.siliconwise.common.config.data;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
 * Frais d'édition de macaron defini dans les données de configuration globale
 * @author GNAKALE Bernardin
 *
 */
public @interface DataConfigFraisEditionMacaron {

}
