package de.derteufelqwe.AutoPluginProcessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MCPlugin {
    String pluginName();
    String srcPath() default "src/main/java/";
    String resourcePath() default "src/main/resources/";
}
