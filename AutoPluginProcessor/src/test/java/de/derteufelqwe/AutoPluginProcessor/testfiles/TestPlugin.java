package de.derteufelqwe.AutoPluginProcessor.testfiles;


import de.derteufelqwe.AutoPluginProcessor.annotations.MCAPIVersion;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCAuthor;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCDepend;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.plugin.java.JavaPlugin;


@MCAPIVersion("1.14")
@MCDepend("alles")
@MCAuthor({"Arne", "Nicht Arne"})
@MCPlugin(pluginName = "TestPlugin", version = "1.14", srcPath = "src/test/java/", resourcePath = "src/test/resources/")
public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

    }
}
