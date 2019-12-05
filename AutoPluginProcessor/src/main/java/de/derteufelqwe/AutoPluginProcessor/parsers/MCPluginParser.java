package de.derteufelqwe.AutoPluginProcessor.parsers;

import com.sun.tools.javac.code.Symbol;
import de.derteufelqwe.AutoPluginProcessor.Config;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Parses all {@Link MCPlugin} Annotations
 */
public class MCPluginParser extends Parser {

    private Yaml yaml;

    public MCPluginParser(RoundEnvironment roundEnv, Messager messager, Yaml yaml) {
        super(roundEnv, messager, MCPlugin.class);
        this.yaml = yaml;

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MCPlugin.class);

        if (elements.size() == 0) {
            return;
        } else if (elements.size() > 1) {
            warning(null, String.format("More than one classes are annotated with @%s. Remove one annotation.",
                    MCPlugin.class.getSimpleName()));
        }

    }


    @Override
    protected Map<String, Object> singleParse(Element element) throws ProcessingException {
        note(element, "Found Spigot Plugin " + element.toString());

        Map<String, Object> map = new HashMap<>();
        MCPlugin annotation = element.getAnnotation(MCPlugin.class);

        try {
            map.putAll(generateBasePluginYML(element));
        } catch (IOException e) {
            error(element, String.format("IOException while parsing %s. Exception: %s", element.toString(), e.getMessage()));
        }

        map.put("name", annotation.pluginName());
        map.put("version", annotation.version());
        map.put("main", element.toString());

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onJavaPlugin(element);
    }


    private Map<String, Object> generateBasePluginYML(Element element) throws IOException {
        MCPlugin annotation = element.getAnnotation(MCPlugin.class);
        Map<String, Object> map = new HashMap<>();
        File resourceFile = getResourceFile(element, annotation.srcPath(), annotation.resourcePath(), Config.CONFIG_FILE_NAME);

        if (resourceFile.exists()) {
            Reader fr = new FileReader(resourceFile);
            map = yaml.load(fr);
            fr.close();
        }

        return map;
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

}
