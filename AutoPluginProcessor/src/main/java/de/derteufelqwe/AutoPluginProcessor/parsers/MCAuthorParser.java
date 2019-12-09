package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCAuthor;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses all {@Link MCAuthor} Annotations
 */
public class MCAuthorParser extends Parser {

    public MCAuthorParser(RoundEnvironment roundEnv, Messager messager, Types typeUtils) {
        super(roundEnv, messager, MCAuthor.class, typeUtils);
    }

    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("authors", Arrays.asList(element.getAnnotation(MCAuthor.class).value()));

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onJavaPlugin(element) && validator.onMCPlugin(element);
    }
}
