package de.derteufelqwe.AutoPluginProcessor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import de.derteufelqwe.AutoPluginProcessor.testfiles.TestPlugin;
import org.junit.Before;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PluginProcessorTest {

    Compiler compiler;

    @Before
    public void setup() {
        compiler = Compiler.javac().withProcessors(new PluginProcessor());
    }

    @Test
    public void printCompileOutput() {
        Compilation compilation = compiler.compile(TestUtils.source(TestPlugin.class));

        List<JavaFileObject> files = compilation.generatedFiles().stream().filter(f -> f.getKind() != JavaFileObject.Kind.CLASS)
                .collect(Collectors.toList());

        List<JavaFileObject> sourceFiles = files.stream().filter(f -> f.getKind() == JavaFileObject.Kind.SOURCE)
                .collect(Collectors.toList());

        List<JavaFileObject> otherFiles = files.stream().filter(f -> f.getKind() == JavaFileObject.Kind.OTHER)
                .collect(Collectors.toList());


        assertEquals(sourceFiles.size(), 0);
        assertEquals(otherFiles.size(), 1);

        System.out.println(TestUtils.JFOToString(otherFiles.get(0)));
    }

}