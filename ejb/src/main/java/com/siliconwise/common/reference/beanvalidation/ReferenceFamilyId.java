package com.siliconwise.common.reference.beanvalidation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.siliconwise.common.reference.beanvalidation.ReferenceFamilyId.List ;

/** Annotation to enforce that reference id is equal to the supplied family id
 * @author sysadmin
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, ANNOTATION_TYPE, TYPE_PARAMETER, TYPE_USE })
@Repeatable(List.class)
@Constraint(validatedBy = {ReferenceFamilyIdValidator.class})
public @interface ReferenceFamilyId {

    String message() default "Reference family id must be {value}";
    
    Class<?>[] groups() default { };
    
    Class<? extends Payload>[] payload() default { };

    String value(); // reference family id

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
    	ReferenceFamilyId[] value();
    }

}
