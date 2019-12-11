package de.derteufelqwe.AutoPluginProcessor.Plugin;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCAPIVersion;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCAuthor;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.plugin.java.JavaPlugin;

@MCAuthor(value = {"Author1", "Author2"}, website = "www.example.com")
@MCAPIVersion("1.14")
@MCPlugin(name = "Plugin2", version = "1.2", description = "Test-Description", prefix = "Test-Prefix", srcPath = "src/test/java/", resourcePath = "src/test/resources/")
public class Plugin2 extends JavaPlugin {
}
