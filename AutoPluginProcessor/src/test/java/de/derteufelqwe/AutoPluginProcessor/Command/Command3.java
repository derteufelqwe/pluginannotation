package de.derteufelqwe.AutoPluginProcessor.Command;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCTabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@MCTabComplete("cmd3")
@MCCommand(command = "cmd3")
public class Command3 implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
