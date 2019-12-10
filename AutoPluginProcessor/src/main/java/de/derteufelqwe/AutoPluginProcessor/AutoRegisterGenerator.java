package de.derteufelqwe.AutoPluginProcessor;

import com.squareup.javapoet.*;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.parsers.MCCommandParser;
import de.derteufelqwe.AutoPluginProcessor.parsers.MCListenerParser;
import de.derteufelqwe.AutoPluginProcessor.parsers.MCTabCompletParser;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AutoRegisterGenerator {

    private final String CLASS_NAME = "AutoRegister";
    private final String GENERATE_METHOD_NAME = "register";

    private MCCommandParser mcCommandParser;
    private MCTabCompletParser mcTabCompletParser;
    private MCListenerParser mcListenerParser;

    private Messager messager;
    private Filer filer;
    private RoundEnvironment roundEnv;
    private int annotationSize = 0;


    public AutoRegisterGenerator(RoundEnvironment roundEnv, Messager messager, Types typeUtils, Filer filer, int annotationSize) {
        this.mcCommandParser = new MCCommandParser(roundEnv, messager, typeUtils);
        this.mcTabCompletParser = new MCTabCompletParser(roundEnv, messager, typeUtils);
        this.mcListenerParser = new MCListenerParser(roundEnv, messager, typeUtils);

        this.roundEnv = roundEnv;
        this.messager = messager;
        this.filer = filer;
        this.annotationSize = annotationSize;
    }


    private Map<String, Pair<ClassName, ClassName>> getParsedCommandMap() {
        Map<String, Pair<ClassName, ClassName>> resMap = new HashMap<>();
        Map<String, String> cmdMap = this.mcCommandParser.getCommandMap();
        Map<String, String> completerMap = this.mcTabCompletParser.getCompleterMap();

        for (String key : cmdMap.keySet()) {
            Pair<String, String> clazz1 = splitClass(cmdMap.get(key));
            Pair<String, String> clazz2 = splitClass(completerMap.get(key));

            resMap.put(key, new Pair<>(ClassName.get(clazz1.first, clazz1.second),
                    clazz2 == null ? null : ClassName.get(clazz2.first, clazz2.second)));
        }

        return resMap;
    }


    private List<ClassName> getParsedListeners() {
        List<ClassName> resList = new ArrayList<>();
        List<String> rawListeners = this.mcListenerParser.getListeners();

        for (String entry : rawListeners) {
            Pair<String, String> splitClass = splitClass(entry);
            resList.add(ClassName.get(splitClass.first, splitClass.second));
        }

        return resList;
    }


    public void generateClass() {
        // Used to skip all rounds where nothing needs to be generated.
        if (annotationSize == 0)
            return;

        Map<String, Pair<ClassName, ClassName>> commands = getParsedCommandMap();
        List<ClassName> listeners = getParsedListeners();

        // Method setup
        TypeSpec.Builder cmdGenClass = TypeSpec
                .classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder genMethodBuilder = MethodSpec
                .methodBuilder(GENERATE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(JavaPlugin.class, "plugin");


        // Add the Commands with their TabCompleters
        genMethodBuilder.addComment("Commands and TabCompleters");

        for (String key : commands.keySet()) {
            ClassName executor = commands.get(key).first;
            ClassName completer = commands.get(key).second;

            // Create variable if Executor and Completer are the same class
            if (completer != null && executor.equals(completer)) {
                String variableName = executor.packageName().replace('.', '_') + "_" + executor.simpleName();
                genMethodBuilder.addStatement("$T $N = new $T()", executor, variableName, executor);

                genMethodBuilder.addStatement("plugin.getServer().getPluginCommand($S).setExecutor($N)", key, variableName);
                genMethodBuilder.addStatement("plugin.getServer().getPluginCommand($S).setTabCompleter($N)", key, variableName);

            } else if (completer != null) {
                genMethodBuilder.addStatement("plugin.getServer().getPluginCommand($S).setExecutor(new $T())", key, executor);
                genMethodBuilder.addStatement("plugin.getServer().getPluginCommand($S).setTabCompleter(new $T())", key, completer);

            } else {
                genMethodBuilder.addStatement("plugin.getServer().getPluginCommand($S).setExecutor(new $T())", key, executor);
            }

        }


        // Register the listeners
        genMethodBuilder.addComment("Listeners");

        for (ClassName listener : listeners) {
            genMethodBuilder.addStatement("plugin.getServer().getPluginManager().registerEvents(new $T(), plugin)", listener);
        }

        // Save to File
        cmdGenClass.addMethod(genMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder("de.derteufelqwe.AutoPlugin", cmdGenClass.build()).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new ProcessingException("Failed to generate %s class: %s", CLASS_NAME, e.getMessage());
        }

        this.messager.printMessage(Diagnostic.Kind.NOTE, String.format("AutoPlugin registered %s commands.", commands.size()));
    }


    @Nullable
    private Pair<String, String> splitClass(String className) {
        String pack = "";
        String clazz = "";

        if (className == null)
            return null;

        int index = className.lastIndexOf('.');

        pack = className.substring(0, index);
        clazz = className.substring(1 + index);

        return new Pair<>(pack, clazz);
    }


}
