package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.misc.Validator;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Parser {

    protected RoundEnvironment roundEnv;
    protected Class<? extends Annotation> annotationClass;
    protected Validator validator;
    protected Messager messager;


    public Parser(Data constructorData, Class<? extends Annotation> annotationClass) {
        this.roundEnv = constructorData.roundEnv;
        this.messager = constructorData.messager;
        this.annotationClass = annotationClass;

        this.validator = new Validator(annotationClass, constructorData.typeUtils);
    }


    /**
     * Returns a List of all valid elements.
     */
    public List<Element> getElements() {
        List<Element> elements = new ArrayList<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            if (validate(element)) {
                elements.add(element);
            }
        }

        return elements;
    }

    public Map<String, Object> parse() throws ProcessingException {
        Map<String, Object> configMap = new HashMap<>();

        for (Element element : getElements()) {
            configMap.putAll(singleParse(element));
        }

        return configMap;
    }

    protected abstract Map<String, Object> singleParse(Element element) throws ProcessingException;

    protected abstract boolean validate(Element element) throws ValidationException;

    public void addContent(Map<String, Object> destination) {
        destination.putAll(this.parse());
    }


    protected void note(Element element, String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg, element);
    }

    protected void warning(Element element, String msg) {
        messager.printMessage(Diagnostic.Kind.WARNING, msg, element);
    }

    protected void error(Element element, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, element);
    }


    // ---------  Parser-Data  ---------


    public static class Data {

        public RoundEnvironment roundEnv;
        public Messager messager;
        public Types typeUtils;
        public List<Element> additionalElements;


        public Data(RoundEnvironment roundEnv, Messager messager, Types typeUtils, List<Element> additionalElements) {
            this.roundEnv = roundEnv;
            this.messager = messager;
            this.typeUtils = typeUtils;
            this.additionalElements = additionalElements;
        }


    }
}
