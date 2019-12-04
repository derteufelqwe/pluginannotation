package de.derteufelqwe.AutoPluginProcessor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Symbol;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCAPIVersion;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCAuthor;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.plugin.java.JavaPlugin;
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

    private static final String CONFIG_FILE_NAME = "plugin.yml";

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
        Set<? extends Element> mcPluginElements = roundEnv.getElementsAnnotatedWith(MCPlugin.class);

        if (mcPluginElements.size() == 0)
            return true;
        else if (mcPluginElements.size() > 1)
            warning(null, "More than 1 Classes are annotated with @" + MCPlugin.class.getSimpleName() +
                    ". Remove one annotation.");

        Element mcPluginElement = mcPluginElements.iterator().next();

        Set<? extends Element> mcAPIVersionElements = roundEnv.getElementsAnnotatedWith(MCAPIVersion.class);
        Set<? extends Element> mcAuthorElements = roundEnv.getElementsAnnotatedWith(MCAuthor.class);

        try {
            setupConfig();

            parseMCPlugin(mcPluginElement);
            for (Element e : mcAPIVersionElements)
                parseMCAPIVersion(e);
            for (Element e : mcAuthorElements)
                parseMCAuthor(e);

            saveConfigToFile();
        } catch (ExitException e1) {
        } catch (ProcessingException e2) {
            error(null, "Exception: " + e2.getMessage());
        }catch (Exception e3) {
            error(null, "Unknown exception: " + e3.getMessage());
        }

        return true;
    }

    // ---------------  Annotation parsers  ---------------

    /**
     * Parses all {@Link MCPlugin} Annotations
     */
    private void parseMCPlugin(Element element) throws IOException {
        MCPlugin annotation = element.getAnnotation(MCPlugin.class);
        validateMCPlugin(element);

        note(element, "Found Minecraft Plugin " + element.toString());

        generateBasePluginYML(element);

        pluginConfigMap.put("name", annotation.pluginName());
        pluginConfigMap.put("version", annotation.version());
        pluginConfigMap.put("main", element.toString());
    }

    /**
     * Parses all {@Link MCAPIVersion} Annotations
     */
    private void parseMCAPIVersion(Element element) {
        MCAPIVersion annotation = element.getAnnotation(MCAPIVersion.class);
        validateMCAPIVersion(element);

        pluginConfigMap.put("api-version", annotation.value());
    }

    private void parseMCAuthor(Element element) {
        MCAuthor annotation = element.getAnnotation(MCAuthor.class);
        validateMCAuthor(element);

        pluginConfigMap.put("authors", Arrays.asList(annotation.value()));
    }

    // ---------------  General Validators  ---------------

    /**
     * Validates if the element, which is annotated by the class clazz, is a Minecraft Plugin main.
     * @param element Element to check.
     * @param clazz Class of annotation to check for. (Only for messages)
     */
    private void checkOnMCPlugin(Element element, Class clazz) {
        // Check if on Class
        if (element.getKind() != ElementKind.CLASS)
            throw new ProcessingException(element, String.format("@%s can only be applied to Classes. Source: %s",
                    clazz.getSimpleName(), element.toString()));
        TypeElement typeElement = (TypeElement) element;

        // Check if it inherits JavaPlugin
        // ToDo: Add support for non-direct inheritance
        if (!typeElement.getSuperclass().toString().equals(JavaPlugin.class.getName()))
            throw new ProcessingException(element, String.format("@%s annotated classes need to extend JavaPlugin. Source: %s",
                    clazz.getSimpleName(), element.toString()));
    }

    private void checkOnMCPluginAnnotation(Element element, Class clazz) {
        if (element.getAnnotation(MCPlugin.class) == null)
            throw new ProcessingException(element, String.format("@%s can only be applied to classes annotated with @%s. Source: %s",
                    clazz.getSimpleName(), MCPlugin.class.getSimpleName(), element.toString()));
    }

    // ---------------  Specific Validators  ---------------

    /**
     * Checks if an element fits the MCPlugin annotation
     */
    private void validateMCPlugin(Element element) {
        checkOnMCPlugin(element, MCPlugin.class);
    }

    /**
     * Checks if an element fits the MCAPIVersion annotation.
     */
    private void validateMCAPIVersion(Element element) {
        checkOnMCPlugin(element, MCAPIVersion.class);
        checkOnMCPluginAnnotation(element, MCAPIVersion.class);
    }

    /**
     * Checks if an element fits the MCAuthor annotation
     */
    private void validateMCAuthor(Element element) {
        checkOnMCPlugin(element, MCAuthor.class);
        checkOnMCPluginAnnotation(element, MCAuthor.class);
    }

    // ---------------  Setups  ---------------

    private void setupConfig() throws IOException {
        if (pluginYMLResource == null)
            pluginYMLResource = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", CONFIG_FILE_NAME);
    }


    private void saveConfigToFile() throws IOException {
        if (writer == null)
            writer = pluginYMLResource.openWriter();
        yaml.dump(pluginConfigMap, writer);
        writer.close();
    }


    private void generateBasePluginYML(Element element) throws IOException {
        MCPlugin annotation = element.getAnnotation(MCPlugin.class);
        File resourceFile = getResourceFile(element, annotation.srcPath(), annotation.resourcePath(), CONFIG_FILE_NAME);

        if (resourceFile.exists()) {
            Reader fr = new FileReader(resourceFile);
            pluginConfigMap = yaml.load(fr);
            fr.close();
        }

    }


    private File getResourceFile(Element element, String srcPath, String resourcePath, String relativePath) {
        String fullPath = ((Symbol.ClassSymbol) element).sourcefile.getName();
        int pos = fullPath.replace('\\', '/').indexOf(srcPath);
        if (pos == -1) {
            throw new ProcessingException(String.format("Can't parse annotation for %s. Did you forget to set srcPath or resourcePath?",
                    ((Symbol.ClassSymbol) element).className()));
        }

        String mainPath = fullPath.substring(0, pos);

        return new File(mainPath + resourcePath + relativePath);
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
