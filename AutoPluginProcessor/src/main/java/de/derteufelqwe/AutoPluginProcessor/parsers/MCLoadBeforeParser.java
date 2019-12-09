package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCLoadBefore;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MCLoadBeforeParser extends Parser {

    public MCLoadBeforeParser(RoundEnvironment roundEnv, Messager messager, Types typeUtils) {
        super(roundEnv, messager, MCLoadBefore.class, typeUtils);
    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        MCLoadBefore annotation = element.getAnnotation(MCLoadBefore.class);

        map.put("loadbefore", Arrays.asList(annotation.value()));

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onMCPlugin(element);
    }

}
