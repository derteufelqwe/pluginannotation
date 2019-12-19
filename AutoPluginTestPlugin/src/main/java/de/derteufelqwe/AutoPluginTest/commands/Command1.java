package de.derteufelqwe.AutoPluginTest.commands;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCDontIgnore;
import de.derteufelqwe.AutoPluginProcessor.annotations.MCTabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;


@MCTabComplete("cmd1")
@MCCommand(command = "cmd1", description = "Command 2")
@MCDontIgnore
public class Command1 implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Testcommand1!");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Arrays.asList("Nichts", "NochMehrNichts");
    }

}
