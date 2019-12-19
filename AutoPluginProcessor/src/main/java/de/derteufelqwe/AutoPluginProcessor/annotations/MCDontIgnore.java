package de.derteufelqwe.AutoPluginProcessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Mark a class for the annotation processor, even when there are no valid classes.
 */
@Target(ElementType.TYPE)
public @interface MCDontIgnore {
}
