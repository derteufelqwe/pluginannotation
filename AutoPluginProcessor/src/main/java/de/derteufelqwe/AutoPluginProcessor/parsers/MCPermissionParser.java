package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCPermission;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.HashMap;
import java.util.Map;

public class MCPermissionParser extends Parser {

    public MCPermissionParser(Parser.Data data) {
        super(data, MCPermission.class);
    }

    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> subCfgMap = new HashMap<>();
        MCPermission annotation = element.getAnnotation(MCPermission.class);

        subCfgMap.put("description", annotation.description());
        if (!annotation.default_().equals(MCPermission.Defaults.NONE))
            subCfgMap.put("default", annotation.default_().toString());

        map.put(annotation.name(), subCfgMap);

        return map;
    }

    @Override
    public Map<String, Object> parse() throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> result = super.parse();

        if (result.size() != 0)
            map.put("permissions", result);

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return true;
    }
}
