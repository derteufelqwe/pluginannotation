package de.derteufelqwe.AutoPluginProcessor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.processing.JavacFiler;
import de.derteufelqwe.AutoPluginProcessor.annotations.*;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.misc.AutoRegisterGenerator;
import de.derteufelqwe.AutoPluginProcessor.misc.Config;
import de.derteufelqwe.AutoPluginProcessor.parsers.*;
import de.derteufelqwe.AutoPluginProcessor.processors.BetterProcessor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


@AutoService(Processor.class)
public class PluginProcessor extends BetterProcessor {

    private String fileName = Config.CONFIG_FILE_NAME;
    private FileObject outputFile;
    private Map<String, Object> yamlContent = new HashMap<>();
    private Yaml yaml = getYAML();


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(MCPlugin.class.getCanonicalName());
        set.add(MCAuthor.class.getCanonicalName());
        set.add(MCAPIVersion.class.getCanonicalName());
        set.add(MCAPIVersion.class.getCanonicalName());
        set.add(MCDepend.class.getCanonicalName());
        set.add(MCLoad.class.getCanonicalName());
        set.add(MCLoadBefore.class.getCanonicalName());
        set.add(MCSoftDepend.class.getCanonicalName());
        set.add(MCPermission.class.getCanonicalName());

        set.add(MCCommand.class.getCanonicalName());
        set.add(MCTabComplete.class.getCanonicalName());
        set.add(MCPlugin.class.getCanonicalName());

        return set;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public void setup() {
        try {
            outputFile = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
        } catch (IOException e) {
            throw new ProcessingException("Failed to create resource %s: %s", fileName, e.getMessage());
        }

    }

    @Override
    public boolean safeProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        MCPluginParser mcPluginParser = new MCPluginParser(roundEnv, messager, typeUtils, yaml);
        yamlContent.putAll(mcPluginParser.parse());

        MCAuthorParser mcAuthorParser = new MCAuthorParser(roundEnv, messager, typeUtils);
        yamlContent.putAll(mcAuthorParser.parse());

        MCAPIVersionParser mcapiVersionParser = new MCAPIVersionParser(roundEnv, messager, typeUtils);
        yamlContent.putAll(mcapiVersionParser.parse());

        MCDependParser mcDependParser = new MCDependParser(roundEnv, messager, typeUtils);
        yamlContent.putAll(mcDependParser.parse());

        MCSoftDependParser mcSoftDependParser = new MCSoftDependParser(roundEnv, messager, typeUtils);
        yamlContent.putAll(mcSoftDependParser.parse());

        MCLoadParser mcLoadParser = new MCLoadParser(roundEnv, messager, typeUtils);
        yamlContent.putAll(mcLoadParser.parse());

        MCLoadBeforeParser mcLoadBeforeParser = new MCLoadBeforeParser(roundEnv, messager, typeUtils);
        yamlContent.putAll(mcLoadBeforeParser.parse());

        MCCommandParser mcCommandParser = new MCCommandParser(roundEnv, messager, typeUtils);
        yamlContent.putAll(mcCommandParser.parse());

        AutoRegisterGenerator cmdGen = new AutoRegisterGenerator(roundEnv, messager, typeUtils, filer, annotations.size());
        cmdGen.generateClass();

        return true;
    }


    @Override
    public synchronized void finish() {
        Writer writer = null;
        try {
            writer = outputFile.openWriter();
            yaml.dump(yamlContent, writer);
            writer.close();
        } catch (IOException e) {
            throw new ProcessingException("Failed to write content to file '%s': %s", fileName, e.getMessage());
        }
        ((JavacFiler) filer).close();
    }

    private Yaml getYAML() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);

        return new Yaml(options);
    }

}
