package de.derteufelqwe.AutoPluginTest;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@MCCommand(command = "cmd2")
public class Command2 implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Command 2!");

        return true;
    }
}
