package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCDepend;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MCDependParser extends Parser {

    private final Pattern dependNamePattern = Pattern.compile("[a-z,A-Z,0-9,_]+");


    public MCDependParser(Parser.Data data) {
        super(data, MCDepend.class);
    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        Map<String, Object> map = new HashMap<>();
        MCDepend annotation = element.getAnnotation(MCDepend.class);
        List<String> dependencys = Arrays.asList(annotation.value());

        for (String dep : dependencys) {
            if (!dependNamePattern.matcher(dep).matches())
                throw new ValidationException(element, "Depend has faulty dependency name '%s'. Source: %s",
                        dep, element);
        }

        map.put("depend", dependencys);

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onMCPlugin(element);
    }
}
