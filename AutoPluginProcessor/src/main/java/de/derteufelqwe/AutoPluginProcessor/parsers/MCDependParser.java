package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCDepend;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MCDependParser extends Parser {

    public MCDependParser(RoundEnvironment roundEnv, Messager messager) {
        super(roundEnv, messager, MCDepend.class);
    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        MCDepend annotation = element.getAnnotation(MCDepend.class);

        map.put("depend", Arrays.asList(annotation.value()));

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onMCPlugin(element);
    }
}
