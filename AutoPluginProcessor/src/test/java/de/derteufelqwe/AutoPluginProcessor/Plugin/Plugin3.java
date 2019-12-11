package de.derteufelqwe.AutoPluginProcessor.Plugin;

import de.derteufelqwe.AutoPluginProcessor.annotations.*;
import org.bukkit.plugin.java.JavaPlugin;

@MCDepend("Plugin1")
@MCSoftDepend("Plugin2")
@MCLoad(MCLoad.Values.STARTUP)
@MCLoadBefore({"Plugin1", "Plugin2"})
@MCAuthor("Single Author")
@MCPlugin(name = "Plugin3", version = "Aktuelle Version", prefix = "Prefix", srcPath = "src/test/java/", resourcePath = "src/test/resources/")
public class Plugin3 extends JavaPlugin {
}
