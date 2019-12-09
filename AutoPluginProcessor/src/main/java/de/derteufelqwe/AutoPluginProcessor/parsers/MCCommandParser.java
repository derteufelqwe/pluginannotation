package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.Map;

public class MCCommandParser extends Parser {


    public MCCommandParser(RoundEnvironment roundEnv, Messager messager, Types typeUtils) {
        super(roundEnv, messager, MCCommand.class, typeUtils);
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
        Map<String, Object> result = super.parse();

        if (result.size() != 0)
            map.put("commands", result);

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onCommandExecutor(element);
    }

    public Map<String, String> getCommandMap() {
        Map<String, String> resMap = new HashMap<>();

        for (Element element : getElements()) {
            String cmdName = element.getAnnotation(MCCommand.class).command();
            String clazz = element.toString();
            if (resMap.containsKey(cmdName)) {
                throw new ProcessingException("Found multiple classes with '%s' command. Class %s and %s.",
                        cmdName, resMap.get(cmdName), clazz);
            } else {
                resMap.put(cmdName, clazz);
            }
        }

        return resMap;
    }

}
