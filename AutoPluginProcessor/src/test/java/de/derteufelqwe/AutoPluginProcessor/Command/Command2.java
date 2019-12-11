package de.derteufelqwe.AutoPluginProcessor.Command;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@MCCommand(command = "cmd2", description = "Ich bin ein command", permission = "cmd2.use", permissionMessage = "No permission", usage = "/cmd2")
public class Command2 implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
