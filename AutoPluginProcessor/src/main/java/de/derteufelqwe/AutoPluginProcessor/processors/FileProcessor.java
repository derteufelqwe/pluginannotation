package de.derteufelqwe.AutoPluginProcessor.processors;

import com.sun.tools.internal.ws.processor.ProcessorException;
import com.sun.tools.javac.processing.JavacFiler;
import de.derteufelqwe.AutoPluginProcessor.JFiles;
import de.derteufelqwe.AutoPluginProcessor.Resource;
import de.derteufelqwe.AutoPluginProcessor.exceptions.ProcessingException;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;


public abstract class FileProcessor extends BetterProcessor {

    private Map<String, Resource> files = new HashMap<>();


    public Resource createFile(String fileName) {
        if (files.containsKey(fileName))
            throw new ProcessingException("File '%s' already exists.", fileName);

        files.put(fileName, new Resource((JavacFiler) filer, fileName));

        return files.get(fileName);
    }


    public Resource getFile(String fileName) {
        if (!files.containsKey(fileName))
            createFile(fileName);

        return files.get(fileName);
    }

    public void deleteFile(String fileName) {
        if (!files.containsKey(fileName))
            throw new ProcessingException("File '%s' doesn't exist.", fileName);

        files.remove(fileName);
    }

    public boolean exists(String fileName) {
        return files.containsKey(fileName);
    }

    @Override
    public synchronized void finish() {
        for (Resource resource : files.values()) {
            resource.load();
            resource.save();
        }
    }
}
