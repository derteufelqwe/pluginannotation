package de.derteufelqwe.AutoPluginTest;

import de.derteufelqwe.AutoPlugin.CommandGenerator;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.plugin.java.JavaPlugin;

@MCPlugin(pluginName = "AutoPluginTestPlugin", version = "1.14")
public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {

        new CommandGenerator().generate(getServer());
    }
}
