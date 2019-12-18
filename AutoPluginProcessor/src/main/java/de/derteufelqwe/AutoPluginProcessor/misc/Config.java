package de.derteufelqwe.AutoPluginProcessor.misc;

import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;

public class Config {

    public static final String CONFIG_FILE_NAME = "plugin.yml";
    public static final JavaFileManager.Location CONFIG_LOCATION = StandardLocation.CLASS_OUTPUT;

    public static final String CACHE_FILE_NAME = "cache.json";
    public static final JavaFileManager.Location CACHE_LOCATION = StandardLocation.SOURCE_OUTPUT;

}
