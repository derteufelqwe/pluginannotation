package de.derteufelqwe.AutoPluginTest;

import de.derteufelqwe.AutoPlugin.AutoRegister;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCLoadBefore;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCPlugin;
import org.bukkit.plugin.java.JavaPlugin;


@MCLoadBefore("Dependency3")
@MCPlugin(name = "TestPlugin", version = "1.0", description = "This is a Testplugin")
public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {


        new AutoRegister().register(this);
    }
}
