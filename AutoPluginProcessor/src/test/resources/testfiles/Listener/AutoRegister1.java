package de.derteufelqwe.AutoPlugin;

import de.derteufelqwe.AutoPluginProcessor.Listener1;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRegister {
    public void register(JavaPlugin plugin) {
        // Commands and TabCompleters
        // Listeners
        plugin.getServer().getPluginManager().registerEvents(new Listener1(), plugin);
    }
}
