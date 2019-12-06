package de.derteufelqwe.AutoPluginProcessor;

import com.google.auto.service.AutoService;
import de.derteufelqwe.AutoPluginProcessor.annotations.*;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ExitException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.parsers.MCCommandParser;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import java.util.Set;


@AutoService(Processor.class)
public class CommandProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(MCCommand.class.getCanonicalName());

        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        try {
            MCCommandParser mcCommandParser = new MCCommandParser(roundEnv, messager);
            mcCommandParser.parse();
//            pluginConfigMap.putAll(mcCommandParser.parse());
        } catch (ExitException e1) {
        } catch (ProcessingException e2) {
            error(null, "Exception: " + e2.getMessage());
        }catch (Exception e3) {
            error(null, "Unknown exception: " + e3.getMessage());
        }


        return false;
    }


    private void error(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
    }

    private void warning(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.WARNING, msg, e);
    }

    private void note(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg, e);
    }


}
