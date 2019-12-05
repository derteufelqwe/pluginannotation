package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCDepend;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCSoftDepend;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MCSoftDependParser extends Parser {

    public MCSoftDependParser(RoundEnvironment roundEnv, Messager messager) {
        super(roundEnv, messager, MCSoftDepend.class);
    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        MCSoftDepend annotation = element.getAnnotation(MCSoftDepend.class);

        map.put("soft-depend", Arrays.asList(annotation.value()));

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onMCPlugin(element);
    }

}
