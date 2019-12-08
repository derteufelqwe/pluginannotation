//package de.derteufelqwe.AutoPluginProcessor.processors;
//
//import de.derteufelqwe.AutoPluginProcessor.JFiles;
//import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
//import org.yaml.snakeyaml.DumperOptions;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.IOException;
//import java.io.Writer;
//import java.util.HashMap;
//import java.util.Map;
//
//public abstract class YamlProcessor extends FileProcessor {
//
//
//    protected Yaml yaml = getYAML();
//    private Map<String, Map<String, Object>> yamlContents = new HashMap<>();
//
//
//    private Yaml getYAML() {
//        DumperOptions options = new DumperOptions();
//            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//            options.setIndent(4);
//            options.setPrettyFlow(true);
//
//        return new Yaml(options);
//    }
//
//
//    public Map<String, Object> getYAMLContent(String fileName) {
//        if (!yamlContents.containsKey(fileName))
//            throw new ProcessingException(String.format("Content for config '%s' doesn't exist. Use loadYAMLFile() first.", fileName));
//
//        return yamlContents.get(fileName);
//    }
//
//
//    public Map<String, Object> setupCleanYAMLFile(String fileName) {
//        if (yamlContents.containsKey(fileName))
//            throw new ProcessingException(String.format("Trying to create file '%s', which already exists %s.", fileName));
//
//        this.yamlContents.put(fileName, new HashMap<>());
//
//        return this.yamlContents.get(fileName);
//    }
//
//
//    public Map<String, Object> loadYAMLFile(String fileName) {
//        JFiles.NewFile file = getFile(fileName);
//
//        Map<String, Object> contents = yaml.load(file.getContent());
//        if (contents == null)
//            contents = new HashMap<>();
//
//        yamlContents.put(fileName, contents);
//
//        return contents;
//    }
//
//    public boolean closeYAMLFile(String fileName) {
//        if (!yamlContents.containsKey(fileName))
//            throw new ProcessingException(String.format("YAMLConfig '%s', doesn't exist. Can't save it.", fileName));
//
//        JFiles.NewFile file = getFile(fileName);
//
//        try {
//            Writer writer = file.getFile().openWriter();
//            yaml.dump(yamlContents.get(fileName), writer);
//            writer.close();
//        } catch (IOException e) {
//            throw new ProcessingException(String.format("IOException while saving YAMLFile '%s': %s", fileName, e.getMessage()));
//        }
//
//
//        return true;
//    }
//
//
//}
