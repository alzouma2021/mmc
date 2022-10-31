package com.siliconwise.mmc.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import javax.interceptor.InterceptorBinding;

/** Check if incoming call has a session id defined
 * @author sysadmin
 *
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
public @interface UserIsLoggedIn {	

}
