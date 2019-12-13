package de.derteufelqwe.AutoPluginProcessor;

import com.google.testing.compile.JavaFileObjects;

import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

public class UUtils {

    /**
     * Ssssshhhht. Be quiet.
     *
     * @param clazz
     * @return
     */
    public static JavaFileObject source(Class<?> clazz) {
        try {
            String basePath = new File("").toURI().toURL().getPath();
            File source = new File(basePath + "src/main/java/" + clazz.getName().replace('.', '/') + ".java");

            if (!source.exists())
                source = new File(basePath + "src/test/java/" + clazz.getName().replace('.', '/') + ".java");

            return JavaFileObjects.forResource(source.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        return JavaFileObjects.forResource("src/main/java/" + clazz.getName().replace('.', '/') + ".java");
        return null;
    }

    /**
     * In-Memory JavaFileObject to String
     *
     * @param jfo
     * @return
     */
    public static String JFOToString(FileObject jfo) {
        StringBuffer sb = new StringBuffer();
        String str;

        try {
            Reader reader = jfo.openReader(true);
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static JavaFileObject resourceToJFO(String path, String fileName) {
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


}
