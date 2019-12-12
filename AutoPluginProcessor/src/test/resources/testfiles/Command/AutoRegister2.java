package de.derteufelqwe.AutoPlugin;

import de.derteufelqwe.AutoPluginProcessor.Command2;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRegister {
    public void register(JavaPlugin plugin) {
        // Commands and TabCompleters
        plugin.getServer().getPluginCommand("Command2").setExecutor(new Command2());
        // Listeners
    }
}
