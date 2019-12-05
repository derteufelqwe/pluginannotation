package de.derteufelqwe.AutoPluginProcessor.exceptions;

import javax.lang.model.element.Element;

public class ProcessingException extends RuntimeException {

    private Element element;

    public ProcessingException() {
        super();
    }

    public ProcessingException(String message) {
        super(message);
    }

    public ProcessingException(Element element, String message) {
        super(message);
        this.element = element;
    }


    public Element getElement() {
        return this.element;
    }

}
