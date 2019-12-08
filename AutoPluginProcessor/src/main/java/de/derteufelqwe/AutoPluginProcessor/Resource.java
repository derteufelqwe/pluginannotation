package de.derteufelqwe.AutoPluginProcessor;

import com.sun.tools.javac.processing.JavacFiler;

import javax.lang.model.SourceVersion;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;

public class Resource {

    private JavacFiler filer;
    private String fileName;
    private InMemoryFile file = new InMemoryFile();


    public Resource(JavacFiler filer, String fileName) {
        this.filer = filer;
        this.fileName = fileName;
    }


    public boolean load() {
        try {
            FileObject fo = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
            file.write(readFileObject(fo));
            filer.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean save() {
        try {
            FileObject fo = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
            writeFileObject(fo, file.readAsString());
            filer.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void closeAllFiles() {
        filer.close();
    }

    public InMemoryFile getFile() {
        return this.file;
    }


    // Private

    private String readFileObject(FileObject fo) {
        StringBuffer sb = new StringBuffer();
        String str;

        try {
            Reader reader = fo.openReader(true);
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private void writeFileObject(FileObject jfo, String msg) {
        try {
            Writer writer = jfo.openWriter();
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(msg);

            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
