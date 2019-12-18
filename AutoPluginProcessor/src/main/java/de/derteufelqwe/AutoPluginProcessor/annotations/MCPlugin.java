package de.derteufelqwe.AutoPluginProcessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MCPlugin {
    String name();
    String version();
    String description() default "";
    String prefix() default "";

}
