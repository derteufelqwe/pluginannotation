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
import java.util.regex.Pattern;

public class MCCommandParser extends Parser {

    private final Pattern cmdPattern = Pattern.compile("^(?!\\/).+");


    public MCCommandParser(RoundEnvironment roundEnv, Messager messager, Types typeUtils) {
        super(roundEnv, messager, MCCommand.class, typeUtils);
    }

    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> subCfgMap = new HashMap<>();
        MCCommand annotation = element.getAnnotation(MCCommand.class);

        if (!cmdPattern.matcher(annotation.command()).matches())
            throw new ValidationException(element, "Command '%s' contains invalid characters. Source: %s",
                    annotation.command(), element.toString());

        subCfgMap.put("description", annotation.description());
        if (!annotation.permission().equals(""))
            subCfgMap.put("permission", annotation.permission());
        if (!annotation.permissionMessage().equals(""))
            subCfgMap.put("permission-message", annotation.permissionMessage());
        if (!annotation.usage().equals(""))
            subCfgMap.put("usage", annotation.usage());

        map.put(annotation.command(), subCfgMap);

        return map;
    }

    @Override
    public void addContent(Map<String, Object> destination) {
        Map<String, Object> cmdMap = destination.keySet().contains("commands") ? (Map<String, Object>) destination.get("commands") : new HashMap<>();
        Map<String, Object> parsedData = super.parse();

        for (String key : parsedData.keySet()) {
            cmdMap.put(key, parsedData.get(key));
        }

        if (cmdMap.size() != 0)
            destination.put("commands", cmdMap);
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
