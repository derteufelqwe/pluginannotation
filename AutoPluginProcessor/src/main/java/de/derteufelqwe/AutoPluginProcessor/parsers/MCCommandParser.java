package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.HashMap;
import java.util.Map;

public class MCCommandParser extends Parser {


    public MCCommandParser(RoundEnvironment roundEnv, Messager messager) {
        super(roundEnv, messager, MCCommand.class);
    }

    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> subCfgMap = new HashMap<>();
        MCCommand annotation = element.getAnnotation(MCCommand.class);

        subCfgMap.put("description", annotation.description());
        if (!annotation.permission().equals(""))
            subCfgMap.put("permission", annotation.permission());
        if (!annotation.permissionMessage().equals(""))
            subCfgMap.put("permission-message", annotation.permissionMessage());

        map.put(annotation.command(), subCfgMap);

        return map;
    }

    @Override
    public Map<String, Object> parse() throws ProcessingException {
        Map<String, Object> map = new HashMap<>();

        map.put("commands", super.parse());

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onCommandExecutor(element);
    }

}
