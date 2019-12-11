package de.derteufelqwe.AutoPluginProcessor.Command;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@MCCommand(command = "cmd1")
public class Command1 implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
