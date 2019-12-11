package de.derteufelqwe.AutoPluginProcessor;

import com.google.common.base.Utf8;
import com.google.testing.compile.*;
import com.google.testing.compile.Compiler;
import de.derteufelqwe.AutoPluginProcessor.Command.Command1;
import de.derteufelqwe.AutoPluginProcessor.Command.Command2;
import de.derteufelqwe.AutoPluginProcessor.Plugin.Plugin1;
import de.derteufelqwe.AutoPluginProcessor.Plugin.Plugin2;
import de.derteufelqwe.AutoPluginProcessor.Plugin.Plugin3;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static com.google.common.truth.Truth.*;

// https://github.com/guhilling/java-metamodel-generator/blob/master/processor/src/test/java/de/hilling/lang/metamodel/MetamodelGeneratorTest.java
// https://github.com/google/compile-testing/blob/master/src/test/java/com/google/testing/compile/JavaSourcesSubjectFactoryTest.java#L100

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
        TestResult testResult = basicTest("testfiles/Plugin1.yml", Plugin1.class);
        assertThat(testResult.isResult());
    }

    /**
     * Tests following classes:
     *  - MCPlugin - with description
     *  - MCAPIVersion
     *  - MCAuthor
     *  - Website
     */
    @Test
    public void testOnPlugin1() {
        TestResult testResult = basicTest("testfiles/Plugin2.yml", Plugin2.class);
        assertThat(testResult.isResult());
    }

    /**
     * Tests following classes:
     *  - MCPlugin
     *  - MCDepend
     *  - MCSoftDepend
     *  - MCLoad
     *  - MCLoadBefore
     */
    @Test
    public void testOnPlugin2() {
        TestResult testResult = basicTest("testfiles/Plugin3.yml", Plugin3.class);
        assertThat(testResult.isResult());
    }

    /**
     * Tests following classes:
     *  - MCCommand basic usage
     *  - MCCommand with optional parameter
     */
    @Test
    public void testCommand1() {
        TestResult testResult = basicTest("testfiles/Plugin3.yml", Command1.class, Command2.class);

    }

    @Test
    public void test() {
        JavaFileObject aa = resourceToJFO("testsources/", "Command1");

        Compilation compilation = compiler.compile(aa);

//        assertAbout(CompilationSubject.compilations()).that(compilation)
//                .hadErrorContaining("@MCPlugin")
//                ;

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .generatedFile(StandardLocation.SOURCE_OUTPUT, "plugin.yml")
                .contentsAsUtf8String().isEqualTo(yaml.dump(resourceToYaml("testfiles/Command1.yml")))
                ;

    }


    @Test
    public void printCompileOutput() {
        Compilation compilation = compiler.compile(TestUtils.source(Command1.class), TestUtils.source(Command2.class));

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

    private TestResult basicTest(String outputFileName, Class... input) {
        Compilation compilation = compiler.compile(Arrays.stream(input).map(TestUtils::source).collect(Collectors.toList()));

        List<JavaFileObject> allFiles = getNonClassFiles(compilation);
        List<JavaFileObject> sourceFiles = getSourceFiles(compilation);
        List<JavaFileObject> otherFiles = getOtherFiles(compilation);

        assertEquals(sourceFiles.size(), 1);
        assertEquals(otherFiles.size(), 1);
        assertThat(compilation.status()).isEqualTo(Compilation.Status.SUCCESS);

        Map<String, Object> ymlContent = fileToYaml(otherFiles.get(0));
        Map<String, Object> referenceContent = resourceToYaml(outputFileName);

        return new TestResult(compilation, allFiles, sourceFiles, otherFiles, ymlContent.equals(referenceContent));
    }


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

    private JavaFileObject resourceToJFO(String path, String fileName) {
        File file = new File("src/test/resources/" + path + fileName + ".java");

        if (!file.exists())
            return null;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());
            return JavaFileObjects.forSourceLines("test." + fileName, lines);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}