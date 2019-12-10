package de.derteufelqwe.AutoPluginTest;

import de.derteufelqwe.AutoPlugin.AutoRegister;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCLoad;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.plugin.java.JavaPlugin;

@MCLoad(MCLoad.Values.STARTUP)
@MCPlugin(name = "AutoPluginTestPlugin", version = "1.14")
public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {

        new AutoRegister().register(this);
    }
}
