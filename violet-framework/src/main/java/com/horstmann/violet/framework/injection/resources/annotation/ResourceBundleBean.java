package com.horstmann.violet.framework.injection.resources.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ResourceBundleBean
{

    public String key() default "";

    public Class resourceReference() default Object.class;

}
