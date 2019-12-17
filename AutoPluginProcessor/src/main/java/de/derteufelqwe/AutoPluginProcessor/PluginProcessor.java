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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
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
        set.add(MCListener.class.getCanonicalName());

        return set;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public void setup() {
        try {
            try {
                FileObject fileObject = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
                Reader reader = fileObject.openReader(true);
                yamlContent = yaml.load(reader);
                reader.close();
                fileObject.delete();
            } catch (FileNotFoundException ignored) {}

            outputFile = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
        } catch (IOException e) {
            throw new ProcessingException("Failed to create resource %s: %s", fileName, e.getMessage());
        }

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("d");
        return super.process(annotations, roundEnv);
    }

    @Override
    public boolean safeProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<Element> list = new ArrayList<>();
            list.add(elementUtils.getTypeElement("de.derteufelqwe.AutoPluginTest.Listener2"));
        Parser.Data data = new Parser.Data(roundEnv, messager, typeUtils, list);

        MCPluginParser mcPluginParser = new MCPluginParser(data, yaml);
        mcPluginParser.addContent(yamlContent);

        MCAuthorParser mcAuthorParser = new MCAuthorParser(data);
        mcAuthorParser.addContent(yamlContent);

        MCAPIVersionParser mcapiVersionParser = new MCAPIVersionParser(data);
        mcapiVersionParser.addContent(yamlContent);

        MCDependParser mcDependParser = new MCDependParser(data);
        mcDependParser.addContent(yamlContent);

        MCSoftDependParser mcSoftDependParser = new MCSoftDependParser(data);
        mcSoftDependParser.addContent(yamlContent);

        MCLoadParser mcLoadParser = new MCLoadParser(data);
        mcLoadParser.addContent(yamlContent);

        MCLoadBeforeParser mcLoadBeforeParser = new MCLoadBeforeParser(data);
        mcLoadBeforeParser.addContent(yamlContent);

        MCCommandParser mcCommandParser = new MCCommandParser(data);
        mcCommandParser.addContent(yamlContent);

        AutoRegisterGenerator cmdGen = new AutoRegisterGenerator(data, filer, annotations.size());
        cmdGen.generateClass();

        return false;
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
