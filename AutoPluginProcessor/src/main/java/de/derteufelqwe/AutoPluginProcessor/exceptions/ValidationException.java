package de.derteufelqwe.AutoPluginProcessor.exceptions;

import javax.lang.model.element.Element;

public class ValidationException extends ProcessingException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Element element, String message) {
        super(element, message);
    }


}
