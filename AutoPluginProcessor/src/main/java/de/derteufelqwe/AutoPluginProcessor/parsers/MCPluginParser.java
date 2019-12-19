package de.derteufelqwe.AutoPluginProcessor.parsers;


import com.sun.tools.javac.processing.JavacFiler;
import de.derteufelqwe.AutoPluginProcessor.misc.Config;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ValidationException;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Parses all {@Link MCPlugin} Annotations
 */
public class MCPluginParser extends Parser {

    private final Pattern namePattern = Pattern.compile("[a-z,A-Z,0-9,_]+");
    private Filer filer;

    private Yaml yaml;

    public MCPluginParser(Parser.Data data, Filer filer, Yaml yaml) {
        super(data, MCPlugin.class);
        this.filer = filer;
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
            Map<String, Object> base = generateBasePluginYML();
            map.putAll(base);
        } catch (IOException e) {
            throw new ProcessingException("" +
                    "IOException while parsing %s. Exception: %s", element.toString(), e.getMessage());
        }

        if (!namePattern.matcher(annotation.name()).matches())
            throw new ValidationException(element, "PluginName '%s' does not match name-criteria. Source: %s",
                    annotation.name(), element.toString());

        map.put("name", annotation.name());
        map.put("version", annotation.version());
        map.put("main", element.toString());

        if (!annotation.description().equals(""))
            map.put("description", annotation.description());
        if (!annotation.prefix().equals(""))
            map.put("prefix", annotation.prefix());

        return map;
    }

    @Override
    protected boolean validate(Element element) throws ValidationException {
        return validator.onJavaPlugin(element);
    }


    private Map<String, Object> generateBasePluginYML() throws IOException {
        Map<String, Object> map = new HashMap<>();

        try {
            FileObject fileObject = filer.getResource(Config.CONFIG_LOCATION, "", Config.CONFIG_FILE_NAME);

            Reader reader = fileObject.openReader(true);
            map = (Map<String, Object>) yaml.loadAs(reader, Map.class);
            reader.close();
            ((JavacFiler) filer).close();

        } catch (IOException ignored) {}

        if (map == null)
            map = new HashMap<>();

        return map;
    }

}
