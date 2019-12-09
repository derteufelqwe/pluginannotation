package de.derteufelqwe.AutoPluginProcessor;

import com.squareup.javapoet.*;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;
import de.derteufelqwe.AutoPluginProcessor.parsers.MCCommandParser;
import de.derteufelqwe.AutoPluginProcessor.parsers.MCTabCompletParser;
import org.bukkit.Server;

import javax.annotation.Nullable;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CommandRegisterGenerator {

    private final String CLASS_NAME = "CommandGenerator";
    private final String GENERATE_METHOD_NAME = "generate";

    private MCCommandParser mcCommandParser;
    private MCTabCompletParser mcTabCompletParser;
    private Messager messager;
    private Filer filer;
    private RoundEnvironment roundEnv;
    private int annotationSize = 0;


    public CommandRegisterGenerator(RoundEnvironment roundEnv, Messager messager, Types typeUtils, Filer filer, int annotationSize) {
        this.messager = messager;
        this.filer = filer;
        this.roundEnv = roundEnv;
        this.annotationSize = annotationSize;
        this.mcCommandParser = new MCCommandParser(roundEnv, messager, typeUtils);
        this.mcTabCompletParser = new MCTabCompletParser(roundEnv, messager, typeUtils);
    }


    private Map<String, Pair<ClassName, ClassName>> getCommandMap() {
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


    public void generateClass() {
        // Used to skip all rounds where nothing needs to be generated.
        if (annotationSize == 0)
            return;

        Map<String, Pair<ClassName, ClassName>> commands = getCommandMap();

        TypeSpec.Builder cmdGenClass = TypeSpec
                .classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder genMethodBuilder = MethodSpec
                .methodBuilder(GENERATE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(Server.class, "server");

        for (String key : commands.keySet()) {
            ClassName executor = commands.get(key).first;
            ClassName completer = commands.get(key).second;

            genMethodBuilder.addStatement("server.getPluginCommand($S).setExecutor(new $T())", key, executor);
            if (completer != null)
                genMethodBuilder.addStatement("server.getPluginCommand($S).setTabCompleter(new $T())", key, completer);
        }

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
