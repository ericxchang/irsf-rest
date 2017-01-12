package com.iconectiv.irsf.json.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonRule {
	public boolean required() default false;
	public Class<?> dataType() default String.class;
}
