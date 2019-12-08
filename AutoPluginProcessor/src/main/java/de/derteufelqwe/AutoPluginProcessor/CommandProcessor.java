//package de.derteufelqwe.AutoPluginProcessor;
//
//import com.google.auto.service.AutoService;
//import com.sun.tools.javac.processing.JavacFiler;
//import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
//import de.derteufelqwe.AutoPluginProcessor.parsers.MCCommandParser;
//import de.derteufelqwe.AutoPluginProcessor.processors.BetterProcessor;
//
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.lang.model.element.TypeElement;
//import java.util.LinkedHashSet;
//import java.util.Set;
//
//
//@AutoService(Processor.class)
//public class CommandProcessor extends BetterProcessor {
//
//    private String fileName = Config.CONFIG_FILE_NAME;
//
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> set = new LinkedHashSet<>();
////        set.add(MCCommand.class.getCanonicalName());
//
//        return set;
//    }
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//    }
//
//    @Override
//    public void setup() {
////        loadYAMLFile(fileName);
//    }
//
//    @Override
//    public boolean safeProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//
//        MCCommandParser mcCommandParser = new MCCommandParser(roundEnv, messager);
////        getYAMLContent(fileName).putAll(mcCommandParser.parse());
//
//        return false;
//    }
//
//    @Override
//    public synchronized void finish() {
////        closeYAMLFile(fileName);
//        ((JavacFiler) filer).close();
//    }
//}
