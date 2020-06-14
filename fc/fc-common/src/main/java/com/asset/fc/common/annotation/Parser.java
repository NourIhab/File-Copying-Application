package com.asset.fc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//2- add meta-annotations to specify the scope and the target of our custom annotation
@Retention(RetentionPolicy.RUNTIME) // has run time visiblity
@Target(ElementType.TYPE) // can be applied into (types (classes))
// 1-Creating a custom annotation 
public @interface Parser {

    public String type();
}
