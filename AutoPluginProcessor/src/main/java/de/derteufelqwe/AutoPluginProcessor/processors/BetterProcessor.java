package de.derteufelqwe.AutoPluginProcessor.processors;

import de.derteufelqwe.AutoPluginProcessor.exceptions.ExitException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

public abstract class BetterProcessor extends AbstractProcessor {

    private boolean didSetup = false;

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeUtils = processingEnv.getTypeUtils();
        this.elementUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }


    public synchronized void finish() {

    }

    /**
     * Called once in the process method.
     */
    public void setup() {

    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean returnValue = false;

        try {
            if (!didSetup) {
                try {
                    setup();
                } catch (Exception e) {
                    error(e.getMessage());
                }
                didSetup = true;
            }

            returnValue = safeProcess(annotations, roundEnv);

        } catch (ExitException e1) {

        } catch (ProcessingException e2) {
            error("ProcessingException: " + e2.getMessage());
        } catch (Exception e3) {
            error("Unknown exception: " + e3.getMessage());
        }

        if (roundEnv.processingOver()) {
            try {
                this.finish();
            } catch (Exception e) {
                error(e.getMessage());
            }
        }

        return returnValue;
    }

    public abstract boolean safeProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);


    protected void note(String msg, Element element) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, msg, element);
    }

    protected void note(String msg) {
        note(msg, null);
    }

    protected void warning(String msg, Element element) {
        this.messager.printMessage(Diagnostic.Kind.WARNING, msg, element);
    }

    protected void warning(String msg) {
        warning(msg, null);
    }

    protected void error(String msg, Element element) {
        this.messager.printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    protected void error(String msg) {
        error(msg, null);
    }


}
