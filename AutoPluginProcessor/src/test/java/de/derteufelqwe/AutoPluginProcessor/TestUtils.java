package de.derteufelqwe.AutoPluginProcessor;

import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;

public class TestUtils {

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
    public static String JFOToString(JavaFileObject jfo) {
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

}
