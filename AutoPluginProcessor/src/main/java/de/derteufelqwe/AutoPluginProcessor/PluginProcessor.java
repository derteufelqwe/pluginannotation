package de.derteufelqwe.AutoPluginProcessor;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.tools.javac.processing.JavacFiler;
import de.derteufelqwe.AutoPluginProcessor.annotations.*;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.misc.AutoRegisterGenerator;
import de.derteufelqwe.AutoPluginProcessor.misc.Config;
import de.derteufelqwe.AutoPluginProcessor.parsers.*;
import de.derteufelqwe.AutoPluginProcessor.processors.BetterProcessor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;


@AutoService(Processor.class)
public class PluginProcessor extends BetterProcessor {

    private JavaFileManager.Location pluginLocation = StandardLocation.CLASS_OUTPUT;
    private Map<String, Object> yamlContent = new HashMap<>();
    private Yaml yaml = getYAML();
    private Gson gson = getGson();
    private Set<String> annotatedFiles = new HashSet<>();
    private Set<Class<? extends Annotation>> supportedAnnotations = getSupportedAnnotationClasses();


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return getSupportedAnnotationClasses().stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        Set<Class<? extends Annotation>> set = new LinkedHashSet<>();
        set.add(MCPlugin.class);
        set.add(MCAuthor.class);
        set.add(MCAPIVersion.class);
        set.add(MCDepend.class);
        set.add(MCLoad.class);
        set.add(MCLoadBefore.class);
        set.add(MCSoftDepend.class);
        set.add(MCPermission.class);

        set.add(MCCommand.class);
        set.add(MCTabComplete.class);
        set.add(MCListener.class);

        return set;
    }


    @Override
    public void setup() {
        note("NOTE");
        warning("XWARNING");
        try {
            try {
                FileObject file = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", Config.CACHE_FILE_NAME);
                Reader reader = file.openReader(true);
                annotatedFiles = (Set<String>) gson.fromJson(reader, Set.class);
                reader.close();
                ((JavacFiler) filer).close();
            } catch (FileNotFoundException ignored) {
            }

        } catch (IOException e) {
            throw new ProcessingException("Failed to create resource %s: %s", Config.CONFIG_FILE_NAME, e.getMessage());
        }

        getAndProcessCachedFiles(new HashSet<>());
    }

    /**
     * Parse the new elements together with the already existing elements to create a Set of existing
     * and properly annotated classes.
     *
     * @param elements Elements of this round
     * @return Set of elements which exist and have atleast one annotation from 'supportedAnnotations'
     */
    private Set<Element> getAndProcessCachedFiles(Set<? extends Element> elements) {
        Set<Element> resSet = new HashSet<>();
        resSet.addAll(elements);
        resSet.addAll(annotatedFiles.stream()
                .map(f -> elementUtils.getTypeElement(f))
                .collect(Collectors.toSet()));

        resSet = resSet.stream().filter(Objects::nonNull).collect(Collectors.toSet());

        for (Element e : new HashSet<>(resSet)) {
            boolean keep = false;
            for (Class<? extends Annotation> c : supportedAnnotations) {
                if (e.getAnnotation(c) != null) {
                    keep = true;
                }
            }

            if (!keep) {
                resSet.remove(e);
            }
        }

        annotatedFiles = resSet.stream().map(Element::toString).collect(Collectors.toSet());

        return resSet;
    }


    @Override
    public boolean safeProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<Element> cachedFilesSet = getAndProcessCachedFiles(roundEnv.getRootElements());
        Parser.Data data = new Parser.Data(roundEnv, messager, typeUtils, cachedFilesSet);

        MCPluginParser mcPluginParser = new MCPluginParser(data, filer, yaml);
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
        try {
            FileObject outputFile = filer.createResource(pluginLocation, "", Config.CONFIG_FILE_NAME);
            Writer writer = outputFile.openWriter();
            yaml.dump(yamlContent, writer);
            writer.close();
        } catch (IOException e) {
            throw new ProcessingException("Failed to write content to file '%s': %s", Config.CONFIG_FILE_NAME, e.getMessage());
        }

        try {
            FileObject file = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", Config.CACHE_FILE_NAME);
            Writer writer = file.openWriter();
            gson.toJson(annotatedFiles, writer);
            writer.close();
        } catch (IOException e) {
            throw new ProcessingException("Failed to save cache. " + e.getMessage());
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

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting();

        return builder.create();
    }

}
