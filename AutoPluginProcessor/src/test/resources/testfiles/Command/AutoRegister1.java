package de.derteufelqwe.AutoPlugin;

import de.derteufelqwe.AutoPluginProcessor.Command1;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRegister {
    public void register(JavaPlugin plugin) {
        // Commands and TabCompleters
        plugin.getServer().getPluginCommand("Command1").setExecutor(new Command1());
        // Listeners
    }
}