package de.derteufelqwe.AutoPluginTest;

import de.derteufelqwe.AutoPluginProcessor.annotations.MCListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@MCListener
public class Listener1 implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GOLD + event.getPlayer().getDisplayName() + " ist dem Server beigetreten.");
    }

}
