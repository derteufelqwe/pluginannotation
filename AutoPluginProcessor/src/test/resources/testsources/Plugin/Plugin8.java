package de.derteufelqwe.AutoPluginProcessor.Plugin;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCAPIVersion;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCAuthor;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCDepend;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.plugin.java.JavaPlugin;


@MCDepend("Dependency 1")
@MCPlugin(name = "Plugin8", version = "1.2.3")
public class Plugin8 extends JavaPlugin {

}
