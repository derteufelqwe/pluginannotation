package de.derteufelqwe.AutoPluginTest;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCTabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

@MCTabComplete("cmd2")
public class TabCompleter2 implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Arrays.asList("Comp1");
    }
}
