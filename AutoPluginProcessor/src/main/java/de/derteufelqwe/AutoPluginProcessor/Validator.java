package de.derteufelqwe.AutoPluginProcessor;

import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {

    private Class<? extends Annotation> clazz;

    public Validator(Class<? extends Annotation> annotationClass) {
        this.clazz = annotationClass;
    }


    public boolean onJavaPlugin(Element element) throws ValidationException {
        // Check if on Class
        if (element.getKind() != ElementKind.CLASS)
            throw new ValidationException(element, String.format("@%s can only be applied to Classes. Source: %s",
                    clazz.getSimpleName(), element.toString()));
        TypeElement typeElement = (TypeElement) element;

        // Check if it inherits JavaPlugin
        // ToDo: Add support for non-direct inheritance
        if (!typeElement.getSuperclass().toString().equals(JavaPlugin.class.getName()))
            throw new ValidationException(element, String.format("@%s annotated classes need to extend %s. Source: %s",
                    clazz.getSimpleName(), JavaPlugin.class.getSimpleName(), element.toString()));

        return true;
    }

    public boolean onMCPlugin(Element element) throws ValidationException {
        if (element.getAnnotation(MCPlugin.class) == null)
            throw new ValidationException(element, String.format("@%s can only be applied to classes annotated with @%s. Source: %s",
                    clazz.getSimpleName(), MCPlugin.class.getSimpleName(), element.toString()));
        return true;
    }

    public boolean onCommandExecutor(Element element) {
        // Check if on Class
        if (element.getKind() != ElementKind.CLASS)
            throw new ValidationException(element, String.format("@%s can only be applied to Classes. Source: %s",
                    clazz.getSimpleName(), element.toString()));
        TypeElement typeElement = (TypeElement) element;

        // Check if it inherits CommandExecutor
        // ToDo: Add support for non-direct inheritance
        List<String> interfaces = typeElement.getInterfaces().stream().map(i -> ((TypeMirror) i).toString()).collect(Collectors.toList());

        if (!interfaces.contains(CommandExecutor.class.getName()))
            throw new ValidationException(element, String.format("@%s annotated classes need to implement %s. Source: %s",
                    clazz.getSimpleName(), CommandExecutor.class.getSimpleName(), element.toString()));

        return true;
    }

}
