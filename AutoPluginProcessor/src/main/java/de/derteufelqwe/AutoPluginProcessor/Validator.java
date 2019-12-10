package de.derteufelqwe.AutoPluginProcessor;

import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {

    private Class<? extends Annotation> clazz;
    private Types typeUtils;


    public Validator(Class<? extends Annotation> annotationClass, Types typeUtils) {
        this.clazz = annotationClass;
        this.typeUtils = typeUtils;
    }


    public boolean onJavaPlugin(Element element) throws ValidationException {
        if (!onClass(element))
            return false;
        TypeElement typeElement = (TypeElement) element;

        // Check if it inherits JavaPlugin
        if (!transitiveInheritanceCheck(typeElement, JavaPlugin.class))
            throw new ValidationException(element, "@%s annotated classes need to extend %s. Source: %s",
                    clazz.getSimpleName(), JavaPlugin.class.getSimpleName(), element.toString());

        return true;
    }


    public boolean onMCPlugin(Element element) throws ValidationException {
        if (element.getAnnotation(MCPlugin.class) == null)
            throw new ValidationException(element,"@%s can only be applied to classes annotated with @%s. Source: %s",
                    clazz.getSimpleName(), MCPlugin.class.getSimpleName(), element.toString());
        return true;
    }


    public boolean onCommandExecutor(Element element) {
        if (!onClass(element))
            return false;
        TypeElement typeElement = (TypeElement) element;

        // Check if it implements CommandExecutor
        if (!transitiveInterfaceCheck(typeElement, CommandExecutor.class))
            throw new ValidationException(element, "@%s annotated classes need to implement %s. Source: %s",
                    clazz.getSimpleName(), CommandExecutor.class.getSimpleName(), element.toString());

        return true;
    }


    public boolean onTabCompleter(Element element) {
        if (!onClass(element))
            return false;
        TypeElement typeElement = (TypeElement) element;

        if (!transitiveInterfaceCheck(typeElement, TabCompleter.class))
            throw new ValidationException(element, "@%s annotated classes need to implement %s. Source: %s",
                    clazz.getSimpleName(), TabCompleter.class.getSimpleName(), element.toString());

        return true;
    }


    public boolean onListener(Element element) {
        if (!onClass(element))
            return false;
        TypeElement typeElement = (TypeElement) element;

        if (!transitiveInterfaceCheck(typeElement, Listener.class))
            throw new ValidationException(element, "@%s annotated classes need to implement %s. Source: %s",
                    clazz.getSimpleName(), Listener.class.getSimpleName(), element.toString());

        return true;
    }


    /**
     * Check if element is on a Class
     * @param element Element to check
     */
    private boolean onClass(Element element) {
        if (element.getKind() != ElementKind.CLASS)
            throw new ValidationException(element, "@%s can only be applied to Classes. Source: %s",
                    clazz.getSimpleName(), element.toString());

        return true;
    }


    private boolean transitiveInheritanceCheck(TypeElement element, Class clazz) {
        TypeElement workingElement = element;

        while (true) {
            TypeMirror superClassType = workingElement.getSuperclass();

            if (superClassType.getKind() == TypeKind.NONE) {
                return false;
            }

            if (superClassType.toString().equals(clazz.getName())) {
                return true;
            }

            workingElement = (TypeElement) typeUtils.asElement(superClassType);
        }
    }


    private boolean transitiveInterfaceCheck(TypeElement element, Class clazz) {
        TypeElement workingElement = element;

        while (true) {
            TypeMirror superClassType = workingElement.getSuperclass();

            if (superClassType.getKind() == TypeKind.NONE) {
                return false;
            }

            List<? extends TypeMirror> classInterfaces = workingElement.getInterfaces();

            for (TypeMirror classInterface : classInterfaces) {

                if (classInterface.toString().equals(clazz.getName())) {
                    return true;
                }
            }

            workingElement = (TypeElement) typeUtils.asElement(superClassType);
        }
    }

}
