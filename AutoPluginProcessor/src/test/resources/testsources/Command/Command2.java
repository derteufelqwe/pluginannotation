package de.derteufelqwe.AutoPluginProcessor;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@MCCommand(command = "Command2", description = "Description1", permission = "permission1", permissionMessage = "permission-message", usage = "/Command2")
public class Command2 implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}