package de.derteufelqwe.AutoPluginProcessor.Plugin;

import de.derteufelqwe.AutoPluginProcessor.annotations.*;
import org.bukkit.plugin.java.JavaPlugin;


@MCLoadBefore("%%Dependency 1")
@MCPlugin(name = "Plugin11", version = "1.2.3", srcPath = "src/test/java/", resourcePath = "src/test/resources/")
public class Plugin11 extends JavaPlugin {

}
