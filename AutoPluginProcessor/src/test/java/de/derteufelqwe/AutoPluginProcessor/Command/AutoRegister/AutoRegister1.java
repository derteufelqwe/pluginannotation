package de.derteufelqwe.AutoPlugin;

import de.derteufelqwe.AutoPluginProcessor.Command.Command3;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRegister1 {
    public void register(JavaPlugin plugin) {
        // Commands and TabCompleters
        Command3 de_derteufelqwe_AutoPluginProcessor_Command_Command3 = new Command3();
        plugin.getServer().getPluginCommand("cmd3").setExecutor(de_derteufelqwe_AutoPluginProcessor_Command_Command3);
        plugin.getServer().getPluginCommand("cmd3").setTabCompleter(de_derteufelqwe_AutoPluginProcessor_Command_Command3);
        // Listeners
    }
}
