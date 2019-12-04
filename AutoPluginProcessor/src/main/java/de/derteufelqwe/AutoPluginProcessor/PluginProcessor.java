package de.derteufelqwe.AutoPluginProcessor;

import com.google.auto.service.AutoService;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
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


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(MCPlugin.class.getCanonicalName());

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
            for (Element element : roundEnv.getElementsAnnotatedWith(MCPlugin.class)) {
                note(element, "Found Minecraft Plugin " + element.toString());
                generateBasePluginYML(element);
            }
        } catch (Exception e) {
            error(null, e.getMessage());
        }

        return false;
    }


    private void generateBasePluginYML(Element element) throws IOException {
        MCPlugin annotation = element.getAnnotation(MCPlugin.class);
        pluginYMLResource = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", CONFIG_FILE_NAME);
        Writer writer = pluginYMLResource.openWriter();

        File resourceFile = getResourceFile(element, annotation.srcPath(), annotation.resourcePath(), CONFIG_FILE_NAME);
        DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        Map<String, Object> cfgMap = new HashMap<>();

        if (resourceFile.exists()) {
            Reader fr = new FileReader(resourceFile);
            cfgMap = yaml.load(fr);
            fr.close();
        }

        cfgMap.put("authors", new ArrayList<String>());

        yaml.dump(cfgMap, writer);

        writer.close();
    }

    private File getResourceFile(Element element, String srcPath, String resourcePath, String relativePath) {
        String fullPath = ((com.sun.tools.javac.code.Symbol.ClassSymbol) element).sourcefile.getName();
        int pos = fullPath.replace('\\', '/').indexOf(srcPath);
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
