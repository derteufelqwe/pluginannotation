package de.derteufelqwe.AutoPluginProcessor;

import com.sun.tools.internal.ws.processor.ProcessorException;
import com.sun.tools.javac.processing.JavacFiler;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;

public class JFiles {

    public static class NewFile {

        private FileObject fileObject;
        private String content = "";

        public NewFile(FileObject fileObject, String content) {
            this.fileObject = fileObject;
            this.content = content;
        }

        public FileObject getFile() {
            return this.fileObject;
        }

        public String getContent() {
            return this.content;
        }

    }

    public static NewFile openJFO(JavacFiler filer, String fileName) throws ProcessorException {
        FileObject jfo = null;
        String oldContent = null;
        try {
            jfo = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
            oldContent = readJFO(jfo);
            jfo.delete();

            jfo = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", fileName);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ProcessingException(String.format("Can't open file %s: %s", fileName, e.getMessage()));
        }

        return new NewFile(jfo, oldContent);
    }

    public static String readJFO(FileObject jfo) {
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

    public static void writeJFO(FileObject jfo, String msg) {
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
