package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCTabComplete;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.Map;

public class MCTabCompletParser extends Parser {



    public MCTabCompletParser(Parser.Data data) {
        super(data, MCTabComplete.class);
    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        return null;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onTabCompleter(element);
    }

    public Map<String, String> getCompleterMap() {
        Map<String, String> resMap = new HashMap<>();

        for (Element element : getElementsWithCache()) {
            String cmdName = element.getAnnotation(MCTabComplete.class).value();
            String clazz = element.toString();
            if (resMap.containsKey(cmdName)) {
                throw new ProcessingException("Found multiple classes with '%s' command for TabCompleters. Class %s and %s.",
                        cmdName, resMap.get(cmdName), clazz);
            } else {
                resMap.put(cmdName, clazz);
            }
        }

        return resMap;
    }

}
