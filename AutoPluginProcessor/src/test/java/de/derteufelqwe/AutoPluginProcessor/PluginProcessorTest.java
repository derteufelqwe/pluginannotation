package de.derteufelqwe.AutoPluginProcessor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import de.derteufelqwe.AutoPluginProcessor.Plugins.Plugin1;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static com.google.common.truth.Truth.*;
import static com.google.testing.compile.CompilationSubject.compilations;

// https://github.com/guhilling/java-metamodel-generator/blob/master/processor/src/test/java/de/hilling/lang/metamodel/MetamodelGeneratorTest.java

public class PluginProcessorTest {

    private Compiler compiler;
    private Yaml yaml = getYaml();

    @Before
    public void setup() {
        compiler = Compiler.javac().withProcessors(new PluginProcessor());
    }

    /**
     * Tests following classes:
     *  - MCPlugin - without description
     */
    @Test
    public void testPlugin() {
        Compilation compilation = compiler.compile(TestUtils.source(Plugin1.class));

        List<JavaFileObject> sourceFiles = getSourceFiles(compilation);
        List<JavaFileObject> otherFiles = getOtherFiles(compilation);

        assertEquals(sourceFiles.size(), 1);
        assertEquals(otherFiles.size(), 1);
        assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        Map<String, Object> ymlContent = fileToYaml(otherFiles.get(0));

        Map<String, Object> referenceContent = resourceToYaml("testfiles/Plugin1.yml");


        return;
    }

    /**
     *  - MCPlugin - with description
     *  - MCAPIVersion
     *  - MCAuthor
     *  - Website
     */
    @Test
    public void testMCAPIVersion() {

    }

    @Test
    public void printCompileOutput() {
        Compilation compilation = compiler.compile(TestUtils.source(Plugin1.class));

        List<JavaFileObject> files = compilation.generatedFiles().stream().filter(f -> f.getKind() != JavaFileObject.Kind.CLASS)
                .collect(Collectors.toList());

        List<JavaFileObject> sourceFiles = files.stream().filter(f -> f.getKind() == JavaFileObject.Kind.SOURCE)
                .collect(Collectors.toList());

        List<JavaFileObject> otherFiles = files.stream().filter(f -> f.getKind() == JavaFileObject.Kind.OTHER)
                .collect(Collectors.toList());


        assertEquals(sourceFiles.size(), 1);
        assertEquals(otherFiles.size(), 1);

        System.out.println(TestUtils.JFOToString(otherFiles.get(0)));
    }


    // ----------  Non-Tests  ----------

    private List<JavaFileObject> getFileBase(Compilation compilation, JavaFileObject.Kind kind) {
        return compilation.generatedFiles().stream().filter(f -> f.getKind() == kind)
                .collect(Collectors.toList());
    }

    private List<JavaFileObject> getNonClassFiles(Compilation compilation) {
        return compilation.generatedFiles().stream().filter(f -> f.getKind() != JavaFileObject.Kind.CLASS)
                .collect(Collectors.toList());
    }

    private List<JavaFileObject> getSourceFiles(Compilation compilation) {
        return getFileBase(compilation, JavaFileObject.Kind.SOURCE);
    }

    private List<JavaFileObject> getOtherFiles(Compilation compilation) {
        return getFileBase(compilation, JavaFileObject.Kind.OTHER);
    }


    private Yaml getYaml() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);

        return new Yaml(options);
    }

    private Map<String, Object> fileToYaml(JavaFileObject jfo) {
        try {
            return yaml.load(jfo.openReader(true));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> resourceToYaml(String fileName) {
        File file = new File("src/test/resources/" + fileName);

        if (!file.exists())
            return null;

        try {
            return yaml.load(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}