package de.derteufelqwe.AutoPlugin;

import de.derteufelqwe.AutoPluginProcessor.Command5;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRegister {
    public void register(JavaPlugin plugin) {
        // Commands and TabCompleters
        Command5 de_derteufelqwe_AutoPluginProcessor_Command5 = new Command5();
        plugin.getServer().getPluginCommand("Command5").setExecutor(de_derteufelqwe_AutoPluginProcessor_Command5);
        plugin.getServer().getPluginCommand("Command5").setTabCompleter(de_derteufelqwe_AutoPluginProcessor_Command5);
        // Listeners
    }
}
