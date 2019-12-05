package de.derteufelqwe.AutoPluginProcessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MCCommand {
    String command();
    String description() default "";
    String permission() default "";
    String permissionMessage() default "";
}
