package de.derteufelqwe.AutoPluginProcessor;

import com.google.auto.service.AutoService;
import de.derteufelqwe.AutoPluginProcessor.annotations.*;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ExitException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.parsers.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.*;


@AutoService(Processor.class)
public class PluginProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    private FileObject pluginYMLResource;
    private Yaml yaml;
    private Map<String, Object> pluginConfigMap = new HashMap<>();
    private Writer writer;


    public PluginProcessor() {
        DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new Yaml(options);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(MCPlugin.class.getCanonicalName());
        set.add(MCAPIVersion.class.getCanonicalName());
        set.add(MCAuthor.class.getCanonicalName());
        set.add(MCAPIVersion.class.getCanonicalName());
        set.add(MCDepend.class.getCanonicalName());
        set.add(MCLoad.class.getCanonicalName());
        set.add(MCLoadBefore.class.getCanonicalName());
        set.add(MCSoftDepend.class.getCanonicalName());

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

        // Initialize the config.
        try {
            setupConfig();
        } catch (IOException e) {
            error(null, String.format("IOException while setting up config: %s", e.getMessage()));
        }

        return;

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        try {
            MCPluginParser mcPluginParser = new MCPluginParser(roundEnv, messager, yaml);
            pluginConfigMap.putAll(mcPluginParser.parse());

            MCAuthorParser mcAuthorParser = new MCAuthorParser(roundEnv, messager);
            pluginConfigMap.putAll(mcAuthorParser.parse());

            MCAPIVersionParser mcapiVersionParser = new MCAPIVersionParser(roundEnv, messager);
            pluginConfigMap.putAll(mcapiVersionParser.parse());

            MCDependParser mcDependParser = new MCDependParser(roundEnv, messager);
            pluginConfigMap.putAll(mcDependParser.parse());

            MCSoftDependParser mcSoftDependParser = new MCSoftDependParser(roundEnv, messager);
            pluginConfigMap.putAll(mcSoftDependParser.parse());

            MCLoadParser mcLoadParser = new MCLoadParser(roundEnv, messager);
            pluginConfigMap.putAll(mcLoadParser.parse());

            MCLoadBeforeParser mcLoadBeforeParser = new MCLoadBeforeParser(roundEnv, messager);
            pluginConfigMap.putAll(mcLoadBeforeParser.parse());

        } catch (ExitException e1) {
        } catch (ProcessingException e2) {
            error(null, "Exception: " + e2.getMessage());
        }catch (Exception e3) {
            error(null, "Unknown exception: " + e3.getMessage());
        }

        // Save config to file when processing is done
        if (roundEnv.processingOver()) {
            try {
                saveConfigToFile();
            } catch (IOException e) {
                error(null, String.format("IOException while saving config: %s", e.getMessage()));
            }
        }

        return true;
    }


    // ---------------  Setups  ---------------

    private void setupConfig() throws IOException {
        if (pluginYMLResource == null)
            pluginYMLResource = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", Config.CONFIG_FILE_NAME);
    }


    private void saveConfigToFile() throws IOException {
        if (writer == null)
            writer = pluginYMLResource.openWriter();
        yaml.dump(pluginConfigMap, writer);
        writer.close();
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
