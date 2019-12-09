package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCAPIVersion;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses all {@Link MCAPIVersion} Annotations
 */
public class MCAPIVersionParser extends Parser {

    public MCAPIVersionParser(RoundEnvironment roundEnv, Messager messager, Types typeUtils) {
        super(roundEnv, messager, MCAPIVersion.class, typeUtils);
    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        MCAPIVersion annotation = element.getAnnotation(MCAPIVersion.class);

        map.put("api-version", annotation.value());

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onMCPlugin(element);
    }

}
