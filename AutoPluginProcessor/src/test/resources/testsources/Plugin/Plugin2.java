package de.derteufelqwe.AutoPluginProcessor.Plugin;

import de.derteufelqwe.AutoPluginProcessor.annotations.*;
import org.bukkit.plugin.java.JavaPlugin;


@MCSoftDepend({"SoftDepend1", "SoftDepend2"})
@MCLoadBefore({"LoadBefore1", "LoadBefore2"})
@MCLoad(MCLoad.Values.STARTUP)
@MCDepend({"Dependency1", "Dependency2"})
@MCAuthor({"Author1", "Author2"})
@MCAPIVersion("1.12")
@MCPlugin(name = "Plugin2", version = "1.2.3", description = "Description2", prefix = "Prefix2")
public class Plugin2 extends JavaPlugin {

}
