package de.derteufelqwe.AutoPluginProcessor;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCTabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

@MCTabComplete("TabComplete1")
public class TabComplete1 implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
