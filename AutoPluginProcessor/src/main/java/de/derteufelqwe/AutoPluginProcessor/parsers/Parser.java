package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.Validator;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class Parser {

    protected RoundEnvironment roundEnv;
    protected Class<? extends Annotation> annotationClass;
    protected Validator validator;
    protected Messager messager;

    private Map<String, Object> configMap = new HashMap<>();

    public Parser(RoundEnvironment roundEnv, Messager messager, Class<? extends Annotation> annotationClass) {
        this.roundEnv = roundEnv;
        this.messager = messager;
        this.annotationClass = annotationClass;

        this.validator = new Validator(annotationClass);
    }

    public Map<String, Object> parse() throws ProcessingException {
        for (Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            validate(element);
            configMap.putAll(singleParse(element));
        }

        return configMap;
    }

    protected abstract Map<String, Object> singleParse(Element element) throws ProcessingException;

    protected abstract boolean validate(Element element) throws ValidationException;


    protected void note(Element element, String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg, element);
    }

    protected void warning(Element element, String msg) {
        messager.printMessage(Diagnostic.Kind.WARNING, msg, element);
    }

    protected void error(Element element, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

}
