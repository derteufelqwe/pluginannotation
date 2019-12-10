package de.derteufelqwe.AutoPluginProcessor.Plugins;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;


@MCPlugin(name = "Plugin1", version = "1.2.3", srcPath = "src/test/java/", resourcePath = "src/test/resources/")
public class Plugin1 extends JavaPlugin implements CommandExecutor {

}
