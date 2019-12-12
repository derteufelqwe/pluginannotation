package de.derteufelqwe.AutoPluginProcessor;

public class TestException extends RuntimeException {

    public TestException(String message) {
        super(message);
    }

    public TestException(String message, Object... args) {
        super(String.format(message, args));
    }

}
