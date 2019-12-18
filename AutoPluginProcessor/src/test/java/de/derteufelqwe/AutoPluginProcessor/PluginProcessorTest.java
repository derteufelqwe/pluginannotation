package de.derteufelqwe.AutoPluginProcessor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// https://github.com/guhilling/java-metamodel-generator/blob/master/processor/src/test/java/de/hilling/lang/metamodel/MetamodelGeneratorTest.java
// https://github.com/google/compile-testing/blob/master/src/test/java/com/google/testing/compile/JavaSourcesSubjectFactoryTest.java#L100

public class PluginProcessorTest {

    private final String TEST_SOURCES_FOLDER = "testsources/";
    private final String TEST_FILES_FOLDER = "testfiles/";
    private final String AUTOREGISTER_PACKAGE = "de.derteufelqwe.AutoPlugin";
    private final String AUTOREGISTER_FILE = "AutoRegister.java";

    private Compiler compiler;
    private Yaml yaml = getYaml();

    @Before
    public void setup() {
        compiler = Compiler.javac().withProcessors(new PluginProcessor());
    }


    // ----------  Everything around MCPlugin

    /**
     * Tests the following:
     *  - MCPlugin - working, minimal
     */
    @Test
    public void testPluginWorking1() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "/Plugin/", "Plugin1");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .succeededWithoutWarnings();

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "plugin.yml")
                .contentsAsUtf8String().isEqualTo(resourceToString(TEST_FILES_FOLDER + "Plugin/Plugin1.yml"));
    }

    /**
     * Tests the following:
     *  - MCPlugin - working, maximal
     *  - MCAPIVersion
     *  - MCAuthor
     *  - MCDepend
     *  - MCLoad
     *  - MCLoadBefore
     *  - MCSoftDepend
     */
    @Test
    public void testPluginWorking2() {
        TestResult result = basicTest("Plugin/Plugin2.yml", "Plugin/", "Plugin2");

        assertAbout(CompilationSubject.compilations()).that(result.getCompilation())
                .succeededWithoutWarnings();

        assertTrue("Generated plugin.yml is false.", result.isResult());

    }

    /**
     * Tests the following:
     *  - MCPlugin - not extending JavaPlugin
     */
    @Test
    public void testPluginNotWorking1() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin3");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCPlugin annotated classes need to extend JavaPlugin");
    }

    /**
     * Tests the following:
     *  - MCPlugin - wrong Plugin name
     */
    @Test
    public void testPluginNotWorking2() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin4");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("does not match name-criteria.");
    }

    /**
     * Tests the following:
     *  - MCAPIVersion - Not on MCPlugin
     */
    @Test
    public void testMCAPIVersionNotWorking() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin5");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCAPIVersion can only be applied to classes");
    }

    /**
     * Tests the following:
     *  - MCAuthor - Not on MCPlugin
     */
    @Test
    public void testMCAuthorNotWorking() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin6");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCAuthor can only be applied to classes");
    }

    /**
     * Tests the following:
     *  - MCDepend - Not on MCPlugin
     */
    @Test
    public void testMCDependNotWorking() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin7");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCDepend can only be applied to classes");
    }

    /**
     * Tests the following:
     *  - MCDepend - Dependency name wrong
     */
    @Test
    public void testMCDependNotWorking2() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin8");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("Depend has faulty dependency name");
    }

    /**
     * Tests the following:
     *  - MCLoad - Not on MCPlugin
     */
    @Test
    public void testMCLoadNotWorking() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin9");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCLoad can only be applied to classes");
    }

    /**
     * Tests the following:
     *  - MCLoadBefore - Not on MCPlugin
     */
    @Test
    public void testMCLoadBeforeNotWorking1() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin10");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCLoadBefore can only be applied to classes");
    }

    /**
     * Tests the following:
     *  - MCLoadBefore - Wrong dependency name
     */
    @Test
    public void testMCLoadBeforeNotWorking2() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin11");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("LoadBefore has faulty dependency name");
    }

    /**
     * Tests the following:
     *  - MCSoftDepend - Not on MCPlugin
     */
    @Test
    public void testMCSoftDependNotWorking1() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin12");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCSoftDepend can only be applied to classes");
    }

    /**
     * Tests the following:
     *  - MCSoftDepend - Wrong dependency name
     */
    @Test
    public void testMCSoftDependNotWorking2() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Plugin/", "Plugin13");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("SoftDepend has faulty dependency name");
    }

    // ----------  Commands and TabCompleters  ----------

    /**
     * Tests the following:
     *  - MCCommand - working, minimal
     */
    @Test
    public void testMCCommandWorking1() {
        TestResult result = basicTest("Command/Command1.yml", "Command/", "Command1");

        assertAbout(CompilationSubject.compilations()).that(result.getCompilation())
                .succeededWithoutWarnings();

        assertTrue("Generated plugin.yml is incorrect.", result.isResult());

        assertAbout(CompilationSubject.compilations()).that(result.getCompilation())
                .generatedFile(StandardLocation.SOURCE_OUTPUT, AUTOREGISTER_PACKAGE, AUTOREGISTER_FILE)
                .contentsAsUtf8String().isEqualTo(resourceToString(TEST_FILES_FOLDER + "Command/AutoRegister1.java"));
    }

    /**
     * Tests the following:
     *  - MCCommand - working, full
     */
    @Test
    public void testMCCommandWorking2() {
        TestResult result = basicTest("Command/Command2.yml", "Command/", "Command2");

        assertAbout(CompilationSubject.compilations()).that(result.getCompilation())
                .succeededWithoutWarnings();

        assertTrue("Generated plugin.yml is incorrect.", result.isResult());

        assertAbout(CompilationSubject.compilations()).that(result.getCompilation())
                .generatedFile(StandardLocation.SOURCE_OUTPUT, AUTOREGISTER_PACKAGE, AUTOREGISTER_FILE)
                .contentsAsUtf8String().isEqualTo(resourceToString(TEST_FILES_FOLDER + "Command/AutoRegister2.java"));
    }

    /**
     * Tests the following:
     *  - MCCommand - Not on CommandExecutor
     */
    @Test
    public void testMCCommandNotWorking1() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Command/", "Command3");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCCommand annotated classes need to implement CommandExecutor.");
    }

    /**
     * Tests the following:
     *  - MCCommand - Faulty command in annotation
     */
    @Test
    public void testMCCommandNotWorking2() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Command/", "Command4");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("contains invalid characters.");
    }

    /**
     * Tests the following:
     *  - MCTabComplete - working
     */
    @Test
    public void testMCTabCompleteWorking1() {
        Compilation compilation = compiler.compile(resourceToJFO(TEST_SOURCES_FOLDER + "Command/", "TabComplete1"));

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .succeededWithoutWarnings();

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .generatedFile(StandardLocation.SOURCE_OUTPUT, AUTOREGISTER_PACKAGE, AUTOREGISTER_FILE)
                .contentsAsUtf8String().isEqualTo(resourceToString(TEST_FILES_FOLDER + "Command/AutoRegister3.java"));
    }

    /**
     * Tests the following:
     *  - MCTabComplete - Not on TabCompleter
     */
    @Test
    public void testMCTabCompleteNotWorking1() {
        JavaFileObject file = resourceToJFO(TEST_SOURCES_FOLDER + "Command/", "TabComplete2");
        Compilation compilation = compiler.compile(file);

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCTabComplete annotated classes");
    }

    /**
     * Tests the following:
     *  - MCCommand - working
     *  - MCTabComplete - working
     */
    @Test
    public void testMCCommandAndMCTabCompleteWorking1() {
        TestResult result = basicTest("Command/Command5.yml", "Command/", "Command5");

        assertAbout(CompilationSubject.compilations()).that(result.getCompilation())
                .succeededWithoutWarnings();

        if (!result.isResult())
            System.out.println(result.getGeneratedFile());
        assertTrue("Generated plugin.yml is incorrect.", result.isResult());

        assertAbout(CompilationSubject.compilations()).that(result.getCompilation())
                .generatedFile(StandardLocation.SOURCE_OUTPUT, AUTOREGISTER_PACKAGE, AUTOREGISTER_FILE)
                .contentsAsUtf8String().isEqualTo(resourceToString(TEST_FILES_FOLDER + "Command/AutoRegister4.java"));
    }

    // ----------  Listeners  ----------

    /**
     * Tests the following:
     *  - MCListener - working
     */
    @Test
    public void testMCListenerWorking() {
        Compilation compilation = compiler.compile(resourceToJFO(TEST_SOURCES_FOLDER + "Listener/", "Listener1"));

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .succeededWithoutWarnings();

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .generatedFile(StandardLocation.SOURCE_OUTPUT, AUTOREGISTER_PACKAGE, AUTOREGISTER_FILE)
                .contentsAsUtf8String().isEqualTo(resourceToString(TEST_FILES_FOLDER + "Listener/AutoRegister1.java"));
    }

    /**
     * Tests the following:
     *  - MCListener - not on Listener
     */
    @Test
    public void testMCListenerNotWorking() {
        Compilation compilation = compiler.compile(resourceToJFO(TEST_SOURCES_FOLDER + "Listener/", "Listener2"));

        assertAbout(CompilationSubject.compilations()).that(compilation)
                .hadErrorContaining("@MCListener annotated classes");
    }


    // ----------  Non-tests  ----------

    private TestResult basicTest(String outputFileName, String resourcePath, String... fileName) {
        Compilation compilation = compiler.compile(Arrays.stream(fileName)
                .map(f -> resourceToJFO(TEST_SOURCES_FOLDER + resourcePath, f))
                .collect(Collectors.toList()));

        List<JavaFileObject> allFiles = getNonClassFiles(compilation);
        List<JavaFileObject> sourceFiles = getSourceFiles(compilation);
        List<JavaFileObject> otherFiles = getOtherFiles(compilation);
        List<JavaFileObject> otherClassFiles = new ArrayList<>(otherFiles).stream()
                .filter(f -> f.toUri().toString().startsWith("mem:///CLASS_OUTPUT"))
                .collect(Collectors.toList());

        Map<String, Object> ymlContent = fileToYaml(otherClassFiles.get(0));
        Map<String, Object> referenceContent = resourceToYaml(TEST_FILES_FOLDER + outputFileName);

        if (referenceContent == null)
            throw new TestException("Failed to find reference file '%s'.", TEST_FILES_FOLDER + outputFileName);

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
        if (fileName.endsWith(".java"))
            throw new RuntimeException("FileName can't end with .java");

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

    private String resourceToString(String fileName) {
        File file = new File("src/test/resources/" + fileName);
        StringBuilder builder = new StringBuilder();
        String str;

        if (!file.exists())
            return null;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str).append("\n");
            }
            bufferedReader.close();

            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}