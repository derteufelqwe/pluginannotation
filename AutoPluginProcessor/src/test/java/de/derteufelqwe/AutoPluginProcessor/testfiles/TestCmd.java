package de.derteufelqwe.AutoPluginProcessor.testfiles;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;


@MCCommand(command = "help")
public class TestCmd extends Aaaal implements TabCompleter{



    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
