package com.ioc.annotations;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;

import java.lang.annotation.Target;

@Target({ METHOD, CONSTRUCTOR, FIELD, TYPE })
@Retention(RUNTIME)
public @interface Bean {
public String name();
}