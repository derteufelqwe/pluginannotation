package de.derteufelqwe.AutoPluginProcessor.parsers;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCListener;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MCListenerParser extends Parser {


    public MCListenerParser(Parser.Data data) {
        super(data, MCListener.class);
    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        return null;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onListener(element);
    }

    public List<String> getListeners() {
        List<String> resList = new ArrayList<>();

        for (Element element : getElements()) {
            resList.add(element.toString());
        }

        return resList;
    }

}
