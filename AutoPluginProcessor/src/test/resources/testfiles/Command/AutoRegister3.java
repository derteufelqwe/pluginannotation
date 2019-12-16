package de.derteufelqwe.AutoPlugin;

import de.derteufelqwe.AutoPluginProcessor.TabComplete1;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRegister {
    public void register(JavaPlugin plugin) {
        // Commands and TabCompleters
        plugin.getServer().getPluginCommand("TabComplete1").setTabCompleter(new TabComplete1());
        // Listeners
    }
}