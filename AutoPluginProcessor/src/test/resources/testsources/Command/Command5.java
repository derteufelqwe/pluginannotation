package de.derteufelqwe.AutoPluginProcessor;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCTabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@MCTabComplete("Command5")
@MCCommand(command = "Command5")
public class Command5 implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}