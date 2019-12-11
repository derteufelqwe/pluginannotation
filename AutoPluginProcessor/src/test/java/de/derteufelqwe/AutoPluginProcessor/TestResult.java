package de.derteufelqwe.AutoPluginProcessor;

import com.google.testing.compile.Compilation;

import javax.tools.JavaFileObject;
import java.util.List;

public class TestResult {

    private Compilation compilation;
    private List<JavaFileObject> allFiles;
    private List<JavaFileObject> sourceFiles;
    private List<JavaFileObject> otherFiles;
    private boolean result;

    public TestResult(Compilation compilation, List<JavaFileObject> allFiles, List<JavaFileObject> sourceFiles, List<JavaFileObject> otherFiles, boolean result) {
        this.compilation = compilation;
        this.allFiles = allFiles;
        this.sourceFiles = sourceFiles;
        this.otherFiles = otherFiles;
        this.result = result;
    }

    public List<JavaFileObject> getAllFiles() {
        return allFiles;
    }

    public List<JavaFileObject> getSourceFiles() {
        return sourceFiles;
    }

    public List<JavaFileObject> getOtherFiles() {
        return otherFiles;
    }

    public boolean isResult() {
        return result;
    }

    public Compilation getCompilation() {
        return compilation;
    }
}
