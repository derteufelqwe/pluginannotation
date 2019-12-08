package de.derteufelqwe.AutoPluginProcessor;

import java.io.*;

public class InMemoryFile {

    private String fileName = null;
    private ByteArrayOutputStream out = new ByteArrayOutputStream(1024);


    public InMemoryFile() {

    }

    public InMemoryFile(String fileName) {
        this.fileName = fileName;
    }


    public void clear() {
        out.reset();
    }

    public String getFileName() {
        return fileName;
    }


    public void write(byte[] data) throws IOException {
        out.write(data);
    }

    public void write(String data) throws IOException {
        write(data.getBytes());
    }

    public byte[] read() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        byte[] data = new byte[in.available()];
        in.read(data);
        return data;
    }

    public String readAsString() throws IOException {
        return new String(read());
    }

}
