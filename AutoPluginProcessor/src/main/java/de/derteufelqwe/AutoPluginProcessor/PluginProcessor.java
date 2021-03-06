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


    private Yaml yaml = getYAML();
    private Gson gson = getGson();

    private Map<String, Object> yamlContent = new HashMap<>();
    private Set<String> annotatedFiles = new HashSet<>();
    private Set<Class<? extends Annotation>> supportedAnnotations = getSupportedAnnotationClasses();


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = getSupportedAnnotationClasses().stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());

        // Added here, since it's not really a valid annotation but only used to notify the AP about a class
        set.add(MCDontIgnore.class.getCanonicalName());

        return set;
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


    /**
     * Load the cache if possible
     */
    @Override
    public void setup() {

        try {
            FileObject file = filer.getResource(Config.CACHE_LOCATION, "", Config.CACHE_FILE_NAME);
            Reader reader = file.openReader(true);
            annotatedFiles = (Set<String>) gson.fromJson(reader, Set.class);
            reader.close();
            ((JavacFiler) filer).close();
        } catch (IOException ignored) {}

        getAndProcessCachedFiles(new HashSet<>());
    }

    /**
     * Parse the new elements together with the already existing elements to create a Set of existing
     * and properly annotated classes. 'annotatedFiles' will be updated.
     *
     * @param elements Elements of this round
     * @return Set of elements which exist and have at least one annotation from 'supportedAnnotations'
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


    /**
     * Saves the cache and the plugin.yml
     */
    @Override
    public synchronized void finish() {
        try {
            FileObject outputFile = filer.createResource(Config.CONFIG_OUT_LOCATION, "", Config.CONFIG_FILE_OUT_NAME);
            Writer writer = outputFile.openWriter();
            yaml.dump(yamlContent, writer);
            writer.close();
        } catch (IOException e) {
            throw new ProcessingException("Failed to save plugin.yml. %s", e.getMessage());
        }

        try {
            FileObject file = filer.createResource(Config.CACHE_LOCATION, "", Config.CACHE_FILE_NAME);
            Writer writer = file.openWriter();
            gson.toJson(annotatedFiles, writer);
            writer.close();
        } catch (IOException e) {
            throw new ProcessingException("Failed to save cache. %s", e.getMessage());
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
